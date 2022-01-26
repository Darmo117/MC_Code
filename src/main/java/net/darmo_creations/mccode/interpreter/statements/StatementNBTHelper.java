package net.darmo_creations.mccode.interpreter.statements;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    STMT_PROVIDERS.put(IfStatement.ID, IfStatement::new);
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

  public static List<Statement> deserializeStatementsList(final NBTTagCompound tag, final String key) {
    List<Statement> statements = new ArrayList<>();
    for (NBTBase t : tag.getTagList(key, new NBTTagCompound().getId())) {
      statements.add(getStatementForTag((NBTTagCompound) t));
    }
    return statements;
  }

  public static NBTTagList serializeStatementsList(final List<Statement> statements) {
    NBTTagList statementsList = new NBTTagList();
    statements.forEach(s -> statementsList.appendTag(s.writeToNBT()));
    return statementsList;
  }

  private StatementNBTHelper() {
  }
}