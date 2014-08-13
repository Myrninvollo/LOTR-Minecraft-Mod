package lotr.common.world;

import java.util.List;

import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.npc.LOTREntityBandit;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTRInvasionSpawner
{
	public static int getChance(float probability, int seconds)
	{
		int ticks = seconds * 20;
		
		double d = (double)probability;
		d = 1D - d;
		d = Math.pow(d, 1D / (double)ticks);
		d = 1D - d;
		d = 1D / d;
		d /= 20D;
		
		int chance = (int)Math.round(d);
		return chance;
	}
	
	public static int RARE = getChance(0.1F, 3600);
	public static int UNCOMMON = getChance(0.3F, 3600);
	public static int COMMON = getChance(0.9F, 3600);
	
	public static void performSpawning(World world)
	{
		if (world.difficultySetting == EnumDifficulty.PEACEFUL)
		{
			return;
		}
		
		invasionSpawningLoop:
		for (int players = 0; players < world.playerEntities.size(); players++)
		{
			EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(players);
			if (entityplayer.capabilities.isCreativeMode)
			{
				continue invasionSpawningLoop;
			}
			
			int i = MathHelper.floor_double(entityplayer.posX);
			int k = MathHelper.floor_double(entityplayer.posZ);
			BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
			if (biome instanceof LOTRBiome)
			{
				List invasionSpawns = ((LOTRBiome)biome).invasionSpawns;
				for (Object obj : invasionSpawns)
				{
					BiomeInvasionListEntry entry = (BiomeInvasionListEntry)obj;
					LOTRFaction faction = entry.faction;
					
					if (LOTRLevelData.getData(entityplayer).getAlignment(faction) < 0 && world.rand.nextInt(entry.chance) == 0)
					{
						for (int attempts = 0; attempts < 16; attempts++)
						{
							int i1 = i - world.rand.nextInt(32) + world.rand.nextInt(32);
							int k1 = k - world.rand.nextInt(32) + world.rand.nextInt(32);
							int j1 = world.getHeightValue(i1, k1);
							if (j1 > 62)
							{
								Block block = world.getBlock(i1, j1 - 1, k1);
								if (block == biome.topBlock && !world.getBlock(i1, j1, k1).isNormalCube() && !world.getBlock(i1, j1 + 1, k1).isNormalCube())
								{
									j1 += 3 + world.rand.nextInt(3);
									
									LOTREntityInvasionSpawner invasion = new LOTREntityInvasionSpawner(world);
									invasion.setFaction(faction);
									invasion.setLocationAndAngles(i1 + 0.5D, j1, k1 + 0.5D, 0F, 0F);
									if (invasion.canInvasionSpawnHere())
									{
										world.spawnEntityInWorld(invasion);
										invasion.announceInvasion();
										continue invasionSpawningLoop;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static class BiomeInvasionListEntry
	{
		public LOTRFaction faction;
		public int chance;
		
		public BiomeInvasionListEntry(LOTRFaction f, int i)
		{
			faction = f;
			chance = i;
		}
	}
	
	public static class InvasionSpawnEntry extends WeightedRandom.Item
	{
		private Class entityClass;
		
		public InvasionSpawnEntry(Class<? extends LOTREntityNPC> c, int chance)
		{
			super(chance);
			entityClass = c;
		}
		
		public Class getEntityClass()
		{
			return entityClass;
		}
	}
}
