package me.vp.yawnclient.saveload;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.Setting;
import me.vp.yawnclient.module.setting.settings.*;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Save {
    public File MainDirectory;

    public Save() {
        MainDirectory = new File(MinecraftClient.getInstance().runDirectory, YawnClient.name);
        if(!MainDirectory.exists()) {
            MainDirectory.mkdir();
        }
    }

    public void save() {
        saveModules();
        saveSettings();
        savePrefix();
    }

    private void writeFile(ArrayList<String> toSave, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for(String string : toSave) {
                printWriter.println(string);
            }
            printWriter.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    public void saveModules() {
        try {
            File file = new File(MainDirectory, "modules.txt");
            ArrayList<String> toSave = new ArrayList<>();

            for(Module module : YawnClient.INSTANCE.moduleManager.getModules()) {
                if (module.isEnabled() && !module.getName().equals("clickgui") && !module.getName().equals("hudeditor") && !module.getName().equals("commandline") && !module.getName().equals("options")) {
                    toSave.add(module.getName());
                }
            }

            writeFile(toSave, file);
        } catch (Exception ignored) {
        }
    }

    public void saveSettings() {
        try {
            File file = new File(MainDirectory, "settings.txt");
            ArrayList<String> toSave = new ArrayList<>();

            for(Module mod : YawnClient.INSTANCE.moduleManager.modules) {
                for(Setting setting : mod.settings) {

                    if(setting instanceof BooleanSetting bool) {
                        toSave.add(mod.getName() + ":" + setting.name + ":" + bool.isEnabled());
                    }

                    if(setting instanceof NumberSetting number) {
                        toSave.add(mod.getName() + ":" + setting.name + ":" + number.getValue());
                    }

                    if(setting instanceof ModeSetting mode) {
                        toSave.add(mod.getName() + ":" + setting.name + ":" + mode.getMode());
                    }

                    if(setting instanceof ColorSetting color) {
                        toSave.add(setting.parent.getName() + ":" + setting.name + ":" + color.toInteger() + ":" + color.getRainbow());
                    }

                    if(setting instanceof KeybindSetting keybind) {
                        toSave.add(mod.getName() + ":" + setting.name + ":" + mod.getKey());
                    }
                }
            }

            writeFile(toSave, file);
        } catch (Exception ignored) {
        }
    }

    public void savePrefix() {
        try {
            File file = new File(MainDirectory, "prefix.txt");
            ArrayList<String> toSave = new ArrayList<>();

            toSave.add(YawnClient.INSTANCE.commandManager.prefix);

            writeFile(toSave, file);
        } catch (Exception ignored) {
        }
    }

}
