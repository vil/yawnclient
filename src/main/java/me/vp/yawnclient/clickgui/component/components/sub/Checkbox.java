package me.vp.yawnclient.clickgui.component.components.sub;

import me.vp.yawnclient.clickgui.component.Component;
import me.vp.yawnclient.clickgui.component.components.Button;
import me.vp.yawnclient.module.setting.settings.BooleanSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class Checkbox extends Component {

	private boolean hovered;
	private final BooleanSetting setting;
	private Button parent;
	private int offset;
	private int x;
	private int y;

	public Checkbox(BooleanSetting setting, Button button, int offset) {
		this.setting = new BooleanSetting(setting.name, setting.parent, setting.isEnabled());
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent(MatrixStack matrixStack, TextRenderer textRenderer) {
		DrawableHelper.fill(matrixStack, parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ?
        new Color(20, 20, 20, 191).getRGB() : new Color(0, 0, 0, 191).getRGB());

		DrawableHelper.fill(matrixStack, parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, new Color(0, 0, 0, 191).getRGB());
		DrawableHelper.fill(matrixStack, parent.parent.getX() + 1 + 5, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 3, parent.parent.getY() + offset + 9,  new Color(89, 89, 89, 191).getRGB());
		DrawableHelper.drawStringWithShadow(matrixStack, textRenderer, this.setting.name, (parent.parent.getX()) + 17, (parent.parent.getY() + offset + 2) + 1, new Color(255, 255, 255, 255).getRGB());
		if (this.setting.isEnabled()) {
			DrawableHelper.fill(matrixStack, parent.parent.getX() + 1 + 5, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 3, parent.parent.getY() + offset + 9,  new Color(234, 0, 255, 191).getRGB());
		}
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
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
            this.setting.setEnabled(!this.setting.isEnabled());
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}