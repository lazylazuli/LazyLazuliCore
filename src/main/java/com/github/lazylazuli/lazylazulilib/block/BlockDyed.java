package com.github.lazylazuli.lazylazulilib.block;

import com.github.lazylazuli.lazylazulilib.Stack;
import com.github.lazylazuli.lazylazulilib.block.state.BlockState;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.material.MapColor;
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

public interface BlockDyed extends IBlock
{
	PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	
	default IBlockState getDefaultState()
	{
		return getBlockState().getBaseState()
							  .withProperty(COLOR, EnumDyeColor.WHITE);
	}
	
	@Override
	default int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	default void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (EnumDyeColor dye : EnumDyeColor.values())
		{
			list.add(Stack.ofMeta(itemIn, dye.getMetadata()));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	default IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}
	
	@Override
	default int getMetaFromState(IBlockState state)
	{
		return state.getValue(COLOR)
					.getMetadata();
	}
	
	@Override
	default BlockState createBlockState(ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
	{
		return new BlockState(getBlock(), propertiesIn)
		{
			@Override
			public MapColor getMapColor()
			{
				return getValue(COLOR).getMapColor();
			}
		};
	}
	
	@Override
	default IProperty<?>[] getProperties()
	{
		return new IProperty[] { COLOR };
	}
}