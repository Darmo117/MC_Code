package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.types.Range;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;

/**
 * Wrapper type for {@link Range} class.
 * <p>
 * Ranges are iterable.
 */
public class RangeType extends Type<Range> {
  public static final String NAME = "range";

  private static final String START_KEY = "Start";
  private static final String END_KEY = "End";
  private static final String STEP_KEY = "Step";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Range> getWrappedType() {
    return Range.class;
  }

  @Override
  public boolean generateCastOperator() {
    return false;
  }

  @Override
  protected Iterator<?> __iter__(final Scope scope, final Range self) {
    return self.iterator();
  }

  @Override
  public Range __copy__(final Scope scope, final Range self) {
    return self.clone();
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Range self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setInteger(START_KEY, self.getStart());
    tag.setInteger(END_KEY, self.getEnd());
    tag.setInteger(STEP_KEY, self.getStep());
    return tag;
  }

  @Override
  public Range readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return new Range(tag.getInteger(START_KEY), tag.getInteger(END_KEY), tag.getInteger(STEP_KEY));
  }
}
