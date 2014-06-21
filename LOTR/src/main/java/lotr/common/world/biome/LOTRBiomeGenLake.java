package lotr.common.world.biome;

import net.minecraft.block.Block;

public class LOTRBiomeGenLake extends LOTRBiome
{
	public LOTRBiomeGenLake(int i)
	{
		super(i);
		
		setMinMaxHeight(-0.5F, 0.2F);
		
		spawnableCreatureList.clear();
		
		spawnableEvilList.clear();
		
		decorator.sandPerChunk = 0;
		decorator.sandPerChunk2 = 0;
	}
	
	public LOTRBiomeGenLake setLakeBlock(Block block)
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
}
