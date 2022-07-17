package me.vp.yawnclient.module.setting.settings;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.Setting;

public class KeybindSetting extends Setting {

    public int code;

    public KeybindSetting(int code) {
        this.name = "KeyBind";
        this.code = code;
    }

    public KeybindSetting(Module module) {
        // TODO Auto-generated constructor stub
    }

    public int getKeyCode() {
        return this.code;
    }

    public void setKeyCode(int code) {
        this.code = code;

        if (YawnClient.INSTANCE.save != null) {
            try {
                YawnClient.INSTANCE.save.saveSettings();
            } catch (Exception ignored) {
            }
        }
    }

}
