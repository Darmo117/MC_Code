package net.darmo_creations.naissancee.mccode.interpreter;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.darmo_creations.naissancee.mccode.interpreter.exceptions.TypeException;
import net.darmo_creations.naissancee.mccode.interpreter.type_wrappers.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {
  private static final Map<String, Type<?>> TYPE_NAMES = new HashMap<>();
  private static final Map<Class<?>, Type<?>> TRUE_TYPES = new HashMap<>();

  static {
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
    declareType(EntityType.class);
  }

  public static Type<?> getTypeForName(final String name) {
    return TYPE_NAMES.get(name);
  }

  public static <T, U extends Type<T>> U getTypeForClass(final Class<T> class_) {
    //noinspection unchecked
    return (U) TRUE_TYPES.get(class_);
  }

  public static Type<?> getTypeForValue(final Object o) {
    return o != null ? getTypeForClass(o.getClass()) : getTypeForName(NullType.NAME);
  }

  public static void declareType(final Class<? extends Type<?>> typeClass) throws TypeException {
    WrapperType typeAnnotation = typeClass.getAnnotation(WrapperType.class);
    String name = typeAnnotation.name();
    Class<?> trueType = typeAnnotation.trueType();

    if (TYPE_NAMES.containsKey(name)) {
      throw new TypeException(String.format("a type with the name \"%s\" already exists", name));
    }
    if (TRUE_TYPES.containsKey(trueType)) {
      throw new TypeException("a wrapper is already declared for class %s" + trueType);
    }

    Type<?> type;
    try {
      type = typeClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new TypeException("missing empty constructor for class %s" + typeClass);
    }
    if (TYPE_NAMES.values().stream().anyMatch(t -> t.getClass() == type.getClass())) {
      throw new TypeException(String.format("cannot redeclare type \"%s\"", name));
    }

    // Set private name field of type object
    try {
      Field nameField = type.getClass().getField("name");
      nameField.setAccessible(true);
      nameField.set(type, name);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new TypeException("missing field name for class " + typeClass);
    }

    // Set private trueType field of type object
    try {
      Field nameField = type.getClass().getField("trueType");
      nameField.setAccessible(true);
      nameField.set(type, trueType);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new TypeException("missing field trueType for class " + typeClass);
    }

    // Set private properties field of type object
    Map<String, Method> properties = new HashMap<>();
    for (Method method : typeClass.getMethods()) {
      if (method.isAnnotationPresent(Property.class)) {
        properties.put(method.getAnnotation(Property.class).name(), method);
      }
    }
    try {
      Field nameField = type.getClass().getField("properties");
      nameField.setAccessible(true);
      nameField.set(type, properties);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new TypeException("missing field properties for class " + typeClass);
    }

    TYPE_NAMES.put(name, type);
    TRUE_TYPES.put(trueType, type);
  }
}
