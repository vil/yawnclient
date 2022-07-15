package me.vp.yawnclient;

import me.vp.yawnclient.clickgui.Clickgui;
import me.vp.yawnclient.saveload.Load;
import me.vp.yawnclient.saveload.Save;
import me.vp.yawnclient.command.Command;
import me.vp.yawnclient.command.CommandManager;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.ModuleManager;
import me.vp.yawnclient.module.setting.SettingManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantumclient.energy.EventBus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class YawnClient implements ModInitializer {
	public static YawnClient INSTANCE;
	public static String name = "YawnClient";
	public static final String version = "0.1b " + getBuildDay();
	public static final MinecraftClient mc = MinecraftClient.getInstance();

	public YawnClient() {
		INSTANCE = this;
	}

	public static final EventBus EVENT_BUS = new EventBus();
	public ModuleManager moduleManager;
	public SettingManager settingManager;
	public CommandManager commandManager;
    public Clickgui clickgui;
	public Save save;
	public Load load;

	public static final Logger LOGGER = LogManager.getLogger("yawnclient");

	public static final Object syncronize = new Object();
	public static void printLog(String text) {
		synchronized (syncronize) {
			LOGGER.info(text);
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
		printLog("Yawnclient \uD83E\uDD71 " + version + " by Vp");

		printLog("variables initialized.");

		commandManager = new CommandManager();
		printLog("command system initialized.");

		moduleManager = new ModuleManager();
		printLog("module system initialized.");

		settingManager = new SettingManager();
		printLog("setting system initialized.");

        clickgui = new Clickgui();
        printLog("clickgui intialized.");

		save = new Save();
		load = new Load();
		printLog("saves and loads initialized.");
		long finishTime = System.currentTimeMillis() - startTime;
		printLog("Yawnclient \uD83E\uDD71 initialized in " + finishTime + "ms.");
	}

    private static String getBuildDay() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
