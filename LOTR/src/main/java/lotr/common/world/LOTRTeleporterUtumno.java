package lotr.common.world;

import lotr.common.LOTRDimension;
import lotr.common.LOTRStructureLocations;
import lotr.common.world.LOTRChunkProviderUtumno.UtumnoLevel;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class LOTRTeleporterUtumno extends Teleporter
{
	private static int COORDINATE_RANGE = 1000000;
	
	private WorldServer worldObj;
	
    public LOTRTeleporterUtumno(WorldServer worldserver)
    {
		super(worldserver);
		worldObj = worldserver;
    }

	@Override
    public void placeInPortal(Entity entity, double d, double d1, double d2, float f)
    {
		int i;
		int j;
		int k;
		
		if (worldObj.provider.dimensionId == LOTRDimension.UTUMNO.dimensionID)
		{
			while (true)
			{
				int x = MathHelper.getRandomIntegerInRange(worldObj.rand, -COORDINATE_RANGE, COORDINATE_RANGE);
				int z = MathHelper.getRandomIntegerInRange(worldObj.rand, -COORDINATE_RANGE, COORDINATE_RANGE);
				int y = UtumnoLevel.ICE.corridorBaseLevels[UtumnoLevel.ICE.corridorBaseLevels.length - 1];
				
				int x1 = MathHelper.getRandomIntegerInRange(worldObj.rand, x - 128, x + 128);
				int z1 = MathHelper.getRandomIntegerInRange(worldObj.rand, z - 128, z + 128);
				
				if (worldObj.getBlock(x1, y - 1, z1).isOpaqueCube() && worldObj.isAirBlock(x1, y, z1) && worldObj.isAirBlock(x1, y, z1))
				{
					i = x1;
					j = y;
					k = z1;
					break;
				}
			}
		}
		else
		{
			double randomDistance = MathHelper.getRandomDoubleInRange(worldObj.rand, 40D, 80D);
			float angle = worldObj.rand.nextFloat() * (float)Math.PI * 2F;
			
			i = LOTRStructureLocations.UTUMNO_ENTRANCE.xCoord + (int)(randomDistance * MathHelper.sin(angle));
			k = LOTRStructureLocations.UTUMNO_ENTRANCE.zCoord + (int)(randomDistance * MathHelper.cos(angle));
			j = worldObj.getTopSolidOrLiquidBlock(i, k);
		}
		
		entity.setLocationAndAngles((double)i + 0.5D, (double)j + 1D, (double)k + 0.5D, entity.rotationYaw, 0F);
    }
}
