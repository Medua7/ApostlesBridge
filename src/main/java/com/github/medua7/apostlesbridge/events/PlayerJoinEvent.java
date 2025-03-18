package com.github.medua7.apostlesbridge.events;

import com.github.medua7.apostlesbridge.ApostlesBridge;
import com.github.medua7.apostlesbridge.config.Config;
import com.github.medua7.apostlesbridge.handler.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerJoinEvent {

    private static String lastServerIP = "";

    ApostlesBridge apostlesBridge;

    public PlayerJoinEvent(ApostlesBridge apostlesBridge) {
        this.apostlesBridge = apostlesBridge;
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer && event.world.isRemote && event.entity == Minecraft.getMinecraft().thePlayer) {
            ServerData currentServer = Minecraft.getMinecraft().getCurrentServerData();
            String serverIP = (currentServer != null) ? currentServer.serverIP : "singleplayer";

            if (!serverIP.equals(lastServerIP)) {
                lastServerIP = serverIP;
                if (Config.getGuild().isEmpty() && Config.getURL().isEmpty() && Config.getToken().isEmpty()) {
                    MessageHandler.sendSpacerMessage();
                    MessageHandler.sendMessage("§c§lATTENTION", false);
                    MessageHandler.sendMessage("§cThere is no info set for Apostles Bridge.", false);
                    MessageHandler.sendMessage("§cUse §4/apostles §cto set one", false);
                    MessageHandler.sendSpacerMessage();
                }
            }
        }
    }
}
