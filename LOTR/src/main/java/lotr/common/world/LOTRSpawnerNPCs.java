package lotr.common.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lotr.common.LOTRDimension;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import cpw.mods.fml.common.eventhandler.Event;

public class LOTRSpawnerNPCs
{
    private static HashMap eligibleChunksForSpawning = new HashMap();
	
    public static ChunkPosition getRandomSpawningPointInChunk(World world, int i, int k)
    {
        Chunk chunk = world.getChunkFromChunkCoords(i, k);
        int i1 = i * 16 + world.rand.nextInt(16);
        int k1 = k * 16 + world.rand.nextInt(16);
        int j = world.rand.nextInt(chunk == null ? world.getActualHeight() : chunk.getTopFilledSegment() + 16 - 1);
        return new ChunkPosition(i1, j, k1);
    }
	
    public static void performSpawning(World world)
    {
		eligibleChunksForSpawning.clear();
		for (int l = 0; l < world.playerEntities.size(); l++)
		{
			EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(l);
			int i = MathHelper.floor_double(entityplayer.posX / 16.0D);
			int k = MathHelper.floor_double(entityplayer.posZ / 16.0D);
			byte range = 8;

			for (int i1 = -range; i1 <= range; i1++)
			{
				for (int k1 = -range; k1 <= range; k1++)
				{
					boolean onEdge = i1 == -range || i1 == range || k1 == -range || k1 == range;
					ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i + i1, k + k1);

					if (!onEdge)
					{
						eligibleChunksForSpawning.put(chunkcoordintpair, Boolean.valueOf(false));
					}
					else if (!eligibleChunksForSpawning.containsKey(chunkcoordintpair))
					{
						eligibleChunksForSpawning.put(chunkcoordintpair, Boolean.valueOf(true));
					}
				}
			}
		}

		ChunkCoordinates spawnPointCoords = world.getSpawnPoint();
		int totalSpawnCountValue = countNPCs(world);
		int maxSpawnCountValue = LOTRDimension.getCurrentDimension(world).maxSpawnedNPCs * eligibleChunksForSpawning.size() / 256;
		if (totalSpawnCountValue <= maxSpawnCountValue)
		{
			ArrayList<ChunkCoordIntPair> tmp = new ArrayList(eligibleChunksForSpawning.keySet());
			Collections.shuffle(tmp);
			Iterator iterator = tmp.iterator();
			
			spawnLoop:
			while (iterator.hasNext())
			{
				ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();
				if (!((Boolean)eligibleChunksForSpawning.get(chunkcoordintpair)).booleanValue())
				{
					ChunkPosition chunkposition = getRandomSpawningPointInChunk(world, chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
					int i = chunkposition.chunkPosX;
					int j = chunkposition.chunkPosY;
					int k = chunkposition.chunkPosZ;

					if (!world.getBlock(i, j, k).isNormalCube() && world.getBlock(i, j, k).getMaterial() == Material.air)
					{
						int entitiesSpawnedInChunk = 0;
						int groupsSpawnedInChunk = 0;

						while (groupsSpawnedInChunk < 3)
						{
							int i1 = i;
							int j1 = j;
							int k1 = k;
							byte range = 6;
							SpawnListEntry spawnlistentry = null;
							IEntityLivingData IEntityLivingData = null;
							int groupSpawnAttempts = 0;

							while (true)
							{
								if (groupSpawnAttempts < 4)
								{
									entitySpawnLoop:
									{
										i1 += world.rand.nextInt(range) - world.rand.nextInt(range);
										j1 += world.rand.nextInt(1) - world.rand.nextInt(1);
										k1 += world.rand.nextInt(range) - world.rand.nextInt(range);

										if (canEntityGroupSpawnAtLocation(world, i1, j1, k1))
										{
											float f = (float)i1 + 0.5F;
											float f1 = (float)j1;
											float f2 = (float)k1 + 0.5F;

											if (world.getClosestPlayer((double)f, (double)f1, (double)f2, 24D) == null)
											{
												float f3 = f - (float)spawnPointCoords.posX;
												float f4 = f1 - (float)spawnPointCoords.posY;
												float f5 = f2 - (float)spawnPointCoords.posZ;
												float distSq = f3 * f3 + f4 * f4 + f5 * f5;

												if (distSq >= 576F)
												{
													if (spawnlistentry == null)
													{
														spawnlistentry = getRandomSpawnListEntry(world, i1, j1, k1);
														if (spawnlistentry == null)
														{
															break entitySpawnLoop;
														}
													}
															
													EntityLiving entityliving;

													try
													{
														entityliving = (EntityLiving)spawnlistentry.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {world});
													}
													catch (Exception exception)
													{
														exception.printStackTrace();
														return;
													}

													entityliving.setLocationAndAngles((double)f, (double)f1, (double)f2, world.rand.nextFloat() * 360F, 0F);

													Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, world, f, f1, f2);
													if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT && entityliving.getCanSpawnHere()))
													{
														entitiesSpawnedInChunk++;
														world.spawnEntityInWorld(entityliving);
														if (!ForgeEventFactory.doSpecialSpawn(entityliving, world, f, f1, f2))
														{
															IEntityLivingData = entityliving.onSpawnWithEgg(IEntityLivingData);
														}

														if (entitiesSpawnedInChunk >= ForgeEventFactory.getMaxSpawnPackSize(entityliving))
														{
															continue spawnLoop;
														}
													}
												}
											}
										}

										++groupSpawnAttempts;
										continue;
									}
								}

								++groupsSpawnedInChunk;
								break;
							}
						}
					}
				}
			}
		}
    }

	private static int countNPCs(World world)
	{
		int count = 0;
		for (int i = 0; i < world.loadedEntityList.size(); i++)
		{
			Entity entity = (Entity)world.loadedEntityList.get(i);
			if (entity instanceof LOTREntityNPC)
			{
				int spawnCountValue = ((LOTREntityNPC)entity).getSpawnCountValue();
				count += spawnCountValue;
			}
		}
		return count;
	}

    private static boolean canEntityGroupSpawnAtLocation(World world, int i, int j, int k)
    {
        if (!world.doesBlockHaveSolidTopSurface(world, i, j - 1, k))
        {
            return false;
        }
        else
        {
            Block l = world.getBlock(i, j - 1, k);
			int l1 = world.getBlockMetadata(i, j - 1, k);
            boolean spawnBlock = l.canCreatureSpawn(EnumCreatureType.monster, world, i, j - 1, k);
            return spawnBlock && l != Blocks.bedrock && !world.getBlock(i, j, k).isNormalCube() && !world.getBlock(i, j, k).getMaterial().isLiquid() && !world.getBlock(i, j + 1, k).isNormalCube();
        }
	}
	
	private static SpawnListEntry getRandomSpawnListEntry(World world, int i, int j, int k)
	{
		List spawnlist = null;
		
		if (LOTRDimension.getCurrentDimension(world) == LOTRDimension.UTUMNO)
		{
			spawnlist = LOTRChunkProviderUtumno.UtumnoLevel.forY(j).getNPCSpawnList();
		}
		else
		{
			BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
			if (biome instanceof LOTRBiome)
			{
				spawnlist = ((LOTRBiome)biome).getNPCSpawnList();
			}
		}
		
		if (spawnlist != null && !spawnlist.isEmpty())
		{
			return (SpawnListEntry)WeightedRandom.getRandomItem(world.rand, spawnlist);
		}
		
		return null;
	}
}
