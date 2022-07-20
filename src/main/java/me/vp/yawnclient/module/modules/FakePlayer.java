package me.vp.yawnclient.module.modules;

import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.util.FakePlayerUtil;
import org.lwjgl.glfw.GLFW;

public class FakePlayer extends Module {

    public FakePlayer() {
        super("FakePlayer", "Spawns a fake player.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.world == null && mc.player == null) return;
        FakePlayerUtil.spawn();
    }

    @Override
    public void onDisable() {
        FakePlayerUtil.despawn();
    }
}