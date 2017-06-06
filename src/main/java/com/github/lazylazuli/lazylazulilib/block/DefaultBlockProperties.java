package com.github.lazylazuli.lazylazulilib.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.properties.IProperty;

public interface DefaultBlockProperties
{
	default IProperty<?>[] getProperties()
	{
		return new IProperty[0];
	}
	
	default BlockState createBlockState(BlockBase block, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
	{
		return new BlockState(block, propertiesIn);
	}
}
