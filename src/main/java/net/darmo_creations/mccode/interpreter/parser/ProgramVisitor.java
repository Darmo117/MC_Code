package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeBaseVisitor;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeParser;
import net.darmo_creations.mccode.interpreter.statements.ImportStatement;
import net.darmo_creations.mccode.interpreter.statements.Statement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Visitor for {@link Program} class.
 */
public class ProgramVisitor extends MCCodeBaseVisitor<Program> {
  private final ProgramManager programManager;
  private final String programName;

  /**
   * Create a visitor for the given program name.
   *
   * @param programManager Program manager that requests the program instance.
   * @param programName    Program’s name.
   */
  public ProgramVisitor(final ProgramManager programManager, final String programName) {
    this.programManager = programManager;
    this.programName = programName;
  }

  @Override
  public Program visitModule(MCCodeParser.ModuleContext ctx) {
    Long scheduleDelay = null;
    Long repeatAmount = null;
    if (ctx.SCHED() != null) {
      scheduleDelay = Long.parseLong(ctx.ticks.getText());
      if (ctx.REPEAT() != null) {
        String timesText = ctx.times.getText();
        if ("forever".equals(timesText)) {
          repeatAmount = Long.MAX_VALUE;
        } else {
          repeatAmount = Long.parseLong(timesText);
        }
      }
    }

    StatementVisitor statementVisitor = new StatementVisitor();

    Set<String> modules = new HashSet<>();
    List<Statement> statements = ctx.import_statement().stream().map(tree -> {
      ImportStatement stmt = (ImportStatement) statementVisitor.visit(tree);
      String modulePath = stmt.getModulePath();
      if (modules.contains(modulePath)) {
        throw new SyntaxErrorException(String.format("module %s imported twice", modulePath));
      }
      modules.add(modulePath);
      return stmt;
    }).collect(Collectors.toList());

    ctx.global_statement().stream().map(statementVisitor::visit).forEach(statements::add);

    return new Program(this.programName, statements, scheduleDelay, repeatAmount, this.programManager);
  }
}
