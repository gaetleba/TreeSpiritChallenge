package com.warrows.plugins.TreeSpirit.util;

public class Text
{
	public static String getMessage(String key)
	{
		if ("starting".equals(key))
			return "Choisissez votre bois, un emplacement, plantez votre arbre, et en route pour l'aventure!";
		return "text not found";
	}
}
