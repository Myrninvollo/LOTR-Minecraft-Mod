package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityBlueDwarfMerchant;
import lotr.common.world.feature.LOTRWorldGenLarch;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenRedMountains extends LOTRBiome
{
	private WorldGenerator redRockVein = new WorldGenMinable(LOTRMod.rock, 4, 60, Blocks.stone);
	
	public LOTRBiomeGenRedMountains(int i)
	{
		super(i);

		spawnableEvilList.clear();

		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 1;
		decorator.grassPerChunk = 4;
		decorator.doubleGrassPerChunk = 1;
		
		registerMountainsFlowers();
		
		registerTravellingTrader(LOTREntityBlueDwarfMerchant.class);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterRedMountains;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.RED_MOUNTAINS;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 6, redRockVein, 0, 96);

        super.decorate(world, random, i, k);
    }
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(4) == 0)
		{
			return new LOTRWorldGenLarch(false);
		}
		if (random.nextInt(4) > 0)
		{
			return new WorldGenTaiga2(false);
		}
		else
		{
			return super.func_150567_a(random);
		}
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.2F;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float f)
    {
		return 0xCEA092;
    }
}
