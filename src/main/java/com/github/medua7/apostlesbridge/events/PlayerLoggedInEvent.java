package com.github.medua7.apostlesbridge.events;

import com.github.medua7.apostlesbridge.ApostlesBridge;
import com.github.medua7.apostlesbridge.handler.WebSocketHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerLoggedInEvent {

    private final ApostlesBridge apostlesBridge;
    private final WebSocketHandler socketHandler;

    public PlayerLoggedInEvent(ApostlesBridge apostlesBridge, WebSocketHandler handler) {
        this.apostlesBridge = apostlesBridge;
        this.socketHandler = handler;
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player == Minecraft.getMinecraft().thePlayer) {
            socketHandler.restartWebSocket();
        }
    }
}
