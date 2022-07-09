package me.vp.yawnclient.event.events;

import me.vp.yawnclient.event.Event;

// posted in MixinKeyboard
public class KeyPressEvent extends Event {
    private int key;
    private int scanCode;

    public KeyPressEvent(int key, int scanCode) {
        this.key = key;
        this.scanCode = scanCode;
    }

    public int getKey() {
        return key;
    }

    public int getScanCode() {
        return scanCode;
    }
}