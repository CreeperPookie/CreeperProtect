package creeperpookie.creeperprotect.util;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class Utility
{
	public static String formatText(String message)
	{
		StringBuilder newMessage = new StringBuilder();
		String[] itemTypeSegments = message.split("_");
		for (int i = 0; i < itemTypeSegments.length; i++)
		{
			if (itemTypeSegments[i].equalsIgnoreCase("to") || itemTypeSegments[i].equalsIgnoreCase("and") || itemTypeSegments[i].equalsIgnoreCase("or") || itemTypeSegments[i].equalsIgnoreCase("a"))
			{
				itemTypeSegments[i] = itemTypeSegments[i].toLowerCase();
			}
			else
			{
				itemTypeSegments[i] = itemTypeSegments[i].substring(0, 1).toUpperCase() + itemTypeSegments[i].substring(1).toLowerCase();
			}
			newMessage.append(itemTypeSegments[i]);
			if (i < itemTypeSegments.length - 1)
			{
				newMessage.append(" ");
			}
		}
		return newMessage.toString();
	}

	public static <T> boolean arrayContains(T[] array, T value)
	{
		for (T arrayValue : array)
		{
			if (array instanceof String[] && value instanceof String && ((String) arrayValue).equalsIgnoreCase((String) value))
			{
				return true;
			}
			else if (arrayValue.equals(value))
			{
				return true;
			}
		}
		return false;
	}

	public static String locationAsString(BlockPos location)
	{
		return location.getX() + " " + location.getY() + " " + location.getZ();
	}

	public static BlockPos stringAsLocation(String location) // valid formats: "x, y, z"; "x,y,z"; "x y z" |
	{
		if (location == null) return null;
		String[] type1 = location.split(", ");
		if (type1.length != 3)
		{
			String[] type2 = location.split(",");
			if (type2.length != 3)
			{
				String[] type3 = location.split(" ");
				if (type3.length != 3)
				{
					return null;
				}
				return parseBlockPos(type3);
			}
			else
			{
				return parseBlockPos(type2);
			}
		}
		else
		{
			return parseBlockPos(type1);
		}
	}

	@Nullable
	private static BlockPos parseBlockPos(String[] positions)
	{
		if (positions.length != 3) return null;
		int x, y, z;
		try
		{
			x = Integer.parseInt(positions[0]);
			y = Integer.parseInt(positions[1]);
			z = Integer.parseInt(positions[2]);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		return new BlockPos(x, y, z);
	}
}
