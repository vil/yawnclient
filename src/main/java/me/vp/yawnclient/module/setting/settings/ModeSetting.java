package me.vp.yawnclient.module.setting.settings;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.Setting;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
	public int index;
	public List<String> modes;

	public ModeSetting(String name, Module parent, String... modes) {
		this.name = name;
		this.parent = parent;
		this.modes = Arrays.asList(modes);
		this.index = 0;
        YawnClient.printLog(String.valueOf(this.index));
	}

	public String getMode() {
		return this.modes.get(this.index);
	}

	public void setMode(String mode) {
		this.index = this.modes.indexOf(mode);

		if (YawnClient.INSTANCE.save != null) {
			try {
				YawnClient.INSTANCE.save.saveSettings();
			} catch (Exception ignored) {}
		}
	}

	public boolean is(String mode) {
		return (this.index == this.modes.indexOf(mode));
	}

	public void cycle() {
		if (this.index < this.modes.size() - 1) {
			this.index++;
		} else {
			this.index = 0;
		}

		if (YawnClient.INSTANCE.save != null) {
			try {
				YawnClient.INSTANCE.save.saveModules();
			} catch (Exception ignored) {}
		}
	}
}
