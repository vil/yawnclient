package me.vp.yawnclient.clickgui.component;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.clickgui.component.components.Button;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;

public class Frame {

	public ArrayList<Component> components;
	public Module.Category category;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;

	public Frame(Module.Category cat) {
		this.components = new ArrayList<>();
		this.category = cat;
		this.width = 88;
		this.x = 5;
		this.y = 5;
		this.barHeight = 13;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		int tY = this.barHeight;

		for(Module mod : YawnClient.INSTANCE.moduleManager.getModules()) {
			if (!mod.getCategory().equals(cat)) continue;
			Button modButton = new Button(mod, this, tY, open);
			this.components.add(modButton);
			tY += 12;
		}
	}

	public ArrayList<Component> getComponents() {
		return components;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void renderFrame(MatrixStack matrixStack, TextRenderer textRenderer) {
		DrawableHelper.fill(matrixStack, this.x, this.y , this.x + this.width, this.y + this.barHeight, new Color(0, 0, 0, 191).getRGB());
		DrawableHelper.drawCenteredText(matrixStack, textRenderer, this.category.name(), (this.x + 40) + 3, (int) ((this.y + 0.0f) * 1 + 4), new Color(255, 255, 255, 255).getRGB());
		if(this.open) {
			if(!this.components.isEmpty()) {
				for(Component component : components) {
					component.renderComponent(matrixStack, textRenderer);
				}
			}
		}
	}

	public void refresh() {
		int off = this.barHeight;
		for(Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public void updatePosition(int mouseX, int mouseY) {
		if(this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}

	public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}

}