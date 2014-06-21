package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenMordorLava extends WorldGenerator
{
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        if (world.getBlock(i, j + 1, k) != LOTRMod.rock || world.getBlockMetadata(i, j + 1, k) != 0)
        {
            return false;
        }
        else if (world.getBlock(i, j - 1, k) != LOTRMod.rock || world.getBlockMetadata(i, j - 1, k) != 0)
        {
            return false;
        }
        else if (world.getBlock(i, j, k).getMaterial() != Material.air && (world.getBlock(i, j, k) != LOTRMod.rock || world.getBlockMetadata(i, j, k) != 0))
        {
            return false;
        }
        else
        {
            int sides = 0;

            if (world.getBlock(i - 1, j, k) == LOTRMod.rock && world.getBlockMetadata(i - 1, j, k) == 0)
            {
                ++sides;
            }

            if (world.getBlock(i + 1, j, k) == LOTRMod.rock && world.getBlockMetadata(i + 1, j, k) == 0)
            {
                ++sides;
            }

            if (world.getBlock(i, j, k - 1) == LOTRMod.rock && world.getBlockMetadata(i, j, k - 1) == 0)
            {
                ++sides;
            }

            if (world.getBlock(i, j, k + 1) == LOTRMod.rock && world.getBlockMetadata(i, j, k + 1) == 0)
            {
                ++sides;
            }

            int openAir = 0;

            if (world.isAirBlock(i - 1, j, k))
            {
                ++openAir;
            }

            if (world.isAirBlock(i + 1, j, k))
            {
                ++openAir;
            }

            if (world.isAirBlock(i, j, k - 1))
            {
                ++openAir;
            }

            if (world.isAirBlock(i, j, k + 1))
            {
                ++openAir;
            }

            if (sides == 3 && openAir == 1)
            {
                world.setBlock(i, j, k, Blocks.flowing_lava, 0, 2);
                world.scheduledUpdatesAreImmediate = true;
                Blocks.flowing_lava.updateTick(world, i, j, k, random);
                world.scheduledUpdatesAreImmediate = false;
            }

            return true;
        }
    }
}
