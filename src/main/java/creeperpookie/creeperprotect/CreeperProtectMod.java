package creeperpookie.creeperprotect;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;

@Mod(CreeperProtectMod.MODID)
public class CreeperProtectMod
{
    public static final String MODID = "creeperprotect";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreeperProtectMod()
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint. DisplayTest.class, () -> new IExtensionPoint.DisplayTest(/* Ignore this mod if not present on the client*/ () -> NetworkConstants. IGNORESERVERONLY, /*If present on the client, accept any version if from a server*/(remoteVersion, isFromServer) -> isFromServer));

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Initialized CreeperProtect");
    }
}