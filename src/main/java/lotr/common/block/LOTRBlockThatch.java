
package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class LOTRBlockThatch extends Block
{
	public LOTRBlockThatch()
	{
		super(Material.grass);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
}
