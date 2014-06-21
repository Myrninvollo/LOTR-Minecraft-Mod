package lotr.common.world.mapgen.dwarvenmine;

import java.util.List;
import java.util.Random;

import lotr.common.world.structure.LOTRWorldGenDwarvenMineEntrance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class LOTRComponentDwarvenMineEntrance extends StructureComponent
{
	private int posX;
	private int posY;
	private int posZ;
	private static LOTRWorldGenDwarvenMineEntrance entranceGen = new LOTRWorldGenDwarvenMineEntrance(false);
	static
	{
		entranceGen.restrictions = false;
	}
	private int direction;
	
	public LOTRComponentDwarvenMineEntrance() {}

    public LOTRComponentDwarvenMineEntrance(World world, int l, Random random, int i, int k)
    {
        super(l);
		int j = world.getHeightValue(i, k) - 1;
        boundingBox = new StructureBoundingBox(i - 4, 40, k - 4, i + 4, j + 4, k + 4);
		posX = i;
		posY = j + 1;
		posZ = k;
    }
	
	@Override
    protected void func_143012_a(NBTTagCompound nbt)
	{
		nbt.setInteger("EntranceX", posX);
		nbt.setInteger("EntranceY", posY);
		nbt.setInteger("EntranceZ", posZ);
		nbt.setInteger("Direction", direction);
	}

	@Override
    protected void func_143011_b(NBTTagCompound nbt)
	{
		posX = nbt.getInteger("EntranceX");
		posY = nbt.getInteger("EntranceY");
		posZ = nbt.getInteger("EntranceZ");
		direction = nbt.getInteger("Direction");
	}

    @Override
    public void buildComponent(StructureComponent component, List list, Random random)
    {
		StructureBoundingBox structureBoundingBox = null;
		direction = random.nextInt(4);
		switch (direction)
		{
			case 0:
				structureBoundingBox = new StructureBoundingBox(posX - 1, boundingBox.minY + 1, posZ + 4, posX + 1, boundingBox.minY + 4, posZ + 15);
				break;
			case 1:
				structureBoundingBox = new StructureBoundingBox(posX - 15, boundingBox.minY + 1, posZ - 1, posX - 4, boundingBox.minY + 4, posZ + 1);
				break;
			case 2:
				structureBoundingBox = new StructureBoundingBox(posX - 1, boundingBox.minY + 1, posZ - 15, posX + 1, boundingBox.minY + 4, posZ - 4);
				break;
			case 3:
				structureBoundingBox = new StructureBoundingBox(posX + 4, boundingBox.minY + 1, posZ - 1, posX + 15, boundingBox.minY + 4, posZ + 1);
				break;
		}
		LOTRComponentDwarvenMineCorridor corridor = new LOTRComponentDwarvenMineCorridor(0, random, structureBoundingBox, direction);
		list.add(corridor);
		corridor.buildComponent(component, list, random);
    }

	@Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox)
    {
		entranceGen.generateWithDwarvenForgeDirection(world, random, posX, posY, posZ, direction);
		return true;
    }
}
