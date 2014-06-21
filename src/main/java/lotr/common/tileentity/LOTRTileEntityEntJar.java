package lotr.common.tileentity;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenFangorn;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LOTRTileEntityEntJar extends TileEntity
{
	public Item drinkItem;
	public int drinkAmount;
	public static int MAX_CAPACITY = 6;
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote && worldObj.canLightningStrikeAt(xCoord, yCoord, zCoord) && worldObj.getBiomeGenForCoords(xCoord, zCoord) instanceof LOTRBiomeGenFangorn)
		{
			if (worldObj.rand.nextInt(4000) == 0)
			{
				fillFromRain();
			}
		}
	}
	
	public boolean fillFrom(ItemStack itemstack)
	{
		if (drinkItem == null)
		{
			drinkItem = itemstack.getItem();
			drinkAmount++;
			return true;
		}
		else if (drinkItem == itemstack.getItem() && drinkAmount < MAX_CAPACITY)
		{
			drinkAmount++;
			return true;
		}
		return false;
	}
	
	public void fillFromRain()
	{
		if (drinkItem == null)
		{
			if (worldObj.rand.nextBoolean())
			{
				drinkItem = LOTRMod.entDraughtGreen;
			}
			else
			{
				drinkItem = LOTRMod.entDraughtBrown;
			}
			drinkAmount++;
		}
		else if (drinkAmount < MAX_CAPACITY)
		{
			drinkAmount++;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void consume()
	{
		drinkAmount--;
		if (drinkAmount <= 0)
		{
			drinkItem = null;
		}
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("DrinkID", Item.getIdFromItem(drinkItem));
		nbt.setInteger("DrinkAmount", drinkAmount);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		drinkItem = Item.getItemById(nbt.getInteger("DrinkID"));
		drinkAmount = nbt.getInteger("DrinkAmount");
		if (drinkItem == null)
		{
			drinkAmount = 0;
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
