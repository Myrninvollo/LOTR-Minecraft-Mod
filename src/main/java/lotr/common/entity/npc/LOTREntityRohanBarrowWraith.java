package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityRohanBarrowWraith extends LOTREntitySkeletalWraith
{
	public LOTREntityRohanBarrowWraith(World world)
	{
		super(world);
	}

	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordRohan));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsRohan));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsRohan));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyRohan));
		return data;
    }
}
