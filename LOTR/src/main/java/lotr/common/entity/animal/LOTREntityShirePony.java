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
	public LOTREntityShirePony(World world)
	{
		super(world);
		setSize(1.12F, 1.28F);
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		if (!worldObj.isRemote)
		{
			data = super.onSpawnWithEgg(data);
			applyShirePonyTransformations();
			int i = getHorseVariant();
			int j = i & 255;
			if (j < 2)
			{
				j = 2 + rand.nextInt(5);
				int k = rand.nextInt(5);
				i = j | k << 8;
				setHorseVariant(i);
			}
			if (data instanceof EntityHorse.GroupData)
			{
				((EntityHorse.GroupData)data).field_111107_a = 1;
				((EntityHorse.GroupData)data).field_111106_b = j;
			}
		}
		else
		{
			int i;
			int j = 2 + rand.nextInt(5);
			int k = rand.nextInt(5);
			i = j | k << 8;
			setHorseVariant(i);
		}
		return data;
	}
	
	@Override
	public int getHorseType()
	{
		return worldObj.isRemote ? 0 : super.getHorseType();
	}
	
	@Override
    public boolean func_110259_cr()
    {
        return false;
    }
	
	@Override
    public String getCommandSenderName()
    {
        if (hasCustomNameTag())
        {
            return getCustomNameTag();
        }
        else
        {
        	String s = EntityList.getEntityString(this);
            return StatCollector.translateToLocal("entity." + s + ".name");
        }
    }
	
	@Override
    public EntityAgeable createChild(EntityAgeable entityageable)
	{
		LOTREntityShirePony pony = (LOTREntityShirePony)super.createChild(entityageable);
		pony.applyShirePonyTransformations();
        return pony;
    }
	
	public void applyShirePonyTransformations()
	{
		setHorseType(1);
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
        super.getLivingSound();
		return "mob.horse.idle";
    }
	
	@Override
    protected String getHurtSound()
    {
        super.getHurtSound();
		return "mob.horse.hit";
    }
	
	@Override
    protected String getDeathSound()
    {
        super.getDeathSound();
		return "mob.horse.death";
    }
	
	@Override
    protected String getAngrySoundName()
    {
        super.getAngrySoundName();
        return "mob.horse.angry";
    }
}
