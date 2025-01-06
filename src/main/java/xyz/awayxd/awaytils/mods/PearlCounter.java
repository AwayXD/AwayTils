package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import xyz.awayxd.awaytils.commands.ModManager;
import xyz.awayxd.awaytils.utils.ChatUtils;

public class PearlCounter implements ModManager.ModLifecycle {

    private final Minecraft mc = Minecraft.getMinecraft();
    private int countdown = -1;
    private long lastTime = 0;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String cleanMessage = ChatUtils.getHypixelMessage(event.message);
        if (cleanMessage != null && cleanMessage.toLowerCase().contains("cages opened! fight!")) {
            countdown = 30;
            lastTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {
        if (countdown > 0 && hasEnderPearlInHotbar()) {
            drawCountdownText();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (countdown > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 1000) {
                countdown--;
                lastTime = currentTime;
            }
        }
    }

    private boolean hasEnderPearlInHotbar() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() == Items.ender_pearl) {
                return true;
            }
        }
        return false;
    }

    private void drawCountdownText() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();
        String timerText = EnumChatFormatting.DARK_PURPLE + "Pearl Timer" + EnumChatFormatting.WHITE + " : " + EnumChatFormatting.RED + countdown + "s";
        int x = screenWidth - mc.fontRendererObj.getStringWidth(timerText) - 300;
        int y = screenHeight - 15;
        mc.fontRendererObj.drawString(timerText, x, y, 0xFFFFFF, true);
    }

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
}
