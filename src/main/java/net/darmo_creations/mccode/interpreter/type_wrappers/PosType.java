package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.types.RelativeBlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper type for {@link BlockPos} class.
 * <p>
 * New instances can be created by casting {@link List}s or {@link Map}s.
 */
@Doc("Type representing a block position.")
public class PosType extends Type<BlockPos> { // TODO add support for RelativeBlockPos
  public static final String NAME = "pos";

  public static final String X_KEY = "X";
  public static final String Y_KEY = "Y";
  public static final String Z_KEY = "Z";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<BlockPos> getWrappedType() {
    return BlockPos.class;
  }

  @Property(name = "x")
  public Integer getX(final BlockPos self) {
    return self.getX();
  }

  @Property(name = "y")
  public Integer getY(final BlockPos self) {
    return self.getY();
  }

  @Property(name = "z")
  public Integer getZ(final BlockPos self) {
    return self.getZ();
  }

  @Override
  protected Object __get_property__(final Scope scope, final BlockPos self, final String propertyName) {
    if (self instanceof RelativeBlockPos) {
      if ("x_relative".equals(propertyName)) {
        return ((RelativeBlockPos) self).x();
      }
      if ("y_relative".equals(propertyName)) {
        return ((RelativeBlockPos) self).y();
      }
      if ("z_relative".equals(propertyName)) {
        return ((RelativeBlockPos) self).z();
      }
    }
    return super.__get_property__(scope, self, propertyName);
  }

  @Method(name = "up")
  public BlockPos up(final Scope scope, final BlockPos self, final Long offset) {
    return self.up(offset.intValue());
  }

  @Method(name = "down")
  public BlockPos down(final Scope scope, final BlockPos self, final Long offset) {
    return self.down(offset.intValue());
  }

  @Method(name = "north")
  public BlockPos north(final Scope scope, final BlockPos self, final Long offset) {
    return self.north(offset.intValue());
  }

  @Method(name = "south")
  public BlockPos south(final Scope scope, final BlockPos self, final Long offset) {
    return self.south(offset.intValue());
  }

  @Method(name = "west")
  public BlockPos west(final Scope scope, final BlockPos self, final Long offset) {
    return self.west(offset.intValue());
  }

  @Method(name = "east")
  public BlockPos east(final Scope scope, final BlockPos self, final Long offset) {
    return self.east(offset.intValue());
  }

  @Override
  protected Object __minus__(final Scope scope, final BlockPos self) {
    return new BlockPos(-self.getX(), -self.getY(), -self.getZ());
  }

  @Override
  protected Object __add__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    if (o instanceof BlockPos) {
      return self.add(ProgramManager.getTypeInstance(PosType.class).implicitCast(scope, o));
    } else if (o instanceof String) {
      return self.toString() + o;
    }
    return super.__add__(scope, self, o, inPlace);
  }

  @Override
  protected Object __sub__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    if (o instanceof BlockPos) {
      return self.subtract(ProgramManager.getTypeInstance(PosType.class).implicitCast(scope, o));
    }
    return super.__sub__(scope, self, o, inPlace);
  }

  @Override
  protected Object __mul__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
      return new BlockPos(self.getX() * n, self.getY() * n, self.getZ() * n);
    }
    return super.__mul__(scope, self, o, inPlace);
  }

  @Override
  protected Object __div__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
      if (n == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return new BlockPos(self.getX() / n, self.getY() / n, self.getZ() / n);
    }
    return super.__div__(scope, self, o, inPlace);
  }

  @Override
  protected Object __intdiv__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
      if (n == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return new BlockPos((int) (self.getX() / n), (int) (self.getY() / n), (int) (self.getZ() / n));
    }
    return super.__intdiv__(scope, self, o, inPlace);
  }

  @Override
  protected Object __mod__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
      if (n == 0) {
        throw new ArithmeticException("/ by 0");
      }
      return new BlockPos(Utils.trueModulo(self.getX(), n), Utils.trueModulo(self.getY(), n), Utils.trueModulo(self.getZ(), n));
    }
    return super.__mod__(scope, self, o, inPlace);
  }

  @Override
  protected Object __pow__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    if (o instanceof Number || o instanceof Boolean) {
      double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
      return new BlockPos(Math.pow(self.getX(), n), Math.pow(self.getY(), n), Math.pow(self.getZ(), n));
    }
    return super.__pow__(scope, self, o, inPlace);
  }

  @Override
  protected Object __eq__(final Scope scope, final BlockPos self, final Object o) {
    if (!(o instanceof BlockPos)) {
      return false;
    }
    return self.equals(o);
  }

  @Override
  protected Object __gt__(final Scope scope, final BlockPos self, final Object o) {
    if (o instanceof BlockPos) {
      return self.compareTo((BlockPos) o) > 0;
    }
    return super.__gt__(scope, self, o);
  }

  @Override
  public BlockPos explicitCast(final Scope scope, final Object o) {
    if (o instanceof List) {
      //noinspection unchecked
      List<Object> list = (List<Object>) o;
      if (list.size() != 3) {
        throw new EvaluationException(scope, "mccode.interpreter.error.pos_list_format", list);
      }
      IntType intType = ProgramManager.getTypeInstance(IntType.class);
      List<Long> values = list.stream().map(o1 -> intType.implicitCast(scope, o1)).collect(Collectors.toList());
      return new BlockPos(values.get(0), values.get(1), values.get(2));
    } else if (o instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) o;
      if (map.size() != 3 || !map.containsKey("x") || !map.containsKey("y") || !map.containsKey("z")) {
        throw new EvaluationException(scope, "mccode.interpreter.error.pos_map_format", map);
      }
      IntType intType = ProgramManager.getTypeInstance(IntType.class);
      return new BlockPos(intType.implicitCast(scope, map.get("x")), intType.implicitCast(scope, map.get("y")), intType.implicitCast(scope, map.get("z")));
    }
    return super.explicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final BlockPos self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setInteger(X_KEY, self.getX());
    tag.setInteger(Y_KEY, self.getY());
    tag.setInteger(Z_KEY, self.getZ());
    return tag;
  }

  @Override
  public BlockPos readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return NBTUtil.getPosFromTag(tag);
  }
}
