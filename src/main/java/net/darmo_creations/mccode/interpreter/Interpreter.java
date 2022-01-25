package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.exceptions.ProgramException;
import net.darmo_creations.mccode.interpreter.exceptions.TypeException;
import net.darmo_creations.mccode.interpreter.type_wrappers.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Interpreter implements NBTDeserializable {
  private final Map<String, Type<?>> types = new HashMap<>();
  private final Map<Class<?>, Type<?>> wrappedTypes = new HashMap<>();

  private static final String PROGRAMS_KEY = "Programs";

  private final World world;
  private boolean initialized;
  private Map<String, Program> programs; // TODO program scheduling

  public Interpreter(World world) {
    this.declareBuiltinTypes();
    this.world = world;
    this.programs = new HashMap<>();
  }

  /**
   * Finish setup of this interpreter.
   * Call only after all types have been declared.
   */
  public void initialize() {
    this.processTypeAnnotations();
    this.initialized = true;
  }

  public World getWorld() {
    return this.world;
  }

  public void executePrograms(final int worldTick) {
    Iterator<Map.Entry<String, Program>> programs = this.programs.entrySet().iterator();
    while (programs.hasNext()) {
      Map.Entry<String, Program> p = programs.next();
      try {
        p.getValue().execute(worldTick);
      } catch (ProgramException e) {
        programs.remove();
        // TODO report exception
      }
    }
  }

  public void loadProgram(final String name) {
    Program program = null; // TODO load from file here
    // TODO throw error if no program with this name exists
    this.programs.put(name, program);
  }

  public void unloadProgram(final String name) {
    this.programs.remove(name);
  }

  public List<String> getLoadedPrograms() {
    List<String> list = new ArrayList<>(this.programs.keySet());
    list.sort(Comparator.comparing(String::toLowerCase));
    return list;
  }

  public <T extends Type<?>> T getTypeInstance(final Class<T> typeClass) {
    //noinspection unchecked
    return (T) this.types.values().stream().filter(t -> t.getClass() == typeClass).findFirst().orElse(null);
  }

  public <T extends Type<?>> T getTypeForName(final String name) {
    //noinspection unchecked
    return (T) this.types.get(name);
  }

  public <T, U extends Type<T>> U getTypeForWrappedClass(final Class<T> wrappedClass) {
    //noinspection unchecked
    return (U) this.wrappedTypes.entrySet().stream()
        .filter(e -> e.getKey().isAssignableFrom(wrappedClass))
        .findFirst()
        .map(Map.Entry::getValue)
        .orElse(null);
  }

  public Type<?> getTypeForValue(final Object o) {
    return o != null ? this.getTypeForWrappedClass(o.getClass()) : this.getTypeInstance(NullType.class);
  }

  public void declareType(final Class<? extends Type<?>> typeClass) throws TypeException {
    this.ensureNotInitialized();

    Type<?> type;
    try {
      type = typeClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new TypeException("missing empty constructor for class %s" + typeClass);
    }
    String name = type.getName();
    Class<?> wrappedType = type.getWrappedType();

    if (this.types.containsKey(name)) {
      throw new TypeException(String.format("a type with the name \"%s\" already exists", name));
    }
    if (this.types.values().stream().anyMatch(t -> t.getClass() == type.getClass())) {
      throw new TypeException(String.format("cannot redeclare type \"%s\"", name));
    }
    if (this.wrappedTypes.containsKey(wrappedType)) {
      throw new TypeException("a wrapper class is already declared for class %s" + wrappedType);
    }

    this.types.put(name, type);
    this.wrappedTypes.put(wrappedType, type);
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList statementsList = new NBTTagList();
    this.programs.values().forEach(p -> statementsList.appendTag(p.writeToNBT()));
    tag.setTag(PROGRAMS_KEY, statementsList);
    return tag;
  }

  @Override
  public void readFromNBT(final NBTTagCompound tag) {
    if (!this.initialized) {
      throw new MCCodeException("interpreter not yet initialized");
    }
    NBTTagList list = tag.getTagList(PROGRAMS_KEY, new NBTTagCompound().getId());
    this.programs = new HashMap<>();
    for (NBTBase t : list) {
      Program program = new Program((NBTTagCompound) t, this);
      this.programs.put(program.getName(), program);
    }
  }

  private void ensureNotInitialized() {
    if (this.initialized) {
      throw new MCCodeException("interpreter already initialized");
    }
  }

  private void declareBuiltinTypes() {
    this.declareType(AnyType.class);
    this.declareType(NullType.class);
    this.declareType(BooleanType.class);
    this.declareType(IntType.class);
    this.declareType(FloatType.class);
    this.declareType(StringType.class);
    this.declareType(ResourceLocationType.class);
    this.declareType(PosType.class);
    this.declareType(ListType.class);
    this.declareType(SetType.class);
    this.declareType(MapType.class);
    this.declareType(BlockType.class);
    this.declareType(ItemType.class);
    this.declareType(WorldType.class);
    this.declareType(FunctionType.class);
  }

  /**
   * Process annotations of all declared types.
   */
  private void processTypeAnnotations() {
    for (Type<?> type : this.types.values()) {
      Map<String, ObjectProperty> properties = this.setTypeProperties(type);
      this.setTypeMethods(type, properties);
      this.setTypeDoc(type);
    }
  }

  // Set private "doc" field
  private void setTypeDoc(Type<?> type) {
    //noinspection unchecked
    Class<? extends Type<?>> typeClass = (Class<? extends Type<?>>) type.getClass();

    if (typeClass.isAnnotationPresent(Doc.class)) {
      Doc docAnnotation = typeClass.getAnnotation(Doc.class);
      String doc = docAnnotation.value();
      try {
        Field nameField = type.getClass().getField("doc");
        nameField.setAccessible(true);
        nameField.set(type, doc);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new TypeException("missing field 'doc' for class " + typeClass);
      }
    }
  }

  // Set private "properties" field
  private Map<String, ObjectProperty> setTypeProperties(Type<?> type) {
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
          throw new MCCodeException(String.format("invalid number of arguments for property %s.%s: expected at 1, got %s",
              typeName, propertyName, parameterTypes.length));
        }

        if (parameterTypes[0] != type.getWrappedType()) {
          throw new TypeException(String.format("method argument type does not match any wrapped type in %s.%s",
              typeName, propertyName));
        }

        Type<?> returnType = this.getTypeForWrappedClass(getterMethod.getReturnType());
        if (returnType == null) {
          throw new TypeException(String.format("method return type does not match declared Property annotation type: %s.%s",
              typeName, propertyName));
        }

        String doc = String.format("%s.%s -> %s", typeName, propertyName, returnType.getName());
        if (getterMethod.isAnnotationPresent(Doc.class)) {
          doc = getterMethod.getAnnotation(Doc.class).value();
        }

        ObjectProperty property = new ObjectProperty(propertyName, returnType, getterMethod, null, doc);
        properties.put(property.getName(), property);
      }
    }

    try {
      Field nameField = type.getClass().getField("properties");
      nameField.setAccessible(true);
      nameField.set(type, properties);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new TypeException("missing field 'properties' for class " + typeClass);
    }
    return properties;
  }

  // Set private "methods" field
  private void setTypeMethods(Type<?> type, final Map<String, ObjectProperty> properties) {
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

        if (properties.containsKey(methodName)) {
          throw new MCCodeException(String.format("a property named %s already exists for type %s",
              methodName, typeName));
        }
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
            .map(c -> (Type<?>) this.getTypeForWrappedClass(c))
            .collect(Collectors.toList());
        if (paramsTypes.stream().anyMatch(Objects::isNull)) {
          throw new TypeException(String.format("method argument type does not match any declared type: %s in %s.%s",
              paramsTypes, typeName, methodName));
        }

        Type<?> returnType = this.getTypeForWrappedClass(method.getReturnType());
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

    try {
      Field nameField = type.getClass().getField("methods");
      nameField.setAccessible(true);
      nameField.set(type, methods);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new TypeException("missing field 'methods' for class " + typeClass);
    }
  }
}
