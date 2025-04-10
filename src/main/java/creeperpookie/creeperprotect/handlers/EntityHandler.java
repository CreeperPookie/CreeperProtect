package creeperpookie.creeperprotect.handlers;

import creeperpookie.creeperprotect.CreeperProtectConfig;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EntityHandler
{
	@SubscribeEvent
	public static void onEntityTarget(LivingSetAttackTargetEvent event)
	{
		if (CreeperProtectConfig.enableTargeting && event.getEntityLiving() instanceof EntityCreeper && event.getTarget() != null)
		{
			EntityCreeper creeper = (EntityCreeper) event.getEntityLiving();
			creeper.setAttackTarget(null);
		}
	}

	@SubscribeEvent
	public static void onEntityDamage(LivingDamageEvent event)
	{
		if (event.getEntityLiving() instanceof EntityCreeper)
		{
			event.setCanceled(true);
			event.setAmount(0);
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event)
	{
		if (event.getEntityLiving() instanceof EntityCreeper)
		{
			event.setCanceled(true);
		}
	}
}
