package lotr.common.item;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.entity.item.LOTREntityStoneTroll;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemTrollStatue extends Item
{
	public LOTRItemTrollStatue()
	{
		super();
        setHasSubtypes(true);
        setMaxDamage(0);
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
	{
        Block block = world.getBlock(i, j, k);
        if (block == Blocks.snow_layer)
        {
            l = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, i, j, k))
        {
            if (l == 0)
            {
                j--;
            }
            if (l == 1)
            {
                j++;
            }
            if (l == 2)
            {
                k--;
            }
            if (l == 3)
            {
                k++;
            }
            if (l == 4)
            {
                i--;
            }
            if (l == 5)
            {
                i++;
            }
        }
		if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack))
        {
            return false;
        }
		if (world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP))
		{
			if (!world.isRemote)
			{
				LOTREntityStoneTroll trollStatue = new LOTREntityStoneTroll(world);
				trollStatue.setLocationAndAngles((double)i + f, (double)j, (double)k + f2, 180F - (entityplayer.rotationYaw % 360F), 0F);
				if (world.checkNoEntityCollision(trollStatue.boundingBox) && world.getCollidingBoundingBoxes(trollStatue, trollStatue.boundingBox).size() == 0 && !world.isAnyLiquid(trollStatue.boundingBox))
				{
					trollStatue.setTrollOutfit(itemstack.getItemDamage());
					if (itemstack.hasTagCompound())
					{
						trollStatue.setHasTwoHeads(itemstack.getTagCompound().getBoolean("TwoHeads"));
					}
					trollStatue.placedByPlayer = true;
					world.spawnEntityInWorld(trollStatue);
					world.playSoundAtEntity(trollStatue, Blocks.stone.stepSound.func_150496_b(), (Blocks.stone.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.stone.stepSound.getPitch() * 0.8F);
					itemstack.stackSize--;
					return true;
				}
				trollStatue.setDead();
			}
		}
		return false;
	}
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
		if (itemstack.hasTagCompound())
		{
			boolean twoHeads = itemstack.getTagCompound().getBoolean("TwoHeads");
			if (twoHeads)
			{
				list.add(StatCollector.translateToLocal("item.lotr.trollStatue.twoHeads"));
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j <= 2; j++)
        {
        	ItemStack statue = new ItemStack(item, 1, j);
        	list.add(statue);
        	
        	statue = statue.copy();
        	statue.setTagCompound(new NBTTagCompound());
        	statue.getTagCompound().setBoolean("TwoHeads", true);
        	list.add(statue);
        }
    }
}
