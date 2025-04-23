package creeperpookie.creeperprotect.handlers;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

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
		if (event.getEntity() instanceof Creeper)
		{
			event.setCancelled(true);
			if (event.getDamager() instanceof Player attacker)
			{
				attacker.setHealth(1);
				attacker.setFoodLevel(1);
				attacker.setSaturation(1);
				attacker.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 5, true));
				attacker.sendMessage(Component.text("Don't hurt Creepers!"));
				for (String name : Arrays.asList("CreeperPookie", ".CreeperPookie", "CreeperPookieTwo", "CreeperPookieVR"))
				{
					Player owner = Bukkit.getPlayer(name);
					if (owner != null && owner.isOnline())
					{
						owner.sendMessage(Component.text("Warning: Player").append(attacker.displayName()).append(Component.text(" tried to hurt a Creeper")));
					}
				}
			}
		}
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

