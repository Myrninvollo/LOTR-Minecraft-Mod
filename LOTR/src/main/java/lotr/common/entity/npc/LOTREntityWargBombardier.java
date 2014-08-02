package lotr.common.entity.npc;

import lotr.common.entity.ai.LOTREntityAIWargBombardierAttack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class LOTREntityWargBombardier extends LOTREntityWarg
{
	public LOTREntityWargBombardier(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase getWargAttackAI()
	{
		return new LOTREntityAIWargBombardierAttack(this, 1.7D);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Byte.valueOf((byte)35));
		dataWatcher.addObject(21, Byte.valueOf((byte)1));
	}
	
	public int getBombFuse()
	{
		return dataWatcher.getWatchableObjectByte(20);
	}
	
	public void setBombFuse(int i)
	{
		dataWatcher.updateObject(20, Byte.valueOf((byte)i));
	}
	
	public int getBombStrengthLevel()
	{
		return dataWatcher.getWatchableObjectByte(21);
	}
	
	public void setBombStrengthLevel(int i)
	{
		dataWatcher.updateObject(21, Byte.valueOf((byte)i));
	}
	
	@Override
	public LOTREntityNPC createWargRider()
	{
		return null;
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setByte("BombFuse", (byte)getBombFuse());
		nbt.setByte("BombStrengthLevel", (byte)getBombStrengthLevel());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        setBombFuse(nbt.getByte("BombFuse"));
		setBombStrengthLevel(nbt.getByte("BombStrengthLevel"));
    }
	
	@Override
	public boolean canWargBeRidden()
	{
		return false;
	}
	
	@Override
    public boolean isMountSaddled()
    {
        return false;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (getBombFuse() < 35)
		{
			worldObj.spawnParticle("smoke", posX, posY + 2.2D, posZ, 0D, 0D, 0D);
		}
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase target)
	{
		super.setAttackTarget(target);
		if (target != null)
		{
			worldObj.playSoundAtEntity(this, "random.fuse", 1F, 1F);
		}
	}
}
