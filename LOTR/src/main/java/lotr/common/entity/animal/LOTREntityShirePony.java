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
		double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getAttributeValue();
		jumpStrength *= 0.5D;
		getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength);
        
		double moveSpeed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue();
		moveSpeed *= 0.8D;
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(moveSpeed);
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
				LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.rideShirePony);
			}
		}
	}
	
	@Override
    protected String getLivingSound()
	{
		return "mob.horse.idle";
    }
	
	@Override
    protected String getHurtSound()
    {
		return "mob.horse.hit";
    }
	
	@Override
    protected String getDeathSound()
    {
		return "mob.horse.death";
    }
	
	@Override
    protected String getAngrySoundName()
    {
		return "mob.horse.angry";
    }
}
