package lotr.common.world;

import java.util.Random;

import lotr.common.LOTRLevelData;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityElvenTrader;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRTravellingTrader;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTRTravellingTraderSpawner
{
	private static Random rand = new Random();
	
	private Class theEntityClass;
	public String name;
	
	private int timeUntilTrader;
	
	public LOTRTravellingTraderSpawner(Class entityClass)
	{
		theEntityClass = entityClass;
		name = LOTREntities.getStringFromClass(theEntityClass);
	}
	
	private static int getRandomTraderTime()
	{
		return 50000 + rand.nextInt(250000);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("TraderTime", timeUntilTrader);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("TraderTime"))
		{
			timeUntilTrader = nbt.getInteger("TraderTime");
		}
		else
		{
			timeUntilTrader = getRandomTraderTime();
		}
	}
	
	public void performSpawning(World world)
	{
		if (timeUntilTrader > 0)
		{
			timeUntilTrader--;
		}
		else
		{
			boolean spawned = false;
			
			LOTREntityNPC entityTrader = (LOTREntityNPC)LOTREntities.createEntityByClass(theEntityClass, world);
			LOTRTravellingTrader trader = (LOTRTravellingTrader)entityTrader;
			
			traderSpawningLoop:
			for (int players = 0; players < world.playerEntities.size(); players++)
			{
				EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(players);
				for (int attempts = 0; attempts < 16; attempts++)
				{
					float angle = world.rand.nextFloat() * 360F;
					int i = MathHelper.floor_double(entityplayer.posX) + MathHelper.floor_double(MathHelper.sin(angle) * (48 + world.rand.nextInt(33)));
					int k = MathHelper.floor_double(entityplayer.posZ) + MathHelper.floor_double(MathHelper.cos(angle) * (48 + world.rand.nextInt(33)));
					BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
					if (biome instanceof LOTRBiome && ((LOTRBiome)biome).canSpawnTravellingTrader(theEntityClass))
					{
						int j = world.getHeightValue(i, k);
						if (j > 62 && world.getBlock(i, j - 1, k) == biome.topBlock && !world.getBlock(i, j, k).isNormalCube() && !world.getBlock(i, j + 1, k).isNormalCube())
						{
							entityTrader.setLocationAndAngles(i + 0.5D, j, k + 0.5D, world.rand.nextFloat() * 360F, 0F);
							entityTrader.liftSpawnRestrictions = true;
							if (entityTrader.getCanSpawnHere())
							{
								entityTrader.liftSpawnRestrictions = false;
								world.spawnEntityInWorld(entityTrader);
								trader.startTraderVisiting(entityplayer);
								
								spawned = true;
								timeUntilTrader = getRandomTraderTime();
								LOTRLevelData.markDirty();
								break traderSpawningLoop;
							}
						}
					}
				}
			}
			
			if (!spawned)
			{
				timeUntilTrader = 100;
			}
		}
	}
}
