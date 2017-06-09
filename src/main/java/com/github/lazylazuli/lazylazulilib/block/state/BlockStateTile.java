package com.github.lazylazuli.lazylazulilib.block.state;

import com.github.lazylazuli.lazylazulilib.block.BlockBase;
import com.google.common.collect.ImmutableMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.properties.IProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockStateTile extends BlockState
{
	public BlockStateTile(BlockBase blockIn,
			ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
	{
		super(blockIn, propertiesIn);
	}
	
	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}
	
	@Override
	public EnumBlockRenderType getRenderType()
	{
		return EnumBlockRenderType.INVISIBLE;
	}
}
