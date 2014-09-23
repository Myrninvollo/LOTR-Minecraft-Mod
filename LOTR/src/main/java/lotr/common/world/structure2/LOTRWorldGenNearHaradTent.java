package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarrior;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenNearHaradTent extends LOTRWorldGenStructureBase2
{
	public LOTRWorldGenNearHaradTent(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation)
	{
		if (restrictions)
		{
			Block block = world.getBlock(i, j - 1, k);
			if (block != Blocks.sand && block != Blocks.dirt && block != Blocks.grass)
			{
				return false;
			}
			if (world.getBlock(i, j, k) != Blocks.air)
			{
				return false;
			}
		}
		
		j--;
		
		setRotationMode(rotation);
		
		switch (getRotationMode())
		{
			case 0:
				k += 4;
				break;
			case 1:
				i -= 4;
				break;
			case 2:
				k -= 4;
				break;
			case 3:
				i += 4;
				break;
		}
		
		setOrigin(i, j, k);
		
		if (restrictions)
		{
			for (int i1 = -3; i1 <= 3; i1++)
			{
				for (int k1 = -3; k1 <= 3; k1++)
				{
					int j1 = getTopBlock(world, i1, k1);
					Block block = getBlock(world, i1, j1 - 1, k1);
					if (block != Blocks.sand && block != Blocks.dirt && block != Blocks.grass)
					{
						return false;
					}
				}
			}
		}
		
		for (int i1 = -3; i1 <= 3; i1++)
		{
			for (int k1 = -3; k1 <= 3; k1++)
			{
				for (int j1 = 0; (j1 == 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; j1--)
				{
					setBlockAndMetadata(world, i1, j1, k1, Blocks.sandstone, 0);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				
				for (int j1 = 1; j1 <= 6; j1++)
				{
					setAir(world, i1, j1, k1);
				}
				
				int i2 = Math.abs(i1);
				int k2 = Math.abs(k1);
				
				if (i2 == 3 && k2 == 3)
				{
					for (int j1 = 1; j1 <= 5; j1++)
					{
						setBlockAndMetadata(world, i1, j1, k1, LOTRMod.fence2, 2);
					}
					setBlockAndMetadata(world, i1, 6, k1, Blocks.torch, 5);
				}
				else if (i2 == 3 || k2 == 3)
				{
					for (int j1 = 1; j1 <= 4; j1++)
					{
						setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 14);
					}
				}
				
				if ((i2 == 2 && k2 <= 2) || (k2 == 2 && i2 <= 2))
				{
					for (int j1 = 4; j1 <= 5; j1++)
					{
						setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 15);
					}
					
					setBlockAndMetadata(world, i1, 1, k1, Blocks.carpet, 15);
				}
				
				if (i2 == 2 && k2 == 2)
				{
					for (int j1 = 1; j1 <= 4; j1++)
					{
						setBlockAndMetadata(world, i1, j1, k1, LOTRMod.fence2, 2);
					}
				}
				
				if ((i2 == 1 && k2 <= 1) || (k2 == 1 && i2 <= 1))
				{
					setBlockAndMetadata(world, i1, 6, k1, Blocks.wool, 14);
					
					setBlockAndMetadata(world, i1, 1, k1, Blocks.carpet, 14);
				}
			}
		}
		
		for (int j1 = 1; j1 <= 5; j1++)
		{
			setBlockAndMetadata(world, 0, j1, 0, LOTRMod.fence2, 2);
		}
		setBlockAndMetadata(world, 0, 6, 0, LOTRMod.planks2, 2);
		placeBanner(world, 0, 7, 0, LOTRFaction.NEAR_HARAD, 0);
		
		for (int j1 = 2; j1 <= 3; j1++)
		{
			for (int i1 = -1; i1 <= 1; i1++)
			{
				setBlockAndMetadata(world, i1, j1, -3, Blocks.wool, 15);
				setBlockAndMetadata(world, i1, j1, 3, Blocks.wool, 15);
			}
			
			for (int k1 = -1; k1 <= 1; k1++)
			{
				setBlockAndMetadata(world, -3, j1, k1, Blocks.wool, 15);
				setBlockAndMetadata(world, 3, j1, k1, Blocks.wool, 15);
			}
		}
		
		setAir(world, 0, 2, -3);
		setAir(world, 0, 2, 3);
		setAir(world, -3, 2, 0);
		setAir(world, 3, 2, 0);
		
		setBlockAndMetadata(world, -1, 1, -3, Blocks.wool, 15);
		setBlockAndMetadata(world, 0, 1, -3, Blocks.carpet, 14);
		setBlockAndMetadata(world, 0, 1, -2, Blocks.carpet, 14);
		setBlockAndMetadata(world, 1, 1, -3, Blocks.wool, 15);
		
		setBlockAndMetadata(world, -2, 1, 0, LOTRMod.strawBed, 0);
		setBlockAndMetadata(world, -2, 1, 1, LOTRMod.strawBed, 8);
		
		placeBarrel(world, random, -1, 1, 2, 2, LOTRMod.mugAraq);
		setBlockAndMetadata(world, 0, 1, 2, LOTRMod.nearHaradTable, 0);
		placeChest(world, random, 1, 1, 2, 2, LOTRChestContents.NEAR_HARAD_HOUSE);
		
		placeChest(world, random, 2, 1, 1, 5, LOTRChestContents.NEAR_HARAD_TOWER);
		setBlockAndMetadata(world, 2, 1, 0, Blocks.crafting_table, 0);
		
		setBlockAndMetadata(world, 0, 3, -2, Blocks.torch, 3);
		setBlockAndMetadata(world, 0, 3, 2, Blocks.torch, 4);
		
		placeWallBanner(world, -1, 3, 3, LOTRFaction.NEAR_HARAD, 2);
		placeWallBanner(world, 1, 3, 3, LOTRFaction.NEAR_HARAD, 2);
		
		placeWallBanner(world, 3, 3, 1, LOTRFaction.NEAR_HARAD, 3);
		placeWallBanner(world, 3, 3, -1, LOTRFaction.NEAR_HARAD, 3);
		
		placeWallBanner(world, -1, 3, -3, LOTRFaction.NEAR_HARAD, 0);
		placeWallBanner(world, 1, 3, -3, LOTRFaction.NEAR_HARAD, 0);
		
		placeWallBanner(world, -3, 3, 1, LOTRFaction.NEAR_HARAD, 1);
		placeWallBanner(world, -3, 3, -1, LOTRFaction.NEAR_HARAD, 1);
		
		LOTREntityNearHaradrim haradrim = new LOTREntityNearHaradrimWarrior(world);
		haradrim.spawnRidingHorse = false;
		spawnNPCAndSetHome(haradrim, world, 0, 1, -1, 16);
		
		return true;
	}
}
