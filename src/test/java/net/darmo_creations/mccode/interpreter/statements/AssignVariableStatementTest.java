package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.ListLiteralNode;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SetupProgramManager.class)
class AssignVariableStatementTest extends StatementTest {
  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 1L));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(AssignVariableStatement.ID_KEY, AssignVariableStatement.ID);
    tag.setString(AssignVariableStatement.VAR_NAME_KEY, "a");
    tag.setString(AssignVariableStatement.OPERATOR_KEY, "+=");
    tag.setTag(AssignVariableStatement.VALUE_KEY, new IntLiteralNode(1).writeToNBT());
    assertEquals(tag, new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1)).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(AssignVariableStatement.ID_KEY, AssignVariableStatement.ID);
    tag.setString(AssignVariableStatement.VAR_NAME_KEY, "a");
    tag.setString(AssignVariableStatement.OPERATOR_KEY, "+=");
    tag.setTag(AssignVariableStatement.VALUE_KEY, new IntLiteralNode(1).writeToNBT());
    assertEquals(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1)), new AssignVariableStatement(tag));
  }

  @Test
  void execute() {
    assertEquals(StatementAction.PROCEED, new AssignVariableStatement("a", AssigmentOperator.PLUS,
        new IntLiteralNode(1)).execute(this.p.getScope()));
    assertEquals(2L, this.p.getScope().getVariable("a", false));

    MCList list = new MCList();
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, list));
    assertEquals(StatementAction.PROCEED, new AssignVariableStatement("b", AssigmentOperator.PLUS,
        new ListLiteralNode(Collections.singletonList(new IntLiteralNode(1)))).execute(this.p.getScope()));
    Object res = this.p.getScope().getVariable("b", false);
    assertEquals(new MCList(Collections.singletonList(1L)), res);
    assertSame(list, res);
  }

  @Test
  void testEquals() {
    assertEquals(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1)));
  }
}
