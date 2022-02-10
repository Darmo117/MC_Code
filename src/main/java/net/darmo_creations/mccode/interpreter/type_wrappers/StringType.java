package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.exceptions.IndexOutOfBoundsException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.WorldProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Wrapper type for {@link String} class.
 * <p>
 * Strings are iterable and support the __get_item__ operator.
 */
@Doc("Type representing strings.")
public class StringType extends Type<String> {
  public static final String NAME = "string";

  public static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<String> getWrappedType() {
    return String.class;
  }

  @Method(name = "lower")
  @Doc("Returns a string as lower case.")
  public String toLowerCase(final Scope scope, final String self) {
    return self.toLowerCase();
  }

  @Method(name = "upper")
  @Doc("Returns a string as upper case.")
  public String toUpperCase(final Scope scope, final String self) {
    return self.toUpperCase();
  }

  @Method(name = "title")
  @Doc("Returns a string as title case.")
  public String toTitleCase(final Scope scope, final String self) {
    // Based on https://stackoverflow.com/a/1086134/3779986
    StringBuilder titleCase = new StringBuilder(self.length());
    boolean nextTitleCase = true;

    for (char c : self.toCharArray()) {
      if (Character.isSpaceChar(c)) {
        nextTitleCase = true;
      } else if (nextTitleCase) {
        c = Character.toTitleCase(c);
        nextTitleCase = false;
      } else {
        c = Character.toLowerCase(c);
      }
      titleCase.append(c);
    }

    return titleCase.toString();
  }

  @Method(name = "starts_with")
  @Doc("Returns whether a string starts with the given string.")
  public Boolean startsWith(final Scope scope, final String self, final String prefix) {
    return self.startsWith(prefix);
  }

  @Method(name = "ends_with")
  @Doc("Returns whether a string ends with the given string.")
  public Boolean endsWith(final Scope scope, final String self, final String suffix) {
    return self.endsWith(suffix);
  }

  @Method(name = "count")
  @Doc("Returns the number of times the given string is present in another.")
  public Long count(final Scope scope, final String self, final String needle) {
    if ("".equals(needle)) {
      return (long) self.length() + 1;
    }
    return (long) self.length() - self.replace(needle, "").length();
  }

  @Method(name = "index")
  @Doc("Returns the index of the first occurence of the given string in another, or -1 if no occurence were found.")
  public Long indexOf(final Scope scope, final String self, final String needle) {
    return (long) self.indexOf(needle);
  }

  @Method(name = "strip")
  @Doc("Removes all leading and trailing whitespace from a string.")
  public String trim(final Scope scope, final String self) {
    return self.trim();
  }

  @Method(name = "left_strip")
  @Doc("Removes all leading whitespace from a string.")
  public String trimLeft(final Scope scope, final String self) {
    return self.replaceFirst("^\\s+", "");
  }

  @Method(name = "right_strip")
  @Doc("Removes all trailing whitespace from a string.")
  public String trimRight(final Scope scope, final String self) {
    return self.replaceFirst("\\s+$", "");
  }

  @Method(name = "replace")
  @Doc("Replaces each substring of a string that matches the target string with the specified literal replacement sequence.")
  public String replace(final Scope scope, final String self, final String target, final String replacement) {
    return self.replace(target, replacement);
  }

  @Method(name = "replace_regex")
  @Doc("Replaces each substring of a string that matches the regex string with the specified literal replacement sequence.")
  public String replaceRegex(final Scope scope, final String self, final String target, final String replacement) {
    return self.replaceAll(target, replacement);
  }

  @Method(name = "split")
  @Doc("Splits a string around matches of the given regular expression.")
  public MCList split(final Scope scope, final String self, final String separator) {
    return new MCList(Arrays.asList(self.split(separator, -1)));
  }

  @Method(name = "join")
  @Doc("Joins all strings from the given list using a specified delimiter.")
  public String join(final Scope scope, final String self, final Object collection) {
    MCList list = ProgramManager.getTypeInstance(ListType.class).implicitCast(scope, collection);
    return list.stream().map(e -> this.implicitCast(scope, e)).collect(Collectors.joining(self));
  }

  @Method(name = "format")
  @Doc("Formats a string using the specified arguments.")
  public String format(final Scope scope, final String self, final MCList args) {
    return String.format(self, args.toArray());
  }

  @Override
  protected Object __get_item__(final Scope scope, final String self, final Object key) {
    if (key instanceof Long || key instanceof Boolean) {
      Long index = ProgramManager.getTypeInstance(IntType.class).implicitCast(scope, key);
      if (index < 0 || index >= self.length()) {
        throw new IndexOutOfBoundsException(scope, index.intValue());
      }
      return String.valueOf(self.charAt(index.intValue()));
    }
    return super.__get_item__(scope, self, key);
  }

  @Override
  protected Object __add__(final Scope scope, final String self, final Object o, final boolean inPlace) {
    return self + this.implicitCast(scope, o);
  }

  @Override
  protected Object __mul__(final Scope scope, final String self, final Object o, final boolean inPlace) {
    if (o instanceof Long || o instanceof Boolean) {
      Long nb = ProgramManager.getTypeInstance(IntType.class).implicitCast(scope, o);
      if (nb <= 0) {
        return "";
      }
      StringBuilder s = new StringBuilder(self);
      for (int i = 0; i < nb - 1; i++) {
        s.append(self);
      }
      return s.toString();
    }
    return super.__mul__(scope, self, o, inPlace);
  }

  @Override
  protected Object __eq__(final Scope scope, final String self, final Object o) {
    if (o instanceof String) {
      return self.equals(o);
    } else if (o instanceof ResourceLocation) {
      return self.equals(o.toString());
    }
    return false;
  }

  @Override
  protected Object __gt__(final Scope scope, final String self, final Object o) {
    if (o instanceof String) {
      return self.compareTo((String) o) > 0;
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
  protected long __len__(final Scope scope, final String self) {
    return self.length();
  }

  @Override
  public String implicitCast(final Scope scope, final Object o) {
    // “Override” toString() for some Minecraft classes
    if (o instanceof BlockPos) {
      BlockPos pos = (BlockPos) o;
      return String.format("(%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ());
    }
    if (o instanceof Block) {
      //noinspection ConstantConditions
      return ((Block) o).getRegistryName().toString();
    }
    if (o instanceof Item) {
      //noinspection ConstantConditions
      return ((Item) o).getRegistryName().toString();
    }
    if (o instanceof WorldProxy) {
      return "<this world>";
    }
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
