package lotr.common.entity.npc;

import lotr.common.LOTRFoods;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class LOTREntityDunlendingDrunkard extends LOTREntityDunlending
{
	public LOTREntityDunlendingDrunkard(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		Item drink = LOTRFoods.DUNLENDING_DRINK.getRandomFood(rand).getItem();
		setCurrentItemOrArmor(0, new ItemStack(drink, 1, 3 + rand.nextInt(2)));
		return data;
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote)
		{
			addPotionEffect(new PotionEffect(Potion.confusion.id, 20));
		}
	}
	
	@Override
	public void dropDunlendingItems(boolean flag, int i)
	{
		if (getEquipmentInSlot(0) != null)
		{
			entityDropItem(getEquipmentInSlot(0), 0F);
		}
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return "dunlendingDrunkard";
	}
	
	@Override
	public boolean isDrunkard()
	{
		return true;
	}
}
