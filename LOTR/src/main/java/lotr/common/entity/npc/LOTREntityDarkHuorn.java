package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetHuorn;
import lotr.common.world.biome.LOTRBiomeGenOldForest;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityDarkHuorn extends LOTREntityHuornBase
{
	public LOTREntityDarkHuorn(World world)
	{
		super(world);
		addTargetTasks(true, LOTREntityAINearestAttackableTargetHuorn.class);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		setTreeType(0);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.HOSTILE;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killDarkHuorn;
	}
	
	@Override
	protected boolean isTreeHomeBiome(BiomeGenBase biome)
	{
		return biome instanceof LOTRBiomeGenOldForest;
	}
}
