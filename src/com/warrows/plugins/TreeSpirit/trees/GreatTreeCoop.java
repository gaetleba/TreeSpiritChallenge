package com.warrows.plugins.TreeSpirit.trees;

import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.warrows.plugins.TreeSpirit.CoOpLogsHandler;
import com.warrows.plugins.TreeSpirit.TreeSpiritPlugin;

public class GreatTreeCoop extends GreatTree
{
	/**
	 * 
	 */
	private HashSet<String> players;
	
	public GreatTreeCoop(Block heart, Player player, byte type)
	{
		super(heart, player.getName(), type);
		players = new HashSet<String>();
		players.add(player.getName());
		CoOpLogsHandler.addTree(this);
	}
	
	public GreatTreeCoop(GreatTree tree)
	{
		this(tree.getHeart(),TreeSpiritPlugin.getServerInstance().getPlayer(tree.getPlayerName()),tree.getType());
	}
	
	public void add(Player player)
	{
		players.add(player.getName());
		TreesData.greatTreesByPlayerName.put(player.getName(),this);
	}
	
	public void add(String player)
	{
		players.add(player);
		TreesData.greatTreesByPlayerName.put(player,this);
	}
	
	public void remove(Player player)
	{
		players.remove(player.getName());
		TreesData.greatTreesByPlayerName.remove(player.getName());
	}

	@Override
	public boolean ownedBy(Player player)
	{
		for (String p : players)
		{
			if (p.equals(player.getName()))
				return true;
		}
		return false;
	}
	
	public String toString()
	{
		String s = "";
		s += ("Founder: " + getPlayerName() + "\n");
		return s;
	}
}
