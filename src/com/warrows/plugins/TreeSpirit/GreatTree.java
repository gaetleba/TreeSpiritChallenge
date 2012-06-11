package com.warrows.plugins.TreeSpirit;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.warrows.plugins.TreeSpirit.util.SBlock;

public class GreatTree implements Serializable
{
	private static final long					serialVersionUID		= 6368304605618553997L;

	private static HashMap<SBlock, GreatTree>	greatTreesByBlock		= new HashMap<SBlock, GreatTree>();
	private static HashMap<String, GreatTree>	greatTreesByPlayerName	= new HashMap<String, GreatTree>();
	private static HashSet<String>				newPlayersNames			= new HashSet<String>();

	private HashSet<ItemStack>					drops;
	private int									score;
	private SBlock								heart;
	private HashSet<SBlock>						body;
	private String								playerName;

	public static void initialize(HashSet<GreatTree> greatTrees,
			HashSet<String> newPlayers)
	{
		greatTreesByBlock = new HashMap<SBlock, GreatTree>();
		greatTreesByPlayerName = new HashMap<String, GreatTree>();
		for (GreatTree tree : greatTrees)
		{
			for (SBlock b : tree.getBlocks())
				greatTreesByBlock.put(b, tree);
			greatTreesByPlayerName.put(tree.getPlayerName(), tree);
		}
		newPlayersNames = newPlayers;
	}

	public Set<SBlock> getBlocks()
	{
		return body;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public static boolean isNew(Player player)
	{
		return newPlayersNames.contains(player.getName());
	}

	public static HashSet<GreatTree> saveTrees()
	{
		HashSet<GreatTree> trees = new HashSet<GreatTree>();
		for (GreatTree tree : greatTreesByPlayerName.values())
		{
			trees.add(tree);
		}
		return trees;
	}

	public static HashSet<String> saveNewPlayers()
	{
		return newPlayersNames;
	}

	public static GreatTree getGreatTree(Player player)
	{
		return greatTreesByPlayerName.get(player.getName());
	}

	public static GreatTree getGreatTree(Block block)
	{
		for (SBlock sb : greatTreesByBlock.keySet())
			if (new SBlock(block).equals(sb))
				return greatTreesByBlock.get(sb);
		return null;
	}

	public GreatTree(Block heart, Player player, byte type)
	{
		this.drops = new HashSet<ItemStack>();
		this.heart = new SBlock(heart);
		this.body = new HashSet<SBlock>();
		this.body.add(this.heart);
		this.playerName = player.getName();
		score = 1;
		greatTreesByBlock.put(this.heart, this);
		greatTreesByPlayerName.put(player.getName(), this);
		newPlayersNames.remove(player.getName());
	}

	public int getScore()
	{
		return score;
	}

	private double getDistMax()
	{
		return 2;// + Math.log(getScore());
	}

	private boolean isInBody(Block block)
	{
		for (SBlock bodyPart : body)
		{
			if (bodyPart.equals(new SBlock(block)))
				return true;
		}
		return false;
	}

	public Block getHeart()
	{
		return heart.getBukkitBlock();
	}

	public boolean hasDrop(ItemStack item)
	{
		return drops.contains(item);
	}

	public void takeDrop(ItemStack item)
	{
		drops.remove(item);
	}

	public void addDrop(ItemStack item)
	{
		drops.add(item);
	}

	public void addDrops(Collection<ItemStack> items)
	{
		drops.addAll(items);
	}

	public boolean isAdjacent(Block block)
	{
		BlockFace[] directions =
		{ BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST,
				BlockFace.UP, BlockFace.DOWN };
		for (BlockFace direction : directions)
			if (isInBody(block.getRelative(direction)))
				return true;
		return false;
	}

	public boolean isAtProximity(Block block)
	{
		for (double x = -getDistMax() + 1; x < getDistMax(); x += 1)
			for (double y = -getDistMax() + 1; y < getDistMax(); y += 1)
				for (double z = -getDistMax() + 1; z < getDistMax(); z += 1)
				{
					Block b = block.getLocation()
							.add(new Location(block.getWorld(), x, y, z))
							.getBlock();
					if (isInBody(b))
						return true;
				}
		return false;
	}

	public void addToBody(Block block)
	{
		greatTreesByBlock.put(new SBlock(block), this);
		body.add(new SBlock(block));
		score++;
	}

	public void destroy()
	{
		Set<SBlock> a = new HashSet<SBlock>();
		for (SBlock sb : body)
		{
			a.add(sb);
		}
		for (SBlock sb : a)
			removeFromBody(sb.getBukkitBlock());
		greatTreesByPlayerName.remove(playerName);
		TreeSpiritPlugin.getServerInstance().getPlayer(playerName).damage(200);
		heart.getBukkitBlock().setType(Material.OBSIDIAN);
	}

	public void removeFromBody(Block block)
	{
		SBlock a = null;
		SBlock b = null;
		for (SBlock sb : greatTreesByBlock.keySet())
			if (new SBlock(block).equals(sb))
			{
				a = sb;
				continue;
			}
		greatTreesByBlock.remove(a);
		for (SBlock sb : body)
			if (new SBlock(block).equals(sb))
			{
				b = sb;
				continue;
			}
		body.remove(b);
		addDrops(block.getDrops());
		score--;
		if (heart.equals(new SBlock(block)))
			destroy();
	}

	public static boolean hasStarted(Player player)
	{
		return (greatTreesByPlayerName.keySet().contains(player.getName()) || newPlayersNames
				.contains(player.getName()));
	}

	public static void start(Player player)
	{
		player.setAllowFlight(true);
		player.getInventory().clear();
		newPlayersNames.add(player.getName());
		player.getInventory().addItem(
				new ItemStack(Material.LOG, 1, (short) 0, (byte) 0));
		player.getInventory().addItem(
				new ItemStack(Material.LOG, 1, (short) 0, (byte) 1));
		player.getInventory().addItem(
				new ItemStack(Material.LOG, 1, (short) 0, (byte) 2));
		player.getInventory().addItem(
				new ItemStack(Material.LOG, 1, (short) 0, (byte) 3));
	}
}