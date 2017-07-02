package com.github.lazylazuli.lib.common.mod;

import com.github.lazylazuli.lib.common.block.BlockDyed;
import com.github.lazylazuli.lib.common.inventory.Stack;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

public abstract class Proxy
{
	protected final Logger log;
	
	public Proxy()
	{
		log = getMod().getLogger();
	}
	
	protected abstract LazyLazuliMod getMod();
	
	protected Block[] getBlocksForRegistry()
	{
		return new Block[0];
	}
	
	protected Item[] getItemsForRegistry()
	{
		return new Item[0];
	}
	
	protected CreativeTabs[] getCreativeTabsForRegistry()
	{
		return new CreativeTabs[0];
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
	
	final void registerBlocks(RegistryEvent.Register<Block> event)
	{
		Block[] blocks = getBlocksForRegistry();
		
		if (blocks.length == 0)
		{
			return;
		}
		
		log.info(String.format("Registering %s blocks", blocks.length));
		
		for (Block block : blocks)
		{
			log.debug("\t" + block.getRegistryName());
			
			event.getRegistry().register(block);
		}
	}
	
	final void registerItems(RegistryEvent.Register<Item> event)
	{
		Item[] items = getItemsForRegistry();
		
		if (items.length == 0)
		{
			return;
		}
		
		log.info(String.format("Registering %s items", items.length));
		
		for (Item item : getItemsForRegistry())
		{
			log.debug("\t" + item.getRegistryName());
			
			event.getRegistry().register(item);
		}
	}
	
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
		
		for (CreativeTabs tab : getCreativeTabsForRegistry())
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
	
	//
	
	@SideOnly(Side.CLIENT)
	protected void setModelResourceFor(Block... blocks)
	{
		log.info("Setting model resources for blocks");
		
		Item[] items = new Item[blocks.length];
		
		for (int i = 0; i < blocks.length; i++)
		{
			items[i] = Item.getItemFromBlock(blocks[i]);
		}
		
		setModelResources(items);
	}
	
	@SideOnly(Side.CLIENT)
	protected void setModelResourceFor(Item... items)
	{
		log.info("Setting model resources for items");
		
		setModelResources(items);
	}
	
	@SideOnly(Side.CLIENT)
	private void setModelResources(Item... items)
	{
		for (Item item : items)
		{
			ResourceLocation resLoc = item.getRegistryName();
			if (resLoc == null)
			{
				String name = item.getUnlocalizedName().substring(5);
				log.warn(String.format("\t%s has no registry name. Skipping...", name));
				continue;
			}
			
			boolean b = item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockDyed;
			
			setModelResourceFor(item, b);
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected final void setModelResourceFor(Item item, boolean registerMetadata)
	{
		ResourceLocation resLoc = item.getRegistryName();
		if (resLoc == null)
		{
			String name = item.getUnlocalizedName().substring(5);
			log.warn(String.format("\t%s has no registry name. Skipping...", name));
		}
		
		ModelResourceLocation mrl = new ModelResourceLocation(resLoc, "inventory");
		
		if (registerMetadata)
		{
			for (int i = 0; i < 16; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, mrl);
			}
		} else
		{
			ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
		}
	}
	
	//
	
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
	
	public void preInit(FMLPreInitializationEvent event) {}
	
	public void init(FMLInitializationEvent event)
	{
		registerCreativeTabs();
	}
	
	public void postInit(FMLPostInitializationEvent event) {}
	
	public void serverAboutToStart(FMLServerAboutToStartEvent event) {}
	
	public void serverStarting(FMLServerStartingEvent event) {}
	
	public void serverStarted(FMLServerStartedEvent event) {}
	
	public void serverStopping(FMLServerStoppingEvent event) {}
	
	public void serverStopped(FMLServerStoppedEvent event) {}
	
	//
	
	protected static CreativeTabs newCreativeTab(String name, Block block)
	{
		return newCreativeTab(name, Stack.of(block));
	}
	
	protected static CreativeTabs newCreativeTab(String name, Item item)
	{
		return newCreativeTab(name, Stack.of(item));
	}
	
	protected static CreativeTabs newCreativeTab(String name, ItemStack stack)
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
