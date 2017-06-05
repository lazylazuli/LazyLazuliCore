package com.github.lazylazuli.lazylazulilib;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemColored;
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
			public Item getTabIconItem()
			{
				return icon.getItem();
			}
		};
	}
	
	public void registerItems(FMLPreInitializationEvent event, Item... items)
	{
		log.info("Registering items...");
		
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
				
				log.info("\t" + modId + ":" + name);
				
				if (item instanceof ItemColored)
				{
					log.debug("\t...setting colored model resources");
					
					for (int i = 0; i < 16; i++)
					{
						StringBuilder sb = new StringBuilder();
						sb.append(modId)
						  .append(":")
						  .append(name)
						  .append("_")
						  .append(EnumDyeColor.byMetadata(i)
											  .getUnlocalizedName());
						
						log.debug("\t\t" + sb.toString());
						
						ModelResourceLocation resourceLocation = new ModelResourceLocation(sb.toString(), "inventory");
						
						if (event.getSide() == Side.CLIENT)
						{
							ModelLoader.setCustomModelResourceLocation(item, i, resourceLocation);
						}
					}
				}
			} else
			{
				GameRegistry.register(item);
				
				name = item.getUnlocalizedName()
						   .substring(5);
				
				log.info("\t" + modId + ":" + name);
				
				if (event.getSide() == Side.CLIENT)
				{
					registerCustomModelLocation(item);
				}
			}
		}
	}
	
	public void registerBlocks(FMLPreInitializationEvent event, Block... blocks)
	{
		log.info("Registering blocks...");
		
		for (Block block : blocks)
		{
			GameRegistry.register(block);
			GameRegistry.register(new ItemBlock(block), block.getRegistryName());
			
			String name = block.getUnlocalizedName()
							   .substring(5);
			
			log.info("\t" + modId + ":" + name);
			
			if (event.getSide() == Side.CLIENT)
			{
				registerCustomModelLocation(block);
			}
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
	
	private void registerCustomModelLocation(Block block)
	{
		ResourceLocation resourceLocation = block.getRegistryName();
		
		if (resourceLocation != null)
		{
			registerCustomModelLocation(Item.getItemFromBlock(block), resourceLocation);
		}
	}
	
	private void registerCustomModelLocation(@Nonnull Item item, @Nonnull ResourceLocation resourceLocation)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(resourceLocation, "inventory"));
	}
}
