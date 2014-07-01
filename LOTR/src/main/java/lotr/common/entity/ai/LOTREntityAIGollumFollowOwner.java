package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityGollum;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTREntityAIGollumFollowOwner extends EntityAIBase
{
    private LOTREntityGollum theGollum;
    private EntityPlayer theOwner;
    private double moveSpeed;
    private PathNavigate theGollumPathfinder;
    private int followTick;
    private float maxDist;
    private float minDist;
    private boolean avoidsWater;
	private World theWorld;
	
    public LOTREntityAIGollumFollowOwner(LOTREntityGollum entity, double d, float f, float f1)
    {
        theGollum = entity;
        moveSpeed = d;
        theGollumPathfinder = entity.getNavigator();
        minDist = f;
        maxDist = f1;
		theWorld = entity.worldObj;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
		EntityPlayer entityplayer = theGollum.getGollumOwner();
		if (entityplayer == null)
		{
			return false;
		}
		else if (theGollum.isGollumSitting())
		{
			return false;
		}
		else if (theGollum.getDistanceSqToEntity(entityplayer) < (double)(minDist * minDist))
		{
			return false;
		}
		else
		{
			theOwner = entityplayer;
			return true;
		}
    }

    @Override
    public boolean continueExecuting()
    {
        return theGollum.getGollumOwner() != null && !theGollumPathfinder.noPath() && theGollum.getDistanceSqToEntity(theOwner) > (double)(maxDist * maxDist) && !theGollum.isGollumSitting();
    }

    @Override
    public void startExecuting()
    {
        followTick = 0;
        avoidsWater = theGollum.getNavigator().getAvoidsWater();
        theGollum.getNavigator().setAvoidsWater(false);
    }

    @Override
    public void resetTask()
    {
        theOwner = null;
        theGollumPathfinder.clearPathEntity();
        theGollum.getNavigator().setAvoidsWater(avoidsWater);
    }

    @Override
    public void updateTask()
    {
        theGollum.getLookHelper().setLookPositionWithEntity(theOwner, 10.0F, (float)theGollum.getVerticalFaceSpeed());

        if (!theGollum.isGollumSitting())
        {
            if (--followTick <= 0)
            {
                followTick = 10;
                if (!theGollumPathfinder.tryMoveToEntityLiving(theOwner, moveSpeed) && theGollum.getDistanceSqToEntity(theOwner) >= 256D)
				{
					int i = MathHelper.floor_double(theOwner.posX);
					int j = MathHelper.floor_double(theOwner.boundingBox.minY);
					int k = MathHelper.floor_double(theOwner.posZ);
					
					float f = theGollum.width / 2F;
					float f1 = theGollum.height;
					AxisAlignedBB theGollumBoundingBox = AxisAlignedBB.getBoundingBox(theOwner.posX - (double)f, theOwner.posY - (double)theGollum.yOffset + (double)theGollum.ySize, theOwner.posZ - (double)f, theOwner.posX + (double)f, theOwner.posY - (double)theGollum.yOffset + (double)theGollum.ySize + (double)f1, theOwner.posZ + (double)f);
					
					if (theWorld.func_147461_a(theGollumBoundingBox).isEmpty() && theWorld.getBlock(i, j - 1, k).isSideSolid(theWorld, i, j - 1, k, ForgeDirection.UP))
					{
						theGollum.setLocationAndAngles(theOwner.posX, theOwner.boundingBox.minY, theOwner.posZ, theGollum.rotationYaw, theGollum.rotationPitch);
						theGollum.fallDistance = 0F;
						theGollum.getNavigator().clearPathEntity();
					}
				}
            }
        }
    }
}
