package lotr.common.block;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;

public class LOTRBlockPane extends BlockPane
{
	public LOTRBlockPane(String s, String s1, Material material, boolean flag)
	{
		super(s, s1, material, flag);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
}
