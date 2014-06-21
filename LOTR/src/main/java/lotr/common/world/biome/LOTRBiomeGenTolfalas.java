package lotr.common.world.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import lotr.common.world.structure.LOTRWorldGenGondorFortress;
import lotr.common.world.structure.LOTRWorldGenGondorObelisk;
import lotr.common.world.structure.LOTRWorldGenGondorRuin;
import lotr.common.world.structure.LOTRWorldGenGondorRuins;
import lotr.common.world.structure.LOTRWorldGenGondorSmithy;
import lotr.common.world.structure.LOTRWorldGenRuinedGondorTower;
import lotr.common.world.structure2.LOTRWorldGenGondorTurret;

public class LOTRBiomeGenTolfalas extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 6);
	
	public LOTRBiomeGenTolfalas(int i)
	{
		super(i);
		
		spawnableGoodList.clear();
		spawnableEvilList.clear();
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 0;
		decorator.doubleFlowersPerChunk = 0;
		decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 6;
		decorator.generateAthelas = true;
		
		decorator.addRandomStructure(new LOTRWorldGenGondorRuins(), 1000);
		decorator.addRandomStructure(new LOTRWorldGenRuinedGondorTower(false), 1500);
		decorator.addRandomStructure(new LOTRWorldGenGondorObelisk(false), 1500);
		decorator.addRandomStructure(new LOTRWorldGenGondorRuin(false), 1500);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterTolfalas;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.TOLFALAS;
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
		if (random.nextInt(3) > 0)
		{
			return LOTRWorldGenDeadTrees.newOak();
		}
        return super.func_150567_a(random);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.25F;
	}
}
