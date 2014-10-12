package lotr.common.entity.item;

import lotr.common.block.LOTRBlockOrcBomb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTREntityOrcBomb extends LOTREntityTNT
{
	public int orcBombFuse;
	public boolean droppedByPlayer;
	public boolean droppedByHiredUnit;
	public boolean droppedTargetingPlayer;
	
	public LOTREntityOrcBomb(World world)
	{
		super(world);
	}
	
	public LOTREntityOrcBomb(World world, double d, double d1, double d2, EntityLivingBase entity)
	{
		super(world, d, d1, d2, entity);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
	}
	
	public int getBombStrengthLevel()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}
	
	public void setBombStrengthLevel(int i)
	{
		dataWatcher.updateObject(16, Integer.valueOf(i));
		orcBombFuse = 40 + LOTRBlockOrcBomb.getBombStrengthLevel(i) * 20;
	}
	
	public void setFuseFromExplosion()
	{
		orcBombFuse = worldObj.rand.nextInt(orcBombFuse / 4) + orcBombFuse / 8;
	}
	
	public void setFuseFromHiredUnit()
	{
		int strength = LOTRBlockOrcBomb.getBombStrengthLevel(getBombStrengthLevel());
	}
	
	@Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        
        motionY -= 0.04D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.98D;
        motionY *= 0.98D;
        motionZ *= 0.98D;

        if (onGround)
        {
            motionX *= 0.7D;
            motionZ *= 0.7D;
            motionY *= -0.5D;
        }
        
        orcBombFuse--;
        if (orcBombFuse <= 0 && !worldObj.isRemote)
        {
            setDead();
            explodeOrcBomb();
        }
        else
        {
            worldObj.spawnParticle("smoke", posX, posY + 0.7D, posZ, 0D, 0D, 0D);
        }
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("DroppedByPlayer", droppedByPlayer);
		nbt.setBoolean("DroppedByHiredUnit", droppedByHiredUnit);
		nbt.setBoolean("DroppedTargetingPlayer", droppedTargetingPlayer);
		nbt.setInteger("BombStrengthLevel", getBombStrengthLevel());
		nbt.setInteger("OrcBombFuse", orcBombFuse);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		droppedByPlayer = nbt.getBoolean("DroppedByPlayer");
		droppedByHiredUnit = nbt.getBoolean("DroppedByHiredUnit");
		droppedTargetingPlayer = nbt.getBoolean("DroppedTargetingPlayer");
		setBombStrengthLevel(nbt.getInteger("BombStrengthLevel"));
		orcBombFuse = nbt.getInteger("OrcBombFuse");
	}
	
    private void explodeOrcBomb()
    {
		boolean doTerrainDamage = false;
		if (droppedByPlayer)
		{
			doTerrainDamage = true;
		}
		else if (droppedByHiredUnit)
		{
			doTerrainDamage = worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
		}
		else if (droppedTargetingPlayer)
		{
			doTerrainDamage = worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
		}
		
		int meta = getBombStrengthLevel();
		int strength = LOTRBlockOrcBomb.getBombStrengthLevel(meta);
		boolean fire = LOTRBlockOrcBomb.isFireBomb(meta);
		
		worldObj.newExplosion(this, posX, posY, posZ, (strength + 1) * 4F, fire, doTerrainDamage);
    }
}
