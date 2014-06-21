package lotr.common.item;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockColored;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemDye extends Item
{
	@SideOnly(Side.CLIENT)
	private IIcon[] dyeIcons;
	private String[] dyeNames = {"elanor", "niphredil", "bluebell"};
	
	public LOTRItemDye()
	{
		super();
        setHasSubtypes(true);
        setMaxDamage(0);
		setCreativeTab(LOTRCreativeTabs.tabMaterials);
	}
	
	@Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer entityplayer, EntityLivingBase entityliving)
    {
        if (entityliving instanceof EntitySheep)
        {
            EntitySheep sheep = (EntitySheep)entityliving;
			int dye = isItemDye(itemstack);
			if (dye == -1)
			{
				return false;
			}
			
            int blockDye = BlockColored.func_150031_c(dye);
            if (!sheep.getSheared() && sheep.getFleeceColor() != blockDye)
            {
                sheep.setFleeceColor(blockDye);
                itemstack.stackSize--;
            }

            return true;
        }
        return false;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        if (i >= dyeIcons.length)
		{
			i = 0;
		}
		return dyeIcons[i];
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        dyeIcons = new IIcon[dyeNames.length];
        for (int i = 0; i < dyeNames.length; i++)
        {
            dyeIcons[i] = iconregister.registerIcon(getIconString() + "_" + dyeNames[i]);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j <= 2; j++)
        {
            list.add(new ItemStack(item, 1, j));
        }
    }
	
	public static int isItemDye(ItemStack itemstack)
	{
		String oreName = OreDictionary.getOreName(OreDictionary.getOreID(itemstack));
		for (int j = 0; j < 16; j++)
		{
			ItemStack dye = new ItemStack(Items.dye, 1, j);
			if (LOTRMod.isOreNameEqual(dye, oreName))
			{
				return j;
			}
		}
		return -1;
	}
	
	/*public static int isItemDye(ItemStack itemstack)
	{
		int[] ids = OreDictionary.getOreIDs(itemstack);
		for (int i : ids)
		{
			String oreName = OreDictionary.getOreName(i);
			for (int j = 0; j < 16; j++)
			{
				ItemStack dye = new ItemStack(Items.dye, 1, j);
				if (LOTRMod.isOreNameEqual(dye, oreName))
				{
					return j;
				}
			}
		}
		return -1;
	}*/
}
