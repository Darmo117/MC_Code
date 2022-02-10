package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.nodes.*;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.darmo_creations.mccode.interpreter.type_wrappers.UnaryOperator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionParserTest {
  @Test
  void parseNullLiteral() {
    assertEquals(new NullLiteralNode(0, 0), ExpressionParser.parse("null"));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void parseIntLiteral(boolean b) {
    assertEquals(new BooleanLiteralNode(b, 0, 0), ExpressionParser.parse(String.valueOf(b)));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 13})
  void parseIntLiteral(int i) {
    assertEquals(new IntLiteralNode(i, 0, 0), ExpressionParser.parse(String.valueOf(i)));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForParseFloatLiteral")
  void parseFloatLiteral(double expected, String input) {
    assertEquals(new FloatLiteralNode(expected, 0, 0), ExpressionParser.parse(input));
  }

  static Stream<Arguments> provideArgsForParseFloatLiteral() {
    return Stream.of(
        Arguments.of(0.0, "0.0"),
        Arguments.of(0.0, "0."),
        Arguments.of(10.0, "10.0"),
        Arguments.of(10.0, "10."),
        Arguments.of(100.0, "10.0e1"),
        Arguments.of(100.0, "10.0E1"),
        Arguments.of(100.0, "10.e1"),
        Arguments.of(100.0, "10.E1"),
        Arguments.of(100.0, "10e1"),
        Arguments.of(100.0, "10E1"),
        Arguments.of(1.0, "10.0e-1"),
        Arguments.of(1.0, "10.0E-1"),
        Arguments.of(1.0, "10.e-1"),
        Arguments.of(1.0, "10.E-1"),
        Arguments.of(1.0, "10e-1"),
        Arguments.of(1.0, "10E-1")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForParseStringLiteral")
  void parseStringLiteral(String expected, String input) {
    assertEquals(new StringLiteralNode(expected, 0, 0), ExpressionParser.parse(input));
  }

  static Stream<Arguments> provideArgsForParseStringLiteral() {
    return Stream.of(
        Arguments.of("", "\"\""),
        Arguments.of("a", "\"a\""),
        Arguments.of("ab", "\"ab\""),
        Arguments.of("\n", "\"\\n\""),
        Arguments.of("\"", "\"\\\"\"")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForParseListLiteral")
  void parseListLiteral(List<Node> expectedList, String input) {
    assertEquals(new ListLiteralNode(expectedList, 0, 0), ExpressionParser.parse(input));
  }

  static Stream<Arguments> provideArgsForParseListLiteral() {
    return Stream.of(
        Arguments.of(Collections.emptyList(), "[]"),
        Arguments.of(Collections.singletonList(new IntLiteralNode(1, 0, 0)), "[1]"),
        Arguments.of(Collections.singletonList(new IntLiteralNode(1, 0, 0)), "[1,]"),
        Arguments.of(Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "[1,\"a\"]"),
        Arguments.of(Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "[1, \"a\"]"),
        Arguments.of(Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "[1, \"a\",]")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForParseSetLiteral")
  void parseSetLiteral(List<Node> expectedSet, String input) {
    assertEquals(new SetLiteralNode(expectedSet, 0, 0), ExpressionParser.parse(input));
  }

  static Stream<Arguments> provideArgsForParseSetLiteral() {
    return Stream.of(
        Arguments.of(Collections.singletonList(new IntLiteralNode(1, 0, 0)), "{1}"),
        Arguments.of(Collections.singletonList(new IntLiteralNode(1, 0, 0)), "{1,}"),
        Arguments.of(Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "{1,\"a\"}"),
        Arguments.of(Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "{1, \"a\"}"),
        Arguments.of(Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "{1, \"a\",}")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForParseMapLiteral")
  void parseSetLiteral(Map<String, Node> expectedMap, String input) {
    assertEquals(new MapLiteralNode(expectedMap, 0, 0), ExpressionParser.parse(input));
  }

  static Stream<Arguments> provideArgsForParseMapLiteral() {
    Map<String, Node> map = new HashMap<>();
    map.put("a", new IntLiteralNode(1, 0, 0));
    map.put("b", new StringLiteralNode("b", 0, 0));
    return Stream.of(
        Arguments.of(Collections.emptyMap(), "{}"),
        Arguments.of(Collections.singletonMap("a\n", new IntLiteralNode(1, 0, 0)), "{\"a\\n\":1}"),
        Arguments.of(Collections.singletonMap("a", new IntLiteralNode(1, 0, 0)), "{\"a\":1}"),
        Arguments.of(Collections.singletonMap("a", new IntLiteralNode(1, 0, 0)), "{\"a\": 1}"),
        Arguments.of(Collections.singletonMap("a", new IntLiteralNode(1, 0, 0)), "{\"a\":1,}"),
        Arguments.of(Collections.singletonMap("a", new IntLiteralNode(1, 0, 0)), "{\"a\": 1,}"),
        Arguments.of(map, "{\"a\":1,\"b\":\"b\"}"),
        Arguments.of(map, "{\"a\":1,\"b\":\"b\",}"),
        Arguments.of(map, "{\"a\":1, \"b\":\"b\"}"),
        Arguments.of(map, "{\"a\":1, \"b\":\"b\",}"),
        Arguments.of(map, "{\"a\": 1, \"b\": \"b\"}"),
        Arguments.of(map, "{\"a\": 1, \"b\": \"b\",}")
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {"a", "ab", "a1", "_a", "a_", "_1"})
  void parseVariable(String s) {
    assertEquals(new VariableNode(s, 0, 0), ExpressionParser.parse(s));
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"ITERATE", "LENGTH"}, mode = EnumSource.Mode.EXCLUDE)
  void parseUnaryOperator(UnaryOperator op) {
    assertEquals(new UnaryOperatorNode(op, new VariableNode("a", 0, 0), 0, 0),
        ExpressionParser.parse(op.getSymbol() + " a"));
  }

  @ParameterizedTest
  @EnumSource(value = BinaryOperator.class, names = {"GET_ITEM", "DEL_ITEM"}, mode = EnumSource.Mode.EXCLUDE)
  void parseBinaryOperator(BinaryOperator op) {
    assertEquals(new BinaryOperatorNode(op, new VariableNode("a", 0, 0), new VariableNode("b", 0, 0), 0, 0),
        ExpressionParser.parse("a " + op.getSymbol() + " b"));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForParseBinaryOperator")
  void parseBinaryOperator(Node expectedExpr, List<Node> expectedArgs, String input) {
    assertEquals(new FunctionCallNode(expectedExpr, expectedArgs, 0, 0), ExpressionParser.parse(input));
  }

  static Stream<Arguments> provideArgsForParseBinaryOperator() {
    return Stream.of(
        Arguments.of(new VariableNode("f", 0, 0), Collections.emptyList(), "f()"),
        Arguments.of(new VariableNode("f", 0, 0), Collections.singletonList(new IntLiteralNode(1, 0, 0)), "f(1)"),
        Arguments.of(new VariableNode("f", 0, 0), Collections.singletonList(new IntLiteralNode(1, 0, 0)), "f(1,)"),
        Arguments.of(new VariableNode("f", 0, 0), Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "f(1,\"a\")"),
        Arguments.of(new VariableNode("f", 0, 0), Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "f(1, \"a\")"),
        Arguments.of(new VariableNode("f", 0, 0), Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("a", 0, 0)), "f(1, \"a\",)"),
        Arguments.of(new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("a", 0, 0), new VariableNode("b", 0, 0), 0, 0), Collections.emptyList(), "(a + b)()")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForParsePropertyCall")
  void parsePropertyCall(Node expectedExpr, String propertyName, String input) {
    assertEquals(new PropertyCallNode(expectedExpr, propertyName, 0, 0), ExpressionParser.parse(input));
  }

  static Stream<Arguments> provideArgsForParsePropertyCall() {
    return Stream.of(
        Arguments.of(new VariableNode("a", 0, 0), "p", "a.p"),
        Arguments.of(new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("a", 0, 0), new VariableNode("b", 0, 0), 0, 0), "p", "(a + b).p")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForParseMethodCall")
  void parseMethodCall(Node expectedExpr, String methodName, List<Node> expectedArgs, String input) {
    assertEquals(new MethodCallNode(expectedExpr, methodName, expectedArgs, 0, 0), ExpressionParser.parse(input));
  }

  static Stream<Arguments> provideArgsForParseMethodCall() {
    return Stream.of(
        Arguments.of(new VariableNode("a", 0, 0), "m", Collections.emptyList(), "a.m()"),
        Arguments.of(new VariableNode("a", 0, 0), "m", Collections.singletonList(new IntLiteralNode(1, 0, 0)), "a.m(1)"),
        Arguments.of(new VariableNode("a", 0, 0), "m", Collections.singletonList(new IntLiteralNode(1, 0, 0)), "a.m(1,)"),
        Arguments.of(new VariableNode("a", 0, 0), "m", Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(2, 0, 0)), "a.m(1, 2)"),
        Arguments.of(new VariableNode("a", 0, 0), "m", Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(2, 0, 0)), "a.m(1, 2,)"),
        Arguments.of(new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("a", 0, 0), new VariableNode("b", 0, 0), 0, 0), "m", Collections.emptyList(), "(a + b).m()")
    );
  }
}
