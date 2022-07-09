package me.vp.yawnclient.module.modules;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class Test extends Module {
    public Test() {
        super("Test","This is a test.", GLFW.GLFW_KEY_K, Category.CLIENT);
    }

    @Override
    public void onEnable() {
        YawnClient.mc.player.sendMessage(Text.literal("Test enabled."));
    }
}
