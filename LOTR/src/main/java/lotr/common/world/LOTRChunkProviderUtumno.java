package lotr.common.world;

import java.util.*;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.google.common.primitives.Ints;

public class LOTRChunkProviderUtumno implements IChunkProvider
{
	public static enum UtumnoLevel
	{
		ICE(0xD2DFEF, 180, 250, 4, 4, new int[] {2, 3}),
		OBSIDIAN(0x201B2D, 90, 180, 6, 5, new int[] {4, 5}),
		FIRE(0x600E00, 0, 90, 8, 6, new int[] {0, 1});
		
		public final int fogColor;
		
		public final int baseLevel;
		public final int topLevel;
		public final int corridorWidth;
		public final int corridorWidthStart;
		public final int corridorWidthEnd;
		public final int corridorHeight;
		public final int[] corridorBaseLevels;
		
		public final int brickMeta;
		public final int brickMetaGlow;
		
		private List npcSpawnList = new ArrayList();
		
		private UtumnoLevel(int fog, int base, int top, int cWidth, int cHeight, int[] meta)
		{
			fogColor = fog;
			
			baseLevel = base;
			topLevel = top;
			corridorWidth = cWidth;
			corridorWidthStart = 8 - cWidth / 2;
			corridorWidthEnd = corridorWidthStart + cWidth;
			corridorHeight = cHeight;
			
			List<Integer> baseLevels = new ArrayList();
			int y = baseLevel;
			while (true)
			{
				y += corridorHeight * 2;
				if (y >= top - 5)
				{
					break;
				}
				baseLevels.add(y);
			}
			corridorBaseLevels = Ints.toArray(baseLevels);
			
			brickMeta = meta[0];
			brickMetaGlow = meta[1];
		}
		
		public List getNPCSpawnList()
		{
			return npcSpawnList;
		}
		
		public static UtumnoLevel forY(int y)
		{
            for (UtumnoLevel level : values())
            {
            	if (y >= level.baseLevel)
            	{
            		return level;
            	}
            }
            return FIRE;
		}
		
		private static boolean createdSpawnLists = false;
		
