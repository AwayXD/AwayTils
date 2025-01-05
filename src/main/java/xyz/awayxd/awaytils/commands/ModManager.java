package xyz.awayxd.awaytils.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private static File configFile;

    static {
        modStates.put("AutoPlay", false);
        modules.put("AutoPlay", new AutoPlay());
        modStates.put("SkywarsCounter", false);
        modules.put("SkywarsCounter", new SkywarsCounter());
        modStates.put("KillSults", false);
        modules.put("KillSults", new KillSults());
    }

    public static void initialize() {
        File configDir = new File(mc.mcDataDir, "config/awaytils_config");
        if (!configDir.exists()) {
            if (configDir.mkdirs()) {
                System.out.println("Created config directory at: " + configDir.getAbsolutePath());
            } else {
                System.out.println("Failed to create config directory at: " + configDir.getAbsolutePath());
            }
        } else {
            System.out.println("Config directory already exists at: " + configDir.getAbsolutePath());
        }

        configFile = new File(configDir, "awaytils_settings.txt");
        System.out.println("Config file path: " + configFile.getAbsolutePath());

        if (!configFile.exists()) {
            try {
                if (configFile.createNewFile()) {
                    System.out.println("Created config file at: " + configFile.getAbsolutePath());
                } else {
                    System.out.println("Config file already exists at: " + configFile.getAbsolutePath());
                }
            } catch (IOException e) {
                System.out.println("Failed to create config file at: " + configFile.getAbsolutePath());
                e.printStackTrace();
            }
        } else {
            System.out.println("Config file already exists at: " + configFile.getAbsolutePath());
        }

        loadConfig();
    }

    public static void loadConfig() {
        if (configFile == null || !configFile.exists()) {
            return;  // No config file, use default values
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

    public static void saveConfig() {
        if (configFile == null) {
            System.out.println("Config file is null, cannot save.");
            return;
        }

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
            saveConfig();  // Save the settings after toggling
        } else {
            mc.thePlayer.addChatMessage(new ChatComponentText("Mod not found: " + modName));
        }
    }

    public interface ModLifecycle {
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
