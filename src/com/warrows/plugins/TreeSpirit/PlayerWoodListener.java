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

		if (item.getType() != Material.LOG
				&& event.getItem().getItemStack().getType() != Material.SAPLING
				&& item.getType() != Material.LEAVES)
		{
			return;
		}

		TreeSpiritPlugin.log.info("joueur "+player+" arbre "+tree+" item "+item+" est un drop: "+tree.hasDrop(item));
		
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
		/*
		 * Si le joueur n'a pas d'arbre et que la config l'interdit, on annule
		 * l'event
		 */
		if (!GreatTree.hasStarted(event.getPlayer())
				&& TreeSpiritPlugin.getConfigInstance().getBoolean(
						"force-to-play-as-a-tree"))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(Text.getMessage("not-a-tree"));
			return;
		}

		Block item = event.getBlock();
		Player player = event.getPlayer();
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

		/* On agrandis l'arbre ou on annule */
		if (tree.isAdjacent(item))
			tree.addToBody(item);
		else
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBlockBreak(BlockBreakEvent event)
	{
		/*
		 * Si le joueur n'a pas d'arbre et que la config l'interdit, on annule
		 * l'event
		 */
		if (!GreatTree.hasStarted(event.getPlayer())
				&& TreeSpiritPlugin.getConfigInstance().getBoolean(
						"force-to-play-as-a-tree"))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(Text.getMessage("not-a-tree"));
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
		TreeSpiritPlugin.log.info("test");
		if (TreeSpiritPlugin.getConfigInstance().getBoolean("heart-is-vital"))
			if (destroyTree(block))
			{
				((Cancellable) event).setCancelled(true);
				return;
			}
		tree.removeFromBody(block);
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
