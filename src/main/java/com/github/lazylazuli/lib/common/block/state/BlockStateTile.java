package com.github.lazylazuli.lib.common.block.state;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface BlockStateTile
{
	default boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}
	
	default EnumBlockRenderType getRenderType()
	{
		return EnumBlockRenderType.INVISIBLE;
	}
}
