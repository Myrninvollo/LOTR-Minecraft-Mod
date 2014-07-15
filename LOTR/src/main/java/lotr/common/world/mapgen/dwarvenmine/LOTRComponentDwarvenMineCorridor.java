package lotr.common.world.mapgen.dwarvenmine;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class LOTRComponentDwarvenMineCorridor extends StructureComponent
{
    private int sectionCount;
	
	public LOTRComponentDwarvenMineCorridor() {}

    public LOTRComponentDwarvenMineCorridor(int i, Random random, StructureBoundingBox structureBoundingBox, int j)
    {
        super(i);
        coordBaseMode = j;
        boundingBox = structureBoundingBox;

        if (coordBaseMode != 2 && coordBaseMode != 0)
        {
            sectionCount = boundingBox.getXSize() / 4;
        }
        else
        {
            sectionCount = boundingBox.getZSize() / 4;
        }
    }
	
	@Override
    protected void func_143012_a(NBTTagCompound nbt)
	{
		nbt.setInteger("Sections", sectionCount);
	}

	@Override
    protected void func_143011_b(NBTTagCompound nbt)
	{
		sectionCount = nbt.getInteger("Sections");
	}

    public static StructureBoundingBox findValidPlacement(List list, Random random, int i, int j, int k, int l)
    {
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j, k, i, j + 3, k);
        int i1;

        for (i1 = random.nextInt(3) + 2; i1 > 0; --i1)
        {
            int j1 = i1 * 4;

            switch (l)
            {
                case 0:
                    structureboundingbox.maxX = i + 2;
                    structureboundingbox.maxZ = k + (j1 - 1);
                    break;
                case 1:
                    structureboundingbox.minX = i - (j1 - 1);
                    structureboundingbox.maxZ = k + 2;
                    break;
                case 2:
                    structureboundingbox.maxX = i + 2;
                    structureboundingbox.minZ = k - (j1 - 1);
                    break;
                case 3:
                    structureboundingbox.maxX = i + (j1 - 1);
                    structureboundingbox.maxZ = k + 2;
            }

            if (StructureComponent.findIntersecting(list, structureboundingbox) == null)
            {
                break;
            }
        }

        return i1 > 0 ? structureboundingbox : null;
    }

    @Override
    public void buildComponent(StructureComponent component, List list, Random random)
    {
        int i = getComponentType();
        int j = random.nextInt(4);

        switch (coordBaseMode)
        {
            case 0:
                if (j <= 1)
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ + 1, coordBaseMode, i);
                }
                else if (j == 2)
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ - 3, 1, i);
                }
                else
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ - 3, 3, i);
                }

                break;
            case 1:
                if (j <= 1)
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ, coordBaseMode, i);
                }
                else if (j == 2)
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ - 1, 2, i);
                }
                else
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ + 1, 0, i);
                }

                break;
            case 2:
                if (j <= 1)
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ - 1, coordBaseMode, i);
                }
                else if (j == 2)
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ, 1, i);
                }
                else
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ, 3, i);
                }

                break;
            case 3:
                if (j <= 1)
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ, coordBaseMode, i);
                }
                else if (j == 2)
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX - 3, boundingBox.minY - 1 + random.nextInt(3), boundingBox.minZ - 1, 2, i);
                }
                else
                {
                    LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX - 3, boundingBox.minY - 1 + random.nextInt(3), boundingBox.maxZ + 1, 0, i);
                }
        }

        if (i < 12)
        {
            int k;
            int l;

            if (coordBaseMode != 2 && coordBaseMode != 0)
            {
                for (k = boundingBox.minX + 3; k + 3 <= boundingBox.maxX; k += 4)
                {
                    l = random.nextInt(5);

                    if (l == 0)
                    {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, k, boundingBox.minY, boundingBox.minZ - 1, 2, i + 1);
                    }
                    else if (l == 1)
                    {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, k, boundingBox.minY, boundingBox.maxZ + 1, 0, i + 1);
                    }
                }
            }
            else
            {
                for (k = boundingBox.minZ + 3; k + 3 <= boundingBox.maxZ; k += 4)
                {
                    l = random.nextInt(5);

                    if (l == 0)
                    {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, k, 1, i + 1);
                    }
                    else if (l == 1)
                    {
                        LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, k, 3, i + 1);
                    }
                }
            }
        }
    }
	
    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox)
    {
        if (isLiquidInStructureBoundingBox(world, structureBoundingBox))
        {
            return false;
        }
        else
        {
            int i = sectionCount * 4 - 1;
            fillWithBlocks(world, structureBoundingBox, 0, 0, 0, 2, 2, i, Blocks.air, Blocks.air, false);

            int j;
            int k;

            for (j = 0; j < sectionCount; ++j)
            {
                k = 2 + j * 4;
                fillWithMetadataBlocks(world, structureBoundingBox, 0, 0, k, 0, 2, k, LOTRMod.wall, 7, Blocks.air, 0, false);
                fillWithMetadataBlocks(world, structureBoundingBox, 2, 0, k, 2, 2, k, LOTRMod.wall, 7, Blocks.air, 0, false);
                fillWithBlocks(world, structureBoundingBox, -1, 0, k, -1, 2, k, LOTRMod.pillar, Blocks.air, false);
                fillWithBlocks(world, structureBoundingBox, 3, 0, k, 3, 2, k, LOTRMod.pillar, Blocks.air, false);
				
				fillWithBlocks(world, structureBoundingBox, 1, -1, k - 2, 1, -1, k + 2, LOTRMod.pillar, Blocks.air, false);
				if (getBlockAtCurrentPosition(world, 1, -1, k - 3, structureBoundingBox) != Blocks.air)
				{
					placeBlockAtCurrentPosition(world, LOTRMod.pillar, 0, 1, -1, k - 3, structureBoundingBox);
				}
				if (getBlockAtCurrentPosition(world, 1, -1, k + 3, structureBoundingBox) != Blocks.air)
				{
					placeBlockAtCurrentPosition(world, LOTRMod.pillar, 0, 1, -1, k + 3, structureBoundingBox);
				}
				placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 1, -1, k, structureBoundingBox);
				
                if (random.nextInt(80) == 0)
                {
                    placeBlockAtCurrentPosition(world, Blocks.crafting_table, 0, 2, 0, k - 1, structureBoundingBox);
                }

                if (random.nextInt(80) == 0)
                {
                    placeBlockAtCurrentPosition(world, Blocks.crafting_table, 0, 0, 0, k + 1, structureBoundingBox);
                }
				
                if (random.nextInt(120) == 0)
                {
                    generateStructureChestContents(world, structureBoundingBox, random, 2, 0, k - 1, LOTRChestContents.DWARVEN_MINE_CORRIDOR.items, LOTRChestContents.getRandomItemAmount(LOTRChestContents.DWARVEN_MINE_CORRIDOR, random));
                }

                if (random.nextInt(120) == 0)
                {
                    generateStructureChestContents(world, structureBoundingBox, random, 0, 0, k + 1, LOTRChestContents.DWARVEN_MINE_CORRIDOR.items, LOTRChestContents.getRandomItemAmount(LOTRChestContents.DWARVEN_MINE_CORRIDOR, random));
                }
            }


			for (k = 0; k <= i; ++k)
			{
				for (j = -1; j <= 3; ++j)
				{
					Block l = getBlockAtCurrentPosition(world, j, -1, k, structureBoundingBox);

                    if (l == Blocks.air || l == Blocks.gravel || l == Blocks.water || l == Blocks.flowing_water)
                    {
                        placeBlockAtCurrentPosition(world, Blocks.stone, 0, j, -1, k, structureBoundingBox);
                    }
					
                    l = getBlockAtCurrentPosition(world, j, 3, k, structureBoundingBox);

                    if (l == Blocks.air || l == Blocks.gravel || l == Blocks.water || l == Blocks.flowing_water)
                    {
                        placeBlockAtCurrentPosition(world, Blocks.stone, 0, j, 3, k, structureBoundingBox);
                    }
                }
				
				for (j = 0; j <= 2; ++j)
				{
					Block l = getBlockAtCurrentPosition(world, -1, j, k, structureBoundingBox);

                    if (l == Blocks.air || l == Blocks.gravel || l == Blocks.water || l == Blocks.flowing_water)
                    {
                        placeBlockAtCurrentPosition(world, Blocks.stone, 0, -1, j, k, structureBoundingBox);
                    }
					
                    l = getBlockAtCurrentPosition(world, 3, j, k, structureBoundingBox);

                    if (l == Blocks.air || l == Blocks.gravel || l == Blocks.water || l == Blocks.flowing_water)
                    {
                        placeBlockAtCurrentPosition(world, Blocks.stone, 0, 3, j, k, structureBoundingBox);
                    }
                }
            }

            return true;
        }
    }
}
