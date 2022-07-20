package me.vp.yawnclient.clickgui.component.components.sub;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.clickgui.component.Component;
import me.vp.yawnclient.clickgui.component.components.Button;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Keybind extends Component {

    private boolean hovered;
    private boolean binding;
    private Button parent;
    private int offset;
    private int x;
    private int y;

    public Keybind(Button button, int offset) {
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void renderComponent(MatrixStack matrixStack, TextRenderer textRenderer) {
        DrawableHelper.fill(matrixStack, parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered
                ? new Color(20, 20, 20, 191).getRGB() : new Color(0, 0, 0, 191).getRGB());
        DrawableHelper.fill(matrixStack, parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, new Color(0, 0, 0, 191).getRGB());

		String name = parent.mod.getKey() < 0 ? "NONE" : InputUtil.fromKeyCode(parent.mod.getKey(), -1).getLocalizedText().getString();
		if (name == null) name = "KEY" + parent.mod.getKey();
		else if (name.isEmpty()) name = "NONE";

        DrawableHelper.drawStringWithShadow(matrixStack, textRenderer, binding ? "Press a key..." :
                        ("Key: " + name), (parent.parent.getX() + 6),
                (parent.parent.getY() + offset) + 3, new Color(255, 255, 255, 255).getRGB());
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
        super.updateComponent(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.binding = !this.binding;
            YawnClient.INSTANCE.configManager.save();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {
        if (this.binding) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) this.parent.mod.setKey(-1);

            else this.parent.mod.setKey(keyCode);
            this.binding = false;
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}