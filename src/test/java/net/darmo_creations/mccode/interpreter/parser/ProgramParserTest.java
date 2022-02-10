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
    ), 1L, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "schedule 1; a;", false));
  }

  @Test
  void parseScheduleRepeatFixed() {
    Program p = new Program("p", Collections.singletonList(
        new ExpressionStatement(new VariableNode("a"))
    ), 1L, 2L, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "schedule 1 repeat 2; a;", false));
  }

  @Test
  void parseScheduleRepeatForever() {
    Program p = new Program("p", Collections.singletonList(
        new ExpressionStatement(new VariableNode("a"))
    ), 1L, Long.MAX_VALUE, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "schedule 1 repeat forever; a;", false));
  }

  @Test
  void parseScheduleRepeatError() {
    assertThrows(SyntaxErrorException.class, () -> ProgramParser.parse(this.pm, "p", "schedule -1;", false));
    assertThrows(MCCodeRuntimeException.class, () -> ProgramParser.parse(this.pm, "p", "schedule 1 repeat 0;", false));
    assertThrows(SyntaxErrorException.class, () -> ProgramParser.parse(this.pm, "p", "schedule 1 repeat -1;", false));
  }

  @Test
  void parseSimpleImportStatement() {
    Program p = new Program("p", Collections.singletonList(
        new ImportStatement(Collections.singletonList("a"), null)
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "import a;", false));
  }

  @Test
  void parsePathImportStatement() {
    Program p = new Program("p", Collections.singletonList(
        new ImportStatement(Arrays.asList("a", "b"), null)
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "import a.b;", false));
  }

  @Test
  void parseSimpleImportStatementWithAlias() {
    Program p = new Program("p", Collections.singletonList(
        new ImportStatement(Collections.singletonList("a"), "b")
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "import a as b;", false));
  }

  @Test
  void parsePathImportStatementWithAlias() {
    Program p = new Program("p", Collections.singletonList(
        new ImportStatement(Arrays.asList("a", "b"), "b")
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "import a.b as b;", false));
  }

  @Test
  void parseDuplicateSimpleImportsError() {
    assertThrows(SyntaxErrorException.class, () -> ProgramParser.parse(this.pm, "p", "import a as b; import a as c;", false));
  }

  @Test
  void parseDuplicatePathImportsError() {
    assertThrows(SyntaxErrorException.class, () -> ProgramParser.parse(this.pm, "p", "import a.b as b; import a.b as c;", false));
  }

  @ParameterizedTest
  @EnumSource(AssigmentOperator.class)
  void parseAssignVariableStatement(AssigmentOperator op) {
    Program p = new Program("p", Collections.singletonList(
        new AssignVariableStatement("a", op, new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", String.format("a %s 1;", op.getSymbol()), false));
  }

  @Test
  void parseDeclareVariableStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "var a := 1;", false));
  }

  @Test
  void parseDeclarePublicVariableStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(true, false, false, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "public var a := 1;", false));
  }

  @Test
  void parseDeclarePublicEditableVariableStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(true, true, false, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "public editable var a := 1;", false));
  }

  @Test
  void parseDeclareConstStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(false, false, true, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "const a := 1;", false));
  }

  @Test
  void parseDeclarePublicConstStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeclareVariableStatement(true, false, true, "a", new IntLiteralNode(1))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "public const a := 1;", false));
  }

  @Test
  void parseDefineFunctionNoParamsStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DefineFunctionStatement("f", Collections.emptyList(), Collections.singletonList(new ReturnStatement(new IntLiteralNode(1))), false)
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "function f() return 1; end", false));
  }

  @Test
  void parseDefineFunctionOneParamStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DefineFunctionStatement("f", Collections.singletonList("a"), Collections.singletonList(new ReturnStatement(new VariableNode("a"))), false)
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "function f(a) return a; end", false));
  }

  @Test
  void parseDefineFunctionTwoParamStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DefineFunctionStatement("f", Arrays.asList("a", "b"), Collections.singletonList(new ReturnStatement(new BinaryOperatorNode(BinaryOperator.PLUS, new VariableNode("a"), new VariableNode("b")))), false)
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "function f(a, b) return a + b; end", false));
  }

  @Test
  void parseDeleteVariableStatement() {
    Program p = new Program("p", Collections.singletonList(
        new DeleteVariableStatement("a")
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "del a;", false));
  }

  @Test
  void parseExpressionStatement() {
    Program p = new Program("p", Collections.singletonList(new ExpressionStatement(new VariableNode("a"))), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "a;", false));
  }

  @Test
  void parseForLoopStatement() {
    Program p = new Program("p", Collections.singletonList(
        new ForLoopStatement("i", new FunctionCallNode(new VariableNode("range"),
            Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1))),
            Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"), Collections.singletonList(new VariableNode("i"))))))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "for i in range(1, 3, 1) do print(i); end", false));
  }

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
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); end", false));
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
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); else print(2); end", false));
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
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); elseif c then print(2); end", false));
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
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); elseif c then print(2); else print(3); end", false));
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
    assertEquals(p, ProgramParser.parse(this.pm, "p", "if b then print(1); elseif c then print(2); elseif not d then print(3); end", false));
  }

  @ParameterizedTest
  @EnumSource(AssigmentOperator.class)
  void parseSetItemStatement(AssigmentOperator op) {
    Program p = new Program("p", Collections.singletonList(
        new SetItemStatement(new VariableNode("a"), new IntLiteralNode(1), op, new IntLiteralNode(2))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", String.format("a[1] %s 2;", op.getSymbol()), false));
  }

  @ParameterizedTest
  @EnumSource(AssigmentOperator.class)
  void parseSetItemKeyStatement(AssigmentOperator op) {
    Program p = new Program("p", Collections.singletonList(
        new SetItemStatement(new VariableNode("a"), new StringLiteralNode("b"), op, new IntLiteralNode(2))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", String.format("a[\"b\"] %s 2;", op.getSymbol()), false));
  }

  @ParameterizedTest
  @EnumSource(AssigmentOperator.class)
  void parseSetPropertyStatement(AssigmentOperator op) {
    Program p = new Program("p", Collections.singletonList(
        new SetPropertyStatement(new VariableNode("a"), "b", op, new IntLiteralNode(2))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", String.format("a.b %s 2;", op.getSymbol()), false));
  }

  @Test
  void parseWaitStatement() {
    Program p = new Program("p", Collections.singletonList(
        new WaitStatement(new VariableNode("a"))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "wait a;", false));
  }

  @Test
  void parseWhileLoopStatement() {
    Program p = new Program("p", Collections.singletonList(
        new WhileLoopStatement(new BinaryOperatorNode(BinaryOperator.GT, new VariableNode("a"), new IntLiteralNode(1)),
            Collections.singletonList(new ExpressionStatement(new FunctionCallNode(new VariableNode("print"), Collections.singletonList(new FloatLiteralNode(1.0))))))
    ), null, null, this.pm);
    assertEquals(p, ProgramParser.parse(this.pm, "p", "while a > 1 do print(1.0); end", false));
  }
}
