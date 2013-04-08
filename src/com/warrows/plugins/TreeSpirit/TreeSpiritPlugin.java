package com.warrows.plugins.TreeSpirit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import com.warrows.plugins.TreeSpirit.listeners.*;

/**
 * Loads Plugin and manages Data/Permissions
 * 
 * @author Warrows
 * 
 */
public class TreeSpiritPlugin extends JavaPlugin
{
	public static Logger log;
	private static File directory;
	private static Server server;
	private static File treesDirectory;

	@Override
	public void onEnable()
	{
		doBasisLoading();
		loadListeners();
		addRecipes();

		log.info("TreeSpirit enabled");
	}

	private void addRecipes()
	{
		Dye d = new Dye();
		d.setColor(DyeColor.WHITE);
		ShapedRecipe recipe = new ShapedRecipe(new ItemStack(d.toItemStack()));
		recipe.shape("A");
		recipe.setIngredient('A', Material.LEAVES);
		getServerInstance().addRecipe(recipe);

		for (int i = 0; i < 4; i++)
		{
			recipe = new ShapedRecipe(new ItemStack(Material.SAPLING, 1,
					(short) i));
			recipe.shape("AA","AA");
			recipe.setIngredient('A', new MaterialData(Material.LEAVES,
					(byte) i));
			getServerInstance().addRecipe(recipe);
		}
	}

	private void loadListeners()
	{
		getServer().getPluginManager().registerEvents(new StartListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(),
				this);
		getServer().getPluginManager().registerEvents(new BlockBreakListener(),
				this);
	}

	private void doBasisLoading()
	{
		server = this.getServer();
		log = this.getLogger();
		log.info("TreeSpirit loading");

		directory = getDataFolder();
		if (!directory.exists())
			directory.mkdir();
	}

	@Override
	public void onDisable()
	{
		try
		{
			File treesDir = new File(directory, "trees");
			if (!treesDir.exists())
				treesDir.createNewFile();
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