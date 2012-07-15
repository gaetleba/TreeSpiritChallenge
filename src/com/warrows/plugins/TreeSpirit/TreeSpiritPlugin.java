package com.warrows.plugins.TreeSpirit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.warrows.plugins.TreeSpirit.listeners.PlayerServerEventListener;
import com.warrows.plugins.TreeSpirit.listeners.PlayerMoveListener;
import com.warrows.plugins.TreeSpirit.listeners.PlayerWoodListener;
import com.warrows.plugins.TreeSpirit.listeners.TreeListener;
import com.warrows.plugins.TreeSpirit.trees.TreesData;
import com.warrows.plugins.TreeSpirit.util.Text;

/**
 * Loads Plugin and manages Data/Permissions
 * 
 * @author Warrows
 * 
 */
public class TreeSpiritPlugin extends JavaPlugin
{
	public static Logger			log;
	private static File				directory;
	private static Server			server;
	private File					configFile;
	private static Configuration	config;
	private static File				treesDirectory;

	@Override
	public void onEnable()
	{
		/* get basis and warn about loading */
		server = this.getServer();
		log = this.getLogger();
		log.info("TreeSpirit loading");

		directory = getDataFolder();
		if (!directory.exists())
			directory.mkdir();

		/* get config */
		configFile = new File(directory + File.separator + "config.yml");
		if (!configFile.exists())
		{
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
		config = getConfig();

		/* load listeners */
		getServer().getPluginManager().registerEvents(new PlayerWoodListener(),
				this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(),
				this);
		getServer().getPluginManager().registerEvents(new TreeListener(), this);
		getServer().getPluginManager().registerEvents(
				new PlayerServerEventListener(), this);

		/* register commands */
		Commands commands = new Commands();
		getCommand("treespirit").setExecutor(commands);

		/* load language */
		Text.load((String) config.get("language"));

		/* load datas from files */
		try
		{
			File newPlayersFile = new File(directory, "newPlayers.list");
			TreesData.loadNewPlayers(newPlayersFile);
			treesDirectory = new File(directory, "trees");

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		/* getPermissions */

		log.info("TreeSpirit enabled");
	}

	public static Configuration getConfigInstance()
	{
		return config;
	}

	@Override
	public void onDisable()
	{
		try
		{
			File treesDir = new File(directory, "trees");
			if (!treesDir.exists())
				treesDir.createNewFile();
			for (Player p : server.getOnlinePlayers())
				TreesData.saveGreatTree(p);
			File newPlayersFile = new File(directory, "newPlayers.list");
			TreesData.saveNewPlayers(newPlayersFile);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		saveConfig();
		log.info("TreeSpirit disabled");
	}

	public static File getDirectory()
	{
		return directory;
	}

	public static Server getServerInstance()
	{
		return server;
	}

	public static File getTreesDirectory()
	{
		return treesDirectory;
	}
}