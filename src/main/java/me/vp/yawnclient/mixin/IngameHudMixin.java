package me.vp.yawnclient.mixin;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.event.events.RenderIngameHudEvent;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IngameHudMixin.class)
public class IngameHudMixin {
    @Inject(method = "render", at = @At("RETURN"), cancellable = true)
	private void render(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
		RenderIngameHudEvent event = new RenderIngameHudEvent(matrixStack);
		YawnClient.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}