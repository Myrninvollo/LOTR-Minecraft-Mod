package lotr.common.inventory;

import lotr.common.item.LOTRItemPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class LOTRInventoryPouch extends InventoryBasic
{
	private LOTRContainerPouch theContainer;
	private EntityPlayer thePlayer;
	private boolean isTemporary;
	private ItemStack tempPouchItem;
	
	public LOTRInventoryPouch(EntityPlayer entityplayer, LOTRContainerPouch container)
	{
		super(entityplayer.inventory.getCurrentItem().getDisplayName(), true, LOTRItemPouch.getCapacity(entityplayer.inventory.getCurrentItem()));
		isTemporary = false;
		thePlayer = entityplayer;
		theContainer = container;
		if (!thePlayer.worldObj.isRemote)
		{
			loadPouchContents();
		}
	}
	
	public LOTRInventoryPouch(ItemStack itemstack)
	{
		super("tempPouch", true, LOTRItemPouch.getCapacity(itemstack));
		isTemporary = true;
		tempPouchItem = itemstack;
		loadPouchContents();
	}
	
	public ItemStack getPouchItem()
	{
		if (isTemporary)
		{
			return tempPouchItem;
		}
		else
		{
			return thePlayer.inventory.getCurrentItem();
		}
	}
	
	@Override
	public void markDirty()
	{
		super.markDirty();
		if (isTemporary || !thePlayer.worldObj.isRemote)
		{
			savePouchContents();
		}
	}
	
	private void loadPouchContents()
	{
		if (getPouchItem().hasTagCompound() && getPouchItem().getTagCompound().hasKey("LOTRPouchData"))
		{
			NBTTagCompound nbt = getPouchItem().getTagCompound().getCompoundTag("LOTRPouchData");
			NBTTagList items = nbt.getTagList("Items", new NBTTagCompound().getId());
			for (int i = 0; i < items.tagCount(); i++)
			{
				NBTTagCompound itemData = (NBTTagCompound)items.getCompoundTagAt(i);
				byte slot = itemData.getByte("Slot");
				if (slot >= 0 && slot < getSizeInventory())
				{
					setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(itemData));
				}
			}
		}
		
		if (!isTemporary)
		{
			theContainer.syncPouchItem(getPouchItem());
		}
	}
	
	private void savePouchContents()
	{
		if (!getPouchItem().hasTagCompound())
		{
			getPouchItem().setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList items = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); i++)
        {
			ItemStack itemstack = getStackInSlot(i);
            if (itemstack != null)
            {
                NBTTagCompound itemData = new NBTTagCompound();
                itemData.setByte("Slot", (byte)i);
                itemstack.writeToNBT(itemData);
                items.appendTag(itemData);
            }
        }

        nbt.setTag("Items", items);
		getPouchItem().getTagCompound().setTag("LOTRPouchData", nbt);
		
		if (!isTemporary)
		{
			theContainer.syncPouchItem(getPouchItem());
		}
	}
}
