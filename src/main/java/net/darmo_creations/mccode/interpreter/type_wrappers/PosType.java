package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  @Override
  protected Object __minus__(final Scope scope, final BlockPos self) {
    return super.__minus__(scope, self); // TODO
  }

  @Override
  protected Object __add__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return self.add(scope.getInterpreter().getTypeInstance(PosType.class).implicitCast(scope, o));
  }

  @Override
  protected Object __sub__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return self.subtract(scope.getInterpreter().getTypeInstance(PosType.class).implicitCast(scope, o));
  }

  @Override
  protected Object __mul__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return super.__mul__(scope, self, o, inPlace); // TODO
  }

  @Override
  protected Object __div__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return super.__div__(scope, self, o, inPlace); // TODO
  }

  @Override
  protected Object __intdiv__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return super.__intdiv__(scope, self, o, inPlace); // TODO
  }

  @Override
  protected Object __mod__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return super.__mod__(scope, self, o, inPlace); // TODO
  }

  @Override
  protected Object __pow__(final Scope scope, final BlockPos self, final Object o, final boolean inPlace) {
    return super.__pow__(scope, self, o, inPlace); // TODO
  }

  @Override
  protected Object __eq__(Scope scope, final BlockPos self, final Object o) {
    return super.__eq__(scope, self, o); // TODO
  }

  @Override
  public BlockPos implicitCast(final Scope scope, final Object o) {
    if (o instanceof List) {
      //noinspection unchecked
      List<Object> list = (List<Object>) o;
      if (list.size() == 3) {
        IntType numberType = scope.getInterpreter().getTypeInstance(IntType.class);
        List<Integer> values = list.stream().map(o1 -> numberType.implicitCast(scope, o1)).collect(Collectors.toList());
        return new BlockPos(values.get(0), values.get(1), values.get(2));
      }
    } else if (o instanceof Map) {
      IntType intType = scope.getInterpreter().getTypeInstance(IntType.class);
      Map<?, ?> m = (Map<?, ?>) o;
      return new BlockPos(intType.implicitCast(scope, m.get("x")), intType.implicitCast(scope, m.get("y")), intType.implicitCast(scope, m.get("z")));
    }
    return super.implicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final BlockPos self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
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
