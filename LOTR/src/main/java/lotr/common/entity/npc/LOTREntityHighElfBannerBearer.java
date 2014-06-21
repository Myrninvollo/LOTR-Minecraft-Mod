package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemBanner;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHighElfBannerBearer extends LOTREntityHighElfWarrior implements LOTRBannerBearer
{
	public LOTREntityHighElfBannerBearer(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
	}

	@Override
	public Item getWeaponItem()
	{
		return LOTRMod.daggerHighElven;
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.banner, 1, LOTRItemBanner.getSubtypeForFaction(getFaction())));
		return data;
	}
	
	@Override
	public void onElfUpdate() {}
}
