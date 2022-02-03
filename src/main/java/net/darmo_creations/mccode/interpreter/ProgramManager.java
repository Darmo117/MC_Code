package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.MCCode;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.builtin_functions.*;
import net.darmo_creations.mccode.interpreter.exceptions.*;
import net.darmo_creations.mccode.interpreter.parser.ProgramParser;
import net.darmo_creations.mccode.interpreter.type_wrappers.*;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class that lets users load/unload programs.
 * <p>
 * The state of program managers can be saved and restored.
 */
public class ProgramManager extends WorldSavedData {
  public static final String DATA_NAME = MCCode.MOD_ID + ":program_manager";

  private static final Map<String, Type<?>> TYPES = new HashMap<>();
  private static final Map<Class<?>, Type<?>> WRAPPED_TYPES = new HashMap<>();
  private static final Map<String, BuiltinFunction> FUNCTIONS = new HashMap<>();
  private static boolean initialized;

  private static final String PROGRAMS_KEY = "Programs";
  private static final String PROGRAM_KEY = "Program";
  private static final String SCHEDULE_KEY = "ScheduleDelay";
  private static final String REPEAT_AMOUNT_KEY = "RepeatAmount";
  private static final String RUNNING_KEY = "Running";

  private File programsDir;
  private World world;
  private final Map<String, Program> programs;
  private final Map<String, Integer> programsSchedules;
  private final Map<String, Integer> programsRepeats;
  private final Map<String, Boolean> runningPrograms;

  /**
   * Create a program manager with the given name.
   */
  public ProgramManager(final String name) {
    super(name);
    this.programs = new HashMap<>();
    this.programsSchedules = new HashMap<>();
    this.programsRepeats = new HashMap<>();
    this.runningPrograms = new HashMap<>();
  }

  /**
   * Return the world associated to this manager.
   */
  public World getWorld() {
    return this.world;
  }

  /**
   * Set the world of this manager.
   */
  public void setWorld(World world) {
    this.world = world;
    this.programsDir = new File(new File(this.world.getSaveHandler().getWorldDirectory(), "data"), "mccode_programs");
  }

