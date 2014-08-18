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
    public boolean matches(InventoryCrafting inv, World world)
    {
        ItemStack hat = null;
        ItemStack feather = null;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == LOTRMod.leatherHat && !LOTRItemLeatherHat.hasFeather(itemstack))
                {
					if (hat != null)
					{
						return false;
					}
					else
					{
						hat = itemstack;
					}
                }
                else if (itemstack.getItem() == Items.feather || itemstack.getItem() == LOTRMod.featherDyed)
                {
                    if (feather != null)
                    {
                        return false;
                    }
                    else
                    {
                    	feather = itemstack;
                    }
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
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack hat = null;
		ItemStack feather = null;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == LOTRMod.leatherHat && !LOTRItemLeatherHat.hasFeather(itemstack))
                {
					if (hat != null)
					{
						return null;
					}
					else
					{
						hat = itemstack.copy();
					}
                }
                else if (itemstack.getItem() == Items.feather || itemstack.getItem() == LOTRMod.featherDyed)
                {
                    if (feather != null)
                    {
                        return null;
                    }
                    else
                    {
                    	feather = itemstack.copy();
                    }
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
