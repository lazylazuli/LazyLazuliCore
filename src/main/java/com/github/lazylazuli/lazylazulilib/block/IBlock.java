package com.github.lazylazuli.lazylazulilib.block;

import com.github.lazylazuli.lazylazulilib.block.state.BlockState;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public interface IBlock
{
	IProperty<?>[] getProperties();
	
	BlockState createBlockState(ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn);
	
	BlockBase getBlock();
	
	boolean isFullyOpaque(IBlockState state);
	
	boolean isFullBlock(IBlockState state);
	
	boolean canEntitySpawn(IBlockState state, Entity entityIn);
	
	int getLightOpacity(IBlockState state);
	
	/**
	 * Used in the renderer to apply ambient occlusion
	 */
	@SideOnly(Side.CLIENT)
	boolean isTranslucent(IBlockState state);
	
	int getLightValue(IBlockState state);
	
	/**
	 * Should block use the brightest neighbor light value as its own
	 */
	boolean getUseNeighborBrightness(IBlockState state);
	
	/**
	 * Get a material of block
	 */
	Material getMaterial(IBlockState state);
	
	/**
	 * Get the MapColor for this Block and the given BlockState
	 */
	MapColor getMapColor(IBlockState state);
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	IBlockState getStateFromMeta(int meta);
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	int getMetaFromState(IBlockState state);
	
	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos);
	
	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	IBlockState withRotation(IBlockState state, Rotation rot);
	
	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	IBlockState withMirror(IBlockState state, Mirror mirrorIn);
	
	/**
	 * Sets how much light is blocked going through this block. Returns the object for convenience in constructing.
	 */
	Block setLightOpacity(int opacity);
	
	/**
	 * Sets the light value that the block emits. Returns resulting block instance for constructing convenience.
	 */
	Block setLightLevel(float value);
	
	/**
	 * Sets the the blocks resistance to explosions. Returns the object for convenience in constructing.
	 */
	Block setResistance(float resistance);
	
	/**
	 * Indicate if a material is a normal solid opaque cube
	 */
	boolean isBlockNormalCube(IBlockState state);
	
	/**
	 * Used for nearly all game logic (non-rendering) purposes. Use Forge-provided isNormalCube(IBlockAccess, BlockPos)
	 * instead.
	 */
	boolean isNormalCube(IBlockState state);
	
	boolean causesSuffocation(IBlockState state);
	
	boolean isFullCube(IBlockState state);
	
	@SideOnly(Side.CLIENT)
	boolean hasCustomBreakingProgress(IBlockState state);
	
	boolean isPassable(IBlockAccess worldIn, BlockPos pos);
	
	/**
	 * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
	 * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
	 */
	EnumBlockRenderType getRenderType(IBlockState state);
	
	/**
	 * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
	 */
	boolean isReplaceable(IBlockAccess worldIn, BlockPos pos);
	
	/**
	 * Sets how many hits it takes to break a block.
	 */
	Block setHardness(float hardness);
	
	Block setBlockUnbreakable();
	
	float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos);
	
	/**
	 * Sets whether this block type will receive random update ticks
	 */
	Block setTickRandomly(boolean shouldTick);
	
	/**
	 * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
	 * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
	 */
	boolean getTickRandomly();
	
	//Forge: New State sensitive version.
	boolean hasTileEntity();
	
	AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos);
	
	@SideOnly(Side.CLIENT)
	int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos);
	
	@SideOnly(Side.CLIENT)
	boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing
			side);
	
	/**
	 * Whether this Block is solid on the given Side
	 */
	boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side);
	
	void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_);
	
	@Nullable
	AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos);
	
	@SideOnly(Side.CLIENT)
	AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos);
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	boolean isOpaqueCube(IBlockState state);
	
	boolean canCollideCheck(IBlockState state, boolean hitIfLiquid);
	
	/**
	 * Returns if this block is collidable. Only used by fire, although stairs return that of the block that the stair
	 * is made of (though nobody's going to make fire stairs, right?)
	 */
	boolean isCollidable();
	
	/**
	 * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
	 */
	void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random);
	
	void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand);
	
	@SideOnly(Side.CLIENT)
	void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand);
	
	/**
	 * Called when a player destroys this Block
	 */
	void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state);
	
	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a
	 * neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	
	void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos);
	
	/**
	 * How many world ticks before ticking
	 */
	int tickRate(World worldIn);
	
	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	void onBlockAdded(World worldIn, BlockPos pos, IBlockState state);
	
	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	void breakBlock(World worldIn, BlockPos pos, IBlockState state);
	
	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	int quantityDropped(Random random);
	
	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	Item getItemDropped(IBlockState state, Random rand, int fortune);
	
	/**
	 * Get the hardness of this Block relative to the ability of the given player
	 */
	float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos);
	
	/**
	 * Spawn this Block's drops into the World as EntityItems
	 */
	void dropBlockAsItem(World worldIn, BlockPos pos, IBlockState state, int fortune);
	
	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune);
	
	/**
	 * Spawns the given amount of experience into the World as XP orb entities
	 */
	void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount);
	
	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	int damageDropped(IBlockState state);
	
	/**
	 * Returns how much this block can resist explosions from the passed in entity.
	 */
	float getExplosionResistance(Entity exploder);
	
	/**
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
	 */
	
	@Nullable
	RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d
			end);
	
	/**
	 * Called when this Block is destroyed by an Explosion
	 */
	void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn);
	
	@SideOnly(Side.CLIENT)
	BlockRenderLayer getBlockLayer();
	
	/**
	 * Check whether this Block can be placed on the given side
	 */
	boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side);
	
	boolean canPlaceBlockAt(World worldIn, BlockPos pos);
	
	/**
	 * Called when the block is right clicked by a player.
	 */
	boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);
	
	/**
	 * Triggered whenever an entity collides with this block (enters into the block)
	 */
	void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn);
	
	// Forge: use getStateForPlacement
	
	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer);
	
	void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn);
	
	Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion);
	
	int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side);
	
	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	boolean canProvidePower(IBlockState state);
	
	/**
	 * Called When an Entity Collided with the Block
	 */
	void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn);
	
	int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side);
	
	void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
			@Nullable TileEntity te, ItemStack stack);
	
	/**
	 * Get the quantity dropped based on the given fortune level
	 */
	int quantityDroppedWithBonus(int fortune, Random random);
	
	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 */
	void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack);
	
	/**
	 * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
	 */
	boolean canSpawnInBlock();
	
	Block setUnlocalizedName(String name);
	
	/**
	 * Gets the localized name of this block. Used for the statistics page.
	 */
	String getLocalizedName();
	
	/**
	 * Returns the unlocalized name of the block with "tile." appended to the front.
	 */
	String getUnlocalizedName();
	
	/**
	 * Called on both Client and Server when World#addBlockEvent is called. On the Server, this may perform additional
	 * changes to the world, like pistons replacing the block with an extended base. On the client, the update may
	 * involve replacing tile entities, playing sounds, or performing other visual actions to reflect the server side
	 * changes.
	 */
	
	boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param);
	
	/**
	 * Return the state of blocks statistics flags - if the block is counted for mined and placed.
	 */
	boolean getEnableStats();
	
	EnumPushReaction getMobilityFlag(IBlockState state);
	
	@SideOnly(Side.CLIENT)
	float getAmbientOcclusionLightValue(IBlockState state);
	
	/**
	 * Block's chance to react to a living entity falling on it.
	 */
	void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance);
	
	/**
	 * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
	 * on its own
	 */
	void onLanded(World worldIn, Entity entityIn);
	
	// Forge: Use more sensitive version below: getPickBlock
	ItemStack getItem(World worldIn, BlockPos pos, IBlockState state);
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list);
	
	Block setCreativeTab(CreativeTabs tab);
	
	void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player);
	
	/**
	 * Returns the CreativeTab to display the given block on.
	 */
	@SideOnly(Side.CLIENT)
	CreativeTabs getCreativeTabToDisplayOn();
	
	/**
	 * Called similar to random ticks, but only when it is raining.
	 */
	void fillWithRain(World worldIn, BlockPos pos);
	
	boolean requiresUpdates();
	
	/**
	 * Return whether this block can drop from an explosion.
	 */
	boolean canDropFromExplosion(Explosion explosionIn);
	
	boolean isAssociatedBlock(Block other);
	
	boolean hasComparatorInputOverride(IBlockState state);
	
	int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos);
	
	BlockStateContainer getBlockState();
	
	IBlockState getDefaultState();
	
	/**
	 * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
	 */
	Block.EnumOffsetType getOffsetType();
	
	Vec3d getOffset(IBlockState state, IBlockAccess worldIn, BlockPos pos);
	
	// Forge - World/state/pos/entity sensitive version below
	SoundType getSoundType();
	
	String toString();
	
	@SideOnly(Side.CLIENT)
	void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced);
	
	/**
	 * Get a light value for the block at the specified coordinates, normal ranges are between 0 and 15
	 *
	 * @param state Block state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return The light value
	 */
	int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Checks if a player or entity can use this block to 'climb' like a ladder.
	 *
	 * @param state  The current state
	 * @param world  The current world
	 * @param pos    Block position in world
	 * @param entity The entity trying to use the ladder, CAN be null.
	 *
	 * @return True if the block should act like a ladder
	 */
	boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity);
	
	/**
	 * Return true if the block is a normal, solid cube.  This
	 * determines indirect power state, entity ejection from blocks, and a few
	 * others.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return True if the block is a full cube
	 */
	boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Check if the face of a block should block rendering.
	 * <p>
	 * Faces which are fully opaque should return true, faces with transparency
	 * or faces which do not span the full size of the block should return false.
	 *
	 * @param state The current block state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @param face  The side to check
	 *
	 * @return True if the block is opaque on the specified side.
	 */
	boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face);
	
	/**
	 * Checks if the block is a solid face on the given side, used by placement logic.
	 *
	 * @param base_state The base state, getActualState should be called first
	 * @param world      The current world
	 * @param pos        Block position in world
	 * @param side       The side to check
	 *
	 * @return True if the block is solid on the specified side.
	 */
	boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side);
	
	/**
	 * Determines if this block should set fire and deal fire damage
	 * to entities coming into contact with it.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return True if the block should deal damage
	 */
	boolean isBurning(IBlockAccess world, BlockPos pos);
	
	/**
	 * Determines this block should be treated as an air block
	 * by the rest of the code. This method is primarily
	 * useful for creating pure logic-blocks that will be invisible
	 * to the player and otherwise interact as air would.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return True if the block considered air
	 */
	boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
	 *
	 * @param player The player damaging the block
	 * @param pos    The block's current position
	 *
	 * @return True to spawn the drops
	 */
	boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player);
	
	/**
	 * Called when a player removes a block.  This is responsible for
	 * actually destroying the block, and the block is intact at time of call.
	 * This is called regardless of whether the player can harvest the block or
	 * not.
	 * <p>
	 * Return true if the block is actually destroyed.
	 * <p>
	 * Note: When used in multiplayer, this is called on both client and
	 * server sides!
	 *
	 * @param state       The current state.
	 * @param world       The current world
	 * @param player      The player damaging the block, may be null
	 * @param pos         Block position in world
	 * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
	 *                    Can be useful to delay the destruction of tile entities till after harvestBlock
	 *
	 * @return True if the block is actually destroyed.
	 */
	boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean
			willHarvest);
	
	/**
	 * Chance that fire will spread and consume this block.
	 * 300 being a 100% chance, 0, being a 0% chance.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 * @param face  The face that the fire is coming from
	 *
	 * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
	 */
	int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face);
	
	/**
	 * Called when fire is updating, checks if a block face can catch fire.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 * @param face  The face that the fire is coming from
	 *
	 * @return True if the face can be on fire, false otherwise.
	 */
	boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face);
	
	/**
	 * Called when fire is updating on a neighbor block.
	 * The higher the number returned, the faster fire will spread around this block.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 * @param face  The face that the fire is coming from
	 *
	 * @return A number that is used to determine the speed of fire growth around the block
	 */
	int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face);
	
	/**
	 * Currently only called by fire when it is on top of this block.
	 * Returning true will prevent the fire from naturally dying during updating.
	 * Also prevents firing from dying from rain.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 * @param side  The face that the fire is coming from
	 *
	 * @return True if this block sustains fire, meaning it will never go out.
	 */
	boolean isFireSource(World world, BlockPos pos, EnumFacing side);
	
	/**
	 * Called throughout the code as a replacement for block instanceof BlockContainer
	 * Moving this to the Block base class allows for mods that wish to extend vanilla
	 * blocks, and also want to have a tile entity on that block, may.
	 * <p>
	 * Return true from this function to specify this block has a tile entity.
	 *
	 * @param state State of the current block
	 *
	 * @return True if block has a tile entity, false otherwise
	 */
	boolean hasTileEntity(IBlockState state);
	
	/**
	 * Called throughout the code as a replacement for ITileEntityProvider.createNewTileEntity
	 * Return the same thing you would from that function.
	 * This will fall back to ITileEntityProvider.createNewTileEntity(World) if this block is a ITileEntityProvider
	 *
	 * @param state The state of the current block
	 *
	 * @return A instance of a class extending TileEntity
	 */
	@Nullable
	TileEntity createTileEntity(World world, IBlockState state);
	
	/**
	 * State and fortune sensitive version, this replaces the old (int meta, Random rand)
	 * version in 1.1.
	 *
	 * @param state   Current state
	 * @param fortune Current item fortune level
	 * @param random  Random number generator
	 *
	 * @return The number of items to drop
	 */
	int quantityDropped(IBlockState state, int fortune, Random random);
	
	/**
	 * This returns a complete list of items dropped from this block.
	 *
	 * @param world   The current world
	 * @param pos     Block position in world
	 * @param state   Current state
	 * @param fortune Breakers fortune level
	 *
	 * @return A ArrayList containing all items this block drops
	 */
	List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune);
	
	/**
	 * Return true from this function if the player with silk touch can harvest this block directly, and not it's
	 * normal
	 * drops.
	 *
	 * @param world  The world
	 * @param pos    Block position in world
	 * @param state  current block state
	 * @param player The player doing the harvesting
	 *
	 * @return True if the block can be directly harvested using silk touch
	 */
	boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player);
	
	/**
	 * Determines if a specified mob type can spawn on this block, returning false will
	 * prevent any mob from spawning on the block.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @param type  The Mob Category Type
	 *
	 * @return True to allow a mob of the specified category to spawn, false to prevent it.
	 */
	boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType
			type);
	
	/**
	 * Determines if this block is classified as a Bed, Allowing
	 * players to sleep in it, though the block has to specifically
	 * perform the sleeping functionality in it's activated event.
	 *
	 * @param state  The current state
	 * @param world  The current world
	 * @param pos    Block position in world
	 * @param player The player or camera entity, null in some cases.
	 *
	 * @return True to treat this as a bed
	 */
	boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player);
	
	/**
	 * Returns the position that the player is moved to upon
	 * waking up, or respawning at the bed.
	 *
	 * @param state  The current state
	 * @param world  The current world
	 * @param pos    Block position in world
	 * @param player The player or camera entity, null in some cases.
	 *
	 * @return The spawn position
	 */
	@Nullable
	BlockPos getBedSpawnPosition(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityPlayer
			player);
	
	/**
	 * Called when a user either starts or stops sleeping in the bed.
	 *
	 * @param world    The current world
	 * @param pos      Block position in world
	 * @param player   The player or camera entity, null in some cases.
	 * @param occupied True if we are occupying the bed, or false if they are stopping use of the bed
	 */
	void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied);
	
	/**
	 * Returns the direction of the block. Same values that
	 * are returned by BlockDirectional
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return Bed direction
	 */
	EnumFacing getBedDirection(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Determines if the current block is the foot half of the bed.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return True if the current block is the foot side of a bed.
	 */
	boolean isBedFoot(IBlockAccess world, BlockPos pos);
	
	/**
	 * Called when a leaf should start its decay process.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 */
	void beginLeavesDecay(IBlockState state, World world, BlockPos pos);
	
	/**
	 * Determines if this block can prevent leaves connected to it from decaying.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return true if the presence this block can prevent leaves from decaying.
	 */
	boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Determines if this block is considered a leaf block, used to apply the leaf decay and generation system.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return true if this block is considered leaves.
	 */
	boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Used during tree growth to determine if newly generated leaves can replace this block.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return true if this block can be replaced by growing leaves.
	 */
	boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return true if the block is wood (logs)
	 */
	boolean isWood(IBlockAccess world, BlockPos pos);
	
	/**
	 * Determines if the current block is replaceable by Ore veins during world generation.
	 *
	 * @param state  The current state
	 * @param world  The current world
	 * @param pos    Block position in world
	 * @param target The generic target block the gen is looking for, Standards define stone
	 *               for overworld generation, and neatherack for the nether.
	 *
	 * @return True to allow this block to be replaced by a ore
	 */
	boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target);
	
	/**
	 * Location sensitive version of getExplosionRestance
	 *
	 * @param world     The current world
	 * @param pos       Block position in world
	 * @param exploder  The entity that caused the explosion, can be null
	 * @param explosion The explosion
	 *
	 * @return The amount of the explosion absorbed.
	 */
	float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion);
	
	/**
	 * Called when the block is destroyed by an explosion.
	 * Useful for allowing the block to take into account tile entities,
	 * state, etc. when exploded, before it is removed.
	 *
	 * @param world     The current world
	 * @param pos       Block position in world
	 * @param explosion The explosion instance affecting the block
	 */
	void onBlockExploded(World world, BlockPos pos, Explosion explosion);
	
	/**
	 * Determine if this block can make a redstone connection on the side provided,
	 * Useful to control which sides are inputs and outputs for redstone wires.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @param side  The side that is trying to make the connection, CAN BE NULL
	 *
	 * @return True to make the connection
	 */
	boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side);
	
	/**
	 * Determines if a torch can be placed on the top surface of this block.
	 * Useful for creating your own block that torches can be on, such as fences.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return True to allow the torch to be placed
	 */
	boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Called when a user uses the creative pick block button on this block
	 *
	 * @param target The full target the player is looking at
	 *
	 * @return A ItemStack to add to the player's inventory, empty itemstack if nothing should be added.
	 */
	ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer
			player);
	
	/**
	 * Used by getTopSolidOrLiquidBlock while placing biome decorations, villages, etc
	 * Also used to determine if the player can spawn on this block.
	 *
	 * @return False to disallow spawning
	 */
	boolean isFoliage(IBlockAccess world, BlockPos pos);
	
	/**
	 * Allows a block to override the standard EntityLivingBase.updateFallState
	 * particles, this is a server side method that spawns particles with
	 * WorldServer.spawnParticle
	 *
	 * @param world             The current Server world
	 * @param blockPosition     of the block that the entity landed on.
	 * @param iblockstate       State at the specific world/pos
	 * @param entity            the entity that hit landed on the block.
	 * @param numberOfParticles that vanilla would have spawned.
	 *
	 * @return True to prevent vanilla landing particles form spawning.
	 */
	boolean addLandingEffects(IBlockState state, WorldServer world, BlockPos
			blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles);
	
	/**
	 * Spawn a digging particle effect in the world, this is a wrapper
	 * around EffectRenderer.addBlockHitEffects to allow the block more
	 * control over the particles. Useful when you have entirely different
	 * texture sheets for different sides/locations in the world.
	 *
	 * @param state   The current state
	 * @param world   The current world
	 * @param target  The target the player is looking at {x/y/z/side/sub}
	 * @param manager A reference to the current particle manager.
	 *
	 * @return True to prevent vanilla digging particles form spawning.
	 */
	@SideOnly(Side.CLIENT)
	boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager);
	
	/**
	 * Spawn particles for when the block is destroyed. Due to the nature
	 * of how this is invoked, the x/y/z locations are not always guaranteed
	 * to host your block. So be sure to do proper sanity checks before assuming
	 * that the location is this block.
	 *
	 * @param world   The current world
	 * @param pos     Position to spawn the particle
	 * @param manager A reference to the current particle manager.
	 *
	 * @return True to prevent vanilla break particles from spawning.
	 */
	@SideOnly(Side.CLIENT)
	boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager);
	
	/**
	 * Determines if this block can support the passed in plant, allowing it to be planted and grow.
	 * Some examples:
	 * Reeds check if its a reed, or if its sand/dirt/grass and adjacent to water
	 * Cacti checks if its a cacti, or if its sand
	 * Nether types check for soul sand
	 * Crops check for tilled soil
	 * Caves check if it's a solid surface
	 * Plains check if its grass or dirt
	 * Water check if its still water
	 *
	 * @param state     The Current state
	 * @param world     The current world
	 * @param pos       Block position in world
	 * @param direction The direction relative to the given position the plant wants to be, typically its UP
	 * @param plantable The plant that wants to check
	 *
	 * @return True to allow the plant to be planted/stay.
	 */
	boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable
			plantable);
	
	/**
	 * Called when a plant grows on this block, only implemented for saplings using the WorldGen*Trees classes right
	 * now.
	 * Modder may implement this for custom plants.
	 * This does not use ForgeDirection, because large/huge trees can be located in non-representable direction,
	 * so the source location is specified.
	 * Currently this just changes the block to dirt if it was grass.
	 * <p>
	 * Note: This happens DURING the generation, the generation may not be complete when this is called.
	 *
	 * @param state  The current state
	 * @param world  Current world
	 * @param pos    Block position in world
	 * @param source Source plant's position in world
	 */
	void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source);
	
	/**
	 * Checks if this soil is fertile, typically this means that growth rates
	 * of plants on this soil will be slightly sped up.
	 * Only vanilla case is tilledField when it is within range of water.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return True if the soil should be considered fertile.
	 */
	boolean isFertile(World world, BlockPos pos);
	
	/**
	 * Location aware and overrideable version of the lightOpacity array,
	 * return the number to subtract from the light value when it passes through this block.
	 * <p>
	 * This is not guaranteed to have the tile entity in place before this is called, so it is
	 * Recommended that you have your tile entity call relight after being placed if you
	 * rely on it for light info.
	 *
	 * @param state The Block state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return The amount of light to block, 0 for air, 255 for fully opaque.
	 */
	int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Determines if this block is can be destroyed by the specified entities normal behavior.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return True to allow the ender dragon to destroy this block
	 */
	boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity);
	
	/**
	 * Determines if this block can be used as the base of a beacon.
	 *
	 * @param world  The current world
	 * @param pos    Block position in world
	 * @param beacon Beacon position in world
	 *
	 * @return True, to support the beacon, and make it active with this block.
	 */
	boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon);
	
	/**
	 * Rotate the block. For vanilla blocks this rotates around the axis passed in (generally, it should be the "face"
	 * that was hit).
	 * Note: for mod blocks, this is up to the block and modder to decide. It is not mandated that it be a rotation
	 * around the
	 * face, but could be a rotation to orient *to* that face, or a visiting of possible rotations.
	 * The method should return true if the rotation was successful though.
	 *
	 * @param world The world
	 * @param pos   Block position in world
	 * @param axis  The axis to rotate around
	 *
	 * @return True if the rotation was successful, False if the rotation failed, or is not possible
	 */
	boolean rotateBlock(World world, BlockPos pos, EnumFacing axis);
	
	/**
	 * Get the rotations that can apply to the block at the specified coordinates. Null means no rotations are
	 * possible.
	 * Note, this is up to the block to decide. It may not be accurate or representative.
	 *
	 * @param world The world
	 * @param pos   Block position in world
	 *
	 * @return An array of valid axes to rotate around, or null for none or unknown
	 */
	@Nullable
	EnumFacing[] getValidRotations(World world, BlockPos pos);
	
	/**
	 * Determines the amount of enchanting power this block can provide to an enchanting table.
	 *
	 * @param world The World
	 * @param pos   Block position in world
	 *
	 * @return The amount of enchanting power this block produces.
	 */
	float getEnchantPowerBonus(World world, BlockPos pos);
	
	/**
	 * Common way to recolor a block with an external tool
	 *
	 * @param world The world
	 * @param pos   Block position in world
	 * @param side  The side hit with the coloring tool
	 * @param color The color to change to
	 *
	 * @return If the recoloring was successful
	 */
	@SuppressWarnings({
			"unchecked",
			"rawtypes"
	})
	boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color);
	
	/**
	 * Gathers how much experience this block drops when broken.
	 *
	 * @param state   The current state
	 * @param world   The world
	 * @param pos     Block position
	 * @param fortune
	 *
	 * @return Amount of XP from breaking this block.
	 */
	int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune);
	
	/**
	 * Called when a tile entity on a side of this block changes is created or is destroyed.
	 *
	 * @param world    The world
	 * @param pos      Block position in world
	 * @param neighbor Block position of neighbor
	 */
	void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor);
	
	/**
	 * Called on an Observer block whenever an update for an Observer is received.
	 *
	 * @param observerState   The Observer block's state.
	 * @param world           The current world.
	 * @param observerPos     The Observer block's position.
	 * @param changedBlock    The updated block.
	 * @param changedBlockPos The updated block's position.
	 */
	void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block
			changedBlock,
			BlockPos changedBlockPos);
	
	/**
	 * Called to determine whether to allow the a block to handle its own indirect power rather than using the default
	 * rules.
	 *
	 * @param world The world
	 * @param pos   Block position in world
	 * @param side  The INPUT side of the block to be powered - ie the opposite of this block's output side
	 *
	 * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
	 */
	boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side);
	
	/**
	 * If this block should be notified of weak changes.
	 * Weak changes are changes 1 block away through a solid block.
	 * Similar to comparators.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 *
	 * @return true To be notified of changes
	 */
	boolean getWeakChanges(IBlockAccess world, BlockPos pos);
	
	/**
	 * Sets or removes the tool and level required to harvest this block.
	 *
	 * @param toolClass Class
	 * @param level     Harvest level:
	 *                  Wood:    0
	 *                  Stone:   1
	 *                  Iron:    2
	 *                  Diamond: 3
	 *                  Gold:    0
	 */
	void setHarvestLevel(String toolClass, int level);
	
	/**
	 * Sets or removes the tool and level required to harvest this block.
	 *
	 * @param toolClass Class
	 * @param level     Harvest level:
	 *                  Wood:    0
	 *                  Stone:   1
	 *                  Iron:    2
	 *                  Diamond: 3
	 *                  Gold:    0
	 * @param state     The specific state.
	 */
	void setHarvestLevel(String toolClass, int level, IBlockState state);
	
	/**
	 * Queries the class of tool required to harvest this block, if null is returned
	 * we assume that anything can harvest this block.
	 */
	@Nullable
	String getHarvestTool(IBlockState state);
	
	/**
	 * Queries the harvest level of this item stack for the specified tool class,
	 * Returns -1 if this tool is not of the specified type
	 *
	 * @return Harvest level, or -1 if not the specified tool type.
	 */
	int getHarvestLevel(IBlockState state);
	
	/**
	 * Checks if the specified tool type is efficient on this block,
	 * meaning that it digs at full speed.
	 */
	boolean isToolEffective(String type, IBlockState state);
	
	/**
	 * Can return IExtendedBlockState
	 */
	IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos);
	
	/**
	 * Called when the entity is inside this block, may be used to determined if the entity can breathing,
	 * display material overlays, or if the entity can swim inside a block.
	 *
	 * @param world       that is being tested.
	 * @param blockpos    position thats being tested.
	 * @param iblockstate state at world/blockpos
	 * @param entity      that is being tested.
	 * @param yToTest,    primarily for testingHead, which sends the the eye level of the entity, other wise it sends a
	 *                    y that can be tested vs liquid height.
	 * @param materialIn  to test for.
	 * @param testingHead when true, its testing the entities head for vision, breathing ect... otherwise its testing
	 *                    the body, for swimming and movement adjustment.
	 *
	 * @return null for default behavior, true if the entity is within the material, false if it was not.
	 */
	@Nullable
	Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity
			entity,
			double yToTest, Material materialIn, boolean testingHead);
	
	/**
	 * Called when boats or fishing hooks are inside the block to check if they are inside
	 * the material requested.
	 *
	 * @param world       world that is being tested.
	 * @param pos         block thats being tested.
	 * @param boundingBox box to test, generally the bounds of an entity that are besting tested.
	 * @param materialIn  to check for.
	 *
	 * @return null for default behavior, true if the box is within the material, false if it was not.
	 */
	@Nullable
	Boolean isAABBInsideMaterial(World world, BlockPos pos, AxisAlignedBB boundingBox, Material materialIn);
	
	/**
	 * Queries if this block should render in a given layer.
	 * ISmartBlockModel can use {@link net.minecraftforge.client.MinecraftForgeClient#getRenderLayer()} to alter their
	 * model based on layer.
	 */
	boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer);
	
	/**
	 * Sensitive version of getSoundType
	 *
	 * @param state  The state
	 * @param world  The world
	 * @param pos    The position. Note that the world may not necessarily have {@code state} here!
	 * @param entity The entity that is breaking/stepping on/placing/hitting/falling on this block, or null if no
	 *               entity
	 *               is in this context
	 *
	 * @return A SoundType to use
	 */
	SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity);
	
	/**
	 * @param state     The state
	 * @param world     The world
	 * @param pos       The position of this state
	 * @param beaconPos The position of the beacon
	 *
	 * @return A float RGB [0.0, 1.0] array to be averaged with a beacon's existing beam color, or null to do
	 * nothing to
	 * the beam
	 */
	@Nullable
	float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos);
	
	/**
	 * Gets the {@link IBlockState} to place
	 *
	 * @param world  The world the block is being placed in
	 * @param pos    The position the block is being placed at
	 * @param facing The side the block is being placed on
	 * @param hitX   The X coordinate of the hit vector
	 * @param hitY   The Y coordinate of the hit vector
	 * @param hitZ   The Z coordinate of the hit vector
	 * @param meta   The metadata of {@link ItemStack} as processed by {@link Item#getMetadata(int)}
	 * @param placer The entity placing the block
	 * @param hand   The player hand used to place this block
	 *
	 * @return The state to be placed in the world
	 */
	IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand);
	
	/**
	 * Determines if another block can connect to this block
	 *
	 * @param world  The current world
	 * @param pos    The position of this block
	 * @param facing The side the connecting block is on
	 *
	 * @return True to allow another block to connect to this block
	 */
	boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing);
	
	/**
	 * Get the {@code PathNodeType} for this block. Return {@code null} for vanilla behavior.
	 *
	 * @return the PathNodeType
	 */
	@Nullable
	PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos
			pos);
}