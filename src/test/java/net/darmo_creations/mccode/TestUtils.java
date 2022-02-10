package net.darmo_creations.mccode;

import net.darmo_creations.mccode.interpreter.nodes.*;

import java.util.Arrays;
import java.util.Collections;

public final class TestUtils {
  public static Node blockPosNode(final int x, final int y, final int z) {
    return new FunctionCallNode(new VariableNode("to_pos", 0, 0),
        Collections.singletonList(new ListLiteralNode(Arrays.asList(new IntLiteralNode(x, 0, 0), new IntLiteralNode(y, 0, 0), new IntLiteralNode(z, 0, 0)), 0, 0)), 0, 0);
  }

  public static Node nodeList(final Node... values) {
    return new ListLiteralNode(Arrays.asList(values), 0, 0);
  }

  public static Node nodeSet(final Node... values) {
    return new SetLiteralNode(Arrays.asList(values), 0, 0);
  }

  public static Node nodeMap(final String key, final Node value) {
    return new MapLiteralNode(Collections.singletonMap(key, value), 0, 0);
  }

  private TestUtils() {
  }
}
