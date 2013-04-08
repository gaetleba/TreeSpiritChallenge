package com.warrows.plugins.TreeSpirit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
			// Use a command
		}
		return false;
	}
}
