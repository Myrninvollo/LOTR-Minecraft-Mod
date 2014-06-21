package lotr.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LOTRTileEntityMobSpawner extends TileEntity
{
    public int delay = -1;
    private String mobID = "";
    public double yaw;
    public double prevYaw = 0D;
	private Entity spawnedMob;
	
	public int active = 1;
	public boolean spawnsPersistentNPCs = false;
    public int minSpawnDelay = 200;
    public int maxSpawnDelay = 800;
    public int nearbyMobLimit = 6;
	public int nearbyMobCheckRange = 8;
    public int requiredPlayerRange = 16;
    public int maxSpawnCount = 4;
    public int maxSpawnRange = 4;
	public int maxSpawnRangeVertical = 1;
	public int maxHealth = 20;
	public int navigatorRange = 16;
	public float moveSpeed = 0.2F;
	public int attackDamage = 2;
	private NBTTagCompound customSpawnData;

    public LOTRTileEntityMobSpawner()
    {
        delay = 20;
    }

    public String getMobID()
    {
        return mobID;
    }
    
    public void setMobID(int i)
    {
    	setMobID(LOTREntities.getStringFromID(i));
    }

    public void setMobID(String s)
    {
        mobID = s;
        
		if (!worldObj.isRemote)
		{
			Entity entity = EntityList.createEntityByName(LOTREntities.getFullEntityName(getMobID()), worldObj);
			if (entity != null && entity instanceof EntityLiving)
			{
				EntityLiving entityliving = (EntityLiving)entity;
				
				if (entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth) != null)
				{
					maxHealth = (int)entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).getBaseValue();
				}
				
				if (entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) != null)
				{
					navigatorRange = (int)entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange).getBaseValue();
				}
				
				if (entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) != null)
				{
					moveSpeed = (float)entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed).getBaseValue();
				}
				
				if (entityliving.getAttributeMap().getAttributeInstance(LOTREntityNPC.npcAttackDamage) != null)
				{
					attackDamage = (int)entityliving.getAttributeMap().getAttributeInstance(LOTREntityNPC.npcAttackDamage).getBaseValue();
				}
			}
		}
    }

    public boolean anyPlayerInRange()
    {
        return worldObj.getClosestPlayer((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, (double)requiredPlayerRange) != null;
    }
	
	public boolean isActive()
	{
		if (active == 1)
		{
			return true;
		}
		
		if (active == 2)
		{
			return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		}
		
		return false;
	}

    @Override
    public void updateEntity()
    {
        if (anyPlayerInRange() && isActive())
        {
            if (worldObj.isRemote)
            {
                double d = (double)((float)xCoord + worldObj.rand.nextFloat());
                double d1 = (double)((float)yCoord + worldObj.rand.nextFloat());
                double d2 = (double)((float)zCoord + worldObj.rand.nextFloat());
                worldObj.spawnParticle("smoke", d, d1, d2, 0D, 0D, 0D);
                worldObj.spawnParticle("flame", d, d1, d2, 0D, 0D, 0D);

                if (delay > 0)
                {
                    delay--;
                }

                prevYaw = yaw;
                yaw = (yaw + (double)(1000F / ((float)delay + 200F))) % 360D;
            }
            else
            {
                if (delay == -1)
                {
                    updateDelay();
                }

                if (delay > 0)
                {
                    delay--;
                    return;
                }

                boolean needsDelayUpdate = false;

				spawnLoop:
                for (int i = 0; i < maxSpawnCount; i++)
                {
                    Entity entity = EntityList.createEntityByName(LOTREntities.getFullEntityName(getMobID()), worldObj);
                    if (entity == null)
                    {
                        return;
                    }

                    List nearbyEntitiesList = worldObj.getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getAABBPool().getAABB((double)xCoord, (double)yCoord, (double)zCoord, (double)(xCoord + 1), (double)(yCoord + 1), (double)(zCoord + 1)).expand((double)nearbyMobCheckRange, (double)nearbyMobCheckRange, (double)nearbyMobCheckRange));
					List nearbySameEntitiesList = new ArrayList();
					for (int l = 0; l < nearbyEntitiesList.size(); l++)
					{
						Entity nearbyEntity = (Entity)nearbyEntitiesList.get(l);
						if (nearbyEntity.getClass() == entity.getClass())
						{
							nearbySameEntitiesList.add(nearbyEntity);
						}
					}
					
					int nearbyEntities = nearbySameEntitiesList.size();
                    if (nearbyEntities >= nearbyMobLimit)
                    {
                        updateDelay();
                        return;
                    }

                    if (entity != null)
                    {
                        double d = (double)xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)maxSpawnRange;
                        double d1 = (double)yCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)maxSpawnRangeVertical;
                        double d2 = (double)zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)maxSpawnRange;
                        EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                        entity.setLocationAndAngles(d, d1, d2, worldObj.rand.nextFloat() * 360F, 0F);
						if (entityliving instanceof LOTREntityNPC)
						{
							((LOTREntityNPC)entityliving).isNPCPersistent = spawnsPersistentNPCs;
							((LOTREntityNPC)entityliving).liftSpawnRestrictions = true;
						}

                        if (entityliving == null || entityliving.getCanSpawnHere())
                        {
							if (entityliving instanceof LOTREntityNPC)
							{
								((LOTREntityNPC)entityliving).liftSpawnRestrictions = false;
							}
							
                            writeNBTTagsTo(entity);
							if (entity instanceof LOTREntityNPC)
							{
								((LOTREntityNPC)entity).onArtificalSpawn();
							}
                            worldObj.spawnEntityInWorld(entity);
                            worldObj.playAuxSFX(2004, xCoord, yCoord, zCoord, 0);

                            if (entityliving != null)
                            {
                                entityliving.spawnExplosionParticle();
                            }

                            needsDelayUpdate = true;
							
							nearbyEntities++;
							if (nearbyEntities >= nearbyMobLimit)
							{
								break spawnLoop;
							}
                        }
                    }
                }

                if (needsDelayUpdate)
                {
                    updateDelay();
                }
            }

            super.updateEntity();
        }
    }

    public void writeNBTTagsTo(Entity entity)
    {
        if (entity instanceof EntityLiving && entity.worldObj != null)
        {
			EntityLiving entityliving = (EntityLiving)entity;
			if (!worldObj.isRemote)
			{
				if (entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth) != null)
				{
					entityliving.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)maxHealth);
				}
				
				if (entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) != null)
				{
					entityliving.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue((double)navigatorRange);
				}
				
				if (entityliving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) != null)
				{
					entityliving.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)moveSpeed);
				}
				
				if (entityliving.getAttributeMap().getAttributeInstance(LOTREntityNPC.npcAttackDamage) != null)
				{
					entityliving.getEntityAttribute(LOTREntityNPC.npcAttackDamage).setBaseValue((double)attackDamage);
				}
			}
			entityliving.onSpawnWithEgg(null);
			
			if (customSpawnData != null)
			{
				entityliving.readFromNBT(customSpawnData);
			}
        }
    }

    private void updateDelay()
    {
        if (maxSpawnDelay <= minSpawnDelay)
        {
            delay = minSpawnDelay;
        }
        else
        {
            delay = minSpawnDelay + worldObj.rand.nextInt(maxSpawnDelay - minSpawnDelay);
        }

        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, 0);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        
        if (nbt.hasKey("EntityID", 3))
        {
        	int id = nbt.getInteger("EntityID");
        	mobID = LOTREntities.getStringFromID(id);
        }
        else
        {
        	mobID = nbt.getString("EntityID");
        }
        		
        delay = nbt.getShort("Delay");

        if (nbt.hasKey("MinSpawnDelay"))
        {
            minSpawnDelay = nbt.getShort("MinSpawnDelay");
            maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            maxSpawnCount = nbt.getShort("MaxSpawnCount");
        }

        if (nbt.hasKey("NearbyMobLimit"))
        {
            nearbyMobLimit = nbt.getShort("NearbyMobLimit");
            requiredPlayerRange = nbt.getShort("RequiredPlayerRange");
        }

        if (nbt.hasKey("MaxSpawnRange"))
        {
            maxSpawnRange = nbt.getShort("MaxSpawnRange");
        }
		
        if (nbt.hasKey("MaxSpawnRangeVertical"))
        {
            maxSpawnRangeVertical = nbt.getShort("MaxSpawnRangeVertical");
        }
		
        if (nbt.hasKey("SpawnsPersistentNPCs"))
        {
            spawnsPersistentNPCs = nbt.getBoolean("SpawnsPersistentNPCs");
			active = nbt.getByte("ActiveMode");
			nearbyMobCheckRange = nbt.getShort("MobCheckRange");
        }
		
		if (nbt.hasKey("MaxHealth"))
		{
			maxHealth = nbt.getShort("MaxHealth");
			navigatorRange = nbt.getShort("NavigatorRange");
			moveSpeed = nbt.getFloat("MoveSpeed");
			attackDamage = nbt.getShort("AttackDamage");
		}
		
		if (nbt.hasKey("CustomSpawnData"))
		{
			customSpawnData = nbt.getCompoundTag("CustomSpawnData");
		}

        if (worldObj != null && worldObj.isRemote)
        {
            spawnedMob = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setString("EntityID", getMobID());
        nbt.setShort("Delay", (short)delay);
        nbt.setShort("MinSpawnDelay", (short)minSpawnDelay);
        nbt.setShort("MaxSpawnDelay", (short)maxSpawnDelay);
        nbt.setShort("MaxSpawnCount", (short)maxSpawnCount);
        nbt.setShort("NearbyMobLimit", (short)nearbyMobLimit);
        nbt.setShort("RequiredPlayerRange", (short)requiredPlayerRange);
        nbt.setShort("MaxSpawnRange", (short)maxSpawnRange);
		nbt.setShort("MaxSpawnRangeVertical", (short)maxSpawnRangeVertical);
		nbt.setBoolean("SpawnsPersistentNPCs", spawnsPersistentNPCs);
		nbt.setByte("ActiveMode", (byte)active);
		nbt.setShort("MobCheckRange", (short)nearbyMobCheckRange);
		nbt.setShort("MaxHealth", (short)maxHealth);
		nbt.setShort("NavigatorRange", (short)navigatorRange);
		nbt.setFloat("MoveSpeed", moveSpeed);
		nbt.setShort("AttackDamage", (short)attackDamage);
		
		if (customSpawnData != null)
		{
			nbt.setTag("CustomSpawnData", customSpawnData);
		}
    }

    public Entity getMobEntity(World world)
    {
        if (spawnedMob == null)
        {
            Entity entity = EntityList.createEntityByName(LOTREntities.getFullEntityName(getMobID()), world);
			if (entity instanceof LOTREntityNPC)
			{
				((LOTREntityNPC)entity).onArtificalSpawn();
			}
            writeNBTTagsTo(entity);
            spawnedMob = entity;
        }

        return spawnedMob;
    }

	@Override
    public Packet getDescriptionPacket()
    {
		NBTTagCompound data = new NBTTagCompound();
		writeToNBT(data);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, data);
    }
	
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet)
	{
		NBTTagCompound data = packet.func_148857_g();
		readFromNBT(data);
	}

    @Override
    public boolean receiveClientEvent(int i, int j)
    {
        if (i == 1 && worldObj.isRemote)
        {
            delay = minSpawnDelay;
			return true;
        }
        if (i == 2 && worldObj.isRemote)
        {
            delay = -1;
			return true;
        }
		return false;
    }
}
