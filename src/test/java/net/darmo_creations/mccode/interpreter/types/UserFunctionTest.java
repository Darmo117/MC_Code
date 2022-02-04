package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.TestBase;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.nodes.FunctionCallNode;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.VariableNode;
import net.darmo_creations.mccode.interpreter.statements.ExpressionStatement;
import net.darmo_creations.mccode.interpreter.statements.ReturnStatement;
import net.darmo_creations.mccode.interpreter.statements.WaitStatement;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UserFunctionTest extends TestBase {
  UserFunction f;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.f = new UserFunction("f", Collections.singletonList("p"),
        Collections.singletonList(new ReturnStatement(new VariableNode("p"))));
  }

  @Test
  void getName() {
    assertEquals("f", this.f.getName());
  }

  @Test
  void getReturnType() {
    assertSame(AnyType.class, this.f.getReturnType().getClass());
  }

  @Test
  void getParameters() {
    assertEquals(Collections.singletonList(new Parameter("p", ProgramManager.getTypeInstance(AnyType.class))), this.f.getParameters());
  }

  @Test
  void apply() {
    this.p.getScope().declareVariable(new Variable("p", false, false, false, true, 1));
    assertEquals(1, this.f.apply(this.p.getScope()));
  }

  @Test
  void waitStatementError() {
    UserFunction f = new UserFunction("f", Collections.emptyList(),
        Collections.singletonList(new WaitStatement(new IntLiteralNode(1))));
    assertThrows(SyntaxErrorException.class, () -> f.apply(this.p.getScope()));
  }

  @Test
  void recursionError() {
    UserFunction f = new UserFunction("f", Collections.emptyList(),
        Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("f"), Collections.emptyList()))));
    this.p.getScope().declareVariable(new Variable("f", false, false, false, true, f));
    assertThrows(EvaluationException.class, () -> f.apply(this.p.getScope()));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(UserFunction.NAME_KEY, "f");
    tag.setInteger(UserFunction.IP_KEY, 0);
    NBTTagList list = new NBTTagList();
    list.appendTag(new NBTTagString("p"));
    tag.setTag(UserFunction.PARAMETERS_KEY, list);
    NBTTagList statements = new NBTTagList();
    statements.appendTag(new ReturnStatement(new VariableNode("p")).writeToNBT());
    tag.setTag(UserFunction.STATEMENTS_KEY, statements);
    assertEquals(tag, this.f.writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(UserFunction.NAME_KEY, "f");
    tag.setInteger(UserFunction.IP_KEY, 0);
    NBTTagList list = new NBTTagList();
    list.appendTag(new NBTTagString("p"));
    tag.setTag(UserFunction.PARAMETERS_KEY, list);
    NBTTagList statements = new NBTTagList();
    statements.appendTag(new ReturnStatement(new VariableNode("p")).writeToNBT());
    tag.setTag(UserFunction.STATEMENTS_KEY, statements);
    assertEquals(this.f, new UserFunction(tag));
  }

  @Test
  void testEquals() {
    assertEquals(new UserFunction("f", Collections.singletonList("p"),
        Collections.singletonList(new ReturnStatement(new VariableNode("p")))), this.f);
  }
}
