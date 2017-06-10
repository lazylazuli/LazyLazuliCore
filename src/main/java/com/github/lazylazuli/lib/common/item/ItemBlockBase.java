package com.github.lazylazuli.lib.common.item;

import com.github.lazylazuli.lib.common.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class ItemBlockBase extends ItemBlock
{
	public ItemBlockBase(BlockBase block)
	{
		super(block);
		
		ResourceLocation resourceLocation = block.getRegistryName();
		
		if (resourceLocation != null)
		{
			setRegistryName(resourceLocation);
		}
	}
	
	public ItemBlockBase(Block block, String registryName)
	{
		this(block, registryName, registryName);
	}
	
	public ItemBlockBase(Block block, String registryName, String unlocalizedName)
	{
		super(block);
		setRegistryName(registryName);
		setUnlocalizedName(unlocalizedName);
	}
}
