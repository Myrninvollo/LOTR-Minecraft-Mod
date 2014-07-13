package lotr.common;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class LOTRFoods
{
	public static LOTRFoods HOBBIT = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(Items.porkchop),
		new ItemStack(Items.cooked_fished),
		new ItemStack(Items.chicken),
		new ItemStack(Items.cooked_beef),
		new ItemStack(LOTRMod.gammon),
		new ItemStack(Items.baked_potato),
		new ItemStack(Items.apple),
		new ItemStack(LOTRMod.appleGreen),
		new ItemStack(Items.bread),
		new ItemStack(Items.carrot),
		new ItemStack(LOTRMod.lettuce),
		new ItemStack(Items.pumpkin_pie),
		new ItemStack(LOTRMod.pear),
		new ItemStack(LOTRMod.cherry),
		new ItemStack(Items.cookie),
		new ItemStack(LOTRMod.hobbitPancake),
		new ItemStack(LOTRMod.rabbitCooked)
	});
	
	public static LOTRFoods HOBBIT_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugAle),
		new ItemStack(LOTRMod.mugAle),
		new ItemStack(LOTRMod.mugCider),
		new ItemStack(LOTRMod.mugPerry),
		new ItemStack(LOTRMod.mugCherryLiqueur)
	});
	
	public static LOTRFoods ROHAN = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(Items.porkchop),
		new ItemStack(Items.chicken),
		new ItemStack(Items.cooked_beef),
		new ItemStack(Items.baked_potato),
		new ItemStack(Items.apple),
		new ItemStack(LOTRMod.appleGreen),
		new ItemStack(LOTRMod.pear),
		new ItemStack(Items.bread),
		new ItemStack(LOTRMod.rabbitCooked)
	});
	
	public static LOTRFoods ROHAN_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugMead),
		new ItemStack(LOTRMod.mugMead),
		new ItemStack(LOTRMod.mugMead),
		new ItemStack(LOTRMod.mugAle),
		new ItemStack(LOTRMod.mugCider),
		new ItemStack(LOTRMod.mugPerry)
	});
	
	public static LOTRFoods GONDOR = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(Items.porkchop),
		new ItemStack(Items.chicken),
		new ItemStack(Items.cooked_beef),
		new ItemStack(Items.baked_potato),
		new ItemStack(LOTRMod.appleGreen),
		new ItemStack(LOTRMod.pear),
		new ItemStack(Items.bread),
		new ItemStack(LOTRMod.rabbitCooked)
	});
	
	public static LOTRFoods GONDOR_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugAle),
		new ItemStack(LOTRMod.mugAle),
		new ItemStack(LOTRMod.mugAle),
		new ItemStack(LOTRMod.mugMead),
		new ItemStack(LOTRMod.mugCider),
		new ItemStack(LOTRMod.mugPerry)
	});
	
	public static LOTRFoods DWARF = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(Items.porkchop),
		new ItemStack(Items.cooked_fished),
		new ItemStack(Items.chicken),
		new ItemStack(Items.cooked_beef),
		new ItemStack(LOTRMod.gammon),
		new ItemStack(Items.bread)
	});

	public static LOTRFoods DWARF_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugDwarvenAle),
		new ItemStack(LOTRMod.mugDwarvenAle),
		new ItemStack(LOTRMod.mugDwarvenAle),
		new ItemStack(LOTRMod.mugDwarvenTonic)
	});
	
	public static LOTRFoods DUNLENDING = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(Items.porkchop),
		new ItemStack(Items.cooked_fished),
		new ItemStack(Items.chicken),
		new ItemStack(Items.cooked_beef),
		new ItemStack(LOTRMod.gammon),
		new ItemStack(Items.baked_potato),
		new ItemStack(Items.apple),
		new ItemStack(Items.bread),
		new ItemStack(LOTRMod.rabbitCooked)
	});
	
	public static LOTRFoods DUNLENDING_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugAle),
		new ItemStack(LOTRMod.mugAle),
		new ItemStack(LOTRMod.mugMead),
		new ItemStack(LOTRMod.mugCider),
		new ItemStack(LOTRMod.mugRum)
	});
	
	public static LOTRFoods ELF = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(Items.bread),
		new ItemStack(Items.apple),
		new ItemStack(LOTRMod.appleGreen),
		new ItemStack(LOTRMod.pear),
		new ItemStack(LOTRMod.lettuce),
		new ItemStack(Items.carrot),
		new ItemStack(LOTRMod.lembas),
		new ItemStack(LOTRMod.lembas),
		new ItemStack(LOTRMod.lembas)
	});
	
	public static LOTRFoods ELF_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugMiruvor)
	});
	
	public static LOTRFoods WOOD_ELF_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugRedWine)
	});
	
	public static LOTRFoods NEAR_HARAD = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(Items.bread),
		new ItemStack(Items.apple),
		new ItemStack(LOTRMod.appleGreen),
		new ItemStack(LOTRMod.pear),
		new ItemStack(LOTRMod.date),
		new ItemStack(Items.carrot),
		new ItemStack(Items.baked_potato),
		new ItemStack(LOTRMod.lettuce),
		new ItemStack(Items.porkchop),
		new ItemStack(Items.cooked_fished),
		new ItemStack(Items.chicken),
		new ItemStack(Items.cooked_beef)
	});
	
	public static LOTRFoods NEAR_HARAD_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugAraq)
	});
	
	public static LOTRFoods ORC = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.maggotyBread)
	});
	
	public static LOTRFoods ORC_DRINK = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.mugOrcDraught)
	});
	
	public static LOTRFoods NURN_SLAVE = new LOTRFoods(new ItemStack[]
	{
		new ItemStack(LOTRMod.maggotyBread)
	});
				
	private ItemStack[] foodList;
	
	public LOTRFoods(ItemStack[] items)
	{
		foodList = items;
	}
	
	public ItemStack getRandomFood(Random random)
	{
		return foodList[random.nextInt(foodList.length)].copy();
	}
}
