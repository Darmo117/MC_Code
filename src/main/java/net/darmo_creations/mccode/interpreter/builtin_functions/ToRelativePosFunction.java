package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.BooleanType;
import net.darmo_creations.mccode.interpreter.type_wrappers.PosType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.RelativeBlockPos;
import net.minecraft.util.math.BlockPos;

/**
 * Function that casts a value into a {@link RelativeBlockPos} object.
 */
@Doc("Casts a value into a relative position.")
public class ToRelativePosFunction extends BuiltinFunction {
  /**
   * Create a function that casts a value into a {@link RelativeBlockPos} object.
   */
  public ToRelativePosFunction() {
    super("to_relative_pos", ProgramManager.getTypeInstance(PosType.class),
        new Parameter("pos", ProgramManager.getTypeInstance(AnyType.class)),
        new Parameter("x_relative", ProgramManager.getTypeInstance(BooleanType.class)),
        new Parameter("y_relative", ProgramManager.getTypeInstance(BooleanType.class)),
        new Parameter("z_relative", ProgramManager.getTypeInstance(BooleanType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    Object posObject = this.getParameterValue(scope, 0);
    Boolean xRelative = this.getParameterValue(scope, 1);
    Boolean yRelative = this.getParameterValue(scope, 2);
    Boolean zRelative = this.getParameterValue(scope, 3);
    BlockPos pos = ProgramManager.getTypeInstance(PosType.class).explicitCast(scope, posObject);
    return new RelativeBlockPos(pos, xRelative, yRelative, zRelative);
  }
}
