package creeperpookie.creeperprotect;

import creeperpookie.creeperprotect.handlers.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = CreeperProtectMod.MODID, name = CreeperProtectMod.NAME, version = CreeperProtectMod.VERSION, acceptableRemoteVersions = "*")
public class CreeperProtectMod
{
    public static final String MODID = "creeperprotect";
    public static final String NAME = "Creeper Protect";
    public static final String VERSION = "1.0";

    private static Logger logger;

    public CreeperProtectMod()
    {

    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("Initializing CreeperProtect");
        MinecraftForge.EVENT_BUS.register(new PacketHandler());
    }

    public static Logger getLogger()
    {
        return logger;
    }
}
