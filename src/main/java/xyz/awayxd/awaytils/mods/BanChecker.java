package xyz.awayxd.awaytils.mods;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import xyz.awayxd.awaytils.commands.ModManager;
import xyz.awayxd.awaytils.utils.HttpUtils;
import xyz.awayxd.awaytils.utils.MSTimer;

public class BanChecker  {

    private static final String API_PUNISHMENT = decodeHex("68747470733a2f2f6170692e706c616e636b652e696f2f6879706978656c2f76312f70756e6973686d656e745374617473");
    public static int WATCHDOG_BAN_LAST_MIN = 0;
    public static int LAST_TOTAL_STAFF = -1;
    public static int STAFF_BAN_LAST_MIN = 0;
    private String checkTag = "Idle...";

    public BanChecker() {
        new Thread("Hypixel-BanChecker") {
            @Override
            public void run() {
                MSTimer checkTimer = new MSTimer();
                while (true) {
                    if (!checkTimer.hasTimePassed(60000L)) {
                        continue;
                    }
                    try {
                        String apiContent = HttpUtils.get(API_PUNISHMENT);
                        JsonObject jsonObject = new JsonParser().parse(apiContent).getAsJsonObject();

                        if (jsonObject.get("success").getAsBoolean() && jsonObject.has("record")) {
                            JsonObject objectAPI = jsonObject.getAsJsonObject("record");
                            WATCHDOG_BAN_LAST_MIN = objectAPI.get("watchdog_lastMinute").getAsInt();
                            int staffBanTotal = objectAPI.get("staff_total").getAsInt();

                            if (staffBanTotal < LAST_TOTAL_STAFF) {
                                staffBanTotal = LAST_TOTAL_STAFF;
                            }

                            if (LAST_TOTAL_STAFF == -1) {
                                LAST_TOTAL_STAFF = staffBanTotal;
                            } else {
                                STAFF_BAN_LAST_MIN = staffBanTotal - LAST_TOTAL_STAFF;
                                LAST_TOTAL_STAFF = staffBanTotal;
                            }

                            checkTag = STAFF_BAN_LAST_MIN + "";

                            if (isActive() && Minecraft.getMinecraft().thePlayer != null && isOnHypixel()) {
                                if (STAFF_BAN_LAST_MIN > 0) {
                                    sendMessageToChat(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.LIGHT_PURPLE + "AwayTils" + EnumChatFormatting.DARK_GRAY + "] " + EnumChatFormatting.RED + "Staff have banned " + EnumChatFormatting.DARK_RED + STAFF_BAN_LAST_MIN + EnumChatFormatting.RED + " players in the last minute!");
                                } else {
                                    sendMessageToChat(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.LIGHT_PURPLE + "AwayTils" + EnumChatFormatting.DARK_GRAY + "] " + EnumChatFormatting.RED + "No players were banned by staff in the last minute.");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isActive() && Minecraft.getMinecraft().thePlayer != null && isOnHypixel()) {
                            sendMessageToChat("An error occurred while checking bans.");
                        }
                    }
                    checkTimer.reset();
                }
            }
        }.start();
    }

    public boolean isOnHypixel() {
        return !Minecraft.getMinecraft().isSingleplayer() && Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("hypixel.net");
    }

    private void sendMessageToChat(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    private static String decodeHex(String hex) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String st = hex.substring(i, i + 2);
            char ch = (char) Integer.parseInt(st, 16);
            result.append(ch);
        }
        return result.toString();
    }


    public boolean isActive() {
        return true; // Replace with actual logic for determining if the mod is active
    }
}
