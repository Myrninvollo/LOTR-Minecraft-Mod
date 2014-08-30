package lotr.common.world.biome;

import lotr.common.world.LOTRBanditSpawner;
import net.minecraft.block.Block;

public class LOTRBiomeGenBeach extends LOTRBiomeGenOcean
{
	public LOTRBiomeGenBeach(int i)
	{
		super(i);
		
		setMinMaxHeight(0.1F, 0F);
		setTemperatureRainfall(0.8F, 0.4F);
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
	}
	
	public LOTRBiomeGenBeach setBeachBlock(Block block)
	{
		topBlock = block;
		fillerBlock = block;
		return this;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0F;
	}
}
