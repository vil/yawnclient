package me.vp.yawnclient.clickgui;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module.Category;
import me.vp.yawnclient.clickgui.component.Frame;
import me.vp.yawnclient.clickgui.component.Component;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class Clickgui extends Screen {
    MinecraftClient mc = MinecraftClient.getInstance();
    public static ArrayList<Frame> frames;
    public static int color = -1;

    public Clickgui() {
        super(Text.literal("Fuck you mojang"));
        frames = new ArrayList<>();
        int frameX = 5;
        for (Category category : Category.values()) {
            Frame frame = new Frame(category);
            frame.setX(frameX);
            frames.add(frame);
            frameX += frame.getWidth() + 1;
        }

    }

    @Override
    public void init() {
        YawnClient.EVENT_BUS.register(this);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        for (Frame frame : frames) {
            frame.renderFrame(matrixStack, textRenderer);
            for (Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }

        matrixStack.push();
        matrixStack.translate(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), 0);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        DrawableHelper.drawStringWithShadow(matrixStack, textRenderer, Formatting.DARK_PURPLE + YawnClient.name + " " + YawnClient.version,
                mc.textRenderer.getWidth(YawnClient.name + " " + YawnClient.version), -mc.textRenderer.fontHeight, Color.WHITE.getRGB());
        matrixStack.pop();

    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Frame frame : frames) {
            if (frame.isWithinHeader((int) mouseX, (int) mouseY) && button == 0) {
                frame.setDrag(true);
                frame.dragX = (int) mouseX - frame.getX();
                frame.dragY = (int) mouseY - frame.getY();
            }
            if (frame.isWithinHeader((int) mouseX, (int) mouseY) && button == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (frame.isOpen()) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.mouseClicked((int) mouseX, (int) mouseY, button);
                        if (YawnClient.INSTANCE.save != null) {
                            try {
                                YawnClient.INSTANCE.save.saveSettings();
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
        }
        //YawnClient.printLog("mouse clicked");
        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Frame frame : frames) {
            if (frame.isOpen() && keyCode != 1) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.keyTyped(keyCode, scanCode, modifiers);
                    }
                }
            }
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) this.mc.setScreen(null);
        else if (keyCode == GLFW.GLFW_KEY_ESCAPE) this.mc.setScreen(null);

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Frame frame : frames) {
            frame.setDrag(false);
        }
        for (Frame frame : frames) {
            if (frame.isOpen()) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.mouseReleased((int) mouseX, (int) mouseY, button);
                        if (YawnClient.INSTANCE.save != null) {
                            try {YawnClient.INSTANCE.save.saveModules();}
                            catch (Exception ignored) {}
                        }
                    }
                }
            }
        }
        //YawnClient.printLog("mouse releasesd");
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

}