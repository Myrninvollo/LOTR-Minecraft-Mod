package lotr.common.entity.ai;

import java.util.ArrayList;
import java.util.Random;

import lotr.common.LOTRReflection;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class LOTREntityAIFarm extends EntityAIBase
{
	private static int HOEING = 0;
	private static int PLANTING = 1;
	private static int HARVESTING = 2;
	
    private LOTREntityNPC theEntity;
	private World theWorld;
    private double moveSpeed;
    public IPlantable defaultSeedsItem;
	private int action;
    private double xPos;
    private double yPos;
    private double zPos;
    private int pathingTick;
    private int rePathDelay;
	private boolean harvestingSolidBlock;
   
    public LOTREntityAIFarm(LOTREntityNPC npc, double d, IPlantable item)
    {
        theEntity = npc;
		theWorld = npc.worldObj;
        moveSpeed = d;
		defaultSeedsItem = item;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
		if (theEntity.hiredNPCInfo.isActive && !theEntity.hiredNPCInfo.isGuardMode())
		{
			return false;
		}
		
		if (theEntity.getRNG().nextInt(20) == 0)
		{
			Vec3 hoeTarget = findTarget(HOEING);
			if (hoeTarget != null)
			{
				xPos = hoeTarget.xCoord;
				yPos = hoeTarget.yCoord;
				zPos = hoeTarget.zCoord;
				action = HOEING;
				return true;
			}
			else if (!theEntity.hiredNPCInfo.isActive || hasSeeds())
			{
				Vec3 plantTarget = findTarget(PLANTING);
				if (plantTarget != null)
				{
					xPos = plantTarget.xCoord;
					yPos = plantTarget.yCoord;
					zPos = plantTarget.zCoord;
					action = PLANTING;
					return true;
				}
				else if (theEntity.hiredNPCInfo.isActive && hasSpaceForCrops() && getCropForSeed(getSeedsToPlant()) != null)
				{
					Vec3 harvestTarget = findTarget(HARVESTING);
					if (harvestTarget != null)
					{
						xPos = harvestTarget.xCoord;
						yPos = harvestTarget.yCoord;
						zPos = harvestTarget.zCoord;
						action = HARVESTING;
						return true;
					}
				}
			}
		}
		return false;
    }
	
	private boolean hasSeeds()
	{
		if (theEntity.hiredNPCInfo.getHiredInventory() == null)
		{
			return false;
		}
		ItemStack itemstack = theEntity.hiredNPCInfo.getHiredInventory().getStackInSlot(0);
		return itemstack != null && itemstack.getItem() instanceof IPlantable && ((IPlantable)itemstack.getItem()).getPlantType(theWorld, -1, -1, -1) == EnumPlantType.Crop;
	}
	
	private IPlantable getSeedsToPlant()
	{
		if (hasSeeds())
		{
			return (IPlantable)theEntity.hiredNPCInfo.getHiredInventory().getStackInSlot(0).getItem();	
		}
		return defaultSeedsItem;
	}
	
	private boolean hasSpaceForCrops()
	{
		if (theEntity.hiredNPCInfo.getHiredInventory() == null)
		{
			return false;
		}
		for (int l = 1; l <= 2; l++)
		{
			ItemStack itemstack = theEntity.hiredNPCInfo.getHiredInventory().getStackInSlot(l);
			if (itemstack == null || (itemstack.stackSize < itemstack.getMaxStackSize() && itemstack.isItemEqual(getCropForSeed(getSeedsToPlant()))))
			{
				return true;
			}
		}
		return false;
	}
	
	private ItemStack getCropForSeed(IPlantable seed)
	{
		Block block = seed.getPlant(theWorld, -1, -1, -1);
		
		if (block instanceof BlockCrops)
		{
			return new ItemStack(LOTRReflection.getCropItem((BlockCrops)block));
		}
		else if (block instanceof BlockStem)
		{
			return new ItemStack(LOTRReflection.getStemFruitBlock((BlockStem)block).getItemDropped(0, theWorld.rand, 0), 1, 0);
		}
		
		return null;
	}

    @Override
    public boolean continueExecuting()
    {
		if (theEntity.hiredNPCInfo.isActive && !theEntity.hiredNPCInfo.isGuardMode())
		{
			return false;
		}
		
		if (pathingTick < 200)
		{
			int i = MathHelper.floor_double(xPos);
			int j = MathHelper.floor_double(yPos);
			int k = MathHelper.floor_double(zPos);
			
			if (action == HOEING)
			{
				return isSuitableForHoeing(i, j, k);
			}
			else if (action == PLANTING && (!theEntity.hiredNPCInfo.isActive || hasSeeds()))
			{
				return isSuitableForPlanting(i, j, k);
			}
			else if (action == HARVESTING && theEntity.hiredNPCInfo.isActive && hasSeeds() && hasSpaceForCrops() && getCropForSeed(getSeedsToPlant()) != null)
			{
				return isSuitableForHarvesting(i, j, k);
			}
		}
		return false;
    }
	
	@Override
    public void resetTask()
    {
		pathingTick = 0;
		rePathDelay = 0;
		action = -1;
		harvestingSolidBlock = false;
    }

	@Override
    public void updateTask()
    {
		if (theEntity.getDistanceSq(xPos, yPos, zPos) > 4D)
		{
			if (harvestingSolidBlock)
			{
				theEntity.getLookHelper().setLookPosition(xPos, yPos + 0.5D, zPos, 10F, (float)theEntity.getVerticalFaceSpeed());
			}
			else
			{
				theEntity.getLookHelper().setLookPosition(xPos, yPos - 0.5D, zPos, 10F, (float)theEntity.getVerticalFaceSpeed());
			}
			
			rePathDelay--;
			if (rePathDelay <= 0)
			{
				rePathDelay = 10;
				
				if (harvestingSolidBlock)
				{
					if (!theEntity.getNavigator().tryMoveToXYZ(xPos, yPos + 1, zPos, moveSpeed))
					{
						resetTask();
						return;
					}
				}
				else
				{
					if (!theEntity.getNavigator().tryMoveToXYZ(xPos, yPos, zPos, moveSpeed))
					{
						resetTask();
						return;
					}
				}
			}
			
			pathingTick++;
		}
		else
		{
			int i = MathHelper.floor_double(xPos);
			int j = MathHelper.floor_double(yPos);
			int k = MathHelper.floor_double(zPos);
			
			if (action == HOEING && isSuitableForHoeing(i, j, k))
			{
				theEntity.swingItem();
				
				if (isReplaceable(i, j + 1, k))
				{
					theWorld.setBlockToAir(i, j + 1, k);
				}
				
                Block block = Blocks.farmland;
				theWorld.setBlock(i, j, k, block, 0, 3);
				theWorld.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1F) / 2F, block.stepSound.getPitch() * 0.8F);
			}
			else if (action == PLANTING && isSuitableForPlanting(i, j, k))
			{
				theEntity.swingItem();
				
				IPlantable seed = getSeedsToPlant();
				Block plant = seed.getPlant(theWorld, i, j + 1, k);
				int meta = seed.getPlantMetadata(theWorld, i, j + 1, k);
				theWorld.setBlock(i, j + 1, k, plant, meta, 3);
				
				if (theEntity.hiredNPCInfo.isActive)
				{
					theEntity.hiredNPCInfo.getHiredInventory().decrStackSize(0, 1);
				}
			}
			else if (action == HARVESTING && isSuitableForHarvesting(i, j, k))
			{
				theEntity.swingItem();
				
				Block block = theWorld.getBlock(i, j + 1, k);
				ArrayList drops = block.getDrops(theWorld, i, j + 1, k, theWorld.getBlockMetadata(i, j + 1, k), 0);
				theWorld.setBlockToAir(i, j + 1, k);
				theWorld.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1F) / 2F, block.stepSound.getPitch() * 0.8F);
				
				ItemStack seedItem = theEntity.hiredNPCInfo.getHiredInventory().getStackInSlot(0);
				ItemStack cropItem = getCropForSeed(getSeedsToPlant());
				boolean addedOneSeed = false;
				
				dropsLoop:
				for (Object obj : drops)
				{
					ItemStack drop = (ItemStack)obj;
					
					if (drop.isItemEqual(cropItem))
					{
						if (drop.isItemEqual(seedItem) && !addedOneSeed)
						{
							addedOneSeed = true;
							
							ItemStack itemstack = theEntity.hiredNPCInfo.getHiredInventory().getStackInSlot(0);
							if (itemstack.stackSize + drop.stackSize <= itemstack.getMaxStackSize())
							{
								itemstack.stackSize++;
								theEntity.hiredNPCInfo.getHiredInventory().setInventorySlotContents(0, itemstack);
								continue dropsLoop;
							}
						}
						
						findSlot:
						for (int l = 1; l <= 2; l++)
						{
							ItemStack itemstack = theEntity.hiredNPCInfo.getHiredInventory().getStackInSlot(l);
							if (itemstack == null)
							{
								theEntity.hiredNPCInfo.getHiredInventory().setInventorySlotContents(l, drop);
								break findSlot;
							}
							else if (itemstack.stackSize + drop.stackSize <= itemstack.getMaxStackSize() && itemstack.isItemEqual(cropItem))
							{
								itemstack.stackSize++;
								theEntity.hiredNPCInfo.getHiredInventory().setInventorySlotContents(l, itemstack);
								break findSlot;
							}
						}
					}
					else if (drop.isItemEqual(seedItem))
					{
						ItemStack itemstack = theEntity.hiredNPCInfo.getHiredInventory().getStackInSlot(0);
						if (itemstack.stackSize + drop.stackSize <= itemstack.getMaxStackSize())
						{
							itemstack.stackSize++;
							theEntity.hiredNPCInfo.getHiredInventory().setInventorySlotContents(0, itemstack);
							continue dropsLoop;
						}
					}
				}
			}
		}
	}
	
    private Vec3 findTarget(int targetAction)
    {
        Random random = theEntity.getRNG();
        for (int l = 0; l < 32; l++)
        {
            int i = MathHelper.floor_double(theEntity.posX) - 8 + random.nextInt(17);
            int j = MathHelper.floor_double(theEntity.boundingBox.minY) - 4 + random.nextInt(9);
            int k = MathHelper.floor_double(theEntity.posZ) - 8 + random.nextInt(17);

			boolean flag = (targetAction == HOEING && isSuitableForHoeing(i, j, k)) || (targetAction == PLANTING && isSuitableForPlanting(i, j, k)) || (targetAction == HARVESTING && isSuitableForHarvesting(i, j, k));
			if (flag && !theEntity.isWithinHomeDistance(i, j, k))
			{
				flag = false;
			}
			if (flag)
			{
				if (harvestingSolidBlock)
				{
					if (theEntity.getNavigator().getPathToXYZ(i, j + 2, k) == null)
					{
						flag = false;
					}
				}
				else
				{
					if (theEntity.getNavigator().getPathToXYZ(i, j + 1, k) == null)
					{
						flag = false;
					}
				}
			}

			if (flag)
            {
                return theWorld.getWorldVec3Pool().getVecFromPool((double)i + 0.5D, (double)j, (double)k + 0.5D);
            }
        }

        return null;
    }
	
	private boolean isSuitableForHoeing(int i, int j, int k)
	{
		harvestingSolidBlock = false;
		
		Block block = theWorld.getBlock(i, j, k);
		if ((block == Blocks.dirt || block == Blocks.grass) && isReplaceable(i, j + 1, k))
		{
			for (int i1 = i - 4; i1 <= i + 4; i1++)
			{
				for (int k1 = k - 4; k1 <= k + 4; k1++)
				{
					if (theWorld.getBlock(i1, j, k1).getMaterial() == Material.water)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean isSuitableForPlanting(int i, int j, int k)
	{
		harvestingSolidBlock = false;
		return theWorld.getBlock(i, j, k) == Blocks.farmland && theWorld.getBlockMetadata(i, j, k) > 0 && isReplaceable(i, j + 1, k);
	}
	
	private boolean isSuitableForHarvesting(int i, int j, int k)
	{
		if (hasSeeds())
		{
			IPlantable seed = getSeedsToPlant();
			Block block = seed.getPlant(theWorld, i, j + 1, k);
			
			if (block instanceof BlockCrops)
			{
				harvestingSolidBlock = false;
				return theWorld.getBlock(i, j + 1, k) == block && theWorld.getBlockMetadata(i, j + 1, k) >= 7;
			}
			else if (block instanceof BlockStem)
			{
				harvestingSolidBlock = true;
				return theWorld.getBlock(i, j + 1, k) == LOTRReflection.getStemFruitBlock((BlockStem)block);
			}
		}
		return false;
	}
	
	private boolean isReplaceable(int i, int j, int k)
	{
		Block block = theWorld.getBlock(i, j, k);
        return (!block.getMaterial().isLiquid() && block.isReplaceable(theWorld, i, j, k));
	}
}
