package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;

public class LOTRBlockFlower extends BlockBush
{
	public LOTRBlockFlower()
	{
		this(Material.plants);
	}
	
	public LOTRBlockFlower(Material material)
	{
		super(material);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setFlowerBounds(0.3F, 0F, 0.3F, 0.7F, 0.8F, 0.7F);
		setHardness(0F);
		setStepSound(Block.soundTypeGrass);
	}
	
	public Block setFlowerBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		return this;
	}
}
