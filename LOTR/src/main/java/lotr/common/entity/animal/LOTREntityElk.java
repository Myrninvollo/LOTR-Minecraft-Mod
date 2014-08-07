package lotr.common.entity.animal;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class LOTREntityElk extends LOTREntityHorse
{
	public LOTREntityElk(World world)
	{
		super(world);
	}
	
	@Override
	public int getHorseType()
	{
		return 0;
	}
	
	@Override
    public boolean func_110259_cr()
    {
        return false;
    }
	
	@Override
	protected void onLOTRHorseSpawn()
	{
		double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
		maxHealth = (double)(maxHealth * (1F + rand.nextFloat() * 0.5F));
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
	}
}
