package lotr.common.entity.ai;

import java.util.Random;

import lotr.common.entity.npc.LOTREntityTroll;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityAITrollFleeSun extends EntityAIBase
{
    private LOTREntityTroll theTroll;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double moveSpeed;
    private World theWorld;

    public LOTREntityAITrollFleeSun(LOTREntityTroll troll, double d)
    {
        theTroll = troll;
        moveSpeed = d;
        theWorld = troll.worldObj;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!theWorld.isDaytime())
        {
            return false;
        }
        else if (!theWorld.canBlockSeeTheSky(MathHelper.floor_double(theTroll.posX), (int)theTroll.boundingBox.minY, MathHelper.floor_double(theTroll.posZ)))
        {
            return false;
        }
		else if (theTroll.isImmuneToSun)
		{
			return false;
		}
        else
        {
			BiomeGenBase biome = theWorld.getBiomeGenForCoords(MathHelper.floor_double(theTroll.posX), MathHelper.floor_double(theTroll.posZ));
			if (biome instanceof LOTRBiome && ((LOTRBiome)biome).canSpawnHostilesInDay())
			{
				return false;
			}
			
			if (theTroll.getTrollBurnTime() == -1)
			{
				theTroll.setTrollBurnTime(300);
			}
			
            Vec3 vec3 = findPossibleShelter();
            if (vec3 == null)
            {
                vec3 = RandomPositionGenerator.findRandomTarget(theTroll, 12, 6);
				if (vec3 == null)
				{
					return false;
				}
            }

			xPosition = vec3.xCoord;
			yPosition = vec3.yCoord;
			zPosition = vec3.zCoord;
			return true;
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return !theTroll.getNavigator().noPath();
    }

    @Override
    public void startExecuting()
    {
        theTroll.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, moveSpeed);
    }

    private Vec3 findPossibleShelter()
    {
        Random random = theTroll.getRNG();
        for (int l = 0; l < 32; l++)
        {
            int i = MathHelper.floor_double(theTroll.posX) - 24 + random.nextInt(49);
            int j = MathHelper.floor_double(theTroll.boundingBox.minY) - 12 + random.nextInt(25);
            int k = MathHelper.floor_double(theTroll.posZ) - 24 + random.nextInt(49);

            if (!theWorld.canBlockSeeTheSky(i, j, k) && theTroll.getBlockPathWeight(i, j, k) < 0.0F)
            {
                return Vec3.createVectorHelper((double)i, (double)j, (double)k);
            }
        }

        return null;
    }
}
