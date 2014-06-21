package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenCharredTrees;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenDagorlad extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 2).setSpawnBlock(Blocks.gravel, 0);
	
	public LOTRBiomeGenDagorlad(int i)
	{
		super(i);
		
		topBlock = Blocks.gravel;
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		
		spawnableEvilList.clear();
		
		decorator.treesPerChunk = 0;
		decorator.flowersPerChunk = 0;
		decorator.grassPerChunk = 0;
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterDagorlad;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.DAGORLAD;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(24) == 0)
		{
			int boulders = 1 + random.nextInt(4);
			for (int l = 0; l < boulders; l++)
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
		return new LOTRWorldGenCharredTrees();
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.05F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0F;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float f)
    {
		return 0x54493D;
    }
	
	@Override
	public Vec3 getCloudColor(Vec3 clouds)
	{
		clouds.xCoord *= 0.2D;
		clouds.yCoord *= 0.2D;
		clouds.zCoord *= 0.2D;
		return clouds;
	}
	
	@Override
	public Vec3 getFogColor(Vec3 fog)
	{
		fog.xCoord *= 0.4D;
		fog.yCoord *= 0.4D;
		fog.zCoord *= 0.4D;
		return fog;
	}
}