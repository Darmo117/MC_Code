package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeBaseVisitor;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeParser;

public class ProgramVisitor extends MCCodeBaseVisitor<Program> {
  private final ProgramManager programManager;

  public ProgramVisitor(final ProgramManager programManager) {
    this.programManager = programManager;
  }

  @Override
  public Program visitModule(MCCodeParser.ModuleContext ctx) {
    return super.visitModule(ctx); // TODO
  }
}
