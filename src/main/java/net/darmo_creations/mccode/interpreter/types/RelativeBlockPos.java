package net.darmo_creations.mccode.interpreter.types;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import javax.annotation.concurrent.Immutable;

/**
 * {@link BlockPos} subclass that allows representing coordinates relative to an entity (tilde command notation).
 */
@Immutable
public class RelativeBlockPos extends BlockPos {
  private final boolean xRelative;
  private final boolean yRelative;
  private final boolean zRelative;

  public RelativeBlockPos(final int x, final int y, final int z, final boolean xRelative, final boolean yRelative, final boolean zRelative) {
    super(x, y, z);
    this.xRelative = xRelative;
    this.yRelative = yRelative;
    this.zRelative = zRelative;
  }

  public RelativeBlockPos(final double x, final double y, final double z, final boolean xRelative, final boolean yRelative, final boolean zRelative) {
    super(x, y, z);
    this.xRelative = xRelative;
    this.yRelative = yRelative;
    this.zRelative = zRelative;
  }

  public RelativeBlockPos(final Vec3i source, final boolean xRelative, final boolean yRelative, final boolean zRelative) {
    super(source);
    this.xRelative = xRelative;
    this.yRelative = yRelative;
    this.zRelative = zRelative;
  }

  public RelativeBlockPos(final Vec3d vec, final boolean xRelative, final boolean yRelative, final boolean zRelative) {
    super(vec);
    this.xRelative = xRelative;
    this.yRelative = yRelative;
    this.zRelative = zRelative;
  }

  public String x() {
    int x = super.getX();
    if (this.xRelative) {
      return "~" + (x != 0 ? x : "");
    }
    return "" + x;
  }

  public String y() {
    int y = super.getY();
    if (this.yRelative) {
      return "~" + (y != 0 ? y : "");
    }
    return "" + y;
  }

  public String z() {
    int z = super.getZ();
    if (this.zRelative) {
      return "~" + (z != 0 ? z : "");
    }
    return "" + z;
  }

  @Override
  public BlockPos add(int x, int y, int z) {
    return x == 0 && y == 0 && z == 0 ? this : new RelativeBlockPos(this.getX() + x, this.getY() + y, this.getZ() + z, this.xRelative, this.yRelative, this.zRelative);
  }

  @Override
  public BlockPos offset(EnumFacing facing, int n) {
    return n == 0 ? this : new RelativeBlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n, this.xRelative, this.yRelative, this.zRelative);
  }

  @Override
  public BlockPos rotate(Rotation rotationIn) {
    switch (rotationIn) {
      case NONE:
      default:
        return this;
      case CLOCKWISE_90:
        return new RelativeBlockPos(-this.getZ(), this.getY(), this.getX(), this.zRelative, this.yRelative, this.xRelative);
      case CLOCKWISE_180:
        return new RelativeBlockPos(-this.getX(), this.getY(), -this.getZ(), this.xRelative, this.yRelative, this.zRelative);
      case COUNTERCLOCKWISE_90:
        return new RelativeBlockPos(this.getZ(), this.getY(), -this.getX(), this.zRelative, this.yRelative, this.xRelative);
    }
  }

  @Override
  public BlockPos crossProduct(Vec3i vec) {
    return new RelativeBlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX(), this.xRelative, this.yRelative, this.zRelative);
  }
}
