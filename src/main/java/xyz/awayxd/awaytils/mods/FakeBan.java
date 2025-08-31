package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.awayxd.awaytils.commands.ModManager;
import xyz.awayxd.awaytils.utils.ChatUtils;

import java.util.Random;

public class FakeBan implements ModManager.ModLifecycle {
    @Override
    public String getTag() {
        return "FakeBan";
    }

    @Override
    public void onEnable() {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        IChatComponent message = event.message;
        if (message != null) {
            String cleanMessage = ChatUtils.getHypixelMessage(event.message);

            if (cleanMessage != null && cleanMessage.toLowerCase().contains(".ban")) {
                // Prevent the original .ban message from showing
                event.setCanceled(true);

                ChatUtils.sendMessage(
                        EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD.toString() +
                                "A player has been removed from your game."
                );
                ChatUtils.sendMessage(
                        EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD.toString() +
                                "Use /report to continue helping out the server!"
                );
            }

            String username = Minecraft.getMinecraft().thePlayer.getName();
            if (cleanMessage != null && cleanMessage.toLowerCase().contains((".ban " + username).toLowerCase())) {
                event.setCanceled(true);

                String banId = generateRandomHexId(8).toUpperCase();

                Minecraft.getMinecraft().getNetHandler().getNetworkManager()
                        .closeChannel(new ChatComponentText(
                                EnumChatFormatting.RED + "You are temporarily banned for " + EnumChatFormatting.WHITE + "29d 23h 59m 57s" + EnumChatFormatting.RED + " from this server!\n" +
                                        EnumChatFormatting.GRAY + "Reason: " + EnumChatFormatting.WHITE + "Cheating through the use of unfair game advantages.\n" +
                                        "Find out more: " + EnumChatFormatting.AQUA + "https://www.hypixel.net/appeal\n\n" +
                                        EnumChatFormatting.GRAY + "Ban ID: " + EnumChatFormatting.WHITE + "#" + banId + "\n" +
                                        EnumChatFormatting.GRAY + "Sharing your Ban ID may affect the processing of your appeal!"
                        ));
            }
        }
    }


            private String generateRandomHexId(int length) {
        Random random = new Random();
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < length; i++) {
            hex.append(Integer.toHexString(random.nextInt(16)));
        }
        return hex.toString();
    }
}
