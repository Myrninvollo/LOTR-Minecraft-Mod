package lotr.common.entity.ai;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAINearestAttackableTargetTroll extends LOTREntityAINearestAttackableTargetBasic
{
    public LOTREntityAINearestAttackableTargetTroll(EntityCreature entity, Class targetClass, int chance, boolean flag)
    {
        super(entity, targetClass, chance, flag);
    }
	
    public LOTREntityAINearestAttackableTargetTroll(EntityCreature entity, Class targetClass, int chance, boolean flag, IEntitySelector selector)
    {
        super(entity, targetClass, chance, flag, selector);
    }
	
	@Override
    protected boolean isPlayerSuitableTarget(EntityPlayer entityplayer)
    {
		int alignment = LOTRLevelData.getData(entityplayer).getAlignment(LOTRMod.getNPCFaction(taskOwner));
		if (alignment >= LOTRAlignmentValues.Levels.TROLL_TRUST)
		{
			return false;
		}
		else if (alignment >= 0)
		{
			int chance = (alignment + 1) * 10;
			if (taskOwner.getRNG().nextInt(chance) != 0)
			{
				return false;
			}
		}
		return super.isPlayerSuitableTarget(entityplayer);
	}
}
