package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityHobbitFarmer;
import lotr.common.entity.npc.LOTREntityHobbitFarmhand;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.structure.LOTRChestContents;
import lotr.common.world.structure.LOTRWorldGenHobbitHole;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

public class LOTRWorldGenHobbitFarm extends LOTRWorldGenStructureBase2
{
	private Block wood1Block;
	private int wood1Meta;
	
	private Block wood1SlabBlock;
	private int wood1SlabMeta;
	
	private Block wood1Stair;
	
	private Block wood2Block;
	private int wood2Meta;
	
	private Block cropBlock;
	private Item seedItem;
	
	public LOTRWorldGenHobbitFarm(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
		}
		
		j--;
		
		setRotationMode(rotation);
		
		switch (getRotationMode())
		{
			case 0:
				k += 6;
				break;
			case 1:
				i -= 6;
				break;
			case 2:
				k -= 6;
				break;
			case 3:
				i += 6;
				break;
		}
		
		setOrigin(i, j, k);
		
		int randomWood = random.nextInt(4);
		switch (randomWood)
		{
			case 0:
				wood1Block = Blocks.planks;
				wood1Meta = 0;
				wood1SlabBlock = Blocks.wooden_slab;
				wood1SlabMeta = 0;
				wood1Stair = Blocks.oak_stairs;
				break;
			case 1:
				wood1Block = Blocks.planks;
				wood1Meta = 2;
				wood1SlabBlock = Blocks.wooden_slab;
				wood1SlabMeta = 2;
				wood1Stair = Blocks.birch_stairs;
				break;
			case 2:
				wood1Block = LOTRMod.planks;
				wood1Meta = 0;
				wood1SlabBlock = LOTRMod.woodSlabSingle;
				wood1SlabMeta = 0;
				wood1Stair = LOTRMod.stairsShirePine;
				break;
			case 3:
				wood1Block = LOTRMod.planks;
				wood1Meta = 4;
				wood1SlabBlock = LOTRMod.woodSlabSingle;
				wood1SlabMeta = 4;
				wood1Stair = LOTRMod.stairsApple;
				break;
		}
		
		int randomWood2 = random.nextInt(2);
		switch (randomWood2)
		{
			case 0:
				wood2Block = Blocks.planks;
				wood2Meta = 1;
				break;
			case 1:
				wood2Block = LOTRMod.planks;
				wood2Meta = 6;
				break;
		}
		
		int randomCrop = random.nextInt(5);
		switch (randomCrop)
		{
			case 0:
				cropBlock = Blocks.wheat;
				seedItem = Items.wheat_seeds;
				break;
			case 1:
				cropBlock = Blocks.carrots;
				seedItem = Items.carrot;
				break;
			case 2:
				cropBlock = Blocks.potatoes;
				seedItem = Items.potato;
				break;
			case 3:
				cropBlock = LOTRMod.lettuceCrop;
				seedItem = LOTRMod.lettuce;
				break;
			case 4:
				cropBlock = LOTRMod.pipeweedCrop;
				seedItem = LOTRMod.pipeweedSeeds;
				break;
		}
		
		if (restrictions)
		{
			int minHeight = 1;
			int maxHeight = 1;
			
			for (int i1 = -5; i1 <= 10; i1++)
			{
				for (int k1 = -7; k1 <= 8; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone)
					{
						return false;
					}
					
					if (j1 > maxHeight)
					{
						maxHeight = j1;
					}
					if (j1 < minHeight)
					{
						minHeight = j1;
					}
				}
			}
			
