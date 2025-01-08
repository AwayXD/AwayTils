package xyz.awayxd.awaytils.mods;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.awayxd.awaytils.commands.ModManager;
import xyz.awayxd.awaytils.utils.ChatUtils;

public class AutoFriend implements ModManager.ModLifecycle {

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String cleanMessage = ChatUtils.getHypixelMessage(event.message);
        if (cleanMessage != null && cleanMessage.contains("[ACCEPT] - [DENY] - [BLOCK]")) {
            System.out.println("pasiergboiaejhrb goljikahderfbg.");
            ChatUtils.sendCommand("f accept");
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

