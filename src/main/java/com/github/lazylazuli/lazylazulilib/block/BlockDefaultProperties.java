package com.github.lazylazuli.lazylazulilib.block;

import com.github.lazylazuli.lazylazulilib.block.state.BlockState;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.properties.IProperty;

public interface BlockDefaultProperties extends IBlock
{
	default IProperty<?>[] getProperties()
	{
		return new IProperty[0];
	}
	
	default BlockState createBlockState(ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
	{
		return new BlockState(getBlock(), propertiesIn);
	}
}
