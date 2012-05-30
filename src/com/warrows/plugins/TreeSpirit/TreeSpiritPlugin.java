package com.warrows.plugins.TreeSpirit;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Loads Plugin and manages Data/Permissions
 * 
 * @author Warrows
 */
public class TreeSpiritPlugin extends JavaPlugin
{
	protected static Logger						log;
	protected static World						world;
	protected static File						directory;
	private static HashMap<Block, GreatTree>	greatTreesByBlock;
	private static HashMap<Player, GreatTree>	greatTreesByPlayer;

	@Override
	public void onEnable()
	{
		log = this.getLogger();
		log.info("TreeSpirit loading");

		directory = getDataFolder();
		if (!directory.exists())
			directory.mkdir();

		world = this.getServer().getWorlds().get(0);

		// Enregistrement des listeners
		getServer().getPluginManager().registerEvents(
				new PlayerSpawnListener(), this);
		getServer().getPluginManager().registerEvents(
				new PlayerWoodListener(), this);
		getServer().getPluginManager().registerEvents(
				new PlayerMoveListener(), this);

		greatTreesByBlock = new HashMap<Block, GreatTree>();
		greatTreesByPlayer = new HashMap<Player, GreatTree>();

		log.info("TreeSpirit enabled");
	}
	
	public static boolean addGreatTree(Block heart, Player player)
	{
		GreatTree greatTree = new GreatTree(heart, player);
		if (heart.getType()!=Material.GLOWSTONE)
			return false;
		greatTreesByBlock.put(heart,greatTree);
		greatTreesByPlayer.put(player,greatTree);
		return true;
	}
	
	public static GreatTree getGreatTree(Player player)
	{
		return greatTreesByPlayer.get(player);
	}

	@Override
	public void onDisable()
	{
		log.info("TreeSpirit disabled");
	}
}