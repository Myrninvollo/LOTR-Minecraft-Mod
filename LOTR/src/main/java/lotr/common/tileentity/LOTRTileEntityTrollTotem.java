package lotr.common.tileentity;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityMountainTrollChieftain;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LOTRTileEntityTrollTotem extends TileEntity
{
	private int prevJawRotation;
	private int jawRotation;
	private boolean prevCanSummon;
	private boolean clientCanSummon;
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			boolean flag = canSummon();
			if (flag != prevCanSummon)
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			prevCanSummon = flag;
		}
		else
		{
			prevJawRotation = jawRotation;
			if (jawRotation < 60 && canSummon())
			{
				jawRotation++;
			}
			else if (jawRotation > 0 && !canSummon())
			{
				jawRotation--;
			}
		}
	}
	
	public float getJawRotation(float f)
	{
		float rotation = (float)prevJawRotation + (float)(jawRotation - prevJawRotation) * f;
		return rotation / 60F * -35F;
	}
	
	public boolean canSummon()
	{
		if (worldObj.isRemote)
		{
			return clientCanSummon;
		}
		else
		{
			if (worldObj.isDaytime())
			{
				return false;
			}
			if (getBlockType() == LOTRMod.trollTotem && (getBlockMetadata() & 3) == 0)
			{
				int rotation = (getBlockMetadata() & 12) >> 2;
				if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) == LOTRMod.trollTotem && worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord) == (1 | (rotation << 2)))
				{
					if (worldObj.getBlock(xCoord, yCoord - 2, zCoord) == LOTRMod.trollTotem && worldObj.getBlockMetadata(xCoord, yCoord - 2, zCoord) == (2 | (rotation << 2)))
					{
						return true;
					}
				}
			}
			return false;
		}
	}
	
	public void summon()
	{
		if (!worldObj.isRemote)
		{
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			worldObj.setBlockToAir(xCoord, yCoord - 1, zCoord);
			worldObj.setBlockToAir(xCoord, yCoord - 2, zCoord);
			
			LOTREntityMountainTrollChieftain troll = new LOTREntityMountainTrollChieftain(worldObj);
			troll.setLocationAndAngles(xCoord + 0.5D, yCoord - 2, zCoord + 0.5D, worldObj.rand.nextFloat() * 360F, 0F);
			troll.onSpawnWithEgg(null);
			worldObj.spawnEntityInWorld(troll);
		}
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
		NBTTagCompound data = new NBTTagCompound();
		data.setBoolean("CanSummon", canSummon());
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, data);
    }
	
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet)
	{
		NBTTagCompound data = packet.func_148857_g();
		clientCanSummon = data.getBoolean("CanSummon");
	}
}
