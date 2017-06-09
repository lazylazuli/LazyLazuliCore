package com.github.lazylazuli.lazylazulilib.registry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface CreativeTabRegistry
{
	CreativeTabs[] getCreativeTabsForRegistry();
	
	Block[] getBlocksForTab(CreativeTabs tab);
	
	Item[] getItemsForTab(CreativeTabs tab);
	
	static CreativeTabs create(String name, Block block)
	{
		return create(name, new ItemStack(block));
	}
	
	static CreativeTabs create(String name, Item item)
	{
		return create(name, new ItemStack(item));
	}
	
	static CreativeTabs create(String name, ItemStack stack)
	{
		return new CreativeTabs(name)
		{
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return stack;
			}
		};
	}
}