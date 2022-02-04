package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.nodes.*;
import net.darmo_creations.mccode.interpreter.statements.*;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.darmo_creations.mccode.interpreter.type_wrappers.UnaryOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProgramParserTest {
  ProgramManager pm;

  @BeforeEach
  void setUp() {
    this.pm = new ProgramManager("pm");
  }

  @Test
  void parseScheduleNoRepeat() {
    Program p = new Program("p", Collections.singletonList(
        new ExpressionStatement(new VariableNode("a"))
    ), 1, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "schedule 1; a;"));
  }

  @Test
  void parseScheduleRepeatFixed() {
    Program p = new Program("p", Collections.singletonList(
        new ExpressionStatement(new VariableNode("a"))
    ), 1, 2, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "schedule 1 repeat 2; a;"));
  }

  @Test
  void parseScheduleRepeatForever() {
    Program p = new Program("p", Collections.singletonList(
        new ExpressionStatement(new VariableNode("a"))
    ), 1, Integer.MAX_VALUE, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "schedule 1 repeat forever; a;"));
  }

  @Test
  void parseScheduleRepeatError() {
    assertThrows(SyntaxErrorException.class, () -> ProgramParser.parse(this.pm, "p", "schedule -1;"));
    assertThrows(MCCodeRuntimeException.class, () -> ProgramParser.parse(this.pm, "p", "schedule 1 repeat 0;"));
    assertThrows(SyntaxErrorException.class, () -> ProgramParser.parse(this.pm, "p", "schedule 1 repeat -1;"));
  }

  @ParameterizedTest
  @EnumSource(AssigmentOperator.class)
  void parseAssignVariableStatement(AssigmentOperator op) {
    Program p = new Program("p", Collections.singletonList(
        new AssignVariableStatement("a", op, new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", String.format("a %s 1;", op.getSymbol())));
  }

  @Test
  void parseDeclareVariableStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "var a := 1;"));
  }

  @Test
  void parseDeclarePublicVariableStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(true, false, false, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "public var a := 1;"));
  }

  @Test
  void parseDeclarePublicEditableVariableStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(true, true, false, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "public editable var a := 1;"));
  }

  @Test
  void parseDeclareConstStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(false, false, true, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "const a := 1;"));
  }

  @Test
  void parseDeclarePublicConstStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(true, false, true, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "public const a := 1;"));
  }

  @Test
  void parseDefineFunctionNoParamsStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DefineFunctionStatement("f", Collections.emptyList(), Collections.singletonList(new ReturnStatement(new IntLiteralNode(1))))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "function f() return 1; end"));
  }

  @Test
  void parseDefineFunctionOneParamStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DefineFunctionStatement("f", Collections.singletonList("a"), Collections.singletonList(new ReturnStatement(new VariableNode("a"))))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "function f(a) return a; end"));
  }

  @Test
  void parseDefineFunctionTwoParamStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DefineFunctionStatement("f", Arrays.asList("a", "b"), Collections.singletonList(new ReturnStatement(new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("a"), new VariableNode("b")))))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "function f(a, b) return a + b; end"));
  }

  @Test
  void parseDeleteVariableStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeleteVariableStatement("a")
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "del a;"));
  }

  @Test
  void parseExpressionStatement() {
    Program p = new Program("p", Collections.singletonList(new ExpressionStatement(new VariableNode("a"))), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "a;"));
  }

  @Test
  void parseForLoopStatement() {
    Program p = new Program("p", Collections.singletonList(
        new ForLoopStatement("i", new FunctionCallNode(new VariableNode("range"),
            Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1))),
            Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"), Collections.singletonList(new VariableNode("i"))))))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "for i in range(1, 3, 1) do print(i); end"));
  }

  // TODO Test If

  @Test
  void parseIfStatement() {
    Program p = new Program("p", Collections.singletonList(
        new IfStatement(
            Collections.singletonList(new VariableNode("b")),
            Collections.singletonList(Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                Collections.singletonList(new IntLiteralNode(1)))))),
            Collections.emptyList()
        )
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); end"));
  }

  @Test
  void parseIfElseStatement() {
    Program p = new Program("p", Collections.singletonList(
        new IfStatement(
            Collections.singletonList(new VariableNode("b")),
            Collections.singletonList(
                Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                    Collections.singletonList(new IntLiteralNode(1)))))
            ),
            Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                Collections.singletonList(new IntLiteralNode(2)))))
        )
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); else print(2); end"));
  }

  @Test
  void parseIfElseifStatement() {
    Program p = new Program("p", Collections.singletonList(
        new IfStatement(
            Arrays.asList(
                new VariableNode("b"),
                new VariableNode("c")
            ),
            Arrays.asList(
                Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                    Collections.singletonList(new IntLiteralNode(1))))),
                Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                    Collections.singletonList(new IntLiteralNode(2)))))
            ),
            Collections.emptyList()
        )
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); elseif c then print(2); end"));
  }

  @Test
  void parseIfElseifElseStatement() {
    Program p = new Program("p", Collections.singletonList(
        new IfStatement(
            Arrays.asList(
                new VariableNode("b"),
                new VariableNode("c")
            ),
            Arrays.asList(
                Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                    Collections.singletonList(new IntLiteralNode(1))))),
                Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                    Collections.singletonList(new IntLiteralNode(2)))))
            ),
            Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                Collections.singletonList(new IntLiteralNode(3)))))
        )
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); elseif c then print(2); else print(3); end"));
  }

  @Test
  void parseMultipleElsifsStatement() {
    Program p = new Program("p", Collections.singletonList(
        new IfStatement(
            Arrays.asList(
                new VariableNode("b"),
                new VariableNode("c"),
                new UnaryOperatorNode(UnaryOperator.NOT, new VariableNode("d"))
            ),
            Arrays.asList(
                Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                    Collections.singletonList(new IntLiteralNode(1))))),
                Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                    Collections.singletonList(new IntLiteralNode(2))))),
                Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"),
                    Collections.singletonList(new IntLiteralNode(3)))))
            ),
            Collections.emptyList()
        )
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); elseif c then print(2); elseif not d then print(3); end"));
  }

  @ParameterizedTest
  @EnumSource(AssigmentOperator.class)
  void parseSetItemStatement(AssigmentOperator op) {
    Program p = new Program("p", Collections.singletonList(
        new SetItemStatement(new VariableNode("a"), new IntLiteralNode(1), op, new IntLiteralNode(2))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", String.format("a[1] %s 2;", op.getSymbol())));
  }

  @ParameterizedTest
  @EnumSource(AssigmentOperator.class)
  void parseSetItemKeyStatement(AssigmentOperator op) {
    Program p = new Program("p", Collections.singletonList(
        new SetItemStatement(new VariableNode("a"), new StringLiteralNode("b"), op, new IntLiteralNode(2))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", String.format("a[\"b\"] %s 2;", op.getSymbol())));
  }

  @ParameterizedTest
  @EnumSource(AssigmentOperator.class)
  void parseSetPropertyStatement(AssigmentOperator op) {
    Program p = new Program("p", Collections.singletonList(
        new SetPropertyStatement(new VariableNode("a"), "b", op, new IntLiteralNode(2))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", String.format("a.b %s 2;", op.getSymbol())));
  }

  @Test
  void parseWaitStatement() {
    Program p = new Program("p", Collections.singletonList(
        new WaitStatement(new VariableNode("a"))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "wait a;"));
  }

  @Test
  void parseWhileLoopStatement() {
    Program p = new Program("p", Collections.singletonList(
        new WhileLoopStatement(new BinaryOperatorNode(BinaryOperator.GT, new VariableNode("a"), new IntLiteralNode(1)),
            Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"), Collections.singletonList(new FloatLiteralNode(1.0))))))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "while a > 1 do print(1.0); end"));
  }
}