			if (Math.abs(maxHeight - minHeight) > 6)
			{
				return false;
			}
		}
		
		for (int i1 = -5; i1 <= 10; i1++)
		{
			for (int k1 = -7; k1 <= 8; k1++)
			{
				for (int j1 = 1; j1 <= 10; j1++)
				{
					setAir(world, i1, j1, k1);
				}
				
				setBlockAndMetadata(world, i1, 0, k1, Blocks.grass, 0);
				setGrassToDirt(world, i1, -1, k1);
				
				for (int j1 = -1; !isOpaque(world, i1, j1, k1) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, Blocks.dirt, 0);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
			}
		}
		
		for (int k1 = -5; k1 <= 6; k1++)
		{
			for (int i1 = -5; i1 <= 4; i1++)
			{
				if (k1 == -5 || k1 == 6 || i1 == -5 || i1 == 4)
				{
					for (int j1 = 1; j1 <= 5; j1++)
					{
						setBlockAndMetadata(world, i1, j1, k1, wood2Block, wood2Meta);
						setGrassToDirt(world, i1, j1 - 1, k1);
					}
				}
			}
		}
		
		for (int stair = 0; stair <= 4; stair++)
		{
			int j1 = 5 + stair;
			for (int i1 = -5 + stair; i1 <= 4 - stair; i1++)
			{
				for (int k1 = -5; k1 <= 6; k1 += 11)
				{
					setBlockAndMetadata(world, i1, j1, k1, wood2Block, wood2Meta);
				}
			}
			
			setBlockAndMetadata(world, -6 + stair, j1, -6, wood1Stair, 1);
			setBlockAndMetadata(world, -6 + stair, j1, 7, wood1Stair, 1);
			setBlockAndMetadata(world, 5 - stair, j1, -6, wood1Stair, 0);
			setBlockAndMetadata(world, 5 - stair, j1, 7, wood1Stair, 0);
			
			for (int k1 = -5; k1 <= 6; k1++)
			{
				setBlockAndMetadata(world, -6 + stair, j1, k1, Blocks.stone_brick_stairs, 1);
				setBlockAndMetadata(world, 5 - stair, j1, k1, Blocks.stone_brick_stairs, 0);
			}
		}
		
		for (int k1 = -4; k1 <= 5; k1++)
		{
			for (int i1 = -4; i1 <= 3; i1++)
			{
				setBlockAndMetadata(world, i1, 5, k1, wood1Block, wood1Meta);
			}
		}
		
		for (int j1 = 1; j1 <= 5; j1++)
		{
			for (int k1 = -5; k1 <= 6; k1 += 11)
			{
				setBlockAndMetadata(world, -5, j1, k1, wood1Block, wood1Meta);
				setBlockAndMetadata(world, -2, j1, k1, wood1Block, wood1Meta);
				setBlockAndMetadata(world, 1, j1, k1, wood1Block, wood1Meta);
				setBlockAndMetadata(world, 4, j1, k1, wood1Block, wood1Meta);
			}
			
			for (int i1 = -5; i1 <= 4; i1 += 9)
			{
				setBlockAndMetadata(world, i1, j1, -1, wood1Block, wood1Meta);
				setBlockAndMetadata(world, i1, j1, 2, wood1Block, wood1Meta);
			}
		}
		
		for (int k1 = 0; k1 <= 1; k1++)
		{
			for (int i1 = -5; i1 <= 4; i1 += 9)
			{
				setBlockAndMetadata(world, i1, 2, k1, wood1Block, wood1Meta);
				setBlockAndMetadata(world, i1, 4, k1, wood1Block, wood1Meta);
			}
		}
		
		for (int i1 = -1; i1 <= 0; i1++)
		{
			for (int k1 = -5; k1 <= 6; k1 += 11)
			{
				setBlockAndMetadata(world, i1, 0, k1, Blocks.grass, 0);
				setBlockAndMetadata(world, i1, 1, k1, Blocks.fence_gate, 0);
				setAir(world, i1, 2, k1);
				
				setBlockAndMetadata(world, i1, 3, k1, wood1Block, wood1Meta);
				setBlockAndMetadata(world, i1, 5, k1, wood1Block, wood1Meta);
				
				setBlockAndMetadata(world, i1, 7, k1, Blocks.glass_pane, 0);
			}
			
			setBlockAndMetadata(world, i1, 10, -6, wood1SlabBlock, wood1SlabMeta);
			setBlockAndMetadata(world, i1, 10, 7, wood1SlabBlock, wood1SlabMeta);
			for (int k1 = -5; k1 <= 6; k1++)
			{
				setBlockAndMetadata(world, i1, 10, k1, Blocks.stone_slab, 5);
			}
		}
		
		for (int i1 = -3; i1 <= 2; i1++)
		{
			setBlockAndMetadata(world, i1, 5, -6, wood1Stair, 6);
			setBlockAndMetadata(world, i1, 5, 7, wood1Stair, 7);
		}
		
		setBlockAndMetadata(world, -5, 5, -6, wood1Block, wood1Meta);
		setBlockAndMetadata(world, -4, 5, -6, wood1Stair, 4);
		setBlockAndMetadata(world, 3, 5, -6, wood1Stair, 5);
		setBlockAndMetadata(world, 4, 5, -6, wood1Block, wood1Meta);
		
		setBlockAndMetadata(world, -5, 5, 7, wood1Block, wood1Meta);
		setBlockAndMetadata(world, -4, 5, 7, wood1Stair, 4);
		setBlockAndMetadata(world, 3, 5, 7, wood1Stair, 5);
		setBlockAndMetadata(world, 4, 5, 7, wood1Block, wood1Meta);
		
		for (int i1 = -4; i1 <= 3; i1 += 7)
		{
			for (int k1 = -1; k1 <= 2; k1 += 3)
			{
				setBlockAndMetadata(world, i1, 1, k1, Blocks.crafting_table, 0);
				setBlockAndMetadata(world, i1, 2, k1, Blocks.fence, 0);
				setBlockAndMetadata(world, i1, 3, k1, Blocks.fence, 0);
				setBlockAndMetadata(world, i1, 4, k1, Blocks.torch, 5);
			}
		}
		
		setBlockAndMetadata(world, -4, 1, -4, Blocks.hay_block, 0);
		setBlockAndMetadata(world, -4, 2, -4, Blocks.hay_block, 0);
		setBlockAndMetadata(world, -3, 1, -4, Blocks.hay_block, 0);
		setBlockAndMetadata(world, -4, 1, -3, Blocks.hay_block, 0);
		
		setBlockAndMetadata(world, -4, 1, 5, Blocks.hay_block, 0);
		setBlockAndMetadata(world, -4, 2, 5, Blocks.hay_block, 0);
		setBlockAndMetadata(world, -3, 1, 5, Blocks.hay_block, 0);
		setBlockAndMetadata(world, -4, 1, 4, Blocks.hay_block, 0);
		
		setBlockAndMetadata(world, 3, 1, 5, Blocks.hay_block, 0);
		setBlockAndMetadata(world, 3, 2, 5, Blocks.hay_block, 0);
		setBlockAndMetadata(world, 2, 1, 5, Blocks.hay_block, 0);
		setBlockAndMetadata(world, 3, 1, 4, Blocks.hay_block, 0);
		
		for (int j1 = 1; j1 <= 4; j1++)
		{
			setBlockAndMetadata(world, 2, j1, -3, Blocks.fence, 0);
		}
		
		setBlockAndMetadata(world, 1, 1, -4, wood1Stair, 1);
		setBlockAndMetadata(world, 2, 1, -4, wood1Block, wood1Meta);
		setBlockAndMetadata(world, 2, 2, -4, wood1Stair, 1);
		setBlockAndMetadata(world, 3, 1, -4, wood1Block, wood1Meta);
		setBlockAndMetadata(world, 3, 2, -4, wood1Block, wood1Meta);
		setBlockAndMetadata(world, 3, 2, -3, wood1Stair, 7);
		setBlockAndMetadata(world, 3, 3, -3, wood1Stair, 2);
		setBlockAndMetadata(world, 3, 3, -2, wood1Block, wood1Meta);
		setBlockAndMetadata(world, 2, 3, -2, wood1Stair, 5);
		setBlockAndMetadata(world, 2, 4, -2, wood1Stair, 0);
		setBlockAndMetadata(world, 1, 4, -2, wood1Stair, 5);
		setBlockAndMetadata(world, 1, 5, -2, wood1Stair, 0);
		
		setAir(world, 3, 5, -4);
		setAir(world, 3, 5, -3);
		setAir(world, 3, 5, -2);
		setAir(world, 2, 5, -2);
		
		for (int i1 = 0; i1 <= 2; i1++)
		{
			setBlockAndMetadata(world, i1, 6, -3, Blocks.fence, 0);
			setBlockAndMetadata(world, i1, 6, -1, Blocks.fence, 0);
		}
		
		setBlockAndMetadata(world, 2, 6, -4, Blocks.fence, 0);
		setBlockAndMetadata(world, 0, 6, -2, Blocks.fence_gate, 3);
		
		for (int k1 = -4; k1 <= 5; k1++)
		{
			setBlockAndMetadata(world, -4, 6, k1, wood2Block, wood2Meta);
		}
		
		for (int k1 = -1; k1 <= 5; k1++)
		{
			setBlockAndMetadata(world, 3, 6, k1, wood2Block, wood2Meta);
		}
		
		setBlockAndMetadata(world, -2, 7, -4, Blocks.torch, 3);
		setBlockAndMetadata(world, 1, 7, -4, Blocks.torch, 3);
		
		setBlockAndMetadata(world, -2, 7, 5, Blocks.torch, 4);
		setBlockAndMetadata(world, 1, 7, 5, Blocks.torch, 4);
		
		int carpet = random.nextInt(16);
		setBlockAndMetadata(world, -1, 6, 2, Blocks.carpet, carpet);
		setBlockAndMetadata(world, 0, 6, 2, Blocks.carpet, carpet);
		setBlockAndMetadata(world, -1, 6, 3, Blocks.carpet, carpet);
		setBlockAndMetadata(world, 0, 6, 3, Blocks.carpet, carpet);
		
		for (int k1 = 4; k1 <= 5; k1++)
		{
			for (int j1 = 6; j1 <= 7; j1++)
			{
				setBlockAndMetadata(world, -3, j1, k1, Blocks.bookshelf, 0);
				setBlockAndMetadata(world, 2, j1, k1, Blocks.bookshelf, 0);
			}
		}
		
		setBlockAndMetadata(world, -3, 6, 0, wood2Block, wood2Meta);
		setBlockAndMetadata(world, -3, 7, 0, LOTRWorldGenHobbitHole.getRandomFoodBlock(random), 0);
		setBlockAndMetadata(world, -3, 6, 1, Blocks.cauldron, 3);
		setBlockAndMetadata(world, -3, 6, 2, LOTRMod.hobbitOven, 4);
		setBlockAndMetadata(world, -3, 6, 3, Blocks.crafting_table, 0);
		
		setBlockAndMetadata(world, 2, 6, 1, Blocks.chest, 5);
		fillChest(world, random, 2, 6, 1, LOTRChestContents.HOBBIT_HOLE_LARDER);
		setBlockAndMetadata(world, 2, 6, 2, Blocks.bed, 0);
		setBlockAndMetadata(world, 2, 6, 3, Blocks.bed, 8);
		
		for (int i1 = 5; i1 <= 10; i1++)
		{
			setBlockAndMetadata(world, i1, 1, -5, Blocks.fence, 0);
			setBlockAndMetadata(world, i1, 1, 6, Blocks.fence, 0);
		}
		
		for (int k1 = -4; k1 <= 5; k1++)
		{
			setBlockAndMetadata(world, 10, 1, k1, Blocks.fence, 0);
		}
		
		setBlockAndMetadata(world, 7, 1, -5, Blocks.fence_gate, 0);
		
		setBlockAndMetadata(world, 5, 2, -5, Blocks.torch, 5);
		setBlockAndMetadata(world, 10, 2, -5, Blocks.torch, 5);
		
		setBlockAndMetadata(world, 10, 2, -1, Blocks.torch, 5);
		setBlockAndMetadata(world, 10, 2, 2, Blocks.torch, 5);
		
		setBlockAndMetadata(world, 5, 2, 6, Blocks.torch, 5);
		setBlockAndMetadata(world, 10, 2, 6, Blocks.torch, 5);
		
		for (int i1 = 5; i1 <= 9; i1++)
		{
			setBlockAndMetadata(world, i1, 0, -4, Blocks.gravel, 0);
			setBlockAndMetadata(world, i1, 0, 5, Blocks.gravel, 0);
		}
		
		for (int k1 = -3; k1 <= 4; k1++)
		{
			setBlockAndMetadata(world, 4, 0, k1, Blocks.stonebrick, 0);
			setBlockAndMetadata(world, 5, 0, k1, Blocks.water, 0);
			setBlockAndMetadata(world, 5, 1, k1, Blocks.stone_slab, 5);
			
			setBlockAndMetadata(world, 9, 0, k1, Blocks.gravel, 0);
			
			for (int i1 = 6; i1 <= 8; i1++)
			{
				setBlockAndMetadata(world, i1, 0, k1, Blocks.farmland, 7);
				setBlockAndMetadata(world, i1, 1, k1, cropBlock, 7);
			}
		}
		
		LOTREntityHobbit farmer = new LOTREntityHobbitFarmer(world);
		spawnNPCAndSetHome(farmer, world, 0, 6, 0, 16);
		
		int farmhands = 1 + random.nextInt(3);
		for (int l = 0; l < farmhands; l++)
		{
			LOTREntityHobbitFarmhand farmhand = new LOTREntityHobbitFarmhand(world);
			farmhand.seedsItem = seedItem;
			spawnNPCAndSetHome(farmhand, world, 7, 1, 0, 8);
		}
		
		int animals = 3 + random.nextInt(6);
		for (int l = 0; l < animals; l++)
		{
			Class animalClass = ((SpawnListEntry)WeightedRandom.getRandomItem(world.rand, LOTRBiome.shire.getSpawnableList(EnumCreatureType.creature))).entityClass;
			EntityCreature animal = null;
			
			try
			{
				animal = (EntityCreature)animalClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {world});
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				continue;
			}
			
			spawnNPCAndSetHome(animal, world, 0, 1, 0, 8);
		}
		
		return true;
	}
}
