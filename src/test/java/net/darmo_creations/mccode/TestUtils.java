package net.darmo_creations.mccode;

import net.darmo_creations.mccode.interpreter.nodes.*;

import java.util.Arrays;
import java.util.Collections;

public final class TestUtils {
  public static Node blockPosNode(final int x, final int y, final int z) {
    return new FunctionCallNode(new VariableNode("to_pos"),
        Collections.singletonList(new ListLiteralNode(Arrays.asList(new IntLiteralNode(x), new IntLiteralNode(y), new IntLiteralNode(z)))));
  }

  public static Node nodeList(final Node... values) {
    return new ListLiteralNode(Arrays.asList(values));
  }

  public static Node nodeSet(final Node... values) {
    return new SetLiteralNode(Arrays.asList(values));
  }

  public static Node nodeMap(final String key, final Node value) {
    return new MapLiteralNode(Collections.singletonMap(key, value));
  }

  private TestUtils() {
  }
}
