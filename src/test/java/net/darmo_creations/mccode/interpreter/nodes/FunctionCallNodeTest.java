package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class FunctionCallNodeTest extends NodeTest {
  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new FunctionCallNode(null, Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0));
    assertThrows(NullPointerException.class, () -> new FunctionCallNode(new VariableNode("floor", 0, 0), null, 0, 0));
  }

  @Test
  void evaluate() {
    Object r = new FunctionCallNode(new VariableNode("floor", 0, 0), Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0).evaluate(this.p.getScope());
    assertSame(Long.class, r.getClass());
    assertEquals(1L, r);
  }

  @Test
  void evaluateNotCallableError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, 1));
    assertThrows(EvaluationException.class, () -> new FunctionCallNode(new VariableNode("a", 0, 0),
        Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void evaluateUndefinedError() {
    assertThrows(EvaluationException.class, () -> new FunctionCallNode(new VariableNode("uaieaie", 0, 0),
        Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void evaluateMissingParameterError() {
    assertThrows(EvaluationException.class, () -> new FunctionCallNode(new VariableNode("hypot", 0, 0),
        Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void evaluateTooManyParametersError() {
    assertThrows(EvaluationException.class, () -> new FunctionCallNode(new VariableNode("hypot", 0, 0),
        Arrays.asList(new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(1.0, 0, 0), new FloatLiteralNode(1.0, 0, 0)), 0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void evaluateCastError() {
    assertThrows(CastException.class, () -> new FunctionCallNode(new VariableNode("floor", 0, 0),
        Collections.singletonList(new StringLiteralNode("s", 0, 0)), 0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(FunctionCallNode.ID_KEY, FunctionCallNode.ID);
    tag.setTag(FunctionCallNode.FUNCTION_OBJ_KEY, new VariableNode("hypot", 0, 0).writeToNBT());
    NBTTagList args = new NBTTagList();
    args.appendTag(new FloatLiteralNode(1.0, 0, 0).writeToNBT());
    args.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    tag.setTag(FunctionCallNode.ARGUMENTS_KEY, args);
    assertEquals(tag, new FunctionCallNode(new VariableNode("hypot", 0, 0),
        Arrays.asList(new FloatLiteralNode(1.0, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(FunctionCallNode.ID_KEY, FunctionCallNode.ID);
    tag.setTag(FunctionCallNode.FUNCTION_OBJ_KEY, new VariableNode("hypot", 0, 0).writeToNBT());
    NBTTagList args = new NBTTagList();
    args.appendTag(new FloatLiteralNode(1.0, 0, 0).writeToNBT());
    args.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    tag.setTag(FunctionCallNode.ARGUMENTS_KEY, args);
    assertEquals("hypot(1.0, 1)", new FunctionCallNode(tag).toString());
  }

  @Test
  void testToString() {
    assertEquals("floor(1.0)", new FunctionCallNode(new VariableNode("floor", 0, 0),
        Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0).toString());
    assertEquals("hypot(1.0, 1)", new FunctionCallNode(new VariableNode("hypot", 0, 0),
        Arrays.asList(new FloatLiteralNode(1.0, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0).toString());
  }

  @Test
  void testEquals() {
    assertEquals(new FunctionCallNode(new VariableNode("floor", 0, 0), Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0),
        new FunctionCallNode(new VariableNode("floor", 0, 0), Collections.singletonList(new FloatLiteralNode(1.0, 0, 0)), 0, 0));
  }
}
