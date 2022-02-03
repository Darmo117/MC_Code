package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.nodes.BinaryOperatorNode;
import net.darmo_creations.mccode.interpreter.nodes.VariableNode;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.darmo_creations.mccode.interpreter.types.UserFunction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class DefineFunctionStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(DefineFunctionStatement.ID_KEY, DefineFunctionStatement.ID);
    tag.setString(DefineFunctionStatement.NAME_KEY, "f");
    NBTTagList paramsList = new NBTTagList();
    List<String> params = Arrays.asList("p1", "p2");
    params.forEach(s -> paramsList.appendTag(new NBTTagString(s)));
    tag.setTag(DefineFunctionStatement.PARAMS_LIST_KEY, paramsList);
    List<Statement> statements = Collections.singletonList(new ReturnStatement(
        new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("p1"), new VariableNode("p2"))));
    NBTTagList statementsList = new NBTTagList();
    statements.forEach(s -> statementsList.appendTag(s.writeToNBT()));
    tag.setTag(DefineFunctionStatement.STATEMENTS_LIST_KEY, statementsList);
    assertEquals(tag, new DefineFunctionStatement("f", params, statements).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(DefineFunctionStatement.ID_KEY, DefineFunctionStatement.ID);
    tag.setString(DefineFunctionStatement.NAME_KEY, "f");
    NBTTagList paramsList = new NBTTagList();
    List<String> params = Arrays.asList("p1", "p2");
    params.forEach(s -> paramsList.appendTag(new NBTTagString(s)));
    tag.setTag(DefineFunctionStatement.PARAMS_LIST_KEY, paramsList);
    List<Statement> statements = Collections.singletonList(new ReturnStatement(
        new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("p1"), new VariableNode("p2"))));
    NBTTagList statementsList = new NBTTagList();
    statements.forEach(s -> statementsList.appendTag(s.writeToNBT()));
    tag.setTag(DefineFunctionStatement.STATEMENTS_LIST_KEY, statementsList);
    assertEquals(new DefineFunctionStatement("f", params, statements), new DefineFunctionStatement(tag));
  }

  @Test
  void execute() {
    List<String> params = Arrays.asList("p1", "p2");
    List<Statement> statements = Collections.singletonList(new ReturnStatement(
        new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("p1"), new VariableNode("p2"))));
    assertEquals(StatementAction.PROCEED, new DefineFunctionStatement("f", params, statements).execute(this.p.getScope()));
    assertTrue(this.p.getScope().isVariableDefined("f"));
    assertEquals(new UserFunction("f", params, statements), this.p.getScope().getVariable("f", false));
  }

  @Test
  void nullNameError() {
    List<String> params = Arrays.asList("p1", "p2");
    List<Statement> statements = Collections.singletonList(new ReturnStatement(
        new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("p1"), new VariableNode("p2"))));
    assertThrows(NullPointerException.class, () -> new DefineFunctionStatement(null, params, statements).execute(this.p.getScope()));
  }

  @Test
  void nullParametersError() {
    List<Statement> statements = Collections.singletonList(new ReturnStatement(
        new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("p1"), new VariableNode("p2"))));
    assertThrows(NullPointerException.class, () -> new DefineFunctionStatement("f", null, statements).execute(this.p.getScope()));
  }

  @Test
  void nullStatementsError() {
    List<String> params = Arrays.asList("p1", "p2");
    assertThrows(NullPointerException.class, () -> new DefineFunctionStatement("f", params, null).execute(this.p.getScope()));
  }
}
