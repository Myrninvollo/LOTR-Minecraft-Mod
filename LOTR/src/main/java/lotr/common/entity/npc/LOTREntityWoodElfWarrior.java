package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.animal.LOTREntityElk;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityWoodElfWarrior extends LOTREntityWoodElfScout
{
	public LOTREntityWoodElfWarrior(World world)
	{
		super(world);
		tasks.removeTask(rangedAttackAI);
		tasks.addTask(2, meleeAttackAI);
		spawnRidingHorse = rand.nextInt(4) == 0;
	}
	
	@Override
	public LOTRNPCMount createMountToRide()
	{
		return new LOTREntityElk(worldObj);
	}
	
	@Override
	public EntityAIBase createElfMeleeAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		if (rand.nextInt(5) == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearWoodElven));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		}
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsWoodElven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsWoodElven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyWoodElven));
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetWoodElven));
		}
		else
		{
			setCurrentItemOrArmor(4, null);
		}
		return data;
    }
	
	@Override
	public Item getElfSwordId()
	{
		return LOTRMod.swordWoodElven;
	}
	
	@Override
	public void onAttackModeChange(AttackMode mode)
	{
		if (mode == AttackMode.IDLE)
		{
			setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		}
		else
		{
			super.onAttackModeChange(mode);
		}
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.WOOD_ELF_WARRIOR_BONUS;
	}
}
