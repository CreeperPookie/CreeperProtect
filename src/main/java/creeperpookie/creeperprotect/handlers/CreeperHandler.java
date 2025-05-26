package creeperpookie.creeperprotect.handlers;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class CreeperHandler implements Listener
{
	private static boolean creeperTargetingDisabled = true;

	public static boolean isCreeperTargetingDisabled()
	{
		return creeperTargetingDisabled;
	}

	public static void setCreeperTargetingDisabled(boolean enabled)
	{
		creeperTargetingDisabled = enabled;
	}

	@EventHandler
	public void onServerTick(ServerTickEndEvent event)
	{
		Bukkit.getWorlds().forEach(world -> world.getEntitiesByClass(Creeper.class).forEach(creeper ->
		{
			creeper.setInvulnerable(true);
			creeper.setFireTicks(0);
		}));
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event)
	{
		if (event.getEntity() instanceof Creeper creeper)
		{
			if (creeper.getFuseTicks() < 20) creeper.setFuseTicks(20);
			creeper.setInvulnerable(true);
		}
	}

	@EventHandler
	public void onEntityDespawn(EntityRemoveFromWorldEvent event)
	{
		if (event.getEntity() instanceof Creeper creeper && creeper.getChunk().isLoaded())
		{
			// creeper.getWorld().spawnEntity(creeper.getLocation(), EntityType.CREEPER);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Creeper) event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if (event.getEntity() instanceof Creeper) event.setCancelled(true);
	}

	@EventHandler
	public void onEntityApplyEffect(EntityPotionEffectEvent event)
	{
		if (event.getEntity() instanceof Creeper && (event.getAction() == EntityPotionEffectEvent.Action.ADDED || event.getAction() == EntityPotionEffectEvent.Action.CHANGED) && (event.getNewEffect() != null && event.getNewEffect().getType() == PotionEffectType.HARM || event.getNewEffect().getType() == PotionEffectType.HEAL) && (event.getNewEffect().getType() == PotionEffectType.HARM ? 6 : 4) << event.getNewEffect().getAmplifier() < 0) event.setCancelled(true);
	}

	@EventHandler
	public void onHeal(EntityRegainHealthEvent event)
	{
		if (event.getEntity() instanceof Creeper && event.getAmount() < 0) event.setCancelled(true);
	}

	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent event)
	{
		ItemStack heldItem = event.getPlayer().getInventory().getItem(event.getHand());
		if (event.getRightClicked() instanceof Creeper && heldItem.getType() == Material.FLINT_AND_STEEL || heldItem.getType() == Material.FIRE_CHARGE)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityTarget(EntityTargetLivingEntityEvent event)
	{
		if (event.getEntity() instanceof Creeper && event.getTarget() instanceof Player && !isCreeperTargetingDisabled()) event.setCancelled(true);
	}
}

