package lotr.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class LOTRItemBerry extends LOTRItemFood
{
	private static List<Item> allBerries = new ArrayList();
	
	public LOTRItemBerry()
	{
		super(2, 0.2F, false);
		allBerries.add(this);
	}
	
	public static void registerAllBerries(String name)
	{
		for (Item berry : allBerries)
		{
			OreDictionary.registerOre(name, berry);
		}
	}
}
