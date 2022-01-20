package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.Interpreter;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCList;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.stream.Collectors;

@WrapperType(name = PosType.NAME, trueType = BlockPos.class)
public class PosType extends Type<BlockPos> {
  public static final String NAME = "pos";

  @Property(name = "x")
  public Integer getX(final BlockPos pos) {
    return pos.getX();
  }

  @Property(name = "y")
  public Integer getY(final BlockPos pos) {
    return pos.getY();
  }

  @Property(name = "z")
  public Integer getZ(final BlockPos pos) {
    return pos.getZ();
  }

  @Override
  public BlockPos getDefault() {
    return new BlockPos(0, 0, 0);
  }

  @Override
  public BlockPos cast(final Object o) {
    if (o instanceof MCList) {
      MCList list = (MCList) o;
      if (list.size() == 3) {
        IntType numberType = Interpreter.getTypeForClass(Integer.class);
        List<Integer> values = list.stream().map(numberType::cast).collect(Collectors.toList());
        return new BlockPos(values.get(0), values.get(1), values.get(2));
      }
    }
    return super.cast(o);
  }
}
