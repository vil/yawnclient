package me.vp.yawnclient.module.modules;

import me.vp.yawnclient.event.events.TickEvent;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.settings.ModeSetting;
import org.lwjgl.glfw.GLFW;
import org.quantumclient.energy.Subscribe;

public class Sprint extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", this, "Legit", "Rage");
    public Sprint() {
        super("Sprint", "Makes the player automatically sprint.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
        this.addSettings(mode);
    }

    @Override
    public void onDisable() {
        mc.player.setSprinting(false);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mode.is("Legit")) mc.options.sprintKey.setPressed(true);
        else if (mode.is("Rage")) mc.player.setSprinting(mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0);
    }
}