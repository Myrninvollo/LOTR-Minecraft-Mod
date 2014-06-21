package lotr.common.recipe;

import lotr.common.LOTRMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class LOTRRecipeHobbitPipe extends ShapelessOreRecipe
{
	private int smokeColour;
	
	public LOTRRecipeHobbitPipe(int i, Object... recipe)
	{
		super(new ItemStack(LOTRMod.hobbitPipe), recipe);
		smokeColour = i;
	}

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		int damage = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack itemstack = inv.getStackInSlot(i);
			if (itemstack != null && itemstack.getItem() == LOTRMod.hobbitPipe)
			{
				damage = itemstack.getItemDamage();
				break;
			}
		}
		
		ItemStack result = super.getCraftingResult(inv);
		result.setItemDamage(damage);
		result.stackTagCompound = new NBTTagCompound();
		result.stackTagCompound.setInteger("SmokeColour", smokeColour);
		return result;
	}
}
