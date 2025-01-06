package xyz.awayxd.awaytils.commands;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import xyz.awayxd.awaytils.mods.*;

public class ModManager {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Map<String, Boolean> modStates = new HashMap<>();
    private static final Map<String, Object> modules = new HashMap<>();
    private static final File configFile = new File(Minecraft.getMinecraft().mcDataDir, "awaytils_mods.cfg");

    static {
        modStates.put("AutoPlay", false);
        modules.put("AutoPlay", new AutoPlay());
        modStates.put("SkywarsCounter", false);
        modules.put("SkywarsCounter", new SkywarsCounter());
        modStates.put("KillSults", false);
        modules.put("KillSults", new KillSults());
        loadConfig();
    }

    public static void loadConfig() {
        if (!configFile.exists()) {
            return; // No config file, use default values
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String modName = parts[0].trim();
                    boolean enabled = Boolean.parseBoolean(parts[1].trim());
                    modStates.put(modName, enabled);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load config file.");
            e.printStackTrace();
        }
    }

    private static void saveConfig() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            for (Map.Entry<String, Boolean> entry : modStates.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save config file.");
            e.printStackTrace();
        }
    }

    public static boolean isModEnabled(String modName) {
        return modStates.getOrDefault(modName, false);
    }

    public static void toggleMod(String modName) {
        if (modStates.containsKey(modName)) {
            boolean newState = !modStates.get(modName);
            modStates.put(modName, newState);
            saveConfig(); // Save updated states to the config file
            String status = newState ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled";

            if (modules.containsKey(modName)) {
                Object module = modules.get(modName);
                if (module instanceof ModLifecycle) {
                    if (newState) {
                        ((ModLifecycle) module).onEnable();
                    } else {
                        ((ModLifecycle) module).onDisable();
                    }
                }
            }

            mc.thePlayer.addChatMessage(new ChatComponentText(modName + " has been " + status + "."));
        } else {
            mc.thePlayer.addChatMessage(new ChatComponentText("Mod not found: " + modName));
        }
    }

    public interface ModLifecycle {
        String getTag();

        void onEnable();
        void onDisable();
    }

    public static class UtilsCommand extends CommandBase {
        @Override
        public String getCommandName() {
            return "utils";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/utils [mod]";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            if (args.length == 0) {
                mc.thePlayer.addChatMessage(new ChatComponentText("-------------------"));
                StringBuilder modList = new StringBuilder(EnumChatFormatting.LIGHT_PURPLE + "Away's Utils" + EnumChatFormatting.WHITE + " :\n");
                modStates.forEach((mod, enabled) -> modList.append(mod)
                        .append(enabled ? EnumChatFormatting.GREEN + " [Enabled]" : EnumChatFormatting.RED + " [Disabled]").append("\n"));
                mc.thePlayer.addChatMessage(new ChatComponentText(modList.toString()));
                mc.thePlayer.addChatMessage(new ChatComponentText("-------------------"));
            } else {
                String modName = args[0];
                toggleMod(modName);
            }
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 0;
        }
    }
}
