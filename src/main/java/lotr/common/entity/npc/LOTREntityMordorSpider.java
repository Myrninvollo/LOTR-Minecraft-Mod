package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LOTREntityMordorSpider extends LOTREntitySpiderBase
{
	public LOTREntityMordorSpider(World world)
	{
		super(world);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
		tasks.addTask(3, new LOTREntityAIAttackOnCollide(this, 1.2D, false, 0.8F));
		tasks.addTask(4, new LOTREntityAIFollowHiringPlayer(this));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F, 0.02F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4);
	}
	
	@Override
	protected int getRandomSpiderScale()
	{
		return 1 + rand.nextInt(3);
	}
	
	@Override
	protected int getRandomSpiderType()
	{
		return VENOM_POISON;
	}
	
	@Override
	public IEntityLivingData initCreatureForHire(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		return data;
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		if (!worldObj.isRemote && rand.nextInt(3) == 0)
		{
			LOTREntityNPC rider = rand.nextBoolean() ? new LOTREntityMordorOrcArcher(worldObj) : new LOTREntityMordorOrc(worldObj);
			rider.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0F);
			rider.onSpawnWithEgg(null);
			rider.isNPCPersistent = isNPCPersistent;
			worldObj.spawnEntityInWorld(rider);
			rider.mountEntity(this);
		}
		return data;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.MORDOR;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.MORDOR_SPIDER_BONUS;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killMordorSpider;
	}
}
