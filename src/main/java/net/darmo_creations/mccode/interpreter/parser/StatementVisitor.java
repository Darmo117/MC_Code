package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeBaseVisitor;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeParser;
import net.darmo_creations.mccode.interpreter.statements.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Visitor for {@link Statement} class.
 */
public class StatementVisitor extends MCCodeBaseVisitor<Statement> {
  private final NodeVisitor nodeVisitor;
  private final IfVisitor ifVisitor;

  public StatementVisitor() {
    this.nodeVisitor = new NodeVisitor();
    this.ifVisitor = new IfVisitor(this, this.nodeVisitor);
  }

  @Override
  public Statement visitStmt(MCCodeParser.StmtContext ctx) {
    return super.visit(ctx.statement());
  }

  @Override
  public Statement visitStatement_(MCCodeParser.Statement_Context ctx) {
    return super.visit(ctx.statement());
  }

  @Override
  public Statement visitImport_statement(MCCodeParser.Import_statementContext ctx) {
    String alias = null;
    List<String> path = ctx.IDENT().stream().map(TerminalNode::getText).collect(Collectors.toList());
    if (ctx.alias != null) {
      alias = ctx.alias.getText();
      // Last IDENT token is the alias
      path.remove(path.size() - 1);
    }
    return new ImportStatement(path, alias);
  }

  @Override
  public Statement visitDeclareGlobalVariable(MCCodeParser.DeclareGlobalVariableContext ctx) {
    return new DeclareVariableStatement(true, ctx.EDITABLE() != null, false,
        ctx.name.getText(), this.nodeVisitor.visit(ctx.value));
  }

  @Override
  public Statement visitDeclareGlobalConstant(MCCodeParser.DeclareGlobalConstantContext ctx) {
    return new DeclareVariableStatement(true, false, true,
        ctx.name.getText(), this.nodeVisitor.visit(ctx.value));
  }

  @Override
  public Statement visitDefineFunctionStatement(MCCodeParser.DefineFunctionStatementContext ctx) {
    String name = ctx.name.getText();
    List<String> params = ctx.IDENT().stream().skip(1).map(TerminalNode::getText).collect(Collectors.toList());
    List<Statement> statements = ctx.statement().stream().map(super::visit).collect(Collectors.toList());
    return new DefineFunctionStatement(name, params, statements);
  }

  @Override
  public Statement visitDeclareVariableStatement(MCCodeParser.DeclareVariableStatementContext ctx) {
    return new DeclareVariableStatement(false, false, ctx.CONST() != null,
        ctx.name.getText(), this.nodeVisitor.visit(ctx.value));
  }

  @Override
  public Statement visitVariableAssignmentStatement(MCCodeParser.VariableAssignmentStatementContext ctx) {
    return new AssignVariableStatement(
        ctx.name.getText(),
        AssigmentOperator.fromString(ctx.operator.getText()),
        this.nodeVisitor.visit(ctx.value)
    );
  }

  @Override
  public Statement visitSetItemStatement(MCCodeParser.SetItemStatementContext ctx) {
    return new SetItemStatement(
        this.nodeVisitor.visit(ctx.target),
        this.nodeVisitor.visit(ctx.key),
        AssigmentOperator.fromString(ctx.operator.getText()),
        this.nodeVisitor.visit(ctx.value)
    );
  }

  @Override
  public Statement visitSetPropertyStatement(MCCodeParser.SetPropertyStatementContext ctx) {
    return new SetPropertyStatement(
        this.nodeVisitor.visit(ctx.target),
        ctx.name.getText(),
        AssigmentOperator.fromString(ctx.operator.getText()),
        this.nodeVisitor.visit(ctx.value)
    );
  }

  @Override
  public Statement visitDeleteStatement(MCCodeParser.DeleteStatementContext ctx) {
    return new DeleteVariableStatement(ctx.name.getText());
  }

  @Override
  public Statement visitDeleteItemStatement(MCCodeParser.DeleteItemStatementContext ctx) {
    return new DeleteItemStatement(this.nodeVisitor.visit(ctx.target), this.nodeVisitor.visit(ctx.key));
  }

  @Override
  public Statement visitExpressionStatement(MCCodeParser.ExpressionStatementContext ctx) {
    return new ExpressionStatement(this.nodeVisitor.visit(ctx.expr()));
  }

  @Override
  public Statement visitIfStatement(MCCodeParser.IfStatementContext ctx) {
    List<Node> conditions = new LinkedList<>();
    List<List<Statement>> branches = new LinkedList<>();

    conditions.add(this.nodeVisitor.visit(ctx.cond));
    branches.add(ctx.statement().stream().map(super::visit).collect(Collectors.toList()));

    for (MCCodeParser.ElseifContext elseifContext : ctx.elseif()) {
      Pair<Node, List<Statement>> branch = this.ifVisitor.visit(elseifContext);
      conditions.add(branch.getLeft());
      branches.add(branch.getRight());
    }

    List<Statement> elseStatements = new ArrayList<>();
    if (ctx.else_() != null) {
      elseStatements = this.ifVisitor.visit(ctx.else_()).getRight();
    }

    return new IfStatement(
        conditions,
        branches,
        elseStatements
    );
  }

  @Override
  public Statement visitWhileLoopStatement(MCCodeParser.WhileLoopStatementContext ctx) {
    return new WhileLoopStatement(
        this.nodeVisitor.visit(ctx.cond),
        ctx.loop_stmt().stream().map(super::visit).collect(Collectors.toList())
    );
  }

  @Override
  public Statement visitForLoopStatement(MCCodeParser.ForLoopStatementContext ctx) {
    return new ForLoopStatement(
        ctx.variable.getText(),
        this.nodeVisitor.visit(ctx.range),
        ctx.loop_stmt().stream().map(super::visit).collect(Collectors.toList())
    );
  }

  @Override
  public Statement visitWaitStatement(MCCodeParser.WaitStatementContext ctx) {
    return new WaitStatement(this.nodeVisitor.visit(ctx.expr()));
  }

  @Override
  public Statement visitReturnStatement(MCCodeParser.ReturnStatementContext ctx) {
    return new ReturnStatement(ctx.returned != null ? this.nodeVisitor.visit(ctx.returned) : null);
  }

  @Override
  public Statement visitBreakStatement(MCCodeParser.BreakStatementContext ctx) {
    return new BreakStatement();
  }

  @Override
  public Statement visitContinueStatement(MCCodeParser.ContinueStatementContext ctx) {
    return new ContinueStatement();
  }
}
