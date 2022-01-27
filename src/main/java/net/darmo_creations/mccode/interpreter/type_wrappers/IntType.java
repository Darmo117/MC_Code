package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Wrapper type for {@link Integer}.
 */
public class IntType extends Type<Integer> {
  public static final String NAME = "int";

  private static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Integer> getWrappedType() {
    return Integer.class;
  }

  @Override
  protected Object __minus__(final Scope scope, final Integer self) {
    return -self;
  }

  @Override
  protected Object __add__(final Scope scope, final Integer self, final Object o, boolean inPlace) {
    if (o instanceof Integer) {
      return self + (Integer) o;
    } else if (o instanceof Double) {
      return self + (Double) o;
    } else if (o instanceof Boolean) {
      return self + ((Boolean) o ? 1 : 0);
    } else if (o instanceof String) {
      return self.toString() + o;
    }
    return super.__add__(scope, self, o, inPlace);
  }

  @Override
  protected Object __sub__(final Scope scope, final Integer self, final Object o, boolean inPlace) {
    if (o instanceof Integer) {
      return self - (Integer) o;
    } else if (o instanceof Double) {
      return self - (Double) o;
    } else if (o instanceof Boolean) {
      return self - ((Boolean) o ? 1 : 0);
    }
    return super.__sub__(scope, self, o, inPlace);
  }

  @Override
  protected Object __mul__(final Scope scope, final Integer self, final Object o, boolean inPlace) {
    if (o instanceof Integer) {
      return self * (Integer) o;
    } else if (o instanceof Double) {
      return self * (Double) o;
    } else if (o instanceof Boolean) {
      return self * ((Boolean) o ? 1 : 0);
    } else if (o instanceof BlockPos) {
      return ProgramManager.getTypeInstance(PosType.class).__mul__(scope, (BlockPos) o, self, inPlace);
    } else if (o instanceof String) {
      // Return a new string instance everytime
      return ProgramManager.getTypeInstance(StringType.class).__mul__(scope, (String) o, self, false);
    } else if (o instanceof MCList) {
      // Return a new list instance everytime
      return ProgramManager.getTypeInstance(ListType.class).__mul__(scope, (MCList) o, self, false);
    }
    return super.__mul__(scope, self, o, inPlace);
  }

  @Override
  protected Object __div__(final Scope scope, final Integer self, final Object o, boolean inPlace) {
    FloatType floatType = ProgramManager.getTypeInstance(FloatType.class);
    double d = floatType.implicitCast(scope, self);
    return floatType.__div__(scope, d, o, inPlace);
  }

  @Override
  protected Object __mod__(final Scope scope, final Integer self, final Object o, boolean inPlace) {
    FloatType floatType = ProgramManager.getTypeInstance(FloatType.class);
    double d = floatType.implicitCast(scope, self);
    return floatType.__mod__(scope, d, o, inPlace);
  }

  @Override
  protected Object __pow__(final Scope scope, final Integer self, final Object o, boolean inPlace) {
    if (o instanceof Integer) {
      return (int) Math.pow(self, (Integer) o);
    } else if (o instanceof Double) {
      return Math.pow(self, (Double) o);
    } else if (o instanceof Boolean) {
      return (!(Boolean) o) ? 1 : self;
    }
    return super.__pow__(scope, self, o, inPlace);
  }

  @Override
  protected Object __eq__(final Scope scope, final Integer self, final Object o) {
    FloatType floatType = ProgramManager.getTypeInstance(FloatType.class);
    double d = floatType.implicitCast(scope, self);
    return floatType.__eq__(scope, d, o);
  }

  @Override
  protected Object __gt__(final Scope scope, final Integer self, final Object o) {
    FloatType floatType = ProgramManager.getTypeInstance(FloatType.class);
    double d = floatType.implicitCast(scope, self);
    return floatType.__gt__(scope, d, o);
  }

  @Override
  protected boolean __bool__(final Integer self) {
    return self != 0;
  }

  @Override
  public Integer implicitCast(final Scope scope, final Object o) {
    if (o instanceof Boolean) {
      return ((Boolean) o) ? 1 : 0;
    }
    return super.implicitCast(scope, o);
  }

  @Override
  public Integer explicitCast(final Scope scope, final Object o) throws MCCodeRuntimeException {
    if (o instanceof Number) {
      return ((Number) o).intValue();
    } else if (o instanceof String) {
      return Integer.parseInt((String) o);
    }
    return super.explicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Integer self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setInteger(VALUE_KEY, self);
    return tag;
  }

  @Override
  public Integer readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getInteger(VALUE_KEY);
  }
}
