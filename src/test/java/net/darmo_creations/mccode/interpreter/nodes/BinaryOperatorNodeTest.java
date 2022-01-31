package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.TestUtils;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
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
    Object r = new BinaryOperatorNode(operator, left, right).evaluate(this.p.getScope());
    assertSame(expectedValue.getClass(), r.getClass());
    assertEquals(expectedValue, r);
  }

  private static Stream<Arguments> provideArgsForEvaluateValidOperands() {
    MCMap map = new MCMap();
    map.put("a", 1);
    map.put("b", 2);

    // TODO update once all type wrapper tests are written
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new IntLiteralNode(1), new IntLiteralNode(1), 2),
        Arguments.of(BinaryOperator.PLUS, new FloatLiteralNode(1.0), new FloatLiteralNode(1.0), 2.0),
        Arguments.of(BinaryOperator.PLUS, new IntLiteralNode(1), new FloatLiteralNode(1.0), 2.0),
        Arguments.of(BinaryOperator.PLUS, new FloatLiteralNode(1.0), new IntLiteralNode(1), 2.0),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(true), new BooleanLiteralNode(true), 2),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(false), new BooleanLiteralNode(false), 0),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(true), new IntLiteralNode(1), 2),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(false), new IntLiteralNode(1), 1),
        Arguments.of(BinaryOperator.PLUS, new IntLiteralNode(1), new BooleanLiteralNode(true), 2),
        Arguments.of(BinaryOperator.PLUS, new IntLiteralNode(1), new BooleanLiteralNode(false), 1),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(true), new FloatLiteralNode(1.0), 2.0),
        Arguments.of(BinaryOperator.PLUS, new BooleanLiteralNode(false), new FloatLiteralNode(1.0), 1.0),
        Arguments.of(BinaryOperator.PLUS, new FloatLiteralNode(1.0), new BooleanLiteralNode(true), 2.0),
        Arguments.of(BinaryOperator.PLUS, new FloatLiteralNode(1.0), new BooleanLiteralNode(false), 1.0),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.blockPosNode(1, 2, 3),
            TestUtils.blockPosNode(1, 2, 3),
            new BlockPos(2, 4, 6)),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeList(new IntLiteralNode(1)),
            TestUtils.nodeList(new IntLiteralNode(2)),
            new MCList(Arrays.asList(1, 2))),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeSet(new IntLiteralNode(1)),
            TestUtils.nodeSet(new IntLiteralNode(2)),
            new MCSet(Arrays.asList(1, 2))),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeSet(new IntLiteralNode(1)),
            TestUtils.nodeSet(new IntLiteralNode(1)),
            new MCSet(Collections.singletonList(1))),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeMap("a", new IntLiteralNode(1)),
            TestUtils.nodeMap("b", new IntLiteralNode(2)),
            map),
        Arguments.of(BinaryOperator.PLUS,
            TestUtils.nodeMap("a", new IntLiteralNode(1)),
            TestUtils.nodeMap("a", new IntLiteralNode(2)),
            new MCMap(Collections.singletonMap("a", 2))),

        Arguments.of(BinaryOperator.SUB, new IntLiteralNode(1), new IntLiteralNode(1), 0),
        Arguments.of(BinaryOperator.SUB, new FloatLiteralNode(1.0), new FloatLiteralNode(1.0), 0.0),
        Arguments.of(BinaryOperator.SUB, new IntLiteralNode(1), new FloatLiteralNode(1.0), 0.0),
        Arguments.of(BinaryOperator.SUB, new FloatLiteralNode(1.0), new IntLiteralNode(1), 0.0),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(true), new BooleanLiteralNode(true), 0),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(false), new BooleanLiteralNode(false), 0),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(true), new IntLiteralNode(1), 0),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(false), new IntLiteralNode(1), -1),
        Arguments.of(BinaryOperator.SUB, new IntLiteralNode(1), new BooleanLiteralNode(true), 0),
        Arguments.of(BinaryOperator.SUB, new IntLiteralNode(1), new BooleanLiteralNode(false), 1),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(true), new FloatLiteralNode(1.0), 0.0),
        Arguments.of(BinaryOperator.SUB, new BooleanLiteralNode(false), new FloatLiteralNode(1.0), -1.0),
        Arguments.of(BinaryOperator.SUB, new FloatLiteralNode(1.0), new BooleanLiteralNode(true), 0.0),
        Arguments.of(BinaryOperator.SUB, new FloatLiteralNode(1.0), new BooleanLiteralNode(false), 1.0),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.blockPosNode(1, 2, 3),
            TestUtils.blockPosNode(3, 2, 1),
            new BlockPos(-2, 0, 2)),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeList(new IntLiteralNode(1), new IntLiteralNode(3)),
            TestUtils.nodeList(new IntLiteralNode(1)),
            new MCList(Collections.singletonList(3))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeList(new IntLiteralNode(1), new IntLiteralNode(3)),
            TestUtils.nodeList(new IntLiteralNode(1), new IntLiteralNode(2)),
            new MCList(Collections.singletonList(3))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeList(new IntLiteralNode(1), new IntLiteralNode(3)),
            TestUtils.nodeList(new IntLiteralNode(2)),
            new MCList(Arrays.asList(1, 3))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1)),
            TestUtils.nodeList(new IntLiteralNode(1)),
            new MCList(Collections.singletonList(3))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeSet(new IntLiteralNode(1), new IntLiteralNode(3)),
            TestUtils.nodeSet(new IntLiteralNode(1)),
            new MCSet(Collections.singletonList(3))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeSet(new IntLiteralNode(1), new IntLiteralNode(3)),
            TestUtils.nodeSet(new IntLiteralNode(1), new IntLiteralNode(2)),
            new MCSet(Collections.singletonList(3))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeSet(new IntLiteralNode(1), new IntLiteralNode(3)),
            TestUtils.nodeSet(new IntLiteralNode(2)),
            new MCSet(Arrays.asList(1, 3))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeMap("a", new IntLiteralNode(1)),
            TestUtils.nodeMap("b", new IntLiteralNode(2)),
            new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(BinaryOperator.SUB,
            TestUtils.nodeMap("a", new IntLiteralNode(1)),
            TestUtils.nodeMap("a", new IntLiteralNode(2)),
            new MCMap(Collections.emptyMap())),

        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(2), new IntLiteralNode(2), 4),
        Arguments.of(BinaryOperator.MUL, new FloatLiteralNode(2.0), new FloatLiteralNode(2.0), 4.0),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(2), new FloatLiteralNode(2.0), 4.0),
        Arguments.of(BinaryOperator.MUL, new FloatLiteralNode(2.0), new IntLiteralNode(2), 4.0),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(true), new BooleanLiteralNode(true), 1),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(false), new BooleanLiteralNode(false), 0),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(true), new IntLiteralNode(1), 1),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(false), new IntLiteralNode(1), 0),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(1), new BooleanLiteralNode(true), 1),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(1), new BooleanLiteralNode(false), 0),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(true), new FloatLiteralNode(1.0), 1.0),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(false), new FloatLiteralNode(1.0), 0.0),
        Arguments.of(BinaryOperator.MUL, new FloatLiteralNode(1.0), new BooleanLiteralNode(true), 1.0),
        Arguments.of(BinaryOperator.MUL, new FloatLiteralNode(1.0), new BooleanLiteralNode(false), 0.0),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(2), new StringLiteralNode("ab"), "abab"),
        Arguments.of(BinaryOperator.MUL, new IntLiteralNode(0), new StringLiteralNode("ab"), ""),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(true), new StringLiteralNode("ab"), "ab"),
        Arguments.of(BinaryOperator.MUL, new BooleanLiteralNode(false), new StringLiteralNode("a"), ""),
        Arguments.of(BinaryOperator.MUL, new StringLiteralNode("ab"), new IntLiteralNode(2), "abab"),
        Arguments.of(BinaryOperator.MUL, new StringLiteralNode("ab"), new IntLiteralNode(0), ""),
        Arguments.of(BinaryOperator.MUL, new StringLiteralNode("ab"), new BooleanLiteralNode(true), "ab"),
        Arguments.of(BinaryOperator.MUL, new StringLiteralNode("ab"), new BooleanLiteralNode(false), ""),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.blockPosNode(1, 2, 3),
            new IntLiteralNode(2),
            new BlockPos(2, 4, 6)),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.blockPosNode(1, 2, 3),
            new FloatLiteralNode(2.5),
            new BlockPos(2, 5, 7)),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(true),
            new BlockPos(1, 2, 3)),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(false),
            new BlockPos(0, 0, 0)),
        Arguments.of(BinaryOperator.MUL,
            new IntLiteralNode(2),
            TestUtils.blockPosNode(1, 2, 3),
            new BlockPos(2, 4, 6)),
        Arguments.of(BinaryOperator.MUL,
            new FloatLiteralNode(2.5),
            TestUtils.blockPosNode(1, 2, 3),
            new BlockPos(2, 5, 7)),
        Arguments.of(BinaryOperator.MUL,
            new BooleanLiteralNode(true),
            TestUtils.blockPosNode(1, 2, 3),
            new BlockPos(1, 2, 3)),
        Arguments.of(BinaryOperator.MUL,
            new BooleanLiteralNode(false),
            TestUtils.blockPosNode(1, 2, 3),
            new BlockPos(0, 0, 0)),
        Arguments.of(BinaryOperator.MUL,
            new IntLiteralNode(2),
            TestUtils.nodeList(new FloatLiteralNode(1.0), new FloatLiteralNode(2.0)),
            new MCList(Arrays.asList(1.0, 2.0, 1.0, 2.0))),
        Arguments.of(BinaryOperator.MUL,
            new IntLiteralNode(0),
            TestUtils.nodeList(new FloatLiteralNode(1.0), new FloatLiteralNode(2.0)),
            new MCList()),
        Arguments.of(BinaryOperator.MUL,
            new BooleanLiteralNode(true),
            TestUtils.nodeList(new FloatLiteralNode(1.0), new FloatLiteralNode(2.0)),
            new MCList(Arrays.asList(1.0, 2.0))),
        Arguments.of(BinaryOperator.MUL,
            new BooleanLiteralNode(false),
            TestUtils.nodeList(new FloatLiteralNode(1.0), new FloatLiteralNode(2.0)),
            new MCList()),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.nodeList(new FloatLiteralNode(1.0), new FloatLiteralNode(2.0)),
            new IntLiteralNode(2),
            new MCList(Arrays.asList(1.0, 2.0, 1.0, 2.0))),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.nodeList(new FloatLiteralNode(1.0), new FloatLiteralNode(2.0)),
            new IntLiteralNode(0),
            new MCList()),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.nodeList(new FloatLiteralNode(1.0), new FloatLiteralNode(2.0)),
            new BooleanLiteralNode(true),
            new MCList(Arrays.asList(1.0, 2.0))),
        Arguments.of(BinaryOperator.MUL,
            TestUtils.nodeList(new FloatLiteralNode(1.0), new FloatLiteralNode(2.0)),
            new BooleanLiteralNode(false),
            new MCList()),

        // TODO test div by 0
        Arguments.of(BinaryOperator.DIV, new IntLiteralNode(2), new IntLiteralNode(2), 1.0),
        Arguments.of(BinaryOperator.DIV, new FloatLiteralNode(2.0), new FloatLiteralNode(2.0), 1.0),
        Arguments.of(BinaryOperator.DIV, new IntLiteralNode(2), new FloatLiteralNode(2.0), 1.0),
        Arguments.of(BinaryOperator.DIV, new FloatLiteralNode(2.0), new IntLiteralNode(2), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(true), new BooleanLiteralNode(true), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(true), new IntLiteralNode(1), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(false), new IntLiteralNode(1), 0.0),
        Arguments.of(BinaryOperator.DIV, new IntLiteralNode(1), new BooleanLiteralNode(true), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(true), new FloatLiteralNode(1.0), 1.0),
        Arguments.of(BinaryOperator.DIV, new BooleanLiteralNode(false), new FloatLiteralNode(1.0), 0.0),
        Arguments.of(BinaryOperator.DIV, new FloatLiteralNode(1.0), new BooleanLiteralNode(true), 1.0),
        Arguments.of(BinaryOperator.DIV,
            TestUtils.blockPosNode(2, 4, 6),
            new IntLiteralNode(2),
            new BlockPos(1, 2, 3)),
        Arguments.of(BinaryOperator.DIV,
            TestUtils.blockPosNode(2, 5, 7),
            new FloatLiteralNode(2.5),
            new BlockPos(0, 2, 2)),
        Arguments.of(BinaryOperator.DIV,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(true),
            new BlockPos(1, 2, 3)),

        // TODO test div by 0
        Arguments.of(BinaryOperator.INT_DIV, new IntLiteralNode(3), new IntLiteralNode(2), 1),
        Arguments.of(BinaryOperator.INT_DIV, new FloatLiteralNode(3.0), new FloatLiteralNode(2.0), 1),
        Arguments.of(BinaryOperator.INT_DIV, new IntLiteralNode(3), new FloatLiteralNode(2.0), 1),
        Arguments.of(BinaryOperator.INT_DIV, new FloatLiteralNode(3.0), new IntLiteralNode(2), 1),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(true), new BooleanLiteralNode(true), 1),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(true), new IntLiteralNode(1), 1),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(false), new IntLiteralNode(1), 0),
        Arguments.of(BinaryOperator.INT_DIV, new IntLiteralNode(1), new BooleanLiteralNode(true), 1),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(true), new FloatLiteralNode(1.0), 1),
        Arguments.of(BinaryOperator.INT_DIV, new BooleanLiteralNode(false), new FloatLiteralNode(1.0), 0),
        Arguments.of(BinaryOperator.INT_DIV, new FloatLiteralNode(1.0), new BooleanLiteralNode(true), 1),
        Arguments.of(BinaryOperator.INT_DIV,
            TestUtils.blockPosNode(2, 4, 6),
            new IntLiteralNode(2),
            new BlockPos(1, 2, 3)),
        Arguments.of(BinaryOperator.INT_DIV,
            TestUtils.blockPosNode(2, 5, 7),
            new FloatLiteralNode(2.5),
            new BlockPos(0, 2, 2)),
        Arguments.of(BinaryOperator.INT_DIV,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(true),
            new BlockPos(1, 2, 3)),

        // TODO test div by 0
        Arguments.of(BinaryOperator.MOD, new IntLiteralNode(3), new IntLiteralNode(2), 1),
        Arguments.of(BinaryOperator.MOD, new FloatLiteralNode(3.0), new FloatLiteralNode(2.0), 1.0),
        Arguments.of(BinaryOperator.MOD, new IntLiteralNode(3), new FloatLiteralNode(2.0), 1.0),
        Arguments.of(BinaryOperator.MOD, new FloatLiteralNode(3.0), new IntLiteralNode(2), 1.0),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(true), new BooleanLiteralNode(true), 0),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(true), new IntLiteralNode(1), 0),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(false), new IntLiteralNode(1), 0),
        Arguments.of(BinaryOperator.MOD, new IntLiteralNode(1), new BooleanLiteralNode(true), 0),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(true), new FloatLiteralNode(1.0), 0.0),
        Arguments.of(BinaryOperator.MOD, new BooleanLiteralNode(false), new FloatLiteralNode(1.0), 0.0),
        Arguments.of(BinaryOperator.MOD, new FloatLiteralNode(1.0), new BooleanLiteralNode(true), 0.0),
        Arguments.of(BinaryOperator.MOD,
            TestUtils.blockPosNode(2, 4, 6),
            new IntLiteralNode(3),
            new BlockPos(2, 1, 0)),
        Arguments.of(BinaryOperator.MOD,
            TestUtils.blockPosNode(2, 4, 6),
            new FloatLiteralNode(3.0),
            new BlockPos(2, 1, 0)),
        Arguments.of(BinaryOperator.MOD,
            TestUtils.blockPosNode(1, 2, 3),
            new BooleanLiteralNode(true),
            new BlockPos(0, 0, 0))

        // TODO comparison, logic, collections
    );
  }

  @Test
  void evaluateNullOperatorError() {
    //noinspection ConstantConditions
    assertThrows(NullPointerException.class, () -> new BinaryOperatorNode(null, new IntLiteralNode(1), new IntLiteralNode(1)));
    assertThrows(NullPointerException.class, () -> new BinaryOperatorNode(BinaryOperator.PLUS, null, new IntLiteralNode(1)));
    assertThrows(NullPointerException.class, () -> new BinaryOperatorNode(BinaryOperator.PLUS, new IntLiteralNode(1), null));
  }

  @ParameterizedTest
  @EnumSource(BinaryOperator.class)
  void writeToNBT(BinaryOperator operator) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BinaryOperatorNode.ID_KEY, BinaryOperatorNode.ID);
    tag.setString(BinaryOperatorNode.SYMBOL_KEY, operator.getSymbol());
    NBTTagList args = new NBTTagList();
    args.appendTag(new IntLiteralNode(1).writeToNBT());
    args.appendTag(new IntLiteralNode(2).writeToNBT());
    tag.setTag(BinaryOperatorNode.ARGUMENTS_KEY, args);
    assertEquals(tag, new BinaryOperatorNode(operator, new IntLiteralNode(1), new IntLiteralNode(2)).writeToNBT());
  }

  @ParameterizedTest
  @EnumSource(BinaryOperator.class)
  void deserializeNBT(BinaryOperator operator) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BinaryOperatorNode.ID_KEY, BinaryOperatorNode.ID);
    tag.setString(BinaryOperatorNode.SYMBOL_KEY, operator.getSymbol());
    NBTTagList args = new NBTTagList();
    args.appendTag(new VariableNode("a").writeToNBT());
    args.appendTag(new VariableNode("b").writeToNBT());
    tag.setTag(BinaryOperatorNode.ARGUMENTS_KEY, args);
    assertEquals(String.format("a %s b", operator.getSymbol()), new BinaryOperatorNode(tag).toString());
  }

  @ParameterizedTest
  @EnumSource(BinaryOperator.class)
  void testToString(BinaryOperator operator) {
    assertEquals(String.format("a %s b", operator.getSymbol()),
        new BinaryOperatorNode(operator, new VariableNode("a"), new VariableNode("b")).toString());
  }
}
