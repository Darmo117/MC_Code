package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.IndexOutOfBoundsException;
import net.darmo_creations.mccode.interpreter.exceptions.UnsupportedOperatorException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.darmo_creations.mccode.interpreter.types.Range;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class StringTypeTest extends TypeTest<StringType> {
  @Test
  void getName() {
    assertEquals("string", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(String.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames("").isEmpty());
  }

  @Test
  void getUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), "", "a"));
  }

  @Test
  void setUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), "", "a", true));
  }

  @Test
  void lowerCase() {
    assertEquals("ab", this.typeInstance.toLowerCase(this.p.getScope(), "AB"));
    assertEquals("ab", this.typeInstance.toLowerCase(this.p.getScope(), "Ab"));
    assertEquals("ab", this.typeInstance.toLowerCase(this.p.getScope(), "aB"));
    assertEquals("ab", this.typeInstance.toLowerCase(this.p.getScope(), "ab"));
  }

  @Test
  void upperCase() {
    assertEquals("AB", this.typeInstance.toUpperCase(this.p.getScope(), "ab"));
    assertEquals("AB", this.typeInstance.toUpperCase(this.p.getScope(), "aB"));
    assertEquals("AB", this.typeInstance.toUpperCase(this.p.getScope(), "Ab"));
    assertEquals("AB", this.typeInstance.toUpperCase(this.p.getScope(), "AB"));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForTitleCase")
  void titleCase(String expected, String s) {
    assertEquals(expected, this.typeInstance.toTitleCase(this.p.getScope(), s));
  }

  static Stream<Arguments> provideArgsForTitleCase() {
    return Stream.of(
        Arguments.of("Ab Cd", "ab cd"),
        Arguments.of("Ab Cd", "aB cd"),
        Arguments.of("Ab Cd", "Ab cd"),
        Arguments.of("Ab Cd", "AB cd"),
        Arguments.of("Ab Cd", "ab cD"),
        Arguments.of("Ab Cd", "aB cD"),
        Arguments.of("Ab Cd", "Ab cD"),
        Arguments.of("Ab Cd", "AB cD"),
        Arguments.of("Ab Cd", "ab Cd"),
        Arguments.of("Ab Cd", "aB Cd"),
        Arguments.of("Ab Cd", "Ab Cd"),
        Arguments.of("Ab Cd", "AB Cd"),
        Arguments.of("Ab Cd", "ab CD"),
        Arguments.of("Ab Cd", "aB CD"),
        Arguments.of("Ab Cd", "Ab CD"),
        Arguments.of("Ab Cd", "AB CD")
    );
  }

  @Test
  void startsWith() {
    assertEquals(true, this.typeInstance.startsWith(this.p.getScope(), "ab", "a"));
    assertEquals(false, this.typeInstance.startsWith(this.p.getScope(), "ab", "b"));
    assertEquals(true, this.typeInstance.startsWith(this.p.getScope(), "ab", ""));
  }

  @Test
  void endsWith() {
    assertEquals(true, this.typeInstance.endsWith(this.p.getScope(), "ab", "b"));
    assertEquals(false, this.typeInstance.endsWith(this.p.getScope(), "ab", "a"));
    assertEquals(true, this.typeInstance.endsWith(this.p.getScope(), "ab", ""));
  }

  @Test
  void count() {
    assertEquals(2, this.typeInstance.count(this.p.getScope(), "abab", "a"));
    assertEquals(0, this.typeInstance.count(this.p.getScope(), "abab", "c"));
    assertEquals(0, this.typeInstance.count(this.p.getScope(), "", "a"));
    assertEquals(2, this.typeInstance.count(this.p.getScope(), "a", ""));
    assertEquals(5, this.typeInstance.count(this.p.getScope(), "abab", ""));
  }

  @Test
  void index() {
    assertEquals(0, this.typeInstance.indexOf(this.p.getScope(), "abab", "a"));
    assertEquals(1, this.typeInstance.indexOf(this.p.getScope(), "abab", "b"));
    assertEquals(-1, this.typeInstance.indexOf(this.p.getScope(), "abab", "c"));
    assertEquals(-1, this.typeInstance.indexOf(this.p.getScope(), "", "a"));
    assertEquals(0, this.typeInstance.indexOf(this.p.getScope(), "a", ""));
  }

  @Test
  void strip() {
    assertEquals("abab", this.typeInstance.trim(this.p.getScope(), "  abab  "));
    assertEquals("abab", this.typeInstance.trim(this.p.getScope(), "  abab"));
    assertEquals("abab", this.typeInstance.trim(this.p.getScope(), "abab  "));
  }

  @Test
  void leftStrip() {
    assertEquals("abab  ", this.typeInstance.trimLeft(this.p.getScope(), "  abab  "));
    assertEquals("abab", this.typeInstance.trimLeft(this.p.getScope(), "  abab"));
    assertEquals("abab  ", this.typeInstance.trimLeft(this.p.getScope(), "abab  "));
  }

  @Test
  void rightStrip() {
    assertEquals("  abab", this.typeInstance.trimRight(this.p.getScope(), "  abab  "));
    assertEquals("  abab", this.typeInstance.trimRight(this.p.getScope(), "  abab"));
    assertEquals("abab", this.typeInstance.trimRight(this.p.getScope(), "abab  "));
  }

  @Test
  void replace() {
    assertEquals("cbcb", this.typeInstance.replace(this.p.getScope(), "abab", "a", "c"));
    assertEquals("abab", this.typeInstance.replace(this.p.getScope(), "abab", "c", "d"));
    assertEquals("ccd", this.typeInstance.replace(this.p.getScope(), "ababd", "ab", "c"));
  }

  @Test
  void replaceRegex() {
    assertEquals("abcab", this.typeInstance.replaceRegex(this.p.getScope(), "ab1ab", "\\d", "c"));
    assertEquals("ab1ab", this.typeInstance.replaceRegex(this.p.getScope(), "ab1ab", "\\s", "c"));
  }

  @Test
  void split() {
    assertEquals(new MCList(Arrays.asList("a", "a", "a")), this.typeInstance.split(this.p.getScope(), "ababa", "b"));
    assertEquals(new MCList(Arrays.asList("a", "a", "")), this.typeInstance.split(this.p.getScope(), "abab", "b"));
    assertEquals(new MCList(Arrays.asList("", "a", "a")), this.typeInstance.split(this.p.getScope(), "baba", "b"));
    assertEquals(new MCList(Arrays.asList("", "", "a")), this.typeInstance.split(this.p.getScope(), "bba", "b"));
    assertEquals(new MCList(Arrays.asList("a", "a", "a")), this.typeInstance.split(this.p.getScope(), "a1a2a", "\\d"));
  }

  @Test
  void join() {
    assertEquals("a,b,c", this.typeInstance.join(this.p.getScope(), ",", new MCList(Arrays.asList("a", "b", "c"))));
    assertEquals("", this.typeInstance.join(this.p.getScope(), ",", new MCList()));
    assertEquals("a,1,c,1.2", this.typeInstance.join(this.p.getScope(), ",", new MCList(Arrays.asList("a", 1L, "c", 1.2))));
  }

  @Test
  void getUndefinedMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @Test
  void iteration() {
    String s = "abcdef";
    Iterator<?> it = (Iterator<?>) this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, s, null, null, false);
    int i = 0;
    while (it.hasNext()) {
      assertEquals(s.charAt(i) + "", it.next());
      i++;
    }
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, String self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.NOT, "a", false),
        Arguments.of(UnaryOperator.NOT, "", true),

        Arguments.of(UnaryOperator.LENGTH, "", 0L),
        Arguments.of(UnaryOperator.LENGTH, "a", 1L),
        Arguments.of(UnaryOperator.LENGTH, "aa", 2L)
    );
  }

  @Test
  void applyUnaryOperatorError() {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.MINUS, "", null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, String self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, "a", 1L, "a1"),
        Arguments.of(BinaryOperator.PLUS, "a", 1.0, "a1.0"),
        Arguments.of(BinaryOperator.PLUS, "a", true, "atrue"),
        Arguments.of(BinaryOperator.PLUS, "a", false, "afalse"),
        Arguments.of(BinaryOperator.PLUS, "a", "b", "ab"),
