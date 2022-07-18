package me.vp.yawnclient.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.vp.yawnclient.YawnClient;
import me.vp.yawnclient.command.commands.HelpCmd;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CommandManager {

    public static List<Command> commands;
    public static String prefix = "+";

    public CommandManager() {
        commands = new ArrayList<>();

        commands.add(new HelpCmd());
    }

    public static void callCommandReturn(String input) {
        String message = input;

        if (!message.startsWith(prefix))
            return;

        message = message.substring(prefix.length());
        if (message.split(" ").length > 0) {
            boolean commandFound = false;
            String commandName = message.split(" ")[0];
            for (Command c : commands) {
                if (c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
                    c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                    commandFound = true;
                    break;
                }
            }
            if (!commandFound) {
                addChatMessage(Formatting.RED + "command not found, use" + Formatting.RESET + prefix + "help " + "" + Formatting.RED + "for help.");
            }
        }
    }

    // opens chat when prefix is clicked (called in MixinKeyboard).
    public void openChatScreen() {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), prefix.charAt(0)))
            if (prefix.length() == 1) {
                MinecraftClient.getInstance().setScreen(new ChatScreen(""));
            }
    }

    public void setCommandPrefix(String pre) {
        this.prefix = pre;

        if (YawnClient.INSTANCE.save != null) {
            try {
                YawnClient.INSTANCE.save.savePrefix();
            } catch (Exception ignored) {
            }
        }
    }

    public Command getCommand(String name) {
        for (Command c : this.commands) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * send a client side chat message without a prefix to the minecraft chat.
     *
     * @param message
     */
    public void addCustomChatMessage(String message) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(message));
    }

    /**
     * send a client side chat message with a prefix to the minecraft chat.
     *
     * @param message
     */
    @SuppressWarnings("resource")
    public static void addChatMessage(String message) {
        String messageWithPre = Formatting.AQUA + "[" + YawnClient.name + "]" + Formatting.GRAY + ": " + message;
        Text textComponentString = Text.literal(messageWithPre);

        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(textComponentString);
    }

    /**
     * send a client side message to the minecraft chat telling the user how to correctly use a certain command.
     *
     * @param name
     * @param syntax
     */
    @SuppressWarnings("resource")
    public void correctUsageMsg(String name, String syntax) {
        String usage = Formatting.RED + "correct usage of " + name + " command -> " + Formatting.GRAY + prefix + syntax;
        String message = Formatting.AQUA + "[" + YawnClient.name + "]" + Formatting.GRAY + ": " + usage;
        Text textComponentString = Text.literal(message);

        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(textComponentString);
    }

}