package me.vp.yawnclient.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;

import java.util.UUID;

public class FakePlayerUtil extends AbstractClientPlayerEntity {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final OtherClientPlayerEntity player = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString("0155842b-0328-4e6a-94ee-4b3b9cd85c01"), "NotVp"), mc.player.getPublicKey());
    public FakePlayerUtil() {
        super(mc.world, mc.player.getGameProfile(), mc.player.getPublicKey());
        if (mc.world == null && mc.player == null) return;
        player.setPos(mc.player.getX(), mc.player.getY(), mc.player.getZ());
    }

    public static void spawn() {
        player.copyFrom(mc.player);
        player.setVelocity(0, 0, 0);
        player.setYaw(mc.player.getYaw());
        player.setPitch(mc.player.getPitch());
        mc.world.addEntity(-1, player);
    }

    public static void despawn() {
        mc.world.removeEntity(player.getId(), RemovalReason.DISCARDED);
    }
}