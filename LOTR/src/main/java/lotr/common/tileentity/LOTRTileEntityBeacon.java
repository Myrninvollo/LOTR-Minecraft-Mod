package lotr.common.tileentity;

import lotr.common.LOTRLevelData;
import lotr.common.block.LOTRBlockBeacon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.EnumSkyBlock;

public class LOTRTileEntityBeacon extends TileEntity
{
	public boolean isLit;
	public int litCounter;
	public int unlitCounter;
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			if (isLit && litCounter < 100)
			{
				litCounter++;
				if (litCounter == 100)
				{
					worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
			
			if (!isLit && unlitCounter < 100)
			{
				unlitCounter++;
			}
			
			if (isLit && litCounter == 100 && LOTRLevelData.beaconState == 1)
			{
				ChunkCoordinates coords = new ChunkCoordinates(xCoord, yCoord, zCoord);
				for (int i = 0; i < worldObj.loadedTileEntityList.size(); i++)
				{
					TileEntity tileentity = (TileEntity)worldObj.loadedTileEntityList.get(i);
					if (tileentity != null && tileentity instanceof LOTRTileEntityBeacon)
					{
						LOTRTileEntityBeacon beacon = (LOTRTileEntityBeacon)tileentity;
						if (!beacon.isLit && coords.getDistanceSquared(beacon.xCoord, beacon.yCoord, beacon.zCoord) < 6400F)
						{
							LOTRBlockBeacon.setLit(worldObj, beacon.xCoord, beacon.yCoord, beacon.zCoord, true);
						}
					}
				}
			}
			
			if (!isLit && unlitCounter == 100 && LOTRLevelData.beaconState == 0)
			{
				ChunkCoordinates coords = new ChunkCoordinates(xCoord, yCoord, zCoord);
				for (int i = 0; i < worldObj.loadedTileEntityList.size(); i++)
				{
					TileEntity tileentity = (TileEntity)worldObj.loadedTileEntityList.get(i);
					if (tileentity != null && tileentity instanceof LOTRTileEntityBeacon)
					{
						LOTRTileEntityBeacon beacon = (LOTRTileEntityBeacon)tileentity;
						if (beacon.isLit && coords.getDistanceSquared(beacon.xCoord, beacon.yCoord, beacon.zCoord) < 6400F)
						{
							LOTRBlockBeacon.setLit(worldObj, beacon.xCoord, beacon.yCoord, beacon.zCoord, false);
						}
					}
				}
			}
		}
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("IsLit", isLit);
		nbt.setByte("LitCounter", (byte)litCounter);
		nbt.setByte("UnlitCounter", (byte)unlitCounter);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		isLit = nbt.getBoolean("IsLit");
		litCounter = nbt.getByte("LitCounter");
		unlitCounter = nbt.getByte("UnlitCounter");
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
		worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
	}
}
