package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.MCCode;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.annotations.PropertySetter;
import net.darmo_creations.mccode.interpreter.builtin_functions.*;
import net.darmo_creations.mccode.interpreter.exceptions.*;
import net.darmo_creations.mccode.interpreter.parser.ProgramParser;
import net.darmo_creations.mccode.interpreter.type_wrappers.*;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A class that lets users load/unload programs.
 * <p>
 * The state of program managers can be saved and restored.
 */
public class ProgramManager implements NBTDeserializable {
  private static final Map<String, Type<?>> TYPES = new HashMap<>();
  private static final Map<Class<?>, Type<?>> WRAPPED_TYPES = new HashMap<>();
  private static final Map<String, BuiltinFunction> FUNCTIONS = new HashMap<>();
  private static boolean initialized;

  public static final String PROGRAMS_KEY = "Programs";
  public static final String PROGRAM_KEY = "Program";
  public static final String SCHEDULE_KEY = "ScheduleDelay";
  public static final String REPEAT_AMOUNT_KEY = "RepeatAmount";
  public static final String RUNNING_KEY = "Running";

  private final File dataDir;
  private final File programsDir;
  private final World world;
  private final Map<String, Program> programs;
  private final Map<String, Long> programsSchedules;
  private final Map<String, Long> programsRepeats;
  private final Map<String, Boolean> runningPrograms;
  private long lastTick;

  /**
   * Create a program manager for the given world.
   *
   * @param world The world to attach.
   */
  public ProgramManager(World world) {
    this.programs = new HashMap<>();
    this.programsSchedules = new HashMap<>();
    this.programsRepeats = new HashMap<>();
    this.runningPrograms = new HashMap<>();
    this.lastTick = -1;
    this.world = world;
    this.dataDir = new File(this.world.getSaveHandler().getWorldDirectory().getAbsolutePath(), "data");
    this.programsDir = new File(this.dataDir, "mccode_programs");
    this.loadFromFile();
  }

  /**
   * Return the dimension-specific save file path.
   */
  private File getSaveFile() {
    return new File(
        this.dataDir,
        String.format("%s_program_manager_%d.dat", MCCode.MOD_ID, this.world.provider.getDimension())
    );
  }

  /**
   * Load programs from disk.
   */
  private void loadFromFile() {
    File file = this.getSaveFile();
    if (file.exists()) {
      try (FileInputStream fileinputstream = new FileInputStream(file)) {
        this.readFromNBT(CompressedStreamTools.readCompressed(fileinputstream));
      } catch (Exception e) {
        e.printStackTrace();
        if (this.world.getGameRules().getBoolean(MCCode.GR_SHOW_ERROR_MESSAGES)) {
          //noinspection ConstantConditions
          this.world.getMinecraftServer().sendMessage(
              new TextComponentTranslation("mccode.interpreter.error.exception",
                  e.getClass().getSimpleName(), e.getMessage()).setStyle(new Style().setColor(TextFormatting.RED))
          );
        }
      }
    }
  }

