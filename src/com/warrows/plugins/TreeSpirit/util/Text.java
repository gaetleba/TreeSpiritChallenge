package com.warrows.plugins.TreeSpirit.util;

import java.util.HashMap;

public class Text
{
	private static HashMap<String, String>	mappings;

	public Text()
	{
		return;
	}
	
	//TODO not-close-heart

	public static void load(String language)
	{
		if ("french".equals(language))
		{
			mappings = frenchMappings();
			return;
		}
		mappings = englishMappings();
	}

	private static HashMap<String, String> englishMappings()
	{
		HashMap<String, String> mappings = new HashMap<String, String>();

		mappings.put(
				"starting",
				"Choose your wood, pick a nice place, plant your tree, and let's go to adventure!");
		mappings.put("not-a-tree",
				"You can't do that because you don't belong to a tree. /ts start to get one.");
		mappings.put("cannot-start", "You belong to a tree yet.");
		mappings.put("pvp-disabled", "You can't hurt another people's tree.");
		mappings.put("heart-destroyed", "Your tree's heart has been destroyed.");
		mappings.put("not-close-heart", "No dead material where the sap have to pass.");
		mappings.put("start", "Here you go. Build your tree with those new materials.");
		mappings.put("no-tree", "You do not belong to a tree.");
		mappings.put("only-logs", "You can only place a log in order to start your tree.");
		mappings.put("tree-need-dirt", "Your tree have to be on dirt. Be carreful to have enough dirt around to spread your tree.");

		return mappings;
	}

	private static HashMap<String, String> frenchMappings()
	{
		HashMap<String, String> mappings = new HashMap<String, String>();

		mappings.put(
				"starting",
				"Choisissez votre bois, un emplacement, plantez votre arbre, et en route pour l'aventure!");
		mappings.put("not-a-tree",
				"Vous ne pouvez pas faire ça car vous n'avez pas d'arbre. /ts start pour en obtenir un.");
		mappings.put("cannot-start", "Vous avez déja un arbre.");
		mappings.put("pvp-disabled", "Vous ne pouvez pas abimer l'arbre d'un autre joueur.");
		mappings.put("heart-destroyed", "Le coeur de votre arbre a été detruit.");
		mappings.put("not-close-heart", "Pas de matière morte la ou la sève doit passer.");
		mappings.put("start", "C'est parti! Construisez un arbre avec le bois et les plantes que vous avez.");
		mappings.put("no-tree", "Vous n'avez pas d'arbre.");
		mappings.put("only-logs", "Vous pouvez seulement placer une buche pour commencer votre arbre.");
		mappings.put("tree-need-dirt", "Votre arbre a besoin de suffisement de terre pour grandir.");
		
		return mappings;
	}

	public static String getMessage(String key)
	{
		String string;
		if ((string = mappings.get(key)) != null)
			return string;
		if ((string = mappings.get("text-not-found")) != null)
			return string;
		return "text not found for key '"+key+"'";
	}
}
