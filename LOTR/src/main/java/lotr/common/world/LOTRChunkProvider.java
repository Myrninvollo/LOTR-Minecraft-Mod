package lotr.common.world;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.*;
import lotr.common.world.mapgen.LOTRMapGenCaves;
import lotr.common.world.mapgen.LOTRMapGenRavine;
import lotr.common.world.mapgen.dwarvenmine.LOTRMapGenDwarvenMine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenStructure;

public class LOTRChunkProvider implements IChunkProvider
{
	private World worldObj;
    private Random rand;
	private BiomeGenBase[] biomesForGeneration;
	
	private static double COORDINATE_SCALE = 684.412D;
	private static double HEIGHT_SCALE = 1D;
	private static double MAIN_NOISE_SCALE_XZ = 400D;
	private static double MAIN_NOISE_SCALE_Y = 5000D;
	private static double DEPTH_NOISE_SCALE = 200D;
	private static double DEPTH_NOISE_EXP = 0.5D;
	private static double HEIGHT_STRETCH = 6D;
	private static double UPPER_LIMIT_SCALE = 512D;
	private static double LOWER_LIMIT_SCALE = 512D;
	
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen5;
    private NoiseGeneratorOctaves noiseGen6;
	private NoiseGeneratorOctaves stoneNoiseGen;
    private double[] noise1;
    private double[] noise2;
    private double[] noise3;
	private double[] noise5;
    private double[] noise6;
	private double[] stoneNoise = new double[256];
    private double[] heightNoise;
	private float[] biomeHeightNoise;
    private int[][] unusedIntArray = new int[32][32];
	
    private MapGenBase caveGenerator = new LOTRMapGenCaves();
	private MapGenBase ravineGenerator = new LOTRMapGenRavine();
	private MapGenStructure dwarvenMineGenerator = new LOTRMapGenDwarvenMine();

    public LOTRChunkProvider(World world, long l)
    {
        worldObj = world;
        rand = new Random(l);
        noiseGen1 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen2 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen3 = new NoiseGeneratorOctaves(rand, 8);
        stoneNoiseGen = new NoiseGeneratorOctaves(rand, 4);
		noiseGen5 = new NoiseGeneratorOctaves(rand, 10);
        noiseGen6 = new NoiseGeneratorOctaves(rand, 16);
    }

    private void generateTerrain(int i, int j, Block[] blocks)
    {
        byte byte0 = 4;
        byte byte1 = 32;
        byte byte2 = 63;
        int k = byte0 + 1;
        byte byte3 = 33;
        int l = byte0 + 1;
        biomesForGeneration = worldObj.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, i * 4 - 2, j * 4 - 2, k + 5, l + 5);
        heightNoise = initializeHeightNoise(heightNoise, i * byte0, 0, j * byte0, k, byte3, l);

