package com.warrows.plugins.TreeSpirit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
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
		Block item = event.getBlock();
		if (item.getType() != Material.LOG
				&& item.getType() != Material.SAPLING
				&& item.getType() != Material.LEAVES)
			return;

		Player player = event.getPlayer();
		GreatTree tree = GreatTree.getGreatTree(player);

		if (tree == null && item.getType() == Material.LOG)
		{
			if (item.getRelative(BlockFace.DOWN).getType() == Material.DIRT
					|| item.getRelative(BlockFace.DOWN).getType() == Material.GRASS)
				createTree(player, item);
			else
				event.setCancelled(true);
			return;
		}

		if (tree.isAdjacent(item))
			tree.addToBody(item);
		else
			event.setCancelled(true);
	}

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

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBlockBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = GreatTree.getGreatTree(block);

		if (tree == null)
		{
			if (TreeSpiritPlugin.getConfigInstance().getBoolean(
					"force-to-play-as-a-tree"))
			{
				event.setCancelled(true);
				event.getPlayer().sendMessage(Text.getMessage("not-a-tree"));
			}
			return;
		}

		if (block.getType() != Material.LOG
				&& block.getType() != Material.SAPLING
				&& block.getType() != Material.LEAVES
				&& block.getType() != Material.GLOWSTONE)
			return;

		destroyBlock(tree, block, event);
	}

	private void destroyBlock(GreatTree tree, Block block, BlockBreakEvent event)
	{
		tree.removeFromBody(block);
		if (block.getType() == Material.LEAVES
				&& event.getPlayer().getItemInHand().getType()
						.equals(Material.SHEARS))
		{
			ItemStack item = new ItemStack(block.getType(), 1, block.getData());
			item = block.getWorld()
					.dropItemNaturally(block.getLocation(), item)
					.getItemStack();
			tree.addDrop(item);
			block.setType(Material.AIR);
			event.setCancelled(true);
		}
		for (ItemStack item : block.getDrops())
			tree.addDrop(item);
	}
}
