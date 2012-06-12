package com.warrows.plugins.TreeSpirit.util;

import java.util.HashMap;

public class Text
{
	private static HashMap<String, String>	mappings;

	public Text()
	{
		return;
	}

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
				"You can't do that because you don't belong to a tree.");
		mappings.put("cannot-start", "You belong to a tree yet.");

		return mappings;
	}

	private static HashMap<String, String> frenchMappings()
	{
		HashMap<String, String> mappings = new HashMap<String, String>();

		mappings.put(
				"starting",
				"Choisissez votre bois, un emplacement, plantez votre arbre, et en route pour l'aventure!");
		mappings.put("not-a-tree",
				"Vous ne pouvez pas faire ça car vous n'avez pas d'arbre.");
		mappings.put("cannot-start", "Vous avez déja un arbre.");

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
