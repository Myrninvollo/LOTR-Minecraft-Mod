package lotr.common.item;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.inventory.LOTRInventoryPouch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemPouch extends Item
{
	@SideOnly(Side.CLIENT)
	private IIcon[] pouchIcons;
	private String[] pouchTypes = {"small", "medium", "large"};
	
	public LOTRItemPouch()
	{
		super();
        setHasSubtypes(true);
        setMaxDamage(0);
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}
	
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_POUCH, world, 0, 0, 0);
        return itemstack;
    }
	
	public static int getCapacity(ItemStack itemstack)
	{
		return (itemstack.getItemDamage() + 1) * 9;
	}
	
	public static int getRandomPouchSize(Random random)
	{
		float f = random.nextFloat();
		if (f < 0.6F)
		{
			return 0;
		}
		else if (f < 0.9F)
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        if (i >= pouchIcons.length)
		{
			i = 0;
		}
		return pouchIcons[i];
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
        pouchIcons = new IIcon[pouchTypes.length];
        for (int i = 0; i < pouchTypes.length; i++)
        {
            pouchIcons[i] = iconregister.registerIcon(getIconString() + "_" + pouchTypes[i]);
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
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
		int slotsFull = 0;
		if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("LOTRPouchData"))
		{
			NBTTagCompound nbt = itemstack.getTagCompound().getCompoundTag("LOTRPouchData");
			NBTTagList items = nbt.getTagList("Items", new NBTTagCompound().getId());
			for (int i = 0; i < items.tagCount(); i++)
			{
				slotsFull++;
			}
		}
		int slots = getCapacity(itemstack);
		list.add(StatCollector.translateToLocalFormatted("item.lotr.pouch.slots", new Object[] {slotsFull, slots}));
	}
	
	public static boolean tryAddItemToPouch(ItemStack pouch, ItemStack itemstack, boolean requireMatchInPouch)
	{
		if (itemstack != null && itemstack.stackSize > 0)
		{
			LOTRInventoryPouch tempInventory = new LOTRInventoryPouch(pouch);
			for (int i = 0; i < tempInventory.getSizeInventory() && itemstack.stackSize > 0; i++)
			{
				ItemStack itemInSlot = tempInventory.getStackInSlot(i);
				if (itemInSlot == null)
				{
					if (requireMatchInPouch)
					{
						continue;
					}
				}
				else if (itemInSlot.stackSize >= itemInSlot.getMaxStackSize())
				{
					continue;
				}
				else
				{
					if (itemInSlot.getItem() != itemstack.getItem())
					{
						continue;
					}
					if (!itemInSlot.isStackable())
					{
						continue;
					}
					if (itemInSlot.getHasSubtypes() && itemInSlot.getItemDamage() != itemstack.getItemDamage())
					{
						continue;
					}
					if (!ItemStack.areItemStackTagsEqual(itemInSlot, itemstack))
					{
						continue;
					}
				}
				
				if (itemInSlot == null)
				{
					tempInventory.setInventorySlotContents(i, itemstack);
				}
				else
				{
					int maxStackSize = itemInSlot.getMaxStackSize();
					if (tempInventory.getInventoryStackLimit() < maxStackSize)
					{
						maxStackSize = tempInventory.getInventoryStackLimit();
					}
					int difference = maxStackSize - itemInSlot.stackSize;
					if (difference > itemstack.stackSize)
					{
						difference = itemstack.stackSize;
					}
					itemstack.stackSize -= difference;
					itemInSlot.stackSize += difference;
					tempInventory.setInventorySlotContents(i, itemInSlot);
				}
				
				return true;
			}
		}
		
		return false;
	}
}
