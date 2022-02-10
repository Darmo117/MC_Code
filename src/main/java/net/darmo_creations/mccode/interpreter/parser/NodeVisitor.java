package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.nodes.*;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeBaseVisitor;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeParser;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.darmo_creations.mccode.interpreter.type_wrappers.UnaryOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Visitor for {@link Node} class.
 */
public class NodeVisitor extends MCCodeBaseVisitor<Node> {
  @Override
  public Node visitExpression(MCCodeParser.ExpressionContext ctx) {
    return super.visit(ctx.expr());
  }

  @Override
  public Node visitParentheses(MCCodeParser.ParenthesesContext ctx) {
    return super.visit(ctx.exp);
  }

  @Override
  public Node visitNullLiteral(MCCodeParser.NullLiteralContext ctx) {
    return new NullLiteralNode();
  }

  @Override
  public Node visitBoolLiteral(MCCodeParser.BoolLiteralContext ctx) {
    return new BooleanLiteralNode(ctx.TRUE() != null);
  }

  @Override
  public Node visitIntLiteral(MCCodeParser.IntLiteralContext ctx) {
    return new IntLiteralNode(Long.parseLong(ctx.INT().getText()));
  }

  @Override
  public Node visitFloatLiteral(MCCodeParser.FloatLiteralContext ctx) {
    return new FloatLiteralNode(Double.parseDouble(ctx.FLOAT().getText()));
  }

  @Override
  public Node visitStringLiteral(MCCodeParser.StringLiteralContext ctx) {
    return new StringLiteralNode(Utils.unescapeString(ctx.STRING().getText()));
  }

  @Override
  public Node visitListLiteral(MCCodeParser.ListLiteralContext ctx) {
    return new ListLiteralNode(ctx.expr().stream().map(super::visit).collect(Collectors.toList()));
  }

  @Override
  public Node visitMapLiteral(MCCodeParser.MapLiteralContext ctx) {
    Map<String, Node> values = new HashMap<>();
    for (int i = 0; i < ctx.STRING().size(); i++) {
      values.put(Utils.unescapeString(ctx.STRING(i).getText()), super.visit(ctx.expr(i)));
    }
    return new MapLiteralNode(values);
  }

  @Override
  public Node visitSetLiteral(MCCodeParser.SetLiteralContext ctx) {
    return new SetLiteralNode(ctx.expr().stream().map(super::visit).collect(Collectors.toList()));
  }

  @Override
  public Node visitVariable(MCCodeParser.VariableContext ctx) {
    if (ctx.IDENT() != null) {
      return new VariableNode(ctx.IDENT().getText());
    }
    return new VariableNode(ctx.CMDARG().getText());
  }

  @Override
  public Node visitGetProperty(MCCodeParser.GetPropertyContext ctx) {
    return new PropertyCallNode(super.visit(ctx.object), ctx.property.getText());
  }

  @Override
  public Node visitMethodCall(MCCodeParser.MethodCallContext ctx) {
    return new MethodCallNode(
        super.visit(ctx.object),
        ctx.property.getText(),
        ctx.expr().stream().skip(1).map(super::visit).collect(Collectors.toList())
    );
  }

  @Override
  public Node visitFunctionCall(MCCodeParser.FunctionCallContext ctx) {
    return new FunctionCallNode(
        super.visit(ctx.expr(0)),
        ctx.expr().stream().skip(1).map(super::visit).collect(Collectors.toList())
    );
  }

  @Override
  public Node visitUnaryOperator(MCCodeParser.UnaryOperatorContext ctx) {
    //noinspection ConstantConditions
    return new UnaryOperatorNode(UnaryOperator.fromString(ctx.operator.getText()), super.visit(ctx.operand));
  }

  @Override
  public Node visitGetItem(MCCodeParser.GetItemContext ctx) {
    return new BinaryOperatorNode(BinaryOperator.GET_ITEM, super.visit(ctx.source), super.visit(ctx.key));
  }

  @Override
  public Node visitBinaryOperator(MCCodeParser.BinaryOperatorContext ctx) {
    BinaryOperator operator;
    if (ctx.IN() != null) {
      if (ctx.NOT() == null) {
        operator = BinaryOperator.IN;
      } else {
        operator = BinaryOperator.NOT_IN;
      }
    } else {
      operator = BinaryOperator.fromString(ctx.operator.getText());
    }
    //noinspection ConstantConditions
    return new BinaryOperatorNode(operator, super.visit(ctx.left), super.visit(ctx.right));
  }
}
