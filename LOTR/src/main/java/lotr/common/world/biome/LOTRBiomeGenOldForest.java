package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityDarkHuorn;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import net.minecraft.util.Vec3;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenOldForest extends LOTRBiome
{
	public LOTRBiomeGenOldForest(int i)
	{
		super(i);
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityDarkHuorn.class, 10, 4, 4));
		
		hasPodzol = true;
		decorator.treesPerChunk = 16;
		decorator.flowersPerChunk = 1;
		decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 5;
		decorator.enableFern = true;
		decorator.mushroomsPerChunk = 2;
		
		registerForestFlowers();
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterOldForest;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.OLD_FOREST;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(10) == 0)
		{
			return new WorldGenTrees(false, 16 + random.nextInt(8), 0, 0, false);
		}
		if (random.nextInt(3) == 0)
		{
			return new WorldGenCanopyTree(false);
		}
		if (random.nextInt(4) == 0)
		{
			return new WorldGenBigTree(false);
		}
		else if (random.nextInt(3) == 0)
		{
			return new WorldGenTrees(false);
		}
		else
		{
			return new WorldGenTrees(false, 8 + random.nextInt(8), 0, 0, false);
		}
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0F;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int i, int j, int k)
    {
		return 0x47823E;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor(int i, int j, int k)
    {
		return 0x30682A;
    }
	
	@Override
	public Vec3 getFogColor(Vec3 fog)
	{
		fog.xCoord *= 0.1D;
		fog.yCoord *= 0.2D;
		fog.zCoord *= 0.1D;
		return fog;
	}
	
	@Override
	public boolean hasFog()
	{
		return true;
	}
}
