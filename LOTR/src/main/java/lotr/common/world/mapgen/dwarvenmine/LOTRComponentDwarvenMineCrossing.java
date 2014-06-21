package lotr.common.world.mapgen.dwarvenmine;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class LOTRComponentDwarvenMineCrossing extends StructureComponent
{
    private int corridorDirection;
    private boolean isMultipleFloors;
	
	public LOTRComponentDwarvenMineCrossing() {}

    public LOTRComponentDwarvenMineCrossing(int i, Random random, StructureBoundingBox structureBoundingBox, int j)
    {
        super(i);
        corridorDirection = j;
        boundingBox = structureBoundingBox;
        isMultipleFloors = boundingBox.getYSize() > 3;
    }
	
	@Override
    protected void func_143012_a(NBTTagCompound nbt)
	{
		nbt.setInteger("Direction", corridorDirection);
		nbt.setBoolean("Multiple", isMultipleFloors);
	}

	@Override
    protected void func_143011_b(NBTTagCompound nbt)
	{
		corridorDirection = nbt.getInteger("Direction");
		isMultipleFloors = nbt.getBoolean("Multiple");
	}

    public static StructureBoundingBox findValidPlacement(List list, Random random, int i, int j, int k, int l)
    {
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j, k, i, j + 2, k);

        if (random.nextInt(4) == 0)
        {
            structureboundingbox.maxY += 4;
        }

        switch (l)
        {
            case 0:
                structureboundingbox.minX = i - 1;
                structureboundingbox.maxX = i + 3;
                structureboundingbox.maxZ = k + 4;
                break;
            case 1:
                structureboundingbox.minX = i - 4;
                structureboundingbox.minZ = k - 1;
                structureboundingbox.maxZ = k + 3;
                break;
            case 2:
                structureboundingbox.minX = i - 1;
                structureboundingbox.maxX = i + 3;
                structureboundingbox.minZ = k - 4;
                break;
            case 3:
                structureboundingbox.maxX = i + 4;
                structureboundingbox.minZ = k - 1;
                structureboundingbox.maxZ = k + 3;
        }

        return StructureComponent.findIntersecting(list, structureboundingbox) != null ? null : structureboundingbox;
    }

    @Override
    public void buildComponent(StructureComponent component, List list, Random random)
    {
        int i = getComponentType();

        switch (corridorDirection)
        {
            case 0:
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
                break;
            case 1:
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
                break;
            case 2:
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
                break;
            case 3:
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
        }

        if (isMultipleFloors)
        {
            if (random.nextBoolean())
            {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY + 3 + 1, boundingBox.minZ - 1, 2, i);
            }

            if (random.nextBoolean())
            {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY + 3 + 1, boundingBox.minZ + 1, 1, i);
            }

            if (random.nextBoolean())
            {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY + 3 + 1, boundingBox.minZ + 1, 3, i);
            }

            if (random.nextBoolean())
            {
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX + 1, boundingBox.minY + 3 + 1, boundingBox.maxZ + 1, 0, i);
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
			fillWithBlocks(world, structureBoundingBox, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ, Blocks.air, Blocks.air, false);
			fillWithBlocks(world, structureBoundingBox, boundingBox.minX, boundingBox.minY, boundingBox.minZ + 1, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ - 1, Blocks.air, Blocks.air, false);

            fillWithBlocks(world, structureBoundingBox, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ + 1, boundingBox.minX + 1, boundingBox.maxY, boundingBox.minZ + 1, LOTRMod.pillar, Blocks.air, false);
            fillWithBlocks(world, structureBoundingBox, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ - 1, boundingBox.minX + 1, boundingBox.maxY, boundingBox.maxZ - 1, LOTRMod.pillar, Blocks.air, false);
            fillWithBlocks(world, structureBoundingBox, boundingBox.maxX - 1, boundingBox.minY, boundingBox.minZ + 1, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.minZ + 1, LOTRMod.pillar, Blocks.air, false);
            fillWithBlocks(world, structureBoundingBox, boundingBox.maxX - 1, boundingBox.minY, boundingBox.maxZ - 1, boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ - 1, LOTRMod.pillar, Blocks.air, false);

            for (int i = boundingBox.minX; i <= boundingBox.maxX; ++i)
            {
                for (int j = boundingBox.minZ; j <= boundingBox.maxZ; ++j)
                {
                    Block k = getBlockAtCurrentPosition(world, i, boundingBox.minY - 1, j, structureBoundingBox);

                    if (k == Blocks.air || k == Blocks.gravel || k == Blocks.water || k == Blocks.flowing_water)
                    {
                        placeBlockAtCurrentPosition(world, Blocks.stone, 0, i, boundingBox.minY - 1, j, structureBoundingBox);
                    }
					
                    k = getBlockAtCurrentPosition(world, i, boundingBox.maxY + 1, j, structureBoundingBox);

                    if (k == Blocks.air || k == Blocks.gravel || k == Blocks.water || k == Blocks.flowing_water)
                    {
                        placeBlockAtCurrentPosition(world, Blocks.stone, 0, i, boundingBox.maxY + 1, j, structureBoundingBox);
                    }
                }
            }
			
			fillWithBlocks(world, structureBoundingBox, boundingBox.minX + 2, boundingBox.minY - 1, boundingBox.minZ - 1, boundingBox.minX + 2, boundingBox.minY - 1, boundingBox.maxZ + 1, LOTRMod.pillar, Blocks.air, false);
			fillWithBlocks(world, structureBoundingBox, boundingBox.minX - 1, boundingBox.minY - 1, boundingBox.minZ + 2, boundingBox.maxX + 1, boundingBox.minY - 1, boundingBox.minZ + 2, LOTRMod.pillar, Blocks.air, false);
			placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, boundingBox.minX + 2, boundingBox.minY - 1, boundingBox.minZ + 2, structureBoundingBox);

            return true;
        }
    }
}
