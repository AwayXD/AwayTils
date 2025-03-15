package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import xyz.awayxd.awaytils.commands.ModManager;
import xyz.awayxd.awaytils.utils.ChatUtils;

public class AutoPlay implements ModManager.ModLifecycle {

    private int countdown = -1;
    private long lastTime = 0;
    private Minecraft mc = Minecraft.getMinecraft();
    private static String playMode = "solo_insane";

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

    public static void setPlayMode(String mode) {
        if (mode.equalsIgnoreCase("solo_insane") || mode.equalsIgnoreCase("solo_normal")) {
            playMode = mode;
            ChatUtils.sendMessage(ChatUtils.getTagAwayTils() + EnumChatFormatting.GREEN + "AutoPlay mode set to: " + mode);
        } else {
            ChatUtils.sendMessage(ChatUtils.getTagAwayTils() + EnumChatFormatting.RED + "Invalid mode! Use solo_insane or solo_normal.");
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String cleanMessage = ChatUtils.getHypixelMessage(event.message);
        if (cleanMessage != null && (cleanMessage.toLowerCase().contains("you won! want to play again? click here!") ||
                cleanMessage.toLowerCase().contains("you died! want to play again? click here!"))) {
            ChatUtils.sendMessage(ChatUtils.getTagAwayTils() + EnumChatFormatting.GREEN + "Sending you to a new game!");
            countdown = 3;
            lastTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (countdown > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 1000) {
                lastTime = currentTime;
                countdown--;
            }
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();
            String text = EnumChatFormatting.DARK_GRAY + "AutoPlay : " + EnumChatFormatting.WHITE + countdown;
            int yOffset = height / 2 + 35;
            mc.fontRendererObj.drawStringWithShadow(text, width / 2 - mc.fontRendererObj.getStringWidth(text) / 2, yOffset, 0xFFFFFF);
        } else if (countdown == 0) {
            ChatUtils.sendCommand("play " + playMode);
            countdown = -1;
        }
    }

    public static class AutoPlayCommand extends CommandBase {
        @Override
        public String getCommandName() {
            return "autoplay";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/autoplay <solo_insane|solo_normal>";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            if (args.length == 1) {
                setPlayMode(args[0]);
            } else {
                ChatUtils.sendMessage(ChatUtils.getTagAwayTils() + EnumChatFormatting.RED + "Usage: /autoplay <solo_insane | solo_normal>");
            }
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 0;
        }
    }
}
