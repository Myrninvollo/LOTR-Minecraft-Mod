package lotr.common.entity.animal;

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
	protected void onLOTRHorseSpawn() {}
}
