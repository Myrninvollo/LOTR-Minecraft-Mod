package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenBoulder extends WorldGenerator
{
	private Block id;
	private int meta;
	private int minWidth;
	private int maxWidth;
	private Block spawnID;
	private int spawnMeta;
	private int heightCheck = 3;
	
	public LOTRWorldGenBoulder(Block i, int j, int k, int l)
	{
		super(false);
		id = i;
		meta = j;
		minWidth = k;
		maxWidth = l;
		spawnID = Blocks.grass;
		spawnMeta = 0;
	}
	
	public LOTRWorldGenBoulder setSpawnBlock(Block i, int j)
	{
		spawnID = i;
		spawnMeta = j;
		return this;
	}
	
	public LOTRWorldGenBoulder setHeightCheck(int i)
	{
		heightCheck = i;
		return this;
	}
	
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (world.getBlock(i, j - 1, k) != spawnID || world.getBlockMetadata(i, j - 1, k) != spawnMeta)
		{
			return false;
		}
		
		int boulderWidth = MathHelper.getRandomIntegerInRange(random, minWidth, maxWidth);

		int highestHeight = j;
		int lowestHeight = j;
		for (int i1 = i - boulderWidth; i1 <= i + boulderWidth; i1++)
		{
			for (int k1 = k - boulderWidth; k1 <= k + boulderWidth; k1++)
			{
				int heightValue = world.getHeightValue(i1, k1);
				if (world.getBlock(i1, heightValue - 1, k1) != spawnID || world.getBlockMetadata(i1, heightValue - 1, k1) != spawnMeta)
				{
					return false;
				}
				if (heightValue > highestHeight)
				{
					highestHeight = heightValue;
				}
				if (heightValue < lowestHeight)
				{
					lowestHeight = heightValue;
				}
			}
		}
		
		
		if (highestHeight - lowestHeight > heightCheck)
		{
			return false;
		}
		
		int spheres = 1 + random.nextInt(boulderWidth + 1);
		
		for (int l = 0; l < spheres; l++)
		{
			int posX = i - boulderWidth - random.nextInt(boulderWidth * 2);
			int posZ = k - boulderWidth - random.nextInt(boulderWidth * 2);
			int posY = world.getTopSolidOrLiquidBlock(posX, posZ);
			int sphereWidth = MathHelper.getRandomIntegerInRange(random, minWidth, maxWidth);
			
			for (int i1 = posX - sphereWidth; i1 <= posX + sphereWidth; i1++)
			{
				for (int j1 = posY - sphereWidth; j1 <= posY + sphereWidth; j1++)
				{
					for (int k1 = posZ - sphereWidth; k1 <= posZ + sphereWidth; k1++)
					{
						int i2 = i1 - posX;
						int j2 = j1 - posY;
						int k2 = k1 - posZ;
						
						if (i2 * i2 + j2 * j2 + k2 * k2 < sphereWidth * sphereWidth)
						{
							int j3 = j1;
							while (j3 >= 0 && !LOTRMod.isOpaque(world, i1, j3 - 1, k1))
							{
								j3--;
							}
							
							setBlockAndNotifyAdequately(world, i1, j3, k1, id, meta);
							if (world.getBlock(i1, j3 - 1, k1) == Blocks.grass)
							{
								setBlockAndNotifyAdequately(world, i1, j3 - 1, k1, Blocks.dirt, 0);
							}
						}
					}
				}
			}
		}
		
		return true;
	}
}
