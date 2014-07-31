package lotr.common.entity.npc;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRFaction;
import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIEat;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LOTREntitySaruman extends LOTREntityNPC
{
	private LOTREntityRabbit targetingRabbit;
	private int ticksChasingRabbit;
	private String randomNameTag;
	
	public LOTREntitySaruman(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIOpenDoor(this, true));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new LOTREntityAIEat(this, new LOTRFoods(new ItemStack[] {new ItemStack(Blocks.log)}), 200));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityLivingBase.class, 20F, 0.05F));
        tasks.addTask(5, new EntityAILookIdle(this));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.gandalfStaffWhite));
		return data;
    }
	
	@Override
	public String getCustomNameTag()
    {
		if (randomNameTag == null)
		{
			StringBuilder tmp = new StringBuilder();
	        for (int l = 0; l < 100; l++)
	        {
	        	tmp.append((char)(rand.nextInt(1000)));
	        }
	        randomNameTag = tmp.toString();
		}
		return randomNameTag;
    }
	
	@Override
	public boolean hasCustomNameTag()
    {
        return true;
    }
	
	@Override
	public boolean getAlwaysRenderNameTag()
    {
        return true;
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.URUK_HAI;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote)
		{
			if (rand.nextInt(10) == 0)
			{
				playSound(getLivingSound(), getSoundVolume(), getSoundPitch());
			}
			
			List allMobsExcludingRabbits = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(24D, 24D, 24D));
			for (int i = 0; i < allMobsExcludingRabbits.size(); i++)
			{
				Entity entity = (Entity)allMobsExcludingRabbits.get(i);
				if (entity instanceof LOTREntityRabbit)
				{
					continue;
				}
				
				float f = getDistanceToEntity(entity);
				float strength = 1F;
				if (entity instanceof EntityPlayer)
				{
					strength /= 3F;
				}
				float force = strength / (f * f);
				
				double x = entity.posX - posX;
				double y = entity.posY - posY;
				double z = entity.posZ - posZ;
				
				x *= force;
				y *= force;
				z *= force;
				
				if (entity instanceof EntityPlayerMP)
				{
					 ((EntityPlayerMP)entity).playerNetServerHandler.sendPacket(new S27PacketExplosion(posX, posY, posZ, 0F, new ArrayList(), Vec3.createVectorHelper(x, y, z)));
				}
				else
				{
					entity.motionX += x;
					entity.motionY += y;
					entity.motionZ += z;
				}
			}
			
			if (rand.nextInt(40) == 0)
			{
				LOTREntityRabbit rabbit = new LOTREntityRabbit(worldObj);
				int i = MathHelper.floor_double(posX) - rand.nextInt(16) + rand.nextInt(16);
				int j = MathHelper.floor_double(boundingBox.minY) - rand.nextInt(8) + rand.nextInt(8);
				int k = MathHelper.floor_double(posZ) - rand.nextInt(16) + rand.nextInt(16);
				rabbit.setLocationAndAngles(i, j, k, 0F, 0F);
				AxisAlignedBB aabb = rabbit.boundingBox;
				if (worldObj.checkNoEntityCollision(aabb) && worldObj.getCollidingBoundingBoxes(rabbit, aabb).isEmpty() && !worldObj.isAnyLiquid(aabb))
				{
					worldObj.spawnEntityInWorld(rabbit);
				}
			}
			
			if (targetingRabbit == null && rand.nextInt(20) == 0)
			{
				List rabbits = worldObj.getEntitiesWithinAABB(LOTREntityRabbit.class, boundingBox.expand(24D, 24D, 24D));
				if (!rabbits.isEmpty())
				{
					LOTREntityRabbit rabbit = (LOTREntityRabbit)rabbits.get(rand.nextInt(rabbits.size()));
					if (rabbit.ridingEntity == null)
					{
						targetingRabbit = rabbit;
					}
				}
			}
			if (targetingRabbit != null)
			{
				if (!targetingRabbit.isEntityAlive())
				{
					targetingRabbit = null;
				}
				else
				{
					getNavigator().tryMoveToEntityLiving(targetingRabbit, 1D);
					if (getDistanceToEntity(targetingRabbit) < 1D)
					{
						Entity entityToMount = this;
						while (entityToMount.riddenByEntity != null)
						{
							entityToMount = entityToMount.riddenByEntity;
						}
						targetingRabbit.mountEntity(entityToMount);
						targetingRabbit = null;
					}
				}
			}
		}
	}
	
	@Override
    protected String getLivingSound()
    {
        return "lotr:orc.say";
    }
	
	@Override
	public int getTalkInterval()
	{
		return 10;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		int j = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(Items.bone, 1);
		}
	}
}
