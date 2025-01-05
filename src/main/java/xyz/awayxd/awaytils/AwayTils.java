package xyz.awayxd.awaytils;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import xyz.awayxd.awaytils.mods.AutoPlay;
import xyz.awayxd.awaytils.mods.AutoSprint;
import xyz.awayxd.awaytils.mods.KillSults;
import xyz.awayxd.awaytils.mods.SkywarsCounter;

@Mod(modid = AwayTils.MODID, version = AwayTils.VERSION)
public class AwayTils
{
    public static final String MODID = "awaytils";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)  {
        MinecraftForge.EVENT_BUS.register(new AutoPlay());
        MinecraftForge.EVENT_BUS.register(new KillSults());
        MinecraftForge.EVENT_BUS.register(new SkywarsCounter());
        MinecraftForge.EVENT_BUS.register(new AutoSprint());




    }
}
