package lotr.common.world.biome;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.structure.LOTRWorldGenRuinedDunedainTower;
import lotr.common.world.structure.LOTRWorldGenStoneRuin;
import lotr.common.world.structure2.LOTRWorldGenBDBarrow;
import net.minecraft.util.Vec3;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class LOTRBiomeGenBarrowDowns extends LOTRBiome
{
	public LOTRBiomeGenBarrowDowns(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityHorse.class, 8, 2, 6));
		
		spawnableEvilList.clear();
		
		setGoodEvilWeight(0, 100);
		
		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 6;
		decorator.generateAthelas = true;
		
        registerPlainsFlowers();
		
		decorator.generateOrcDungeon = true;
		
		decorator.addRandomStructure(new LOTRWorldGenBDBarrow(false), 5);
		decorator.addRandomStructure(new LOTRWorldGenRuinedDunedainTower(false), 500);
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin(2, 7), 15);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterBarrowDowns;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.ERIADOR;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(20) == 0)
		{
			return new WorldGenForest(false, false);
		}
		else if (random.nextInt(5) == 0)
		{
			return new WorldGenTaiga2(false);
		}
		else if (random.nextInt(3) > 0)
		{
			return LOTRWorldGenDeadTrees.newOak();
		}
		else
		{
			return super.func_150567_a(random);
		}
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.1F;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.2F;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int i, int j, int k)
    {
        return 0x718E59;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor(int i, int j, int k)
    {
        return 0x868766;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float f)
    {
		return 0x8CA088;
    }
	
	@Override
	public Vec3 getCloudColor(Vec3 clouds)
	{
		clouds.xCoord *= 0.7D;
		clouds.yCoord *= 0.7D;
		clouds.zCoord *= 0.7D;
		return clouds;
	}
	
	@Override
	public Vec3 getFogColor(Vec3 fog)
	{
		fog.xCoord *= 0.6D;
		fog.yCoord *= 0.6D;
		fog.zCoord *= 0.6D;
		return fog;
	}
}