  /**
   * Save this manager’s programs to disk.
   */
  public void save() {
    try (FileOutputStream fileoutputstream = new FileOutputStream(this.getSaveFile())) {
      CompressedStreamTools.writeCompressed(this.writeToNBT(), fileoutputstream);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Return the world associated to this manager.
   */
  public World getWorld() {
    return this.world;
  }

  /**
   * Execute all loaded programs. If a program raises an error,
   * it is automatically unloaded and the error is returned.
   *
   * @return A list of program errors that have occured.
   */
  public List<ProgramErrorReport> executePrograms() {
    long currentTick = this.world.getTotalWorldTime();
    // Tick event still fired when unloading world but actual tick value stays the same
    // -> Ignore them
    if (currentTick == this.lastTick) {
      return Collections.emptyList();
    }
    this.lastTick = currentTick;
    List<ProgramErrorReport> errorReports = new ArrayList<>();

    // Execute all programs
    List<Program> toRemove = new LinkedList<>();
    for (Program program : this.programs.values()) {
      if (this.runningPrograms.get(program.getName())) {
        boolean error = true;
        try {
          program.execute();
          error = false; // Not executed if an error is thrown by execute().
        } catch (ProgramFileNotFoundException e) {
          errorReports.add(new ProgramErrorReport(
              program.getScope(), -1, -1, e.getTranslationKey(), e.getProgramName()));
        } catch (SyntaxErrorException e) {
          errorReports.add(new ProgramErrorReport(
              program.getScope(), e.getLine(), e.getColumn(), e.getTranslationKey(), e.getArgs()));
        } catch (MCCodeRuntimeException e) {
          errorReports.add(new ProgramErrorReport(
              e.getScope(), e.getLine(), e.getColumn(), e.getTranslationKey(), e.getArgs()));
        } catch (WrappedException e) {
          errorReports.add(new ProgramErrorReport(
              program.getScope(), e.getLine(), e.getColumn(), e.getTranslationKey(), e.getArgs()));
        }
        // Unload programs that have terminated or failed
        if (error || program.hasTerminated() && (!this.programsSchedules.containsKey(program.getName()) || this.programsRepeats.get(program.getName()) == 0)) {
          toRemove.add(program);
        }
      }
    }
    toRemove.forEach(p -> this.unloadProgram(p.getName()));

    // Update schedules and repeats of terminated programs
    for (Map.Entry<String, Long> e : this.programsSchedules.entrySet()) {
      String programName = e.getKey();
      long delay = e.getValue();
      Program program = this.programs.get(programName);

      if (program.hasTerminated()) {
        if (delay <= 0) {
          long repeatAmount = this.programsRepeats.get(programName);
          if (repeatAmount != Long.MAX_VALUE) {
            this.programsRepeats.put(programName, repeatAmount - 1);
          }
          //noinspection OptionalGetWithoutIsPresent
          this.programsSchedules.put(programName, program.getScheduleDelay().get());
          program.reset();
        } else {
          this.programsSchedules.put(programName, delay - 1);
        }
      }
    }

    return errorReports;
  }

  /**
   * Load a program.
   *
   * @param name     Program’s name.
   * @param alias    An optional alias to use in registries instead of first argument.
   *                 Useful when loading same program multiple times.
   * @param asModule If true, the program instance is returned instead of being loaded in this manager.
   * @param args     Optional command arguments for the program. Ignored if the program is loaded as a module.
   * @return The program.
   * @throws SyntaxErrorException         If a syntax error is present in the program’s source file.
   * @throws ProgramFileNotFoundException If no .mccode file was found for the given name.
   */
  public Program loadProgram(final String name, final String alias, final boolean asModule, Object... args)
      throws SyntaxErrorException, ProgramFileNotFoundException {
    String actualName = alias != null ? alias : name;
    if (!asModule && this.programs.containsKey(actualName)) {
      throw new ProgramAlreadyLoadedException(actualName);
    }
    String[] splitPath = name.split("\\.");
    splitPath[splitPath.length - 1] += ".mccode";
    File programFile = Paths.get(this.programsDir.getAbsolutePath(), splitPath).toFile();
    if (!programFile.exists()) {
      throw new ProgramFileNotFoundException(programFile.getName());
    }
    StringBuilder code = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(programFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        code.append(line).append('\n');
      }
    } catch (IOException e) {
      throw new ProgramFileNotFoundException(programFile.getName());
    }

    Program program = ProgramParser.parse(this, actualName, code.toString(), asModule, args);
    if (!asModule) {
      this.loadProgram(program);
    }
    return program;
  }

  /**
   * Load a program.
   *
   * @param program The program to load.
   * @apiNote Package access level for tests.
   */
  void loadProgram(Program program) {
    String name = program.getName();
    if (this.programs.containsKey(name)) {
      throw new ProgramAlreadyLoadedException(name);
    }
    this.programs.put(name, program);
    program.getScheduleDelay().ifPresent(t -> {
      this.programsSchedules.put(program.getName(), t);
      this.programsRepeats.put(program.getName(), program.getRepeatAmount().orElse(1L));
    });
    this.runningPrograms.put(name, false);
  }

  /**
   * Unload the given program.
   *
   * @param name Program’s name.
   * @throws ProgramNotFoundException If no program with this name is loaded.
   */
  public void unloadProgram(final String name) throws ProgramNotFoundException {
    if (!this.programs.containsKey(name)) {
      throw new ProgramNotFoundException(name);
    }
    this.programs.remove(name);
    this.programsSchedules.remove(name);
    this.programsRepeats.remove(name);
    this.runningPrograms.remove(name);
  }

  /**
   * Reset the given program.
   *
   * @param name Program’s name.
   * @throws ProgramNotFoundException If no program with this name is loaded.
   */
  public void resetProgram(final String name) throws ProgramNotFoundException {
    if (!this.programs.containsKey(name)) {
      throw new ProgramNotFoundException(name);
    }
    this.programs.get(name).reset();
    this.runningPrograms.put(name, false);
  }

  /**
   * Run the given program.
   *
   * @param name Program’s name.
   * @throws ProgramNotFoundException       If no program with this name is loaded.
   * @throws ProgramAlreadyRunningException If the program is already running.
   */
  public void runProgram(final String name) throws ProgramNotFoundException, ProgramAlreadyRunningException {
    if (!this.programs.containsKey(name)) {
      throw new ProgramNotFoundException(name);
    }
    if (this.runningPrograms.get(name)) {
      throw new ProgramAlreadyRunningException(name);
    }
    this.runningPrograms.put(name, true);
  }

  /**
   * Pause the given program.
   *
   * @param name Program’s name.
   * @throws ProgramNotFoundException      If no program with this name is loaded.
   * @throws ProgramAlreadyPausedException If the program is already paused.
   */
  public void pauseProgram(final String name) throws ProgramNotFoundException, ProgramAlreadyPausedException {
    if (!this.programs.containsKey(name)) {
      throw new ProgramNotFoundException(name);
    }
    if (!this.runningPrograms.get(name)) {
      throw new ProgramAlreadyPausedException(name);
    }
    this.runningPrograms.put(name, false);
  }

  /**
   * Return the names of all loaded programs sorted alphabetically.
   */
  public List<String> getLoadedPrograms() {
    List<String> list = new ArrayList<>(this.programs.keySet());
    list.sort(Comparator.comparing(String::toLowerCase));
    return list;
  }

  /**
   * Return the program with the given name.
   *
   * @param name Program’s name.
   * @return The program.
   */
  public Optional<Program> getProgram(final String name) {
    return Optional.ofNullable(this.programs.get(name));
  }

  /**
   * Return the directory containing program files.
   */
  public File getProgramsDirectory() {
    return this.programsDir;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList programs = new NBTTagList();
    for (Program p : this.programs.values()) {
      NBTTagCompound programTag = new NBTTagCompound();
      programTag.setTag(PROGRAM_KEY, p.writeToNBT());
      String programName = p.getName();
      if (this.programsSchedules.containsKey(programName)) {
        programTag.setLong(SCHEDULE_KEY, this.programsSchedules.get(programName));
        programTag.setLong(REPEAT_AMOUNT_KEY, this.programsRepeats.get(programName));
      }
      programTag.setBoolean(RUNNING_KEY, this.runningPrograms.get(programName));
      programs.appendTag(programTag);
    }
    tag.setTag(PROGRAMS_KEY, programs);
    return tag;
  }

  @Override
  public void readFromNBT(final NBTTagCompound tag) {
    NBTTagList list = tag.getTagList(PROGRAMS_KEY, new NBTTagCompound().getId());
    this.programs.clear();
    this.programsSchedules.clear();
    this.programsRepeats.clear();
    this.runningPrograms.clear();
    for (NBTBase t : list) {
      NBTTagCompound programTag = (NBTTagCompound) t;
      try {
        Program program = new Program(programTag.getCompoundTag(PROGRAM_KEY), this);
        this.programs.put(program.getName(), program);
        if (programTag.hasKey(SCHEDULE_KEY)) {
          this.programsSchedules.put(program.getName(), programTag.getLong(SCHEDULE_KEY));
          if (programTag.hasKey(REPEAT_AMOUNT_KEY)) {
            this.programsRepeats.put(program.getName(), programTag.getLong(REPEAT_AMOUNT_KEY));
          }
        }
        this.runningPrograms.put(program.getName(), programTag.getBoolean(RUNNING_KEY));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /*
   * Static methods
   */

  /**
   * Finish setup of this interpreter.
   * Call only after all types have been declared.
   */
  public static void initialize() {
    processTypeAnnotations();
    processFunctionsAnnotations();
    initialized = true;
  }

  /**
   * Return all declared types.
   */
  public static List<Type<?>> getTypes() {
    return new ArrayList<>(TYPES.values());
  }

  /**
   * Return the {@link Type} instance for the given class.
   *
   * @param typeClass Type’s class.
   * @param <T>       Type’s wrapped type.
   * @return The type class’ instance.
   */
  public static <T extends Type<?>> T getTypeInstance(final Class<T> typeClass) {
    //noinspection unchecked
    return (T) TYPES.values().stream().filter(t -> t.getClass() == typeClass).findFirst().orElse(null);
  }

  /**
   * Return the {@link Type} instance for the given type name.
   *
   * @param name Type’s name.
   * @param <T>  Type’s wrapped type.
   * @return The type instance.
   */
  public static <T extends Type<?>> T getTypeForName(final String name) {
    //noinspection unchecked
    return (T) TYPES.get(name);
  }

  /**
   * Return the {@link Type} instance for the given wrapped class.
   *
   * @param wrappedClass Wrapped type’s class.
   * @param <T>          Type’s wrapped type.
   * @param <U>          Instance’s type.
   * @return The type instance.
   */
  public static <T, U extends Type<T>> U getTypeForWrappedClass(final Class<T> wrappedClass) {
    //noinspection unchecked
    return (U) WRAPPED_TYPES.entrySet().stream()
        .min(Comparator.comparing(e -> classDistance(e.getKey(), wrappedClass)))
        .map(Map.Entry::getValue)
        .orElse(null);
  }

  /**
   * Return the class hierarchy distance between the two given classes.
   *
   * @param key          A superclass of the second argument.
   * @param wrappedClass The class to get the distance to the first one.
   * @return The distance between the two classes.
   */
  private static int classDistance(final Class<?> key, final Class<?> wrappedClass) {
    if (!key.isAssignableFrom(wrappedClass)) {
      return Integer.MAX_VALUE;
    }
    Class<?> c = wrappedClass;
    int distance = 0;
    while (c != key) {
      c = c.getSuperclass();
      distance++;
    }
    return distance;
  }

  /**
   * Return the {@link Type} instance for the given object.
   *
   * @param o Object to get the type of.
   * @return The type instance.
   */
  public static Type<?> getTypeForValue(final Object o) {
    return o != null ? getTypeForWrappedClass(o.getClass()) : getTypeInstance(NullType.class);
  }

  /**
   * Declare a new wrapper type.
   *
   * @param typeClass Wrapper type’s class.
   * @throws TypeException If any error occurs related to {@link Property} or
   *                       {@link net.darmo_creations.mccode.interpreter.annotations.Method} annotations.
   */
  public static void declareType(final Class<? extends Type<?>> typeClass) throws TypeException {
    ensureNotInitialized();

    Type<?> type;
    try {
      type = typeClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new TypeException("missing empty constructor for class %s" + typeClass);
    }
    String name = type.getName();
    Class<?> wrappedType = type.getWrappedType();

    if (TYPES.containsKey(name)) {
      throw new TypeException(String.format("a type with the name \"%s\" already exists", name));
    }
    if (TYPES.values().stream().anyMatch(t -> t.getClass() == type.getClass())) {
      throw new TypeException(String.format("cannot redeclare type \"%s\"", name));
    }
    if (WRAPPED_TYPES.containsKey(wrappedType)) {
      throw new TypeException("a wrapper class is already declared for class %s" + wrappedType);
    }

    TYPES.put(name, type);
    WRAPPED_TYPES.put(wrappedType, type);
  }

  /**
   * Declare all default builtin types.
   */
  public static void declareDefaultBuiltinTypes() {
    declareType(AnyType.class);
    declareType(NullType.class);
    declareType(BooleanType.class);
    declareType(IntType.class);
    declareType(FloatType.class);
    declareType(StringType.class);
    declareType(ResourceLocationType.class);
    declareType(PosType.class);
    declareType(ListType.class);
    declareType(SetType.class);
    declareType(MapType.class);
    declareType(BlockType.class);
    declareType(ItemType.class);
    declareType(WorldType.class);
    declareType(FunctionType.class);
    declareType(RangeType.class);
    declareType(ModuleType.class);
  }

  /**
   * Process annotations of all declared types.
   */
  private static void processTypeAnnotations() {
    for (Type<?> type : TYPES.values()) {
      setTypeProperties(type);
      setTypeMethods(type);
      setTypeDoc(type);
    }
  }

  /**
   * Set private "doc" field of the given type.
   */
  private static void setTypeDoc(Type<?> type) {
    //noinspection unchecked
    Class<? extends Type<?>> typeClass = (Class<? extends Type<?>>) type.getClass();

    if (typeClass.isAnnotationPresent(Doc.class)) {
      Doc docAnnotation = typeClass.getAnnotation(Doc.class);
      String doc = docAnnotation.value();

      // Retrieve Type class
      Class<?> c = typeClass;
      while (c != Type.class) {
        c = c.getSuperclass();
      }

      try {
        Field nameField = c.getDeclaredField("doc");
        nameField.setAccessible(true);
        nameField.set(type, doc);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new TypeException("missing field 'doc' for class " + c);
      }
    }
  }

  /**
   * Set private "properties" field of the given type.
   */
  private static void setTypeProperties(Type<?> type) {
    //noinspection unchecked
    Class<? extends Type<?>> typeClass = (Class<? extends Type<?>>) type.getClass();
    String typeName = type.getName();
    Map<String, ObjectProperty> properties = new HashMap<>();
    Map<String, Method> getterMethods = new HashMap<>();
    Map<String, Method> setterMethods = new HashMap<>();

    for (Method method : typeClass.getMethods()) {
      String methodName = method.getName();
      boolean isGetter = false;

      if (method.isAnnotationPresent(Property.class)) {
        Property propertyAnnotation = method.getAnnotation(Property.class);
        String propertyName = propertyAnnotation.name();
        Class<?>[] parameterTypes = method.getParameterTypes();

        if (getterMethods.containsKey(propertyName)) {
          throw new MCCodeException(String.format("property %s already defined for type %s", propertyName, typeClass));
        }
        if (parameterTypes.length != 1) {
          throw new MCCodeException(String.format("invalid number of arguments for property %s.%s: expected 1, got %s",
              typeClass, methodName, parameterTypes.length));
        }
        if (!parameterTypes[0].isAssignableFrom(type.getWrappedType())) {
          throw new TypeException(String.format("method argument type does not match wrapped type in %s.%s",
              typeClass, methodName));
        }

        getterMethods.put(propertyName, method);
        isGetter = true;
      }

      if (method.isAnnotationPresent(PropertySetter.class)) {
        PropertySetter propertyAnnotation = method.getAnnotation(PropertySetter.class);
        String targetPropertyName = propertyAnnotation.forProperty();

        if (isGetter) {
          throw new TypeException(String.format("annotations Property and PropertySetter both present on method %s.%s", typeClass, methodName));
        }

        setterMethods.put(targetPropertyName, method);
      }
    }

    // Check that every setter has a corresponding getter
    for (Map.Entry<String, Method> entry : setterMethods.entrySet()) {
      if (!getterMethods.containsKey(entry.getKey())) {
        throw new TypeException(String.format("no getter method for setter method %s.%s", typeClass, entry.getValue().getName()));
      }
    }

    for (Map.Entry<String, Method> entry : getterMethods.entrySet()) {
      String propertyName = entry.getKey();
      Method getterMethod = entry.getValue();

      Type<?> returnType = getTypeForWrappedClass(getterMethod.getReturnType());
      if (returnType == null) {
        throw new TypeException(String.format("method return type does not match declared Property annotation type: %s.%s",
            typeName, propertyName));
      }

      String doc = "Type: " + returnType.getName();
      if (getterMethod.isAnnotationPresent(Doc.class)) {
        doc = getterMethod.getAnnotation(Doc.class).value() + "\n" + doc;
      }

      ObjectProperty property = new ObjectProperty(type, propertyName, returnType, getterMethod, setterMethods.get(propertyName), doc);
      properties.put(property.getName(), property);
    }

    // Retrieve Type class to get properties field
    Class<?> c = typeClass;
    while (c != Type.class) {
      c = c.getSuperclass();
    }

    try {
      Field nameField = c.getDeclaredField("properties");
      nameField.setAccessible(true);
      nameField.set(type, properties);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new TypeException("missing field 'properties' for class " + c);
    }
  }

  /**
   * Set private "methods" field of the given type.
   */
  private static void setTypeMethods(Type<?> type) {
    //noinspection unchecked
    Class<? extends Type<?>> typeClass = (Class<? extends Type<?>>) type.getClass();
    String typeName = type.getName();
    Class<?> wrappedType = type.getWrappedType();
    Map<String, MemberFunction> methods = new HashMap<>();

    for (Method method : typeClass.getMethods()) {
      if (method.isAnnotationPresent(net.darmo_creations.mccode.interpreter.annotations.Method.class)) {
        net.darmo_creations.mccode.interpreter.annotations.Method methodAnnotation =
            method.getAnnotation(net.darmo_creations.mccode.interpreter.annotations.Method.class);
        String methodName = methodAnnotation.name();
        Class<?>[] parameterTypes = method.getParameterTypes();
        java.lang.reflect.Parameter[] parameters = method.getParameters();

        if (methods.containsKey(methodName)) {
          throw new MCCodeException(String.format("method %s already defined for type %s",
              methodName, typeName));
        }

        if (parameterTypes.length < 2) {
          throw new MCCodeException(String.format("not enough arguments for method %s.%s: expected at least 2, got %s",
              typeName, methodName, parameterTypes.length));
        }
        if (parameterTypes[0] != Scope.class) {
          throw new TypeException("first argument of method must be Scope object");
        }
        if (parameterTypes[1] != wrappedType) {
          throw new TypeException(String.format("second argument of method must be of same type as wrapped type: expected %s, got %s",
              wrappedType, parameterTypes[1]));
        }

        List<? extends Type<?>> paramsTypes = Arrays.stream(parameterTypes)
            .skip(2) // Skip scope and instance arguments
            .map(c -> (Type<?>) getTypeForWrappedClass(c))
            .collect(Collectors.toList());
        if (paramsTypes.stream().anyMatch(Objects::isNull)) {
          throw new TypeException(String.format("method argument type does not match any declared type: %s in %s.%s",
              paramsTypes, typeName, methodName));
        }

        Type<?> returnType = getTypeForWrappedClass(method.getReturnType());
        if (returnType == null) {
          throw new TypeException(String.format("method return type does not match any declared type: %s in %s.%s",
              method.getReturnType(), typeName, methodName));
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paramsTypes.size(); i++) {
          if (i > 0) {
            sb.append(", ");
          }
          String paramName = parameters[i + 2].getName();
          // Convert name to snake_case
          paramName = Utils.replaceAll(paramName, Pattern.compile("([A-Z])"), m -> "_" + m.group().toLowerCase());
          sb.append(paramsTypes.get(i).getName()).append(' ').append(paramName);
        }
        String doc = "§nSignature§r: " + String.format("%s.%s(%s) -> %s", typeName, methodName, sb, returnType.getName());
        if (method.isAnnotationPresent(Doc.class)) {
          doc = method.getAnnotation(Doc.class).value() + "\n" + doc;
        }

        MemberFunction memberFunction = new MemberFunction(type, methodName, paramsTypes, returnType, method, doc);
        methods.put(memberFunction.getName(), memberFunction);
      }
    }

    // Retrieve Type class
    Class<?> c = typeClass;
    while (c != Type.class) {
      c = c.getSuperclass();
    }

    try {
      Field nameField = c.getDeclaredField("methods");
      nameField.setAccessible(true);
      nameField.set(type, methods);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new TypeException("missing field 'methods' for class " + c);
    }
  }

  /**
   * Return all declared builtin functions.
   */
  public static List<BuiltinFunction> getBuiltinFunctions() {
    return new LinkedList<>(FUNCTIONS.values());
  }

  /**
   * Return the function with the given name.
   *
   * @param name Function’s name.
   * @return The function or null if none matched.
   */
  public static BuiltinFunction getBuiltinFunction(final String name) {
    return FUNCTIONS.get(name);
  }

  /**
   * Declare a new builtin function.
   *
   * @param functionClass Function’s class.
   */
  public static void declareBuiltinFunction(Class<? extends BuiltinFunction> functionClass) {
    ensureNotInitialized();

    BuiltinFunction function;
    try {
      function = functionClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new TypeException("missing empty constructor for class %s" + functionClass);
    }
    String name = function.getName();

    if (FUNCTIONS.containsKey(name)) {
      throw new MCCodeException(String.format("a function with the name \"%s\" already exists", name));
    }

    FUNCTIONS.put(name, function);
  }

  /**
   * Declare all default builtin functions.
   */
  public static void declareDefaultBuiltinFunctions() {
    declareBuiltinFunction(AbsFunction.class);
    declareBuiltinFunction(AcosFunction.class);
    declareBuiltinFunction(AsinFunction.class);
    declareBuiltinFunction(AtanFunction.class);
    declareBuiltinFunction(Atan2Function.class);
    declareBuiltinFunction(CbrtFunction.class);
    declareBuiltinFunction(CeilFunction.class);
    declareBuiltinFunction(CosFunction.class);
    declareBuiltinFunction(ErrorFunction.class);
    declareBuiltinFunction(ExpFunction.class);
    declareBuiltinFunction(FloorFunction.class);
    declareBuiltinFunction(HypotFunction.class);
    declareBuiltinFunction(IsInstanceFunction.class);
    declareBuiltinFunction(LenFunction.class);
    declareBuiltinFunction(Log10Function.class);
    declareBuiltinFunction(LogFunction.class);
    declareBuiltinFunction(MaxFunction.class);
    declareBuiltinFunction(MinFunction.class);
    declareBuiltinFunction(PrintFunction.class);
    declareBuiltinFunction(RandomFloatFunction.class);
    declareBuiltinFunction(RandomIntFunction.class);
    declareBuiltinFunction(RangeFunction.class);
    declareBuiltinFunction(ReversedFunction.class);
    declareBuiltinFunction(RoundFunction.class);
    declareBuiltinFunction(SetRandomSeedFunction.class);
    declareBuiltinFunction(SinFunction.class);
    declareBuiltinFunction(SortedFunction.class);
    declareBuiltinFunction(SqrtFunction.class);
    declareBuiltinFunction(TanFunction.class);
    declareBuiltinFunction(ToDegreesFunction.class);
    declareBuiltinFunction(ToRadiansFunction.class);
    declareBuiltinFunction(ToRelativePosFunction.class);

    for (Type<?> type : TYPES.values()) {
      if (type.generateCastOperator()) {
        String name = "to_" + type.getName();
        FUNCTIONS.put(name, new BuiltinFunction(name, type, new Parameter("o", ProgramManager.getTypeInstance(AnyType.class))) {
          @Override
          public Object apply(final Scope scope) {
            return type.explicitCast(scope, this.getParameterValue(scope, 0));
          }
        });
      }
    }
  }

  /**
   * Process annotations of all declared functions.
   */
  private static void processFunctionsAnnotations() {
    for (BuiltinFunction function : FUNCTIONS.values()) {
      setFunctionDoc(function);
    }
  }

  /**
   * Set private "doc" field of the given funtion.
   */
  private static void setFunctionDoc(BuiltinFunction function) {
    Class<? extends BuiltinFunction> functionClass = function.getClass();

    List<Parameter> parameters = function.getParameters();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < parameters.size(); i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(parameters.get(i).getType().getName()).append(' ').append(parameters.get(i).getName());
    }
    String doc = "§nSignature§r: " + String.format("%s(%s) -> %s", function.getName(), sb, function.getReturnType().getName());
    if (functionClass.isAnnotationPresent(Doc.class)) {
      doc = functionClass.getAnnotation(Doc.class).value() + "\n" + doc;
    }

    // Retrieve Type class
    Class<?> c = functionClass;
    while (c != BuiltinFunction.class) {
      c = c.getSuperclass();
    }

    try {
      Field nameField = c.getDeclaredField("doc");
      nameField.setAccessible(true);
      nameField.set(function, doc);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new TypeException("missing field 'doc' for builtin function " + c);
    }
  }

  private static void ensureNotInitialized() {
    if (initialized) {
      throw new MCCodeException("interpreter already initialized");
    }
  }
}
