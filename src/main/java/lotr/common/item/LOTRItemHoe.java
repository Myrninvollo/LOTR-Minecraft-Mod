package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.item.ItemHoe;

public class LOTRItemHoe extends ItemHoe
{
	public LOTRItemHoe(ToolMaterial material)
	{
		super(material);
		setCreativeTab(LOTRCreativeTabs.tabTools);
	}
}
