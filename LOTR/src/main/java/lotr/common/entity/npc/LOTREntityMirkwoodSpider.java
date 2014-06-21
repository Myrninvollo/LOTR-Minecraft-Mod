package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LOTREntityMirkwoodSpider extends LOTREntitySpiderBase
{
	public LOTREntityMirkwoodSpider(World world)
	{
		super(world);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAILeapAtTarget(this, 0.4F));
		tasks.addTask(2, new LOTREntityAIAttackOnCollide(this, 1.2D, false, 0.8F));
        tasks.addTask(3, new EntityAIWander(this, 1D));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8F, 0.02F));
        tasks.addTask(5, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        addTargetTasks(2);
	}
	
	@Override
	protected int getRandomSpiderScale()
	{
		return rand.nextInt(3);
	}
	
	@Override
	protected int getRandomSpiderType()
	{
		return rand.nextBoolean() ? 0 : 1 + rand.nextInt(2);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.DOL_GULDUR;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.MIRKWOOD_SPIDER_BONUS;
	}
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        super.dropFewItems(flag, i);
		
		if (flag && rand.nextInt(6) == 0)
		{
			dropItem(LOTRMod.mysteryWeb, 1);
		}
    }
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killMirkwoodSpider;
	}
}
