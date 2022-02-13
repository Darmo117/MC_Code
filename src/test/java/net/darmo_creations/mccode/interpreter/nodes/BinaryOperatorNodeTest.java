package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.TestUtils;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.darmo_creations.mccode.interpreter.types.Position;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class BinaryOperatorNodeTest extends NodeTest {
  @ParameterizedTest
  @MethodSource("provideArgsForEvaluateValidOperands")
  void evaluateValidOperands(BinaryOperator operator, Node left, Node right, Object expectedValue) {
    Object r = new BinaryOperatorNode(operator, left, right, 0, 0).evaluate(this.p.getScope());
    assertSame(expectedValue.getClass(), r.getClass());
    assertEquals(expectedValue, r);
  }

  private static Stream<Arguments> provideArgsForEvaluateValidOperands() {
    MCMap map = new MCMap();
    map.put("a", 1L);
    map.put("b", 2L);

    // TODO update once all type wrapper tests are written
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new IntLiteralNode(1, 0, 0), new IntLiteralNode(1, 0, 0), 2L),
        Arguments.of(BinaryOperator.PLUS, new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(1.0, 0, 0), 2.0),
        Arguments.of(BinaryOperator.PLUS, new IntLiteralNode(1, 0, 0), new FloatLiteralNode(1.0, 0, 0), 2.0),
        Arguments.of(BinaryOperator.PLUS, new FloatLiteralNode(1.0, 0, 0), new IntLiteralNode(1, 0, 0), 2.0),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(true, 0, 0), new BooleanLiteralNode(true, 0, 0), 2L),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(false, 0, 0), new BooleanLiteralNode(false, 0, 0), 0L),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(true, 0, 0), new IntLiteralNode(1, 0, 0), 2L),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(false, 0, 0), new IntLiteralNode(1, 0, 0), 1L),
        Arguments.of(BinaryOperator.PLUS, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(true, 0, 0), 2L),
        Arguments.of(BinaryOperator.PLUS, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(false, 0, 0), 1L),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(true, 0, 0), new FloatLiteralNode(1.0, 0, 0), 2.0),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(false, 0, 0), new FloatLiteralNode(1.0, 0, 0), 1.0),
        Arguments.of(BinaryOperator.PLUS, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(true, 0, 0), 2.0),
        Arguments.of(BinaryOperator.PLUS, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(false, 0, 0), 1.0),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.blockPosNode(1, 2, 3),
            TestUtils.blockPosNode(1, 2, 3),
            new Position(2, 4, 6)),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeList(new IntLiteralNode(1, 0, 0)),
            TestUtils.nodeList(new IntLiteralNode(2, 0, 0)),
            new MCList(Arrays.asList(1L, 2L))),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeSet(new IntLiteralNode(1, 0, 0)),
            TestUtils.nodeSet(new IntLiteralNode(2, 0, 0)),
            new MCSet(Arrays.asList(1L, 2L))),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeSet(new IntLiteralNode(1, 0, 0)),
            TestUtils.nodeSet(new IntLiteralNode(1, 0, 0)),
            new MCSet(Collections.singletonList(1L))),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeMap("a", new IntLiteralNode(1, 0, 0)),
            TestUtils.nodeMap("b", new IntLiteralNode(2, 0, 0)),
            map),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeMap("a", new IntLiteralNode(1, 0, 0)),
            TestUtils.nodeMap("a", new IntLiteralNode(2, 0, 0)),
            new MCMap(Collections.singletonMap("a", 2L))),

        Arguments.of(BinaryOperator.SUB, new IntLiteralNode(1, 0, 0), new IntLiteralNode(1, 0, 0), 0L),
        Arguments.of(BinaryOperator.SUB, new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0.0),
        Arguments.of(BinaryOperator.SUB, new IntLiteralNode(1, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0.0),
        Arguments.of(BinaryOperator.SUB, new FloatLiteralNode(1.0, 0, 0), new IntLiteralNode(1, 0, 0), 0.0),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(true, 0, 0), new BooleanLiteralNode(true, 0, 0), 0L),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(false, 0, 0), new BooleanLiteralNode(false, 0, 0), 0L),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(true, 0, 0), new IntLiteralNode(1, 0, 0), 0L),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(false, 0, 0), new IntLiteralNode(1, 0, 0), -1L),
        Arguments.of(BinaryOperator.SUB, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(true, 0, 0), 0L),
        Arguments.of(BinaryOperator.SUB, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(false, 0, 0), 1L),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(true, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0.0),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(false, 0, 0), new FloatLiteralNode(1.0, 0, 0), -1.0),
        Arguments.of(BinaryOperator.SUB, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(true, 0, 0), 0.0),
        Arguments.of(BinaryOperator.SUB, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(false, 0, 0), 1.0),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.blockPosNode(1, 2, 3),
            TestUtils.blockPosNode(3, 2, 1),
            new Position(-2, 0, 2)),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0)),
            TestUtils.nodeList(new IntLiteralNode(1, 0, 0)),
            new MCList(Collections.singletonList(3L))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0)),
            TestUtils.nodeList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(2, 0, 0)),
            new MCList(Collections.singletonList(3L))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0)),
            TestUtils.nodeList(new IntLiteralNode(2, 0, 0)),
            new MCList(Arrays.asList(1L, 3L))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0), new IntLiteralNode(1, 0, 0)),
            TestUtils.nodeList(new IntLiteralNode(1, 0, 0)),
            new MCList(Collections.singletonList(3L))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeSet(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0)),
            TestUtils.nodeSet(new IntLiteralNode(1, 0, 0)),
            new MCSet(Collections.singletonList(3L))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeSet(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0)),
            TestUtils.nodeSet(new IntLiteralNode(1, 0, 0), new IntLiteralNode(2, 0, 0)),
            new MCSet(Collections.singletonList(3L))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeSet(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0)),
            TestUtils.nodeSet(new IntLiteralNode(2, 0, 0)),
            new MCSet(Arrays.asList(1L, 3L))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeMap("a", new IntLiteralNode(1, 0, 0)),
            TestUtils.nodeMap("b", new IntLiteralNode(2, 0, 0)),
            new MCMap(Collections.singletonMap("a", 1L))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeMap("a", new IntLiteralNode(1, 0, 0)),
            TestUtils.nodeMap("a", new IntLiteralNode(2, 0, 0)),
            new MCMap(Collections.emptyMap())),

        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(2, 0, 0), new IntLiteralNode(2, 0, 0), 4L),
        Arguments.of(BinaryOperator.MUL, new FloatLiteralNode(2.0, 0, 0), new FloatLiteralNode(2.0, 0, 0), 4.0),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(2, 0, 0), new FloatLiteralNode(2.0, 0, 0), 4.0),
        Arguments.of(BinaryOperator.MUL, new FloatLiteralNode(2.0, 0, 0), new IntLiteralNode(2, 0, 0), 4.0),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(true, 0, 0), new BooleanLiteralNode(true, 0, 0), 1L),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(false, 0, 0), new BooleanLiteralNode(false, 0, 0), 0L),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(true, 0, 0), new IntLiteralNode(1, 0, 0), 1L),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(false, 0, 0), new IntLiteralNode(1, 0, 0), 0L),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(true, 0, 0), 1L),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(false, 0, 0), 0L),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(true, 0, 0), new FloatLiteralNode(1.0, 0, 0), 1.0),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(false, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0.0),
        Arguments.of(BinaryOperator.MUL, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(true, 0, 0), 1.0),
        Arguments.of(BinaryOperator.MUL, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(false, 0, 0), 0.0),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(2, 0, 0), new StringLiteralNode("ab", 0, 0), "abab"),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(0, 0, 0), new StringLiteralNode("ab", 0, 0), ""),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(true, 0, 0), new StringLiteralNode("ab", 0, 0), "ab"),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(false, 0, 0), new StringLiteralNode("a", 0, 0), ""),
        Arguments.of(BinaryOperator.MUL, new StringLiteralNode("ab", 0, 0), new IntLiteralNode(2, 0, 0), "abab"),
        Arguments.of(BinaryOperator.MUL, new StringLiteralNode("ab", 0, 0), new IntLiteralNode(0, 0, 0), ""),
        Arguments.of(BinaryOperator.MUL, new StringLiteralNode("ab", 0, 0), new BooleanLiteralNode(true, 0, 0), "ab"),
        Arguments.of(BinaryOperator.MUL, new StringLiteralNode("ab", 0, 0), new BooleanLiteralNode(false, 0, 0), ""),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.blockPosNode(1, 2, 3),
            new IntLiteralNode(2, 0, 0),
            new Position(2, 4, 6)),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.blockPosNode(1, 2, 3),
            new FloatLiteralNode(2.5, 0, 0),
            new Position(2, 5, 7)),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(true, 0, 0),
            new Position(1, 2, 3)),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(false, 0, 0),
            new Position(0, 0, 0)),
        Arguments.of(BinaryOperator.MUL,
            new IntLiteralNode(2, 0, 0),
            TestUtils.blockPosNode(1, 2, 3),
            new Position(2, 4, 6)),
        Arguments.of(BinaryOperator.MUL,
            new FloatLiteralNode(2.5, 0, 0),
            TestUtils.blockPosNode(1, 2, 3),
            new Position(2, 5, 7)),
        Arguments.of(BinaryOperator.MUL,
            new BooleanLiteralNode(true, 0, 0),
            TestUtils.blockPosNode(1, 2, 3),
            new Position(1, 2, 3)),
        Arguments.of(BinaryOperator.MUL,
            new BooleanLiteralNode(false, 0, 0),
            TestUtils.blockPosNode(1, 2, 3),
            new Position(0, 0, 0)),
        Arguments.of(BinaryOperator.MUL,
            new IntLiteralNode(2, 0, 0),
            TestUtils.nodeList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(2.0, 0, 0)),
            new MCList(Arrays.asList(1.0, 2.0, 1.0, 2.0))),
        Arguments.of(BinaryOperator.MUL,
            new IntLiteralNode(0, 0, 0),
            TestUtils.nodeList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(2.0, 0, 0)),
            new MCList()),
        Arguments.of(BinaryOperator.MUL,
            new BooleanLiteralNode(true, 0, 0),
            TestUtils.nodeList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(2.0, 0, 0)),
            new MCList(Arrays.asList(1.0, 2.0))),
        Arguments.of(BinaryOperator.MUL,
            new BooleanLiteralNode(false, 0, 0),
            TestUtils.nodeList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(2.0, 0, 0)),
            new MCList()),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.nodeList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(2.0, 0, 0)),
            new IntLiteralNode(2, 0, 0),
            new MCList(Arrays.asList(1.0, 2.0, 1.0, 2.0))),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.nodeList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(2.0, 0, 0)),
            new IntLiteralNode(0, 0, 0),
            new MCList()),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.nodeList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(2.0, 0, 0)),
            new BooleanLiteralNode(true, 0, 0),
            new MCList(Arrays.asList(1.0, 2.0))),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.nodeList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(2.0, 0, 0)),
            new BooleanLiteralNode(false, 0, 0),
            new MCList()),

        Arguments.of(BinaryOperator.DIV, new IntLiteralNode(2, 0, 0), new IntLiteralNode(2, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV, new FloatLiteralNode(2.0, 0, 0), new FloatLiteralNode(2.0, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV, new IntLiteralNode(2, 0, 0), new FloatLiteralNode(2.0, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV, new FloatLiteralNode(2.0, 0, 0), new IntLiteralNode(2, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(true, 0, 0), new BooleanLiteralNode(true, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(true, 0, 0), new IntLiteralNode(1, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(false, 0, 0), new IntLiteralNode(1, 0, 0), 0.0),
        Arguments.of(BinaryOperator.DIV, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(true, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(true, 0, 0), new FloatLiteralNode(1.0, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(false, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0.0),
        Arguments.of(BinaryOperator.DIV, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(true, 0, 0), 1.0),
        Arguments.of(BinaryOperator.DIV,
            TestUtils.blockPosNode(2, 4, 6),
            new IntLiteralNode(2, 0, 0),
            new Position(1, 2, 3)),
        Arguments.of(BinaryOperator.DIV,
            TestUtils.blockPosNode(2, 5, 7),
            new FloatLiteralNode(2.5, 0, 0),
            new Position(0, 2, 2)),
        Arguments.of(BinaryOperator.DIV,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(true, 0, 0),
            new Position(1, 2, 3)),

        // TODO test div by 0
        Arguments.of(BinaryOperator.INT_DIV, new IntLiteralNode(3, 0, 0), new IntLiteralNode(2, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV, new FloatLiteralNode(3.0, 0, 0), new FloatLiteralNode(2.0, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV, new IntLiteralNode(3, 0, 0), new FloatLiteralNode(2.0, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV, new FloatLiteralNode(3.0, 0, 0), new IntLiteralNode(2, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(true, 0, 0), new BooleanLiteralNode(true, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(true, 0, 0), new IntLiteralNode(1, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(false, 0, 0), new IntLiteralNode(1, 0, 0), 0L),
        Arguments.of(BinaryOperator.INT_DIV, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(true, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(true, 0, 0), new FloatLiteralNode(1.0, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(false, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0L),
        Arguments.of(BinaryOperator.INT_DIV, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(true, 0, 0), 1L),
        Arguments.of(BinaryOperator.INT_DIV,
            TestUtils.blockPosNode(2, 4, 6),
            new IntLiteralNode(2, 0, 0),
            new Position(1, 2, 3)),
        Arguments.of(BinaryOperator.INT_DIV,
            TestUtils.blockPosNode(2, 5, 7),
            new FloatLiteralNode(2.5, 0, 0),
            new Position(0, 2, 2)),
        Arguments.of(BinaryOperator.INT_DIV,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(true, 0, 0),
            new Position(1, 2, 3)),

        // TODO test div by 0
        Arguments.of(BinaryOperator.MOD, new IntLiteralNode(3, 0, 0), new IntLiteralNode(2, 0, 0), 1L),
        Arguments.of(BinaryOperator.MOD, new FloatLiteralNode(3.0, 0, 0), new FloatLiteralNode(2.0, 0, 0), 1.0),
        Arguments.of(BinaryOperator.MOD, new IntLiteralNode(3, 0, 0), new FloatLiteralNode(2.0, 0, 0), 1.0),
        Arguments.of(BinaryOperator.MOD, new FloatLiteralNode(3.0, 0, 0), new IntLiteralNode(2, 0, 0), 1.0),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(true, 0, 0), new BooleanLiteralNode(true, 0, 0), 0L),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(true, 0, 0), new IntLiteralNode(1, 0, 0), 0L),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(false, 0, 0), new IntLiteralNode(1, 0, 0), 0L),
        Arguments.of(BinaryOperator.MOD, new IntLiteralNode(1, 0, 0), new BooleanLiteralNode(true, 0, 0), 0L),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(true, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0.0),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(false, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0.0),
        Arguments.of(BinaryOperator.MOD, new FloatLiteralNode(1.0, 0, 0), new BooleanLiteralNode(true, 0, 0), 0.0),
        Arguments.of(BinaryOperator.MOD,
            TestUtils.blockPosNode(2, 4, 6),
            new IntLiteralNode(3, 0, 0),
            new Position(2, 1, 0)),
        Arguments.of(BinaryOperator.MOD,
            TestUtils.blockPosNode(2, 4, 6),
            new FloatLiteralNode(3.0, 0, 0),
            new Position(2, 1, 0)),
        Arguments.of(BinaryOperator.MOD,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(true, 0, 0),
            new Position(0, 0, 0))

        // TODO comparison, logic, collections
    );
  }

  @Test
  void evaluateNullOperatorError() {
    //noinspection ConstantConditions
    assertThrows(NullPointerException.class, () -> new BinaryOperatorNode(null, new IntLiteralNode(1, 0, 0), new IntLiteralNode(1, 0, 0), 0, 0));
    assertThrows(NullPointerException.class, () -> new BinaryOperatorNode(BinaryOperator.PLUS, null, new IntLiteralNode(1, 0, 0), 0, 0));
    assertThrows(NullPointerException.class, () -> new BinaryOperatorNode(BinaryOperator.PLUS, new IntLiteralNode(1, 0, 0), null, 0, 0));
  }

  @ParameterizedTest
  @EnumSource(BinaryOperator.class)
  void writeToNBT(BinaryOperator operator) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BinaryOperatorNode.ID_KEY, BinaryOperatorNode.ID);
    tag.setString(BinaryOperatorNode.SYMBOL_KEY, operator.getSymbol());
    NBTTagList args = new NBTTagList();
    args.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    args.appendTag(new IntLiteralNode(2, 0, 0).writeToNBT());
    tag.setTag(BinaryOperatorNode.ARGUMENTS_KEY, args);
    assertEquals(tag, new BinaryOperatorNode(operator, new IntLiteralNode(1, 0, 0), new IntLiteralNode(2, 0, 0), 0, 0).writeToNBT());
  }

  @ParameterizedTest
  @EnumSource(BinaryOperator.class)
  void deserializeNBT(BinaryOperator operator) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BinaryOperatorNode.ID_KEY, BinaryOperatorNode.ID);
    tag.setString(BinaryOperatorNode.SYMBOL_KEY, operator.getSymbol());
    NBTTagList args = new NBTTagList();
    args.appendTag(new VariableNode("a", 0, 0).writeToNBT());
    args.appendTag(new VariableNode("b", 0, 0).writeToNBT());
    tag.setTag(BinaryOperatorNode.ARGUMENTS_KEY, args);
    assertEquals(String.format("a %s b", operator.getSymbol()), new BinaryOperatorNode(tag).toString());
  }

  @ParameterizedTest
  @EnumSource(BinaryOperator.class)
  void testToString(BinaryOperator operator) {
    assertEquals(String.format("a %s b", operator.getSymbol()),
        new BinaryOperatorNode(operator, new VariableNode("a", 0, 0), new VariableNode("b", 0, 0), 0, 0).toString());
  }

  @Test
  void testEquals() {
    assertEquals(new BinaryOperatorNode(BinaryOperator.PLUS, new IntLiteralNode(1, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0, 0),
        new BinaryOperatorNode(BinaryOperator.PLUS, new IntLiteralNode(1, 0, 0), new FloatLiteralNode(1.0, 0, 0), 0, 0));
  }
}
