package lotr.common.world.mapgen;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenMordor;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.MapGenRavine;

public class LOTRMapGenRavine extends MapGenRavine
{
    private float[] ravineNoise = new float[1024];

	@Override
    protected void func_151538_a(World world, int i, int k, int chunkX, int chunkZ, Block[] blocks)
    {
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(chunkX * 16, chunkZ * 16);
		boolean mordor = biome instanceof LOTRBiomeGenMordor && ((LOTRBiomeGenMordor)biome).isGorgoroth();
        if (mordor || rand.nextBoolean())
		{
			super.func_151538_a(world, i, k, chunkX, chunkZ, blocks);
		}
	}
		
	@Override
    protected void func_151540_a(long seed, int chunkX, int chunkZ, Block[] blocks, double d, double d1, double d2, float f, float f1, float f2, int intPar1, int intPar2, double increase)
    {
        Random random = new Random(seed);
        double d4 = (double)(chunkX * 16 + 8);
        double d5 = (double)(chunkZ * 16 + 8);
        float f3 = 0F;
        float f4 = 0F;

        if (intPar2 <= 0)
        {
            int j1 = range * 16 - 16;
            intPar2 = j1 - random.nextInt(j1 / 4);
        }

        boolean flag = false;

        if (intPar1 == -1)
        {
            intPar1 = intPar2 / 2;
            flag = true;
        }

        float f5 = 1F;

        for (int k1 = 0; k1 < 256; k1++)
        {
            if (k1 == 0 || random.nextInt(3) == 0)
            {
                f5 = 1F + random.nextFloat() * random.nextFloat() * 1F;
            }

            ravineNoise[k1] = f5 * f5;
        }

        for (; intPar1 < intPar2; ++intPar1)
        {
            double d6 = 1.5D + (double)(MathHelper.sin((float)intPar1 * (float)Math.PI / (float)intPar2) * f * 1F);
            double d7 = d6 * increase;
            d6 *= (double)random.nextFloat() * 0.25D + 0.75D;
            d7 *= (double)random.nextFloat() * 0.25D + 0.75D;
            float f6 = MathHelper.cos(f2);
            float f7 = MathHelper.sin(f2);
            d += (double)(MathHelper.cos(f1) * f6);
            d1 += (double)f7;
            d2 += (double)(MathHelper.sin(f1) * f6);
            f2 *= 0.7F;
            f2 += f4 * 0.05F;
            f1 += f3 * 0.05F;
            f4 *= 0.8F;
            f3 *= 0.5F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4F;

            if (flag || random.nextInt(4) != 0)
            {
                double d8 = d - d4;
                double d9 = d2 - d5;
                double d10 = (double)(intPar2 - intPar1);
                double d11 = (double)(f + 2F + 16F);

                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11)
                {
                    return;
                }

                if (d >= d4 - 16D - d6 * 2D && d2 >= d5 - 16D - d6 * 2D && d <= d4 + 16D + d6 * 2D && d2 <= d5 + 16D + d6 * 2D)
                {
                    int l1 = MathHelper.floor_double(d - d6) - chunkX * 16 - 1;
                    int i2 = MathHelper.floor_double(d + d6) - chunkX * 16 + 1;
                    int j2 = MathHelper.floor_double(d1 - d7) - 1;
                    int k2 = MathHelper.floor_double(d1 + d7) + 1;
                    int l2 = MathHelper.floor_double(d2 - d6) - chunkZ * 16 - 1;
                    int i3 = MathHelper.floor_double(d2 + d6) - chunkZ * 16 + 1;

                    if (l1 < 0)
                    {
                        l1 = 0;
                    }

                    if (i2 > 16)
                    {
                        i2 = 16;
                    }

                    if (j2 < 1)
                    {
                        j2 = 1;
                    }

                    if (k2 > 120)
                    {
                        k2 = 120;
                    }

                    if (l2 < 0)
                    {
                        l2 = 0;
                    }

                    if (i3 > 16)
                    {
                        i3 = 16;
                    }

                    boolean flag1 = false;
                    int j3;
                    int k3;

                    for (j3 = l1; !flag1 && j3 < i2; ++j3)
                    {
                        for (int l3 = l2; !flag1 && l3 < i3; ++l3)
                        {
                            for (int i4 = k2 + 1; !flag1 && i4 >= j2 - 1; --i4)
                            {
                                k3 = (j3 * 16 + l3) * 256 + i4;

                                if (i4 >= 0 && i4 < 256)
                                {
                                    if (isOceanBlock(blocks, k3, j3, i4, l3, chunkX, chunkZ))
                                    {
                                        flag1 = true;
                                    }

                                    if (i4 != j2 - 1 && j3 != l1 && j3 != i2 - 1 && l3 != l2 && l3 != i3 - 1)
                                    {
                                        i4 = j2;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag1)
                    {
                        for (j3 = l1; j3 < i2; ++j3)
                        {
                            double d12 = ((double)(j3 + chunkX * 16) + 0.5D - d) / d6;

                            for (k3 = l2; k3 < i3; ++k3)
                            {
                                double d13 = ((double)(k3 + chunkZ * 16) + 0.5D - d2) / d6;
                                int j4 = (j3 * 16 + k3) * 256 + k2;
                                boolean flag2 = false;

                                if (d12 * d12 + d13 * d13 < 1D)
                                {
                                    for (int k4 = k2 - 1; k4 >= j2; --k4)
                                    {
                                        double d14 = ((double)k4 + 0.5D - d1) / d7;

                                        if ((d12 * d12 + d13 * d13) * (double)ravineNoise[k4] + d14 * d14 / 6D < 1D)
                                        {
                                            if (isTopBlock(blocks, j4, j3, k4, k3, chunkX, chunkZ))
                                            {
                                                flag2 = true;
                                            }

                                            digBlock(blocks, j4, j3, k4, k3, chunkX, chunkZ, flag2);
                                        }

                                        --j4;
                                    }
                                }
                            }
                        }

                        if (flag)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }
	
    private boolean isTopBlock(Block[] data, int index, int i, int j, int k, int chunkX, int chunkZ)
    {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(i + chunkX * 16, k + chunkZ * 16);
        return data[index] == biome.topBlock;
    }
	
	@Override
    protected void digBlock(Block[] data, int index, int i, int j, int k, int chunkX, int chunkZ, boolean foundTop)
    {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(i + chunkX * 16, k + chunkZ * 16);
		
        Block top = biome.topBlock;
        Block filler = biome.fillerBlock;
        Block block = data[index];

        if (block == Blocks.stone || block == LOTRMod.rock || block == filler || block == top)
        {
            if (j < 10)
            {
                data[index] = Blocks.flowing_lava;
            }
			else if (biome instanceof LOTRBiomeGenMordor && ((LOTRBiomeGenMordor)biome).isGorgoroth() && j < 60)
			{
				data[index] = Blocks.flowing_lava;
			}
            else
            {
                data[index] = Blocks.air;

                if (foundTop && data[index - 1] == filler)
                {
                    data[index - 1] = top;
                }
            }
        }
	}
}
