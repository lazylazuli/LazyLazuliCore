package com.github.lazylazuli.lib.common;

import com.github.lazylazuli.lib.common.block.BlockDyed;
import com.github.lazylazuli.lib.common.registry.BlockRegistry;
import com.github.lazylazuli.lib.common.registry.CreativeTabRegistry;
import com.github.lazylazuli.lib.common.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

public class CommonProxy implements Proxy
{
	private final Logger log;
	
	public CommonProxy(LazyLazuliMod mod)
	{
		this.log = mod.getLogger();
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public final void registerBlocks(RegistryEvent.Register<Block> event)
	{
		if (this instanceof BlockRegistry)
		{
			Block[] blocks = ((BlockRegistry) this).getBlocksForRegistry();
			
			log.info(String.format("Registering %s blocks", blocks.length));
			
			for (Block block : blocks)
			{
				log.debug("\t" + block.getUnlocalizedName().substring(5));
				
				event.getRegistry().register(block);
			}
		}
	}
	
	@SubscribeEvent
	public final void registerItems(RegistryEvent.Register<Item> event)
	{
		if (this instanceof ItemRegistry)
		{
			Item[] items = ((ItemRegistry) this).getItemsForRegistry();
			
			log.info(String.format("Registering %s items", items.length));
			
			for (Item item : ((ItemRegistry) this).getItemsForRegistry())
			{
				log.debug("\t" + item.getUnlocalizedName().substring(5));
				
				event.getRegistry().register(item);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void setModelResourceFor(Block... blocks)
	{
		for (Block block : blocks)
		{
			log.debug(String.format("\t%s", block));
			
			ResourceLocation resLoc = block.getRegistryName();
			
			if (resLoc == null)
			{
				String name = block.getUnlocalizedName().substring(5);
				log.warn(String.format("\tNo resource location found for: %s. Skipping model resource creation...",
						name
				));
			} else
			{
				Item item = Item.getItemFromBlock(block);
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(resLoc, "inventory"));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void setModelResourceFor(Item... items)
	{
		for (Item item : items)
		{
			ResourceLocation resLoc = item.getRegistryName();
			if (resLoc == null)
			{
				String name = item.getUnlocalizedName().substring(5);
				log.warn(String.format("\tNo resource location found for: %s. Skipping model resource creation...",
						name
				));
			} else
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(resLoc, "inventory"));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void setModelResourceFor(BlockDyed... blocks)
	{
		for (BlockDyed block : blocks)
		{
			Item item = Item.getItemFromBlock(block);
			ResourceLocation resLoc = block.getRegistryName();
			
			if (resLoc == null)
			{
				String name = item.getUnlocalizedName().substring(5);
				log.warn(String.format("\tNo resource location found for: %s. Skipping model resource creation...",
						name
				));
			} else
			{
				for (int i = 0; i < 16; i++)
				{
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(resLoc,
							"inventory"));
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerDyedColorHandlerFor(Block... blocks)
	{
		BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		
		blockColors.registerBlockColorHandler(
				(s, w, p, t) -> MapColor.getBlockColor(s.getValue(BlockDyed.COLOR)).colorValue,
				blocks
		);
		
		itemColors.registerItemColorHandler((stack, tintIndex) ->
		{
			Block block = ((ItemBlock) stack.getItem()).getBlock();
			IBlockState iblockstate = block.getStateFromMeta(stack.getMetadata());
			
			return blockColors.colorMultiplier(iblockstate, null, null, tintIndex);
		}, blocks);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerGrassColorHandlerFor(Block... blocks)
	{
		BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) ->
		{
			if (worldIn != null && pos != null)
			{
				return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
			} else
			{
				return ColorizerGrass.getGrassColor(0.5D, 1.0D);
			}
		}, blocks);
		
		itemColors.registerItemColorHandler((stack, tintIndex) ->
		{
			IBlockState iblockstate = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
			return blockColors.colorMultiplier(iblockstate, null, null, tintIndex);
		}, blocks);
	}
	
	//
	
	private void registerCreativeTabs()
	{
		for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY)
		{
			for (Block block : getBlocksForTab(tab))
			{
				block.setCreativeTab(tab);
			}
			
			for (Item item : getItemsForTab(tab))
			{
				item.setCreativeTab(tab);
			}
		}
		
		if (this instanceof CreativeTabRegistry)
		{
			CreativeTabRegistry registry = (CreativeTabRegistry) this;
			for (CreativeTabs tab : registry.getCreativeTabsForRegistry())
			{
				for (Block block : getBlocksForTab(tab))
				{
					block.setCreativeTab(tab);
				}
				
				for (Item item : getItemsForTab(tab))
				{
					item.setCreativeTab(tab);
				}
			}
		}
	}
	
	protected Block[] getBlocksForTab(CreativeTabs tab)
	{
		return new Block[0];
	}
	
	protected Item[] getItemsForTab(CreativeTabs tab)
	{
		return new Item[0];
	}
	
	//
	
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
