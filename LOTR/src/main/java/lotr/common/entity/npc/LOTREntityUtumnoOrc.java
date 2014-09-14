package lotr.common.entity.npc;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityUtumnoOrc extends LOTREntityOrc
{
	public LOTREntityUtumnoOrc(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		isWeakOrc = false;
	}
	
	@Override
	public EntityAIBase createOrcAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.5D, true);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(6);
		
		if (i == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.scimitarOrc));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeOrc));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerOrc));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerOrcPoisoned));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearOrc));
		}
		else if (i == 5)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerOrc));
		}
		
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsUtumno));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsUtumno));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyUtumno));
		
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetUtumno));
		}
		
		return data;
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.UTUMNO;
	}
	
	@Override
	protected float getSoundPitch()
	{
		return super.getSoundPitch() * 0.65F;
	}
	
	@Override
	public boolean canPickUpLoot()
	{
		return false;
	}
	
	@Override
	public boolean canOrcSkirmish()
	{
		return false;
	}
}