		public static void createSpawnLists()
		{
			if (createdSpawnLists)
			{
				return;
			}
			
			createdSpawnLists = true;
			
			ICE.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoOrc.class, 20, 4, 4));
			ICE.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoOrcArcher.class, 10, 4, 4));
			ICE.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoWarg.class, 20, 4, 4));
			ICE.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoIceSpider.class, 10, 4, 4));
			
			OBSIDIAN.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoOrc.class, 20, 4, 4));
			OBSIDIAN.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoOrcArcher.class, 10, 4, 4));
			OBSIDIAN.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoWarg.class, 20, 4, 4));
			
			FIRE.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoOrc.class, 20, 4, 4));
			FIRE.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoOrcArcher.class, 10, 4, 4));
			FIRE.npcSpawnList.add(new SpawnListEntry(LOTREntityUtumnoWarg.class, 20, 4, 4));
		}
	}
	
	private World worldObj;
	private Random rand;
    private Random lightRand;
	private BiomeGenBase[] biomesForGeneration;

    public LOTRChunkProviderUtumno(World world, long l)
    {
        worldObj = world;
        rand = new Random(l);
        lightRand = new Random(l);
        
        UtumnoLevel.createSpawnLists();
    }

    private void generateTerrain(int chunkX, int chunkZ, Block[] blocks, byte[] metadata, BiomeGenBase[] biomeArray)
    {
    	Arrays.fill(blocks, Blocks.air);
    	
    	boolean hugeHoleChunk = rand.nextInt(20) == 0;
    	boolean hugeRavineChunk = rand.nextInt(20) == 0;
    	
    	long seed = worldObj.getSeed();
    	seed *= (chunkX / 2) * 67839703L + (chunkZ / 2) * 368093693L;
    	lightRand.setSeed(seed);
    	boolean lightBricks = lightRand.nextInt(4) > 0;
    	
        for (int i = 0; i < 16; i++)
        {
            for (int k = 0; k < 16; k++)
            {
                BiomeGenBase biome = biomeArray[k + i * 16];
				
                for (int j = 255; j >= 0; --j)
                {
                	UtumnoLevel utumnoLevel = UtumnoLevel.forY(j);
                    
                    int blockIndex = (k * 16 + i) * 256 + j;
                    if (j <= 0 + rand.nextInt(5) || j >= 255 - rand.nextInt(5))
                    {
                        blocks[blockIndex] = Blocks.bedrock;
                    }
                    else
                    {
                		blocks[blockIndex] = LOTRMod.utumnoBrick;
                    	metadata[blockIndex] = (byte)utumnoLevel.brickMeta;
                    	
                    	if (lightBricks)
                    	{
	                    	if (utumnoLevel == UtumnoLevel.ICE && rand.nextInt(16) == 0)
	                    	{
	                        	metadata[blockIndex] = (byte)utumnoLevel.brickMetaGlow;
	                    	}
	                    	else if (utumnoLevel == UtumnoLevel.OBSIDIAN && rand.nextInt(12) == 0)
	                    	{
	                    		metadata[blockIndex] = (byte)utumnoLevel.brickMetaGlow;
	                    	}
	                    	else if (utumnoLevel == UtumnoLevel.FIRE && rand.nextInt(8) == 0)
	                    	{
	                    		metadata[blockIndex] = (byte)utumnoLevel.brickMetaGlow;
	                    	}
                    	}
                    }
                    
                    boolean carveHugeHole = hugeHoleChunk && j >= utumnoLevel.corridorBaseLevels[0] && j < utumnoLevel.corridorBaseLevels[utumnoLevel.corridorBaseLevels.length - 1];
                    boolean carveHugeRavine = hugeRavineChunk && j >= utumnoLevel.corridorBaseLevels[0] && j < utumnoLevel.corridorBaseLevels[utumnoLevel.corridorBaseLevels.length - 1];

                    boolean carveCorridor = false;
                    for (int corridorBase : utumnoLevel.corridorBaseLevels)
                    {
                    	carveCorridor = j >= corridorBase && j < corridorBase + utumnoLevel.corridorHeight;
                    	if (carveCorridor)
                    	{
                    		break;
                    	}
                    }
                    
                	if (carveHugeHole && chunkX % 2 == 0 && chunkZ % 2 == 0)
                	{
                		if (i >= utumnoLevel.corridorWidthStart + 1 && i <= utumnoLevel.corridorWidthEnd - 1 && k >= utumnoLevel.corridorWidthStart + 1 && k <= utumnoLevel.corridorWidthEnd - 1)
                		{
                			blocks[blockIndex] = Blocks.air;
                			metadata[blockIndex] = 0;
                		}
                		else if (i >= utumnoLevel.corridorWidthStart && i <= utumnoLevel.corridorWidthEnd && k >= utumnoLevel.corridorWidthStart && k <= utumnoLevel.corridorWidthEnd)
                		{
                			blocks[blockIndex] = LOTRMod.utumnoBrick;
                        	metadata[blockIndex] = (byte)utumnoLevel.brickMetaGlow;
                		}
                	}
                	
                	if (chunkX % 2 == 0)
                	{
                		if (carveCorridor && k >= utumnoLevel.corridorWidthStart && k <= utumnoLevel.corridorWidthEnd)
                		{
                			blocks[blockIndex] = Blocks.air;
                			metadata[blockIndex] = 0;
                		}
                		
                		if (carveHugeRavine && i >= utumnoLevel.corridorWidthStart + 1 && i <= utumnoLevel.corridorWidthEnd - 1)
                		{
                			blocks[blockIndex] = Blocks.air;
                			metadata[blockIndex] = 0;
                		}
                	}
                	
                	if (chunkZ % 2 == 0)
                	{
                		if (carveCorridor && i >= utumnoLevel.corridorWidthStart && i <= utumnoLevel.corridorWidthEnd)
                		{
                			blocks[blockIndex] = Blocks.air;
                			metadata[blockIndex] = 0;
                		}
                		
                		if (carveHugeRavine && k >= utumnoLevel.corridorWidthStart + 1 && k <= utumnoLevel.corridorWidthEnd - 1)
                		{
                			blocks[blockIndex] = Blocks.air;
                			metadata[blockIndex] = 0;
                		}
                	}
                }
            }
        }
	}
	
	@Override
    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

	@Override
    public Chunk provideChunk(int i, int j)
    {
        rand.setSeed((long)i * 341873128712L + (long)j * 132897987541L);
        
        Block[] blocks = new Block[65536];
		byte[] metadata = new byte[65536];
		biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, i * 16, j * 16, 16, 16);
        generateTerrain(i, j, blocks, metadata, biomesForGeneration);

        Chunk chunk = new Chunk(worldObj, i, j);
		ExtendedBlockStorage blockStorage[] = chunk.getBlockStorageArray();
		
        byte[] biomes = chunk.getBiomeArray();
        for (int k = 0; k < biomes.length; k++)
        {
            biomes[k] = (byte)biomesForGeneration[k].biomeID;
        }
		
        for (int i1 = 0; i1 < 16; i1++)
        {
            for (int k1 = 0; k1 < 16; k1++)
            {
                for (int j1 = 0; j1 < 256; j1++)
                {
                    Block block = blocks[i1 << 12 | k1 << 8 | j1];

                    if (block == Blocks.air)
                    {
                        continue;
                    }
                    
					byte meta = metadata[i1 << 12 | k1 << 8 | j1];

                    int j2 = j1 >> 4;

                    if (blockStorage[j2] == null)
                    {
                        blockStorage[j2] = new ExtendedBlockStorage(j2 << 4, true);
                    }

                    blockStorage[j2].func_150818_a(i1, j1 & 15, k1, block);
					blockStorage[j2].setExtBlockMetadata(i1, j1 & 15, k1, meta & 15);
                }
            }
        }
		
        chunk.resetRelightChecks();
        return chunk;
    }

	@Override
    public boolean chunkExists(int i, int j)
    {
        return true;
    }

	@Override
    public void populate(IChunkProvider ichunkprovider, int chunkX, int chunkZ)
    {
        BlockSand.fallInstantly = true;
        int i = chunkX * 16;
        int k = chunkZ * 16;
        BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(i + 16, k + 16);
		LOTRBiome biome;
		if (biomegenbase instanceof LOTRBiome)
		{
			biome = (LOTRBiome)biomegenbase;
		}
		else
		{
			return;
		}
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)chunkX * l1 + (long)chunkZ * l2 ^ worldObj.getSeed());
		
		biome.decorate(worldObj, rand, i, k);
		
		BlockSand.fallInstantly = false;
    }

	@Override
    public boolean saveChunks(boolean flag, IProgressUpdate update)
    {
        return true;
    }
	
	@Override
	public void saveExtraData() {}

	@Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

	@Override
    public boolean canSave()
    {
        return true;
    }

	@Override
    public String makeString()
    {
        return "UtumnoLevelSource";
    }
	
	@Override
    public List getPossibleCreatures(EnumCreatureType creatureType, int i, int j, int k)
    {
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		return biome == null ? null : biome.getSpawnableList(creatureType);
    }

	@Override
    public ChunkPosition func_147416_a(World world, String type, int i, int j, int k)
    {
        return null;
    }
	
	@Override
    public int getLoadedChunkCount()
    {
        return 0;
    }
	
	@Override
    public void recreateStructures(int i, int j) {}
}
