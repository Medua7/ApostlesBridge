package com.github.medua7.apostlesbridge.handler;

import com.github.medua7.apostlesbridge.ApostlesBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class MessageHandler {
    public static final String MOD_PREFIX = "AB";

    ApostlesBridge apostlesBridge;
    public MessageHandler(ApostlesBridge apostlesBridge) {
        this.apostlesBridge = apostlesBridge;
    }

    public static String getPrefix() {
        return getPrefix(true, false);
    }
    public static String getPrefix(boolean colors) {
        return getPrefix(colors, false);
    }
    public static String getPrefix(boolean colors, boolean brackets) {
        return (brackets ? "§r[" : "") + (colors ? "§5" : "§r") + MOD_PREFIX + "§r" + (brackets ? "] " : " > ");
    }

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }
    public static void sendMessage(String message, boolean prefix) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText((prefix ? getPrefix() : "") + "§r" + message));
    }
    public static void sendMessage(String message, String prefix) {
        sendMessage(prefix + message, false);
    }

    public static void sendSpacerMessage() {
        sendMessage("=====================================================", false);
    }
}
