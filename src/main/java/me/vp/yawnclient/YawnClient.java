package me.vp.yawnclient;

import me.vp.yawnclient.command.Command;
import me.vp.yawnclient.command.CommandManager;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.ModuleManager;
import me.vp.yawnclient.setting.ConfigManager;
import me.vp.yawnclient.setting.SettingManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import net.minecraft.client.MinecraftClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.quantumclient.energy.EventBus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class YawnClient implements ModInitializer {
    public static YawnClient INSTANCE;
    public static String name = "YawnClient";
    public static final String version = "0.2.1b";
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    public YawnClient() {
        INSTANCE = this;
    }

    public final EventBus EVENT_BUS = new EventBus();
    public ModuleManager moduleManager;
    public SettingManager settingManager;
    public CommandManager commandManager;
    public ConfigManager configManager;

    public static final Logger LOGGER = LogManager.getLogger("yawnclient");

    public static final Object syncronize = new Object();

    public static void printInfo(String text) {
        synchronized (syncronize) {
            LOGGER.info(text);
        }
    }

    public static void printError(String text) {
        synchronized (syncronize) {
            LOGGER.error(text);
        }
    }

    public void addModule(Module module) {
        moduleManager.modules.add(module);
    }

    public void addCommand(Command command) {
        commandManager.commands.add(command);
    }


    @Override
    public void onInitialize() {
        long startTime = System.currentTimeMillis();
        printInfo("Yawnclient \uD83E\uDD71 " + version + " by Vp");

        settingManager = new SettingManager();
        printInfo("setting system initialized.");

        moduleManager = new ModuleManager();
        printInfo("module system initialized.");

        commandManager = new CommandManager();
        printInfo("command system initialized.");

        configManager = new ConfigManager();
        printInfo("config manager initialized.");


        long finishTime = System.currentTimeMillis() - startTime;
        printInfo("Yawnclient \uD83E\uDD71 phase 1 initialized in " + finishTime + "ms.");
    }

    public void postInit() {
        long startTime = System.currentTimeMillis();
        printInfo("Yawnclient \uD83E\uDD71 phase 2");

        configManager.load();
        printInfo("configs loaded.");

        long finishTime = System.currentTimeMillis() - startTime;
        printInfo("Yawnclient \uD83E\uDD71 phase 2 initialized in " + finishTime + "ms.");
    }

    private static String getBuildDay() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
