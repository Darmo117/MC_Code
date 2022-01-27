package net.darmo_creations.mccode.interpreter.parser;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeLexer;
import net.darmo_creations.mccode.interpreter.parser.antlr4.MCCodeParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

public class ProgramParser {
  public static Program parse(final ProgramManager programManager, final String script) {
    MCCodeLexer lexer = new MCCodeLexer(CharStreams.fromString(script));
    MCCodeParser parser = new MCCodeParser(new CommonTokenStream(lexer));
    ErrorListener errorListener = new ErrorListener();
    parser.addErrorListener(errorListener);
    return new ProgramVisitor(programManager).visit(parser.module());
  }

  /**
   * Simple error listener to throw syntax errors instead of just logging them.
   */
  private static class ErrorListener implements ANTLRErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
      throw new SyntaxErrorException(msg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
    }
  }
}
