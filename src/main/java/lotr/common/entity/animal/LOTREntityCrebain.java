package lotr.common.entity.animal;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class LOTREntityCrebain extends LOTREntityBird
{
	public LOTREntityCrebain(World world)
	{
		super(world);
		setSize(0.9F, 0.9F);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10D);
    }
	
	@Override
    protected String getLivingSound()
    {
        return "lotr:bird.crebain.say";
    }

   @Override
    protected String getHurtSound()
    {
        return "lotr:bird.crebain.hurt";
    }

    @Override
    protected String getDeathSound()
    {
        return "lotr:bird.crebain.hurt";
    }
}
