package com.github.medua7.apostlesbridge.handler;

import com.google.gson.*;
import com.github.medua7.apostlesbridge.ApostlesBridge;
import com.github.medua7.apostlesbridge.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocketHandler {
    private static final LogHandler LOGGER = new LogHandler(WebSocketHandler.class);

    WebSocketClient webSocketClient;

    private Timer reconnectTimer;
    private static final int RECONNECT_DELAY = 30_000;

    private String authKey = "";

    ApostlesBridge apostlesBridge;

    public WebSocketHandler(ApostlesBridge apostlesBridge) {
        this.apostlesBridge = apostlesBridge;

        waitForPlayerAndConnect();
    }

    private void waitForPlayerAndConnect() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Minecraft.getMinecraft().thePlayer != null) {
                    LOGGER.debug("Player detected! Proceeding with WebSocket connection.");
                    if (shouldConnect()) {
                        connect();
                    }
                    cancel();
                } else {
                    LOGGER.debug("Waiting for player to initialize...");
                }
            }
        }, 0, 500);
    }

    public void connect() {
        if (!canConnect()) {
            LOGGER.warn("Canceled connecting to WebSocket, as the url or the token are unset.");
            return;
        }

        if (webSocketClient != null && !webSocketClient.isClosed()) {
            LOGGER.debug("Closing existing WebSocket connection before reconnecting...");
            webSocketClient.close();
        }

        LOGGER.debug("Trying to connect to WebSocket (" + getServerURL() + ")");
        try {
            webSocketClient = new WebSocketClient(new URI(getServerURL())) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    LOGGER.debug("Connected to WebSocket!");
                }

                @Override
                public void onMessage(String messageJson) {
                    try {
                        LOGGER.debug("WebSocket Recieved: " + messageJson);
                        JsonObject json = new Gson().fromJson(messageJson, JsonObject.class);

                        if (json.has("type")) {
                            String messageType = json.get("type").getAsString();

                            if (messageType.equals("authKey")) {
                                authKey = json.get("authKey").getAsString();
                                LOGGER.debug("Received new auth-key: " + authKey);
                                restartWebSocket();
                            } else if (messageType.equals("message") && json.has("messageData")) {
                                JsonObject messageData = json.getAsJsonObject("messageData");
                                String origin = messageData.has("origin") ? messageData.get("origin").getAsString() : "";
                                String originLongname = messageData.has("originLongname") ? messageData.get("originLongname").getAsString() : "";
                                String message = messageData.has("message") ? messageData.get("message").getAsString() : "";
                                JsonArray images = messageData.has("images") ? messageData.get("images").getAsJsonArray() : new JsonArray();
                                List<String> imageList = new ArrayList<>();

                                for (JsonElement element : images) {
                                    if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                                        imageList.add(element.getAsString());
                                    }
                                }

                                if (Config.getGuild().isEmpty() || (!origin.equals(Config.getGuild()) && !originLongname.equals(Config.getGuild()))) {
                                    MessageHandler.sendMessageWithImages(message, false, imageList);
                                }
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        LOGGER.error("WebSocket error parsing the response: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    LOGGER.debug("Disconnected from WebSocket: " + reason);
                    scheduleReconnect();
                }

                @Override
                public void onError(Exception e) {
                    LOGGER.error("WebSocket error: " + e.getMessage());
                    scheduleReconnect();
                }
            };
        } catch (URISyntaxException e) {
            LOGGER.error("An error occured trying to connect to the WebSocket (" + e.getMessage() + ")");
            e.printStackTrace();
        }
        webSocketClient.connect();
    }

    private boolean canConnect() {
        return !Config.getURL().isEmpty() && !Config.getToken().isEmpty();
    }

    private boolean shouldConnect() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            LOGGER.debug("Reconnect skipped: WebSocket is already connected.");
            return false;
        }

        int mode = Config.getGeneralMode();
        switch (mode) {
            case 0: // OFF
                LOGGER.debug("WebSocket connection canceled: Mode is OFF.");
                return false;
            case 1: // EVERYWHERE
                return true;
            case 2: // HYPIXEL_ONLY
                ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
                if (serverData != null && serverData.serverIP.contains("hypixel.net")) {
                    LOGGER.debug("Player is on Hypixel. Connecting to WebSocket...");
                    return true;
                } else {
                    LOGGER.debug("WebSocket connection canceled: Not on Hypixel.");
                    return false;
                }
            default:
                LOGGER.debug("Unknown mode detected. WebSocket will NOT connect.");
                return false;
        }
    }

    private String getServerURL() {
        return getServerURL(Config.getToken());
    }

    private String getServerURL(String token) {
        String username = Minecraft.getMinecraft().thePlayer.getName();
        String uuid = Minecraft.getMinecraft().thePlayer.getUniqueID().toString();

        return "ws://" + Config.getURL() + "?token=" + token + "&authKey=" + authKey + "&username=" + username + "&uuid=" + uuid;
    }

    private synchronized void scheduleReconnect() {
        if (reconnectTimer != null) {
            reconnectTimer.cancel();
            reconnectTimer.purge();
        }

        reconnectTimer = new Timer();
        reconnectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (shouldConnect()) {
                    LOGGER.info("Reconnecting to WebSocket...");
                    restartWebSocket();
                } else {
                    LOGGER.debug("Reconnect skipped due to mode restrictions or connection status.");
                }
            }
        }, RECONNECT_DELAY);
    }

    public synchronized void restartWebSocket() {
        this.restartWebSocket(false);
    }

    public synchronized void restartWebSocket(boolean clearSession) {
        if (clearSession) {
            authKey = "";
        }

        if (reconnectTimer != null) {
            reconnectTimer.cancel();
        }

        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
        }

        if (shouldConnect()) {
            connect();
        } else {
            LOGGER.debug("Restart skipped due to mode restrictions.");
        }
    }
}
