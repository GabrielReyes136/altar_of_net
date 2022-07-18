package com.altar_of_net;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class altar_of_net_plugin_test
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(altar_of_net_plugin.class);
		RuneLite.main(args);
	}
}