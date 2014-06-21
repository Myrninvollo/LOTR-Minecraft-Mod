package lotr.common.entity.npc;

import lotr.common.LOTRFaction;
import lotr.common.world.biome.LOTRBiomeGenGondor;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class LOTREntityGondorMan extends LOTREntityNPC
{
	public LOTREntityGondorMan(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, LOTRNames.getRandomGondorName(rand));
	}
	
	public String getGondorianName()
	{
		return dataWatcher.getWatchableObjectString(16);
	}
	
	public void setGondorianName(String name)
	{
		dataWatcher.updateObject(16, name);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.GONDOR;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public String getNPCName()
	{
		return getGondorianName();
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
		nbt.setString("GondorName", getGondorianName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("GondorName"))
		{
			setGondorianName(nbt.getString("GondorName"));
		}
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenGondor)
		{
			f += 20F;
		}
		return f;
	}
}
