package com.github.medua7.apostlesbridge.commands;

import com.github.medua7.apostlesbridge.ApostlesBridge;
import com.github.medua7.apostlesbridge.config.ConfigScreen;
import com.github.medua7.apostlesbridge.handler.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ApostlesCommand extends CommandBase {

    ApostlesBridge apostlesBridge;
    public ApostlesCommand(ApostlesBridge apostlesBridge) {
        this.apostlesBridge = apostlesBridge;
    }

    @Override
    public String getCommandName() {
        return "apostles";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/apostles - Opens the settings menu";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof net.minecraft.entity.player.EntityPlayer) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reconnect")) {
                    MessageHandler.sendMessage("Restarting WebSocket and clearing session");
                    this.apostlesBridge.getWebSocketHandler().restartWebSocket(true);
                    return;
                }
            }
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            sender.addChatMessage(new ChatComponentText("This command can only be used in-game!"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen(this.apostlesBridge));
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
