package lotr.common.recipe;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemFeatherDyed;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class LOTRRecipeLeatherHatFeather implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inventory, World world)
    {
        ItemStack hat = null;
        ItemStack feather = null;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == LOTRMod.leatherHat && !LOTRItemLeatherHat.hasFeather(itemstack))
                {
					if (hat != null)
					{
						return false;
					}

                    hat = itemstack;
                }
                else if (itemstack.getItem() == Items.feather || itemstack.getItem() == LOTRMod.featherDyed)
                {
                    if (feather != null)
                    {
                        return false;
                    }

                    feather = itemstack;
                }
				else
				{
					return false;
				}
            }
        }

        return hat != null && feather != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory)
    {
        ItemStack hat = null;
		ItemStack feather = null;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == LOTRMod.leatherHat && !LOTRItemLeatherHat.hasFeather(itemstack))
                {
					if (hat != null)
					{
						return null;
					}

                    hat = itemstack.copy();
                }
                else if (itemstack.getItem() == Items.feather || itemstack.getItem() == LOTRMod.featherDyed)
                {
                    if (feather != null)
                    {
                        return null;
                    }

                    feather = itemstack.copy();
                }
				else
				{
					return null;
				}
            }
        }

        if (hat == null || feather == null)
        {
            return null;
        }
        else
        {
			int featherColor = LOTRItemLeatherHat.FEATHER_WHITE;
			if (feather.getItem() == LOTRMod.featherDyed)
			{
				featherColor = LOTRItemFeatherDyed.getFeatherColor(feather);
			}
            LOTRItemLeatherHat.setFeatherColor(hat, featherColor);
            return hat;
        }
    }

    @Override
    public int getRecipeSize()
    {
        return 2;
    }

	@Override
    public ItemStack getRecipeOutput()
    {
        return null;
    }
}
