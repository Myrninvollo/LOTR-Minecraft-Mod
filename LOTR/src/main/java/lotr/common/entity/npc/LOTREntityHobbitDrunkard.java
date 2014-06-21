package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class LOTREntityHobbitDrunkard extends LOTREntityHobbit
{
	public LOTREntityHobbitDrunkard(World world)
	{
		super(world);
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		Item drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(rand).getItem();
		setCurrentItemOrArmor(0, new ItemStack(drink, 1, 3 + rand.nextInt(2)));
		return data;
	}
	
	@Override
	public void onHobbitUpdate()
	{
		if (!worldObj.isRemote)
		{
			addPotionEffect(new PotionEffect(Potion.confusion.id, 20));
		}
	}
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (super.interact(entityplayer))
		{
			if (!worldObj.isRemote && entityplayer.isPotionActive(Potion.confusion.id))
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.speakToDrunkard);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void dropHobbitItems(boolean flag, int i)
	{
		if (getEquipmentInSlot(0) != null)
		{
			entityDropItem(getEquipmentInSlot(0), 0F);
		}
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return "hobbitDrunkard";
	}
	
	@Override
	public boolean isDrunkard()
	{
		return true;
	}
}
