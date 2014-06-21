package lotr.common.world.mapgen.dwarvenmine;

import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class LOTRComponentDwarvenMineStairs extends StructureComponent
{
	public LOTRComponentDwarvenMineStairs() {}
	
    public LOTRComponentDwarvenMineStairs(int i, Random random, StructureBoundingBox structureBoundingBox, int j)
    {
        super(i);
        coordBaseMode = j;
        boundingBox = structureBoundingBox;
    }
	
	@Override
    protected void func_143012_a(NBTTagCompound nbt) {}

	@Override
    protected void func_143011_b(NBTTagCompound nbt) {}

    public static StructureBoundingBox findValidPlacement(List list, Random random, int i, int j, int k, int l)
    {
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j - 5, k, i, j + 2, k);

        switch (l)
        {
            case 0:
                structureboundingbox.maxX = i + 2;
                structureboundingbox.maxZ = k + 8;
                break;
            case 1:
                structureboundingbox.minX = i - 8;
                structureboundingbox.maxZ = k + 2;
                break;
            case 2:
                structureboundingbox.maxX = i + 2;
                structureboundingbox.minZ = k - 8;
                break;
            case 3:
                structureboundingbox.maxX = i + 8;
                structureboundingbox.maxZ = k + 2;
        }

        return StructureComponent.findIntersecting(list, structureboundingbox) != null ? null : structureboundingbox;
    }

    @Override
    public void buildComponent(StructureComponent component, List list, Random random)
    {
        int i = getComponentType();

        switch (coordBaseMode)
        {
            case 0:
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
                break;
            case 1:
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ, 1, i);
                break;
            case 2:
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.minX, boundingBox.minY, boundingBox.minZ - 1, 2, i);
                break;
            case 3:
                LOTRStructureDwarvenMinePieces.getNextComponent(component, list, random, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ, 3, i);
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
            fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 2, 7, 1, Blocks.air, Blocks.air, false);
            fillWithBlocks(world, structureBoundingBox, 0, 0, 7, 2, 2, 8, Blocks.air, Blocks.air, false);

            for (int i = 0; i < 5; ++i)
            {
                fillWithBlocks(world, structureBoundingBox, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.air, Blocks.air, false);
            }

            return true;
        }
    }
}
