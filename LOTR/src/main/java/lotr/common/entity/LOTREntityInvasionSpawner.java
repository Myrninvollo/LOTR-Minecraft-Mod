package lotr.common.entity;

import java.util.List;

import lotr.common.LOTREventHandler;
import lotr.common.LOTRFaction;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.world.LOTRInvasionSpawner.InvasionSpawnEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTREntityInvasionSpawner extends Entity
{
	private LOTRFaction invasionFaction;
	
	private static double MOB_RANGE = 40D;
	private int mobsRemaining;
	private int timeSinceLastSpawn = 0;
	
	public LOTREntityInvasionSpawner(World world)
	{
		super(world);
		setSize(1.5F, 1.5F);
		renderDistanceWeight = 4D;
	}
	
	@Override
    protected void entityInit() {}
	
	public boolean canSpawnerSpawnHere()
    {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		
		if (LOTREventHandler.isProtectedByBanner(worldObj, i, j, k, this, false))
		{
			return false;
		}
		
        return worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox);
    }
	
	public void setFaction(LOTRFaction f)
	{
		invasionFaction = f;
	}
	
	public LOTRFaction getFaction()
	{
		return invasionFaction;
	}
	
	public void announceInvasion(EntityPlayer entityplayer)
	{
		worldObj.playSoundAtEntity(entityplayer, "lotr:item.horn", 4F, 0.7F);
		entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.invasion.start", new Object[] {invasionFaction.factionName()}));
		mobsRemaining = MathHelper.getRandomIntegerInRange(rand, 30, 70);
	}

	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Faction", invasionFaction.name());
		nbt.setInteger("MobsRemaining", mobsRemaining);
		nbt.setInteger("TimeSinceSpawn", timeSinceLastSpawn);
	}

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
	{
		invasionFaction = LOTRFaction.forName(nbt.getString("Faction"));
		if (invasionFaction == null || invasionFaction.invasionMobs.isEmpty())
		{
			setDead();
		}
		else
		{
			mobsRemaining = nbt.getInteger("MobsRemaining");
			timeSinceLastSpawn = nbt.getInteger("TimeSinceSpawn");
		}
	}

	public void onBreak()
	{
		endInvasion();
	}
	
	private void endInvasion()
	{
		worldObj.createExplosion(this, posX, posY + (double)height / 2D, posZ, 0F, false);
		setDead();
	}
	
	@Override
	public void onUpdate()
	{
		if (!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
		{
			endInvasion();
			return;
		}
		
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        
		prevRotationYaw = rotationYaw;
		rotationYaw += 6F;
		prevRotationYaw = MathHelper.wrapAngleTo180_float(prevRotationYaw);
		rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);
		
		motionX = 0D;
		motionY = 0D;
		motionZ = 0D;
		moveEntity(motionX, motionY, motionZ);
		
		if (!worldObj.isRemote)
		{
			EntityPlayer entityplayer = worldObj.getClosestPlayer(posX, posY, posZ, MOB_RANGE);
			if (entityplayer != null)
			{
				timeSinceLastSpawn++;
				if (timeSinceLastSpawn >= 2400)
				{
					endInvasion();
				}
				else
				{
					if (mobsRemaining > 0)
					{
						List nearbyNPCs = worldObj.selectEntitiesWithinAABB(LOTREntityNPC.class, boundingBox.expand(12D, 12D, 12D), new LOTRNPCSelectByFaction(invasionFaction));
						if (nearbyNPCs.size() < 16 && rand.nextInt(120) == 0)
						{
							int mobSpawns = MathHelper.getRandomIntegerInRange(rand, 1, 4);
							mobSpawns = Math.min(mobSpawns, mobsRemaining);
							
							mobSpawnLoop:
							for (int l = 0; l < mobSpawns; l++)
							{
								InvasionSpawnEntry entry = (InvasionSpawnEntry)WeightedRandom.getRandomItem(rand, invasionFaction.invasionMobs);
								Class entityClass = entry.getEntityClass();
								LOTREntityNPC npc = (LOTREntityNPC)LOTREntities.createEntityByClass(entityClass, worldObj);
								
								for (int attempts = 0; attempts < 8; attempts++)
								{
									int i = MathHelper.floor_double(posX) - rand.nextInt(6) + rand.nextInt(6);
									int k = MathHelper.floor_double(posZ) - rand.nextInt(6) + rand.nextInt(6);
									int j = worldObj.getHeightValue(i, k);
									if (worldObj.getBlock(i, j - 1, k).isSideSolid(worldObj, i, j - 1, k, ForgeDirection.UP))
									{
										npc.setLocationAndAngles(i + 0.5D, j, k + 0.5D, rand.nextFloat() * 360F, 0F);
										npc.liftSpawnRestrictions = true;
										if (npc.getCanSpawnHere())
										{
											npc.liftSpawnRestrictions = false;
											npc.onSpawnWithEgg(null);
											npc.isNPCPersistent = false;
											worldObj.spawnEntityInWorld(npc);
											npc.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(MOB_RANGE);
											
											timeSinceLastSpawn = 0;
											mobsRemaining--;
											
											if (mobsRemaining > 0)
											{
												continue mobSpawnLoop;
											}
											else
											{
												break mobSpawnLoop;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			if (mobsRemaining <= 0)
			{
				endInvasion();
			}
		}
		else
		{
			String particle = rand.nextBoolean() ? "smoke" : "flame";
			worldObj.spawnParticle(particle, posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0D, 0D, 0D);
		}
	}
	
	@Override
	public boolean canBeCollidedWith()
    {
        return true;
    }
	
	@Override
	public void applyEntityCollision(Entity entity) {}
}
