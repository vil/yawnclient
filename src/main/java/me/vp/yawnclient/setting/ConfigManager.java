package me.vp.yawnclient.setting;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.setting.settings.*;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.util.ArrayList;
public class ConfigManager {
    public File MainDirectory;

    public ConfigManager() {
        MainDirectory = new File(MinecraftClient.getInstance().runDirectory, YawnClient.name);
        if (!MainDirectory.exists()) {
            MainDirectory.mkdir();
        }
    }

    // ---------- Save ----------

    public void save() {
        saveModules();
        saveSettings();
        savePrefix();
    }

    private void writeFile(ArrayList<String> toSave, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for (String string : toSave) {
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

            for (Module module : YawnClient.INSTANCE.moduleManager.getModules()) {
                if (module.isEnabled() && !module.getName().equalsIgnoreCase("Clickgui") && !module.getName().equalsIgnoreCase("hudeditor") &&
                        !module.getName().equalsIgnoreCase("commandline") && !module.getName().equalsIgnoreCase("options")) {
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

            for (Module mod : YawnClient.INSTANCE.moduleManager.modules) {
                for (Setting setting : mod.settings) {

                    if (setting instanceof BooleanSetting bool) {
                        toSave.add(mod.getName() + ":" + setting.name + ":" + bool.isEnabled());
                    }

                    if (setting instanceof NumberSetting number) {
                        toSave.add(mod.getName() + ":" + setting.name + ":" + number.getValue());
                    }

                    if (setting instanceof ModeSetting mode) {
                        toSave.add(mod.getName() + ":" + setting.name + ":" + mode.getMode());
                    }

                    if (setting instanceof ColorSetting color) {
                        toSave.add(mod.getName() + ":" + setting.name + ":" + color.toInteger() + ":" + color.getRainbow());
                    }

                    if (setting instanceof KeybindSetting keybind) {
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

    // ---------- Load ----------
    public void load() {
        loadModules();
        loadSettings();
        loadPrefix();
    }

    public void loadModules() {
        try {
            File file = new File(MainDirectory, "modules.txt");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                for (Module m : YawnClient.INSTANCE.moduleManager.getModules()) {
                    if (m.getName().equals(line)) {
                        m.toggle();
                        YawnClient.printInfo(m.getName() + " enabled.");
                    }
                }
            }

            br.close();
        } catch (Exception ignored) {
        }
    }

    public void loadSettings() {
        try {
            File file = new File(MainDirectory, "settings.txt");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;

            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String modname = curLine.split(":")[0];
                String settingname = curLine.split(":")[1];
                String value = curLine.split(":")[2];

                Module module = YawnClient.INSTANCE.moduleManager.getModule(modname);
                if (module != null) {
                    if (!settingname.equals("KeyBind")) {
                        Setting setting = YawnClient.INSTANCE.settingManager.getSettingByName(module, settingname);
                        if (setting instanceof BooleanSetting) {
                            ((BooleanSetting) setting).setEnabled(Boolean.parseBoolean(value));
                        }

                        if (setting instanceof NumberSetting) {
                            ((NumberSetting) setting).setValue(Double.parseDouble(value));
                        }

                        if (setting instanceof ModeSetting && ((ModeSetting) setting).modes.toString().contains(value)) {
                            ((ModeSetting) setting).setMode(value);
                        }

                        if (setting instanceof ColorSetting) {
                            ((ColorSetting) setting).setRainbow(Boolean.parseBoolean(curLine.split(":")[3]));
                            ((ColorSetting) setting).fromInteger(Integer.parseInt(value));
                        }

                        if (setting instanceof KeybindSetting) {
                            ((KeybindSetting) setting).setKeyCode(Integer.parseInt(value));
                        }
                    } else
                        module.setKey(Integer.parseInt(value));
                }
            }

            br.close();
        } catch (Exception ignored) {
        }
    }

    public void loadPrefix() {
        try {
            File file = new File(MainDirectory, "prefix.txt");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                YawnClient.INSTANCE.commandManager.setCommandPrefix(line);
            }

            br.close();
        } catch (Exception ignored) {
        }
    }
}