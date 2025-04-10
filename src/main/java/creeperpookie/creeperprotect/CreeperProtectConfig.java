package creeperpookie.creeperprotect;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
@Config(modid = CreeperProtectMod.MODID)
public class CreeperProtectConfig
{
	@Config.Comment("Enables toggling if Creepers should target the player to explode")
	@Config.Name("Enable Creeper entity targeting")
	public static boolean enableTargeting = true;

	@SubscribeEvent
	public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(CreeperProtectMod.MODID))
		{
			ConfigManager.sync(CreeperProtectMod.MODID, Config.Type.INSTANCE);
		}
	}
}
