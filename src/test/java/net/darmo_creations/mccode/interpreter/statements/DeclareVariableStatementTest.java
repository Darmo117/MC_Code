package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class DeclareVariableStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(DeclareVariableStatement.ID_KEY, DeclareVariableStatement.ID);
    tag.setString(DeclareVariableStatement.VAR_NAME_KEY, "a");
    tag.setBoolean(DeclareVariableStatement.PUBLIC_KEY, true);
    tag.setBoolean(DeclareVariableStatement.EDITABLE_KEY, true);
    tag.setBoolean(DeclareVariableStatement.CONSTANT_KEY, false);
    tag.setTag(DeclareVariableStatement.VALUE_KEY, new IntLiteralNode(1, 0, 0).writeToNBT());
    assertEquals(tag, new DeclareVariableStatement(true, true, false, "a", new IntLiteralNode(1, 0, 0), 0, 0).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(DeclareVariableStatement.ID_KEY, DeclareVariableStatement.ID);
    tag.setString(DeclareVariableStatement.VAR_NAME_KEY, "a");
    tag.setBoolean(DeclareVariableStatement.PUBLIC_KEY, true);
    tag.setBoolean(DeclareVariableStatement.EDITABLE_KEY, true);
    tag.setBoolean(DeclareVariableStatement.CONSTANT_KEY, false);
    tag.setTag(DeclareVariableStatement.VALUE_KEY, new IntLiteralNode(1, 0, 0).writeToNBT());
    assertEquals(new DeclareVariableStatement(true, true, false, "a", new IntLiteralNode(1, 0, 0), 0, 0),
        new DeclareVariableStatement(tag));
  }

  @Test
  void execute() {
    assertEquals(StatementAction.PROCEED, new DeclareVariableStatement(true, true, false, "a",
        new IntLiteralNode(1, 0, 0), 0, 0).execute(this.p.getScope()));
    assertTrue(this.p.getScope().isVariableDefined("a"));
    assertEquals(1L, this.p.getScope().getVariable("a", false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForFlagsErrors")
  void flagsErrors(boolean public_, boolean editable, boolean constant) {
    assertThrows(MCCodeException.class, () -> new DeclareVariableStatement(public_, editable, constant, "a", new IntLiteralNode(1, 0, 0), 0, 0));
  }

  static Stream<Arguments> provideArgsForFlagsErrors() {
    return Stream.of(
        Arguments.of(false, true, false),
        Arguments.of(true, true, true)
    );
  }

  @Test
  void nullNameError() {
    assertThrows(NullPointerException.class, () -> new DeclareVariableStatement(false, false, false, null, new IntLiteralNode(1, 0, 0), 0, 0));
  }

  @Test
  void nullNodeError() {
    assertThrows(NullPointerException.class, () -> new DeclareVariableStatement(false, false, false, "a", null, 0, 0));
  }

  @Test
  void testEquals() {
    assertEquals(new DeclareVariableStatement(true, true, false, "a", new IntLiteralNode(1, 0, 0), 0, 0),
        new DeclareVariableStatement(true, true, false, "a", new IntLiteralNode(1, 0, 0), 0, 0));
  }
}
