package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoPlay {

    private int countdown = -1;
    private long lastTime = 0;
    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        IChatComponent message = event.message;
        if (message != null) {
            String cleanMessage = message.getUnformattedText().replaceAll("§.", "");

            if (cleanMessage.toLowerCase().contains("you won! want to play again? click here!")) {
                countdown = 3;
                lastTime = System.currentTimeMillis();
            } else if (cleanMessage.toLowerCase().contains("you died! want to play again? click here!")) {
                countdown = 3;
                lastTime = System.currentTimeMillis();
            }
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
            int yOffset = height / 2 + 20;
            mc.fontRendererObj.drawStringWithShadow(text, width / 2 - mc.fontRendererObj.getStringWidth(text) / 2, yOffset, 0xFFFFFF);

        } else if (countdown == 0) {
            mc.thePlayer.sendChatMessage("/play solo_normal");
            countdown = -1;
        }
    }
}
