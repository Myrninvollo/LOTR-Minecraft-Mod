package lotr.common.entity.ai;

import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNames;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class LOTREntityAINPCMate extends EntityAIBase
{
    private LOTREntityNPC theNPC;
    private World theWorld;
    private LOTREntityNPC theSpouse;
    private int spawnBabyDelay = 0;
    private double moveSpeed;

    public LOTREntityAINPCMate(LOTREntityNPC npc, double d)
    {
        theNPC = npc;
        theWorld = npc.worldObj;
        moveSpeed = d;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        if (theNPC.getClass() != theNPC.familyInfo.marriageEntityClass || theNPC.familyInfo.spouseUniqueID == null || theNPC.familyInfo.children >= theNPC.familyInfo.maxChildren || theNPC.familyInfo.getNPCAge() != 0)
        {
            return false;
        }
        else
        {
            theSpouse = theNPC.familyInfo.getSpouse();
            return theSpouse != null && theNPC.getDistanceToEntity(theSpouse) < 16D && theSpouse.familyInfo.children < theSpouse.familyInfo.maxChildren && theSpouse.familyInfo.getNPCAge() == 0;
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return theSpouse.isEntityAlive() && spawnBabyDelay < 60 && theNPC.familyInfo.getNPCAge() == 0 && theSpouse.familyInfo.getNPCAge() == 0;
    }

    @Override
    public void resetTask()
    {
        theSpouse = null;
        spawnBabyDelay = 0;
    }

    @Override
    public void updateTask()
    {
        theNPC.getLookHelper().setLookPositionWithEntity(theSpouse, 10F, (float)theNPC.getVerticalFaceSpeed());
        theNPC.getNavigator().tryMoveToEntityLiving(theSpouse, moveSpeed);
        spawnBabyDelay++;
		if (spawnBabyDelay % 20 == 0)
		{
			theNPC.spawnHearts();
		}
        if (spawnBabyDelay >= 60 && theNPC.getDistanceSqToEntity(theSpouse) < 9D)
        {
            spawnBaby();
        }
    }

    private void spawnBaby()
    {
        LOTREntityNPC baby = (LOTREntityNPC)LOTREntities.createEntityByClass(theNPC.familyInfo.marriageEntityClass, theWorld);
        if (theNPC.familyInfo.isNPCMale())
        {
        	baby.familyInfo.maleParentID = theNPC.getPersistentID();
        	baby.familyInfo.femaleParentID = theSpouse.getPersistentID();
        }
        else
        {
        	baby.familyInfo.maleParentID = theSpouse.getPersistentID();
        	baby.familyInfo.femaleParentID = theNPC.getPersistentID();
        }
        
        LOTREntityNPC maleParent = theNPC.familyInfo.isNPCMale() ? theNPC : theSpouse;
        LOTREntityNPC femaleParent = theNPC.familyInfo.isNPCMale() ? theSpouse : theNPC;
        
		baby.createNPCChildName(maleParent, femaleParent);
		baby.onSpawnWithEgg(null);
		theNPC.familyInfo.setMaxBreedingDelay();
		theSpouse.familyInfo.setMaxBreedingDelay();
		baby.familyInfo.setChild();
		baby.setLocationAndAngles(theNPC.posX, theNPC.posY, theNPC.posZ, 0F, 0F);
		baby.isNPCPersistent = true;
		theWorld.spawnEntityInWorld(baby);
		theNPC.familyInfo.children++;
		theSpouse.familyInfo.children++;
		theNPC.spawnHearts();
		theSpouse.spawnHearts();
    }
}
