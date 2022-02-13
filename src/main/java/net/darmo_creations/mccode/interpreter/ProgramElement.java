package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.exceptions.MathException;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.exceptions.WrappedException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Supplier;

/**
 * A program element is a component of a program’s syntax tree.
 * <p>
 * Program elements can be serialized to NBT tags.
 */
public abstract class ProgramElement implements NBTSerializable {
  /**
   * NBT tag key of ID property.
   */
  public static final String ID_KEY = "ElementID";
  public static final String LINE_KEY = "Line";
  public static final String COLUMN_KEY = "Column";

  private final int line;
  private final int column;

  /**
   * Create a program element.
   *
   * @param line   The line this program element starts on.
   * @param column The column in the line this program element starts at.
   */
  public ProgramElement(final int line, final int column) {
    this.line = line;
    this.column = column;
  }

  /**
   * Create a program element from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public ProgramElement(final NBTTagCompound tag) {
    this.line = tag.getInteger(LINE_KEY);
    this.column = tag.getInteger(COLUMN_KEY);
  }

  /**
   * Return the line this node starts on.
   */
  public int getLine() {
    return this.line;
  }

  /**
   * Return the column in the line this node starts at.
   */
  public int getColumn() {
    return this.column;
  }

  /**
   * Return the ID of this element.
   * Used to serialize this element; must be unique to each concrete subclass.
   */
  public abstract int getID();

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ID_KEY, this.getID());
    tag.setInteger(LINE_KEY, this.getLine());
    tag.setInteger(COLUMN_KEY, this.getColumn());
    return tag;
  }

  @Override
  public abstract String toString();

  @Override
  public abstract boolean equals(Object o);

  @Override
  public abstract int hashCode();

  /**
   * Wraps any error in a {@link MCCodeRuntimeException} or {@link SyntaxErrorException},
   * adding the line and column number of this element if missing.
   *
   * @param scope    The current scope.
   * @param supplier Code to catch any error from.
   * @return A value.
   */
  protected <T> T wrapErrors(final Scope scope, final Supplier<T> supplier)
      throws MCCodeRuntimeException, SyntaxErrorException {
    try {
      return supplier.get();
    } catch (SyntaxErrorException | WrappedException e) {
      throw e; // Explicit rethrow to not get caught by last catch clause
    } catch (MCCodeRuntimeException e) {
      if (e.getLine() == -1 || e.getColumn() == -1) {
        e.setLine(this.getLine());
        e.setColumn(this.getColumn());
      }
      throw e;
    } catch (ArithmeticException e) {
      throw new MathException(scope, this.getLine(), this.getColumn(), e.getMessage());
    } catch (Throwable e) {
      // Wrap any other exception to prevent them from being caught by try-except statements
      throw new WrappedException(e, this.getLine(), this.getColumn(),
          "mccode.interpreter.error.exception", e.getClass().getSimpleName(), e.getMessage());
    }
  }
}
