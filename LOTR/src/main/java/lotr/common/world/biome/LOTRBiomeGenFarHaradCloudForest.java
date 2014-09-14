package lotr.common.world.biome;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;

public class LOTRBiomeGenFarHaradCloudForest extends LOTRBiomeGenFarHaradJungle
{
	public LOTRBiomeGenFarHaradCloudForest(int i)
	{
		super(i);

		decorator.treesPerChunk = 10;
		
		biomeColors.setGrass(0x208E4E);
		biomeColors.setFoliage(0x05722A);
		biomeColors.setSky(0xAEC1BB);
		biomeColors.setFoggy(true);
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(3) > 0)
		{
			return new WorldGenMegaJungle(false, 30, 30, 3, 3);
		}
		return super.func_150567_a(random);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return super.getChanceToSpawnAnimals() * 0.5F;
	}
}
