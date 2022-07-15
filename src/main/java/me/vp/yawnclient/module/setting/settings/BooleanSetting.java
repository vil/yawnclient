package me.vp.yawnclient.module.setting.settings;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.Setting;

import java.util.ArrayList;

//the only value you need to worry about is the default value, it can either be true or false.

public class BooleanSetting extends Setting {
	public boolean enabled;

	public BooleanSetting(String name, Module parent, boolean enabled) {
		this.name = name;
		this.parent = parent;
		this.enabled = enabled;
        this.type = "BooleanSetting";
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if(YawnClient.INSTANCE.save != null) {
			try {
				YawnClient.INSTANCE.save.saveSettings();
			} catch (Exception ignored) {}
		}
	}

	public void toggle() {
		setEnabled(!enabled);
	}
}
