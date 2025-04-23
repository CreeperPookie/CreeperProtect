package creeperpookie.creeperprotect.commands;

import creeperpookie.creeperprotect.CreeperProtectPlugin;
import creeperpookie.creeperprotect.util.DefaultTextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreeperProtectCommand implements CommandExecutor, TabCompleter
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
	{
		if (!sender.isOp() && !sender.hasPermission("creeperprotect.command")) sender.sendMessage(Component.text("You do not have permission to use this command.", DefaultTextColor.RED).decoration(TextDecoration.ITALIC, false));
		else if (args.length != 1) printHelp(sender, command, label, args);
		else if (args[0].equalsIgnoreCase("reload"))
		{
			CreeperProtectPlugin.getInstance().loadConfig();
			sender.sendMessage(Component.text("Reloaded CreeperProtect configuration", DefaultTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
		}
		else printHelp(sender, command, label, args);
		return true;
	}

	@Override
	@Nullable
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
	{
		if (!sender.isOp() && !sender.hasPermission("creeperprotect.command"))
		{
			return List.of();
		}
		else if (args.length == 1) return List.of("reload");
		else return List.of();
	}

	private void printHelp(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
	{
		if (!sender.isOp() && !sender.hasPermission("creeperprotect.command")) sender.sendMessage(Component.text("You do not have permission to use this command.", DefaultTextColor.RED).decoration(TextDecoration.ITALIC, false));
		else sender.sendMessage(Component.text("Usage: /" + label + " reload - Reload the plugin configuration", DefaultTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
	}
}
