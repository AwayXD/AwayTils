package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.awayxd.awaytils.commands.ModManager;
import xyz.awayxd.awaytils.utils.ChatUtils;

public class AutoList implements ModManager.ModLifecycle {

    private static final Minecraft mc = Minecraft.getMinecraft();
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String cleanMessage = ChatUtils.getHypixelMessage(event.message);
        if (cleanMessage != null && (cleanMessage.toLowerCase().contains("to access powerful upgrades.") ||
                cleanMessage.toLowerCase().contains("cages opened! fight!"))) {
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    mc.thePlayer.sendChatMessage("/list");
                } catch (InterruptedException ignored) {}
            }).start();
            ChatUtils.sendMessage(ChatUtils.getTagAwayTils() + EnumChatFormatting.GREEN + "debug");

        }
    }
    @Override
    public String getTag() {
        return null;
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}

