package lotr.common.entity.animal;

import lotr.common.*;
import lotr.common.world.biome.LOTRBiomeGenNearHarad.ImmuneToHeat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LOTREntityCamel extends LOTREntityHorse implements ImmuneToHeat
{
	public LOTREntityCamel(World world)
	{
		super(world);
		setSize(1.6F, 1.8F);
	}
	
	@Override
	public int getHorseType()
	{
		return worldObj.isRemote ? 0 : 1;
	}
	
	@Override
	protected void onLOTRHorseSpawn()
	{
		double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getAttributeValue();
		jumpStrength *= 0.5D;
		getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

        if (!worldObj.isRemote)
        {
        	if (riddenByEntity instanceof EntityPlayer && isMountSaddled())
			{
				LOTRLevelData.getData((EntityPlayer)riddenByEntity).addAchievement(LOTRAchievement.rideCamel);
			}
		}
	}
	
	@Override
    protected String getLivingSound()
    {
		return "";
    }
	
	@Override
    protected String getHurtSound()
    {
		return "";
    }
	
	@Override
    protected String getDeathSound()
    {
		return "";
    }
	
	@Override
    protected String getAngrySoundName()
    {
		return "";
    }
}
