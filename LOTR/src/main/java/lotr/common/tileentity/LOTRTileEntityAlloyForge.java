package lotr.common.tileentity;

import java.util.*;

import lotr.common.LOTRMod;
import lotr.common.inventory.LOTRSlotStackSize;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTRTileEntityAlloyForge extends TileEntity implements IInventory, ISidedInventory
{
    private ItemStack[] inventory = new ItemStack[13];
    public int forgeSmeltTime = 0;
    public int currentItemFuelValue = 0;
    public int currentSmeltTime = 0;
	private String specialForgeName;
	private int[] inputSlots = new int[] {4, 5, 6, 7};
	private int[] outputSlots = new int[] {8, 9, 10, 11};
	private int[] fuelSlots = new int[] {12};

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
        return hasCustomInventoryName() ? specialForgeName : getForgeName();
    }
	
	public abstract String getForgeName();

	@Override
    public boolean hasCustomInventoryName()
    {
        return specialForgeName != null && specialForgeName.length() > 0;
    }

    public void setSpecialForgeName(String s)
    {
        specialForgeName = s;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
		
        NBTTagList items = nbt.getTagList("Items", new NBTTagCompound().getId());
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

        forgeSmeltTime = nbt.getShort("BurnTime");
        currentSmeltTime = nbt.getShort("SmeltTime");
        currentItemFuelValue = TileEntityFurnace.getItemBurnTime(inventory[12]);
		
        if (nbt.hasKey("CustomName"))
        {
            specialForgeName = nbt.getString("CustomName");
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
		
        nbt.setShort("BurnTime", (short)forgeSmeltTime);
        nbt.setShort("SmeltTime", (short)currentSmeltTime);
		
        if (hasCustomInventoryName())
        {
            nbt.setString("CustomName", specialForgeName);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @SideOnly(Side.CLIENT)
    public int getSmeltProgressScaled(int i)
    {
        return currentSmeltTime * i / 200;
    }

    @SideOnly(Side.CLIENT)
    public int getSmeltTimeRemainingScaled(int i)
    {
        if (currentItemFuelValue == 0)
        {
            currentItemFuelValue = 200;
        }

        return forgeSmeltTime * i / currentItemFuelValue;
    }

    public boolean isSmelting()
    {
        return forgeSmeltTime > 0;
    }

    @Override
    public void updateEntity()
    {
        boolean smelting = forgeSmeltTime > 0;
        boolean needUpdate = false;

        if (forgeSmeltTime > 0)
        {
            --forgeSmeltTime;
        }

        if (!worldObj.isRemote)
        {
            if (forgeSmeltTime == 0 && canSmeltAnyItem())
            {
                currentItemFuelValue = forgeSmeltTime = TileEntityFurnace.getItemBurnTime(inventory[12]);

                if (forgeSmeltTime > 0)
                {
                    needUpdate = true;

                    if (inventory[12] != null)
                    {
                        inventory[12].stackSize--;

                        if (inventory[12].stackSize == 0)
                        {
                            inventory[12] = inventory[12].getItem().getContainerItem(inventory[12]);
                        }
                    }
                }
            }

            if (isSmelting() && canSmeltAnyItem())
            {
                ++currentSmeltTime;

                if (currentSmeltTime == 200)
                {
                    currentSmeltTime = 0;
					for (int i = 4; i < 8; i++)
					{
						smeltItem(i);
					}
                    needUpdate = true;
                }
            }
            else
            {
                currentSmeltTime = 0;
            }

            if (smelting != forgeSmeltTime > 0)
            {
                needUpdate = true;
                setForgeActive();
            }
        }

        if (needUpdate)
        {
            markDirty();
        }
    }
	
	public abstract void setForgeActive();

    private boolean canSmeltAnyItem()
    {
		for (int i = 4; i < 8; i++)
		{
			if (canSmelt(i))
			{
				return true;
			}
		}
		return false;
    }
	
	private boolean canSmelt(int i)
	{
        if (inventory[i] == null)
        {
            return false;
        }
        else
        {
			if (inventory[i - 4] != null)
			{
				ItemStack alloyResult = getAlloySmeltingResult(inventory[i], inventory[i - 4]);
				if (alloyResult != null)
				{
					if (inventory[i + 4] == null)
					{
						return true;
					}
					else
					{
						int resultSize = inventory[i + 4].stackSize + alloyResult.stackSize;
						if (inventory[i + 4].isItemEqual(alloyResult) && resultSize <= getInventoryStackLimit() && resultSize <= alloyResult.getMaxStackSize())
						{
							return true;
						}
					}
				}
			}

			ItemStack result = getSmeltingResult(inventory[i]);
			if (result == null)
			{
				return false;
			}
			
			if (inventory[i + 4] == null)
			{
				return true;
			}
			if (!inventory[i + 4].isItemEqual(result))
			{
				return false;
			}
			
			int resultSize = inventory[i + 4].stackSize + result.stackSize;
			return (resultSize <= getInventoryStackLimit() && resultSize <= result.getMaxStackSize());
        }
	}

    public void smeltItem(int i)
    {
        if (canSmelt(i))
        {
        	boolean smeltedAlloyItem = false;
        	
			if (inventory[i - 4] != null)
			{
				ItemStack alloyResult = getAlloySmeltingResult(inventory[i], inventory[i - 4]);
				if (alloyResult != null && (inventory[i + 4] == null || inventory[i + 4].isItemEqual(alloyResult)))
				{
					if (inventory[i + 4] == null)
					{
						inventory[i + 4] = alloyResult.copy();
					}
					else if (inventory[i + 4].isItemEqual(alloyResult))
					{
						inventory[i + 4].stackSize += alloyResult.stackSize;
					}
	
					inventory[i].stackSize--;
	
					if (inventory[i].stackSize <= 0)
					{
						inventory[i] = null;
					}
					
					inventory[i - 4].stackSize--;
	
					if (inventory[i - 4].stackSize <= 0)
					{
						inventory[i - 4] = null;
					}
					
					smeltedAlloyItem = true;
				}
			}
			
			if (!smeltedAlloyItem)
			{
				ItemStack result = getSmeltingResult(inventory[i]);

				if (inventory[i + 4] == null)
				{
					inventory[i + 4] = result.copy();
				}
				else if (inventory[i + 4].isItemEqual(result))
				{
					inventory[i + 4].stackSize += result.stackSize;
				}

				inventory[i].stackSize--;

				if (inventory[i].stackSize <= 0)
				{
					inventory[i] = null;
				}
			}
        }
    }

	public ItemStack getSmeltingResult(ItemStack itemstack)
	{
		boolean isStoneMaterial = false;
		if (itemstack.getItem() == Item.getItemFromBlock(Blocks.sand) || itemstack.getItem() == Items.clay_ball)
		{
			isStoneMaterial = true;
		}
		else
		{
			Block block = Block.getBlockFromItem(itemstack.getItem());
			if (block != null && block.getMaterial() == Material.rock)
			{
				isStoneMaterial = true;
			}
		}
		
		if (isStoneMaterial)
		{
			return FurnaceRecipes.smelting().getSmeltingResult(itemstack);
		}
		else
		{
			return null;
		}
	}
	
	protected ItemStack getAlloySmeltingResult(ItemStack itemstack, ItemStack alloyItem)
	{
		if ((isCopper(itemstack) && isTin(alloyItem)) || isTin(itemstack) && isCopper(alloyItem))
		{
			return new ItemStack(LOTRMod.bronze, 2);
		}
		
		return null;
	}
	
	protected boolean isCopper(ItemStack itemstack)
	{	
		return LOTRMod.isOreNameEqual(itemstack, "oreCopper") || LOTRMod.isOreNameEqual(itemstack, "ingotCopper");
	}
	
	protected boolean isTin(ItemStack itemstack)
	{
		return LOTRMod.isOreNameEqual(itemstack, "oreTin") || LOTRMod.isOreNameEqual(itemstack, "ingotTin");
	}
	
	protected boolean isIron(ItemStack itemstack)
	{	
		return itemstack.getItem() == Item.getItemFromBlock(Blocks.iron_ore) || itemstack.getItem() == Items.iron_ingot;
	}
	
	protected boolean isOrcSteel(ItemStack itemstack)
	{	
		return itemstack.getItem() == Item.getItemFromBlock(LOTRMod.oreMorgulIron) || itemstack.getItem() == LOTRMod.orcSteel;
	}
	
	protected boolean isWood(ItemStack itemstack)
	{	
		return LOTRMod.isOreNameEqual(itemstack, "logWood");
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
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
        if (slot > 3 && slot < 8)
		{
			return itemstack == null ? false : getSmeltingResult(itemstack) != null;
		}
		else if (slot < 12)
		{
			return false;
		}
		else if (slot == 12)
		{
			return TileEntityFurnace.isItemFuel(itemstack);
		}
		return false;
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
        return i != 0 || slot != 12 || itemstack.getItem() == Items.bucket;
    }
	
	@Override
    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet)
    {
		if (packet.func_148857_g() != null && packet.func_148857_g().hasKey("CustomName"))
		{
			specialForgeName = packet.func_148857_g().getString("CustomName");
		}
    }
}
