package xyz.awayxd.awaytils.utils;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;


public class ChatUtils {

    public static String getTagAwayTils() {
        return EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.LIGHT_PURPLE + "AwayTils" + EnumChatFormatting.DARK_GRAY + "] ";
    }

    public static String getHypixelMessage(IChatComponent chatComponent) {
        if (chatComponent == null) {
            return null;
        }
        String cleanMessage = chatComponent.getUnformattedText().replaceAll("§.", "");
        return getTagAwayTils() + cleanMessage;
    }
}
