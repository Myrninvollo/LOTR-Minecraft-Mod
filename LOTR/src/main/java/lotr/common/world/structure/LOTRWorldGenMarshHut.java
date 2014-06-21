package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.LOTREntityTroll;
import lotr.common.world.feature.LOTRWorldGenFangornTrees;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class LOTRWorldGenMarshHut extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenMarshHut()
	{
		super(false);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		j--;
		
		int radius = 8;
		int radiusPlusOne = radius + 1;
		int wallThresholdMin = radius * radius;
		int wallThresholdMax = radiusPlusOne * radiusPlusOne;
		
		for (int i1 = i - radiusPlusOne; i1 <= i + radiusPlusOne; i1++)
		{
			for (int k1 = k - radiusPlusOne; k1 <= k + radiusPlusOne; k1++)
			{
				int i2 = i1 - i;
				int k2 = k1 - k;
				int distSq = i2 * i2 + k2 * k2;
				if (distSq >= wallThresholdMax)
				{
					continue;
				}
				else
				{
					for (int j1 = j; (j1 == j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 0);
						if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
						{
							setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
						}
					}
					
					for (int j1 = j + 1; j1 <= j + 6; j1++)
					{
						if (distSq >= wallThresholdMin)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.planks, 0);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
						}
					}
				}
			}
		}
		
		int roofThresholdMax = (radiusPlusOne + 2) * (radiusPlusOne + 2);
		
		for (int i1 = i - radiusPlusOne - 2; i1 <= i + radiusPlusOne + 2; i1++)
		{
			for (int k1 = k - radiusPlusOne - 2; k1 <= k + radiusPlusOne + 2; k1++)
			{
				for (int j1 = j + 6; j1 <= j + 10; j1++)
				{
					int i2 = i1 - i;
					int k2 = k1 - k;
					int j2 = j1 - (j + 4);
					int distSq = i2 * i2 + k2 * k2 + j2 * j2;
					if (distSq + j2 * j2 >= wallThresholdMax)
					{
						continue;
					}
					
					boolean grass = !LOTRMod.isOpaque(world, i1, j1 + 1, k1);
					setBlockAndNotifyAdequately(world, i1, j1, k1, grass ? Blocks.grass : Blocks.dirt, 0);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		setBlockAndNotifyAdequately(world, i - (radius - 1), j + 3, k, Blocks.torch, 1);
		setBlockAndNotifyAdequately(world, i + (radius - 1), j + 3, k, Blocks.torch, 2);
		setBlockAndNotifyAdequately(world, i, j + 3, k - (radius - 1), Blocks.torch, 3);
		setBlockAndNotifyAdequately(world, i, j + 3, k + (radius - 1), Blocks.torch, 4);
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - radius, Blocks.wooden_door, 1);
		setBlockAndNotifyAdequately(world, i, j + 2, k - radius, Blocks.wooden_door, 8);
		
		LOTRWorldGenFangornTrees.newOak(false).disableRestrictions().generate(world, random, i, j + 11, k);
		
		LOTREntityTroll troll = new LOTREntityTroll(world);
		troll.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		troll.isNPCPersistent = true;
		troll.onSpawnWithEgg(null);
		troll.isImmuneToSun = true;
		troll.isPassive = true;
		troll.setTrollName(new StringBuilder().append('\u0053').append('\u0068').append('\u0072').append('\u0065').append('\u006B').toString());
		troll.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100000000D);
		troll.setHealth(troll.getMaxHealth());
		world.spawnEntityInWorld(troll);
		
		LOTREntityHorse horse = new LOTREntityHorse(world);
		horse.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		horse.setHorseType(1);
		horse.setMountable(false);
		horse.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100000000D);
		horse.setHealth(horse.getMaxHealth());
		world.spawnEntityInWorld(horse);
		
		EntityOcelot cat = new EntityOcelot(world);
		cat.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		cat.setTamed(true);
		cat.setTameSkin(2);
		cat.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100000000D);
		cat.setHealth(cat.getMaxHealth());
		world.spawnEntityInWorld(cat);
		
		setBlockAndNotifyAdequately(world, i, j + 2, k + (radius - 1), Blocks.wall_sign, 2);
		TileEntity tileentity = world.getTileEntity(i, j + 2, k + (radius - 1));
		if (tileentity != null && tileentity instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)tileentity;
			sign.signText[0] = "Check yourself";
			sign.signText[1] = "before you";
			sign.signText[2] = troll.getTrollName() + " yourself";
		}
		
		return true;
	}
}
