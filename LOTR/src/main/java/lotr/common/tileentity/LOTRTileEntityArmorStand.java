package lotr.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRTileEntityArmorStand extends TileEntity implements IInventory
{
    private ItemStack[] inventory = new ItemStack[4];
	public int ticksExisted;
	
	@Override
    public void setWorldObj(World world)
    {
		super.setWorldObj(world);
		ticksExisted = world.rand.nextInt(100);
    }
	
	@Override
	public void updateEntity()
	{
		ticksExisted++;
	}

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
				markDirty();
                return itemstack;
            }
            else
            {
                itemstack = inventory[i].splitStack(j);

                if (inventory[i].stackSize == 0)
                {
                    inventory[i] = null;
                }

				markDirty();
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
		
		markDirty();
    }
	
	@Override
	public void markDirty()
	{
		super.markDirty();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
    public String getInventoryName()
    {
        return StatCollector.translateToLocal("container.lotr.armorStand");
    }

	@Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
		readArmorStandFromNBT(nbt);
    }
	
	private void readArmorStandFromNBT(NBTTagCompound nbt)
	{
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
	}

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
		writeArmorStandToNBT(nbt);
    }
	
	private void writeArmorStandToNBT(NBTTagCompound nbt)
	{
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
	}

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
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
		return false;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 1, yCoord + 2, zCoord + 1);
    }
	
	@Override
    public Packet getDescriptionPacket()
    {
		NBTTagCompound data = new NBTTagCompound();
		writeArmorStandToNBT(data);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, data);
    }
	
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet)
	{
		readArmorStandFromNBT(packet.func_148857_g());
	}
}
