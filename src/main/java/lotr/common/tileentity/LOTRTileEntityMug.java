package lotr.common.tileentity;

import lotr.common.LOTRMod;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LOTRTileEntityMug extends TileEntity
{
	public Item mugItem = LOTRMod.mug;
	public int itemDamage;
	
	public void setEmpty()
	{
		mugItem = LOTRMod.mug;
		itemDamage = 0;
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("ItemID", Item.getIdFromItem(mugItem));
		nbt.setInteger("ItemDamage", itemDamage);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		mugItem = Item.getItemById(nbt.getInteger("ItemID"));
		itemDamage = nbt.getInteger("ItemDamage");
		if (mugItem == null)
		{
			setEmpty();
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
