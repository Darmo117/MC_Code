package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.*;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.statements.ReturnStatement;
import net.darmo_creations.mccode.interpreter.types.*;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class FunctionTypeTest extends TypeTest<FunctionType> {
  BuiltinFunction builtinFunction;
  UserFunction userFunction;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.builtinFunction = new BuiltinFunction("f", ProgramManager.getTypeInstance(AnyType.class),
        new Parameter("p", ProgramManager.getTypeInstance(IntType.class))) {
      @Override
      public Object apply(Scope scope) {
        return null;
      }
    };
    this.userFunction = new UserFunction("g", Collections.singletonList("a"), Collections.singletonList(new ReturnStatement(new IntLiteralNode(1, 0, 0), 0, 0)));
    this.p.getScope().declareVariable(new Variable(this.builtinFunction.getName(), false, false, true, true, this.builtinFunction));
    this.p.getScope().declareVariable(new Variable(this.userFunction.getName(), false, false, true, true, this.userFunction));
  }

  @Test
  void getName() {
    assertEquals("function", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Function.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertFalse(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames(this.userFunction).isEmpty());
  }

  @Test
  void getPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getPropertyValue(this.p.getScope(), this.userFunction, "a"));
  }

  @Test
  void setPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), this.userFunction, "a", true));
  }

  @Test
  void getMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod("a"));
  }

  @Test
  void implicitCast() {
    assertSame(this.builtinFunction, this.typeInstance.implicitCast(this.p.getScope(), this.builtinFunction));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCastError")
  void implicitCast(Object o) {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCastError")
  void explicitCastError(Object o) {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForCastError() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(1),
        Arguments.of(1.0),
        Arguments.of((Object) null),
        Arguments.of(new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap()),
        Arguments.of(new Item()),
//        Arguments.of(new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new BlockPos(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone")),
        Arguments.of("")
    );
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(this.builtinFunction));
  }

  @Test
  void writeToNBTBuiltinFunction() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(FunctionType.NAME_KEY, "function");
    tag.setString(FunctionType.FUNCTION_TYPE_KEY, "builtin");
    tag.setString(FunctionType.FUNCTION_KEY, "f");
    assertEquals(tag, this.typeInstance.writeToNBT(this.builtinFunction));
  }

  @Test
  void readFromNBTBuiltinFunction() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(FunctionType.NAME_KEY, "function");
    tag.setString(FunctionType.FUNCTION_TYPE_KEY, "builtin");
    tag.setString(FunctionType.FUNCTION_KEY, "f");
    assertSame(this.builtinFunction, this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Test
  void writeToNBTUserFunction() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(FunctionType.NAME_KEY, "function");
    tag.setString(FunctionType.FUNCTION_TYPE_KEY, "user");
    tag.setTag(FunctionType.FUNCTION_KEY, this.userFunction.writeToNBT());
    assertEquals(tag, this.typeInstance.writeToNBT(this.userFunction));
  }

  @Test
  void readFromNBTUserFunction() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(FunctionType.NAME_KEY, "function");
    tag.setString(FunctionType.FUNCTION_TYPE_KEY, "user");
    tag.setTag(FunctionType.FUNCTION_KEY, this.userFunction.writeToNBT());
    assertEquals(this.userFunction, this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<FunctionType> getTypeClass() {
    return FunctionType.class;
  }
}
