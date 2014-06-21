package lotr.common.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class LOTRTeleporterMorgulPortal extends Teleporter
{
    private final WorldServer theWorld;
    private final LongHashMap portalPositions = new LongHashMap();
    private final List portalTimes = new ArrayList();
	private Random rand = new Random();

    public LOTRTeleporterMorgulPortal(WorldServer world)
    {
        super(world);
		theWorld = world;
    }

	@Override
    public void placeInPortal(Entity entity, double d, double d1, double d2, float f)
    {
		if (!placeInExistingPortal(entity, d, d1, d2, f))
		{
			makePortal(entity);
			placeInExistingPortal(entity, d, d1, d2, f);
		}
    }

	@Override
    public boolean placeInExistingPortal(Entity entity, double d, double d1, double d2, float f)
    {
        short range = 128;
        double distanceToPortal = -1.0D;
        int i = 0;
        int j = 0;
        int k = 0;
        int posX = MathHelper.floor_double(entity.posX);
        int posZ = MathHelper.floor_double(entity.posZ);
        long chunkLocation = ChunkCoordIntPair.chunkXZ2Int(posX, posZ);
        boolean isChunkLocationInPortalPositions = true;

        if (portalPositions.containsItem(chunkLocation))
        {
            PortalPosition portalposition = (PortalPosition)portalPositions.getValueByKey(chunkLocation);
            distanceToPortal = 0.0D;
            i = portalposition.posX;
            j = portalposition.posY;
            k = portalposition.posZ;
            portalposition.lastUpdateTime = theWorld.getTotalWorldTime();
            isChunkLocationInPortalPositions = false;
        }
        else
        {
            for (int i1 = posX - range; i1 <= posX + range; i1++)
            {
                double xDistance = (double)i1 + 0.5D - entity.posX;

                for (int k1 = posZ - range; k1 <= posZ + range; k1++)
                {
                    double zDistance = (double)k1 + 0.5D - entity.posZ;

                    for (int j1 = theWorld.getActualHeight() - 1; j1 >= 0; j1--)
                    {
						boolean portalHere = true;
						for (int i2 = i1 - 1; i2 <= i1 + 1; i2++)
						{
							for (int k2 = k1 - 1; k2 <= k1 + 1; k2++)
							{
								if (theWorld.getBlock(i2, j1, k2) != LOTRMod.morgulPortal)
								{
									portalHere = false;
								}
							}
						}
						
                        if (portalHere)
                        {
                            double yDistance = (double)j1 + 0.5D - entity.posY;
                            double distanceSq = xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;

                            if (distanceToPortal < 0.0D || distanceSq < distanceToPortal)
                            {
                                distanceToPortal = distanceSq;
                                i = i1;
                                j = j1;
                                k = k1;
                            }
                        }
                    }
                }
            }
        }

        if (distanceToPortal >= 0D)
        {
            if (isChunkLocationInPortalPositions)
            {
                portalPositions.add(chunkLocation, new PortalPosition(i, j, k, theWorld.getTotalWorldTime()));
                portalTimes.add(Long.valueOf(chunkLocation));
            }

			int side = rand.nextInt(4);
			switch (side)
			{
				case 0:
					entity.setLocationAndAngles(i - 2 + 0.5D, j + 1, k - 1 + 0.25D + (double)(rand.nextFloat() * 2F), entity.rotationYaw, entity.rotationPitch);
					break;
				case 1:
					entity.setLocationAndAngles(i + 2 + 0.5D, j + 1, k - 1 + 0.25D + (double)(rand.nextFloat() * 2F), entity.rotationYaw, entity.rotationPitch);
					break;
				case 2:
					entity.setLocationAndAngles(i - 1 + 0.25D + (double)(rand.nextFloat() * 2F), j + 1, k - 2 + 0.25D, entity.rotationYaw, entity.rotationPitch);
					break;
				case 3:
					entity.setLocationAndAngles(i - 1 + 0.25D + (double)(rand.nextFloat() * 2F), j + 1, k + 2 + 0.25D, entity.rotationYaw, entity.rotationPitch);
					break;
			}
			
            return true;
        }
        else
        {
            return false;
        }
    }

	@Override
    public boolean makePortal(Entity entity)
    {
        byte range = 16;
        double distanceToPortal = -1.0D;
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY);
        int k = MathHelper.floor_double(entity.posZ);
        int posX = i;
        int posY = j;
        int posZ = k;

        for (int i1 = i - range; i1 <= i + range; i1++)
        {
            double xDistance = (double)i1 + 0.5D - entity.posX;

            for (int k1 = k - range; k1 <= k + range; k1++)
            {
                double zDistance = (double)k1 + 0.5D - entity.posZ;
				
                portalSearchingLoop:
                for (int j1 = theWorld.getActualHeight() - 1; j1 >= 0; j1--)
                {
                    if (theWorld.isAirBlock(i1, j1, k1))
                    {
                        while (j1 > 0 && theWorld.isAirBlock(i1, j1 - 1, k1))
                        {
                            j1--;
                        }

						for (int i2 = i1 - 2; i2 <= i1 + 2; i2++)
						{
							for (int k2 = k1 - 2; k2 <= k1 + 2; k2++)
							{
								for (int j2 = -1; j2 <= 3; j2++)
								{
									int j3 = j1 + j2;
									if (j2 < 0 && !LOTRMod.isOpaque(theWorld, i2, j3, k2) || j2 >= 0 && !theWorld.isAirBlock(i2, j3, k2))
									{
										continue portalSearchingLoop;
									}
                                }
                            }
						}

						double yDistance = (double)j1 + 0.5D - entity.posY;
						double distanceSq = xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;

						if (distanceToPortal < 0.0D || distanceSq < distanceToPortal)
						{
							distanceToPortal = distanceSq;
							posX = i1;
							posY = j1;
							posZ = k1;
						}
                    }
                }
            }
        }

        int actualPosX = posX;
        int actualPosY = posY;
        int actualPosZ = posZ;
		boolean generateRockBelow = false;

        if (distanceToPortal < 0.0D)
        {
            if (posY < 70)
            {
                posY = 70;
            }

            if (posY > theWorld.getActualHeight() - 10)
            {
                posY = theWorld.getActualHeight() - 10;
            }

            actualPosY = posY;
			generateRockBelow = true;
        }

		for (int i1 = -2; i1 <= 2; i1++)
		{
			for (int k1 = -2; k1 <= 2; k1++)
			{
				for (int j1 = (generateRockBelow ? -1 : 0); j1 <= 3; j1++)
				{
					int i2 = actualPosX + i1;
					int j2 = actualPosY + j1;
					int k2 = actualPosZ + k1;
					if (j1 > 0)
					{
						if (Math.abs(i1) == 2 && Math.abs(k1) == 2)
						{
							theWorld.setBlock(i2, j2, k2, LOTRMod.guldurilBrick, 0, 2);
						}
						else
						{
							theWorld.setBlock(i2, j2, k2, Blocks.air, 0, 2);
						}
					}
					else if (j1 < 0 && generateRockBelow)
					{
						theWorld.setBlock(i2, j2, k2, LOTRMod.rock, 0, 2);
					}
					else
					{
						boolean isFrame = i1 == -2 || i1 == 2 || k1 == -2 || k1 == 2;
						theWorld.setBlock(i2, j2, k2, isFrame ? LOTRMod.rock : LOTRMod.morgulPortal, 0, 2);
					}
				}
			}
		}
		
		for (int i1 = -3; i1 <= 3; i1++)
		{
			for (int k1 = -3; k1 <= 3; k1++)
			{
				for (int j1 = (generateRockBelow ? -2 : -1); j1 <= 4; j1++)
				{
					int i2 = actualPosX + i1;
					int j2 = actualPosY + j1;
					int k2 = actualPosZ + k1;
					theWorld.notifyBlocksOfNeighborChange(i2, j2, k2, theWorld.getBlock(i2, j2, k2));
				}
			}
		}

        return true;
    }

	@Override
    public void removeStalePortalLocations(long time)
    {
        if (time % 100L == 0L)
        {
            Iterator iterator = portalTimes.iterator();
            long j = time - 600L;

            while (iterator.hasNext())
            {
                Long olong = (Long)iterator.next();
                PortalPosition portalposition = (PortalPosition)portalPositions.getValueByKey(olong.longValue());

                if (portalposition == null || portalposition.lastUpdateTime < j)
                {
                    iterator.remove();
                    portalPositions.remove(olong.longValue());
                }
            }
        }
    }
}
