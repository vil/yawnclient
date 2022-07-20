package me.vp.yawnclient.module.modules;

import com.mojang.blaze3d.systems.RenderSystem;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.event.events.RenderEntityEvent;
import me.vp.yawnclient.event.events.RenderIngameHudEvent;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.setting.settings.BooleanSetting;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
    public final BooleanSetting targetHud = new BooleanSetting("TargetHud", this, false);

    private final Identifier logo1 = new Identifier("yawnclient", "yawn.png");
    private PlayerEntity target;
    private boolean found;
    float temp = 10000;

    public Hud() {
        super("Hud", "Renders stuff on screen.", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        this.addSettings(watermark, logo, arraylist, fps, ping, speed, coords, netherCoords, facing, paperDoll, targetHud);
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
            final String speedString = "Speed [" + decimalFormat.format((speed)) + "km/h]";

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
                            mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(mod.getName()), 1 + (iteration * 10),
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

        // TargetHud
        // This looks so goofy ahh :skull:, someone please rewrite this for me :3 <3 pleawse
        if (targetHud.isEnabled()) {
            if (target == null) return;

            int x = mc.getWindow().getScaledWidth() - 280;
            int y = mc.getWindow().getScaledHeight() - 65;
            PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(target.getUuid());
            int targetLatency = playerListEntry == null ? 0 : playerListEntry.getLatency();
            String info = target.getEntityName() + " || " + targetLatency + "ms";
            String health = String.format("%.1f", target.getHealth() + target.getAbsorptionAmount()) + " health";
            String location = String.format("%.1f", mc.player.distanceTo(target)) + "m";

            if (target != null) {
                DrawableHelper.fill(event.getMatrix(), x, y, x + 150, y + 70, new Color(0, 0, 0, 100).getRGB());
                DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, info, x + 10, y + 9, Color.WHITE.getRGB());
                DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, health, x + 10, y + 20, Color.WHITE.getRGB());
                DrawableHelper.drawStringWithShadow(event.getMatrix(), mc.textRenderer, location, x + 90, y + 20, Color.WHITE.getRGB());

                int i = 1;
                for (ItemStack item : target.getArmorItems()) {
                    mc.getItemRenderer().renderGuiItemIcon(item, x + (9 * i) + (i * 9) - 9, y + 30);
                    i++;
                }

                mc.getItemRenderer().renderGuiItemIcon(target.getMainHandStack(), x + 80, y + 30);
                mc.getItemRenderer().renderGuiItemIcon(target.getOffHandStack(), x + 100, y + 30);
                InventoryScreen.drawEntity(x + 130, y + 62, 25, -MathHelper.wrapDegrees(target.prevYaw + (target.getYaw() - target.prevYaw) * mc.getTickDelta()), -target.getPitch(), target);

                DrawableHelper.fill(event.getMatrix(), x + 5, y + 50, x + getWidth(target.getAbsorptionAmount() + target.getHealth()) + 10, y + 60,
                                    getColor(36, 100 / 36f * target.getHealth() + target.getAbsorptionAmount()).getRGB());
            }
        }
    }

    @Subscribe
    public void onRender(RenderEntityEvent event) {
        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (mc.player.distanceTo(player) < 150 && mc.player.distanceTo(player) < temp && player != mc.player) {
                target = player;
                found = true;
                temp = mc.player.distanceTo(player);
            }
        }
        if (!found) target = null;
        else found = false;
        temp = 10000;
    }

    private Color getColor(float max, float value) {
        double percent = 100 / (max / value);
        if (percent <= 30) return Color.RED;
        if (30 < percent && percent <= 70) return Color.YELLOW;
        if (percent > 70) return Color.GREEN;
        return null;
    }

    private int getWidth(float value) {
        double percent = 100 / (target.getMaxHealth() / value);
        return (int) (170 / 100 * percent);
    }
}