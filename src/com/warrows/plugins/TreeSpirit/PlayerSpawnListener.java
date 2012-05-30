package com.warrows.plugins.TreeSpirit;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSpawnListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerFirstJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		File file = new File(TreeSpiritPlugin.world.getName() + "/players/"
				+ player.getName() + ".dat");

		/** Si ce n'est pas le 1er login du joueur, on laisse tomber*/
		if (file.exists())
			return;
		
		/**
		 * Si c'est son premier login, on l'attache à un arbre
		 */

		player.setAllowFlight(true);
		
		Block heart = player.getLocation().getBlock()
				.getRelative(BlockFace.DOWN);
		heart.setType(Material.GLOWSTONE);
		
		TreeSpiritPlugin.addGreatTree(heart,player);
		Byte type = Byte.parseByte( (new Integer((int)(Math.random()*4))).toString());
		player.getInventory().addItem(new ItemStack(Material.LOG,5,(short)0,type));
		player.getInventory().addItem(new ItemStack(Material.SAPLING,2,(short)0,type));
		
		/*Block heart = player.getLocation().getBlock()
				.getRelative(BlockFace.DOWN);
		heart.setType(Material.GLOWSTONE);
		
		TreeSpiritPlugin.addGreatTree(heart,player);
		Byte type = Byte.parseByte( (new Integer((int)(Math.random()*4))).toString());
		player.getInventory().addItem(new ItemStack(Material.LOG,5,(short)0,type));
		player.getInventory().addItem(new ItemStack(Material.SAPLING,2,(short)0,type));*/

	}

}
