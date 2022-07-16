package me.vp.yawnclient.module.modules;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.settings.*;
import me.vp.yawnclient.util.JColor;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class Test extends Module {
    public final BooleanSetting bool = new BooleanSetting("boolean", this, true);
    public final ModeSetting mode = new ModeSetting("mode", this, "mode", "mode", "mode2");
    public final NumberSetting number = new NumberSetting("number", this, 10, 0, 15, 1);
    public final ColorSetting color = new ColorSetting("color", this, new JColor(0, 0, 0));

    public Test() {
        super("Test", "This is a test.", GLFW.GLFW_KEY_K, Category.CLIENT);
        this.addSettings(bool, mode, number);
    }

    @Override
    public void onEnable() {
        YawnClient.mc.player.sendMessage(Text.literal("Test enabled."));
    }
}
