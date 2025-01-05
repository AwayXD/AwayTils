package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class AutoSprint {
    private final Minecraft mc = Minecraft.getMinecraft();

    public void onUpdate() {
        EntityPlayerSP player = mc.thePlayer;

        if (player != null && !player.isSprinting() && player.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
        }
    }
}

