package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.Utils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Objects;

/**
 * Represents a position in the game world.
 * Positions may have relative coordinates, akin to the tilde notation of in-game commands.
 * <p>
 * Positions are comparable.
 */
public class Position implements Comparable<Position> {
  private final double x;
  private final double y;
  private final double z;
  private final boolean xRelative;
  private final boolean yRelative;
  private final boolean zRelative;

  /**
   * Create an absolute position.
   *
   * @param x X component.
   * @param y Y component.
   * @param z Z component.
   */
  public Position(final double x, final double y, final double z) {
    this(x, y, z, false, false, false);
  }

  /**
   * Create a position from another.
   *
   * @param other The position to copy.
   */
  public Position(final Position other) {
    this(other.getX(), other.getY(), other.getZ(), other.isXRelative(), other.isYRelative(), other.isZRelative());
  }

  /**
   * Create an absolute position from a vector.
   *
   * @param vector The vector.
   */
  public Position(final Vec3d vector) {
    this(vector.x, vector.y, vector.z);
  }

  /**
   * Create an absolute position from a vector.
   *
   * @param vector The vector.
   */
  public Position(final Vec3i vector) {
    this(vector.getX(), vector.getY(), vector.getZ());
  }

  /**
   * Create a relative position.
   *
   * @param x         X component.
   * @param y         Y component.
   * @param z         Z component.
   * @param xRelative True if the X component is relative (tilde notation), false otherwise.
   * @param yRelative True if the Y component is relative (tilde notation), false otherwise.
   * @param zRelative True if the Z component is relative (tilde notation), false otherwise.
   */
  public Position(final double x, final double y, final double z,
                  final boolean xRelative, final boolean yRelative, final boolean zRelative) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.xRelative = xRelative;
    this.yRelative = yRelative;
    this.zRelative = zRelative;
  }

  /**
   * Create a relative position from another.
   *
   * @param other     The position to copy. Its relative flags are ignored.
   * @param xRelative True if the X component is relative (tilde notation), false otherwise.
   * @param yRelative True if the Y component is relative (tilde notation), false otherwise.
   * @param zRelative True if the Z component is relative (tilde notation), false otherwise.
   */
  public Position(final Position other, final boolean xRelative, final boolean yRelative, final boolean zRelative) {
    this(other.getX(), other.getY(), other.getZ(), xRelative, yRelative, zRelative);
  }

  /**
   * Create a relative position from another.
   *
   * @param vector    The vector.
   * @param xRelative True if the X component is relative (tilde notation), false otherwise.
   * @param yRelative True if the Y component is relative (tilde notation), false otherwise.
   * @param zRelative True if the Z component is relative (tilde notation), false otherwise.
   */
  public Position(final Vec3d vector, final boolean xRelative, final boolean yRelative, final boolean zRelative) {
    this(vector.x, vector.y, vector.z, xRelative, yRelative, zRelative);
  }

  /**
   * Create a relative position from another.
   *
   * @param vector    The vector.
   * @param xRelative True if the X component is relative (tilde notation), false otherwise.
   * @param yRelative True if the Y component is relative (tilde notation), false otherwise.
   * @param zRelative True if the Z component is relative (tilde notation), false otherwise.
   */
  public Position(final Vec3i vector, final boolean xRelative, final boolean yRelative, final boolean zRelative) {
    this(vector.getX(), vector.getY(), vector.getZ(), xRelative, yRelative, zRelative);
  }

  /**
   * Return the x compontent.
   */
  public double getX() {
    return this.x;
  }

  /**
   * Return the y compontent.
   */
  public double getY() {
    return this.y;
  }

  /**
   * Return the z compontent.
   */
  public double getZ() {
    return this.z;
  }

  /**
   * Return the command representation of the x coordinate.
   * Features the tilde character if the component is relative.
   */
  public String getXCommandRepresentation() {
    return formatRelative(this.getX(), this.isXRelative());
  }

  /**
   * Return the command representation of the y coordinate.
   * Features the tilde character if the component is relative.
   */
  public String getYCommandRepresentation() {
    return formatRelative(this.getY(), this.isYRelative());
  }

  /**
   * Return the command representation of the z coordinate.
   * Features the tilde character if the component is relative.
   */
  public String getZCommandRepresentation() {
    return formatRelative(this.getZ(), this.isZRelative());
  }

  private static String formatRelative(final double v, final boolean relative) {
    if (relative) {
      return "~" + (v != 0 ? (v == Math.floor(v) ? (long) v : v) : "");
    }
    return "" + v;
  }

  /**
   * Return true if the x component is relative, false otherwise.
   */
  public boolean isXRelative() {
    return this.xRelative;
  }

  /**
   * Return true if the y component is relative, false otherwise.
   */
  public boolean isYRelative() {
    return this.yRelative;
  }

  /**
   * Return true if the z component is relative, false otherwise.
   */
  public boolean isZRelative() {
    return this.zRelative;
  }

  /**
   * Add a position to this position.
   *
   * @param x X component to add.
   * @param y Y component to add.
   * @param z Z component to add.
   * @return A new position.
   */
  public Position add(final double x, final double y, final double z) {
    return new Position(this.x + x, this.y + y, this.z + z,
        this.xRelative, this.yRelative, this.zRelative);
  }

  /**
   * Add a position to this position.
   *
   * @param x X component to add.
   * @param y Y component to add.
   * @param z Z component to add.
   * @return A new position.
   */
  public Position add(final int x, final int y, final int z) {
    return this.add((double) x, y, z);
  }

  /**
   * Add a position to this position.
   *
   * @param other The position to add. Relative flags are ignored.
   * @return A new position.
   */
  public Position add(final Position other) {
    return this.add(other.getX(), other.getY(), other.getZ());
  }

  /**
   * Add a position to this position.
   *
   * @param vector The vector to add.
   * @return A new position.
   */
  public Position add(final Vec3d vector) {
    return this.add(vector.x, vector.y, vector.z);
  }

  /**
   * Add a position to this position.
   *
   * @param vector The vector to add.
   * @return A new position.
   */
  public Position add(final Vec3i vector) {
    return this.add(vector.getX(), vector.getY(), vector.getZ());
  }

  /**
   * Subtract a position from this position.
   *
   * @param x X component to subtract.
   * @param y Y component to subtract.
   * @param z Z component to subtract.
   * @return A new position.
   */
  public Position subtract(final double x, final double y, final double z) {
    return this.add(-x, -y, -z);
  }

  /**
   * Subtract a position from this position.
   *
   * @param x X component to subtract.
   * @param y Y component to subtract.
   * @param z Z component to subtract.
   * @return A new position.
   */
  public Position subtract(final int x, final int y, final int z) {
    return this.add((double) -x, -y, -z);
  }

  /**
   * Subtract a position from this position.
   *
   * @param other The position to subtract. Relative flags are ignored.
   * @return A new position.
   */
  public Position subtract(final Position other) {
    return this.add(-other.getX(), -other.getY(), -other.getZ());
  }

  /**
   * Subtract a position from this position.
   *
   * @param vector The vector to subtract.
   * @return A new position.
   */
  public Position subtract(final Vec3d vector) {
    return this.add(-vector.x, -vector.y, -vector.z);
  }

  /**
   * Subtract a position from this position.
   *
   * @param vector The vector to subtract.
   * @return A new position.
   */
  public Position subtract(final Vec3i vector) {
    return this.add(-vector.getX(), -vector.getY(), -vector.getZ());
  }

  /**
   * Multiply each component of this position by the given value.
   *
   * @param n The multiplication coefficient.
   * @return A new position.
   */
  public Position multiply(final double n) {
    return new Position(this.x * n, this.y * n, this.z * n,
        this.xRelative, this.yRelative, this.zRelative);
  }

  /**
   * Divide each component of this position by the given value.
   *
   * @param n The denominator.
   * @return A new position.
   */
  public Position divide(final double n) {
    return new Position(this.x / n, this.y / n, this.z / n,
        this.xRelative, this.yRelative, this.zRelative);
  }

  /**
   * Perform an integer division on each component of this position with the given value.
   *
   * @param n The denominator.
   * @return A new position.
   */
  public Position intDivide(final double n) {
    return new Position((int) (this.x / n), (int) (this.y / n), (int) (this.z / n),
        this.xRelative, this.yRelative, this.zRelative);
  }

  /**
   * Perform a modulo operation on each component of this position with the given value.
   *
   * @param n The denominator.
   * @return A new position.
   */
  public Position modulo(final double n) {
    return new Position(Utils.trueModulo(this.x, n), Utils.trueModulo(this.y, n), Utils.trueModulo(this.z, n),
        this.xRelative, this.yRelative, this.zRelative);
  }

  /**
   * Raise each component of this position to the given exponent.
   *
   * @param n The exponent.
   * @return A new position.
   */
  public Position pow(final double n) {
    return new Position(Math.pow(this.x, n), Math.pow(this.y, n), Math.pow(this.z, n),
        this.xRelative, this.yRelative, this.zRelative);
  }

  /**
   * Return a position that is 1 block up.
   *
   * @return A new position.
   */
  public Position up() {
    return this.up(1);
  }

  /**
   * Return a position that is n blocks up.
   *
   * @param n The offset.
   * @return A new position.
   */
  public Position up(final int n) {
    return this.offset(EnumFacing.UP, n);
  }

  /**
   * Return a position that is 1 block down.
   *
   * @return A new position.
   */
  public Position down() {
    return this.down(1);
  }

  /**
   * Return a position that is n blocks down.
   *
   * @param n The offset.
   * @return A new position.
   */
  public Position down(final int n) {
    return this.offset(EnumFacing.DOWN, n);
  }

  /**
   * Return a position that is 1 block to the north.
   *
   * @return A new position.
   */
  public Position north() {
    return this.north(1);
  }

  /**
   * Return a position that is n blocks to the north.
   *
   * @param n The offset.
   * @return A new position.
   */
  public Position north(final int n) {
    return this.offset(EnumFacing.NORTH, n);
  }

  /**
   * Return a position that is 1 block to the south.
   *
   * @return A new position.
   */
  public Position south() {
    return this.south(1);
  }

  /**
   * Return a position that is n blocks to the south.
   *
   * @param n The offset.
   * @return A new position.
   */
  public Position south(final int n) {
    return this.offset(EnumFacing.SOUTH, n);
  }

  /**
   * Return a position that is 1 block to the west.
   *
   * @return A new position.
   */
  public Position west() {
    return this.west(1);
  }

  /**
   * Return a position that is n blocks to the west.
   *
   * @param n The offset.
   * @return A new position.
   */
  public Position west(final int n) {
    return this.offset(EnumFacing.WEST, n);
  }

  /**
   * Return a position that is 1 block to the east.
   *
   * @return A new position.
   */
  public Position east() {
    return this.east(1);
  }

  /**
   * Return a position that is n blocks to the east.
   *
   * @param n The offset.
   * @return A new position.
   */
  public Position east(final int n) {
    return this.offset(EnumFacing.EAST, n);
  }

  /**
   * Return a position that is offset by 1 block in the given direction.
   *
   * @return A new position.
   */
  public Position offset(final EnumFacing facing) {
    return this.offset(facing, 1);
  }

  /**
   * Return a position that is offset by n blocks in the given direction.
   *
   * @param n The offset.
   * @return A new position.
   */
  public Position offset(final EnumFacing facing, final int n) {
    return new Position(
        this.x + facing.getFrontOffsetX() * n,
        this.y + facing.getFrontOffsetY() * n,
        this.z + facing.getFrontOffsetZ() * n,
        this.xRelative, this.yRelative, this.zRelative
    );
  }

  /**
   * Rotate this position by the given amount.
   *
   * @param rotation Rotation angle.
   * @return A new position.
   */
  public Position rotate(final Rotation rotation) {
    switch (rotation) {
      case NONE:
      default:
        return new Position(this);
      case CLOCKWISE_90:
        return new Position(-this.z, this.y, this.x, this.zRelative, this.yRelative, this.xRelative);
      case CLOCKWISE_180:
        return new Position(-this.x, this.y, -this.z);
      case COUNTERCLOCKWISE_90:
        return new Position(this.z, this.y, -this.x, this.zRelative, this.yRelative, this.xRelative);
    }
  }

  /**
   * Return the euclidian distance between this position and the specified one.
   *
   * @param other A position
   * @return The euclidian distance.
   */
  public double getDistance(final Position other) {
    return Math.sqrt(this.getDistanceSq(other));
  }

  /**
   * Return the squared euclidian distance between this position and the specified one.
   *
   * @param other A position
   * @return The squared euclidian distance.
   */
  public double getDistanceSq(final Position other) {
    double d0 = other.getX() - this.getX();
    double d1 = other.getY() - this.getY();
    double d2 = other.getZ() - this.getZ();
    return d0 * d0 + d1 * d1 + d2 * d2;
  }

  /**
   * Convert this position to a {@link BlockPos} instance.
   *
   * @return A {@link BlockPos} object.
   * @throws IllegalStateException If this position has at least one relative component.
   */
  public BlockPos toBlockPos() {
    if (this.xRelative || this.yRelative || this.zRelative) {
      throw new IllegalStateException("cannot convert relative positon to block position");
    }
    return new BlockPos(this.x, this.y, this.z);
  }

  /**
   * Convert this position to a {@link Vec3d} instance.
   *
   * @return A {@link Vec3d} object.
   */
  public Vec3d toVec3d() {
    return new Vec3d(this.x, this.y, this.z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Position that = (Position) o;
    return Double.compare(that.x, this.x) == 0 && Double.compare(that.y, this.y) == 0 && Double.compare(that.z, this.z) == 0
        && this.xRelative == that.xRelative && this.yRelative == that.yRelative && this.zRelative == that.zRelative;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y, this.z, this.xRelative, this.yRelative, this.zRelative);
  }

  @Override
  public int compareTo(Position o) {
    if (this.getY() == o.getY()) {
      return this.getZ() == o.getZ()
          ? Double.compare(this.getX(), o.getX())
          : Double.compare(this.getZ(), o.getZ());
    } else {
      return Double.compare(this.getY(), o.getY());
    }
  }

  @Override
  public String toString() {
    return String.format("(%s, %s, %s)", this.getXCommandRepresentation(), this.getYCommandRepresentation(), this.getZCommandRepresentation());
  }
}
