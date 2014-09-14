package lotr.common.world.biome;

import java.util.*;

import lotr.common.LOTRDimension;
import lotr.common.LOTRMod;
import lotr.common.world.LOTRChunkProviderUtumno.UtumnoLevel;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRBiomeGenUtumno extends LOTRBiome
{
	private static List<LOTRBiome> utumnoBiomes = new ArrayList();
	
	public LOTRBiomeGenUtumno(int i)
	{
		super(i, LOTRDimension.UTUMNO);
		utumnoBiomes.add(this);
		
		setDisableRain();
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableLOTRAmbientList.clear();
		spawnableCaveCreatureList.clear();
		spawnableGoodList.clear();
		spawnableEvilList.clear();
		
		setGoodEvilWeight(0, 100);

		biomeColors.setGrass(0x000000);
		biomeColors.setFoliage(0x000000);
		biomeColors.setSky(0x000000);
		biomeColors.setFoggy(true);
		biomeColors.setWater(0x000000);
	}
	
	@Override
	public void decorate(World world, Random random, int i, int k)
	{
		generateHoles(world, random, i, k);
		generatePits(world, random, i, k);
		generateBridges(world, random, i, k);
		generateStairs(world, random, i, k);
	}
	
	private void generateHoles(World world, Random random, int i, int k)
	{
		for (int l = 0; l < 8; l++)
		{
			int i1 = i + 8 + random.nextInt(16);
			int k1 = k + 8 + random.nextInt(16);
			int j1 = MathHelper.getRandomIntegerInRange(random, 20, 240);
			
			if (world.isAirBlock(i1, j1, k1))
			{
				UtumnoLevel level = UtumnoLevel.forY(j1);
				
				for (int j2 = j1; j2 >= level.corridorBaseLevels[0] - 1; j2--)
				{
					if (world.isAirBlock(i1, j2, k1) && random.nextInt(10) == 0)
					{
						break;
					}
					
					if (UtumnoLevel.forY(j2 - 1) != level)
					{
						break;
					}
					
					for (int i2 = i1 - 1; i2 <= i1; i2++)
					{
						for (int k2 = k1 - 1; k2 <= k1; k2++)
						{
							world.setBlockToAir(i2, j2, k2);
						}
					}
				}
			}
		}
	}
	
	private void generatePits(World world, Random random, int i, int k)
	{
		if (random.nextInt(10) == 0)
		{
			int i1 = i + 8 + random.nextInt(16);
			int k1 = k + 8 + random.nextInt(16);
			int j1 = MathHelper.getRandomIntegerInRange(random, 20, 240);
			
			if (world.isAirBlock(i1, j1, k1))
			{
				int radius = 10 + random.nextInt(30);
				
				UtumnoLevel level = UtumnoLevel.forY(j1);
				
				int yMin = Math.max(j1 - radius, level.baseLevel + 5);
				int yMax = Math.min(j1 + radius, level.topLevel - 5);
				
				for (int i2 = i1 - radius; i2 <= i1 + radius; i2++)
				{
					for (int j2 = yMin; j2 <= yMax; j2++)
					{
						for (int k2 = k1 - radius; k2 <= k1 + radius; k2++)
						{
							int i3 = Math.abs(i2 - i1);
							int j3 = Math.abs(j2 - j1);
							int k3 = Math.abs(k2 - k1);
							double dist = i3 * i3 + j3 * j3 + k3 * k3;
							
							if (dist < (radius - 5) * (radius - 5))
							{
								world.setBlockToAir(i2, j2, k2);
							}
							else if (dist < radius * radius && random.nextInt(6) == 0)
							{
								world.setBlockToAir(i2, j2, k2);
							}
						}
					}
				}
			}
		}
	}
	
	private void generateBridges(World world, Random random, int i, int k)
	{
		for (int l = 0; l < 20; l++)
		{
			int i1 = i + 8 + random.nextInt(16);
			int k1 = k + 8 + random.nextInt(16);
			
			UtumnoLevel utumnoLevel = UtumnoLevel.values()[random.nextInt(UtumnoLevel.values().length)];
			int j1 = utumnoLevel.corridorBaseLevels[random.nextInt(utumnoLevel.corridorBaseLevels.length)] - 1;
			
			if (world.getBlock(i1, j1, k1) == LOTRMod.utumnoBrick && world.isAirBlock(i1, j1 + 1, k1))
			{
				if (world.isAirBlock(i1 - 1, j1, k1) || world.isAirBlock(i1 + 1, j1, k1) || world.isAirBlock(i1, j1, k1 - 1) || world.isAirBlock(i1, j1, k1 + 1))
				{
					int[] bridge = searchForBridge(world, i1, j1, k1, -1, 0);
					if (bridge == null)
					{
						bridge = searchForBridge(world, i1, j1, k1, 1, 0);
						if (bridge == null)
						{
							bridge = searchForBridge(world, i1, j1, k1, 0, -1);
							if (bridge == null)
							{
								bridge = searchForBridge(world, i1, j1, k1, 0, 1);
							}
						}
					}
					
					if (bridge != null)
					{
						int xRange = bridge[0];
						int zRange = bridge[1];
						
						int startX = i1;
						int endX = i1;
						int startZ = k1;
						int endZ = k1;
						
						if (xRange >= 0)
						{
							endX += xRange;
						}
						else
						{
							startX -= -xRange;
						}
						
						if (zRange >= 0)
						{
							endZ += zRange;
						}
						else
						{
							startZ -= -zRange;
						}
						
						if (xRange == 0)
						{
							int xWidth = random.nextInt(3);
							startX -= xWidth;
							endX += xWidth;
						}
						if (zRange == 0)
						{
							int zWidth = random.nextInt(3);
							startZ -= zWidth;
							endZ += zWidth;
						}
						
						for (int x = startX; x <= endX; x++)
						{
							for (int z = startZ; z <= endZ; z++)
							{
								if (random.nextInt(8) != 0)
								{
									world.setBlock(x, j1, z, LOTRMod.utumnoBrick, utumnoLevel.brickMeta, 2);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private int[] searchForBridge(World world, int i, int j, int k, int xDirection, int zDirection)
	{
		UtumnoLevel utumnoLevel = UtumnoLevel.forY(j);
				
		int maxBridgeLength = 16;
		int minBridgeLength = 2 + utumnoLevel.corridorWidth / 2;
		
		int foundAir = 0;
		int foundBrick = 0;
		
		int x = 0;
		int z = 0;
		while (true)
		{
			if (Math.abs(x) >= maxBridgeLength || Math.abs(z) >= maxBridgeLength)
			{
				break;
			}
			
			if (xDirection == -1)
			{
				x--;
			}
			if (xDirection == 1)
			{
				x++;
			}
			if (zDirection == -1)
			{
				z--;
			}
			if (zDirection == 1)
			{
				z++;
			}
			
			int i1 = i + x;
			int k1 = k + z;
			if (foundAir == 0 && world.isAirBlock(i1, j, k1))
			{
				if (xDirection == 0)
				{
					foundAir = z;
				}
				else if (zDirection == 0)
				{
					foundAir = x;
				}
			}
			if (foundAir != 0 && world.getBlock(i1, j, k1) == LOTRMod.utumnoBrick)
			{
				if (xDirection == 0)
				{
					foundBrick = z;
				}
				else if (zDirection == 0)
				{
					foundBrick = x;
				}
				break;
			}
		}
		
		if (foundBrick != 0 && Math.abs(foundBrick - foundAir) >= minBridgeLength)
		{
			return new int[] {x, z};
		}
		else
		{
			return null;
		}
	}
	
	private void generateStairs(World world, Random random, int i, int k)
	{
		for (int l = 0; l < 8; l++)
		{
			int i1 = i + 8 + random.nextInt(16);
			int k1 = k + 8 + random.nextInt(16);
			
			UtumnoLevel utumnoLevel = UtumnoLevel.values()[random.nextInt(UtumnoLevel.values().length)];
			int j1 = utumnoLevel.corridorBaseLevels[1 + random.nextInt(utumnoLevel.corridorBaseLevels.length - 1)] - 1;
			
			if (world.getBlock(i1, j1, k1) == LOTRMod.utumnoBrick && world.isAirBlock(i1, j1 + 1, k1))
			{
				int xDirection = 0;
				int zDirection = 0;
				
				if (random.nextBoolean())
				{
					xDirection = random.nextBoolean() ? 1 : -1;
				}
				else
				{
					zDirection = random.nextBoolean() ? 1 : -1;
				}

				int stairX = i1;
				int stairY = j1;
				int stairZ = k1;
				
				int minStairRange = 6;
				int maxStairRange = 20;
				
				int stairWidth = 1 + random.nextInt(3);
				int stairHeight = stairWidth + 2;
				
				int stair = 0;
				while (true)
				{
					stairY--;
					
					if (stairY <= utumnoLevel.corridorBaseLevels[0])
					{
						break;
					}
					
					if (xDirection == 0)
					{
						for (int i2 = stairX; i2 < stairX + stairWidth; i2++)
						{
							world.setBlock(i2, stairY, stairZ, LOTRMod.utumnoBrick, utumnoLevel.brickMeta, 2);
							for (int j2 = stairY + 1; j2 <= stairY + stairHeight; j2++)
							{
								world.setBlock(i2, j2, stairZ, Blocks.air);
							}
						}
					}
					else if (zDirection == 0)
					{
						for (int k2 = stairZ; k2 < stairZ + stairWidth; k2++)
						{
							world.setBlock(stairX, stairY, k2, LOTRMod.utumnoBrick, utumnoLevel.brickMeta, 2);
							for (int j2 = stairY + 1; j2 <= stairY + stairHeight; j2++)
							{
								world.setBlock(stairX, j2, k2, Blocks.air);
							}
						}
					}
					
					stair++;
					if (stair >= maxStairRange || (stair >= minStairRange && random.nextInt(10) == 0))
					{
						break;
					}
					else
					{
						if (xDirection == -1)
						{
							stairX--;
						}
						if (xDirection == 1)
						{
							stairX++;
						}
						if (zDirection == -1)
						{
							stairZ--;
						}
						if (zDirection == 1)
						{
							stairZ++;
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean canSpawnHostilesInDay()
	{
		return true;
	}

	public static void updateFogColor(int i, int j, int k)
	{
		UtumnoLevel utumnoLevel = UtumnoLevel.forY(j);
		
		for (LOTRBiome biome : utumnoBiomes)
		{
			biome.biomeColors.setFog(utumnoLevel.fogColor);
		}
	}
}
