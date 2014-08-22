package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LOTREntityMirkwoodSpider extends LOTREntitySpiderBase
{
	public LOTREntityMirkwoodSpider(World world)
	{
		super(world);
	}
	
	@Override
	protected int getRandomSpiderScale()
	{
		return rand.nextInt(3);
	}
	
	@Override
	protected int getRandomSpiderType()
	{
		return rand.nextBoolean() ? 0 : 1 + rand.nextInt(2);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.DOL_GULDUR;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.MIRKWOOD_SPIDER;
	}
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        super.dropFewItems(flag, i);
		
		if (flag && rand.nextInt(6) == 0)
		{
			dropItem(LOTRMod.mysteryWeb, 1);
		}
    }
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killMirkwoodSpider;
	}
}
