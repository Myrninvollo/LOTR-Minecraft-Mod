package lotr.common.entity.animal;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRReflection;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class LOTREntityShirePony extends LOTREntityHorse
{
	public static float PONY_SCALE = 0.8F;
	
	public LOTREntityShirePony(World world)
	{
		super(world);
		setSize(width * PONY_SCALE, height * PONY_SCALE);
	}
	
	@Override
	public int getHorseType()
	{
		return worldObj.isRemote ? 0 : 1;
	}
	
	@Override
    public boolean func_110259_cr()
    {
        return false;
    }
	
	@Override
	protected void onLOTRHorseSpawn()
	{
        double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getBaseValue();
        getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength * 0.5D);
        
		double moveSpeed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue();
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(moveSpeed * 0.8D);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (!worldObj.isRemote && riddenByEntity != null && riddenByEntity instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)riddenByEntity;
			if (isHorseSaddled() && isChested())
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.rideShirePony);
			}
		}
	}
	
	@Override
    protected String getLivingSound()
    {
		int i = getHorseType();
		setHorseType(0);
        String s = super.getLivingSound();
        setHorseType(i);
		return s;
    }
	
	@Override
    protected String getHurtSound()
    {
		int i = getHorseType();
		setHorseType(0);
        String s = super.getHurtSound();
        setHorseType(i);
		return s;
    }
	
	@Override
    protected String getDeathSound()
    {
		int i = getHorseType();
		setHorseType(0);
        String s = super.getDeathSound();
        setHorseType(i);
		return s;
    }
	
	@Override
    protected String getAngrySoundName()
    {
		int i = getHorseType();
		setHorseType(0);
        String s = super.getAngrySoundName();
        setHorseType(i);
		return s;
    }
}