  /**
   * Execute all loaded programs. If a program raises an error,
   * it is automatically unloaded and the error is returned.
   *
   * @return A list of program errors that have occured.
   */
  public List<ProgramErrorReport> executePrograms() {
    List<ProgramErrorReport> errorReports = new ArrayList<>();

    // Execute all programs
    List<Program> toRemove = new LinkedList<>();
    for (Program program : this.programs.values()) {
      if (this.runningPrograms.get(program.getName())) {
        try {
          program.execute();
        } catch (MCCodeRuntimeException e) {
          errorReports.add(new ProgramErrorReport(e.getScope(), e.getTranslationKey(), e.getArgs()));
          toRemove.add(program);
        } catch (ArithmeticException e) {
          errorReports.add(new ProgramErrorReport(program.getScope(), "mccode.interpreter.error.math_error", e.getMessage()));
          toRemove.add(program);
        }
      }
    }
    toRemove.forEach(p -> this.unloadProgram(p.getName()));

    // Update schedules and repeats of terminated programs
    for (Map.Entry<String, Integer> e : this.programsSchedules.entrySet()) {
      String programName = e.getKey();
      int delay = e.getValue();
      Program program = this.programs.get(programName);

      if (program.hasTerminated()) {
        if (delay <= 0) {
          if (this.programsRepeats.containsKey(programName)) {
            int repeatAmount = this.programsRepeats.get(programName);
            //noinspection OptionalGetWithoutIsPresent
            this.programsSchedules.put(programName, program.getScheduleDelay().get());
            if (repeatAmount > 1) {
              this.programsRepeats.put(programName, repeatAmount - 1);
            } else {
              this.unloadProgram(programName);
            }
          } else {
            this.unloadProgram(programName);
          }
          if (this.programs.containsKey(programName)) {
            program.reset();
          }

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
   * @param name Program’s name.
   * @throws SyntaxErrorException         If a syntax error is present in the program’s source file.
   * @throws ProgramFileNotFoundException If no .mccode file was found for the given name.
   */
  public void loadProgram(final String name)
      throws SyntaxErrorException, ProgramFileNotFoundException {
    if (this.programs.containsKey(name)) {
      throw new ProgramAlreadyLoadedException(name);
    }
    File programFile = new File(this.programsDir, name + ".mccode");
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

    Program program = ProgramParser.parse(this, name, code.toString());
    program.getScheduleDelay().ifPresent(t -> this.programsSchedules.put(program.getName(), t));
    program.getRepeatAmount().ifPresent(t -> this.programsRepeats.put(program.getName(), t));
    this.programs.put(name, program);
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
   * Return the names of all loaded programs.
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

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    NBTTagList programs = new NBTTagList();
    for (Program p : this.programs.values()) {
      NBTTagCompound programTag = new NBTTagCompound();
      programTag.setTag(PROGRAM_KEY, p.writeToNBT());
      String programName = p.getName();
      if (this.programsSchedules.containsKey(programName)) {
        programTag.setInteger(SCHEDULE_KEY, this.programsSchedules.get(programName));
        if (this.programsRepeats.containsKey(programName)) {
          programTag.setInteger(REPEAT_AMOUNT_KEY, this.programsRepeats.get(programName));
        }
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
      Program program = new Program(programTag.getCompoundTag(PROGRAM_KEY), this);
      this.programs.put(program.getName(), program);
      if (programTag.hasKey(SCHEDULE_KEY)) {
        this.programsSchedules.put(program.getName(), programTag.getInteger(SCHEDULE_KEY));
        if (programTag.hasKey(REPEAT_AMOUNT_KEY)) {
          this.programsRepeats.put(program.getName(), programTag.getInteger(REPEAT_AMOUNT_KEY));
        }
      }
      this.runningPrograms.put(program.getName(), programTag.getBoolean(RUNNING_KEY));
    }
  }

  /**
   * Attaches a manager to the global storage through a world instance.
   * If no manager instance is already defined, a new one is created and attached to the storage.
   *
   * @param world The world used to access the global storage.
   * @return The manager instance.
   */
  public static ProgramManager attachToGlobalStorage(World world) {
    MapStorage storage = world.getMapStorage();
    //noinspection ConstantConditions
    ProgramManager instance = (ProgramManager) storage.getOrLoadData(ProgramManager.class, DATA_NAME);

    if (instance == null) {
      instance = new ProgramManager(DATA_NAME);
      storage.setData(DATA_NAME, instance);
    }
    instance.setWorld(world);
    return instance;
  }

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

    for (Method getterMethod : typeClass.getMethods()) {
      if (getterMethod.isAnnotationPresent(Property.class)) {
        // TODO look for setter property
        Property propertyAnnotation = getterMethod.getAnnotation(Property.class);
        String propertyName = propertyAnnotation.name();
        Class<?>[] parameterTypes = getterMethod.getParameterTypes();

        if (properties.containsKey(propertyName)) {
          throw new MCCodeException(String.format("property %s already defined for type %s",
              propertyName, typeName));
        }

        if (parameterTypes.length != 1) {
          throw new MCCodeException(String.format("invalid number of arguments for property %s.%s: expected 1, got %s",
              typeName, propertyName, parameterTypes.length));
        }

        if (parameterTypes[0] != type.getWrappedType()) {
          throw new TypeException(String.format("method argument type does not match any wrapped type in %s.%s",
              typeName, propertyName));
        }

        Type<?> returnType = getTypeForWrappedClass(getterMethod.getReturnType());
        if (returnType == null) {
          throw new TypeException(String.format("method return type does not match declared Property annotation type: %s.%s",
              typeName, propertyName));
        }

        String doc = String.format("%s.%s -> %s", typeName, propertyName, returnType.getName());
        if (getterMethod.isAnnotationPresent(Doc.class)) {
          doc = getterMethod.getAnnotation(Doc.class).value();
        }

        ObjectProperty property = new ObjectProperty(type, propertyName, returnType, getterMethod, null, doc);
        properties.put(property.getName(), property);
      }
    }

    // Retrieve Type class
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

        List<String> paramsTypesNames = paramsTypes.stream().map(Type::getName).collect(Collectors.toList());
        String doc = String.format("%s.%s(%s) -> %s", typeName, methodName, String.join(", ", paramsTypesNames), returnType.getName());
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
    declareBuiltinFunction(ExpFunction.class);
    declareBuiltinFunction(FloorFunction.class);
    declareBuiltinFunction(HypotFunction.class);
    declareBuiltinFunction(LenFunction.class);
    declareBuiltinFunction(Log10Function.class);
    declareBuiltinFunction(LogFunction.class);
    declareBuiltinFunction(MaxFunction.class);
    declareBuiltinFunction(MinFunction.class);
    declareBuiltinFunction(PrintFunction.class);
    declareBuiltinFunction(RangeFunction.class);
    declareBuiltinFunction(ReversedFunction.class);
    declareBuiltinFunction(RoundFunction.class);
    declareBuiltinFunction(SinFunction.class);
    declareBuiltinFunction(SortedFunction.class);
    declareBuiltinFunction(SqrtFunction.class);
    declareBuiltinFunction(TanFunction.class);
    declareBuiltinFunction(ToDegreesFunction.class);
    declareBuiltinFunction(ToRadiansFunction.class);

    for (Type<?> type : TYPES.values()) {
      if (type.generateCastOperator()) {
        String name = "to_" + type.getName();
        FUNCTIONS.put(name, new BuiltinFunction(name, type, ProgramManager.getTypeInstance(AnyType.class)) {
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

    if (functionClass.isAnnotationPresent(Doc.class)) {
      Doc docAnnotation = functionClass.getAnnotation(Doc.class);
      String doc = docAnnotation.value();

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
  }

  private static void ensureNotInitialized() {
    if (initialized) {
      throw new MCCodeException("interpreter already initialized");
    }
  }
}
