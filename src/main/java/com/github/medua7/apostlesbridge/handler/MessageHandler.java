package com.github.medua7.apostlesbridge.handler;

import com.github.medua7.apostlesbridge.ApostlesBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

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

    public static void sendMessage(IChatComponent messageComponent) {
        sendMessage(messageComponent, true);
    }

    public static void sendMessage(IChatComponent messageComponent, boolean prefix) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(messageComponent);
    }

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean prefix) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(getChatComponentTextForMessage(message, prefix));
    }

    public static void sendMessageWithImages(String message, boolean prefix, List<String> imageURLs) {
        IChatComponent messageComponent = getChatComponentTextForMessage(message, prefix);

        if (!imageURLs.isEmpty()) {
            messageComponent.appendSibling(new ChatComponentText(" "));

            int imageCount = 1;
            for (String imageURL : imageURLs) {
                if (imageCount > 1) {
                    messageComponent.appendSibling(new ChatComponentText(", "));
                }
                IChatComponent imageComponent = new ChatComponentText("§r[§dIMAGE_" + imageCount++ + "§r]");
                imageComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to open the image")));
                imageComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, imageURL));

                messageComponent.appendSibling(imageComponent);
            }
        }

        sendMessage(messageComponent, prefix);
    }

    private static ChatComponentText getChatComponentTextForMessage(String message) {
        return getChatComponentTextForMessage(message, true);
    }
    private static ChatComponentText getChatComponentTextForMessage(String message, boolean prefix) {
        return new ChatComponentText((prefix ? getPrefix() : "") + "§r" + message);
    }

    public static void sendSpacerMessage() {
        sendMessage("=====================================================", false);
    }
}
