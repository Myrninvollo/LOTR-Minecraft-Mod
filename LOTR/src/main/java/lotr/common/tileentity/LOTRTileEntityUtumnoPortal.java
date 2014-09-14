package lotr.common.tileentity;

import java.util.List;

import lotr.common.*;
import lotr.common.world.LOTRTeleporterUtumno;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRTileEntityUtumnoPortal extends TileEntity
{
	public static int WIDTH = 3;
	public static int HEIGHT = 30;
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			List players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord - 8, yCoord, zCoord - 8, xCoord + 9, yCoord + 60, zCoord + 9));
			for (Object obj : players)
			{
				EntityPlayer entityplayer = (EntityPlayer)obj;
				LOTRGuiMessageTypes.UTUMNO_WARN.sendMessageIfNotReceived(entityplayer);
			}
			
			List nearbyEntities = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord - (WIDTH - 2), yCoord, zCoord - (WIDTH - 2), xCoord + (WIDTH - 1), yCoord + 3, zCoord + (WIDTH - 1)));
			for (Object obj : nearbyEntities)
			{
				Entity entity = (Entity)obj;
				
				entity.fallDistance = 0F;
				if (!worldObj.isRemote)
				{
					int dimension = LOTRDimension.UTUMNO.dimensionID;
					
					Teleporter teleporter = new LOTRTeleporterUtumno(DimensionManager.getWorld(dimension));
					if (entity instanceof EntityPlayerMP)
					{
						MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)entity, dimension, teleporter);
					}
					else
					{
						LOTRMod.transferEntityToDimension(entity, dimension, teleporter);
					}
				}
		    }
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(xCoord - (WIDTH - 1), yCoord, zCoord - (WIDTH - 1), xCoord + WIDTH, yCoord + HEIGHT, zCoord + WIDTH);
    }
}
