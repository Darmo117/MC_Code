package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.IndexOutOfBoundsException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Wrapper type for {@link String} class.
 * <p>
 * Strings are iterable and support the __get_item__ operator.
 */
public class StringType extends Type<String> {
  public static final String NAME = "string";

  private static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<String> getWrappedType() {
    return String.class;
  }

  @Method(name = "lower")
  @Doc("Returns this string as lower case.")
  public String toLowerCase(final Scope scope, final String self) {
    return self.toLowerCase();
  }

  @Method(name = "upper")
  @Doc("Returns this string as upper case.")
  public String toUpperCase(final Scope scope, final String self) {
    return self.toUpperCase();
  }

  @Method(name = "title")
  @Doc("Returns this string as title case.")
  public String toTitleCase(final Scope scope, final String self) {
    // Code from https://stackoverflow.com/a/1086134/3779986
    StringBuilder titleCase = new StringBuilder(self.length());
    boolean nextTitleCase = true;

    for (char c : self.toCharArray()) {
      if (Character.isSpaceChar(c)) {
        nextTitleCase = true;
      } else if (nextTitleCase) {
        char oldC = c;
        c = Character.toTitleCase(c);
        nextTitleCase = oldC == c;
      }

      titleCase.append(c);
    }

    return titleCase.toString();
  }

  @Method(name = "starts_with")
  @Doc("Returns whether this string starts with the given string.")
  public Boolean startsWith(final Scope scope, final String self, final String prefix) {
    return self.startsWith(prefix);
  }

  @Method(name = "ends_with")
  @Doc("Returns whether this string ends with the given string.")
  public Boolean endsWith(final Scope scope, final String self, final String suffix) {
    return self.endsWith(suffix);
  }

  @Method(name = "count")
  @Doc("Returns the number of times the given string is present in this string.")
  public Integer count(final Scope scope, final String self, final String needle) {
    return self.length() - self.replace(needle, "").length();
  }

  @Method(name = "index")
  @Doc("Returns the index of the first occurence of the given string in this string.")
  public Integer indexOf(final Scope scope, final String self, final String needle) {
    return self.indexOf(needle);
  }

  @Method(name = "strip")
  @Doc("Removes all leading and trailing whitespace from this string.")
  public String trim(final Scope scope, final String self) {
    return self.trim();
  }

  @Method(name = "left_strip")
  @Doc("Removes all leading whitespace from this string.")
  public String trimLeft(final Scope scope, final String self) {
    return self.replaceFirst("^\\s+", "");
  }

  @Method(name = "right_strip")
  @Doc("Removes all trailing whitespace from this string.")
  public String trimRight(final Scope scope, final String self) {
    return self.replaceFirst("\\s+$", "");
  }

  @Method(name = "replace")
  @Doc("Replaces each substring of this string that matches the target string with the specified literal replacement sequence.")
  public String replace(final Scope scope, final String self, final String target, final String replacement) {
    return self.replace(target, replacement);
  }

  @Method(name = "split")
  @Doc("Splits this string around matches of the given regular expression.")
  public MCList split(final Scope scope, final String self, final String separator) {
    return new MCList(Arrays.asList(self.split(separator)));
  }

  @Method(name = "join")
  @Doc("Joins all strings from the given list using this string as a delimiter.")
  public String join(final Scope scope, final String self, final Object collection) {
    MCList list = ProgramManager.getTypeInstance(ListType.class).implicitCast(scope, collection);
    return list.stream().map(e -> this.implicitCast(scope, e)).collect(Collectors.joining(self));
  }

  @Override
  protected Object __get_item__(final Scope scope, final String self, final Object key) {
    if (!(key instanceof Integer)) {
      throw new CastException(scope, ProgramManager.getTypeInstance(IntType.class),
          ProgramManager.getTypeForValue(key));
    }
    int index = (Integer) key;
    if (index < 0 || index > self.length()) {
      throw new IndexOutOfBoundsException(scope, index);
    }
    return String.valueOf(self.charAt(index));
  }

  @Override
  protected Object __add__(final Scope scope, final String self, final Object o, final boolean inPlace) {
    return self + this.implicitCast(scope, o);
  }

  @Override
  protected Object __mul__(final Scope scope, final String self, final Object o, final boolean inPlace) {
    int nb = ProgramManager.getTypeInstance(IntType.class).implicitCast(scope, o);
    if (nb <= 0) {
      return "";
    }
    StringBuilder s = new StringBuilder(self);
    for (int i = 0; i < nb - 1; i++) {
      s.append(self);
    }
    return s.toString();
  }

  @Override
  protected Object __eq__(final Scope scope, final String self, final Object o) {
    if (!(o instanceof String)) {
      return false;
    }
    return self.equals(o);
  }

  @Override
  protected Object __gt__(final Scope scope, final String self, final Object o) {
    if (o instanceof String) {
      String other = (String) o;
      if (self.length() > other.length()) {
        return true;
      } else if (self.length() == other.length()) {
        for (int i = 0; i < self.length(); i++) {
          if (self.charAt(i) != other.charAt(i)) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    return super.__gt__(scope, self, o);
  }

  @Override
  protected Object __in__(final Scope scope, final String self, final Object o) {
    if (o instanceof String) {
      return self.contains((String) o);
    }
    return super.__in__(scope, self, o);
  }

  @Override
  protected boolean __bool__(final String self) {
    return self.length() != 0;
  }

  @Override
  protected Iterator<?> __iter__(final Scope scope, final String self) {
    char[] chars = new char[self.length()];
    self.getChars(0, self.length(), chars, 0);
    return new Iterator<Object>() {
      private final char[] characters = chars;
      private int i;

      @Override
      public boolean hasNext() {
        return this.i < this.characters.length;
      }

      @Override
      public Object next() {
        return String.valueOf(this.characters[this.i++]);
      }
    };
  }

  @Override
  protected int __len__(final Scope scope, final String self) {
    return self.length();
  }

  @Override
  public String implicitCast(final Scope scope, final Object o) {
    return String.valueOf(o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final String self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setString(VALUE_KEY, self);
    return tag;
  }

  @Override
  public String readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getString(VALUE_KEY);
  }
}
