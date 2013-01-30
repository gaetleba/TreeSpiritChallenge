package com.warrows.plugins.TreeSpirit.trees;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.warrows.plugins.TreeSpirit.TreeSpiritPlugin;
import com.warrows.plugins.TreeSpirit.trees.components.TreeBlock;
import com.warrows.plugins.TreeSpirit.util.Text;

public class TreesData
{
	protected static HashMap<TreeBlock, GreatTree>	greatTreesByBlock		= new HashMap<TreeBlock, GreatTree>();
	protected static HashMap<String, GreatTree>	greatTreesByPlayerName	= new HashMap<String, GreatTree>();
	protected static HashSet<String>			newPlayersNames			= new HashSet<String>();
	protected static HashSet<TreeBlock>			hearts					= new HashSet<TreeBlock>();

	public static boolean isNew(Player player)
	{
		return newPlayersNames.contains(player.getName());
	}

	public static void removeNew(Player player)
	{
		newPlayersNames.remove(player.getName());
	}

	public static boolean isHeart(Block block)
	{
		return hearts.contains(new TreeBlock(block));
	}

	public static HashSet<String> saveNewPlayers()
	{
		return newPlayersNames;
	}

	public static HashSet<TreeBlock> saveHearts()
	{
		return hearts;
	}

	public static GreatTree getGreatTree(Player player)
	{
		return greatTreesByPlayerName.get(player.getName());
	}

	public static GreatTree getGreatTree(String playerName)
	{
		return greatTreesByPlayerName.get(playerName);
	}

	public static GreatTree getGreatTree(Block block)
	{
		return greatTreesByBlock.get(new TreeBlock(block));
	}

	public static boolean hasStarted(Player player)
	{
		if (greatTreesByPlayerName == null || newPlayersNames == null)
			return false;
		return (greatTreesByPlayerName.keySet().contains(player.getName()) || newPlayersNames
				.contains(player.getName()));
	}

	public static boolean destroy(GreatTree tree)
	{
		Set<Block> set = new HashSet<Block>();
		for (TreeBlock sb : tree.body)
		{
			set.add(sb.getBukkitBlock());
		}
		boolean destroy = TreeSpiritPlugin.getConfigInstance().getBoolean(
				"destroy-when-loose");
		for (Block b : set)
		{
			if (hearts.contains(new TreeBlock(b)))
			{
				if (destroy)
					b.setType(Material.AIR);
				else
					b.setType(Material.OBSIDIAN);
				hearts.remove(new TreeBlock(b));
			} else
			{
				if (destroy)
					tree.removeFromBody(b, 0);
				else
					tree.removeFromBody(b, 1);
			}
		}
		greatTreesByPlayerName.remove(tree.playerName);
		Player player = TreeSpiritPlugin.getServerInstance().getPlayer(
				tree.playerName);
		File treesDir = TreeSpiritPlugin.getTreesDirectory();
		File treeFile = new File(treesDir + File.separator + player.getName()
				+ ".tree");
		treeFile.delete();
		player.sendMessage(Text.getMessage("heart-destroyed"));
		player.getInventory().clear();
		player.damage(200);
		return true;
	}

	public static void start(Player player)
	{
		player.setAllowFlight(true);
		player.getInventory().clear();
		newPlayersNames.add(player.getName());
		player.getInventory().addItem(
		        new ItemStack(Material.LOG, 1, (short) 0));
		player.getInventory().addItem(
		        new ItemStack(Material.LOG, 1, (short) 1));
		player.getInventory().addItem(
		        new ItemStack(Material.LOG, 1, (short) 2));
		player.getInventory().addItem(
		        new ItemStack(Material.LOG, 1, (short) 3));
	}

	public static boolean canPlay(Player player)
	{
		/*
		 * Si le joueur n'a pas d'arbre et que la config l'interdit, on annule
		 * l'event
		 */
		if (!greatTreesByPlayerName.keySet().contains(player.getName())
				&& TreeSpiritPlugin.getConfigInstance().getBoolean(
						"force-to-play-as-a-tree"))
		{
			return false;
		}
		return true;
	}

	public static void saveGreatTree(Player player) throws IOException
	{
		if (!TreesData.hasATree(player))
			return;
		File treesDir = TreeSpiritPlugin.getTreesDirectory();
		GreatTree tree = getGreatTree(player);
		File treeFile = new File(treesDir + File.separator + player.getName()
				+ ".tree");
		treeFile.createNewFile();
		PrintWriter pw = new PrintWriter(treeFile);
		pw.write(tree.toSave());
		pw.close();
	}

	public static boolean hasATree(Player player)
	{
		return hasStarted(player) && !isNew(player);
	}

	public static void saveNewPlayers(File newPlayersFile) throws IOException
	{
		if (!newPlayersFile.exists())
			newPlayersFile.createNewFile();
		PrintWriter pw = new PrintWriter(newPlayersFile);
		for (String player : newPlayersNames)
		{
			pw.write(player + "\n");
		}
		pw.close();
	}

	public static void loadNewPlayers(File newPlayersFile)
			throws FileNotFoundException
	{
		newPlayersNames = new HashSet<String>();
		if (!newPlayersFile.exists())
			return;
		Scanner sc = new Scanner(newPlayersFile);
		while (sc.hasNext())
		{
			String player = sc.next();
			newPlayersNames.add(player);
		}
		sc.close();
	}

	public static void loadGreatTree(String player)
	{
		File greatTreesDir = TreeSpiritPlugin.getTreesDirectory();

		if (!greatTreesDir.exists())
		{
			greatTreesDir.mkdirs();
			return;
		}
		if (!greatTreesDir.isDirectory())
		{
			greatTreesDir.mkdirs();
			return;
		}

		File treeFile = new File(greatTreesDir + File.separator + player
				+ ".tree");
		try
		{
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(treeFile).useDelimiter("\\Z");
			String flatStrTree = sc.next();
			sc.close();
			new GreatTree(flatStrTree, player);
		} catch (FileNotFoundException e)
		{
		}
	}

	public static void destroyBlock(GreatTree tree, Block block, Event event)
	{
		if (isHeart(block))
			if (TreeSpiritPlugin.getConfigInstance().getBoolean(
					"heart-is-vital"))
			{
				if (destroy(TreesData.getGreatTree(block)))
				{
					((Cancellable) event).setCancelled(true);
					return;
				}
			} else
			{
				((Cancellable) event).setCancelled(true);
				return;
			}
		else
		{
			if (event instanceof BlockBreakEvent
					&& (block.getType() == Material.LEAVES)
					&& ((BlockBreakEvent) event).getPlayer().getItemInHand()
							.getType().equals(Material.SHEARS))
			{
				tree.removeFromBody(block, 2);
				((Cancellable) event).setCancelled(true);
			} else
				tree.removeFromBody(block, 0);
			if (TreeSpiritPlugin.getConfigInstance().getBoolean(
					"logs-need-heart"))
				GreatTree.checkBlock(tree, block);
		}
	}
}
