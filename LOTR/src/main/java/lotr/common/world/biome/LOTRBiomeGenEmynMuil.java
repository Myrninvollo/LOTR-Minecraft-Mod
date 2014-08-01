package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityMordorOrc;
import lotr.common.entity.npc.LOTREntityMordorOrcArcher;
import lotr.common.entity.npc.LOTREntityMordorOrcBombardier;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenGrassPatch;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenEmynMuil extends LOTRBiome
{
	private WorldGenerator boulderGenSmall = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 4).setSpawnBlock(Blocks.stone, 0);
	private WorldGenerator boulderGenLarge = new LOTRWorldGenBoulder(Blocks.stone, 0, 5, 8).setSpawnBlock(Blocks.stone, 0).setHeightCheck(6);
	private WorldGenerator clayBoulderGenSmall = new LOTRWorldGenBoulder(Blocks.hardened_clay, 0, 1, 4).setSpawnBlock(Blocks.stone, 0);
	private WorldGenerator clayBoulderGenLarge = new LOTRWorldGenBoulder(Blocks.hardened_clay, 0, 5, 10).setSpawnBlock(Blocks.stone, 0).setHeightCheck(6);
	private WorldGenerator grassPatchGen = new LOTRWorldGenGrassPatch();
	
	public LOTRBiomeGenEmynMuil(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrc.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrcArcher.class, 10, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrcBombardier.class, 3, 1, 2));
		
		decorator.flowersPerChunk = 1;
		decorator.grassPerChunk = 5;
		decorator.doubleGrassPerChunk = 2;
		
		registerMountainsFlowers();
		
		topBlock = Blocks.stone;
		fillerBlock = Blocks.stone;
		
		decorator.generateOrcDungeon = true;
		
		setBanditChance(LOTRBanditSpawner.COMMON);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.MORDOR, LOTRInvasionSpawner.RARE));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterEmynMuil;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.EMYN_MUIL;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		for (int l = 0; l < 20; l++)
		{
			int i1 = i + random.nextInt(16) + 8;
			int k1 = k + random.nextInt(16) + 8;
			if (random.nextInt(5) == 0)
			{
				clayBoulderGenSmall.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
			else
			{
				boulderGenSmall.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
		
		for (int l = 0; l < 20; l++)
		{
			int i1 = i + random.nextInt(16) + 8;
			int k1 = k + random.nextInt(16) + 8;
			if (random.nextInt(5) == 0)
			{
				clayBoulderGenLarge.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
			else
			{
				boulderGenLarge.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
		
		for (int l = 0; l < 10; l++)
		{
			Block block = Blocks.stone;
			if (random.nextInt(5) == 0)
			{
				block = Blocks.hardened_clay;
			}
			
			for (int l1 = 0; l1 < 10; l1++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				int j1 = world.getHeightValue(i1, k1);
				
				if (world.getBlock(i1, j1 - 1, k1) == block)
				{
					int height = j1 + random.nextInt(4);
					for (int j2 = j1; j2 < height; j2++)
					{
						if (LOTRMod.isOpaque(world, i1, j2, k1))
						{
							break;
						}
						
						world.setBlock(i1, j2, k1, block, 0, 3);
					}
				}
			}
		}
		
		for (int l = 0; l < 3; l++)
		{
			int i1 = i + random.nextInt(16) + 8;
			int k1 = k + random.nextInt(16) + 8;
			grassPatchGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int i, int j, int k)
    {
        return 0x919161;
    }
}
