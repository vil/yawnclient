package me.vp.yawnclient.command.commands;

import me.vp.yawnclient.command.Command;
import me.vp.yawnclient.command.CommandManager;

import java.util.stream.Collectors;

public class HelpCmd extends Command {

    public HelpCmd() {
        super("Help", "Tells you all the commands.", "help", "h");
    }

    @Override
    public void onCommand(String[] args, String command) {
        CommandManager.addChatMessage("Commands (" + CommandManager.commands.size() + "): " + CommandManager.commands.stream().map(Command::getName).collect(Collectors.joining(", ")));
	}
}
