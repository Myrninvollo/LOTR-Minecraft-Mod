package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityAngmarOrc;
import lotr.common.entity.npc.LOTREntityAngmarOrcArcher;
import lotr.common.entity.npc.LOTREntityAngmarOrcBombardier;
import lotr.common.entity.npc.LOTREntityAngmarOrcWarrior;
import lotr.common.entity.npc.LOTREntityAngmarWarg;
import lotr.common.entity.npc.LOTREntityMountainTroll;
import lotr.common.entity.npc.LOTREntityTroll;
import lotr.common.world.feature.LOTRWorldGenBlastedLand;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenCharredTrees;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.structure.LOTRWorldGenAngmarRuin;
import lotr.common.world.structure.LOTRWorldGenAngmarShrine;
import lotr.common.world.structure.LOTRWorldGenAngmarTower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenAngmar extends LOTRBiome
{
	private WorldGenerator stonePatchGen = new WorldGenMinable(Blocks.stone, 0, 50, Blocks.grass);
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 2);
    private WorldGenerator morgulIronGen = new WorldGenMinable(LOTRMod.oreMorgulIron, 8);
	private WorldGenerator guldurilGen = new WorldGenMinable(LOTRMod.oreGulduril, 8);
	
	public LOTRBiomeGenAngmar(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 10, 4, 8));

		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityAngmarOrc.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityAngmarOrcArcher.class, 10, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityAngmarOrcBombardier.class, 5, 1, 2));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityAngmarOrcWarrior.class, 15, 4, 4));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityTroll.class, 40, 1, 3));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMountainTroll.class, 30, 1, 3));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityAngmarWarg.class, 30, 4, 4));
		
		decorator.flowersPerChunk = 0;
		decorator.grassPerChunk = 2;
		decorator.doubleGrassPerChunk = 1;
		
		decorator.generateOrcDungeon = true;
		decorator.generateTrollHoard = true;
		
		decorator.addRandomStructure(new LOTRWorldGenAngmarTower(false), 400);
		decorator.addRandomStructure(new LOTRWorldGenAngmarRuin(), 30);
		decorator.addRandomStructure(new LOTRWorldGenBlastedLand(), 40);
		decorator.addRandomStructure(new LOTRWorldGenAngmarShrine(false), 200);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterAngmar;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.ANGMAR;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 8, stonePatchGen, 60, 100);
        genStandardOre(world, random, i, k, 20, morgulIronGen, 0, 64);
		genStandardOre(world, random, i, k, 8, guldurilGen, 0, 32);
		
        super.decorate(world, random, i, k);
		
		for (int l = 0; l < 4; l++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			if (j1 > 80)
			{
				func_150567_a(random).generate(world, random, i1, j1, k1);
			}
		}
		
		if (random.nextInt(6) == 0)
		{
			int i1 = i + random.nextInt(16) + 8;
			int k1 = k + random.nextInt(16) + 8;
			boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextBoolean())
		{
			if (random.nextBoolean())
			{
				return new LOTRWorldGenCharredTrees();
			}
			else
			{
				return LOTRWorldGenDeadTrees.newSpruce();
			}
		}
		return random.nextInt(3) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2(false);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.1F;
	}
	
	@Override
	public boolean canSpawnHostilesInDay()
	{
		return true;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0.25F;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int i, int j, int k)
    {
        return 0x787C57;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float f)
    {
		return 0x513F33;
    }
	
	@Override
	public Vec3 getCloudColor(Vec3 clouds)
	{
		clouds.xCoord *= 0.1D;
		clouds.yCoord *= 0.1D;
		clouds.zCoord *= 0.1D;
		return clouds;
	}
	
	@Override
	public Vec3 getFogColor(Vec3 fog)
	{
		fog.xCoord *= 0.1D;
		fog.yCoord *= 0.1D;
		fog.zCoord *= 0.1D;
		return fog;
	}
}
