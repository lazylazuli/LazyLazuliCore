package com.github.lazylazuli.lazylazulilib.block.state;

import net.minecraft.block.state.IBlockProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BlockStateTile extends IBlockProperties
{
	default boolean eventReceived(World worldIn, BlockPos pos, int id, int param)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}
	
	default EnumBlockRenderType getRenderType()
	{
		return EnumBlockRenderType.INVISIBLE;
	}
}
