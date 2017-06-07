package com.github.lazylazuli.lazylazulilib;

import com.github.lazylazuli.lazylazulilib.block.BlockDyed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public final class Registry
{
	private final String modId;
	
	private final Logger log;
	
	public Registry(String modId)
	{
		this.modId = modId;
		this.log = LogManager.getLogger(modId);
	}
	
	public CreativeTabs newCreativeTab(String label, ItemStack icon)
	{
		return new CreativeTabs(label)
		{
			@Nonnull
			@Override
			public ItemStack getTabIconItem()
			{
				return icon;
			}
		};
	}
	
	public void registerBlocks(FMLPreInitializationEvent event, Block... blocks)
	{
		log.info("Registering Blocks...");
		
		for (Block block : blocks)
		{
			GameRegistry.register(block);
			GameRegistry.register(new ItemBlock(block), block.getRegistryName());
			
			String name = block.getUnlocalizedName()
							   .substring(5);
			
			log.info("\tBlock:" + name);
			
			if (event.getSide() == Side.CLIENT)
			{
				registerCustomModelLocation(block);
			}
		}
	}
	
	public void registerItems(FMLPreInitializationEvent event, Item... items)
	{
		log.info("Registering Items...");
		
		for (Item item : items)
		{
			String name;
			
			if (item instanceof ItemBlock)
			{
				Block block = ((ItemBlock) item).getBlock();
				
				GameRegistry.register(block);
				GameRegistry.register(item, block.getRegistryName());
				
				name = block.getUnlocalizedName()
							.substring(5);
				
				log.info("\tItemBlock: " + name);
				
			} else
			{
				GameRegistry.register(item);
				
				name = item.getUnlocalizedName()
						   .substring(5);
				
				log.info("\tItem: " + name);
			}
			
			if (event.getSide() == Side.CLIENT)
			{
				registerCustomModelLocation(item);
			}
		}
	}
	
	public void registerDyedBlocks(FMLPreInitializationEvent event, Block... blocks)
	{
		log.info("Registering dyed Blocks...");
		log.info("Creating ItemBlocks for each...");
		
		ItemBlock[] itemBlocks = new ItemBlock[blocks.length];
		
		for (int i = 0; i < blocks.length; i++)
		{
			itemBlocks[i] = new ItemBlock(blocks[i]);
		}
		
		registerItems(event, itemBlocks);
	}
	
	public void registerDyedItems(FMLPreInitializationEvent event, Item... items)
	{
		String name;
		
		log.info("Registering dyed Items...");
		
		for (Item item : items)
		{
			if (item instanceof ItemBlock)
			{
				Block block = ((ItemBlock) item).getBlock();
				GameRegistry.register(block);
				GameRegistry.register(item, block.getRegistryName());
				
				name = block.getUnlocalizedName()
							.substring(5);
				
				log.info("\tItemBlock: " + name);
			} else
			{
				GameRegistry.register(item);
				
				name = item.getUnlocalizedName()
						   .substring(5);
				
				log.info("\tItem: " + name);
			}
			
			if (event.getSide() == Side.CLIENT)
			{
				for (int i = 0; i < 16; i++)
				{
					name = modId + ":" + name;
					ModelResourceLocation resourceLocation = new ModelResourceLocation(name, "inventory");
					ModelLoader.setCustomModelResourceLocation(item, i, resourceLocation);
				}
			}
		}
		
		
	}
	
	private void registerCustomModelLocation(Block block)
	{
		ResourceLocation resourceLocation = block.getRegistryName();
		
		if (resourceLocation != null)
		{
			registerCustomModelLocation(Item.getItemFromBlock(block), resourceLocation);
		}
	}
	
	private void registerCustomModelLocation(Item item)
	{
		ResourceLocation resourceLocation = item.getRegistryName();
		
		if (resourceLocation != null)
		{
			registerCustomModelLocation(item, resourceLocation);
		}
	}
	
	private void registerCustomModelLocation(Item item, ResourceLocation resourceLocation)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(resourceLocation, "inventory"));
	}
	
	public void registerDyedBlockColorHandler(Block... blocks)
	{
		BlockColors blockColors = Minecraft.getMinecraft()
										   .getBlockColors();
		
		blockColors.registerBlockColorHandler((s, w, p, t) -> s.getValue(BlockDyed.COLOR)
															   .getMapColor().colorValue, blocks);
	}
	
	public void registerDyedItemColorHandler(Block... blocks)
	{
		ItemColors itemColors = Minecraft.getMinecraft()
										 .getItemColors();
		BlockColors blockColors = Minecraft.getMinecraft()
										   .getBlockColors();
		
		itemColors.registerItemColorHandler(
				(stack, tintIndex) ->
				{
					Block block = ((ItemBlock) stack.getItem()).getBlock();
					IBlockState iblockstate = block.getStateFromMeta(stack.getMetadata());
					
					return blockColors.colorMultiplier(iblockstate, null, null, tintIndex);
				}, blocks
		);
	}
}
