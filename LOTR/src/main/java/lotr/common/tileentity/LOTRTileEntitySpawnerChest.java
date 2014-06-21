package lotr.common.tileentity;

import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityGondorRuinsWraith;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntityChest;

public class LOTRTileEntitySpawnerChest extends TileEntityChest
{
	private String mobID = "";
	
	public void setMobID(Class entityClass)
	{
		mobID = LOTREntities.getStringFromClass(entityClass);
	}
	
	public Entity createMob()
	{
		return EntityList.createEntityByName(LOTREntities.getFullEntityName(mobID), worldObj);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("MobID", 3))
        {
        	int id = nbt.getInteger("MobID");
        	mobID = LOTREntities.getStringFromID(id);
        }
        else
        {
        	mobID = nbt.getString("MobID");
        }
	}

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
		nbt.setString("MobID", mobID);
	}
}
