package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

public class Variable {
  private static final String NAME_KEY = "Name";
  private static final String PUBLIC_KEY = "Public";
  private static final String EDITABLE_KEY = "Editable";
  private static final String CONSTANT_KEY = "Constant";
  private static final String VALUE_KEY = "Value";
  private static final String TYPE_KEY = "Type";

  private final String name;
  private final boolean publiclyVisible;
  private final boolean editableThroughCommands;
  private final boolean constant;
  private final boolean deletable;
  private Object value;

  public Variable(final String name, boolean publiclyVisible, final boolean editableThroughCommands, final boolean constant, final boolean deletable, Object value) {
    if (constant && editableThroughCommands) {
      throw new MCCodeException("constant cannot be editable through commands");
    }
    if (!publiclyVisible && editableThroughCommands) {
      throw new MCCodeException("private variable cannot be editable through commands");
    }
    this.name = name;
    this.value = value;
    this.constant = constant;
    this.publiclyVisible = publiclyVisible;
    this.editableThroughCommands = editableThroughCommands;
    this.deletable = deletable;
  }

  public Variable(final NBTTagCompound tag, final Scope scope) {
    this.name = tag.getString(NAME_KEY);
    this.publiclyVisible = tag.getBoolean(PUBLIC_KEY);
    this.editableThroughCommands = tag.getBoolean(EDITABLE_KEY);
    this.constant = tag.getBoolean(CONSTANT_KEY);
    this.value = scope.getProgramManager().getTypeForName(tag.getString(TYPE_KEY)).readFromNBT(scope, tag.getCompoundTag(VALUE_KEY));
    this.deletable = false;
  }

  public String getName() {
    return this.name;
  }

  public Object getValue() {
    return this.value;
  }

  public void setValue(final Scope scope, final Object value, final boolean fromCommand) {
    if (this.isConstant()) {
      throw new EvaluationException(scope, "mccode.interpreter.error.setting_constant_variable", this.name);
    }
    if (!this.isEditableThroughCommands() && fromCommand) {
      throw new EvaluationException(scope, "mccode.interpreter.error.setting_from_command", this.name);
    }
    this.value = value;
  }

  public boolean isConstant() {
    return this.constant;
  }

  public boolean isPubliclyVisible() {
    return this.publiclyVisible;
  }

  public boolean isEditableThroughCommands() {
    return this.editableThroughCommands;
  }

  public boolean isDeletable() {
    return this.deletable;
  }

  public NBTTagCompound writeToNBT(final Scope scope) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(NAME_KEY, this.name);
    tag.setBoolean(PUBLIC_KEY, this.publiclyVisible);
    tag.setBoolean(EDITABLE_KEY, this.editableThroughCommands);
    tag.setBoolean(CONSTANT_KEY, this.constant);
    Type<?> type = scope.getProgramManager().getTypeForValue(this.value);
    tag.setString(TYPE_KEY, type.getName());
    tag.setTag(VALUE_KEY, type.writeToNBT(scope, this.value));
    return tag;
  }
}
