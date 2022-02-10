package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.TestUtils;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class MethodCallNodeTest extends NodeTest {
  Node instance;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.instance = TestUtils.blockPosNode(1, 1, 1);
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new MethodCallNode(
        null,
        "up",
        Collections.singletonList(new IntLiteralNode(1, 0, 0)),
        0, 0));
    assertThrows(NullPointerException.class, () -> new MethodCallNode(
        this.instance,
        null,
        Collections.singletonList(new IntLiteralNode(1, 0, 0)),
        0, 0));
    assertThrows(NullPointerException.class, () -> new MethodCallNode(
        this.instance,
        "up",
        null,
        0, 0));
  }

  @Test
  void evaluate() {
    Object r = new MethodCallNode(
        this.instance,
        "up",
        Collections.singletonList(new IntLiteralNode(1, 0, 0)),
        0, 0).evaluate(this.p.getScope());
    assertSame(BlockPos.class, r.getClass());
    assertEquals(new BlockPos(1, 2, 1), r);
  }

  @Test
  void evaluateUndefinedError() {
    assertThrows(EvaluationException.class, () -> new MethodCallNode(
        new IntLiteralNode(1, 0, 0),
        "up",
        Collections.singletonList(new IntLiteralNode(1, 0, 0)),
        0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void evaluateMissingParameterError() {
    assertThrows(EvaluationException.class, () -> new MethodCallNode(
        this.instance,
        "up",
        Collections.emptyList(),
        0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void evaluateTooManyParametersError() {
    assertThrows(EvaluationException.class, () -> new MethodCallNode(
        this.instance,
        "up",
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(2, 0, 0)),
        0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void evaluateCastError() {
    assertThrows(CastException.class, () -> new MethodCallNode(
        this.instance,
        "up",
        Collections.singletonList(new StringLiteralNode("string", 0, 0)),
        0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(MethodCallNode.ID_KEY, MethodCallNode.ID);
    tag.setTag(MethodCallNode.INSTANCE_KEY, this.instance.writeToNBT());
    tag.setString(MethodCallNode.METHOD_NAME_KEY, "up");
    NBTTagList args = new NBTTagList();
    args.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    tag.setTag(FunctionCallNode.ARGUMENTS_KEY, args);
    assertEquals(tag, new MethodCallNode(
        this.instance,
        "up",
        Collections.singletonList(new IntLiteralNode(1, 0, 0)),
        0, 0).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(MethodCallNode.ID_KEY, MethodCallNode.ID);
    tag.setTag(MethodCallNode.INSTANCE_KEY, this.instance.writeToNBT());
    tag.setString(MethodCallNode.METHOD_NAME_KEY, "up");
    NBTTagList args = new NBTTagList();
    args.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    tag.setTag(FunctionCallNode.ARGUMENTS_KEY, args);
    assertEquals("to_pos([1, 1, 1]).up(1)", new MethodCallNode(tag).toString());
  }

  @Test
  void testToString() {
    assertEquals("to_pos([1, 1, 1]).up(1)", new MethodCallNode(
        this.instance,
        "up",
        Collections.singletonList(new IntLiteralNode(1, 0, 0)),
        0, 0).toString());
  }

  @Test
  void testEquals() {
    assertEquals(new MethodCallNode(new VariableNode("floor", 0, 0), "a", Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0),
        new MethodCallNode(new VariableNode("floor", 0, 0), "a", Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0));
  }
}
