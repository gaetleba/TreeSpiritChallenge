package com.warrows.plugins.TreeSpirit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.warrows.plugins.TreeSpirit.util.Text;

public class PlayerWoodListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupEvent(PlayerPickupItemEvent event)
	{
		Player player = event.getPlayer();
		GreatTree tree = GreatTree.getGreatTree(player);

		if (tree == null)
			return;

		ItemStack item = event.getItem().getItemStack();

		if (!GreatTree.canPlay(player))
		{
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
		if (!GreatTree.canPlay(event.getPlayer()))
		{
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

		Player player = event.getPlayer();
		GreatTree tree = GreatTree.getGreatTree(player);

		tree.addDrop(item);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Block item = event.getBlock();
		GreatTree tree = GreatTree.getGreatTree(player);

		/* si le materiel n'est pas vivant, on s'en moque */
		if (item.getType() != Material.LOG
				&& item.getType() != Material.SAPLING
				&& item.getType() != Material.LEAVES)
		{
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

		/* si le joueur n'a pas d'arbre, on essaye de commencer */
		if (tree == null && item.getType() == Material.LOG)
		{
			if (GreatTree.isNew(player)
					&& (item.getRelative(BlockFace.DOWN).getType() == Material.DIRT || item
							.getRelative(BlockFace.DOWN).getType() == Material.GRASS))
				createTree(player, item);
			else
				event.setCancelled(true);
			return;
		}

		if (!GreatTree.canPlay(player))
		{
			event.setCancelled(true);
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
		if (!GreatTree.canPlay(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}

		Block block = event.getBlock();
		GreatTree tree = GreatTree.getGreatTree(block);

		/*
		 * Si l'arbre n'est pas au destructeur et que le pvp est interdit, on
		 * annule
		 */
		if (!TreeSpiritPlugin.getConfigInstance().getBoolean("pvp-enabled"))
		{
			if (tree != null
					&& !tree.getPlayerName()
							.equals(event.getPlayer().getName()))
			{
				event.setCancelled(true);
				event.getPlayer().sendMessage(Text.getMessage("pvp-disabled"));
				return;
			}
		}

		/* on detruit juste le bloc concerné */
		if (tree != null)
			destroyBlock(tree, block, event);
	}

	/*
	 * 
	 * SUIVENT LES METHODES UTILITAIRES
	 */

	private void createTree(Player player, Block heart)
	{
		byte type = heart.getData();

		player.getInventory().clear();
		player.getInventory().addItem(new ItemStack(Material.LOG, 5, type));
		player.getInventory().addItem(new ItemStack(Material.SAPLING, 2, type));

		heart.setType(Material.GLOWSTONE);
		new GreatTree(heart, player, type);

		player.teleport(heart.getLocation().add(0, 1, 0));
	}

	protected static void destroyBlock(GreatTree tree, Block block, Event event)
	{
		if (TreeSpiritPlugin.getConfigInstance().getBoolean("heart-is-vital"))
			if (destroyTree(block))
			{
				((Cancellable) event).setCancelled(true);
				return;
			}
		tree.removeFromBody(block);
		if (TreeSpiritPlugin.getConfigInstance().getBoolean("logs-need-heart"))
		GreatTree.checkBlock(tree, block);
		if (event instanceof BlockBreakEvent)
			if (block.getType() == Material.LEAVES
					&& ((BlockBreakEvent) event).getPlayer().getItemInHand()
							.getType().equals(Material.SHEARS))
			{
				ItemStack item = new ItemStack(block.getType(), 1,
						block.getData());
				item = block.getWorld()
						.dropItemNaturally(block.getLocation(), item)
						.getItemStack();
				tree.addDrop(item);
				block.setType(Material.AIR);
				((Cancellable) event).setCancelled(true);
			}
		for (ItemStack item : block.getDrops())
			tree.addDrop(item);
	}

	private static boolean destroyTree(Block block)
	{
		if (GreatTree.isHeart(block))
			return GreatTree.destroy(block);
		return false;
	}
}
