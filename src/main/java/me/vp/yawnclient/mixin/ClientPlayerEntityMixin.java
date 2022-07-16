package me.vp.yawnclient.mixin;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.event.events.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (mc.player != null && mc.world != null) {
            YawnClient.INSTANCE.moduleManager.onTick();
            TickEvent event = new TickEvent();
            YawnClient.EVENT_BUS.post(event);
        }
    }
}