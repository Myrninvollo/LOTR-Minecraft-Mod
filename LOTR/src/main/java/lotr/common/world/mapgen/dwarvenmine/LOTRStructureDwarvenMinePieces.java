package lotr.common.world.mapgen.dwarvenmine;

import java.util.List;
import java.util.Random;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class LOTRStructureDwarvenMinePieces
{
	public static void register()
	{
		MapGenStructureIO.registerStructure(LOTRStructureDwarvenMineStart.class, "LOTR.DwarvenMine");
		
		MapGenStructureIO.func_143031_a(LOTRComponentDwarvenMineEntrance.class, "LOTR.DwarvenMine.Entrance");
		MapGenStructureIO.func_143031_a(LOTRComponentDwarvenMineCorridor.class, "LOTR.DwarvenMine.Corridor");
		MapGenStructureIO.func_143031_a(LOTRComponentDwarvenMineCrossing.class, "LOTR.DwarvenMine.Crossing");
		MapGenStructureIO.func_143031_a(LOTRComponentDwarvenMineStairs.class, "LOTR.DwarvenMine.Stairs");
	}
	
    private static StructureComponent getRandomComponent(List list, Random random, int i, int j, int k, int direction, int iteration)
    {
        int l = random.nextInt(100);
        StructureBoundingBox structureboundingbox;

        if (l >= 80)
        {
            structureboundingbox = LOTRComponentDwarvenMineCrossing.findValidPlacement(list, random, i, j, k, direction);
            if (structureboundingbox != null)
            {
                return new LOTRComponentDwarvenMineCrossing(iteration, random, structureboundingbox, direction);
            }
        }
        else if (l >= 70)
        {
            structureboundingbox = LOTRComponentDwarvenMineStairs.findValidPlacement(list, random, i, j, k, direction);
            if (structureboundingbox != null)
            {
                return new LOTRComponentDwarvenMineStairs(iteration, random, structureboundingbox, direction);
            }
        }
        else
        {
            structureboundingbox = LOTRComponentDwarvenMineCorridor.findValidPlacement(list, random, i, j, k, direction);
            if (structureboundingbox != null)
            {
                return new LOTRComponentDwarvenMineCorridor(iteration, random, structureboundingbox, direction);
            }
        }

        return null;
    }

    private static StructureComponent getNextMineComponent(StructureComponent component, List list, Random random, int i, int j, int k, int direction, int iteration)
    {
        if (iteration > 12)
        {
            return null;
        }
        else if (Math.abs(i - component.getBoundingBox().minX) <= 80 && Math.abs(k - component.getBoundingBox().minZ) <= 80)
        {
            StructureComponent structurecomponent1 = getRandomComponent(list, random, i, j, k, direction, iteration + 1);

            if (structurecomponent1 != null)
            {
                list.add(structurecomponent1);
                structurecomponent1.buildComponent(component, list, random);
            }

            return structurecomponent1;
        }
        else
        {
            return null;
        }
    }

    public static StructureComponent getNextComponent(StructureComponent component, List list, Random random, int i, int j, int k, int direction, int iteration)
    {
        return getNextMineComponent(component, list, random, i, j, k, direction, iteration);
    }
}
