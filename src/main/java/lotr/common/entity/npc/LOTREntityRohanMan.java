package lotr.common.entity.npc;

import lotr.common.LOTRFaction;
import lotr.common.world.biome.LOTRBiomeGenRohan;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class LOTREntityRohanMan extends LOTREntityNPC
{
	public LOTREntityRohanMan(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, LOTRNames.getRandomRohanName(rand));
	}
	
	public String getRohanName()
	{
		return dataWatcher.getWatchableObjectString(16);
	}
	
	public void setRohanName(String name)
	{
		dataWatcher.updateObject(16, name);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.ROHAN;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public String getNPCName()
	{
		return getRohanName();
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
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("RohanName", getRohanName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("RohanName"))
		{
			setRohanName(nbt.getString("RohanName"));
		}
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenRohan)
		{
			f += 20F;
		}
		return f;
	}
}
