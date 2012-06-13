package com.warrows.plugins.TreeSpirit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.warrows.plugins.TreeSpirit.util.Text;

public class Commands implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args)
	{
		if (!"treespirit".equals(command.getName()) || args[0] == null)
			return false;
		if (sender instanceof Player)
		{
			if ("start".equals(args[0]))
				return start((Player) sender);
			if ("stop".equals(args[0]))
				return stop((Player) sender);
		}
		return false;
	}

	private boolean start(Player player)
	{
		boolean result = false;
		if (!GreatTree.hasStarted(player))
		{
			GreatTree.start(player);
			result = true;
		}
		if (result)
			player.sendMessage(Text.getMessage("starting"));
		else
			player.sendMessage(Text.getMessage("cannot-start"));
		return result;
	}

	private boolean stop(Player player)
	{
		return GreatTree.destroy(GreatTree.getGreatTree(player).getHeart());
	}
}
