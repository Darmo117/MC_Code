package net.darmo_creations.mccode.interpreter;

/**
 * A class that reports an error that occured during execution of a program.
 */
public class ProgramErrorReport {
  private final Scope scope;
  private final String translationKey;
  private final Object[] args;

  /**
   * Create an error report.
   *
   * @param scope          Program that throwed the error.
   * @param translationKey Error’s unlocalized translation key.
   * @param args           Report’s arguments to be used for translation of the error message.
   */
  public ProgramErrorReport(final Scope scope, final String translationKey, final Object... args) {
    this.translationKey = translationKey;
    this.scope = scope;
    this.args = args;
  }

  /**
   * Return scope of the program that throwed the error.
   */
  public Scope getScope() {
    return this.scope;
  }

  /**
   * Error’s unlocalized translation key.
   */
  public String getTranslationKey() {
    return this.translationKey;
  }

  /**
   * Report’s arguments to be used for translation of the error message.
   */
  public Object[] getArgs() {
    return this.args;
  }
}
