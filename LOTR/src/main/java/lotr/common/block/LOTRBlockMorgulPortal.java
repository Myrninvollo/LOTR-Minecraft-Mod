package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityMorgulPortal;
import lotr.common.world.LOTRTeleporterMorgulPortal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockMorgulPortal extends LOTRBlockPortal
{
    public LOTRBlockMorgulPortal()
    {
        super(new LOTRFaction[] {LOTRFaction.MORDOR, LOTRFaction.ANGMAR}, LOTRTeleporterMorgulPortal.class);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new LOTRTileEntityMorgulPortal();
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		double d = (double)((float)i + random.nextFloat());
		double d1 = (double)((float)j + 0.8F);
		double d2 = (double)((float)k + random.nextFloat());
		LOTRMod.proxy.spawnParticle("morgulPortal", d, d1, d2, 0D, 0D, 0D);
    }
	
	@Override
	public void setPlayerInPortal(EntityPlayer entityplayer)
	{
		LOTRMod.proxy.setInMorgulPortal(entityplayer);
		if (!entityplayer.worldObj.isRemote)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useMorgulPortal);
		}
	}

	@Override
	public boolean isValidPortalLocation(World world, int i, int j, int k, boolean portalAlreadyMade)
	{
		for (int i1 = i - 2; i1 <= i + 2; i1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				if (Math.abs(i1 - i) == 2 && Math.abs(k1 - k) == 2)
				{
					for (int j1 = j + 1; j1 <= j + 3; j1++)
					{
						if (world.getBlock(i1, j1, k1) != LOTRMod.guldurilBrick)
						{
							return false;
						}
					}
				}
				else if (Math.abs(i1 - i) == 2 || Math.abs(k1 - k) == 2)
				{
					if (!LOTRMod.isOpaque(world, i1, j, k1))
					{
						return false;
					}
				}
				else
				{
					if (world.getBlock(i1, j, k1) != (portalAlreadyMade ? LOTRMod.morgulPortal : Blocks.lava) || !LOTRMod.isOpaque(world, i1, j - 1, k1))
					{
						return false;
					}
				}
			}
		}
		return true;
	}
}
