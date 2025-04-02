package com.github.medua7.apostlesbridge.commands;

import com.github.medua7.apostlesbridge.ApostlesBridge;
import com.github.medua7.apostlesbridge.config.Config;
import com.github.medua7.apostlesbridge.config.ConfigScreen;
import com.github.medua7.apostlesbridge.config.Ignored;
import com.github.medua7.apostlesbridge.config.IgnoredType;
import com.github.medua7.apostlesbridge.handler.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApostlesCommand extends CommandBase {

    ApostlesBridge apostlesBridge;
    public ApostlesCommand(ApostlesBridge apostlesBridge) {
        this.apostlesBridge = apostlesBridge;
    }


    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Arrays.asList("reconnect", "status", "ignore"));
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("ignore")) {
            return getListOfStringsMatchingLastWord(args, Arrays.asList("add", "remove", "list"));
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("ignore") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))) {
            return getListOfStringsMatchingLastWord(args, Arrays.asList("player", "origin"));
        }
        return Collections.emptyList();
    }

    @Override
    public String getCommandName() {
        return "apostles";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("apostlesbridge", "bridge");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/apostles - Opens the settings menu";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof net.minecraft.entity.player.EntityPlayer) {
            if (args.length == 0) {
                MinecraftForge.EVENT_BUS.register(this);
                return;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reconnect")) {
                    MessageHandler.sendMessage("Restarting WebSocket and clearing session");
                    this.apostlesBridge.getWebSocketHandler().restartWebSocket(true);
                    return;
                } else if (args[0].equalsIgnoreCase("status")) {
                    MessageHandler.sendMessage("WebSocket connection: "+this.apostlesBridge.getWebSocketHandler().getStatus());
                    return;
                } else if (args[0].equalsIgnoreCase("ignore")) {
                    MessageHandler.sendMessage("Command usage: §§d/bridge ignore <add/remove/list> [player/origin] [name]");
                    return;
                } else if (args[0].equalsIgnoreCase("help")) {
                    MessageHandler.sendMessage("§lApostles Command Usages", false);
                    MessageHandler.sendMessage("§d/bridge reconnect §7- Clears the session and restarts the WebSocket-connection", false);
                    MessageHandler.sendMessage("§d/bridge status §7- Returns the current status of the WebSocket-connection", false);
                    MessageHandler.sendMessage("§d/bridge ignore list §7- Lists all ignored players and origins", false);
                    MessageHandler.sendMessage("§d/bridge ignore add <player/origin> [name] §7- Adds the selected player or origin to the ignore list", false);
                    MessageHandler.sendMessage("§d/bridge ignore remove <player/origin> [name] §7- Removes the selected player or origin from the ignore list", false);
                    return;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("ignore")) {
                    if (args[1].equalsIgnoreCase("list")) {

                        List<String> playerNames = Config.getIgnoredListNames(IgnoredType.PLAYER);
                        List<String> originNames = Config.getIgnoredListNames(IgnoredType.ORIGIN);
                        if (playerNames.isEmpty() && originNames.isEmpty()) {
                            MessageHandler.sendMessage("The ignore list is empty! §a^-^");
                        } else {
                            if (!playerNames.isEmpty()) {
                                String players = String.join(", ", playerNames);
                                sender.addChatMessage(new ChatComponentText("§dIgnored Players§r: " + players));
                            }

                            if (!originNames.isEmpty()) {
                                String origins = String.join(", ", originNames);
                                sender.addChatMessage(new ChatComponentText("§dIgnored Origins§r: " + origins));
                            }
                        }
                        return;
                    }
                }
            } else if (args.length >= 4) {
                if (args[0].equalsIgnoreCase("ignore")) {
                    try {
                        IgnoredType ignoredType = IgnoredType.valueOf(args[2].toUpperCase());
                        String name = ignoredType == IgnoredType.PLAYER ? args[3] : String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                        Ignored ignored = new Ignored(name, ignoredType);
                        if (args[1].equalsIgnoreCase("add")) {
                            if (!Config.isIgnored(ignored)) {
                                Config.addIgnored(new Ignored(name, ignoredType));
                                Config.saveConfig();
                                MessageHandler.sendMessage("The §7" + ignoredType + " §d" + name + " §rwas added to the ignore list");
                            } else {
                                MessageHandler.sendMessage("The §7" + ignoredType + " §d" + name + " §ris already on the ignore list");
                            }
                            return;
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (Config.isIgnored(ignored)) {
                                Config.removeIgnored(new Ignored(name, ignoredType));
                                Config.saveConfig();
                                MessageHandler.sendMessage("The §7" + ignoredType + " §d" + name + " §rwas removed from the ignore list");
                            } else {
                                MessageHandler.sendMessage("The §7" + ignoredType + " §d" + name + " §ris not on the ignore list");
                            }
                            return;
                        }
                    } catch (IllegalArgumentException e) {
                        MessageHandler.sendMessage("Command usage: §d/bridge ignore <add/remove/list> [player/origin] [name]");
                        return;
                    }
                }
            }
            MessageHandler.sendMessage("Incorrect usage. Please use §d/bridge help §rto get a list of all commands and their usage");
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
