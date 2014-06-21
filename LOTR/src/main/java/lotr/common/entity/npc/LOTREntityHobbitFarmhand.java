package lotr.common.entity.npc;

import lotr.common.entity.ai.LOTREntityAIFarm;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class LOTREntityHobbitFarmhand extends LOTREntityHobbit
{
	public Item seedsItem;
	private LOTREntityAIFarm farmAI;
	
	public LOTREntityHobbitFarmhand(World world)
	{
		super(world);
		tasks.addTask(5, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(6, (farmAI = new LOTREntityAIFarm(this, 1D, (IPlantable)Items.wheat_seeds)));
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(Items.iron_hoe));
		return data;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && !hiredNPCInfo.isActive)
		{
			if (seedsItem != null)
			{
				farmAI.defaultSeedsItem = (IPlantable)seedsItem;
			}
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		
		if (seedsItem != null)
		{
			nbt.setInteger("SeedsID", Item.getIdFromItem(seedsItem));
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
	
		if (nbt.hasKey("SeedsID"))
		{
			Item item = Item.getItemById(nbt.getInteger("SeedsID"));
			if (item != null)
			{
				seedsItem = item;
			}
		}
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (hiredNPCInfo.getHiringPlayer() == entityplayer)
		{
			return "hobbitFarmhand_hired";
		}
		else
		{
			return super.getSpeechBank(entityplayer);
		}
	}
}
