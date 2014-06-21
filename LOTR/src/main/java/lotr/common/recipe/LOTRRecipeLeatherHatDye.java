package lotr.common.recipe;

import java.util.ArrayList;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemDye;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class LOTRRecipeLeatherHatDye implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inventory, World world)
    {
        ItemStack hat = null;
        ArrayList dyes = new ArrayList();

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == LOTRMod.leatherHat)
                {
					if (hat != null || LOTRItemLeatherHat.getFeatherColor(itemstack) > -1)
					{
						return false;
					}

                    hat = itemstack;
                }
                else
                {
                    if (LOTRItemDye.isItemDye(itemstack) == -1)
                    {
                        return false;
                    }

                    dyes.add(itemstack);
                }
            }
        }

        return hat != null && !dyes.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory)
    {
        ItemStack hat = null;
        int[] rgb = new int[3];
        int totalColor = 0;
        int coloredItems = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == LOTRMod.leatherHat)
                {
					if (hat != null || LOTRItemLeatherHat.getFeatherColor(itemstack) > -1)
					{
						return null;
					}

                    hat = itemstack.copy();
                    hat.stackSize = 1;

                    if (LOTRItemLeatherHat.isHatDyed(hat))
                    {
                        int hatColor = LOTRItemLeatherHat.getHatColor(hat);
                        float r = (float)(hatColor >> 16 & 255) / 255F;
                        float g = (float)(hatColor >> 8 & 255) / 255F;
                        float b = (float)(hatColor & 255) / 255F;
                        totalColor = (int)((float)totalColor + Math.max(r, Math.max(g, b)) * 255F);
                        rgb[0] = (int)((float)rgb[0] + r * 255F);
                        rgb[1] = (int)((float)rgb[1] + g * 255F);
                        rgb[2] = (int)((float)rgb[2] + b * 255F);
                        coloredItems++;
                    }
                }
                else
                {
					int dye = LOTRItemDye.isItemDye(itemstack);
                    if (dye == -1)
                    {
                        return null;
                    }

                    float[] dyeColors = EntitySheep.fleeceColorTable[BlockColored.func_150031_c(dye)];
                    int r = (int)(dyeColors[0] * 255F);
                    int g = (int)(dyeColors[1] * 255F);
                    int b = (int)(dyeColors[2] * 255F);
                    totalColor += Math.max(r, Math.max(g, b));
                    rgb[0] += r;
                    rgb[1] += g;
                    rgb[2] += b;
                    coloredItems++;
                }
            }
        }

        if (hat == null)
        {
            return null;
        }
        else
        {
            int r = rgb[0] / coloredItems;
            int g = rgb[1] / coloredItems;
            int b = rgb[2] / coloredItems;
            float averageColor = (float)totalColor / (float)coloredItems;
            float maxColor = (float)Math.max(r, Math.max(g, b));
            r = (int)((float)r * averageColor / maxColor);
            g = (int)((float)g * averageColor / maxColor);
            b = (int)((float)b * averageColor / maxColor);
            int color = (r << 16) + (g << 8) + b;
            LOTRItemLeatherHat.setHatColor(hat, color);
            return hat;
        }
    }

    @Override
    public int getRecipeSize()
    {
        return 10;
    }

	@Override
    public ItemStack getRecipeOutput()
    {
        return null;
    }
}
