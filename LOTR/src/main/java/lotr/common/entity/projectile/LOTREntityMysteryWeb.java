package lotr.common.entity.projectile;

import lotr.common.LOTRLevelData;
import lotr.common.entity.npc.LOTREntityMirkwoodSpider;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityMysteryWeb extends EntityThrowable
{
	public LOTREntityMysteryWeb(World world)
	{
		super(world);
	}
	
	public LOTREntityMysteryWeb(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
	}
	
	public LOTREntityMysteryWeb(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
	@Override
	protected void onImpact(MovingObjectPosition m)
	{
		if (getThrower() != null && m.entityHit == getThrower())
		{
			return;
		}
		
        if (!worldObj.isRemote)
        {
			boolean spawnedSpider = false;
			if (rand.nextInt(4) == 0)
			{
				LOTREntityMirkwoodSpider spider = new LOTREntityMirkwoodSpider(worldObj);
				spider.setSpiderScale(0);
				spider.liftSpawnRestrictions = true;
				for (int i = -2; i <= -2 && !spawnedSpider; i++)
				{
					for (int j = 0; j <= 3 && !spawnedSpider; j++)
					{
						for (int k = -2; k <= -2 && !spawnedSpider; k++)
						{
							spider.setLocationAndAngles(posX + (double)i / 2D, posY + (double)j / 3D, posZ + (double)k / 2D, rand.nextFloat() * 360F, 0F);
							if (spider.getCanSpawnHere())
							{
								spider.liftSpawnRestrictions = false;
								spider.onSpawnWithEgg(null);
								worldObj.spawnEntityInWorld(spider);
								if (getThrower() != null)
								{
									spider.setAttackTarget(getThrower());
								}
								spawnedSpider = true;
							}
						}
					}
				}
			}
			
			if (!spawnedSpider)
			{
				IInventory tempInventory = new InventoryBasic("mysteryWeb", true, 1);
				LOTRChestContents.fillInventory(tempInventory, rand, LOTRChestContents.MIRKWOOD_LOOT, 1);
				ItemStack item = tempInventory.getStackInSlot(0);
				if (rand.nextInt(500) == 0)
				{
					item = new ItemStack(Items.melon, 64);
				}
				if (item != null)
				{
					EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, item);
					entityitem.delayBeforeCanPickup = 10;
					worldObj.spawnEntityInWorld(entityitem);
				}
			}
			
			playSound("random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1F) * 2F);
            setDead();
        }
	}
	
	@Override
    protected float func_70182_d()
    {
        return 0.5F;
    }
	
	@Override
    protected float getGravityVelocity()
    {
        return 0.01F;
    }
}
