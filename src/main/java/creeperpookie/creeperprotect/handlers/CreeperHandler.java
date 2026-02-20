package creeperpookie.creeperprotect.handlers;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import creeperpookie.creeperprotect.util.DefaultTextColor;
import creeperpookie.creeperprotect.util.Utility;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CreeperHandler implements Listener
{
	private static final Random random = new Random();
	private static final HashMap<Player, Long> lastClickTimes = new HashMap<>();
	private static boolean damageAttackers = false;
	private static boolean creeperTargetingDisabled = true;
	private static boolean dropStatus = false;
	private static int minClickDelay = 6000;
	private static int dropCount = 64;
	private static boolean shiftDropStatus = false;

	public static void setDamageAttackers(boolean damageAttackers)
	{
		CreeperHandler.damageAttackers = damageAttackers;
	}

	public static boolean isCreeperTargetingDisabled()
	{
		return creeperTargetingDisabled;
	}

	public static void setCreeperTargetingDisabled(boolean enabled)
	{
		creeperTargetingDisabled = enabled;
	}

	public static void setClickableCreepersStatus(boolean dropEnabled, int minDelay, int dropCount, boolean shiftDiscStatus)
	{
		dropStatus = dropEnabled;
		minClickDelay = minDelay;
		CreeperHandler.dropCount = dropCount;
		shiftDropStatus = shiftDiscStatus;
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
			// Instant "creeper bomb" prevention
			if (creeper.getFuseTicks() < 20) creeper.setFuseTicks(20);
			creeper.setInvulnerable(true);
		}
	}

	@EventHandler
	public void onEntityDespawn(EntityRemoveFromWorldEvent event)
	{
		if (event.getEntity() instanceof Creeper creeper && creeper.getChunk().isLoaded())
		{
			// Disabled due to instability with respawning despawned creepers
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
			if (damageAttackers && event.getDamager() instanceof LivingEntity livingAttacker) livingAttacker.damage(event.getDamage(), event.getEntity());
		}
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
		if (event.getRightClicked() instanceof Creeper creeper)
		{
			if (Utility.isItemEmpty(heldItem) && dropStatus)
			{
				if (minClickDelay > 0 && System.currentTimeMillis() - lastClickTimes.getOrDefault(event.getPlayer(), 0L) <= minClickDelay / 20.0 * 1000)
				{
					event.getPlayer().sendActionBar(Component.text("You've clicked a creeper too recently!", DefaultTextColor.GOLD));
					return;
				}
				else if (minClickDelay > 0) lastClickTimes.put(event.getPlayer(), System.currentTimeMillis());
				ArrayList<ItemStack> items = new ArrayList<>();
				int remaining = dropCount;
				while (remaining > 0)
				{
					Material material = shiftDropStatus && event.getPlayer().isSneaking() ? Utility.getRandomCreeperDisc(random) : Material.GUNPOWDER;
					int count = Math.min(remaining, material.getMaxStackSize());
					items.add(new ItemStack(material, count));
					if (material.getMaxStackSize() == 1) break;
					remaining -= count;
				}
				while (!items.isEmpty())
				{
					Item item = creeper.getWorld().dropItemNaturally(creeper.getLocation(), items.remove(0));
					item.setVelocity(item.getVelocity().multiply(new Vector(1, 2.5, 1)));
				}
			}
			else if (!Utility.isItemEmpty(heldItem) && (heldItem.getType() == Material.FLINT_AND_STEEL || heldItem.getType() == Material.FIRE_CHARGE)) event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityTarget(EntityTargetLivingEntityEvent event)
	{
		if (event.getEntity() instanceof Creeper && isCreeperTargetingDisabled()) event.setCancelled(true);
	}
}

