package me.vp.yawnclient.mixin;

import me.vp.yawnclient.YawnClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V", shift = At.Shift.BEFORE))
    private void init(RunArgs args, CallbackInfo ci) {
        YawnClient.INSTANCE.yawnInit();
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"close()V"})
    private void onClose(CallbackInfo ci) {
        try {
            YawnClient.INSTANCE.save.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}