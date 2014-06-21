package lotr.common.entity.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lotr.common.entity.animal.LOTREntityLion;
import lotr.common.entity.animal.LOTREntityLionBase;
import lotr.common.entity.animal.LOTREntityLioness;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.world.World;

public class LOTREntityAILionMate extends EntityAIBase
{
    private LOTREntityLionBase theLion;
    World theWorld;
    public LOTREntityLionBase targetMate;
    int breeding = 0;
    double moveSpeed;

    public LOTREntityAILionMate(LOTREntityLionBase lion, double d)
    {
        theLion = lion;
        theWorld = lion.worldObj;
        moveSpeed = d;
        setMutexBits(3);
    }

	@Override
    public boolean shouldExecute()
    {
        if (!theLion.isInLove())
        {
            return false;
        }
        if (theLion.isHostile())
        {
            return false;
        }
        else
        {
            targetMate = findMate();
            return targetMate != null;
        }
    }

	@Override
    public boolean continueExecuting()
    {
        return !theLion.isHostile() && !targetMate.isHostile() && targetMate.isEntityAlive() && targetMate.isInLove() && breeding < 60;
    }

	@Override
    public void resetTask()
    {
        targetMate = null;
        breeding = 0;
    }

	@Override
    public void updateTask()
    {
        theLion.getLookHelper().setLookPositionWithEntity(targetMate, 10F, (float)theLion.getVerticalFaceSpeed());
        theLion.getNavigator().tryMoveToEntityLiving(targetMate, moveSpeed);
        ++breeding;

        if (breeding == 60)
        {
            procreate();
        }
    }

    private LOTREntityLionBase findMate()
    {
        float f = 8F;
		Class mateClass = theLion.getClass();
		if (theLion instanceof LOTREntityLion)
		{
			mateClass = LOTREntityLioness.class;

		}
		else if (theLion instanceof LOTREntityLioness)
		{
			mateClass = LOTREntityLion.class;
		}
		
		List list = theWorld.getEntitiesWithinAABB(mateClass, theLion.boundingBox.expand((double)f, (double)f, (double)f));
		Iterator i = list.iterator();
		LOTREntityLionBase mate;
		do
		{
			if (!i.hasNext())
			{
				return null;
			}

			mate = (LOTREntityLionBase)i.next();
		}
		while (!theLion.canMateWith(mate));
		
		return mate;
    }

    private void procreate()
    {
        EntityAgeable babyAnimal = theLion.createChild(targetMate);
        if (babyAnimal != null)
        {
            theLion.setGrowingAge(6000);
            targetMate.setGrowingAge(6000);
            theLion.resetInLove();
            targetMate.resetInLove();
            babyAnimal.setGrowingAge(-24000);
            babyAnimal.setLocationAndAngles(theLion.posX, theLion.posY, theLion.posZ, 0.0F, 0.0F);
            theWorld.spawnEntityInWorld(babyAnimal);
            Random rand = theLion.getRNG();

            for (int i = 0; i < 7; ++i)
            {
                double var4 = rand.nextGaussian() * 0.02D;
                double var6 = rand.nextGaussian() * 0.02D;
                double var8 = rand.nextGaussian() * 0.02D;
                theWorld.spawnParticle("heart", theLion.posX + (double)(rand.nextFloat() * theLion.width * 2.0F) - (double)theLion.width, theLion.posY + 0.5D + (double)(rand.nextFloat() * theLion.height), theLion.posZ + (double)(rand.nextFloat() * theLion.width * 2.0F) - (double)theLion.width, var4, var6, var8);
            }
			
			theWorld.spawnEntityInWorld(new EntityXPOrb(theWorld, theLion.posX, theLion.posY, theLion.posZ, rand.nextInt(4) + 1));
        }
    }
}
