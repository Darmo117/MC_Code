package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeBaseVisitor;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeParser;
import net.darmo_creations.mccode.interpreter.statements.Statement;

import java.util.List;
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
    Integer scheduleDelay = null;
    Integer repeatAmount = null;
    if (ctx.SCHED() != null) {
      scheduleDelay = Integer.parseInt(ctx.ticks.getText());
      if (ctx.REPEAT() != null) {
        String timesText = ctx.times.getText();
        if ("forever".equals(timesText)) {
          repeatAmount = Integer.MAX_VALUE;
        } else {
          repeatAmount = Integer.parseInt(timesText);
        }
      }
    }

    StatementVisitor statementVisitor = new StatementVisitor();
    List<Statement> statements = ctx.global_statement().stream().map(statementVisitor::visit).collect(Collectors.toList());

    return new Program(this.programName, statements, scheduleDelay, repeatAmount, this.programManager);
  }
}
