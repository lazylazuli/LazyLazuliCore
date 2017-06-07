package com.github.lazylazuli.lazylazulilib;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Craftory
{
	/**
	 * ###
	 */
	public static void horizontal(ItemStack result, ItemStack ingredient)
	{
		GameRegistry.addRecipe(result, "###", '#', ingredient);
	}
	
	/**
	 * #<br>
	 * #<br>
	 * #
	 */
	public static void vertical(ItemStack result, ItemStack ingredient)
	{
		GameRegistry.addRecipe(result, "#", "#", "#", '#', ingredient);
	}
	
	/**
	 * ###<br>
	 * # #<br>
	 * ###
	 */
	public static void frame(ItemStack result, ItemStack ingredient)
	{
		GameRegistry.addRecipe(result, "###", "# #", "###", '#', ingredient);
	}
	
	/**
	 * ###<br>
	 * #X#<br>
	 * ###
	 */
	public static void framed(ItemStack result, ItemStack frame, ItemStack framed)
	{
		GameRegistry.addRecipe(result, "###", "#X#", "###", '#', frame, 'X', framed);
	}
	
	/**
	 * ###<br>
	 * ###<br>
	 * ###
	 */
	public static void block(ItemStack result, ItemStack ingredient)
	{
		GameRegistry.addRecipe(result, "###", "###", "###", '#', ingredient);
	}
	
	/**
	 * X#X<br>
	 * #X#<br>
	 * X#X
	 */
	public static void mix(ItemStack result, ItemStack diamond, ItemStack x)
	{
		GameRegistry.addRecipe(result, "X#X", "#X#", "X#X", '#', diamond, 'X', x);
	}
}
