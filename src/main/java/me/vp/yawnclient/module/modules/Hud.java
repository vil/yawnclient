package me.vp.yawnclient.module.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.event.events.RenderIngameHudEvent;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.settings.BooleanSetting;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.quantumclient.energy.Subscribe;

import java.awt.*;
import java.text.DecimalFormat;

public class Hud extends Module {
    public final BooleanSetting watermark = new BooleanSetting("Watermark", this, true);
    public final BooleanSetting logo = new BooleanSetting("Logo", this, true);
    public final BooleanSetting coords = new BooleanSetting("Coords", this, true);
    public final BooleanSetting netherCoords = new BooleanSetting("NetherCoords", this, true);


    private final Identifier logo1 = new Identifier("yawnclient", "yawn.png");
    public Hud() {
        super("Hud", "Renders stuff on screen.", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        this.addSettings(watermark, logo, coords, netherCoords);
    }

    @Subscribe
    public void onRender(RenderIngameHudEvent event) {
        if (mc.options.debugEnabled) return;

        // Watermark
        if (watermark.isEnabled()) {
            DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, Formatting.DARK_PURPLE + YawnClient.name + " " + Formatting.RESET + YawnClient.version, 1, 1, 0xFFFFFF);
        }

        // Logo
        if (logo.isEnabled()) {
            RenderSystem.setShaderTexture(0, logo1);
            RenderSystem.enableBlend();
            DrawableHelper.drawTexture(event.getMatrix(), 5, 20, 0, 0, 50, 50, 50, 50);
        }

        // Coords
        if (coords.isEnabled()) {
            final DecimalFormat decimalFormat = new DecimalFormat("###.#");
            double cx = mc.player.getX();
            double cy = mc.player.getY();
            double cz = mc.player.getZ();

            if (mc.world.getDimension().respawnAnchorWorks()) {
                cx *= 8;
                cz *= 8;
            }

            final String overWorld = "XYZ [" + decimalFormat.format(cx) + ", " + decimalFormat.format(cy) + ", " + decimalFormat.format(cz) + "]";
            final String nether = "XYZ [" + decimalFormat.format(cx / 8) + ", " + decimalFormat.format(cy) + ", " + decimalFormat.format(cz / 8) + "]";

            DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, overWorld, 1, mc.getWindow().getScaledHeight() - 20, Color.LIGHT_GRAY.getRGB());
            if (netherCoords.isEnabled())
                DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, nether, 1, mc.getWindow().getScaledHeight() - 10, Color.RED.getRGB());
        }
    }
}