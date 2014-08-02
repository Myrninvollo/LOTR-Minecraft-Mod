package lotr.common.world;

import lotr.common.entity.npc.LOTREntityBandit;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTRBanditSpawner
{
	public static int NEVER = 0;
	public static int RARE = 20000;
	public static int UNCOMMON = 8000;
	public static int COMMON = 2000;
	
	public static void performSpawning(World world)
	{
		if (world.difficultySetting == EnumDifficulty.PEACEFUL)
		{
			return;
		}
		
		banditSpawningLoop:
		for (int players = 0; players < world.playerEntities.size(); players++)
		{
			EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(players);
			if (entityplayer.capabilities.isCreativeMode)
			{
				continue banditSpawningLoop;
			}
			
			int i = MathHelper.floor_double(entityplayer.posX);
			int k = MathHelper.floor_double(entityplayer.posZ);
			BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
			if (biome instanceof LOTRBiome)
			{
				int banditChance = ((LOTRBiome)biome).getBanditChance();
				if (banditChance > 0 && world.rand.nextInt(banditChance) == 0)
				{
					int banditsSpawned = 0;
					int maxBandits = MathHelper.getRandomIntegerInRange(world.rand, 1, 4);
					
					for (int attempts = 0; attempts < 32; attempts++)
					{
						int i1 = i - world.rand.nextInt(32) + world.rand.nextInt(32);
						int k1 = k - world.rand.nextInt(32) + world.rand.nextInt(32);
						int j1 = world.getHeightValue(i1, k1);
						
						if (j1 > 62)
						{
							Block block = world.getBlock(i1, j1 - 1, k1);
							if ((block == biome.topBlock || block == biome.fillerBlock) && !world.getBlock(i1, j1, k1).isNormalCube() && !world.getBlock(i1, j1 + 1, k1).isNormalCube())
							{
								LOTREntityBandit bandit = new LOTREntityBandit(world);
								bandit.setLocationAndAngles(i1 + 0.5D, j1, k1 + 0.5D, world.rand.nextFloat() * 360F, 0F);
								if (bandit.getCanSpawnHere())
								{
									bandit.onSpawnWithEgg(null);
									world.spawnEntityInWorld(bandit);
									bandit.isNPCPersistent = false;
									banditsSpawned++;
									
									if (banditsSpawned >= maxBandits)
									{
										continue banditSpawningLoop;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
