package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class AutoPlay {
    // done but only works on solo normal - away

    private int countdown = -1;
    private long lastTime = 0;

    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        IChatComponent message = event.message;
        if (message != null) {
            String cleanMessage = message.getUnformattedText().replaceAll("§.", "");

            if (cleanMessage.toLowerCase().contains("you won! want to play again? click here!")) {
            }
            if (cleanMessage.toLowerCase().contains("you died! want to play again? click here!")) {
            }
                countdown = 5;
                lastTime = System.currentTimeMillis();
            }
        }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (countdown > 0) {
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();

            String text = "AutoPlay : " + countdown;
            mc.fontRendererObj.drawStringWithShadow(text, width / 2 - mc.fontRendererObj.getStringWidth(text) / 2, height / 2, 0xFFFFFF);
            countdown--;
        } else if (countdown == 0) {
                mc.thePlayer.sendChatMessage("/play solo_normal");
            }
            countdown = -1;
        }
    }

