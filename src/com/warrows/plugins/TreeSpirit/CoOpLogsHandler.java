package com.warrows.plugins.TreeSpirit;

import com.warrows.plugins.TreeSpirit.trees.GreatTree;
import com.warrows.plugins.TreeSpirit.trees.GreatTreeCoop;

public class CoOpLogsHandler
{
	private static GreatTreeCoop[]	trees	= new GreatTreeCoop[4];

	public static boolean addTree(GreatTreeCoop tree)
	{
		if (trees[tree.getType()] == null)
		{
			trees[tree.getType()] = tree;
			return true;
		}
		return false;
	}

	public static GreatTreeCoop getTreeByType(byte type)
	{
		return trees[type];
	}

	public static void remove(GreatTree tree)
	{
		trees[tree.getType()] = null;
	}
}
