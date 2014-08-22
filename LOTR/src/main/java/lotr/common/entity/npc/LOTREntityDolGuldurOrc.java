package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDolGuldurOrc extends LOTREntityOrc
{
	public LOTREntityDolGuldurOrc(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase createOrcAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		
		int i = rand.nextInt(8);
		
		if (i == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeDolGuldur));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerDolGuldur));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerDolGuldurPoisoned));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearDolGuldur));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordDolGuldur));
		}
		else if (i == 5)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerDolGuldur));
		}
		else if (i == 6)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.pickaxeDolGuldur));
		}
		else if (i == 7)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.axeDolGuldur));
		}
		
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsDolGuldur));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsDolGuldur));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyDolGuldur));
		
		if (rand.nextInt(5) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetDolGuldur));
		}

		if (!worldObj.isRemote && spawnRidingHorse && !(this instanceof LOTRBannerBearer))
		{
			LOTREntityMirkwoodSpider spider = new LOTREntityMirkwoodSpider(worldObj);
			spider.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0F);
			if (worldObj.func_147461_a(spider.boundingBox).isEmpty())
			{
				spider.onSpawnWithEgg(null);
				spider.isNPCPersistent = isNPCPersistent;
				worldObj.spawnEntityInWorld(spider);
				mountEntity(spider);
			}
		}
		
		return data;
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.DOL_GULDUR;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.DOL_GULDUR_ORC;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killDolGuldurOrc;
	}
}
