package lotr.common.world.mapgen.dwarvenmine;

import lotr.common.LOTRMod;
import lotr.common.world.biome.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class LOTRMapGenDwarvenMine extends MapGenStructure
{
    private double spawnChance = 0.0075D;

	@Override
    protected boolean canSpawnStructureAtCoords(int i, int j)
    {
		int i1 = i * 16 + 8;
		int k1 = j * 16 + 8;
		int j1 = worldObj.getHeightValue(i1, k1);
		if (j1 > 60)
		{
			BiomeGenBase biome = worldObj.getBiomeGenForCoords(i1, k1);
			if (biome instanceof LOTRBiomeGenIronHills || (biome instanceof LOTRBiomeGenBlueMountains && biome != LOTRBiome.blueMountainsFoothills))
			{
				Block block = worldObj.getBlock(i1, j1 - 1, k1);
				int meta = worldObj.getBlockMetadata(i1, j1 - 1, k1);
				if (block == Blocks.grass || (block == LOTRMod.rock && meta == 3))
				{
					return rand.nextDouble() < spawnChance;
				}
			}
		}
        return false;
    }

	@Override
    protected StructureStart getStructureStart(int i, int j)
    {
        return new LOTRStructureDwarvenMineStart(worldObj, rand, i, j);
    }
	
	@Override
    public String func_143025_a()
    {
        return "LOTR.DwarvenMine";
    }
}
