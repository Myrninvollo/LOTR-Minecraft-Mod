package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.item.ItemSpade;

public class LOTRItemShovel extends ItemSpade
{
	public LOTRItemShovel(ToolMaterial material)
	{
		super(material);
		setCreativeTab(LOTRCreativeTabs.tabTools);
	}
}
