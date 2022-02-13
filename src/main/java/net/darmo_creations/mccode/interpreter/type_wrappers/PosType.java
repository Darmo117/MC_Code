package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.types.Position;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * Wrapper type for {@link Position} class.
 * <p>
 * New instances can be created by casting {@link List}s or {@link Map}s.
 */
@Doc("Type representing a block position.")
public class PosType extends Type<Position> {
  public static final String NAME = "pos";

  public static final String X_KEY = "X";
  public static final String Y_KEY = "Y";
  public static final String Z_KEY = "Z";
  public static final String X_REL_KEY = "XRelative";
  public static final String Y_REL_KEY = "YRelative";
  public static final String Z_REL_KEY = "ZRelative";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Position> getWrappedType() {
    return Position.class;
  }

  @Property(name = "x")
  @Doc("The x component of a position.")
  public Number getX(final Position self) {
    double x = self.getX();
    if (x == Math.floor(x)) { // Cannot use ?: as it will convert long to double
      return (long) x;
    }
    return x;
  }

  @Property(name = "y")
  @Doc("The y component of a position.")
  public Number getY(final Position self) {
    double y = self.getY();
    if (y == Math.floor(y)) { // Cannot use ?: as it will convert long to double
      return (long) y;
    }
    return y;
  }

  @Property(name = "z")
  @Doc("The z component of a position.")
  public Number getZ(final Position self) {
    double z = self.getZ();
    if (z == Math.floor(z)) { // Cannot use ?: as it will convert long to double
      return (long) z;
    }
    return z;
  }

  @Property(name = "x_relative")
  @Doc("Indicates whether the x component is relative (tilde notation).")
  public Boolean isXRelative(final Position self) {
    return self.isXRelative();
  }

  @Property(name = "y_relative")
  @Doc("Indicates whether the y component is relative (tilde notation).")
  public Boolean isYRelative(final Position self) {
    return self.isYRelative();
  }

  @Property(name = "z_relative")
  @Doc("Indicates whether the z component is relative (tilde notation).")
  public Boolean isZRelative(final Position self) {
    return self.isZRelative();
  }

  @Method(name = "up")
  @Doc("Returns a position that is \"offset\" blocks above.")
  public Position up(final Scope scope, final Position self, final Long offset) {
    return self.up(offset.intValue());
  }

  @Method(name = "down")
  @Doc("Returns a position that is \"offset\" blocks below.")
  public Position down(final Scope scope, final Position self, final Long offset) {
    return self.down(offset.intValue());
  }

  @Method(name = "north")
  @Doc("Returns a position that is \"offset\" blocks to the north.")
  public Position north(final Scope scope, final Position self, final Long offset) {
    return self.north(offset.intValue());
  }

  @Method(name = "south")
  @Doc("Returns a position that is \"offset\" blocks to the south.")
  public Position south(final Scope scope, final Position self, final Long offset) {
    return self.south(offset.intValue());
  }

  @Method(name = "west")
  @Doc("Returns a position that is \"offset\" blocks to the west.")
  public Position west(final Scope scope, final Position self, final Long offset) {
    return self.west(offset.intValue());
  }

  @Method(name = "east")
  @Doc("Returns a position that is \"offset\" blocks to the east.")
  public Position east(final Scope scope, final Position self, final Long offset) {
    return self.east(offset.intValue());
  }

  @Method(name = "rotate")
  @Doc("Rotates a position by 0° (0), 90° (1), 180° (2) or 270° (3).")
  public Position rotate(final Scope scope, final Position self, final Long quadrant) {
    Rotation rotation = Rotation.NONE;
    switch (quadrant.intValue() % 4) {
      case 1:
        rotation = Rotation.CLOCKWISE_90;
        break;
      case 2:
        rotation = Rotation.CLOCKWISE_180;
        break;
      case 3:
        rotation = Rotation.COUNTERCLOCKWISE_90;
    }
    return self.rotate(rotation);
  }

  @Method(name = "distance")
  @Doc("Returns the distance between two positions.")
  public double getDistance(final Scope scope, final Position self, final Position other) {
    return self.getDistance(other);
  }

  @Method(name = "distance_sq")
  @Doc("Returns the squared distance between two positions.")
  public double getDistanceSq(final Scope scope, final Position self, final Position other) {
    return self.getDistanceSq(other);
  }

  @Override
  protected Object __minus__(final Scope scope, final Position self) {
    return new Position(-self.getX(), -self.getY(), -self.getZ(),
        self.isXRelative(), self.isYRelative(), self.isZRelative());
  }

