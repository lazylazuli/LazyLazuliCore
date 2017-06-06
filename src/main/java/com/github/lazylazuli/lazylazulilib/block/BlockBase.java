package com.github.lazylazuli.lazylazulilib.block;

import com.github.lazylazuli.lazylazulilib.Stack;
import com.github.lazylazuli.lazylazulilib.block.state.BlockState;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class BlockBase extends Block
{
	public BlockBase(Material material)
	{
		super(material);
		fullBlock = getDefaultState().isFullBlock();
		lightOpacity = getDefaultState().getLightOpacity();
		translucent = getDefaultState().isTranslucent();
	}
	
	protected abstract IProperty<?>[] getProperties();
	
	protected abstract BlockState createBlockState(ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn);
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, getProperties())
		{
			@Override
			protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>>
					properties,
					@Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
			{
				return createBlockState(properties);
			}
		};
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
			@Nullable TileEntity te, ItemStack stack)
	{
		if (te instanceof IWorldNameable && ((IWorldNameable) te).hasCustomName())
		{
			StatBase stats = StatList.getBlockStats(this);
			
			if (stats != null)
			{
				player.addStat(stats);
			}
			
			player.addExhaustion(0.005F);
			
			if (worldIn.isRemote)
			{
				return;
			}
			
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			Item item = getItemDropped(state, worldIn.rand, i);
			
			if (item == Items.AIR)
			{
				return;
			}
			
			ItemStack itemstack = Stack.of(item, quantityDropped(worldIn.rand));
			itemstack.setStackDisplayName(((IWorldNameable) te).getName());
			spawnAsEntity(worldIn, pos, itemstack);
		} else
		{
			super.harvestBlock(worldIn, player, pos, state, null, stack);
		}
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return !worldIn.getBlockState(pos)
					   .getMaterial()
					   .blocksMovement();
	}
	
	@Override
	public boolean canSpawnInBlock()
	{
		return !getDefaultState().getMaterial()
								 .isSolid() && !getDefaultState().getMaterial()
																 .isLiquid();
	}
	
	@Override
	public boolean canEntitySpawn(IBlockState state, Entity entityIn)
	{
		return state.canEntitySpawn(entityIn);
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos)
	{
		return state.getBlockHardness(worldIn, pos);
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		return state.getPlayerRelativeBlockHardness(player, worldIn, pos);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return state.getMobilityFlag();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return state.getActualState(blockAccess, pos);
	}
	
	//
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.isOpaqueCube();
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return state.isFullBlock();
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return state.isFullCube();
	}
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return state.isNormalCube();
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state)
	{
		return state.isBlockNormalCube();
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return state.isFullyOpaque();
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return state.isSideSolid(world, pos, side);
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return state.causesSuffocation();
	}
	
	// REDSTONE POWER
	
	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return state.canProvidePower();
	}
	
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return state.getWeakPower(blockAccess, pos, side);
	}
	
	@Override
	public int getStrongPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return state.getStrongPower(blockAccess, pos, side);
	}
	
	// COMPARATOR
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return state.hasComparatorInputOverride();
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World worldIn, BlockPos pos)
	{
		return state.getComparatorInputOverride(worldIn, pos);
	}
	
	// LIGHT
	
	@Override
	@Deprecated
	public int getLightOpacity(IBlockState state)
	{
		return state.getLightOpacity();
	}
	
	@Override
	@Deprecated
	public int getLightValue(IBlockState state)
	{
		return state.getLightValue();
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.getLightOpacity(world, pos);
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.getLightValue(world, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		return state.getAmbientOcclusionLightValue();
	}
	
	@Override
	public boolean getUseNeighborBrightness(IBlockState state)
	{
		return state.useNeighborBrightness();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isTranslucent(IBlockState state)
	{
		return state.isTranslucent();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return state.getPackedLightmapCoords(source, pos);
	}
	
	// RENDER
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		return state.getMapColor();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return state.getRenderType();
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return state.doesSideBlockRendering(world, pos, side);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomBreakingProgress(IBlockState state)
	{
		return state.hasCustomBreakingProgress();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing facing)
	{
		return state.shouldSideBeRendered(blockAccess, pos, facing);
	}
	
	// AABB
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return state.getSelectedBoundingBox(worldIn, pos);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return state.getCollisionBoundingBox(worldIn, pos);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185908_6_)
	{
		state.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return state.getBoundingBox(blockAccess, pos);
	}
	
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		return state.collisionRayTrace(worldIn, pos, start, end);
	}
	
	//
	
	@Override
	public Vec3d getOffset(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		return state.getOffset(access, pos);
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withRotation(rot);
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withMirror(mirrorIn);
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
	{
		return state.onBlockEventReceived(worldIn, pos, id, param);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		state.neighborChanged(worldIn, pos, blockIn, fromPos);
	}
}
