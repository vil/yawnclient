package me.vp.yawnclient.clickgui.component.components.sub;

import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.clickgui.component.Component;
import me.vp.yawnclient.clickgui.component.components.Button;
import me.vp.yawnclient.module.setting.Setting;

import me.vp.yawnclient.module.setting.settings.ModeSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModeButton extends Component {

	private boolean hovered;
	private Button parent;
	private ModeSetting set;
	private int offset;
	private int x;
	private int y;
	private Module mod;

	private int modeIndex;

	public ModeButton(ModeSetting value, Button button, Module mod, int offset) {
		this.set = value;
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
		this.modeIndex = 0;
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}

	@Override
	public void renderComponent(MatrixStack matrixStack, TextRenderer textRenderer) {
		DrawableHelper.fill(matrixStack, parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered
        ? new Color(20, 20, 20, 191).getRGB() : new Color(0, 0, 0, 191).getRGB());

		DrawableHelper.fill(matrixStack, parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, new Color(0, 0, 0, 191).getRGB());
		DrawableHelper.drawStringWithShadow(matrixStack, textRenderer, set.name + ": " + set.parent.getName(), (parent.parent.getX() + 6), (parent.parent.getY() + offset) + 3, new Color(255, 255, 255, 255).getRGB());
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			int maxIndex = set.parent.settings.size();

			if(modeIndex + 1 >= maxIndex)
				modeIndex = 0;
			else
				modeIndex++;

			set.modes.add(set.getMode());
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}