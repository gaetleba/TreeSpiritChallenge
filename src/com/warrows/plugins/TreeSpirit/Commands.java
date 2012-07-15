package com.warrows.plugins.TreeSpirit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.warrows.plugins.TreeSpirit.trees.GreatTree;
import com.warrows.plugins.TreeSpirit.trees.GreatTreeCoop;
import com.warrows.plugins.TreeSpirit.trees.TreesData;
import com.warrows.plugins.TreeSpirit.trees.components.SBlock;
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
			if ("debug".equals(args[0]))
				return debug((Player) sender);
			if ("stop".equals(args[0]))
				return stop((Player) sender);
			if ("info".equals(args[0]))
				return info((Player) sender);
			if ("join".equals(args[0]))
				if (args.length < 2)
					return false;
				else
					return join((Player) sender, args[1]);
			if ("leave".equals(args[0]))
				return leave((Player) sender);
		}
		return false;
	}

	private boolean start(Player player)
	{
		boolean result = false;
		if (!TreesData.hasStarted(player))
		{
			TreesData.start(player);
			result = true;
		}
		if (result)
			player.sendMessage(Text.getMessage("starting"));
		else
			player.sendMessage(Text.getMessage("cannot-start"));
		return result;
	}

	private boolean debug(Player sender)
	{
		GreatTree tree = TreesData.getGreatTree(sender);
		for (SBlock sb : tree.getBody())
		{
			Block item = sb.getBukkitBlock();
			if (item.getType() != Material.LOG
					&& item.getType() != Material.SAPLING
					&& item.getType() != Material.LEAVES
					&& item.getType() != Material.GLOWSTONE)
				item.setType(Material.GLASS);
		}
		return true;
	}

	private boolean stop(Player player)
	{
		if (TreesData.isNew(player))
		{
			TreesData.removeNew(player);
			player.getInventory().clear();
			return true;
		}
		if (TreesData.hasStarted(player))
			return TreesData.destroy(TreesData.getGreatTree(player));
		return false;
	}

	private boolean info(Player player)
	{
		if (TreesData.hasStarted(player))
		{
			GreatTree tree = TreesData.getGreatTree(player);
			if (tree != null)
			{
				player.sendMessage(tree.toString());
				return true;
			}
		}
		player.sendMessage(Text.getMessage("no-tree"));
		return true;
	}

	private boolean join(Player player, String playerToJoin)
	{
		GreatTree tree = TreesData.getGreatTree(player);
		if (null != tree)
			return false;
		if (!"joining".equals(TreeSpiritPlugin.getConfigInstance().getString(
				"co-op-type")))
			return false;
		tree = TreesData.getGreatTree(playerToJoin);
		if (null == tree)
			return false;
		if (!(tree instanceof GreatTreeCoop))
			tree = tree.update();
		((GreatTreeCoop) tree).add(player);
		player.teleport(tree.getTop());
		return true;
	}

	private boolean leave(Player player)
	{
		if ("joining".equals(TreeSpiritPlugin.getConfigInstance().getString(
				"co-op-type"))
				|| "logs".equals(TreeSpiritPlugin.getConfigInstance()
						.getString("co-op-type")))
		{
			GreatTreeCoop tree = (GreatTreeCoop) TreesData.getGreatTree(player);
			if (tree == null)
				return false;
			if (tree.getPlayerName().equals(player.getName()))
				return false;
			tree.remove(player);
			return true;
		}
		return false;
	}
}
