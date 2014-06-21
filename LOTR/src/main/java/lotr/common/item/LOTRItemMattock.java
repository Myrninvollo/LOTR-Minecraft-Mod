package lotr.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class LOTRItemMattock extends LOTRItemPickaxe
{
	private float efficiencyOnProperMaterial;
	
	public LOTRItemMattock(ToolMaterial material)
	{
		super(material);
		efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
		setHarvestLevel("axe", material.getHarvestLevel());
	}
	
	@Override
    public float func_150893_a(ItemStack itemstack, Block block)
	{
		float f = super.func_150893_a(itemstack, block);
		if (f == 1F && block != null && (block.getMaterial() == Material.wood || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine))
		{
			return efficiencyOnProperMaterial;
		}
		else
		{
			return f;
		}
    }
}
