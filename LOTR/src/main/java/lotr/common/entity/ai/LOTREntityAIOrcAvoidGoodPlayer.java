package lotr.common.entity.ai;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.entity.npc.LOTREntityOrc;
import lotr.common.world.biome.LOTRBiomeGenMordor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityAIOrcAvoidGoodPlayer extends EntityAIBase
{
    private LOTREntityOrc theOrc;
    private double speed;
    private EntityLivingBase closestLivingEntity;
    private float distanceFromEntity;
    private PathEntity entityPathEntity;
    private PathNavigate entityPathNavigate;

    public LOTREntityAIOrcAvoidGoodPlayer(LOTREntityOrc orc, float f, double d)
    {
        theOrc = orc;
        distanceFromEntity = f;
        speed = d;
        entityPathNavigate = orc.getNavigator();
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
		if (!theOrc.isWeakOrc || theOrc.hiredNPCInfo.isActive)
		{
			return false;
		}
		BiomeGenBase biome = theOrc.worldObj.getBiomeGenForCoords(MathHelper.floor_double(theOrc.posX), MathHelper.floor_double(theOrc.posZ));
		if (biome instanceof LOTRBiomeGenMordor)
		{
			return false;
		}

		List validPlayers = new ArrayList();
		List nearbyPlayers = theOrc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theOrc.boundingBox.expand((double)distanceFromEntity, (double)distanceFromEntity / 2D, (double)distanceFromEntity));
		if (nearbyPlayers.isEmpty())
		{
			return false;
		}
		
		List nearbyEvilNPCs = theOrc.worldObj.selectEntitiesWithinAABB(EntityLiving.class, theOrc.boundingBox.expand((double)distanceFromEntity, (double)distanceFromEntity / 2D, (double)distanceFromEntity), new LOTRNPCTargetSelector(theOrc));
		nearbyPlayersLoop:
		for (int i = 0; i < nearbyPlayers.size(); i++)
		{
			EntityPlayer entityplayer = (EntityPlayer)nearbyPlayers.get(i);
			if (entityplayer.capabilities.isCreativeMode || theOrc.getAITarget() == entityplayer)
			{
				continue nearbyPlayersLoop;
			}
			int alignment = LOTRLevelData.getAlignment(entityplayer, theOrc.getFaction());
			if (alignment <= LOTRAlignmentValues.ORC_FLEE)
			{
				for (int j = 0; j < nearbyEvilNPCs.size(); j++)
				{
					EntityLiving npc = (EntityLiving)nearbyEvilNPCs.get(j);
					if (npc.getAITarget() == entityplayer)
					{
						continue nearbyPlayersLoop;
					}
				}
				validPlayers.add(entityplayer);
			}
		}

		if (validPlayers.isEmpty())
		{
			return false;
		}
		closestLivingEntity = (EntityLivingBase)validPlayers.get(0);
		
        Vec3 fleePath = RandomPositionGenerator.findRandomTargetBlockAwayFrom(theOrc, 16, 7, Vec3.createVectorHelper(closestLivingEntity.posX, closestLivingEntity.posY, closestLivingEntity.posZ));
        if (fleePath == null)
        {
            return false;
        }
        else if (closestLivingEntity.getDistanceSq(fleePath.xCoord, fleePath.yCoord, fleePath.zCoord) < closestLivingEntity.getDistanceSqToEntity(theOrc))
        {
            return false;
        }
        else
        {
            entityPathEntity = entityPathNavigate.getPathToXYZ(fleePath.xCoord, fleePath.yCoord, fleePath.zCoord);
            return entityPathEntity == null ? false : entityPathEntity.isDestinationSame(fleePath);
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return !entityPathNavigate.noPath() && theOrc.getAITarget() != closestLivingEntity;
    }

    @Override
    public void startExecuting()
    {
        entityPathNavigate.setPath(entityPathEntity, speed);
    }

    @Override
    public void resetTask()
    {
        closestLivingEntity = null;
    }
}
