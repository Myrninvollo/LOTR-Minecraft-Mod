package lotr.common.item;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class LOTRItemBone extends Item
{
	private static List<Item> boneItems = new ArrayList();
	
	public LOTRItemBone()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabMaterials);
		setFull3D();
		boneItems.add(this);
	}
	
	public static void registerAllBones(String name)
	{
		for (Item bone : boneItems)
		{
			OreDictionary.registerOre(name, bone);
		}
	}
}
