package lotr.common.entity.ai;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LOTREntityAIRabbitEatCrops extends EntityAIBase
{
    private LOTREntityRabbit theRabbit;
    private double xPos;
    private double yPos;
    private double zPos;
    private double moveSpeed;
    private World theWorld;
    private int pathingTick;
    private int eatingTick;
    private int rePathDelay;

    public LOTREntityAIRabbitEatCrops(LOTREntityRabbit rabbit, double d)
    {
        theRabbit = rabbit;
        moveSpeed = d;
        theWorld = rabbit.worldObj;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
		if (theRabbit.getRNG().nextInt(20) == 0)
		{
			Vec3 vec3 = findCropsLocation();
			if (vec3 != null)
			{
				xPos = vec3.xCoord;
				yPos = vec3.yCoord;
				zPos = vec3.zCoord;
				return true;
			}
		}
		return false;
    }

    @Override
    public boolean continueExecuting()
    {
		if (pathingTick < 200 && eatingTick < 60)
		{
			Block block = theWorld.getBlock(MathHelper.floor_double(xPos), MathHelper.floor_double(yPos), MathHelper.floor_double(zPos));
			return block instanceof BlockCrops;
		}
		return false;
    }
	
	@Override
    public void resetTask()
    {
		pathingTick = 0;
		eatingTick = 0;
		rePathDelay = 0;
		theRabbit.setRabbitEating(false);
    }

	@Override
    public void updateTask()
    {
		if (theRabbit.getDistanceSq(xPos, yPos, zPos) > 1D)
		{
			theRabbit.setRabbitEating(false);
			
			theRabbit.getLookHelper().setLookPosition(xPos, yPos - 0.5D, zPos, 10F, (float)theRabbit.getVerticalFaceSpeed());
			
			rePathDelay--;
			if (rePathDelay <= 0)
			{
				rePathDelay = 10;
				theRabbit.getNavigator().tryMoveToXYZ(xPos, yPos, zPos, moveSpeed);
			}
			
			pathingTick++;
		}
		else
		{
			theRabbit.setRabbitEating(true);
			
			eatingTick++;
			
			if (eatingTick % 6 == 0)
			{
				theRabbit.playSound("random.eat", 0.75F, (theWorld.rand.nextFloat() - theWorld.rand.nextFloat()) * 0.2F + 1F);
			}
			
			if (eatingTick >= 60)
			{
				theWorld.setBlockToAir(MathHelper.floor_double(xPos), MathHelper.floor_double(yPos), MathHelper.floor_double(zPos));
			}
		}
	}

    private Vec3 findCropsLocation()
    {
        Random random = theRabbit.getRNG();
        for (int l = 0; l < 32; l++)
        {
            int i = MathHelper.floor_double(theRabbit.posX) - 16 + random.nextInt(33);
            int j = MathHelper.floor_double(theRabbit.boundingBox.minY) - 8 + random.nextInt(17);
            int k = MathHelper.floor_double(theRabbit.posZ) - 16 + random.nextInt(33);

            Block block = theWorld.getBlock(i, j, k);
			if (block instanceof BlockCrops)
            {
                return Vec3.createVectorHelper((double)i + 0.5D, (double)j, (double)k + 0.5D);
            }
        }

        return null;
    }
}
