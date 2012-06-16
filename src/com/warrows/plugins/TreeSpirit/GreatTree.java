package com.warrows.plugins.TreeSpirit;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.warrows.plugins.TreeSpirit.util.SBlock;
import com.warrows.plugins.TreeSpirit.util.Text;

public class GreatTree implements Serializable
{
	private static final long					serialVersionUID	= -6091386416369849135L;

	private static HashMap<SBlock, GreatTree>	greatTreesByBlock;
	private static HashMap<String, GreatTree>	greatTreesByPlayerName;
	private static HashSet<String>				newPlayersNames;
	private static HashSet<SBlock>				hearts;

	private Stack<ItemStack>					drops;
	private int									score;
	private SBlock								heart;
	private List<SBlock>						body;
	private String								playerName;

	/**
	 * Constructeur
	 * 
	 * @param heart
	 * @param player
	 * @param type
	 */
	public GreatTree(Block heart, Player player, byte type)
	{
		this.drops = new Stack<ItemStack>();
		this.heart = new SBlock(heart);
		this.body = new Stack<SBlock>();
		this.body.add(this.heart);
		this.playerName = player.getName();
		score = 1;
		greatTreesByBlock.put(this.heart, this);
		greatTreesByPlayerName.put(player.getName(), this);
		newPlayersNames.remove(player.getName());
		hearts.add(this.heart);
	}

	/**
	 * Methode permettant de charger des données existantes lors du lancement du
	 * serveur
	 * 
	 * @param greatTrees
	 * @param newPlayers
	 * @param savedHearts
	 */
	public static void initialize(HashSet<GreatTree> greatTrees,
			HashSet<String> newPlayers, HashSet<SBlock> savedHearts)
	{
		greatTreesByBlock = new HashMap<SBlock, GreatTree>();
		greatTreesByPlayerName = new HashMap<String, GreatTree>();
		for (GreatTree tree : greatTrees)
		{
			for (SBlock b : tree.body)
				greatTreesByBlock.put(b, tree);
			greatTreesByPlayerName.put(tree.getPlayerName(), tree);
		}
		newPlayersNames = newPlayers;
		hearts = savedHearts;
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
			tree.drops.clear();
			trees.add(tree);
		}
		return trees;
	}

	public static boolean isHeart(Block block)
	{
		return hearts.contains(new SBlock(block));
	}

	public static HashSet<String> saveNewPlayers()
	{
		return newPlayersNames;
	}

	public static HashSet<SBlock> saveHearts()
	{
		return hearts;
	}

	public static GreatTree getGreatTree(Player player)
	{
		return greatTreesByPlayerName.get(player.getName());
	}

	public static GreatTree getGreatTree(Block block)
	{
		return greatTreesByBlock.get(new SBlock(block));
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

	public static boolean destroy(Block block)
	{
		GreatTree tree = getGreatTree(block);
		if (tree == null)
			return false;
		Set<Block> set = new HashSet<Block>();
		for (SBlock sb : tree.body)
		{
			set.add(sb.getBukkitBlock());
		}
		boolean destroy = TreeSpiritPlugin.getConfigInstance().getBoolean(
				"destroy-when-loose");
		for (Block b : set)
		{
			if (hearts.contains(new SBlock(b)))
			{
				b.setType(Material.OBSIDIAN);
				hearts.remove(new SBlock(b));
			} else
			{
				if (destroy)
					b.breakNaturally();
			}
			tree.removeFromBody(b);
		}
		greatTreesByPlayerName.remove(tree.playerName);
		Player player = TreeSpiritPlugin.getServerInstance().getPlayer(
				tree.playerName);
		player.sendMessage(Text.getMessage("heart-destroyed"));
		player.getInventory().clear();
		player.damage(200);
		return true;
	}

	public void removeFromBody(Block block)
	{
		greatTreesByBlock.remove(new SBlock(block));
		body.remove(new SBlock(block));
		addDrops(block.getDrops());
		score--;
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

	public Block getHeart()
	{
		return heart.getBukkitBlock();
	}

	public String toString()
	{
		return ("Joueur: " + playerName);
	}

	public static void checkBlock(GreatTree tree, Block origin)
	{
		for (BlockFace bf : BlockFace.values())
		{
			HashSet<SBlock> toDestroy = new HashSet<SBlock>();
			SBlock sb = new SBlock(origin.getRelative(bf));
			tree.checkBlock(sb, toDestroy);
			for (SBlock sb1 : toDestroy)
			{
				Block b = sb1.getBukkitBlock();
				for (ItemStack item : b.getDrops())
					tree.addDrop(item);
				b.breakNaturally();
				tree.removeFromBody(b);
			}
		}
	}

	private boolean checkBlock(SBlock origin, HashSet<SBlock> tested)
	{
		for (BlockFace bf : BlockFace.values())
		{
			SBlock sb = new SBlock(origin.getBukkitBlock().getRelative(bf));
			if (!tested.contains(sb) && isInBody(sb.getBukkitBlock()))
			{
				if (heart.equals(sb))
				{
					tested.clear();
					return true;
				}
				tested.add(sb);
				if (checkBlock(sb, tested))
					return true;
			}
		}
		return false;
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
			player.sendMessage(Text.getMessage("not-a-tree"));
			return false;
		}
		return true;
	}
}