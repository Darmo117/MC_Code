package net.darmo_creations.mccode.interpreter.statements;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for deserializing {@link Statement}s from NBT tags.
 */
public final class StatementNBTHelper {
  private static final Map<Integer, Function<NBTTagCompound, Statement>> STMT_PROVIDERS = new HashMap<>();

  static {
    STMT_PROVIDERS.put(ImportStatement.ID, ImportStatement::new);

    STMT_PROVIDERS.put(DeclareVariableStatement.ID, DeclareVariableStatement::new);
    STMT_PROVIDERS.put(DefineFunctionStatement.ID, DefineFunctionStatement::new);
    STMT_PROVIDERS.put(AssignVariableStatement.ID, AssignVariableStatement::new);
    STMT_PROVIDERS.put(SetItemStatement.ID, SetItemStatement::new);
    STMT_PROVIDERS.put(SetPropertyStatement.ID, SetPropertyStatement::new);

    STMT_PROVIDERS.put(DeleteVariableStatement.ID, DeleteVariableStatement::new);
    STMT_PROVIDERS.put(DeleteItemStatement.ID, DeleteItemStatement::new);

    STMT_PROVIDERS.put(ExpressionStatement.ID, ExpressionStatement::new);

    STMT_PROVIDERS.put(WhileLoopStatement.ID, WhileLoopStatement::new);
    STMT_PROVIDERS.put(ForLoopStatement.ID, ForLoopStatement::new);

    STMT_PROVIDERS.put(WaitStatement.ID, WaitStatement::new);

    STMT_PROVIDERS.put(BreakStatement.ID, BreakStatement::new);
    STMT_PROVIDERS.put(ContinueStatement.ID, ContinueStatement::new);
    STMT_PROVIDERS.put(ReturnStatement.ID, ReturnStatement::new);
  }

  /**
   * Return the statement corresponding to the given tag.
   *
   * @param tag The tag to deserialize.
   * @return The statement.
   * @throws IllegalArgumentException If no {@link Statement} correspond to the {@link Statement#ID_KEY} property.
   */
  public static Statement getStatementForTag(final NBTTagCompound tag) {
    int tagID = tag.getInteger(Statement.ID_KEY);
    if (!STMT_PROVIDERS.containsKey(tagID)) {
      throw new IllegalArgumentException("Undefined statement ID: " + tagID);
    }
    return STMT_PROVIDERS.get(tagID).apply(tag);
  }

  private StatementNBTHelper() {
  }
}
