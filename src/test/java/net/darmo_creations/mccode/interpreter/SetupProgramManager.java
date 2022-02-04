package net.darmo_creations.mccode.interpreter;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class SetupProgramManager implements BeforeAllCallback {
  @Override
  public void beforeAll(ExtensionContext context) {
    String uniqueKey = this.getClass().getName();
    Object value = context.getRoot().getStore(GLOBAL).get(uniqueKey);
    if (value == null) {
      System.out.println("Program manager setup");
      context.getRoot().getStore(GLOBAL).put(uniqueKey, this);
      ProgramManager.declareDefaultBuiltinTypes();
      ProgramManager.declareDefaultBuiltinFunctions();
      ProgramManager.declareType(DummyType.class);
      ProgramManager.initialize();
    }
  }
}
