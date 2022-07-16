package me.vp.yawnclient.event.events;

import me.vp.yawnclient.event.Event;
import net.minecraft.client.gui.screen.Screen;

public class MouseEvent extends Event {
    private final Screen screen;
    private final double mouseX;
    private final double mouseY;
    private final int button;
    private final boolean state;

    public MouseEvent(Screen screen, double mouseX, double mouseY, int button, boolean state) {
        super();
        this.screen = screen;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
        this.state = state;
    }

    public double getMouseX() {
        return this.mouseX;
    }

    public double getMouseY() {
        return this.mouseY;
    }

    public int getButton() {
        return this.button;
    }

    public boolean getState() {
        return this.state;
    }

    public static class afterMouseClick extends MouseEvent {
        public afterMouseClick(Screen screen, double mouseX, double mouseY, int button, boolean state) {
            super(screen, mouseX, mouseY, button, state);
        }
    }

    public static class afterMouseRelease extends MouseEvent {
        public afterMouseRelease(Screen screen, double mouseX, double mouseY, int button, boolean state) {
            super(screen, mouseX, mouseY, button, state);
        }
    }


}