package creeperpookie.creeperprotect;

import creeperpookie.creeperprotect.commands.CreeperProtectCommand;
import creeperpookie.creeperprotect.handlers.CreeperHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class CreeperProtectPlugin extends JavaPlugin
{
	private static CreeperProtectPlugin instance;
	private static final Random RANDOM = new Random();

	@Override
	public void onEnable()
	{
		instance = this;
		Bukkit.getPluginManager().registerEvents(new CreeperHandler(), this);
		loadConfig();
		getCommand("creeperprotect").setExecutor(new CreeperProtectCommand());
		Bukkit.getLogger().info("Enabled CreeperProtect");
	}

	@Override
	public void onDisable()
	{
		getCommand("creeperprotect").unregister(Bukkit.getCommandMap());
		Bukkit.getLogger().info("Disabled CreeperProtect");
	}

	public void loadConfig()
	{
		saveDefaultConfig();
		reloadConfig();
		CreeperHandler.setDamageAttackers(getConfig().getBoolean("damage-creeper-attackers", false));
		CreeperHandler.setCreeperTargetingDisabled(getConfig().getBoolean("disable-creeper-targeting", true));
		CreeperHandler.setClickableCreepersStatus(getConfig().getBoolean("clickable-creepers", true), Math.abs(getConfig().getInt("creeper-click-timeout", 6000)), Math.min(Math.abs(getConfig().getInt("click-gunpowder-drop", 64)), 1024), getConfig().getBoolean("shift-clickable-creepers", true));
	}

	public static CreeperProtectPlugin getInstance()
	{
		return instance;
	}

	public static Random getRandom()
	{
		return RANDOM;
	}
}
