package com.github.lazylazuli.lib.common.block;

import com.github.lazylazuli.lib.common.block.state.BlockState;
import com.github.lazylazuli.lib.common.inventory.Stack;
import com.google.common.collect.ImmutableMap;
import mcp.MethodsReturnNonnullByDefault;
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

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockDyed extends BlockBase
{
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	
	public BlockDyed(Material material, String registryName)
	{
		this(material, registryName, registryName);
	}
	
	public BlockDyed(Material material, String registryName, String unlocalizedName)
	{
		super(material, registryName, unlocalizedName);
		setDefaultState(getBlockState().getBaseState()
									   .withProperty(COLOR, EnumDyeColor.WHITE));
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
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(COLOR)
					.getMetadata();
	}
	
	@Override
	protected BlockState createBlockState(ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
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
	protected IProperty<?>[] getProperties()
	{
		return new IProperty[] { COLOR };
	}
}