//        Arguments.of(BinaryOperator.PLUS, "a", Items.STICK, "aminecraft:stick"), // FIXME not initialized
//        Arguments.of(BinaryOperator.PLUS, "a", Blocks.STONE, "aminecraft:stone"), // FIXME not initialized
        Arguments.of(BinaryOperator.PLUS, "a", null, "anull"),
        Arguments.of(BinaryOperator.PLUS, "a", new MCList(), "a[]"),
        Arguments.of(BinaryOperator.PLUS, "a", new MCList(Arrays.asList(1L, 2L, 3L)), "a[1, 2, 3]"),
        Arguments.of(BinaryOperator.PLUS, "a", new MCSet(), "a{}"),
        Arguments.of(BinaryOperator.PLUS, "a", new MCSet(Collections.singletonList(1L)), "a{1}"),
        Arguments.of(BinaryOperator.PLUS, "a", new MCMap(), "a{}"),
        Arguments.of(BinaryOperator.PLUS, "a", new BlockPos(0, 0, 0), "aBlockPos{x=0, y=0, z=0}"),
        Arguments.of(BinaryOperator.PLUS, "a", new Range(1, 1, 1), "aRange{start=1, end=1, step=1}"),
        Arguments.of(BinaryOperator.PLUS, "a", new ResourceLocation("minecraft:stone"), "aminecraft:stone"),

        Arguments.of(BinaryOperator.MUL, "ab", 0L, ""),
        Arguments.of(BinaryOperator.MUL, "ab", 1L, "ab"),
        Arguments.of(BinaryOperator.MUL, "ab", 2L, "abab"),
        Arguments.of(BinaryOperator.MUL, "ab", true, "ab"),
        Arguments.of(BinaryOperator.MUL, "ab", false, ""),

        Arguments.of(BinaryOperator.EQUAL, "ab", "", false),
        Arguments.of(BinaryOperator.EQUAL, "ab", "ab", true),
        Arguments.of(BinaryOperator.EQUAL, "ab", true, false),
        Arguments.of(BinaryOperator.EQUAL, "ab", false, false),
        Arguments.of(BinaryOperator.EQUAL, "1", 1L, false),
        Arguments.of(BinaryOperator.EQUAL, "0", 0L, false),
        Arguments.of(BinaryOperator.EQUAL, "", 0L, false),
        Arguments.of(BinaryOperator.EQUAL, "1.0", 1.0, false),
        Arguments.of(BinaryOperator.EQUAL, "0.0", 0.0, false),
        Arguments.of(BinaryOperator.EQUAL, "", 0.0, false),
        Arguments.of(BinaryOperator.EQUAL, "null", null, false),
        Arguments.of(BinaryOperator.EQUAL, "", null, false),
