package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.types.Position;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Wrapper type for {@link Integer}.
 */
@Doc("Type representing integers.")
public class IntType extends Type<Long> {
  public static final String NAME = "int";

  public static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Long> getWrappedType() {
    return Long.class;
  }

  @Override
  protected Object __minus__(final Scope scope, final Long self) {
    return -self;
  }

  @Override
  protected Object __add__(final Scope scope, final Long self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      return self + (Long) o;
    } else if (o instanceof Double) {
      return self + (Double) o;
    } else if (o instanceof Boolean) {
      return self + ((Boolean) o ? 1 : 0);
    } else if (o instanceof String) {
      return this.__str__(self) + o;
    }
    return super.__add__(scope, self, o, inPlace);
  }

  @Override
  protected Object __sub__(final Scope scope, final Long self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      return self - (Long) o;
    } else if (o instanceof Double) {
      return self - (Double) o;
    } else if (o instanceof Boolean) {
      return self - ((Boolean) o ? 1 : 0);
    }
    return super.__sub__(scope, self, o, inPlace);
  }

  @Override
  protected Object __mul__(final Scope scope, final Long self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      return self * (Long) o;
    } else if (o instanceof Double) {
      return self * (Double) o;
    } else if (o instanceof Boolean) {
      return self * ((Boolean) o ? 1 : 0);
    } else if (o instanceof Position) {
      return ProgramManager.getTypeInstance(PosType.class).__mul__(scope, (Position) o, self, inPlace);
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
  protected Object __div__(final Scope scope, final Long self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      if ((Long) o == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return self.doubleValue() / (Long) o;
    } else if (o instanceof Double) {
      if ((Double) o == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return self / (Double) o;
    } else if (o instanceof Boolean) {
      if (!(Boolean) o) {
        throw new ArithmeticException("/ by 0");
      }
      return (double) self;
    }
    return super.__div__(scope, self, o, inPlace);
  }

  @Override
  protected Object __mod__(final Scope scope, final Long self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      if ((Long) o == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return (long) Utils.trueModulo(self, (Long) o);
    } else if (o instanceof Double) {
      if ((Double) o == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return Utils.trueModulo(self, (Double) o);
    } else if (o instanceof Boolean) {
      if ((Boolean) o) {
        return 0L;
      }
      throw new ArithmeticException("/ by 0");
    }
    return super.__mod__(scope, self, o, inPlace);
  }

  @Override
  protected Object __pow__(final Scope scope, final Long self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      return (long) Math.pow(self, (Long) o);
    } else if (o instanceof Double) {
      return Math.pow(self, (Double) o);
    } else if (o instanceof Boolean) {
      return (!(Boolean) o) ? 1 : self;
    }
    return super.__pow__(scope, self, o, inPlace);
  }

  @Override
  protected Object __eq__(final Scope scope, final Long self, final Object o) {
    FloatType floatType = ProgramManager.getTypeInstance(FloatType.class);
    double d = floatType.implicitCast(scope, self);
    return floatType.__eq__(scope, d, o);
  }

  @Override
  protected Object __gt__(final Scope scope, final Long self, final Object o) {
    FloatType floatType = ProgramManager.getTypeInstance(FloatType.class);
    double d = floatType.implicitCast(scope, self);
    return floatType.__gt__(scope, d, o);
  }

  @Override
  protected boolean __bool__(final Long self) {
    return self != 0;
  }

  @Override
  public Long implicitCast(final Scope scope, final Object o) {
    if (o instanceof Boolean) {
      return ((Boolean) o) ? 1L : 0;
    }
    return super.implicitCast(scope, o);
  }

  @Override
  public Long explicitCast(final Scope scope, final Object o) throws MCCodeRuntimeException {
    if (o instanceof Number) {
      return ((Number) o).longValue();
    } else if (o instanceof String) {
      try {
        return Long.parseLong((String) o);
      } catch (NumberFormatException e) {
        throw new EvaluationException(scope, "mccode.interpreter.error.int_format", Utils.escapeString((String) o));
      }
    }
    return super.explicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Long self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setLong(VALUE_KEY, self);
    return tag;
  }

  @Override
  public Long readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getLong(VALUE_KEY);
  }
}
