package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemSpear;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class LOTREntityGondorTowerGuard extends LOTREntityGondorSoldier
{
	private static ResourceLocation towerGuardCape = new ResourceLocation("lotr:mob/gondor/towerGuard_cape.png");
	
	public LOTREntityGondorTowerGuard(World world)
	{
		super(world);
		spawnRidingHorse = false;
		npcCape = towerGuardCape;
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(24D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.24D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetGondorWinged));
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearGondor));
		return data;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote)
		{
			ItemStack itemstack = getHeldItem();
			if (itemstack != null && itemstack.getItem() == ((LOTRItemSpear)LOTRMod.spearGondor).swordReplacement)
			{
				setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearGondor));
			}
		}
	}
}
