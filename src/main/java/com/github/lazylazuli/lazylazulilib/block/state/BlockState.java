package com.github.lazylazuli.lazylazulilib.block.state;

import com.github.lazylazuli.lazylazulilib.block.BlockBase;
import com.google.common.collect.ImmutableMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockState extends BlockStateContainer.StateImplementation
{
	public BlockState(BlockBase blockIn,
			ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
	{
		super(blockIn, propertiesIn);
	}
	
	@Override
	public BlockBase getBlock()
	{
		return (BlockBase) super.getBlock();
	}
	
	public Material getMaterial()
	{
		//noinspection deprecation
		return getBlock().getMaterial(this);
	}
	
	@Override
	public boolean canEntitySpawn(Entity entityIn)
	{
		return true;
	}
	
	@Override
	public float getBlockHardness(World worldIn, BlockPos pos)
	{
		return 1;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos)
	{
		return ForgeHooks.blockStrength(this, player, worldIn, pos);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag()
	{
		return getMaterial().getMobilityFlag();
	}
	
	@Override
	public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos)
	{
		return this;
	}
	
	//
	
	@Override
	public boolean isOpaqueCube()
	{
		return true;
	}
	
	@Override
	public boolean isFullBlock()
	{
		return isOpaqueCube();
	}
	
	@Override
	public boolean isFullCube()
	{
		return true;
	}
	
	@Override
	public boolean isNormalCube()
	{
		return getMaterial().blocksMovement() && isFullCube();
	}
	
	@Override
	public boolean isBlockNormalCube()
	{
		return isNormalCube();
	}
	
	@Override
	public boolean isFullyOpaque()
	{
		return getMaterial().isOpaque() && isFullCube();
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return isFullyOpaque() && side == EnumFacing.UP || isNormalCube();
	}
	
	@Override
	public boolean causesSuffocation()
	{
		return getMaterial().blocksMovement() && isFullCube();
	}
	
	// REDSTONE POWER
	
	@Override
	public boolean canProvidePower()
	{
		return false;
	}
	
	@Override
	public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return 0;
	}
	
	@Override
	public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return 0;
	}
	
	// COMPARATOR
	
	@Override
	public boolean hasComparatorInputOverride()
	{
		return false;
	}
	
	@Override
	public int getComparatorInputOverride(World worldIn, BlockPos pos)
	{
		return 0;
	}
	
	// LIGHT
	
	@Override
	@Deprecated
	public int getLightOpacity()
	{
		return isFullBlock() ? 255 : 0;
	}
	
	@Override
	@Deprecated
	public int getLightValue()
	{
		return 0;
	}
	
	@Override
	public int getLightOpacity(IBlockAccess world, BlockPos pos)
	{
		return isFullBlock() ? 255 : 0;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		IBlockState other = world.getBlockState(pos);
		if (other.getBlock() != getBlock())
		{
			return other.getLightValue(world, pos);
		}
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue()
	{
		return isBlockNormalCube() ? 0.2F : 1.0F;
	}
	
	@Override
	public boolean useNeighborBrightness()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isTranslucent()
	{
		return !getMaterial().blocksLight();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos)
	{
		return source.getCombinedLight(pos, getLightValue(source, pos));
	}
	
	// RENDER
	
	@Override
	public MapColor getMapColor()
	{
		return getMaterial().getMaterialMapColor();
	}
	
	@Override
	public EnumBlockRenderType getRenderType()
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return isOpaqueCube();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomBreakingProgress()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		AxisAlignedBB axisalignedbb = getBoundingBox(blockAccess, pos);
		
		switch (side)
		{
		case DOWN:
			
			if (axisalignedbb.minY > 0.0D)
			{
				return true;
			}
			
			break;
		case UP:
			
			if (axisalignedbb.maxY < 1.0D)
			{
				return true;
			}
			
			break;
		case NORTH:
			
			if (axisalignedbb.minZ > 0.0D)
			{
				return true;
			}
			
			break;
		case SOUTH:
			
			if (axisalignedbb.maxZ < 1.0D)
			{
				return true;
			}
			
			break;
		case WEST:
			
			if (axisalignedbb.minX > 0.0D)
			{
				return true;
			}
			
			break;
		case EAST:
			
			if (axisalignedbb.maxX < 1.0D)
			{
				return true;
			}
		}
		
		return !blockAccess.getBlockState(pos.offset(side))
						   .doesSideBlockRendering(blockAccess, pos.offset(side), side.getOpposite());
	}
	
	// AABB
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
	{
		return getBoundingBox(worldIn, pos).offset(pos);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockAccess worldIn, BlockPos pos)
	{
		return getBoundingBox(worldIn, pos);
	}
	
	@Override
	public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185908_6_)
	{
		AxisAlignedBB blockBox = getCollisionBoundingBox(worldIn, pos);
		if (entityBox != Block.NULL_AABB && blockBox != null)
		{
			AxisAlignedBB axisalignedbb = blockBox.offset(pos);
			
			if (entityBox.intersectsWith(axisalignedbb))
			{
				collidingBoxes.add(axisalignedbb);
			}
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos)
	{
		return Block.FULL_BLOCK_AABB;
	}
	
	@Override
	@Nullable
	public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		Vec3d vec3d = start.subtract((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
		Vec3d vec3d1 = end.subtract((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
		RayTraceResult raytraceresult = getBoundingBox(worldIn, pos).calculateIntercept(vec3d, vec3d1);
		return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector(
				(double) pos.getX(),
				(double) pos.getY(),
				(double) pos.getZ()
		), raytraceresult.sideHit, pos);
	}
	
	//
	
	@Override
	public Vec3d getOffset(IBlockAccess access, BlockPos pos)
	{
		Block.EnumOffsetType offsetType = getBlock().getOffsetType();
		
		if (offsetType == Block.EnumOffsetType.NONE)
		{
			return Vec3d.ZERO;
		} else
		{
			long i = MathHelper.getCoordinateRandom(pos.getX(), 0, pos.getZ());
			return new Vec3d(
					((double) ((float) (i >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D,
					offsetType == Block.EnumOffsetType.XYZ ? ((double) ((float) (i >> 20 & 15L) / 15.0F) -
							1.0D) * 0.2D : 0.0D,
					((double) ((float) (i >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D
			);
		}
	}
	
	@Override
	public IBlockState withRotation(Rotation rot)
	{
		return this;
	}
	
	@Override
	public IBlockState withMirror(Mirror mirrorIn)
	{
		return this;
	}
	
	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param)
	{
		return false;
	}
	
	@Override
	public void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {}
}
