package lotr.common.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRTileEntityDwarvenDoor extends TileEntity
{
	private static float[] lightValueSqrts = new float[16];
	static
	{
		for (int i = 0; i <= 15; i++)
		{
			lightValueSqrts[i] = MathHelper.sqrt_float((float)i / 15F);
		}
	}
	
	private boolean playersNearby;
	private int glow;

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			return;
		}
		
		double yOffset = isLowerDoorBlock() ? 1D : 0D;
		playersNearby = worldObj.getClosestPlayer((double)xCoord + 0.5D, (double)yCoord + yOffset + 0.5D, (double)zCoord + 0.5D, 8D) != null;
		
		if (playersNearby && glow < 80)
		{
			glow++;
		}
		else if (!playersNearby && glow > 0)
		{
			glow--;
		}
	}
	
	private boolean isLowerDoorBlock()
	{
		return (getBlockMetadata() & 8) == 0 && worldObj.getBlock(xCoord, yCoord + 1, zCoord) == getBlockType();
	}
	
	public float getGlowBrightness(float renderPartialTicks)
	{
		float f = (float)glow / 80F;
		float f1 = 1F - worldObj.getSunBrightness(renderPartialTicks);
		f1 -= 0.5F;
		if (f1 < 0F)
		{
			f1 = 0F;
		}
		f1 *= 2F;
		float f2 = lightValueSqrts[worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, xCoord, yCoord + (isLowerDoorBlock() ? 1 : 0), zCoord)];
		return f * f1 * f2;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 1024D;
    }
}
