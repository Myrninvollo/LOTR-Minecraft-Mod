package lotr.common.entity;

import java.util.List;

import lotr.common.*;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.world.LOTRInvasionSpawner.InvasionSpawnEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTREntityInvasionSpawner extends Entity
{
	private LOTRFaction invasionFaction;
	
	private static double MOB_RANGE = 40D;
	private int mobsRemaining;
	private int timeSinceLastSpawn = 0;
	
	public float spawnerSpin;
	public float prevSpawnerSpin;
	
	public LOTREntityInvasionSpawner(World world)
	{
		super(world);
		setSize(1.5F, 1.5F);
		renderDistanceWeight = 4D;
		spawnerSpin = rand.nextFloat() * 360F;
	}
	
	@Override
    protected void entityInit() {}
	
	public boolean canInvasionSpawnHere()
    {
		if (LOTRBannerProtection.isProtectedByBanner(worldObj, this, LOTRBannerProtection.forInvasionSpawner(this), false))
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
	
	private void playHorn()
	{
		worldObj.playSoundAtEntity(this, "lotr:item.horn", 4F, 0.65F + rand.nextFloat() * 0.1F);
	}
	
	public void announceInvasion()
	{
		playHorn();
		mobsRemaining = MathHelper.getRandomIntegerInRange(rand, 30, 70);
		
		List<EntityPlayer> nearbyPlayers = worldObj.getEntitiesWithinAABB(EntityPlayer.class, boundingBox.expand(MOB_RANGE, MOB_RANGE, MOB_RANGE));
		for (EntityPlayer entityplayer : nearbyPlayers)
		{
			if (LOTRLevelData.getData(entityplayer).getAlignment(invasionFaction) < 0)
			{
				entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.invasion.start", new Object[] {invasionFaction.factionName()}));
			}
		}
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
        
        prevSpawnerSpin = spawnerSpin;
		spawnerSpin += 6F;
		prevSpawnerSpin = MathHelper.wrapAngleTo180_float(prevSpawnerSpin);
		spawnerSpin = MathHelper.wrapAngleTo180_float(spawnerSpin);
		
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
						if (nearbyNPCs.size() < 16 && rand.nextInt(160) == 0)
						{
							int mobSpawns = MathHelper.getRandomIntegerInRange(rand, 1, 6);
							mobSpawns = Math.min(mobSpawns, mobsRemaining);
							boolean spawnedAnyMobs = false;
							
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
											
											spawnedAnyMobs = true;
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
							
							if (spawnedAnyMobs)
							{
								timeSinceLastSpawn = 0;
								playHorn();
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
