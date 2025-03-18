package com.github.medua7.apostlesbridge;

import com.github.medua7.apostlesbridge.commands.ApostlesCommand;
import com.github.medua7.apostlesbridge.config.Config;
import com.github.medua7.apostlesbridge.events.PlayerJoinEvent;
import com.github.medua7.apostlesbridge.handler.LogHandler;
import com.github.medua7.apostlesbridge.handler.WebSocketHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ApostlesBridge.MODID, version = ApostlesBridge.VERSION)
public class ApostlesBridge {
    public static final String MODID = "ApostlesBridge";
    public static final String VERSION = "1.0.1";

    private static final LogHandler LOGGER = new LogHandler(ApostlesBridge.class);

    private WebSocketHandler webSocketHandler;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println();
        LOGGER.info(MODID + " v" + VERSION + " initializing..");

        // REGISTER COMMANDS
        ClientCommandHandler.instance.registerCommand(new ApostlesCommand(this));

        //REGISTER EVENTS
        MinecraftForge.EVENT_BUS.register(new PlayerJoinEvent(this));

        // LOAD CONFIG
        Config.loadConfig();

        webSocketHandler = new WebSocketHandler(this);
    }

    public WebSocketHandler getWebSocketHandler() {
        return this.webSocketHandler;
    }
}
