package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.StringLiteralNode;
import net.darmo_creations.mccode.interpreter.statements.AssigmentOperator;
import net.darmo_creations.mccode.interpreter.statements.AssignVariableStatement;
import net.darmo_creations.mccode.interpreter.statements.DeclareVariableStatement;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.darmo_creations.mccode.interpreter.types.Range;
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
class ModuleTypeTest extends TypeTest<ModuleType> {
  Program p;
  Program m;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    ProgramManager pm = new ProgramManager("pm");
    this.p = new Program("p", Collections.emptyList(), null, null, pm);
    this.m = new Program("m", Collections.singletonList(
        new DeclareVariableStatement(true, true, false, "a", new IntLiteralNode(1, 0, 0), 0, 0)), pm);
    this.m.execute();
  }

  @Test
  void getName() {
    assertEquals("module", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Program.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertFalse(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertEquals(Collections.singletonList("__name__"), this.typeInstance.getPropertiesNames(this.m));
  }

  @Test
  void getNameProperty() {
    assertEquals("m", this.typeInstance.getPropertyValue(this.p.getScope(), this.m, "__name__"));
  }

  @Test
  void getNamePropertyOverriden() {
    Program m = new Program("m", Collections.singletonList(
        new AssignVariableStatement("__name__", AssigmentOperator.ASSIGN, new StringLiteralNode("test", 0, 0), 0, 0)), new ProgramManager("pm"));
    m.execute();
    assertEquals("test", this.typeInstance.getPropertyValue(this.p.getScope(), m, "__name__"));
  }

  @Test
  void getPropertyVariable() {
    assertEquals(1L, this.typeInstance.getPropertyValue(this.p.getScope(), this.m, "a"));
  }

  @Test
  void getPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getPropertyValue(this.p.getScope(), this.m, "b"));
  }

  @Test
  void setPropertyVariable() {
    MCList list = new MCList();
    this.typeInstance.setPropertyValue(this.p.getScope(), this.m, "a", list);
    Object value = this.typeInstance.getPropertyValue(this.p.getScope(), this.m, "a");
    assertEquals(list, value);
    assertNotSame(list, value);
  }

  @Test
  void setPropertyVariableError1() {
    this.m = new Program("m", Collections.singletonList(
        new DeclareVariableStatement(true, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0)), new ProgramManager("pm"));
    this.m.execute();
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), this.m, "a", 2));
  }

  @Test
  void setPropertyVariableError2() {
    this.m = new Program("m", Collections.singletonList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0)), new ProgramManager("pm"));
    this.m.execute();
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), this.m, "a", 2));
  }

  @Test
  void setNamePropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), this.m, "__name__", 2));
  }

  @Test
  void setPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), this.m, "b", true));
  }

  @Test
  void getMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod("a"));
  }

  @Test
  void implicitCast() {
    assertSame(this.m, this.typeInstance.implicitCast(this.p.getScope(), this.m));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCastError")
  void explicitCastError(Object o) {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCastError() {
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
    assertTrue(this.typeInstance.toBoolean(this.m));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(ModuleType.NAME_KEY, "module");
    tag.setTag(ModuleType.MODULE_KEY, this.m.writeToNBT());
    assertEquals(tag, this.typeInstance.writeToNBT(this.m));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(ModuleType.NAME_KEY, "module");
    tag.setTag(ModuleType.MODULE_KEY, this.m.writeToNBT());
    assertEquals(this.m, this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<ModuleType> getTypeClass() {
    return ModuleType.class;
  }
}
