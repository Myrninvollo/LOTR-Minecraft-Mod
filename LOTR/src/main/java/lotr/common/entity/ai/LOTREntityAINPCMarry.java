package lotr.common.entity.ai;

import java.util.Iterator;
import java.util.List;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNames;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityAINPCMarry extends EntityAIBase
{
    private LOTREntityNPC theNPC;
    private World theWorld;
    private LOTREntityNPC theSpouse;
    private int marryDelay = 0;
    private double moveSpeed;

    public LOTREntityAINPCMarry(LOTREntityNPC npc, double d)
    {
        theNPC = npc;
        theWorld = npc.worldObj;
        moveSpeed = d;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        if (theNPC.getClass() != theNPC.familyInfo.marriageEntityClass || theNPC.familyInfo.spouseUniqueID != null || theNPC.familyInfo.getNPCAge() != 0 || theNPC.getEquipmentInSlot(4) != null || theNPC.getEquipmentInSlot(0) == null)
        {
            return false;
        }
		
		List list = theNPC.worldObj.getEntitiesWithinAABB(theNPC.familyInfo.marriageEntityClass, theNPC.boundingBox.expand(16D, 4D, 16D));
		LOTREntityNPC spouse = null;
		double distanceSq = Double.MAX_VALUE;
		Iterator iterator = list.iterator();

		while (iterator.hasNext())
		{
			LOTREntityNPC candidate = (LOTREntityNPC)iterator.next();

			if (theNPC.familyInfo.canMarryNPC(candidate) && candidate.familyInfo.canMarryNPC(theNPC))
			{
				double d = theNPC.getDistanceSqToEntity(candidate);

				if (d <= distanceSq)
				{
					distanceSq = d;
					spouse = candidate;
				}
			}
		}

		if (spouse == null)
		{
			return false;
		}
		else
		{
			theSpouse = spouse;
			return true;
		}
    }

    @Override
    public boolean continueExecuting()
    {
        return theSpouse != null && theSpouse.isEntityAlive() && theNPC.familyInfo.canMarryNPC(theSpouse) && theSpouse.familyInfo.canMarryNPC(theNPC);
    }

    @Override
    public void resetTask()
    {
        theSpouse = null;
        marryDelay = 0;
    }

    @Override
    public void updateTask()
    {
        theNPC.getLookHelper().setLookPositionWithEntity(theSpouse, 10F, (float)theNPC.getVerticalFaceSpeed());
        theNPC.getNavigator().tryMoveToEntityLiving(theSpouse, moveSpeed);
        marryDelay++;
		if (marryDelay % 20 == 0)
		{
			theNPC.spawnHearts();
		}
        if (marryDelay >= 60 && theNPC.getDistanceSqToEntity(theSpouse) < 9D)
        {
            marry();
        }
    }

    private void marry()
    {
        theNPC.familyInfo.spouseUniqueID = theSpouse.getPersistentID();
		theSpouse.familyInfo.spouseUniqueID = theNPC.getPersistentID();
		theNPC.setCurrentItemOrArmor(0, null);
		theNPC.setCurrentItemOrArmor(4, new ItemStack(theNPC.familyInfo.marriageRing));
		theSpouse.setCurrentItemOrArmor(0, null);
		theSpouse.setCurrentItemOrArmor(4, new ItemStack(theNPC.familyInfo.marriageRing));
		theNPC.changeNPCNameForMarriage(theSpouse);
		theSpouse.changeNPCNameForMarriage(theNPC);
		
		int maxChildren = theNPC.familyInfo.getRandomMaxChildren();
		theNPC.familyInfo.maxChildren = maxChildren;
		theSpouse.familyInfo.maxChildren = maxChildren;
		theNPC.familyInfo.setMaxBreedingDelay();
		theSpouse.familyInfo.setMaxBreedingDelay();
		theNPC.spawnHearts();
		theSpouse.spawnHearts();
		
		if (theNPC.familyInfo.getRingGivingPlayer() != null)
		{
			LOTRLevelData.getData(theNPC.familyInfo.getRingGivingPlayer()).addAlignment(LOTRAlignmentValues.MARRIAGE_BONUS, theNPC.getFaction(), theNPC);
			if (theNPC.familyInfo.marriageAchievement != null)
			{
				LOTRLevelData.getData(theNPC.familyInfo.getRingGivingPlayer()).addAchievement(theNPC.familyInfo.marriageAchievement);
			}
		}
		
		if (theSpouse.familyInfo.getRingGivingPlayer() != null)
		{
			LOTRLevelData.getData(theSpouse.familyInfo.getRingGivingPlayer()).addAlignment(LOTRAlignmentValues.MARRIAGE_BONUS, theSpouse.getFaction(), theSpouse);
			if (theSpouse.familyInfo.marriageAchievement != null)
			{
				LOTRLevelData.getData(theSpouse.familyInfo.getRingGivingPlayer()).addAchievement(theSpouse.familyInfo.marriageAchievement);
			}
		}
		
		theWorld.spawnEntityInWorld(new EntityXPOrb(theWorld, theNPC.posX, theNPC.posY, theNPC.posZ, theNPC.getRNG().nextInt(8) + 2));
    }
}
