package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.item.ItemPickaxe;

public class LOTRItemPickaxe extends ItemPickaxe
{
	public LOTRItemPickaxe(ToolMaterial material)
	{
		super(material);
		setCreativeTab(LOTRCreativeTabs.tabTools);
	}
}
