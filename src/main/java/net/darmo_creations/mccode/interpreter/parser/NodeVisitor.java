package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.nodes.*;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeBaseVisitor;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeParser;
import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Visitor for {@link Node} class.
 */
public class NodeVisitor extends MCCodeBaseVisitor<Node> {
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
    return new IntLiteralNode(Integer.parseInt(ctx.INT().getText()));
  }

  @Override
  public Node visitFloatLiteral(MCCodeParser.FloatLiteralContext ctx) {
    return new FloatLiteralNode(Double.parseDouble(ctx.FLOAT().getText()));
  }

  @Override
  public Node visitStringLiteral(MCCodeParser.StringLiteralContext ctx) {
    String s = ctx.STRING().getText();
    s = s.substring(1, s.length() - 1); // Remove quotes
    return new StringLiteralNode(s);
  }

  @Override
  public Node visitListLiteral(MCCodeParser.ListLiteralContext ctx) {
    return new ListLiteralNode(ctx.expr().stream().map(super::visit).collect(Collectors.toList()));
  }

  @Override
  public Node visitMapLiteral(MCCodeParser.MapLiteralContext ctx) {
    Map<String, Node> values = new HashMap<>();
    for (int i = 0; i < ctx.IDENT().size(); i++) {
      values.put(ctx.IDENT(i).getText(), super.visit(ctx.expr(i)));
    }
    return new MapLiteralNode(values);
  }

  @Override
  public Node visitSetLiteral(MCCodeParser.SetLiteralContext ctx) {
    return new SetLiteralNode(ctx.expr().stream().map(super::visit).collect(Collectors.toList()));
  }

  @Override
  public Node visitVariable(MCCodeParser.VariableContext ctx) {
    return new VariableNode(ctx.IDENT().getText());
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
    return new UnaryOperatorNode(Operator.fromString(ctx.operator.getText()), super.visit(ctx.operand));
  }

  @Override
  public Node visitGetItem(MCCodeParser.GetItemContext ctx) {
    return new BinaryOperatorNode(Operator.GET_ITEM, super.visit(ctx.source), super.visit(ctx.key));
  }

  @Override
  public Node visitBinaryOperator(MCCodeParser.BinaryOperatorContext ctx) {
    Operator operator;
    if (ctx.IN() != null) {
      if (ctx.NOT() == null) {
        operator = Operator.IN;
      } else {
        operator = Operator.NOT_IN;
      }
    } else {
      operator = Operator.fromString(ctx.operator.getText());
    }
    //noinspection ConstantConditions
    return new BinaryOperatorNode(operator, super.visit(ctx.left), super.visit(ctx.right));
  }
}
