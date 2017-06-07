package com.github.lazylazuli.lazylazulilib.item;

import com.github.lazylazuli.lazylazulilib.block.BlockDyed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class ItemBlockDyed extends ItemBlock
{
	public ItemBlockDyed(BlockDyed blockdyed)
	{
		super(blockdyed);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
	
	@Override
	public String getHighlightTip(ItemStack stack, String displayName)
	{
		return I18n.translateToLocal(getUnlocalizedName(stack)) + " " + displayName;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add("Color: " + I18n.translateToLocal(getUnlocalizedName(stack)));
		
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "color." + EnumDyeColor.byMetadata(stack.getMetadata())
									  .getName() + ".name";
	}
}