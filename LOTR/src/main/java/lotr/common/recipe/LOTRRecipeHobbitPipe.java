package lotr.common.recipe;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemDye;
import net.minecraft.block.BlockColored;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTRRecipeHobbitPipe implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        ItemStack hobbitPipe = null;
        ItemStack dye = null;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == LOTRMod.hobbitPipe)
                {
					if (hobbitPipe != null)
					{
						return false;
					}
					else
					{
						hobbitPipe = itemstack;
					}
                }
                else
                {
                	if (itemstack.getItem() == Items.diamond)
                	{
                		dye = itemstack;
                	}
                	else if (LOTRItemDye.isItemDye(itemstack) == -1)
                    {
                        return false;
                    }
                	else
                	{
                		dye = itemstack;
                	}
                }
            }
        }

        return hobbitPipe != null && dye != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
	{
        ItemStack hobbitPipe = null;
        ItemStack dye = null;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == LOTRMod.hobbitPipe)
                {
					if (hobbitPipe != null)
					{
						return null;
					}
					else
					{
						hobbitPipe = itemstack;
					}
                }
                else
                {
                	if (itemstack.getItem() == Items.diamond)
                	{
                		dye = itemstack;
                	}
                	else if (LOTRItemDye.isItemDye(itemstack) == -1)
                    {
                        return null;
                    }
                	else
                	{
                		dye = itemstack;
                	}
                }
            }
        }

        if (hobbitPipe != null && dye != null)
        {
        	int itemDamage = hobbitPipe.getItemDamage();
        	int smokeType = dye.getItem() == Items.diamond ? 16 : BlockColored.func_150031_c(LOTRItemDye.isItemDye(dye));
        	
			ItemStack result = new ItemStack(LOTRMod.hobbitPipe);
			result.setItemDamage(itemDamage);
			result.stackTagCompound = new NBTTagCompound();
			result.stackTagCompound.setInteger("SmokeColour", smokeType);
			return result;
        }
        else
        {
        	return null;
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
