package com.warrows.plugins.TreeSpirit.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.warrows.plugins.TreeSpirit.CoOpLogsHandler;
import com.warrows.plugins.TreeSpirit.TreeSpiritPlugin;
import com.warrows.plugins.TreeSpirit.trees.GreatTree;
import com.warrows.plugins.TreeSpirit.trees.GreatTreeCoop;
import com.warrows.plugins.TreeSpirit.trees.TreesData;
import com.warrows.plugins.TreeSpirit.util.Text;

public class PlayerWoodListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupEvent(PlayerPickupItemEvent event)
	{
		Player player = event.getPlayer();
		if (!TreesData.canPlay(player))
		{
			player.sendMessage(Text.getMessage("not-a-tree"));
			event.setCancelled(true);
			return;
		}
		GreatTree tree = TreesData.getGreatTree(player);

		if (tree == null)
			return;

		ItemStack item = event.getItem().getItemStack();

		if (!TreesData.canPlay(player))
		{
			player.sendMessage(Text.getMessage("not-a-tree"));
			event.setCancelled(true);
			return;
		}

		if (item.getType() != Material.LOG
				&& event.getItem().getItemStack().getType() != Material.SAPLING
				&& item.getType() != Material.LEAVES)
		{
			return;
		}
		if (!tree.hasDrop(item))
		{
			event.getItem().remove();
			event.setCancelled(true);
		} else
		{
			tree.takeDrop(item);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropEvent(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();
		if (!TreesData.canPlay(player))
		{
			player.sendMessage(Text.getMessage("not-a-tree"));
			event.setCancelled(true);
			return;
		}
		ItemStack item = event.getItemDrop().getItemStack();
		if (item.getType() != Material.LOG
				&& item.getType() != Material.SAPLING
				&& item.getType() != Material.LEAVES)
		{
			return;
		}

		GreatTree tree = TreesData.getGreatTree(player);

		tree.addDrop(item);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBlockPlace(BlockPlaceEvent event)
	{
		/* si le joueur n'as pas le droit, on l'empeche */
		Player player = event.getPlayer();
		Block item = event.getBlock();
		if (!TreesData.canPlay(player))
		{
			/* a moins qu'il essyae de créer un arbre. */
			if (TreesData.isNew(player))
			{
				if (item.getType() == Material.LOG)
				{
					if ((item.getRelative(BlockFace.DOWN).getType() == Material.DIRT || item
							.getRelative(BlockFace.DOWN).getType() == Material.GRASS))
					{
						createTree(player, item);
						return;
					} else
					{
						player.sendMessage(Text.getMessage("tree-need-dirt"));
						event.setCancelled(true);
						return;
					}
				} else
				{
					player.sendMessage(Text.getMessage("only-logs"));
					event.setCancelled(true);
					return;
				}
			} else
			{
				player.sendMessage(Text.getMessage("not-a-tree"));
				event.setCancelled(true);
				return;
			}
		}
		GreatTree tree = TreesData.getGreatTree(player);

		/* si le materiel n'est pas vivant, on s'en moque */
		if (item.getType() != Material.LOG
				&& item.getType() != Material.SAPLING
				&& item.getType() != Material.LEAVES)
		{
			/* verification pour ne pas bloquer le coeur. */
			if (tree.getHeart().getLocation().getBlockX() == item.getLocation()
					.getBlockX()
					&& tree.getHeart().getLocation().getBlockZ() == item
							.getLocation().getBlockZ())
			{
				player.sendMessage(Text.getMessage("not-close-heart"));
				event.setCancelled(true);
			}
			return;
		}

		/* On agrandis l'arbre ou on annule */
		if (tree.isAdjacent(item))
			tree.addToBody(item);
		else
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		if (!TreesData.canPlay(player))
		{
			player.sendMessage(Text.getMessage("not-a-tree"));
			event.setCancelled(true);
			return;
		}

		Block block = event.getBlock();
		GreatTree tree = TreesData.getGreatTree(block);

		/*
		 * Si l'arbre n'est pas au destructeur et que le pvp est interdit, on
		 * annule
		 */
		if (!TreeSpiritPlugin.getConfigInstance().getBoolean("pvp-enabled"))
		{
			if (tree != null && !tree.ownedBy(player))
			{
				event.setCancelled(true);
				event.getPlayer().sendMessage(Text.getMessage("pvp-disabled"));
				return;
			}
		}

		/* on detruit juste le bloc concerné */
		if (tree != null)
			TreesData.destroyBlock(tree, block, event);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerReSpawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		if (TreesData.hasATree(player))
		{
			GreatTree tree = TreesData.getGreatTree(player);
			event.setRespawnLocation(tree.getTop());
			player.setAllowFlight(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		if (TreesData.hasATree(player))
		{
			GreatTree tree = TreesData.getGreatTree(player);
			Inventory inv = player.getInventory();
			for (ItemStack is : inv)
			{
				if (is != null)
				{
					if (is.getType() == Material.LOG
							|| is.getType() == Material.SAPLING
							|| is.getType() == Material.LEAVES)
						tree.addDrop(is);
				}
			}
		}
	}

	/*
	 * 
	 * SUIVENT LES METHODES UTILITAIRES
	 */

	private void createTree(Player player, Block heart)
	{
		byte type = heart.getData();
		if ("logs".equals(TreeSpiritPlugin.getConfigInstance().getString(
				"co-op-type")))
		{
			GreatTreeCoop coopTree = CoOpLogsHandler.getTreeByType(type);
			if (coopTree != null)
			{
				// joining a co-op tree
				player.getInventory().clear();
				player.getInventory().addItem(
						new ItemStack(Material.LOG, 3, type));
				player.getInventory().addItem(
						new ItemStack(Material.SAPLING, 1, type));
				coopTree.add(player);
				player.teleport(coopTree.getTop());
				player.sendMessage(Text.getMessage("co-op-log-new-player"));
				return;
			}
		}
		createClassicTree(player, heart, type);
	}

	private void createClassicTree(Player player, Block heart, byte type)
	{
		// starting a tree
		player.getInventory().clear();
		player.getInventory().addItem(new ItemStack(Material.LOG, 5, type));
		player.getInventory().addItem(new ItemStack(Material.SAPLING, 2, type));
		GreatTree tree;
		if ("logs".equals(TreeSpiritPlugin.getConfigInstance().getString(
				"co-op-type")))
			tree = new GreatTreeCoop(heart, player, type);
		else
			tree = new GreatTree(heart, player.getName(), type);
		player.teleport(tree.getTop());
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(true);
		player.sendMessage(Text.getMessage("start"));
	}
}
