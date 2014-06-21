package lotr.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class LOTRPouchRecipe extends ShapelessOreRecipe
{
	public LOTRPouchRecipe(ItemStack result, Object... recipe)
	{
		super(result, recipe);
	}

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack result = super.getCraftingResult(inv);
		
		NBTTagList tags = new NBTTagList();
		int slot = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack itemstack = inv.getStackInSlot(i);
			if (itemstack != null && itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("LOTRPouchData"))
			{
				NBTTagCompound nbt = itemstack.getTagCompound().getCompoundTag("LOTRPouchData");
				NBTTagList items = nbt.getTagList("Items", new NBTTagCompound().getId());
				for (int j = 0; j < items.tagCount(); j++)
				{
					NBTTagCompound itemData = (NBTTagCompound)items.getCompoundTagAt(j);
					itemData.setByte("Slot", (byte)slot);
					slot++;
					tags.appendTag(itemData);
				}
			}
		}

		if (!result.hasTagCompound())
		{
			result.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Items", tags);
		if (!result.getTagCompound().hasKey("LOTRPouchData"))
		{
			result.getTagCompound().setTag("LOTRPouchData", nbt);
		}
		return result;
	}
}
