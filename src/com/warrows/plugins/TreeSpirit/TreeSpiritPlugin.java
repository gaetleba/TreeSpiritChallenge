package com.warrows.plugins.TreeSpirit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import com.warrows.plugins.TreeSpirit.util.SBlock;
import com.warrows.plugins.TreeSpirit.util.Text;

/**
 * Loads Plugin and manages Data/Permissions
 * 
 * @author Warrows
 * 
 */
public class TreeSpiritPlugin extends JavaPlugin
{
	protected static Logger			log;
	private static File				directory;
	private static Server			server;
	private File					configFile;
	private static Configuration	config;

	@SuppressWarnings("unchecked")
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
		getServer().getPluginManager().registerEvents(new TreeLife(), this);
		getServer().getPluginManager().registerEvents(new PlayerHardEventListener(), this);

		/* register commands */
		Commands commands = new Commands();
		getCommand("treespirit").setExecutor(commands);

		/* load language */
		Text.load((String) config.get("language"));

		/* load datas from files */
		try
		{
			File treesFile = new File(directory, "trees.obj");
			File newPlayersFile = new File(directory, "newPlayers.obj");
			File heartsFile = new File(directory, "hearts.obj");
			HashSet<GreatTree> trees;
			HashSet<String> newPlayers;
			HashSet<SBlock> hearts;

			if (treesFile.exists())
				trees = (HashSet<GreatTree>) new ObjectInputStream(
						new FileInputStream(treesFile)).readObject();
			else
				trees = new HashSet<GreatTree>();

			if (newPlayersFile.exists())
				newPlayers = (HashSet<String>) new ObjectInputStream(
						new FileInputStream(newPlayersFile)).readObject();
			else
				newPlayers = new HashSet<String>();

			if (heartsFile.exists())
				hearts = (HashSet<SBlock>) new ObjectInputStream(
						new FileInputStream(heartsFile)).readObject();
			else
				hearts = new HashSet<SBlock>();

			GreatTree.initialize((HashSet<GreatTree>) trees,
					(HashSet<String>) newPlayers, hearts);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

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
			(new ObjectOutputStream(new FileOutputStream(new File(directory,
					"trees.obj")))).writeObject(GreatTree.saveTrees());
			(new ObjectOutputStream(new FileOutputStream(new File(directory,
					"newPlayers.obj"))))
					.writeObject(GreatTree.saveNewPlayers());
			(new ObjectOutputStream(new FileOutputStream(new File(directory,
					"hearts.obj")))).writeObject(GreatTree.saveHearts());
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
}