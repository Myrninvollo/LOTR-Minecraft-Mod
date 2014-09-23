package lotr.common.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockHobbitOven;
import lotr.common.inventory.LOTRSlotStackSize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRTileEntityHobbitOven extends TileEntity implements IInventory, ISidedInventory
{
    private ItemStack[] inventory = new ItemStack[19];
    public int ovenCookTime = 0;
    public int currentItemFuelValue = 0;
    public int currentCookTime = 0;
	private String specialOvenName;
	private int[] inputSlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
	private int[] outputSlots = new int[] {9, 10, 11, 12, 13, 14, 15, 16, 17};
	private int[] fuelSlots = new int[] {18};

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (inventory[i] != null)
        {
            ItemStack itemstack;

            if (inventory[i].stackSize <= j)
            {
                itemstack = inventory[i];
                inventory[i] = null;
                return itemstack;
            }
            else
            {
                itemstack = inventory[i].splitStack(j);

                if (inventory[i].stackSize == 0)
                {
                    inventory[i] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (inventory[i] != null)
        {
            ItemStack itemstack = inventory[i];
            inventory[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

	@Override
    public String getInventoryName()
    {
        return hasCustomInventoryName() ? specialOvenName : StatCollector.translateToLocal("container.lotr.hobbitOven");
    }

	@Override
    public boolean hasCustomInventoryName()
    {
        return specialOvenName != null && specialOvenName.length() > 0;
    }

    public void setOvenName(String s)
    {
        specialOvenName = s;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
		
        NBTTagList items = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < items.tagCount(); i++)
        {
            NBTTagCompound itemData = (NBTTagCompound)items.getCompoundTagAt(i);
            byte byte0 = itemData.getByte("Slot");

            if (byte0 >= 0 && byte0 < inventory.length)
            {
                inventory[byte0] = ItemStack.loadItemStackFromNBT(itemData);
            }
        }

        ovenCookTime = nbt.getShort("BurnTime");
        currentCookTime = nbt.getShort("CookTime");
        currentItemFuelValue = TileEntityFurnace.getItemBurnTime(inventory[18]);
		
        if (nbt.hasKey("CustomName"))
        {
            specialOvenName = nbt.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
		
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound itemData = new NBTTagCompound();
                itemData.setByte("Slot", (byte)i);
                inventory[i].writeToNBT(itemData);
                items.appendTag(itemData);
            }
        }
        nbt.setTag("Items", items);
		
        nbt.setShort("BurnTime", (short)ovenCookTime);
        nbt.setShort("CookTime", (short)currentCookTime);
		
        if (hasCustomInventoryName())
        {
            nbt.setString("CustomName", specialOvenName);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int i)
    {
        return currentCookTime * i / 400;
    }

    @SideOnly(Side.CLIENT)
    public int getCookTimeRemainingScaled(int i)
    {
        if (currentItemFuelValue == 0)
        {
            currentItemFuelValue = 400;
        }

        return ovenCookTime * i / currentItemFuelValue;
    }

    public boolean isCooking()
    {
        return ovenCookTime > 0;
    }

    @Override
    public void updateEntity()
    {
        boolean cooking = ovenCookTime > 0;
        boolean needUpdate = false;

        if (ovenCookTime > 0)
        {
            --ovenCookTime;
        }

        if (!worldObj.isRemote)
        {
            if (ovenCookTime == 0 && canCookAnyItem())
            {
                currentItemFuelValue = ovenCookTime = TileEntityFurnace.getItemBurnTime(inventory[18]);

                if (ovenCookTime > 0)
                {
                    needUpdate = true;

                    if (inventory[18] != null)
                    {
                        inventory[18].stackSize--;

                        if (inventory[18].stackSize == 0)
                        {
                            inventory[18] = inventory[18].getItem().getContainerItem(inventory[18]);
                        }
                    }
                }
            }

            if (isCooking() && canCookAnyItem())
            {
                ++currentCookTime;

                if (currentCookTime == 400)
                {
                    currentCookTime = 0;
					for (int i = 0; i < 9; i++)
					{
						cookItem(i);
					}
                    needUpdate = true;
                }
            }
            else
            {
                currentCookTime = 0;
            }

            if (cooking != ovenCookTime > 0)
            {
                needUpdate = true;
                LOTRBlockHobbitOven.setOvenActive(worldObj, xCoord, yCoord, zCoord);
            }
        }

        if (needUpdate)
        {
            markDirty();
        }
    }

    private boolean canCookAnyItem()
    {
		for (int i = 0; i < 9; i++)
		{
			if (canCook(i))
			{
				return true;
			}
		}
		return false;
    }
	
	private boolean canCook(int i)
	{
        if (inventory[i] == null)
        {
            return false;
        }
        else
        {
            ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(inventory[i]);
            if (!isCookResultAcceptable(result))
			{
				return false;
			}
			
            if (inventory[i + 9] == null)
			{
				return true;
			}
            if (!inventory[i + 9].isItemEqual(result))
			{
				return false;
			}
            int resultSize = inventory[i + 9].stackSize + result.stackSize;
            return (resultSize <= getInventoryStackLimit() && resultSize <= result.getMaxStackSize());
        }
	}

    public void cookItem(int i)
    {
        if (canCook(i))
        {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(inventory[i]);

			if (inventory[i + 9] == null)
			{
				inventory[i + 9] = itemstack.copy();
			}
			else if (inventory[i + 9].isItemEqual(itemstack))
			{
				inventory[i + 9].stackSize += itemstack.stackSize;
			}

			inventory[i].stackSize--;

			if (inventory[i].stackSize <= 0)
			{
				inventory[i] = null;
			}
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
    }

	@Override
    public void openInventory() {}

	@Override
    public void closeInventory() {}
	
	public static boolean isCookResultAcceptable(ItemStack result)
	{
		if (result == null)
		{
			return false;
		}
		Item item = result.getItem();
		return item instanceof ItemFood || item == LOTRMod.pipeweed;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
        if (slot < 9)
		{
			return itemstack == null ? false : isCookResultAcceptable(FurnaceRecipes.smelting().getSmeltingResult(itemstack));
		}
		else if (slot < 18)
		{
			return false;
		}
		return TileEntityFurnace.isItemFuel(itemstack);
	}
	
	@Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
		if (side == 0)
		{
			return outputSlots;
		}
		if (side == 1)
		{
			List list = new ArrayList();
			for (int i = 0; i < inputSlots.length; i++)
			{
				int slot = inputSlots[i];
				int size = getStackInSlot(slot) == null ? 0 : getStackInSlot(slot).stackSize;
				list.add(new LOTRSlotStackSize(slot, size));
			}
			Collections.sort(list);
			int[] temp = new int[inputSlots.length];
			for (int i = 0; i < temp.length; i++)
			{
				LOTRSlotStackSize obj = (LOTRSlotStackSize)list.get(i);
				temp[i] = obj.slot;
			}
			return temp;
		}
		return fuelSlots;
    }
	
	@Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int j)
    {
		return isItemValidForSlot(slot, itemstack);
    }
	
	@Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int i)
    {
        return i != 0 || slot != 18 || itemstack.getItem() == Items.bucket;
    }
	
	@Override
    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet)
    {
		if (packet.func_148857_g() != null && packet.func_148857_g().hasKey("CustomName"))
		{
			specialOvenName = packet.func_148857_g().getString("CustomName");
		}
    }
}
