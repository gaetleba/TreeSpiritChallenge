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
		
		return mappings;
	}

	public static String getMessage(String key)
	{
		String string;
		if ((string = mappings.get(key)) != null)
			return string;
		if ((string = mappings.get("text-not-found")) != null)
			return string;
		return "text not found";
	}
}
