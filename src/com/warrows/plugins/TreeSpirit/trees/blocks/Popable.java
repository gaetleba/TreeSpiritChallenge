package com.warrows.plugins.TreeSpirit.trees.blocks;

public interface Popable
{
	public void pop();

	public void removeParent(Solid parent);
	public void addParent(Solid parent);
}