        for (int i1 = 0; i1 < byte0; i1++)
        {
            for (int j1 = 0; j1 < byte0; j1++)
            {
                for (int k1 = 0; k1 < byte1; k1++)
                {
                    double d = 0.125D;
                    double d1 = heightNoise[((i1 + 0) * l + j1 + 0) * byte3 + k1 + 0];
                    double d2 = heightNoise[((i1 + 0) * l + j1 + 1) * byte3 + k1 + 0];
                    double d3 = heightNoise[((i1 + 1) * l + j1 + 0) * byte3 + k1 + 0];
                    double d4 = heightNoise[((i1 + 1) * l + j1 + 1) * byte3 + k1 + 0];
                    double d5 = (heightNoise[((i1 + 0) * l + j1 + 0) * byte3 + k1 + 1] - d1) * d;
                    double d6 = (heightNoise[((i1 + 0) * l + j1 + 1) * byte3 + k1 + 1] - d2) * d;
                    double d7 = (heightNoise[((i1 + 1) * l + j1 + 0) * byte3 + k1 + 1] - d3) * d;
                    double d8 = (heightNoise[((i1 + 1) * l + j1 + 1) * byte3 + k1 + 1] - d4) * d;

                    for (int l1 = 0; l1 < 8; ++l1)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i2 = 0; i2 < 4; ++i2)
                        {
                            int j2 = i2 + i1 * 4 << 12 | 0 + j1 * 4 << 8 | k1 * 8 + l1;
                            short s = 256;
                            j2 -= s;
                            double d14 = 0.25D;
                            double d15 = (d11 - d10) * d14;
                            double d16 = d10 - d15;

                            for (int k2 = 0; k2 < 4; ++k2)
                            {
                                if ((d16 += d15) > 0D)
                                {
                                    blocks[j2 += s] = Blocks.stone;
                                }
                                else if (k1 * 8 + l1 < byte2)
                                {
                                    blocks[j2 += s] = Blocks.water;
                                }
                                else
                                {
                                    blocks[j2 += s] = Blocks.air;
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    private void replaceBlocksForBiome(int i, int j, Block[] blocks, byte[] metadata, BiomeGenBase[] biomeArray)
    {
        byte byte0 = 63;
        double d = 0.03125D;
        stoneNoise = stoneNoiseGen.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for (int k = 0; k < 16; k++)
        {
            for (int l = 0; l < 16; l++)
            {
                BiomeGenBase biome = biomeArray[l + k * 16];
                double stoneNoiseValue = stoneNoise[k + l * 16];
                int i1 = (int)(stoneNoiseValue / 3D + 3D + rand.nextDouble() * 0.25D);
                int j1 = -1;
                Block topBlock = biome.topBlock;
                byte topMeta = 0;
                Block fillerBlock = biome.fillerBlock;
				byte fillerMeta = 0;
				
				if (biome instanceof LOTRBiome && ((LOTRBiome)biome).hasPodzol)
				{
					if (stoneNoiseValue > 1.75D)
		            {
						topBlock = Blocks.dirt;
						topMeta = 1;
		            }
		            else if (stoneNoiseValue > -0.95D)
		            {
		            	topBlock = Blocks.dirt;
		            	topMeta = 2;
		            }
				}
				
                for (int k1 = 255; k1 >= 0; --k1)
                {
                    int l1 = (l * 16 + k) * 256 + k1;
                    if (k1 <= 0 + rand.nextInt(5))
                    {
                        blocks[l1] = Blocks.bedrock;
                    }
                    else
                    {
                        Block byte3 = blocks[l1];
                        if (byte3 == Blocks.air)
                        {
                            j1 = -1;
                        }
                        else if (byte3 == Blocks.stone)
                        {
                            if (j1 == -1)
                            {
                                if (i1 <= 0)
                                {
                                	topBlock = Blocks.air;
                                	fillerBlock = Blocks.stone;
                                }
                                else if (k1 >= byte0 - 4 && k1 <= byte0 + 1)
                                {
                                	topBlock = biome.topBlock;
                                	fillerBlock = biome.fillerBlock;
                                }
                                if (k1 < byte0 && topBlock == Blocks.air)
                                {
                                	topBlock = Blocks.water;
                                }
                                j1 = i1;
                                if (k1 >= byte0 - 1)
                                {
                                	blocks[l1] = topBlock;
                                	metadata[l1] = topMeta;
                                }
                                else
                                {
                                    blocks[l1] = fillerBlock;
									metadata[l1] = fillerMeta;
                                }
                            }
                            else if (j1 > 0)
                            {
                                --j1;
                                blocks[l1] = fillerBlock;
								metadata[l1] = fillerMeta;
								
                                if (j1 == 0 && fillerBlock == Blocks.sand)
                                {
                                    j1 = rand.nextInt(4);
                                    fillerBlock = Blocks.sandstone;
                                }
								
                                if ((biome instanceof LOTRBiomeGenGondor || biome instanceof LOTRBiomeGenIthilien) && j1 == 0 && fillerBlock == Blocks.dirt)
                                {
                                    j1 = 6 + rand.nextInt(3);
                                    fillerBlock = LOTRMod.rock;
									fillerMeta = (byte)1;
                                }
								
                                else if (biome instanceof LOTRBiomeGenRohan && j1 == 0 && fillerBlock == Blocks.dirt)
                                {
                                    j1 = 6 + rand.nextInt(3);
                                    fillerBlock = LOTRMod.rock;
                                    fillerMeta = (byte)2;
                                }
                            }
                        }
                    }
                }
				
				if (biome instanceof LOTRBiomeGenMistyMountains && ((LOTRBiomeGenMistyMountains)biome).isSnowCovered() && biome != LOTRBiome.mistyMountainsFoothills)
				{
					int snowHeight = 110 - rand.nextInt(3);
					int stoneHeight = snowHeight - 40 - rand.nextInt(3);
					for (int k1 = 255; k1 >= stoneHeight; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (k1 >= snowHeight && blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.snow;
						}
						else if (blocks[l1] == Blocks.dirt || blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.stone;
						}
					}
				}
				
				if (biome instanceof LOTRBiomeGenGreyMountains && biome != LOTRBiome.greyMountainsFoothills)
				{
					int snowHeight = 150;
					int stoneHeight = snowHeight - 40 - rand.nextInt(3);
					for (int k1 = 255; k1 >= stoneHeight; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (k1 >= snowHeight && blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.snow;
						}
						else if (blocks[l1] == Blocks.dirt || blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.stone;
						}
					}
				}
				
				if (biome instanceof LOTRBiomeGenAngmarMountains)
				{
					int snowHeight = 130;
					int stoneHeight = snowHeight - 20 - rand.nextInt(3);
					for (int k1 = 255; k1 >= stoneHeight; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (k1 >= snowHeight && blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.snow;
						}
						else if (blocks[l1] == Blocks.dirt || blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.stone;
						}
					}
				}
				
				if (biome instanceof LOTRBiomeGenBlueMountains && biome != LOTRBiome.blueMountainsFoothills)
				{
					int snowHeight = 110;
					int stoneHeight = snowHeight - 20 - rand.nextInt(3);
					for (int k1 = 255; k1 >= stoneHeight; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (k1 >= snowHeight && blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.snow;
						}
						else if (blocks[l1] == Blocks.dirt || blocks[l1] == Blocks.grass)
						{
							blocks[l1] = LOTRMod.rock;
							metadata[l1] = (byte)3;
						}
					}
				}
				
				if (biome instanceof LOTRBiomeGenWhiteMountains && biome != LOTRBiome.whiteMountainsFoothills)
				{
					int height = 90 - rand.nextInt(3);
					for (int k1 = 255; k1 >= height; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (blocks[l1] == Blocks.dirt || blocks[l1] == Blocks.grass)
						{
							blocks[l1] = LOTRMod.rock;
							metadata[l1] = (byte)1;
						}
					}
				}
				
				if (biome instanceof LOTRBiomeGenTolfalas)
				{
					int stoneHeight = 90 - rand.nextInt(3);
					for (int k1 = 255; k1 >= stoneHeight; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (blocks[l1] == Blocks.dirt || blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.stone;
						}
					}
				}
				
				if (biome instanceof LOTRBiomeGenMordor)
				{
					for (int k1 = 255; k1 >= 0; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (blocks[l1] == Blocks.stone)
						{
							blocks[l1] = LOTRMod.rock;
						}
					}
				}
				
				if (biome instanceof LOTRBiomeGenHaradMountains)
				{
					int stoneHeight = 90 - rand.nextInt(3);
					for (int k1 = 255; k1 >= stoneHeight; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (blocks[l1] == Blocks.dirt || blocks[l1] == Blocks.grass)
						{
							blocks[l1] = Blocks.stone;
						}
					}
				}
				
				if (biome instanceof LOTRBiomeGenRedMountains && biome != LOTRBiome.redMountainsFoothills)
				{
					int stoneHeight = 90 - rand.nextInt(3);
					for (int k1 = 255; k1 >= stoneHeight; k1--)
					{
						int l1 = (l * 16 + k) * 256 + k1;
						if (blocks[l1] == Blocks.dirt || blocks[l1] == Blocks.grass)
						{
							blocks[l1] = LOTRMod.rock;
							metadata[l1] = (byte)4;
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
        generateTerrain(i, j, blocks);
        biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, i * 16, j * 16, 16, 16);
        replaceBlocksForBiome(i, j, blocks, metadata, biomesForGeneration);
		
        caveGenerator.func_151539_a(this, worldObj, i, j, blocks);
        ravineGenerator.func_151539_a(this, worldObj, i, j, blocks);
        dwarvenMineGenerator.func_151539_a(this, worldObj, i, j, blocks);
		
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
		
        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] initializeHeightNoise(double[] noise, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        if (noise == null)
        {
            noise = new double[par5 * par6 * par7];
        }
        if (biomeHeightNoise == null)
        {
            biomeHeightNoise = new float[25];

            for (int var8 = -2; var8 <= 2; ++var8)
            {
                for (int var9 = -2; var9 <= 2; ++var9)
                {
                    float var10 = 10F / MathHelper.sqrt_float((float)(var8 * var8 + var9 * var9) + 0.2F);
                    biomeHeightNoise[var8 + 2 + (var9 + 2) * 5] = var10;
                }
            }
        }

        noise5 = noiseGen5.generateNoiseOctaves(noise5, par2, par4, par5, par7, 1.121D, 1.121D, DEPTH_NOISE_EXP);
        noise6 = noiseGen6.generateNoiseOctaves(noise6, par2, par4, par5, par7, DEPTH_NOISE_SCALE, DEPTH_NOISE_SCALE, DEPTH_NOISE_EXP);
        noise3 = noiseGen3.generateNoiseOctaves(noise3, par2, par3, par4, par5, par6, par7, COORDINATE_SCALE / MAIN_NOISE_SCALE_XZ, HEIGHT_SCALE / MAIN_NOISE_SCALE_Y, COORDINATE_SCALE / MAIN_NOISE_SCALE_XZ);
        noise1 = noiseGen1.generateNoiseOctaves(noise1, par2, par3, par4, par5, par6, par7, COORDINATE_SCALE, HEIGHT_SCALE, COORDINATE_SCALE);
        noise2 = noiseGen2.generateNoiseOctaves(noise2, par2, par3, par4, par5, par6, par7, COORDINATE_SCALE, HEIGHT_SCALE, COORDINATE_SCALE);
        boolean var43 = false;
        boolean var42 = false;
        int var12 = 0;
        int var13 = 0;

        for (int var14 = 0; var14 < par5; ++var14)
        {
            for (int var15 = 0; var15 < par7; ++var15)
            {
                float var16 = 0F;
                float var17 = 0F;
                float var18 = 0F;
                byte var19 = 2;
                BiomeGenBase var20 = biomesForGeneration[var14 + 2 + (var15 + 2) * (par5 + 5)];

                for (int var21 = -var19; var21 <= var19; ++var21)
                {
                    for (int var22 = -var19; var22 <= var19; ++var22)
                    {
                        BiomeGenBase var23 = biomesForGeneration[var14 + var21 + 2 + (var15 + var22 + 2) * (par5 + 5)];
                        float var24 = biomeHeightNoise[var21 + 2 + (var22 + 2) * 5] / (var23.rootHeight + 2F) / 2F;
						var24 = Math.abs(var24);
                        if (var23.rootHeight > var20.rootHeight)
                        {
                            var24 /= 2F;
                        }

                        var16 += var23.heightVariation * var24;
                        var17 += var23.rootHeight * var24;
                        var18 += var24;
                    }
                }
                var16 /= var18;
                var17 /= var18;
                var16 = var16 * 0.9F + 0.1F;
                var17 = (var17 * 4F - 1F) / 8F;
                double var47 = noise6[var13] / 8000D;
                if (var47 < 0D)
                {
                    var47 = -var47 * 0.3D;
                }
                var47 = var47 * 3D - 2D;
				if (var47 < 0D)
                {
                    var47 /= 2D;
                    if (var47 < -1D)
                    {
                        var47 = -1D;
                    }
                    var47 /= 1.4D;
                    var47 /= 2D;
                }
                else
                {
                    if (var47 > 1D)
                    {
                        var47 = 1D;
                    }
                    var47 /= 8D;
                }
                ++var13;
                for (int var46 = 0; var46 < par6; ++var46)
                {
                    double var48 = (double)var17;
                    double var26 = (double)var16;
                    var48 += var47 * 0.2D;
                    var48 = var48 * (double)par6 / 16D;
                    double var28 = (double)par6 / 2D + var48 * 4D;
                    double var30 = 0D;
                    double var32 = ((double)var46 - var28) * HEIGHT_STRETCH * 128D / 256D / var26;
                    if (var32 < 0D)
                    {
                        var32 *= 4D;
                    }
                    double var34 = noise1[var12] / UPPER_LIMIT_SCALE;
                    double var36 = noise2[var12] / LOWER_LIMIT_SCALE;
                    double var38 = (noise3[var12] / 10D + 1D) / 2D;
                    if (var38 < 0D)
                    {
                        var30 = var34;
                    }
                    else if (var38 > 1D)
                    {
                        var30 = var36;
                    }
                    else
                    {
                        var30 = var34 + (var36 - var34) * var38;
                    }
                    var30 -= var32;
                    if (var46 > par6 - 4)
                    {
                        double var40 = (double)((float)(var46 - (par6 - 4)) / 3F);
                        var30 = var30 * (1D - var40) + -10D * var40;
                    }
                    noise[var12] = var30;
                    ++var12;
                }
            }
        }
        return noise;
    }

	@Override
    public boolean chunkExists(int i, int j)
    {
        return true;
    }

	@Override
    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        BlockSand.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(k + 16, l + 16);
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
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
		
        dwarvenMineGenerator.generateStructuresInChunk(worldObj, rand, i, j);
		
        if (rand.nextInt(4) == 0)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int j2 = rand.nextInt(128);
            int k3 = l + rand.nextInt(16) + 8;
			
			if (j2 < 60 || rand.nextFloat() < biome.getChanceToSpawnLakes())
			{
				(new WorldGenLakes(Blocks.water)).generate(worldObj, rand, i1, j2, k3);
			}
        }
		
        if (rand.nextInt(8) == 0)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int j1 = rand.nextInt(rand.nextInt(120) + 8);
            int k1 = l + rand.nextInt(16) + 8;

            if (j1 < 60 || rand.nextFloat() < biome.getChanceToSpawnLavaLakes())
            {
                (new WorldGenLakes(Blocks.lava)).generate(worldObj, rand, i1, j1, k1);
            }
        }
		
		biome.decorate(worldObj, rand, k, l);
		
		if (biome.getChanceToSpawnAnimals() <= 1F)
		{
			if (rand.nextFloat() < biome.getChanceToSpawnAnimals())
			{
				SpawnerAnimals.performWorldGenSpawning(worldObj, biome, k + 8, l + 8, 16, 16, rand);
			}
		}
		else
		{
			for (int i1 = 0; i1 < MathHelper.floor_double(biome.getChanceToSpawnAnimals()); i1++)
			{
				SpawnerAnimals.performWorldGenSpawning(worldObj, biome, k + 8, l + 8, 16, 16, rand);
			}
		}
		
        k += 8;
        l += 8;

        for (int i1 = 0; i1 < 16; i1++)
        {
            for (int k1 = 0; k1 < 16; k1++)
            {
                int j1 = worldObj.getPrecipitationHeight(k + i1, l + k1);

                if (worldObj.isBlockFreezable(i1 + k, j1 - 1, k1 + l))
                {
                    worldObj.setBlock(i1 + k, j1 - 1, k1 + l, Blocks.ice, 0, 2);
                }

                if (worldObj.func_147478_e(i1 + k, j1, k1 + l, true))
                {
                    worldObj.setBlock(i1 + k, j1, k1 + l, Blocks.snow_layer, 0, 2);
                }
            }
        }

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
        return "MiddleEarthLevelSource";
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
    public void recreateStructures(int i, int j)
	{
		dwarvenMineGenerator.func_151539_a(this, worldObj, i, j, null);
	}
}
