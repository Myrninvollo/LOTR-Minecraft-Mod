package lotr.common.entity.npc;

import java.util.UUID;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.item.LOTRItemMountArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class LOTREntityNPCRideable extends LOTREntityNPC implements LOTRNPCMount
{
	private boolean npcTamed;
	private UUID tamingPlayer;
	private int npcTemper;
	
	public LOTREntityNPCRideable(World world)
	{
		super(world);
	}
	
	@Override
    public boolean isMountArmorValid(ItemStack itemstack)
    {
    	if (itemstack != null && itemstack.getItem() instanceof LOTRItemMountArmor)
        {
        	LOTRItemMountArmor armor = (LOTRItemMountArmor)itemstack.getItem();
        	return armor.isValid(this);
        }
    	return false;
    }
	
	public IInventory getMountInventory()
	{
		return null;
	}
	
	public void openGUI(EntityPlayer entityplayer)
	{
		IInventory inv = getMountInventory();
		if (inv != null)
		{
	        if (!worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer) && isNPCTamed())
	        {
	            entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_MOUNT_INV, worldObj, getEntityId(), inv.getSizeInventory(), 0);
	        }
		}
	}
	
	public boolean isNPCTamed()
	{
		return npcTamed;
	}
	
	public void tameNPC(EntityPlayer entityplayer)
	{
		npcTamed = true;
		tamingPlayer = entityplayer.getUniqueID();
	}
	
	public EntityPlayer getTamingPlayer()
	{
		return worldObj.func_152378_a(tamingPlayer);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		LOTRMountFunctions.update(this);
	}
	
	@Override
    public void moveEntityWithHeading(float strafe, float forward)
    {
		LOTRMountFunctions.move(this, strafe, forward);
    }
	
	@Override
    public void super_moveEntityWithHeading(float strafe, float forward)
    {
		super.moveEntityWithHeading(strafe, forward);
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("NPCTamed", npcTamed);
		if (tamingPlayer != null)
		{
			nbt.setString("NPCTamer", tamingPlayer.toString());
		}
		nbt.setInteger("NPCTemper", npcTemper);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		npcTamed = nbt.getBoolean("NPCTamed");
		if (nbt.hasKey("NPCTamer"))
		{
			tamingPlayer = UUID.fromString(nbt.getString("NPCTamer"));
		}
		npcTemper = nbt.getInteger("NPCTemper");
	}
	
	public int getMaxNPCTemper()
	{
		return 100;
	}
	
    public int getNPCTemper()
    {
        return npcTemper;
    }

    public void setNPCTemper(int i)
    {
    	npcTemper = i;
    }

    public int increaseNPCTemper(int i)
    {
        int temper = MathHelper.clamp_int(getNPCTemper() + i, 0, getMaxNPCTemper());
        setNPCTemper(temper);
        return getNPCTemper();
    }
    
    public void angerNPC()
    {
    	playSound(getHurtSound(), getSoundVolume(), getSoundPitch() * 1.5F);
    }
}
