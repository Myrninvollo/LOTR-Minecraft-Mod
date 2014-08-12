package lotr.common.entity.ai;

import java.util.Random;

import lotr.common.entity.npc.LOTREntityGollum;
import lotr.common.entity.npc.LOTRSpeech;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LOTREntityAIGollumFishing extends EntityAIBase
{
    private LOTREntityGollum theGollum;
    private double moveSpeed;
    private boolean avoidsWater;
	private World theWorld;
    private double xPosition;
    private double yPosition;
    private double zPosition;
	private int moveTick;
	private int fishTick;
	private boolean finished;
	
    public LOTREntityAIGollumFishing(LOTREntityGollum entity, double d)
    {
        theGollum = entity;
        moveSpeed = d;
		theWorld = entity.worldObj;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
		if (theGollum.getGollumOwner() == null)
		{
			return false;
		}
		else if (theGollum.isGollumSitting())
		{
			return false;
		}
		else if (theGollum.prevFishTime > 0)
		{
			return false;
		}
		else if (theGollum.isFishing)
		{
			return false;
		}
		else if (theGollum.getEquipmentInSlot(0) != null)
		{
			return false;
		}
		else if (theGollum.getRNG().nextInt(60) == 0)
		{
            Vec3 vec3 = findPossibleFishingLocation();
            if (vec3 == null)
            {
				return false;
            }

			xPosition = vec3.xCoord;
			yPosition = vec3.yCoord;
			zPosition = vec3.zCoord;
			return true;
		}
		return false;
    }
	
    private Vec3 findPossibleFishingLocation()
    {
        Random random = theGollum.getRNG();
        for (int l = 0; l < 32; l++)
        {
            int i = MathHelper.floor_double(theGollum.posX) - 16 + random.nextInt(33);
            int j = MathHelper.floor_double(theGollum.boundingBox.minY) - 8 + random.nextInt(17);
            int k = MathHelper.floor_double(theGollum.posZ) - 16 + random.nextInt(33);

            if (!theWorld.getBlock(i, j + 1, k).isNormalCube() && !theWorld.getBlock(i, j, k).isNormalCube() && theWorld.getBlock(i, j - 1, k).getMaterial() == Material.water)
			{
				return Vec3.createVectorHelper((double)i, (double)j, (double)k);
			}
        }

        return null;
    }

    @Override
    public boolean continueExecuting()
    {
        return theGollum.getGollumOwner() != null && !theGollum.isGollumSitting() && moveTick < 300 && !finished;
    }

    @Override
    public void startExecuting()
    {
        avoidsWater = theGollum.getNavigator().getAvoidsWater();
        theGollum.getNavigator().setAvoidsWater(false);
		theGollum.isFishing = true;
    }

    @Override
    public void resetTask()
    {
        theGollum.getNavigator().clearPathEntity();
        theGollum.getNavigator().setAvoidsWater(avoidsWater);
		moveTick = 0;
		fishTick = 0;
		if (finished)
		{
			finished = false;
			theGollum.prevFishTime = 3000;
		}
		else
		{
			theGollum.prevFishTime = 600;
		}
		theGollum.isFishing = false;
    }

    @Override
    public void updateTask()
    {
		if (atFishingLocation())
		{
			if (theGollum.isInWater())
			{
				theWorld.setEntityState(theGollum, (byte)15);
				if (theGollum.getRNG().nextInt(4) == 0)
				{
					theWorld.playSoundAtEntity(theGollum, theGollum.getSplashSound(), 1F, 1F + (theGollum.getRNG().nextFloat() - theGollum.getRNG().nextFloat()) * 0.4F);
				}
				theGollum.getJumpHelper().setJumping();
				if (theGollum.getRNG().nextInt(50) == 0)
				{
					theGollum.getGollumOwner().addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(theGollum, "gollum_fishing", theGollum.getGollumOwner()));
				}
			}
			fishTick++;
			if (fishTick > 100)
			{
				theGollum.setCurrentItemOrArmor(0, new ItemStack(Items.fish, 4 + theGollum.getRNG().nextInt(9)));
				finished = true;
				theGollum.getGollumOwner().addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(theGollum, "gollum_catchFish", theGollum.getGollumOwner()));
			}
		}
		else
		{
			theGollum.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, moveSpeed);
			moveTick++;
		}
    }
	
	private boolean atFishingLocation()
	{
		if (theGollum.getDistanceSq(xPosition, yPosition, zPosition) < 4D)
		{
			int i = MathHelper.floor_double(theGollum.posX);
			int j = MathHelper.floor_double(theGollum.boundingBox.minY);
			int k = MathHelper.floor_double(theGollum.posZ);
			return theWorld.getBlock(i, j, k).getMaterial() == Material.water || theWorld.getBlock(i, j - 1, k).getMaterial() == Material.water;
		}
		return false;
	}
}
