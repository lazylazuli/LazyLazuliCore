package com.github.lazylazuli.lib.common;

import com.github.lazylazuli.lib.common.block.BlockDyed;
import com.github.lazylazuli.lib.common.registry.BlockRegistry;
import com.github.lazylazuli.lib.common.registry.CreativeTabRegistry;
import com.github.lazylazuli.lib.common.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy implements Proxy
{
	public CommonProxy()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public final void registerBlocks(RegistryEvent.Register<Block> event)
	{
		if (this instanceof BlockRegistry)
		{
			event.getRegistry()
				 .registerAll(((BlockRegistry) this).getBlocksForRegistry());
		}
	}
	
	@SubscribeEvent
	public final void registerItems(RegistryEvent.Register<Item> event)
	{
		if (this instanceof ItemRegistry)
		{
			event.getRegistry()
				 .registerAll(((ItemRegistry) this).getItemsForRegistry());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void setModelResource(Block block)
	{
		ResourceLocation resLoc = block.getRegistryName();
		if (resLoc != null)
		{
			ModelLoader.setCustomModelResourceLocation(
					Item.getItemFromBlock(block),
					0,
					new ModelResourceLocation(resLoc, "inventory")
			);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void setModelResource(Item item)
	{
		ResourceLocation resLoc = item.getRegistryName();
		if (resLoc != null)
		{
			ModelLoader.setCustomModelResourceLocation(
					item,
					0,
					new ModelResourceLocation(resLoc, "inventory")
			);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void setModelResource(BlockDyed block)
	{
		Item item = Item.getItemFromBlock(block);
		ResourceLocation resLoc = block.getRegistryName();
		
		if (resLoc != null)
		{
			for (int i = 0; i < 16; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(resLoc, "inventory"));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerDyedColorHandler(Block... blocks)
	{
		BlockColors blockColors = Minecraft.getMinecraft()
										   .getBlockColors();
		
		ItemColors itemColors = Minecraft.getMinecraft()
										 .getItemColors();
		
		blockColors.registerBlockColorHandler((s, w, p, t) -> s.getValue(BlockDyed.COLOR)
															   .getMapColor().colorValue, blocks);
		
		itemColors.registerItemColorHandler(
				(stack, tintIndex) ->
				{
					Block block = ((ItemBlock) stack.getItem()).getBlock();
					IBlockState iblockstate = block.getStateFromMeta(stack.getMetadata());
					
					return blockColors.colorMultiplier(iblockstate, null, null, tintIndex);
				}, blocks
		);
	}
	
	private void registerCreativeTabs()
	{
		if (this instanceof CreativeTabRegistry)
		{
			CreativeTabRegistry registry = (CreativeTabRegistry) this;
			for (CreativeTabs tab : registry.getCreativeTabsForRegistry())
			{
				for (Block block : registry.getBlocksForTab(tab))
				{
					block.setCreativeTab(tab);
				}
				
				for (Item item : registry.getItemsForTab(tab))
				{
					item.setCreativeTab(tab);
				}
			}
		}
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
	
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		registerCreativeTabs();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
	
	}
	
	@Override
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
	
	}
	
	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{
	
	}
	
	@Override
	public void serverStarted(FMLServerStartedEvent event)
	{
	
	}
	
	@Override
	public void serverStopping(FMLServerStoppingEvent event)
	{
	
	}
	
	@Override
	public void serverStopped(FMLServerStoppedEvent event)
	{
	
	}
}
