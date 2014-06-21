package lotr.common.world.biome;

import net.minecraft.block.Block;

public class LOTRBiomeGenBeach extends LOTRBiome
{
	public LOTRBiomeGenBeach(int i)
	{
		super(i);
		
		setMinMaxHeight(0.1F, 0F);
		setTemperatureRainfall(0.8F, 0.4F);
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		
		spawnableEvilList.clear();
	}
	
	public LOTRBiomeGenBeach setBeachBlock(Block block)
	{
		topBlock = block;
		fillerBlock = block;
		return this;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
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
