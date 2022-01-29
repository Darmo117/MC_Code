package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
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
public class PosType extends Type<BlockPos> {
  public static final String NAME = "pos";

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

  @Method(name = "up")
  public BlockPos up(final Scope scope, final BlockPos self, final Integer offset) {
    return self.up(offset);
  }

  @Method(name = "down")
  public BlockPos down(final Scope scope, final BlockPos self, final Integer offset) {
    return self.down(offset);
  }

  @Method(name = "north")
  public BlockPos north(final Scope scope, final BlockPos self, final Integer offset) {
    return self.north(offset);
  }

  @Method(name = "south")
  public BlockPos south(final Scope scope, final BlockPos self, final Integer offset) {
    return self.south(offset);
  }

  @Method(name = "west")
  public BlockPos west(final Scope scope, final BlockPos self, final Integer offset) {
    return self.west(offset);
  }

  @Method(name = "east")
  public BlockPos east(final Scope scope, final BlockPos self, final Integer offset) {
    return self.east(offset);
  }

  @Override
  protected Object __minus__(final Scope scope, final BlockPos self) {
    return new BlockPos(-self.getX(), -self.getY(), -self.getZ());
  }

  @Override
  protected Object __add__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return self.add(ProgramManager.getTypeInstance(PosType.class).implicitCast(scope, o));
  }

  @Override
  protected Object __sub__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return self.subtract(ProgramManager.getTypeInstance(PosType.class).implicitCast(scope, o));
  }

  @Override
  protected Object __mul__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
    return new BlockPos(self.getX() * n, self.getY() * n, self.getZ() * n);
  }

  @Override
  protected Object __div__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
    if (n == 0) {
      throw new ArithmeticException("/ by 0");
    }
    return new BlockPos(self.getX() / n, self.getY() / n, self.getZ() / n);
  }

  @Override
  protected Object __intdiv__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
    if (n == 0) {
      throw new ArithmeticException("/ by 0");
    }
    return new BlockPos((int) (self.getX() / n), (int) (self.getY() / n), (int) (self.getZ() / n));
  }

  @Override
  protected Object __mod__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
    return new BlockPos(Utils.trueModulo(self.getX(), n), Utils.trueModulo(self.getY(), n), Utils.trueModulo(self.getZ(), n));
  }

  @Override
  protected Object __pow__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    double n = ProgramManager.getTypeInstance(FloatType.class).implicitCast(scope, o);
    return new BlockPos(Math.pow(self.getX(), n), Math.pow(self.getY(), n), Math.pow(self.getZ(), n));
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
      if (list.size() == 3) {
        IntType intType = ProgramManager.getTypeInstance(IntType.class);
        List<Integer> values = list.stream().map(o1 -> intType.implicitCast(scope, o1)).collect(Collectors.toList());
        return new BlockPos(values.get(0), values.get(1), values.get(2));
      }
    } else if (o instanceof Map) {
      IntType intType = ProgramManager.getTypeInstance(IntType.class);
      Map<?, ?> m = (Map<?, ?>) o;
      return new BlockPos(intType.implicitCast(scope, m.get("x")), intType.implicitCast(scope, m.get("y")), intType.implicitCast(scope, m.get("z")));
    }
    return super.explicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final BlockPos self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setInteger("X", self.getX());
    tag.setInteger("Y", self.getY());
    tag.setInteger("Z", self.getZ());
    return tag;
  }

  @Override
  public BlockPos readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return NBTUtil.getPosFromTag(tag);
  }
}
