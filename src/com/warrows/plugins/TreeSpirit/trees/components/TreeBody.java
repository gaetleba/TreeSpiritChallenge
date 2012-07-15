package com.warrows.plugins.TreeSpirit.trees.components;

import java.util.HashSet;

@SuppressWarnings("hiding")
public class TreeBody<SBlock> extends HashSet<SBlock>
{
	private static final long	serialVersionUID	= 565226848281174079L;

	public TreeBody()
	{
		super();
	}

	@SuppressWarnings("unchecked")
	public String toString()
	{
		String s = "";
		s += "TreeBody: {\n";
		for (Object sb : this.toArray())
		{
			s += "\t";
			sb = (SBlock) sb;
			s += sb.toString() + ",";
			s += "\n";
		}
		s = s.substring(0, s.length() - 1);
		s += "}";
		return s;
	}
	
	public int sizeOf()
	{
		return this.size();
	}
}
