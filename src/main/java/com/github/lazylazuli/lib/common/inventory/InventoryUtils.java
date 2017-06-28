package com.github.lazylazuli.lib.common.inventory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public final class InventoryUtils
{
	/**
	 * This checks the position for an inventory. If the block is not a valid inventory then a random entity
	 * with an
	 * inventory will be returned. Otherwise, this returns null.
	 *
	 * @return Returns an inventory at the given position
	 */
	public static IInventory getInventoryAtPosition(World world, int x, int y, int z)
	{
		IInventory inventory = null;
		BlockPos blockPos = new BlockPos(x, y, z);
		IBlockState state = world.getBlockState(blockPos);
		Block block = state.getBlock();
		
		if (block.hasTileEntity(state))
		{
			TileEntity te = world.getTileEntity(blockPos);
			
			if (te instanceof IInventory)
			{
				inventory = (IInventory) te;
				
				if (inventory instanceof TileEntityChest && block instanceof BlockChest)
				{
					inventory = ((BlockChest) block).getContainer(world, blockPos, true);
				}
			}
		}
		
		if (inventory == null)
		{
			List<Entity> list = getEntitiesWithInventories(world, x, y, z);
			
			if (!list.isEmpty())
			{
				Entity entity = list.get(world.rand.nextInt(list.size()));
				
				if (entity instanceof EntityPlayer)
				{
					inventory = ((EntityPlayer) entity).inventory;
				} else
				{
					inventory = (IInventory) list.get(world.rand.nextInt(list.size()));
				}
			}
		}
		
		return inventory;
	}
	
	public static IInventory getInventoryAtPosition(World world, double x, double y, double z)
	{
		return getInventoryAtPosition(world, (int) x, (int) y, (int) z);
	}
	
	public static IInventory getInventoryAtPosition(World world, BlockPos pos)
	{
		return getInventoryAtPosition(world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static List<Entity> getEntitiesWithInventories(World world, int x, int y, int z)
	{
		AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
		
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, aabb, EntitySelectors.HAS_INVENTORY);
		entities.addAll(world.getEntitiesWithinAABB(EntityPlayer.class, aabb));
		
		return entities;
	}
	
	public static List<Entity> getEntitiesWithInventories(World world, double x, double y, double z)
	{
		return getEntitiesWithInventories(world, (int) x, (int) y, (int) z);
	}
	
	public static List<Entity> getEntitiesWithInventories(World world, BlockPos pos)
	{
		return getEntitiesWithInventories(world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	//
	
	public static boolean isInventorySlotEmpty(IInventory inv, int index)
	{
		return inv.getStackInSlot(index).isEmpty();
	}
	
	public static boolean areInventorySlotsEmpty(IInventory inventory, int... indices)
	{
		for (int index : indices)
		{
			if (!isInventorySlotEmpty(inventory, index))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isInventoryEmpty(IInventory inventory)
	{
		int size = inventory.getSizeInventory();
		
		for (int i = 0; i < size; ++i)
		{
			if (!isInventorySlotEmpty(inventory, i))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isInventoryEmpty(IInventory inventory, EnumFacing side)
	{
		return !(inventory instanceof ISidedInventory)
			   ? isInventoryEmpty(inventory)
			   : areInventorySlotsEmpty(inventory, ((ISidedInventory) inventory).getSlotsForFace(side));
	}
	
	//
	
	public static boolean isInventorySlotFull(IInventory inventory, int index)
	{
		ItemStack stack = inventory.getStackInSlot(index);
		return !stack.isEmpty() && (stack.getCount() >= Math.min(inventory.getInventoryStackLimit(),
				stack.getMaxStackSize()
		));
	}
	
	public static boolean areInventorySlotsFull(IInventory inventory, int... indices)
	{
		for (int index : indices)
		{
			if (!isInventorySlotFull(inventory, index))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isInventoryFull(IInventory inventory)
	{
		int size = inventory.getSizeInventory();
		
		for (int i = 0; i < size; ++i)
		{
			if (!isInventorySlotFull(inventory, i))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isInventoryFull(IInventory inventory, EnumFacing side)
	{
		return !(inventory instanceof ISidedInventory)
			   ? isInventoryFull(inventory)
			   : areInventorySlotsFull(inventory, ((ISidedInventory) inventory).getSlotsForFace(side));
	}
	
	//
	
	public static boolean canExtractItemFromSlot(IInventory inventory, ItemStack stack, int index, @Nullable
			EnumFacing side)
	{
		if (inventory instanceof ISidedInventory)
		{
			ISidedInventory sidedInventory = (ISidedInventory) inventory;
			
			return side != null && sidedInventory.canExtractItem(index, stack, side);
		}
		
		return true;
	}
	
	public static boolean canInsertItemInSlot(IInventory inventory, ItemStack stack, int index, @Nullable EnumFacing
			side)
	{
		if (inventory instanceof ISidedInventory)
		{
			ISidedInventory sidedInventory = (ISidedInventory) inventory;
			
			return side != null && sidedInventory.canInsertItem(index, stack, side);
		}
		
		return inventory.isItemValidForSlot(index, stack);
	}
	
	/**
	 * Attempts to insert stack in any inventory slot.
	 *
	 * @param inventory   the inventory to insert to
	 * @param stack the stack to insert
	 * @param side  the inventory's side to insert in
	 *
	 * @return true if a part or the whole stack was inserted
	 */
	public static boolean insertStack(IInventory inventory, ItemStack stack, @Nullable EnumFacing side)
	{
		boolean stackInserted = false;
		
		if (inventory instanceof ISidedInventory && side != null)
		{
			ISidedInventory isidedinventory = (ISidedInventory) inventory;
			int[] indices = isidedinventory.getSlotsForFace(side);
			
			for (int i = 0; i < indices.length && !stack.isEmpty(); ++i)
			{
				stackInserted = insertStack(inventory, stack, indices[i], side);
			}
		} else
		{
			int size = inventory.getSizeInventory();
			
			for (int i = 0; i < size && !stack.isEmpty(); ++i)
			{
				stackInserted = insertStack(inventory, stack, i, side);
			}
		}
		
		return stackInserted;
	}
	
	/**
	 * @param inventory   the inventory to insert to
	 * @param stack the stack to insert
	 * @param index the slot index
	 * @param side  the inventory's side to insert in
	 *
	 * @return true if a part or the whole stack was inserted
	 */
	public static boolean insertStack(IInventory inventory, ItemStack stack, int index, EnumFacing side)
	{
		ItemStack stackInSlot = inventory.getStackInSlot(index);
		boolean stackInserted = false;
		
		if (canInsertItemInSlot(inventory, stack, index, side))
		{
			if (stackInSlot.isEmpty())
			{
				inventory.setInventorySlotContents(index, stack.copy());
				stack.setCount(0);
				stackInserted = true;
			} else if (Stack.canCombine(stackInSlot, stack))
			{
				int spaceLeft = getRemainingSpaceInSlot(inventory, index);
				
				if (spaceLeft > 0)
				{
					int i = Math.min(stack.getCount(), spaceLeft);
					stack.shrink(i);
					stackInSlot.grow(i);
					stackInserted = i > 0;
				}
			}
		}
		
		return stackInserted;
	}
	
	/**
	 * Checks how many more items can be stacked on a slot respecting inventory stack limit.
	 *
	 * @param inventory   the inventory to check
	 * @param index the slot index
	 *
	 * @return the amount of space in left in the slot
	 */
	public static int getRemainingSpaceInSlot(IInventory inventory, int index)
	{
		ItemStack stack = inventory.getStackInSlot(index);
		
		return Math.min(stack.getMaxStackSize(), inventory.getInventoryStackLimit()) - stack.getCount();
	}
}
