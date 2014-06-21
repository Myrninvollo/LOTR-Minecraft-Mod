package lotr.common.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lotr.common.LOTRLevelData;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRStructureSpawningInfo
{
	public static int TIME_INTERVAL = 3000;
	
	private List structureLocations;
	private Random rand = new Random();
	private long randomWorldTime;
	
	private int checkHorizontalRadius;
	private int checkVerticalMin;
	private int checkVerticalMax;
	private Class checkClass;
	private int checkLimit;
	
	private int spawnHorizontalRadius;
	private int spawnVerticalMin;
	private int spawnVerticalMax;
	private Class primarySpawnClass;
	private Class secondarySpawnClass;
	private int homeRadius;
	
	private boolean setHomePosFromSpawn = false;
	
	private List spawnBlocks = new ArrayList();
	
	private List specialEquipment = new ArrayList();
	
	public LOTRStructureSpawningInfo(List list)
	{
		structureLocations = list;
		randomWorldTime = (long)rand.nextInt(TIME_INTERVAL);
	}
	
	public LOTRStructureSpawningInfo setCheckInfo(int i, int j, int k, Class c, int l)
	{
		checkHorizontalRadius = i;
		checkVerticalMin = j;
		checkVerticalMax = k;
		checkClass = c;
		checkLimit = l;
		return this;
	}
	
	public LOTRStructureSpawningInfo setSpawnInfo(int i, int j, int k, Class c, Class c1, int l)
	{
		spawnHorizontalRadius = i;
		spawnVerticalMin = j;
		spawnVerticalMax = k;
		primarySpawnClass = c;
		secondarySpawnClass = c1;
		homeRadius = l;
		return this;
	}
	
	public LOTRStructureSpawningInfo setHomePosFromSpawn()
	{
		setHomePosFromSpawn = true;
		return this;
	}
	
	public LOTRStructureSpawningInfo addSpawnBlock(Block block)
	{
		return addSpawnBlock(block, -1);
	}
	
	public LOTRStructureSpawningInfo addSpawnBlock(Block block, int j)
	{
		spawnBlocks.add(new SpawnBlockInfo(block, j));
		return this;
	}
	
	public LOTRStructureSpawningInfo addSpecialEquipment(int i, ItemStack itemstack)
	{
		specialEquipment.add(new SpecialEquipmentInfo(i, itemstack));
		return this;
	}
	
	public static class SpawnBlockInfo
	{
		private Block id;
		private int meta;
		
		public SpawnBlockInfo(Block block, int i)
		{
			id = block;
			meta = i;
		}
		
		public boolean matchesBlockIDAndMeta(Block block, int i)
		{
			return id == block && (meta == -1 || meta == i);
		}
	}
	
	public static class SpecialEquipmentInfo
	{
		private int slot;
		private ItemStack equipment;
		
		public SpecialEquipmentInfo(int i, ItemStack itemstack)
		{
			slot = i;
			equipment = itemstack;
		}
		
		public void applyToEntity(LOTREntityNPC entity)
		{
			entity.setCurrentItemOrArmor(slot, equipment.copy());
		}
	}
	
	public void performSpawning(World world)
	{
		if (world.getWorldTime() % (long)TIME_INTERVAL != randomWorldTime || structureLocations.isEmpty())
		{
			return;
		}
		
		for (int l = 0; l < structureLocations.size(); l++)
		{
			ChunkCoordinates coords = (ChunkCoordinates)structureLocations.get(l);
			int posX = coords.posX;
			int posY = coords.posY;
			int posZ = coords.posZ;
			int minX = posX - checkHorizontalRadius;
			int minY = posY + checkVerticalMin;
			int minZ = posZ - checkHorizontalRadius;
			int maxX = posX + checkHorizontalRadius;
			int maxY = posY + checkVerticalMax;
			int maxZ = posZ + checkHorizontalRadius;
			
			if (world.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ) && world.getClosestPlayer(posX + 0.5D, posY + 0.5D, posZ + 0.5D, 24D) == null)
			{
				List nearbyEntities = world.getEntitiesWithinAABB(checkClass, AxisAlignedBB.getAABBPool().getAABB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1));
				int entities = nearbyEntities.size();
				if (entities < checkLimit)
				{
					spawningLoop:
					for (int l1 = 0; l1 < 16; l1++)
					{
						int i = posX - spawnHorizontalRadius + rand.nextInt(spawnHorizontalRadius * 2 + 1); 
						int j = posY + spawnVerticalMin + rand.nextInt(spawnVerticalMax - spawnVerticalMin + 1);
						int k = posZ - spawnHorizontalRadius + rand.nextInt(spawnHorizontalRadius * 2 + 1); 
						Block block = world.getBlock(i, j - 1, k);
						int meta = world.getBlockMetadata(i, j - 1, k);
						boolean match = false;
						
						for (int i2 = 0; i2 < spawnBlocks.size(); i2++)
						{
							SpawnBlockInfo obj = (SpawnBlockInfo)spawnBlocks.get(i2);
							if (obj.matchesBlockIDAndMeta(block, meta))
							{
								match = true;
								break;
							}
						}
						
						if (match && !world.getBlock(i, j, k).isNormalCube() && !world.getBlock(i, j + 1, k).isNormalCube())
						{
							LOTREntityNPC entity = (LOTREntityNPC)LOTREntities.createEntityByClass(rand.nextInt(3) == 0 ? secondarySpawnClass : primarySpawnClass, world);
							entity.setLocationAndAngles(i + 0.5D, j, k + 0.5D, rand.nextFloat() * 360F, 0F);
							entity.isNPCPersistent = true;
							entity.liftSpawnRestrictions = true;
							if (entity.getCanSpawnHere())
							{
								entity.liftSpawnRestrictions = false;
								world.spawnEntityInWorld(entity);
								entity.spawnRidingHorse = false;
								entity.onSpawnWithEgg(null);
								for (int i3 = 0; i3 < specialEquipment.size(); i3++)
								{
									SpecialEquipmentInfo obj = (SpecialEquipmentInfo)specialEquipment.get(i3);
									obj.applyToEntity(entity);
								}
								if (setHomePosFromSpawn)
								{
									entity.setHomeArea(i, j, k, homeRadius);
								}
								else
								{
									entity.setHomeArea(posX, posY, posZ, homeRadius);
								}
								entities++;
								if (entities >= checkLimit)
								{
									break spawningLoop;
								}
							}
						}
					}
				}
			}
		}
	}
}
