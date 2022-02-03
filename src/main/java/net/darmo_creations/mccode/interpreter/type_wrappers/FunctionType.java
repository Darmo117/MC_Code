package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.Function;
import net.darmo_creations.mccode.interpreter.types.UserFunction;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Wrapper for {@link Function} class.
 * <p>
 * It does not have a cast operator.
 */
@Doc("Functions are objects that can be called and return a value based on the values of its parameters.")
public class FunctionType extends Type<Function> {
  public static final String NAME = "function";

  public static final String FUNCTION_TYPE_KEY = "Type";
  public static final String FUNCTION_KEY = "Function";

  private static final String FUNCTION_TYPE_BUILTIN = "builtin";
  private static final String FUNCTION_TYPE_USER = "user";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Function> getWrappedType() {
    return Function.class;
  }

  @Override
  public boolean generateCastOperator() {
    return false;
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Function self) {
    NBTTagCompound tag = super._writeToNBT(self);
    if (self instanceof BuiltinFunction) {
      tag.setString(FUNCTION_TYPE_KEY, FUNCTION_TYPE_BUILTIN);
      tag.setString(FUNCTION_KEY, self.getName());
    } else if (self instanceof UserFunction) {
      tag.setString(FUNCTION_TYPE_KEY, FUNCTION_TYPE_USER);
      NBTTagCompound functionTag = ((UserFunction) self).writeToNBT();
      tag.setTag(FUNCTION_KEY, functionTag);
    }
    return tag;
  }

  @Override
  public Function readFromNBT(final Scope scope, final NBTTagCompound tag) {
    String functionType = tag.getString(FUNCTION_TYPE_KEY);
    switch (functionType) {
      case FUNCTION_TYPE_BUILTIN:
        // Type-safe as builtin functions cannot be deleted nor overridden
        return (Function) scope.getVariable(tag.getString(FUNCTION_KEY), false);
      case FUNCTION_TYPE_USER:
        return new UserFunction(tag.getCompoundTag(FUNCTION_KEY));
      default:
        throw new MCCodeException("invalid function type " + functionType);
    }
  }
}
