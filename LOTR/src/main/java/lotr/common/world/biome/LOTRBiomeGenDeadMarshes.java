package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.feature.LOTRWorldGenMarshLights;
import lotr.common.world.genlayer.LOTRGenLayerWorld;
import lotr.common.world.structure.LOTRWorldGenMarshHut;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenDeadMarshes extends LOTRBiome
{
	private WorldGenerator remainsGen = new WorldGenMinable(LOTRMod.remains, 6, Blocks.dirt);
	
	public LOTRBiomeGenDeadMarshes(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		
		spawnableEvilList.clear();
		
		decorator.sandPerChunk = 0;
		decorator.sandPerChunk2 = 0;
		decorator.quagmirePerChunk = 2;
		decorator.treesPerChunk = 0;
		decorator.logsPerChunk = 2;
		decorator.flowersPerChunk = 3;
		decorator.grassPerChunk = 4;
		decorator.doubleGrassPerChunk = 4;
		decorator.enableFern = true;
		decorator.reedsPerChunk = 10;
		
		flowers.clear();
		addFlower(LOTRMod.deadPlant, 0, 10);
		
		biomeColors.setGrass(0x7F644F);
		biomeColors.setSky(0x565332);
		biomeColors.setClouds(0xA09B66);
		biomeColors.setFog(0x404024);
		biomeColors.setWater(0x382618);
		
		setBanditChance(LOTRBanditSpawner.NEVER);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.MORDOR, LOTRInvasionSpawner.RARE));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterDeadMarshes;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.NINDALF;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 2, remainsGen, 56, 66);
		
        super.decorate(world, random, i, k);
		
		for (int l = 0; l < 6; l++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			new WorldGenLakes(Blocks.water).generate(world, random, i1, j1, k1);
		}
		
        if (random.nextInt(3) == 0)
        {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1;
			for (j1 = 128; j1 > 0 && world.isAirBlock(i1, j1 - 1, k1); j1--) {}
			new LOTRWorldGenMarshLights().generate(world, random, i1, j1, k1);
        }
		
		if (i == 600 * LOTRGenLayerWorld.scale && k == 400 * LOTRGenLayerWorld.scale)
		{
			int i1 = i + 8;
			int k1 = k + 8;
			int j1;
			for (j1 = 128; j1 > 60; j1--)
			{
				Block block = world.getBlock(i1, j1, k1);
				if (block != Blocks.dirt && block != Blocks.grass)
				{
					world.setBlock(i1, j1, k1, Blocks.air, 0, 2);
				}
				if (block == Blocks.water)
				{
					world.setBlock(i1, j1, k1, Blocks.dirt, 0, 2);
				}
			}
			j1 = world.getHeightValue(i1, k1);
			new LOTRWorldGenMarshHut().generate(world, random, i1, j1, k1);
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		return LOTRWorldGenDeadTrees.newOak();
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
}
