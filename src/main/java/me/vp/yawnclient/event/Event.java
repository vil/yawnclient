package me.vp.yawnclient.event;

import net.minecraft.client.MinecraftClient;

public class Event extends org.quantumclient.energy.Event {
    public MinecraftClient mc = MinecraftClient.getInstance();
    public Event() {}
}
