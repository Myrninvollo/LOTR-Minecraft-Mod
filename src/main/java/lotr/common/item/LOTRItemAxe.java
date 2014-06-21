package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.item.ItemAxe;

public class LOTRItemAxe extends ItemAxe
{
	public LOTRItemAxe(ToolMaterial material)
	{
		super(material);
		setCreativeTab(LOTRCreativeTabs.tabTools);
		setHarvestLevel("axe", material.getHarvestLevel());
	}
}
