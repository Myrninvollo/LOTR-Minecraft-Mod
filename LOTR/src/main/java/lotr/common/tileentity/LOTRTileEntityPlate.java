package lotr.common.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LOTRTileEntityPlate extends TileEntity
{
	private Item foodItem;
	private int foodDamage;
	
	public ItemStack getFoodItem()
	{
		if (foodItem == null)
		{
			return null;
		}
		else
		{
			return new ItemStack(foodItem, 1, foodDamage);
		}
	}
	
	public void setFoodItem(ItemStack item)
	{
		if (item == null)
		{
			foodItem = null;
			foodDamage = 0;
		}
		else
		{
			foodItem = item.getItem();
			foodDamage = item.getItemDamage();
		}
		
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("FoodID", Item.getIdFromItem(foodItem));
		nbt.setInteger("FoodDamage", foodDamage);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		foodItem = Item.getItemById(nbt.getInteger("FoodID"));
		foodDamage = nbt.getInteger("FoodDamage");
		if (foodItem == null)
		{
			foodDamage = 0;
		}
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
		NBTTagCompound data = new NBTTagCompound();
		writeToNBT(data);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, data);
    }
	
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet)
	{
		NBTTagCompound data = packet.func_148857_g();
		readFromNBT(data);
	}
}
