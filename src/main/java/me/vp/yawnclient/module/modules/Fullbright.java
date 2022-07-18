package me.vp.yawnclient.module.modules;

import me.vp.yawnclient.event.events.TickEvent;
import me.vp.yawnclient.module.Module;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.lwjgl.glfw.GLFW;
import org.quantumclient.energy.Subscribe;

public class Fullbright extends Module {
    public Fullbright() {
        super("Fullbright", "Puts your brightness to max.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
    }

    @Override
    public void onDisable() {
        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);

        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Subscribe
    public void onTick(TickEvent event) {
        mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 500, 0));
    }

}