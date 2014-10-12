package lotr.common.entity.animal;

import java.util.List;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.world.biome.LOTRBiomeGenMidgewater;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityMidges extends EntityLiving implements LOTRAmbientCreature
{
    private ChunkCoordinates currentFlightTarget;
    private EntityPlayer playerTarget;
	public Midge[] midges;
	
	public class Midge
	{
		public float midge_posX;
		public float midge_posY;
		public float midge_posZ;
		public float midge_prevPosX;
		public float midge_prevPosY;
		public float midge_prevPosZ;
		private float midge_initialPosX;
		private float midge_initialPosY;
		private float midge_initialPosZ;
		public float midge_rotation;
		private int midgeTick;
		private int maxMidgeTick = 80;
		
		public Midge()
		{
			midge_initialPosX = midge_posX = -1F + rand.nextFloat() * 2F;
			midge_initialPosY = midge_posY = rand.nextFloat() * 2F;
			midge_initialPosZ = midge_posZ = -1F + rand.nextFloat() * 2F;
			midge_rotation = rand.nextFloat() * 360F;
			midgeTick = rand.nextInt(maxMidgeTick);
		}
		
		public void update()
		{
			midge_prevPosX = midge_posX;
			midge_prevPosY = midge_posY;
			midge_prevPosZ = midge_posZ;
			
			midgeTick++;
			if (midgeTick > maxMidgeTick)
			{
				midgeTick = 0;
			}
			
			midge_posY = midge_initialPosY + 0.5F * MathHelper.sin((float)midgeTick / ((float)Math.PI * 2F));
		}
	}

    public LOTREntityMidges(World world)
    {
        super(world);
        setSize(2F, 2F);
		renderDistanceWeight = 0.5F;
		midges = new Midge[3 + rand.nextInt(6)];
		for (int l = 0; l < midges.length; l++)
		{
			midges[l] = new Midge();
		}
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2D);
    }

	@Override
    public boolean canBePushed()
    {
        return false;
    }

	@Override
    protected void collideWithEntity(Entity entity) {}

	@Override
    protected void collideWithNearbyEntities() {}

	@Override
    protected boolean isAIEnabled()
    {
        return true;
    }

	@Override
    public void onUpdate()
    {
        super.onUpdate();
        motionY *= 0.6D;
		
		for (int l = 0; l < midges.length; l++)
		{
			midges[l].update();
		}
		
		if (rand.nextInt(5) == 0)
		{
			playSound("lotr:midges.swarm", getSoundVolume(), getSoundPitch());
		}
		
		if (!worldObj.isRemote && isEntityAlive())
		{
			boolean inMidgewater = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ)) instanceof LOTRBiomeGenMidgewater;
			int chance = inMidgewater ? 100 : 1000;
			if (rand.nextInt(chance) == 0)
			{
				double range = inMidgewater ? 16D : 32D;
				int threshold = inMidgewater ? 6 : 4;
				List list = worldObj.getEntitiesWithinAABB(LOTREntityMidges.class, boundingBox.expand(range, range, range));
				if (list.size() < threshold)
				{
					LOTREntityMidges moreMidges = new LOTREntityMidges(worldObj);
					moreMidges.setLocationAndAngles(posX, posY, posZ, rand.nextFloat() * 360F, 0F);
					moreMidges.onSpawnWithEgg(null);
					worldObj.spawnEntityInWorld(moreMidges);
				}
			}
		}
    }

	@Override
    protected void updateAITasks()
    {
        super.updateAITasks();

		if (currentFlightTarget != null && !worldObj.isAirBlock(currentFlightTarget.posX, currentFlightTarget.posY, currentFlightTarget.posZ))
		{
			currentFlightTarget = null;
		}
		
		if (playerTarget != null && (!playerTarget.isEntityAlive() || getDistanceSqToEntity(playerTarget) > 256D))
		{
			playerTarget = null;
		}
		
		if (playerTarget != null)
		{
			if (rand.nextInt(400) == 0)
			{
				playerTarget = null;
			}
			else
			{
				currentFlightTarget = new ChunkCoordinates((int)playerTarget.posX, (int)playerTarget.posY + 3, (int)playerTarget.posZ);
			}
		}
		else
		{
			if (rand.nextInt(100) == 0)
			{
				EntityPlayer closestPlayer = worldObj.getClosestPlayerToEntity(this, 8D);
				if (closestPlayer != null && rand.nextInt(10) == 0)
				{
					playerTarget = closestPlayer;
				}
				else
				{
					int i = (int)posX + rand.nextInt(7) - rand.nextInt(7);
					int j = (int)posY + rand.nextInt(4) - rand.nextInt(3);
					int k = (int)posZ + rand.nextInt(7) - rand.nextInt(7);
					if (j < 1)
					{
						j = 1;
					}
					int height = worldObj.getTopSolidOrLiquidBlock(i, k);
					if (j > height + 8)
					{
						j = height + 8;
					}
					currentFlightTarget = new ChunkCoordinates(i, j, k);
				}
			}
		}

		if (currentFlightTarget != null)
		{
			double dx = (double)currentFlightTarget.posX + 0.5D - posX;
			double dy = (double)currentFlightTarget.posY + 0.5D - posY;
			double dz = (double)currentFlightTarget.posZ + 0.5D - posZ;
			motionX += (Math.signum(dx) * 0.5D - motionX) * 0.1D;
			motionY += (Math.signum(dy) * 0.7D - motionY) * 0.1D;
			motionZ += (Math.signum(dz) * 0.5D - motionZ) * 0.1D;
			moveForward = 0.2F;
		}
		else
		{
			motionX = motionY = motionZ = 0D;
		}
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void fall(float f) {}

    @Override
    protected void updateFallState(double d, boolean flag) {}

    @Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		
		if (!worldObj.isRemote && damagesource instanceof EntityDamageSourceIndirect)
		{
			Entity attacker = damagesource.getEntity();
			if (attacker instanceof LOTREntityNPC)
			{
				LOTREntityNPC npc = (LOTREntityNPC)attacker;
				if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() != null)
				{
					EntityPlayer entityplayer = npc.hiredNPCInfo.getHiringPlayer();
					LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.shootDownMidges);
				}
			}
		}
	}
	
	@Override
    protected boolean canDespawn()
    {
        return true;
    }

    @Override
    public boolean getCanSpawnHere()
    {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

        if (j < 62)
        {
            return false;
        }

		return worldObj.getBlock(i, j - 1, k) == Blocks.grass && super.getCanSpawnHere();
    }
	
	@Override
    public boolean allowLeashing()
    {
        return false;
    }

    @Override
    protected boolean interact(EntityPlayer entityplayer)
    {
        return false;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		int id = LOTREntities.getEntityID(this);
		if (id > 0 && LOTREntities.creatures.containsKey(id))
		{
			return new ItemStack(LOTRMod.spawnEgg, 1, id);
		}
		return null;
    }
}
