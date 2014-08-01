package lotr.common.entity.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityBandit;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class LOTREntityAIBanditFlee extends EntityAIBase
{
    private LOTREntityBandit theBandit;
    private double speed;
    private double range;
    private EntityPlayer targetPlayer;

    public LOTREntityAIBanditFlee(LOTREntityBandit bandit, double d)
    {
    	theBandit = bandit;
        speed = d;
        range = bandit.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
    	if (theBandit.getAttackTarget() != null)
    	{
    		return false;
    	}
    	
    	if (LOTRMod.isInventoryEmpty(theBandit.banditInventory))
    	{
    		return false;
    	}
        else
        {
            targetPlayer = findNearestPlayer();
            return targetPlayer != null;
        }
    }
    
    private EntityPlayer findNearestPlayer()
    {
    	List players = theBandit.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theBandit.boundingBox.expand(range, range, range));
        double distance = range;

        EntityPlayer ret = null;
        for (int i = 0; i < players.size(); i++)
        {
        	EntityPlayer entityplayer = (EntityPlayer)players.get(i);
        	if (entityplayer.capabilities.isCreativeMode)
        	{
        		continue;
        	}
        	
        	double d = theBandit.getDistanceToEntity(entityplayer);
        	if (d < distance)
        	{
        		distance = d;
        		ret = entityplayer;
        	}
        }
        
        return ret;
    }
    
    @Override
    public void updateTask()
    {
		if (theBandit.getNavigator().noPath())
		{
			Vec3 away = RandomPositionGenerator.findRandomTargetBlockAwayFrom(theBandit, (int)range, 10, Vec3.createVectorHelper(targetPlayer.posX, targetPlayer.posY, targetPlayer.posZ));
			theBandit.getNavigator().tryMoveToXYZ(away.xCoord, away.yCoord, away.zCoord, speed);
			
			targetPlayer = findNearestPlayer();
		}
    }
    
    @Override
    public boolean continueExecuting()
    {
    	if (targetPlayer == null || !targetPlayer.isEntityAlive() || targetPlayer.capabilities.isCreativeMode)
    	{
    		return false;
    	}
        return theBandit.getAttackTarget() == null && theBandit.getDistanceSqToEntity(targetPlayer) < range * range;
    }
    
    @Override
    public void resetTask()
    {
    	targetPlayer = null;
    }
}
