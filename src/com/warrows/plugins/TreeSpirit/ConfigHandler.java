package com.warrows.plugins.TreeSpirit;

import java.io.File;

public class ConfigHandler
{
	private String language;
	private boolean heartIsVital;
	private boolean forceToPlayAsATree;
	private boolean pvp;
	private boolean destroyDeadTrees;
	private boolean logsNeedHeart;
	private String coOpType;
	
	public ConfigHandler(File configFile)
	{
		
	}
	
	
	/*
	#Language. At the moment: english or french. Without uppercase.
	language: 'english'
	#Determines if the tree have to be destroyed when the heart is damaged
	heart-is-vital: true
	#Determines if players can play without tree or if they are forced to challenge
	force-to-play-as-a-tree: true
	#Determines if players can harm others players trees
	pvp-enabled: false
	#Determines if the tree have to be destroyed or be kept as a monument when the challenge is loose 
	destroy-when-loose: true
	#Determines if a log not plugged to the heart have to break
	logs-need-heart: true

	#Which kind of co-op should we use ? ('none', 'logs' or 'joining')
	co-op-type: 'none'*/
}
