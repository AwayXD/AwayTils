package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import xyz.awayxd.awaytils.commands.ModManager;

public class BackStab implements ModManager.ModLifecycle {

    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean alertActive = false;

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!(mc.thePlayer instanceof EntityPlayerSP)) return;
        EntityPlayerSP player = mc.thePlayer;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityPlayer) || entity == player) continue;

            EntityPlayer target = (EntityPlayer) entity;

            if (isBehindPlayer(player, target) && isHoldingSword(target) && isWithinDistance(player, target, 3)) {
                alertPlayer();
            }
        }
    }

    @SubscribeEvent
    public void onRenderHUD(RenderGameOverlayEvent.Text event) {
        if (alertActive) {
            mc.fontRendererObj.drawString("Warning: Player Behind You!", 10, 10, 0xFF0000);
        }
    }

    private boolean isBehindPlayer(EntityPlayer player, EntityPlayer target) {
        double angle = MathHelper.wrapAngleTo180_float(player.rotationYaw - getYawToTarget(player, target));
        return angle > 90 || angle < -90;
    }

    private float getYawToTarget(EntityPlayer player, Entity target) {
        double dx = target.posX - player.posX;
        double dz = target.posZ - player.posZ;
        return (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
    }

    private boolean isHoldingSword(EntityPlayer player) {
        return player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemSword;
    }

    private boolean isWithinDistance(EntityPlayer player, Entity target, double distance) {
        return player.getDistanceToEntity(target) <= distance;
    }

    private void alertPlayer() {
        if (!alertActive) {
            alertActive = true;
            mc.thePlayer.playSound("note.bass", 1.0F, 1.0F);
            // Schedule reset of alert
            mc.addScheduledTask(() -> alertActive = false);
        }
    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
