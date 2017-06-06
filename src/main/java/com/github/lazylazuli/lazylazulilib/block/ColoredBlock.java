package com.github.lazylazuli.lazylazulilib.block;

import com.github.lazylazuli.lazylazulilib.Stack;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Default state must be set manually
 * <p>
 * <code>
 * setDefaultState(blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
 * </code>
 */
public class ColoredBlock extends BlockBase
{
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	
	public ColoredBlock(Material material)
	{
		super(material);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (EnumDyeColor dye : EnumDyeColor.values())
		{
			list.add(Stack.ofMeta(itemIn, dye.getMetadata()));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}
	
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(COLOR)
					.getMetadata();
	}
	
	@Override
	public BlockState createBlockState(ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
	{
		return new BlockState(this, propertiesIn)
		{
			@Override
			public MapColor getMapColor()
			{
				return getValue(COLOR).getMapColor();
			}
		};
	}
	
	@Override
	public IProperty<?>[] getProperties()
	{
		return new IProperty[] { COLOR };
	}
}