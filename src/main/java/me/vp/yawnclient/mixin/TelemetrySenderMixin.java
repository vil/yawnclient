package me.vp.yawnclient.mixin;

import me.vp.yawnclient.YawnClient;
import net.minecraft.client.util.telemetry.TelemetrySender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TelemetrySender.class)
public class TelemetrySenderMixin {
    @Shadow
	private boolean sent;

	@Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/client/util/telemetry/TelemetrySender$PlayerGameMode;)V", cancellable = true)
	private void send(TelemetrySender.PlayerGameMode playerGameMode, CallbackInfo ci) {
		sent = true;

		ci.cancel();

		YawnClient.LOGGER.info("Blocked mojangs bullshit.");
	}
}