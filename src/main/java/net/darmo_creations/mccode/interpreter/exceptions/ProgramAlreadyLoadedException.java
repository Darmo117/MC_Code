package net.darmo_creations.mccode.interpreter.exceptions;

/**
 * Exception thrown when attempting to re-load an already loaded program.
 */
public class ProgramAlreadyLoadedException extends ProgramLoadException {
  /**
   * Create an exception.
   *
   * @param programName Program’s name.
   */
  public ProgramAlreadyLoadedException(final String programName) {
    super("mccode.interpreter.error.program_already_loaded", programName);
  }
}
