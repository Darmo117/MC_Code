package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.MemberFunction;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

// TODO delete
/**
 * Special function that represents a member function associated to an object.
 * <p>
 * The instance object is accessible from the scope under the name {@code $this}.
 */
public class BoundMemberFunction extends Function {
  private static final String INSTANCE_KEY = "Instance";
  private static final String TYPE_KEY = "Type";
  private static final String METHOD_NAME_KEY = "MethodName";

  private final Object instance;
  private final MemberFunction method;

  /**
   * Create a bound method for the given object.
   *
   * @param instance Object instance.
   * @param method   Unbound method.
   */
  public BoundMemberFunction(final Object instance, final MemberFunction method) {
    super(method.getName(), method.getParametersTypes(), method.getReturnType());
    this.instance = instance;
    this.method = method;
  }

  public BoundMemberFunction(final NBTTagCompound tag, final Scope scope) {
    this(extractInstance(tag, scope), extractMethod(tag, scope));
  }

  private static Object extractInstance(final NBTTagCompound tag, final Scope scope) {
    Type<?> type = scope.getInterpreter().getTypeForName(tag.getString(TYPE_KEY));
    return type.readFromNBT(scope, tag.getCompoundTag(INSTANCE_KEY));
  }

  private static MemberFunction extractMethod(final NBTTagCompound tag, final Scope scope) {
    Type<?> type = scope.getInterpreter().getTypeForName(tag.getString(TYPE_KEY));
    return type.getMethod(tag.getString(METHOD_NAME_KEY));
  }

  @Override
  public Object apply(Scope scope) {
    scope.declareVariable(new Variable(MemberFunction.SELF_PARAM_NAME, false, false, true, false, this.instance));
    return this.method.apply(scope);
  }

  public NBTTagCompound writeToNBT(final Scope scope) {
    NBTTagCompound tag = new NBTTagCompound();
    Type<?> type = this.method.getTargetType();
    tag.setTag(INSTANCE_KEY, type.writeToNBT(scope, this.instance));
    tag.setString(TYPE_KEY, type.getName());
    tag.setString(METHOD_NAME_KEY, this.method.getName());
    return tag;
  }
}
