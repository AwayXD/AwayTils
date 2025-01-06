package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.awayxd.awaytils.commands.ModManager;
import xyz.awayxd.awaytils.utils.ChatUtils;

public class CorruptGame implements ModManager.ModLifecycle {

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

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        IChatComponent message = event.message;
        if (message != null) {
            String cleanMessage = ChatUtils.getHypixelMessage(event.message);
            if (cleanMessage != null && cleanMessage.toLowerCase().contains("the angel of death has corrupted this game!")) {
                new Thread(() -> {
                    for (int i = 0; i < 20; i++) {
                        sendMessageToChat(ChatUtils.getTagAwayTils() + EnumChatFormatting.DARK_PURPLE + "CORRUPT GAME!!!");
                        playBowDingSound();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    private void playBowDingSound() {
        Minecraft.getMinecraft().thePlayer.playSound("random.orb", 0.5F, 0.5F);
    }

    private void sendMessageToChat(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
}