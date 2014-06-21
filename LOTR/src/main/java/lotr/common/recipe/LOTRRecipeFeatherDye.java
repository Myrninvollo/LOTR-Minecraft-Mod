package lotr.common.recipe;

import java.util.ArrayList;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemDye;
import lotr.common.item.LOTRItemFeatherDyed;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class LOTRRecipeFeatherDye implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inventory, World world)
    {
        ItemStack feather = null;
        ArrayList dyes = new ArrayList();

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == Items.feather || itemstack.getItem() == LOTRMod.featherDyed)
                {
					if (feather != null)
					{
						return false;
					}

                    feather = itemstack;
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

        return feather != null && !dyes.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory)
    {
        ItemStack feather = null;
        int[] rgb = new int[3];
        int totalColor = 0;
        int coloredItems = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == Items.feather || itemstack.getItem() == LOTRMod.featherDyed)
                {
					if (feather != null)
					{
						return null;
					}

                    feather = itemstack.copy();
                    feather.stackSize = 1;

                    if (feather.getItem() == LOTRMod.featherDyed)
                    {
                        int featherColor = LOTRItemFeatherDyed.getFeatherColor(feather);
                        float r = (float)(featherColor >> 16 & 255) / 255F;
                        float g = (float)(featherColor >> 8 & 255) / 255F;
                        float b = (float)(featherColor & 255) / 255F;
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

        if (feather == null)
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
			feather = new ItemStack(LOTRMod.featherDyed);
            LOTRItemFeatherDyed.setFeatherColor(feather, color);
            return feather;
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
