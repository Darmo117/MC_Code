package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EmptyCollectionException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.darmo_creations.mccode.interpreter.types.Range;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.codehaus.plexus.util.cli.Arg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class MaxFunctionTest extends BuiltinFunctionTest<MaxFunction> {
  @Test
  void getName() {
    assertEquals("max", this.function.getName());
  }

  @Test
  void getReturnType() {
    assertSame(AnyType.class, this.function.getReturnType().getClass());
  }

  @Test
  void getParameters() {
    assertEquals(Collections.singletonList(new Parameter("_x0_", ProgramManager.getTypeInstance(AnyType.class))),
        this.function.getParameters());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApply")
  void apply(Object o, Object expected) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, o));
    assertEquals(expected, this.function.apply(this.p.getScope()));
  }

  public static Stream<Arguments> provideArgsForApply() {
    return Stream.of(
        Arguments.of(new MCList(Collections.singletonList(1)), 1),
        Arguments.of(new MCList(Arrays.asList(1, 3, 2)), 3),
        Arguments.of(new MCSet(Collections.singletonList(1)), 1),
        Arguments.of(new MCSet(Arrays.asList(1, 3, 2)), 3),
        Arguments.of("a", "a"),
        Arguments.of("aψb", "ψ"),
        Arguments.of(new Range(1, 3, 1), 2)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyError")
  void applyError(Object o, Class<? extends Throwable> c) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, o));
    assertThrows(c, () -> this.function.apply(this.p.getScope()));
  }

  public static Stream<Arguments> provideArgsForApplyError() {
    return Stream.of(
        Arguments.of("", EmptyCollectionException.class),
        Arguments.of(new MCList(), EmptyCollectionException.class),
        Arguments.of(new MCSet(), EmptyCollectionException.class),
        Arguments.of(new Range(1, 1, 1), EmptyCollectionException.class),

        Arguments.of(true, CastException.class),
        Arguments.of(1, CastException.class),
        Arguments.of(1.0, CastException.class),
        Arguments.of(null, CastException.class),
        Arguments.of(new MCMap(), CastException.class),
        Arguments.of(new Item(), CastException.class),
//        Arguments.of(Blocks.STONE, CastException.class), // TODO not initialized
        Arguments.of(new BlockPos(0, 0, 0), CastException.class),
        Arguments.of(new ResourceLocation("minecraft:stone"), CastException.class)
    );
  }

  @Override
  String functionName() {
    return "max";
  }
}
