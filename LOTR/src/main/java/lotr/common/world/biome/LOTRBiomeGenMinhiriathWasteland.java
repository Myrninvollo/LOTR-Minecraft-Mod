package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenMinhiriathWasteland extends LOTRBiomeGenMinhiriath
{
	public LOTRBiomeGenMinhiriathWasteland(int i)
	{
		super(i);

		decorator.grassPerChunk = 2;
		decorator.doubleGrassPerChunk = 0;
		decorator.flowersPerChunk = 0;
		
		biomeColors.setGrass(0xAEB269);
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(4) == 0)
		{
			for (int l = 0; l < 3; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
	}

	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(5) == 0)
		{
			return super.func_150567_a(random);
		}
		else
		{
			return LOTRWorldGenDeadTrees.newOak();
		}
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
}
