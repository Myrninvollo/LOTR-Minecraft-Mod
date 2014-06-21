package lotr.common.entity.npc;

import java.util.List;
import java.util.UUID;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LOTRFamilyInfo
{
	private LOTREntityNPC theEntity;
	
	public Class marriageEntityClass;
	public Item marriageRing;
	public int marriageAlignmentRequired;
	public LOTRAchievement marriageAchievement;
	public int potentialMaxChildren;
	public int timeToMature;
	public int breedingDelay;
	
	public UUID spouseUniqueID;
	public int children;
	public int maxChildren;
	public UUID maleParentID;
	public UUID femaleParentID;
	public UUID ringGivingPlayer;
	
	public LOTRFamilyInfo(LOTREntityNPC npc)
	{
		theEntity = npc;
		theEntity.getDataWatcher().addObject(25, Integer.valueOf(0));
		theEntity.getDataWatcher().addObject(26, Byte.valueOf((byte)0));
	}
	
	public int getNPCAge()
	{
		return theEntity.getDataWatcher().getWatchableObjectInt(25);
	}
	
	public void setNPCAge(int i)
	{
		theEntity.getDataWatcher().updateObject(25, Integer.valueOf(i));
	}
	
	public boolean isNPCMale()
	{
		return theEntity.getDataWatcher().getWatchableObjectByte(26) == (byte)1;
	}
	
	public void setNPCMale(boolean flag)
	{
		theEntity.getDataWatcher().updateObject(26, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	public void onUpdate()
	{
		if (getNPCAge() < 0)
		{
			setNPCAge(getNPCAge() + 1);
		}
		else if (getNPCAge() > 0)
		{
			setNPCAge(getNPCAge() - 1);
		}
	}
	
	public boolean canMarryNPC(LOTREntityNPC npc)
	{
		if (npc.getClass() != theEntity.getClass() || npc.familyInfo.spouseUniqueID != null || npc.familyInfo.getNPCAge() != 0 || npc.getEquipmentInSlot(4) != null)
		{
			return false;
		}
		if (npc == theEntity || npc.familyInfo.isNPCMale() == isNPCMale() || (maleParentID != null && maleParentID == npc.familyInfo.maleParentID) || (femaleParentID != null && femaleParentID == npc.familyInfo.femaleParentID))
		{
			return false;
		}
		ItemStack heldItem = npc.getEquipmentInSlot(0);
		return heldItem != null && heldItem.getItem() == marriageRing;
	}
	
	public LOTREntityNPC getSpouse()
	{
		if (spouseUniqueID == null)
		{
			return null;
		}
		List list = theEntity.worldObj.getEntitiesWithinAABB(theEntity.getClass(), theEntity.boundingBox.expand(16D, 8D, 16D));
		for (int i = 0; i < list.size(); i++)
		{
			Entity entity = (Entity)list.get(i);
			if (entity instanceof LOTREntityNPC && entity != theEntity && entity.getPersistentID().equals(spouseUniqueID))
			{
				LOTREntityNPC npc = (LOTREntityNPC)entity;
				if (npc.familyInfo.spouseUniqueID != null && theEntity.getPersistentID().equals(npc.familyInfo.spouseUniqueID))
				{
					return npc;
				}
			}
		}
		return null;
	}
	
	public LOTREntityNPC getParentToFollow()
	{
		UUID parentToFollowID = isNPCMale() ? maleParentID : femaleParentID;
		List list = theEntity.worldObj.getEntitiesWithinAABB(theEntity.getClass(), theEntity.boundingBox.expand(16D, 8D, 16D));
		for (int i = 0; i < list.size(); i++)
		{
			Entity entity = (Entity)list.get(i);
			if (entity instanceof LOTREntityNPC && entity != theEntity && parentToFollowID != null && entity.getPersistentID().equals(parentToFollowID))
			{
				return (LOTREntityNPC)entity;
			}
		}
		return null;
	}
	
	public boolean interact(EntityPlayer entityplayer)
	{
		if (theEntity.hiredNPCInfo.isActive)
		{
			return false;
		}
		
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() == marriageRing && LOTRLevelData.getAlignment(entityplayer, theEntity.getFaction()) >= marriageAlignmentRequired)
		{
			if (theEntity.getClass() == marriageEntityClass && getNPCAge() == 0 && theEntity.getEquipmentInSlot(0) == null && theEntity.getEquipmentInSlot(4) == null && spouseUniqueID == null)
			{
				if (!entityplayer.capabilities.isCreativeMode)
				{
					itemstack.stackSize--;
					if (itemstack.stackSize <= 0)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
					}
				}
				if (!theEntity.worldObj.isRemote)
				{
					theEntity.setCurrentItemOrArmor(0, new ItemStack(marriageRing));
					ringGivingPlayer = entityplayer.getUniqueID();
				}
				theEntity.isNPCPersistent = true;
				return true;
			}
		}
		return false;
	}
	
	public EntityPlayer getRingGivingPlayer()
	{
		if (ringGivingPlayer != null)
		{
			for (Object obj : theEntity.worldObj.playerEntities)
			{
				EntityPlayer entityplayer = (EntityPlayer)obj;
				if (entityplayer.getUniqueID().equals(ringGivingPlayer))
				{
					return entityplayer;
				}
			}
		}
		return null;
	}
	
	public void setChild()
	{
		setNPCAge(-timeToMature);
	}
	
	public void setMaxBreedingDelay()
	{
		float f = (float)breedingDelay;
		f *= 0.5F + theEntity.getRNG().nextFloat() * 0.5F;
		setNPCAge((int)f);
	}
	
	public int getRandomMaxChildren()
	{
		return 1 + theEntity.getRNG().nextInt(potentialMaxChildren);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("NPCAge", getNPCAge());
		nbt.setBoolean("NPCMale", isNPCMale());
		
		if (spouseUniqueID != null)
		{
			nbt.setLong("SpouseUUIDMost", spouseUniqueID.getMostSignificantBits());
			nbt.setLong("SpouseUUIDLeast", spouseUniqueID.getLeastSignificantBits());
		}
		nbt.setInteger("Children", children);
		nbt.setInteger("MaxChildren", maxChildren);
		if (maleParentID != null)
		{
			nbt.setLong("MaleParentUUIDMost", maleParentID.getMostSignificantBits());
			nbt.setLong("MaleParentUUIDLeast", maleParentID.getLeastSignificantBits());
		}
		if (femaleParentID != null)
		{
			nbt.setLong("FemaleParentUUIDMost", femaleParentID.getMostSignificantBits());
			nbt.setLong("FemaleParentUUIDLeast", femaleParentID.getLeastSignificantBits());
		}
		if (ringGivingPlayer != null)
		{
			nbt.setLong("RingGivingPlayerUUIDMost", ringGivingPlayer.getMostSignificantBits());
			nbt.setLong("RingGivingPlayerUUIDLeast", ringGivingPlayer.getLeastSignificantBits());
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		setNPCAge(nbt.getInteger("NPCAge"));
		setNPCMale(nbt.getBoolean("NPCMale"));
		
		if (nbt.hasKey("SpouseUUIDMost") && nbt.hasKey("SpouseUUIDLeast"))
		{
			spouseUniqueID = new UUID(nbt.getLong("SpouseUUIDMost"), nbt.getLong("SpouseUUIDLeast"));
		}
		children = nbt.getInteger("Children");
		maxChildren = nbt.getInteger("MaxChildren");
		if (nbt.hasKey("MaleParentUUIDMost") && nbt.hasKey("MaleParentUUIDLeast"))
		{
			maleParentID = new UUID(nbt.getLong("MaleParentUUIDMost"), nbt.getLong("MaleParentUUIDLeast"));
		}
		if (nbt.hasKey("FemaleParentUUIDMost") && nbt.hasKey("FemaleParentUUIDLeast"))
		{
			femaleParentID = new UUID(nbt.getLong("FemaleParentUUIDMost"), nbt.getLong("FemaleParentUUIDLeast"));
		}
		if (nbt.hasKey("RingGivingPlayer"))
		{
			ringGivingPlayer = new UUID(nbt.getLong("RingGivingPlayerUUIDMost"), nbt.getLong("RingGivingPlayerUUIDLeast"));
		}
	}
}
