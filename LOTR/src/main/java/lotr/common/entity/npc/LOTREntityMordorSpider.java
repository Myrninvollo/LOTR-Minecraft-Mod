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
		return LOTRAlignmentValues.Bonuses.MORDOR_SPIDER;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killMordorSpider;
	}
}