  @Override
  protected Object __add__(final Scope scope, final Position self, final Object o, final boolean inPlace) {
    if (o instanceof Position) {
      return self.add(ProgramManager.getTypeInstance(PosType.class).implicitCast(scope, o));
    } else if (o instanceof String) {
      return this.__str__(self) + o;
    }
    return super.__add__(scope, self, o, inPlace);
  }

  @Override
  protected Object __sub__(final Scope scope, final Position self, final Object o, final boolean inPlace) {
    if (o instanceof Position) {
      return self.subtract(ProgramManager.getTypeInstance(PosType.class).implicitCast(scope, o));
    }
    return super.__sub__(scope, self, o, inPlace);
  }

  @Override
  protected Object __mul__(final Scope scope, final Position self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      return self.multiply(ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o));
    }
    return super.__mul__(scope, self, o, inPlace);
  }

  @Override
  protected Object __div__(final Scope scope, final Position self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
      if (n == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return self.divide(n);
    }
    return super.__div__(scope, self, o, inPlace);
  }

  @Override
  protected Object __intdiv__(final Scope scope, final Position self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
      if (n == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return self.intDivide(n);
    }
    return super.__intdiv__(scope, self, o, inPlace);
  }

  @Override
  protected Object __mod__(final Scope scope, final Position self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
      if (n == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return self.modulo(n);
    }
    return super.__mod__(scope, self, o, inPlace);
  }

  @Override
  protected Object __pow__(final Scope scope, final Position self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      return self.pow(ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o));
    }
    return super.__pow__(scope, self, o, inPlace);
  }

  @Override
  protected Object __eq__(final Scope scope, final Position self, final Object o) {
    if (!(o instanceof Position)) {
      return false;
    }
    return self.equals(o);
  }

  @Override
  protected Object __gt__(final Scope scope, final Position self, final Object o) {
    if (o instanceof Position) {
      return self.compareTo((Position) o) > 0;
    }
    return super.__gt__(scope, self, o);
  }

  @Override
  protected String __str__(final Position self) {
    return self.toString();
  }

  @Override
  public Position explicitCast(final Scope scope, final Object o) {
    if (o instanceof List) {
      //noinspection unchecked
      List<Object> list = (List<Object>) o;
      if (list.size() != 3) {
        throw new EvaluationException(scope, "mccode.interpreter.error.pos_list_format", list);
      }
      Pair<Double, Boolean> resX = this.extractComponent(scope, list.get(0));
      Pair<Double, Boolean> resY = this.extractComponent(scope, list.get(1));
      Pair<Double, Boolean> resZ = this.extractComponent(scope, list.get(2));
      return new Position(resX.getLeft(), resY.getLeft(), resZ.getLeft(),
          resX.getRight(), resY.getRight(), resZ.getRight());

    } else if (o instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) o;
      if (map.size() != 3 || !map.containsKey("x") || !map.containsKey("y") || !map.containsKey("z")) {
        throw new EvaluationException(scope, "mccode.interpreter.error.pos_map_format", map);
      }
      Pair<Double, Boolean> resX = this.extractComponent(scope, map.get("x"));
      Pair<Double, Boolean> resY = this.extractComponent(scope, map.get("y"));
      Pair<Double, Boolean> resZ = this.extractComponent(scope, map.get("z"));
      return new Position(resX.getLeft(), resY.getLeft(), resZ.getLeft(),
          resX.getRight(), resY.getRight(), resZ.getRight());
    }

    return super.explicitCast(scope, o);
  }

  private Pair<Double, Boolean> extractComponent(final Scope scope, final Object o) {
    if (o instanceof Number) {
      return new ImmutablePair<>(((Number) o).doubleValue(), false);
    } else if (o instanceof String) {
      String s = (String) o;
      boolean relative = false;
      if (s.charAt(0) == '~') {
        relative = true;
        if (s.length() == 1) {
          s = "0";
        } else {
          s = s.substring(1);
        }
      }
      return new ImmutablePair<>(Double.parseDouble(s), relative);
    } else {
      throw new CastException(scope, ProgramManager.getTypeInstance(FloatType.class), ProgramManager.getTypeForValue(o));
    }
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Position self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setDouble(X_KEY, self.getX());
    tag.setDouble(Y_KEY, self.getY());
    tag.setDouble(Z_KEY, self.getZ());
    tag.setBoolean(X_REL_KEY, self.isXRelative());
    tag.setBoolean(Y_REL_KEY, self.isYRelative());
    tag.setBoolean(Z_REL_KEY, self.isZRelative());
    return tag;
  }

  @Override
  public Position readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return new Position(tag.getDouble(X_KEY), tag.getDouble(Y_KEY), tag.getDouble(Z_KEY),
        tag.getBoolean(X_REL_KEY), tag.getBoolean(Y_REL_KEY), tag.getBoolean(Z_REL_KEY));
  }
}
