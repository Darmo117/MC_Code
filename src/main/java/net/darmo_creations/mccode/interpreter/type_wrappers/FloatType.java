package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.types.Position;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Wrapper type for {@link Double}.
 */
@Doc("Type representing real numbers.")
public class FloatType extends Type<Double> {
  public static final String NAME = "float";

  public static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Double> getWrappedType() {
    return Double.class;
  }

  @Override
  protected Object __minus__(final Scope scope, final Double self) {
    return self == 0 ? 0.0 : -self; // Avoid -0.0
  }

  @Override
  protected Object __add__(final Scope scope, final Double self, final Object o, boolean inPlace) {
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
  protected Object __sub__(final Scope scope, final Double self, final Object o, boolean inPlace) {
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
  protected Object __mul__(final Scope scope, final Double self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      return self * (Long) o;
    } else if (o instanceof Double) {
      return self * (Double) o;
    } else if (o instanceof Boolean) {
      return self * ((Boolean) o ? 1 : 0);
    } else if (o instanceof Position) {
      return ProgramManager.getTypeInstance(PosType.class).__mul__(scope, (Position) o, self, inPlace);
    }
    return super.__mul__(scope, self, o, inPlace);
  }

  @Override
  protected Object __div__(final Scope scope, final Double self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      if ((Long) o == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return self / (Long) o;
    } else if (o instanceof Double) {
      if ((Double) o == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return self / (Double) o;
    } else if (o instanceof Boolean) {
      if (!(Boolean) o) {
        throw new ArithmeticException("/ by 0");
      }
      return self;
    }
    return super.__div__(scope, self, o, inPlace);
  }

  @Override
  protected Object __mod__(final Scope scope, final Double self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      return Utils.trueModulo(self, (Long) o);
    } else if (o instanceof Double) {
      return Utils.trueModulo(self, (Double) o);
    } else if (o instanceof Boolean) {
      if ((Boolean) o) {
        return 0.0;
      }
      throw new ArithmeticException("/ by 0");
    }
    return super.__mod__(scope, self, o, inPlace);
  }

  @Override
  protected Object __pow__(final Scope scope, final Double self, final Object o, boolean inPlace) {
    if (o instanceof Long) {
      return Math.pow(self, (Long) o);
    } else if (o instanceof Double) {
      return Math.pow(self, (Double) o);
    } else if (o instanceof Boolean) {
      return (!(Boolean) o) ? 1.0 : self;
    }
    return super.__pow__(scope, self, o, inPlace);
  }

  @Override
  protected Object __eq__(final Scope scope, final Double self, final Object o) {
    if (o instanceof Number) {
      return self == ((Number) o).doubleValue();
    } else if (o instanceof Boolean) {
      return self == ((Boolean) o ? 1 : 0);
    }
    return super.__eq__(scope, self, o);
  }

  @Override
  protected Object __gt__(final Scope scope, final Double self, final Object o) {
    if (o instanceof Number || o instanceof Boolean) {
      return self > this.implicitCast(scope, o);
    }
    return super.__gt__(scope, self, o);
  }

  @Override
  protected boolean __bool__(final Double self) {
    return self != 0.0;
  }

  @Override
  public Double implicitCast(final Scope scope, final Object o) {
    if (o instanceof Number) {
      return ((Number) o).doubleValue();
    } else if (o instanceof Boolean) {
      return ((Boolean) o) ? 1.0 : 0.0;
    }
    return super.implicitCast(scope, o);
  }

  @Override
  public Double explicitCast(Scope scope, Object o) throws MCCodeRuntimeException {
    if (o instanceof String) {
      try {
        return Double.parseDouble((String) o);
      } catch (NumberFormatException e) {
        throw new EvaluationException(scope, "mccode.interpreter.error.float_format", Utils.escapeString((String) o));
      }
    }
    return super.explicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Double self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setDouble(VALUE_KEY, self);
    return tag;
  }

  @Override
  public Double readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getDouble(VALUE_KEY);
  }
}
