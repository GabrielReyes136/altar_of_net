package com.altar_of_net;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface altar_of_net_config extends Config
{
	@ConfigItem(
		keyName = "This plug-in can only be used if you are part of the clan: Altar of NET",
		name = "This plug-in can only be used if you are part of the clan: Altar of NET",
		description = "This plug-in can only be used if you are part of the clan: Altar of NET"
	)
	default String greeting()
	{
		return "This plug-in can only be used if you are part of the clan: Altar of NET";
	}
}
