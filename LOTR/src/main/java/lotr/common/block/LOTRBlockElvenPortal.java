package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityElvenPortal;
import lotr.common.world.LOTRTeleporterElvenPortal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockElvenPortal extends LOTRBlockPortal
{
    public LOTRBlockElvenPortal()
    {
        super(new LOTRFaction[] {LOTRFaction.GALADHRIM}, LOTRTeleporterElvenPortal.class);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new LOTRTileEntityElvenPortal();
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		if (random.nextInt(3) == 0)
		{
			double d = (double)((float)i + random.nextFloat());
			double d1 = (double)((float)j + 0.8F);
			double d2 = (double)((float)k + random.nextFloat());
			LOTRMod.proxy.spawnParticle("elvenGlow", d, d1, d2, 0D, 0.3D, 0D);
		}
		
        super.randomDisplayTick(world, i, j, k, random);
    }
	
	@Override
	public void setPlayerInPortal(EntityPlayer entityplayer)
	{
		LOTRMod.proxy.setInElvenPortal(entityplayer);
		if (!entityplayer.worldObj.isRemote)
		{
			LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.useElvenPortal);
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
					continue;
				}
				else if (Math.abs(i1 - i) == 2 || Math.abs(k1 - k) == 2)
				{
					if (world.getBlock(i1, j, k1) != LOTRMod.quenditeGrass)
					{
						return false;
					}
				}
				else
				{
					if (world.getBlock(i1, j, k1) != (portalAlreadyMade ? LOTRMod.elvenPortal : Blocks.water) || !LOTRMod.isOpaque(world, i1, j - 1, k1))
					{
						return false;
					}
				}
			}
		}
		return true;
	}
}
