package xyz.awayxd.awaytils.commands;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.config.Configuration;
import xyz.awayxd.awaytils.mods.*;

public class ModManager {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Map<String, Boolean> modStates = new HashMap<>();
    private static final Map<String, Object> modules = new HashMap<>();
    private static Configuration config;

    static {
        modStates.put("AutoPlay", false);
        modules.put("AutoPlay", new AutoPlay());
        modStates.put("SkywarsCounter", false);
        modules.put("SkywarsCounter", new SkywarsCounter());
        modStates.put("KillSults", false);
        modules.put("KillSults", new KillSults());
    }

    public static void initialize() {
        File configFile = new File(mc.mcDataDir, "config/awaytils.cfg");
        config = new Configuration(configFile);
        loadConfig();
        ClientCommandHandler.instance.registerCommand(new UtilsCommand());
        MinecraftForge.EVENT_BUS.register(new ModManager());
    }

    public static void loadConfig() {
        try {
            config.load();
            modStates.put("AutoPlay", config.get("modSettings", "AutoPlay", false).getBoolean());
            modStates.put("SkywarsCounter", config.get("modSettings", "SkywarsCounter", false).getBoolean());
            modStates.put("KillSults", config.get("modSettings", "KillSults", false).getBoolean());
        } catch (Exception e) {
            System.out.println("Failed to load config file.");
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static void saveConfig() {
        try {
            for (Map.Entry<String, Boolean> entry : modStates.entrySet()) {
                config.get("modSettings", entry.getKey(), false).set(entry.getValue());
            }
            if (config.hasChanged()) {
                config.save();
            }
        } catch (Exception e) {
            System.out.println("Failed to save config file.");
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
