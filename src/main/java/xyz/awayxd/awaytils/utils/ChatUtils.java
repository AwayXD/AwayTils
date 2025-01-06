package xyz.awayxd.awaytils.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;


public class ChatUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static String getTagAwayTils() {
        return EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.LIGHT_PURPLE + "AwayTils" + EnumChatFormatting.DARK_GRAY + "] ";
    }
    public static void sendMessage(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
    public static void sendCommand(String command) {
        if (mc.thePlayer != null && command != null && !command.isEmpty()) {
            mc.thePlayer.sendChatMessage("/" + command);
        }
    }


    public static String getHypixelMessage(IChatComponent chatComponent) {
        if (chatComponent == null) {
            return null;
        }
        String cleanMessage = chatComponent.getUnformattedText().replaceAll("§.", "");
        return getTagAwayTils() + cleanMessage;
    }
}
