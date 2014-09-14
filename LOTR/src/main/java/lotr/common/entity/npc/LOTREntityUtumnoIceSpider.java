package lotr.common.entity.npc;

import lotr.common.LOTRFaction;
import lotr.common.world.biome.LOTRBiomeGenForodwaith;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LOTREntityUtumnoIceSpider extends LOTREntitySpiderBase
{
	public LOTREntityUtumnoIceSpider(World world)
	{
		super(world);
	}
	
	@Override
	protected int getRandomSpiderScale()
	{
		return rand.nextInt(4);
	}
	
	@Override
	protected int getRandomSpiderType()
	{
		return VENOM_SLOWNESS;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.UTUMNO;
	}
	
	@Override
	protected boolean canRideSpider()
	{
		return false;
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entity)
    {
        if (super.attackEntityAsMob(entity))
        {
        	entity.attackEntityFrom(LOTRBiomeGenForodwaith.frost, 1F);
        	return true;
        }
        return false;
    }
}
