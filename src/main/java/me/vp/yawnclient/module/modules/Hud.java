package me.vp.yawnclient.module.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.event.events.RenderIngameHudEvent;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.settings.BooleanSetting;
import me.vp.yawnclient.module.setting.settings.ModeSetting;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.quantumclient.energy.Subscribe;

import java.awt.*;
import java.text.DecimalFormat;

public class Hud extends Module {
    public final BooleanSetting watermark = new BooleanSetting("Watermark", this, true);
    public final BooleanSetting logo = new BooleanSetting("Logo", this, true);
    public final BooleanSetting arraylist = new BooleanSetting("ArrayList", this, true);
    public final BooleanSetting fps = new BooleanSetting("Fps", this, false);
    public final BooleanSetting ping = new BooleanSetting("Ping", this, false);
    public final BooleanSetting speed = new BooleanSetting("Speed", this, false);
    public final BooleanSetting coords = new BooleanSetting("Coords", this, true);
    public final BooleanSetting netherCoords = new BooleanSetting("NetherCoords", this, true);
    public final BooleanSetting facing = new BooleanSetting("Facing", this, false);
    public final BooleanSetting paperDoll = new BooleanSetting("Paperdoll", this, false);

    private final Identifier logo1 = new Identifier("yawnclient", "yawn.png");

    public Hud() {
        super("Hud", "Renders stuff on screen.", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        this.addSettings(watermark, logo, arraylist, fps, ping, speed, coords, netherCoords, facing, paperDoll);
    }

    @Subscribe
    public void onRender(RenderIngameHudEvent event) {
        if (mc.options.debugEnabled) return;

        // Watermark
        if (watermark.isEnabled()) {
            DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, YawnClient.name + " " + YawnClient.version, 1, 1, Color.MAGENTA.getRGB());
        }

        // Logo
        if (logo.isEnabled()) {
            RenderSystem.setShaderTexture(0, logo1);
            RenderSystem.enableBlend();
            DrawableHelper.drawTexture(event.getMatrix(), 5, watermark.enabled ? 10 : 5, 0, 0, 50, 50, 50, 50);
        }

        // Fps
        if (fps.isEnabled()) {
            DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, "FPS [" + mc.fpsDebugString.split(" ", 2)[0] + "]", 1, 60, Color.LIGHT_GRAY.getRGB());
        }

        // Ping
        PlayerListEntry playerEntry = mc.player.networkHandler.getPlayerListEntry(mc.player.getGameProfile().getId());
        int latency = playerEntry == null ? 0 : playerEntry.getLatency();

        if (ping.isEnabled()) {
            DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, "Ping [" + latency + "ms]", 1, 70, Color.LIGHT_GRAY.getRGB());
        }

        // Speed
        if (speed.isEnabled()) {
            final DecimalFormat decimalFormat = new DecimalFormat("#.#");
            Vec3d vec = new Vec3d(mc.player.getX() - mc.player.prevX, 0, mc.player.getZ() - mc.player.prevZ).multiply(20);
            final double speed = Math.abs(vec.length());
            final String speedString = "Speed [" + decimalFormat.format((speed)) + "km/s]";

            DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, speedString, 1, 80, Color.LIGHT_GRAY.getRGB());
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

            DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, overWorld, 1, netherCoords.enabled ? mc.getWindow().getScaledHeight() - 20
                    : mc.getWindow().getScaledHeight() - 10, Color.LIGHT_GRAY.getRGB());

            if (netherCoords.isEnabled())
                DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, nether, 1, mc.getWindow().getScaledHeight() - 10, Color.RED.getRGB());
        }

        // Facing
        if (facing.isEnabled()) {
            String facing = mc.player.getHorizontalFacing().name().substring(0, 1).toUpperCase()
                    + mc.player.getHorizontalFacing().name().substring(1).toLowerCase();

            DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, facing, 1, coords.enabled ? mc.getWindow().getScaledHeight() - 30
                    : mc.getWindow().getScaledHeight() - 10, Color.LIGHT_GRAY.getRGB());
        }

        // ArrayList
        int iteration = 0;
        if (arraylist.isEnabled()) {
            for (int i = 0; i < YawnClient.INSTANCE.moduleManager.modules.size(); i++) {
                Module mod = YawnClient.INSTANCE.moduleManager.modules.get(i);
                if (mod.isEnabled()) {
                    DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, mod.getName(),
                            mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(mod.getName()), 10 + (iteration * 10),
                            Color.MAGENTA.getRGB());
                    iteration++;
                }
            }
        }

        // Paperdoll
        if (paperDoll.isEnabled()) {
            if (!(mc.player == null)) {
                float yaw = MathHelper.wrapDegrees(mc.player.prevYaw + (mc.player.getYaw() - mc.player.prevYaw) * mc.getTickDelta());
                float pitch = mc.player.getPitch();
                event.getMatrix().push();
                InventoryScreen.drawEntity(arraylist.enabled ? mc.getWindow().getScaledWidth() - 80 : mc.getWindow().getScaledWidth() - 20, 50, 25, -yaw, -pitch, mc.player);
                RenderSystem.enableDepthTest();
                event.getMatrix().pop();
            }
        }
    }
}