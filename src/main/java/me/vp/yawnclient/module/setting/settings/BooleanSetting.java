package me.vp.yawnclient.module.setting.settings;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.Setting;


public class BooleanSetting extends Setting {
    public boolean enabled;

    public BooleanSetting(String name, Module parent, boolean enabled) {
        this.name = name;
        this.parent = parent;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (YawnClient.INSTANCE.save != null) {
            try {
                YawnClient.INSTANCE.save.saveSettings();
            } catch (Exception ignored) {
            }
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }
}
