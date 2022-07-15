package me.vp.yawnclient.clickgui;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module.Category;
import me.vp.yawnclient.clickgui.component.Frame;
import me.vp.yawnclient.clickgui.component.Component;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.quantumclient.energy.Subscribe;

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
		for(Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 1;
		}

		YawnClient.EVENT_BUS.register(this);
	}

	@Override
	public void init() {

	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		for(Frame frame : frames) {
			frame.renderFrame(matrixStack, textRenderer);
			for(Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}

		matrixStack.push();
		matrixStack.translate(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), 0);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
		mc.textRenderer.drawWithShadow(matrixStack, Formatting.DARK_PURPLE + YawnClient.name + " " + YawnClient.version,
                                       mc.textRenderer.getWidth(YawnClient.name + " " + YawnClient.version) - 317, -mc.textRenderer.fontHeight, Color.WHITE.getRGB());
		matrixStack.pop();

	}

	public boolean mousePressed(final int mouseX, final int mouseY, final int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		for (Frame frame : frames) {
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if (frame.isOpen()) {
				if (!frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
        return super.mouseClicked(mouseX, mouseY, mouseButton);
	}


	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for(Frame frame : frames) {
			if(frame.isOpen() && keyCode != 1) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.keyTyped(keyCode, scanCode, modifiers);
					}
				}
			}
		}
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.mc.setScreen(null);
        }
        return true;
	}

    public boolean mouseReleased(int mouseX, int mouseY, int state) {
		for(Frame frame : frames) {
			frame.setDrag(false);
		}
		for(Frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
        return super.mouseReleased(mouseX, mouseY, state);
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