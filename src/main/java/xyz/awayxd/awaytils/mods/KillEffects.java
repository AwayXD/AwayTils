package xyz.awayxd.awaytils.mods;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;

import xyz.awayxd.awaytils.commands.ModManager;


public class KillEffects implements ModManager.ModLifecycle {

    private final Minecraft mc = Minecraft.getMinecraft();
    private EntityLivingBase target;

    private void playBlockBreak() {
        mc.thePlayer.playSound("dig.stone", 1F, 1F);
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

    public void spawnParticles() {
        if (target == null) return;

        double startY = target.posY;
        double endY = target.posY + target.height + 0.4;
        double step = 0.4;

        for (int i = 0; i < 100; i++) {
            for (double y = startY; y <= endY; y += step) {
                mc.theWorld.spawnParticle(
                        EnumParticleTypes.BLOCK_CRACK,
                        target.posX,
                        y,
                        target.posZ,
                        0, 0, 0,
                        Block.getStateId(Blocks.redstone_block.getDefaultState())
                );
            }
        }
        playBlockBreak();
        this.target = null;
    }
}


