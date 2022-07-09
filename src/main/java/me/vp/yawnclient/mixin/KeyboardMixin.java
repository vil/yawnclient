package me.vp.yawnclient.mixin;

import me.vp.yawnclient.YawnClient;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "net/minecraft/client/util/InputUtil.isKeyPressed(JI)Z", ordinal = 5))
    private void onKeyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        YawnClient.INSTANCE.moduleManager.getModules().forEach(module -> {
            if (module.getKey() == key) {
                module.toggle();
            }
        });
    }

}
