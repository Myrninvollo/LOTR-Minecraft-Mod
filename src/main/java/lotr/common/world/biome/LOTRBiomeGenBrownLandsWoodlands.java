package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenLogs;
import net.minecraft.world.World;

public class LOTRBiomeGenBrownLandsWoodlands extends LOTRBiomeGenBrownLands
{
	public LOTRBiomeGenBrownLandsWoodlands(int i)
	{
		super(i);
		
		decorator.treesPerChunk = 4;
		decorator.logsPerChunk = 3;
		decorator.grassPerChunk = 2;
		decorator.doubleGrassPerChunk = 1;
	}
}
