package me.vp.yawnclient.clickgui.component.components;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.clickgui.component.Component;
import me.vp.yawnclient.clickgui.component.Frame;
import me.vp.yawnclient.clickgui.component.components.sub.Checkbox;
import me.vp.yawnclient.clickgui.component.components.sub.Keybind;
import me.vp.yawnclient.clickgui.component.components.sub.ModeButton;
import me.vp.yawnclient.clickgui.component.components.sub.Slider;
import me.vp.yawnclient.module.Module;
import me.vp.yawnclient.module.setting.Setting;
import me.vp.yawnclient.module.setting.settings.BooleanSetting;
import me.vp.yawnclient.module.setting.settings.ModeSetting;
import me.vp.yawnclient.module.setting.settings.NumberSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

public class Button extends Component {

    public Module mod;
    public Frame parent;
    public int offset;
    private boolean isHovered;
    public ArrayList<Component> subcomponents = new ArrayList<>();
    public boolean open;
    private int height;
    private int opY;

    public Button(Module mod, Frame parent, int offset, boolean open) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.open = open;
        height = 12;
        opY = offset + 12;

        // I am losing braincells
        mod.settings.forEach(setting -> {
            if (mod.settings != null) {
                if (setting instanceof BooleanSetting)
                    subcomponents.add(new Checkbox((BooleanSetting) setting, this, opY));
                else if (setting instanceof ModeSetting)
                    subcomponents.add(new ModeButton((ModeSetting) setting, this, opY));
                else if (setting instanceof NumberSetting)
                    subcomponents.add(new Slider((NumberSetting) setting, this, opY));
            }
        });
        this.subcomponents.add(new Keybind(this, opY));
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
        int opY = offset + 12;
        for (Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }

    @Override
    public void renderComponent(MatrixStack matrixStack, TextRenderer textRenderer) {
        DrawableHelper.fill(matrixStack, parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered
                ? (this.mod.isEnabled() ? new Color(255, 0, 255, 191).darker().getRGB()
                : new Color(15, 15, 15, 191).getRGB()) : (this.mod.isEnabled() ? new Color(255, 0, 255, 191).getRGB()
                : new Color(30, 30, 30, 191).getRGB()));

        DrawableHelper.drawTextWithShadow(matrixStack, textRenderer, Text.of(this.mod.getName()), (parent.getX() + 2) + 2, (parent.getY() + offset + 2) + 1, new Color(255, 255, 255).getRGB());
        if (this.subcomponents.size() > 2)
            DrawableHelper.drawStringWithShadow(matrixStack, textRenderer, this.open ? "-" : "+", (parent.getX() + parent.getWidth() - 10), (parent.getY() + offset) + 4, new Color(255, 255, 255, 255).getRGB());
        if (this.open) {
            if (!this.subcomponents.isEmpty()) {
                for (Component comp : this.subcomponents) {
                    comp.renderComponent(matrixStack, textRenderer);
                }
                DrawableHelper.fill(matrixStack, parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), new Color(255, 0, 255, 191).getRGB());
            }
        }
    }


    @Override
    public int getHeight() {
        if (this.open) {
            return (12 * (this.subcomponents.size() + 1));
        }
        return 12;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.isHovered = isMouseOnButton(mouseX, mouseY);

        if (!this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        for (Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {
        for (Component comp : this.subcomponents) {
            comp.keyTyped(keyCode, scanCode, modifiers);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }

}