package lotr.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LOTRTileEntityEntJar extends TileEntity
{
	public int drinkMeta = -1;
	public int drinkAmount;
	public static int MAX_CAPACITY = 6;
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote && worldObj.canLightningStrikeAt(xCoord, yCoord, zCoord))
		{
			if (worldObj.rand.nextInt(1000) == 0)
			{
				fillWithWater();
			}
		}
	}
	
	public boolean fillFromBowl(ItemStack itemstack)
	{
		if (drinkMeta == -1 && drinkAmount == 0)
		{
			drinkMeta = itemstack.getItemDamage();
			drinkAmount++;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return true;
		}
		else if (drinkMeta == itemstack.getItemDamage() && drinkAmount < MAX_CAPACITY)
		{
			drinkAmount++;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}
	
	public void fillWithWater()
	{
		if (drinkMeta == -1 && drinkAmount < MAX_CAPACITY)
		{
			drinkAmount++;
		}
		drinkAmount = Math.min(drinkAmount, MAX_CAPACITY);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void consume()
	{
		drinkAmount--;
		if (drinkAmount <= 0)
		{
			drinkMeta = -1;
		}
		drinkAmount = Math.max(drinkAmount, 0);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("DrinkMeta", drinkMeta);
		nbt.setInteger("DrinkAmount", drinkAmount);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		drinkMeta = nbt.getInteger("DrinkMeta");
		drinkAmount = nbt.getInteger("DrinkAmount");
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
