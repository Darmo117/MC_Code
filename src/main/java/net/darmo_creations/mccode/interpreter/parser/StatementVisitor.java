package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeBaseVisitor;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeParser;
import net.darmo_creations.mccode.interpreter.statements.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Visitor for {@link Statement} class.
 */
public class StatementVisitor extends MCCodeBaseVisitor<Statement> {
  private final NodeVisitor nodeVisitor;

  public StatementVisitor() {
    this.nodeVisitor = new NodeVisitor();
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
    conditions.add(this.nodeVisitor.visit(ctx.if_cond));
    if (ctx.elif_cond != null) {
      ctx.elif_cond.children.stream().map(this.nodeVisitor::visit).forEach(conditions::add);
    }

    List<List<Statement>> branches = new LinkedList<>();
    branches.add(ctx.if_stmts.children.stream().map(super::visit).collect(Collectors.toList()));
    if (ctx.elif_stmts != null) {
      // TODO how to extract elseif statements?
      ctx.elif_stmts.children.stream()
          .map(super::visit)
          .collect(Collectors.toList());
    }
    List<Statement> elseStatements;
    if (ctx.else_stmts != null) {
      elseStatements = ctx.else_stmts.children.stream().map(super::visit).collect(Collectors.toList());
    } else {
      elseStatements = new ArrayList<>();
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
