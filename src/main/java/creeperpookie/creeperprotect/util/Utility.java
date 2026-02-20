package creeperpookie.creeperprotect.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Utility
{
	/**
	 * Checks if an ItemStack is empty, and is effectively air.
	 *
	 * @param item the ItemStack to check
	 * @return true if the ItemStack is null, air, or has an amount of 0 or less; false otherwise
	 */
	public static boolean isItemEmpty(ItemStack item)
	{
		return item == null || item.getType() == Material.AIR || item.getAmount() <= 0;
	}

	/**
	 * Gets a random music disc from the drop table of creepers
	 * TODO: Migrate from a hardcoded list to a tag-based solution
	 *
	 * @param random Random instance to use for selecting a music disc
	 * @return a random music disc Material from the creeper drop table
	 */
	public static Material getRandomCreeperDisc(Random random)
	{
		return switch (random.nextInt(12))
		{
			case 0 -> Material.MUSIC_DISC_11;
			case 1 -> Material.MUSIC_DISC_13;
			case 2 -> Material.MUSIC_DISC_CAT;
			case 3 -> Material.MUSIC_DISC_BLOCKS;
			case 4 -> Material.MUSIC_DISC_CHIRP;
			case 5 -> Material.MUSIC_DISC_FAR;
			case 6 -> Material.MUSIC_DISC_MALL;
			case 7 -> Material.MUSIC_DISC_MELLOHI;
			case 8 -> Material.MUSIC_DISC_STAL;
			case 9 -> Material.MUSIC_DISC_STRAD;
			case 10 -> Material.MUSIC_DISC_WAIT;
			default -> Material.MUSIC_DISC_WARD;
		};
	}
}
