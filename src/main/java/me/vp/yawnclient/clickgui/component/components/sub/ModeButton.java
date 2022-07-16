package me.vp.yawnclient.clickgui.component.components.sub;

import me.vp.yawnclient.clickgui.component.Component;
import me.vp.yawnclient.clickgui.component.components.Button;

import me.vp.yawnclient.module.setting.settings.ModeSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModeButton extends Component {
    private boolean hovered;
    private Button parent;
    private final ModeSetting setting;
    private int offset;
    private int x;
    private int y;
    private int index;

    public ModeButton(ModeSetting setting, Button button, int offset) {
        this.setting = new ModeSetting(setting.name, setting.parent, setting.modes.get(this.index));
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
        this.index = 0;
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
        DrawableHelper.drawStringWithShadow(matrixStack, textRenderer, setting.name + ": " + setting.modes.get(index), (parent.parent.getX() + 6), (parent.parent.getY() + offset) + 3, new Color(255, 255, 255, 255).getRGB());
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            /*
            if (index + 1 >= setting.modes.size())
				index = 0;
			else
				index++;
            */
            setting.cycle();
            setting.modes.set(index, setting.getMode());
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}