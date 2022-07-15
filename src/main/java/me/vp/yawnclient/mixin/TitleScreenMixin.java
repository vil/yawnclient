package me.vp.yawnclient.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.vp.yawnclient.YawnClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    private static final Identifier logo = new Identifier("yawnclient", "yawn.png");

    @Inject(method = "render", at = @At("RETURN"), cancellable = true)
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        DrawableHelper.drawStringWithShadow(matrices, YawnClient.mc.textRenderer, Formatting.DARK_PURPLE + "YawnClient " + Formatting.RESET +  YawnClient.version, 1, 1, 0xFFFFFF);
        RenderSystem.setShaderTexture(0, logo);
        RenderSystem.enableBlend();
        DrawableHelper.drawTexture(matrices, 5, 20, 0, 0,50, 50, 50, 50);
        ci.cancel();
    }
}
