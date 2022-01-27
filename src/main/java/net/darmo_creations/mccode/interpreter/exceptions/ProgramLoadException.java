package net.darmo_creations.mccode.interpreter.exceptions;

/**
 * Base exception for program loading related errors.
 */
public class ProgramLoadException extends MCCodeException {
  private final String programName;

  /**
   * Create an exception.
   *
   * @param programName Program’s name.
   */
  public ProgramLoadException(final String translationKey, final String programName) {
    super(translationKey);
    this.programName = programName;
  }

  /**
   * Return the unlocalized string of the error message.
   */
  public String getTranslationKey() {
    return this.getMessage();
  }

  /**
   * Return the name of the program.
   */
  public String getProgramName() {
    return this.programName;
  }
}
