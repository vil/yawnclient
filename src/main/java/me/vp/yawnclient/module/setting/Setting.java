package me.vp.yawnclient.module.setting;

import me.vp.yawnclient.module.Module;

public abstract class Setting {
	public String name;
	public Module parent;
    public String type;

	public boolean isModeSetting(){
		return this.type.equalsIgnoreCase("ModeSetting");
	}

	public boolean isBooleanSetting(){
		return this.type.equalsIgnoreCase("BooleanSetting");
	}

	public boolean isNumberSetting(){
		return this.type.equalsIgnoreCase("NumberSetting");
	}

}
