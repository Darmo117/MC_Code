package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.TestUtils;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.type_wrappers.UnaryOperator;
import net.darmo_creations.mccode.interpreter.types.Position;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class UnaryOperatorNodeTest extends NodeTest {
  @ParameterizedTest
  @MethodSource("provideArgsForEvaluateValidOperands")
  void evaluateValidOperands(UnaryOperator operator, Node operand, Object expectedValue) {
    Object r = new UnaryOperatorNode(operator, operand, 0, 0).evaluate(this.p.getScope());
    assertSame(expectedValue.getClass(), r.getClass());
    assertEquals(expectedValue, r);
  }

  private static Stream<Arguments> provideArgsForEvaluateValidOperands() {
    return Stream.of(
        Arguments.of(UnaryOperator.MINUS, new IntLiteralNode(1, 0, 0), -1L),
        Arguments.of(UnaryOperator.MINUS, new FloatLiteralNode(1.0, 0, 0), -1.0),
        Arguments.of(UnaryOperator.MINUS, new BooleanLiteralNode(true, 0, 0), -1L),
        Arguments.of(UnaryOperator.MINUS, new BooleanLiteralNode(false, 0, 0), 0L),
        Arguments.of(UnaryOperator.MINUS, TestUtils.blockPosNode(1, 2, 3), new Position(-1, -2, -3)),

        Arguments.of(UnaryOperator.NOT, new IntLiteralNode(1, 0, 0), false),
        Arguments.of(UnaryOperator.NOT, new IntLiteralNode(0, 0, 0), true),
        Arguments.of(UnaryOperator.NOT, new FloatLiteralNode(1, 0, 0), false),
        Arguments.of(UnaryOperator.NOT, new FloatLiteralNode(0, 0, 0), true),
        Arguments.of(UnaryOperator.NOT, new BooleanLiteralNode(true, 0, 0), false),
        Arguments.of(UnaryOperator.NOT, new BooleanLiteralNode(false, 0, 0), true),
        Arguments.of(UnaryOperator.NOT, new StringLiteralNode("a", 0, 0), false),
        Arguments.of(UnaryOperator.NOT, new StringLiteralNode("", 0, 0), true),
        Arguments.of(UnaryOperator.NOT, new NullLiteralNode(0, 0), true),
        Arguments.of(UnaryOperator.NOT, TestUtils.nodeList(new IntLiteralNode(1, 0, 0)), false),
        Arguments.of(UnaryOperator.NOT, TestUtils.nodeList(), true),
        Arguments.of(UnaryOperator.NOT, TestUtils.nodeSet(new IntLiteralNode(1, 0, 0)), false),
        Arguments.of(UnaryOperator.NOT, TestUtils.nodeSet(), true),
        Arguments.of(UnaryOperator.NOT, TestUtils.nodeMap("a", new IntLiteralNode(1, 0, 0)), false),
        Arguments.of(UnaryOperator.NOT, new MapLiteralNode(Collections.emptyMap(), 0, 0), true),

        Arguments.of(UnaryOperator.LENGTH, new StringLiteralNode("a", 0, 0), 1L),
        Arguments.of(UnaryOperator.LENGTH, new StringLiteralNode("", 0, 0), 0L),
        Arguments.of(UnaryOperator.LENGTH, TestUtils.nodeList(new IntLiteralNode(1, 0, 0)), 1L),
        Arguments.of(UnaryOperator.LENGTH, TestUtils.nodeList(), 0L),
        Arguments.of(UnaryOperator.LENGTH, TestUtils.nodeSet(new IntLiteralNode(1, 0, 0)), 1L),
        Arguments.of(UnaryOperator.LENGTH, TestUtils.nodeSet(), 0L),
        Arguments.of(UnaryOperator.LENGTH, TestUtils.nodeMap("a", new IntLiteralNode(1, 0, 0)), 1L),
        Arguments.of(UnaryOperator.LENGTH, new MapLiteralNode(Collections.emptyMap(), 0, 0), 0L)
    );
  }

  @Test
  void evaluateNullOperatorError() {
    //noinspection ConstantConditions
    assertThrows(NullPointerException.class, () -> new UnaryOperatorNode(null, new IntLiteralNode(1, 0, 0), 0, 0));
    assertThrows(NullPointerException.class, () -> new UnaryOperatorNode(UnaryOperator.MINUS, null, 0, 0));
  }

  @ParameterizedTest
  @EnumSource(UnaryOperator.class)
  void writeToNBT(UnaryOperator operator) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(UnaryOperatorNode.ID_KEY, UnaryOperatorNode.ID);
    tag.setString(UnaryOperatorNode.SYMBOL_KEY, operator.getSymbol());
    NBTTagList args = new NBTTagList();
    args.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    tag.setTag(UnaryOperatorNode.ARGUMENTS_KEY, args);
    assertEquals(tag, new UnaryOperatorNode(operator, new IntLiteralNode(1, 0, 0), 0, 0).writeToNBT());
  }

  @ParameterizedTest
  @EnumSource(UnaryOperator.class)
  void deserializeNBT(UnaryOperator operator) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(UnaryOperatorNode.ID_KEY, UnaryOperatorNode.ID);
    tag.setString(UnaryOperatorNode.SYMBOL_KEY, operator.getSymbol());
    NBTTagList args = new NBTTagList();
    args.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    tag.setTag(UnaryOperatorNode.ARGUMENTS_KEY, args);
    UnaryOperatorNode node = new UnaryOperatorNode(tag);
    assertEquals(operator.getSymbol(), node.getSymbol());
    assertEquals(Collections.singletonList(1L),
        node.getArguments().stream().map(n -> n.evaluate(this.p.getScope())).collect(Collectors.toList()));
  }

  @Test
  void testToString() {
    assertEquals("-a", new UnaryOperatorNode(UnaryOperator.MINUS, new VariableNode("a", 0, 0), 0, 0).toString());
    assertEquals("not a", new UnaryOperatorNode(UnaryOperator.NOT, new VariableNode("a", 0, 0), 0, 0).toString());
  }

  @Test
  void testEquals() {
    assertEquals(new UnaryOperatorNode(UnaryOperator.MINUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new UnaryOperatorNode(UnaryOperator.MINUS, new IntLiteralNode(1, 0, 0), 0, 0));
  }
}