//        Arguments.of(BinaryOperator.EQUAL, "minecraft:stick", Items.STICK, false), // FIXME not initialized
        Arguments.of(BinaryOperator.EQUAL, "[]", new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, "{}", new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, "{}", new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, "BlockPos{x=0, y=0, z=0}", new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, "Range{start=1, end=1, step=1}", new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, "minecraft:stone", new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.EQUAL, "minecraft:stick", new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.NOT_EQUAL, "ab", "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "ab", "ab", false),
        Arguments.of(BinaryOperator.NOT_EQUAL, "ab", true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "ab", false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "1", 1L, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "0", 0L, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "", 0L, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "1.0", 1.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "0.0", 0.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "", 0.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "null", null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "", null, true),
//        Arguments.of(BinaryOperator.NOT_EQUAL, "minecraft:stick", Items.STICK, true), // FIXME not initialized
        Arguments.of(BinaryOperator.NOT_EQUAL, "[]", new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "{}", new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "{}", new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "BlockPos{x=0, y=0, z=0}", new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "Range{start=1, end=1, step=1}", new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, "minecraft:stone", new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.NOT_EQUAL, "minecraft:stick", new ResourceLocation("minecraft:stone"), true),

        Arguments.of(BinaryOperator.GT, "a", "a", false),
        Arguments.of(BinaryOperator.GT, "a", "", true),
        Arguments.of(BinaryOperator.GT, "aa", "a", true),
        Arguments.of(BinaryOperator.GT, "ab", "aa", true),
        Arguments.of(BinaryOperator.GT, "", "a", false),
        Arguments.of(BinaryOperator.GT, "a", "ab", false),

        Arguments.of(BinaryOperator.GE, "a", "a", true),
        Arguments.of(BinaryOperator.GE, "a", "", true),
        Arguments.of(BinaryOperator.GE, "aa", "a", true),
        Arguments.of(BinaryOperator.GE, "ab", "aa", true),
        Arguments.of(BinaryOperator.GE, "", "a", false),
        Arguments.of(BinaryOperator.GE, "a", "ab", false),

        Arguments.of(BinaryOperator.LT, "a", "a", false),
        Arguments.of(BinaryOperator.LT, "a", "", false),
        Arguments.of(BinaryOperator.LT, "aa", "a", false),
        Arguments.of(BinaryOperator.LT, "ab", "aa", false),
        Arguments.of(BinaryOperator.LT, "", "a", true),
        Arguments.of(BinaryOperator.LT, "a", "ab", true),

        Arguments.of(BinaryOperator.LE, "a", "a", true),
        Arguments.of(BinaryOperator.LE, "a", "", false),
        Arguments.of(BinaryOperator.LE, "aa", "a", false),
        Arguments.of(BinaryOperator.LE, "ab", "aa", false),
        Arguments.of(BinaryOperator.LE, "", "a", true),
        Arguments.of(BinaryOperator.LE, "a", "ab", true),

        Arguments.of(BinaryOperator.IN, "a", "", true),
        Arguments.of(BinaryOperator.IN, "a", "a", true),
        Arguments.of(BinaryOperator.IN, "a", "b", false),
        Arguments.of(BinaryOperator.IN, "aba", "ab", true),
        Arguments.of(BinaryOperator.IN, "aba", "aa", false),

        Arguments.of(BinaryOperator.NOT_IN, "a", "", false),
        Arguments.of(BinaryOperator.NOT_IN, "a", "a", false),
        Arguments.of(BinaryOperator.NOT_IN, "a", "b", true),
        Arguments.of(BinaryOperator.NOT_IN, "aba", "ab", false),
        Arguments.of(BinaryOperator.NOT_IN, "aba", "aa", true),

        Arguments.of(BinaryOperator.GET_ITEM, "ab", false, "a"),
        Arguments.of(BinaryOperator.GET_ITEM, "ab", true, "b"),
        Arguments.of(BinaryOperator.GET_ITEM, "ab", 0L, "a"),
        Arguments.of(BinaryOperator.GET_ITEM, "ab", 1L, "b")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, String self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.SUB, "", 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.SUB, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.SUB, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, "", 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, "", "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.MUL, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.MUL, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", 0L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.DIV, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.DIV, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", 0L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.INT_DIV, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.INT_DIV, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", 0L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.MOD, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.MOD, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", 0L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.POW, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.POW, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", 0L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GT, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GT, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", 0L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GE, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GE, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", 0L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.LT, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.LT, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, "", false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", 0L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.LE, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.LE, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, "", true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.IN, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.IN, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, "", true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.NOT_IN, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.NOT_IN, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, "", 0L, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "a", 1L, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", -1L, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GET_ITEM, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GET_ITEM, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, "", true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.DEL_ITEM, "", new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.DEL_ITEM, "", null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, "", new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, "a", o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, "a", o, null, false));
    assertEquals("", this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, "", o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    assertEquals("a", this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, "a", o, null, false));
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, "", o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, "", o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyLogicOperator() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(false),
        Arguments.of(1L),
        Arguments.of(0L),
        Arguments.of(1.0),
        Arguments.of(0.0),
        Arguments.of("a"),
        Arguments.of(""),
        Arguments.of(new Item()),
        Arguments.of(new MCList(Collections.singletonList(1L))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1L))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1L))),
        Arguments.of(new MCMap()),
        Arguments.of((Object) null),
        Arguments.of(new ResourceLocation("minecraft:stone")),
        Arguments.of(new BlockPos(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyTernaryOperatorError")
  void applyTernaryOperatorError(TernaryOperator operator, String self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, "", true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", 1L, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", new BlockPos(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, "", new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCast")
  void implicitCast(String expected, Object o) {
    assertEquals(expected, this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCast")
  void explicitCast(String expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForCast() {
    return Stream.of(
        Arguments.of("true", true),
        Arguments.of("false", false),
        Arguments.of("1", 1L),
        Arguments.of("-1", -1L),
        Arguments.of("0", 0L),
        Arguments.of("1.0", 1.0),
        Arguments.of("-1.0", -1.0),
        Arguments.of("0.0", 0.0),
        Arguments.of("a", "a"),
        Arguments.of("", ""),
        Arguments.of("[1]", new MCList(Collections.singletonList(1L))),
        Arguments.of("[]", new MCList()),
        Arguments.of("{1}", new MCSet(Collections.singletonList(1L))),
        Arguments.of("{}", new MCSet()),
        Arguments.of("{a=1}", new MCMap(Collections.singletonMap("a", 1L))),
        Arguments.of("{}", new MCMap()),
//        Arguments.of("minecraft:stick", Items.STICK), // FIXME not initialized
//        Arguments.of("minecraft:stone", Blocks.STONE), // FIXME raises error because of sound system not initialized
        Arguments.of("null", null),
        Arguments.of("BlockPos{x=0, y=0, z=0}", new BlockPos(0, 0, 0)),
        Arguments.of("Range{start=1, end=1, step=1}", new Range(1, 1, 1)),
        Arguments.of("minecraft:stone", new ResourceLocation("minecraft:stone"))
    );
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean("test"));
    assertFalse(this.typeInstance.toBoolean(""));
  }

  @Test
  void copy() {
    assertEquals("test", this.typeInstance.copy(this.p.getScope(), "test"));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(StringType.NAME_KEY, "string");
    tag.setString(StringType.VALUE_KEY, "test");
    assertEquals(tag, this.typeInstance.writeToNBT("test"));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(StringType.NAME_KEY, "string");
    tag.setString(StringType.VALUE_KEY, "test");
    assertEquals("test", this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<StringType> getTypeClass() {
    return StringType.class;
  }
}
