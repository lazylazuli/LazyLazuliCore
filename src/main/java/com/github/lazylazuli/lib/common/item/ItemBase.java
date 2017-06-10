package com.github.lazylazuli.lib.common.item;

import net.minecraft.item.Item;

public class ItemBase extends Item
{
	public ItemBase(String registryName)
	{
		this(registryName, registryName);
	}
	
	public ItemBase(String registryName, String unlocalizedName)
	{
		setRegistryName(registryName);
		setUnlocalizedName(unlocalizedName);
	}
}
