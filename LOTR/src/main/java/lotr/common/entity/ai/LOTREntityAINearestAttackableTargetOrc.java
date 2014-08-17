package lotr.common.entity.ai;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAINearestAttackableTargetOrc extends LOTREntityAINearestAttackableTargetBasic
{
    public LOTREntityAINearestAttackableTargetOrc(EntityCreature entity, Class targetClass, int chance, boolean flag)
    {
        super(entity, targetClass, chance, flag);
    }
	
    public LOTREntityAINearestAttackableTargetOrc(EntityCreature entity, Class targetClass, int chance, boolean flag, IEntitySelector selector)
    {
        super(entity, targetClass, chance, flag, selector);
    }
	
	@Override
    protected boolean isPlayerSuitableTarget(EntityPlayer entityplayer)
    {
		LOTRFaction faction = LOTRMod.getNPCFaction(taskOwner);
		if (faction == LOTRFaction.MORDOR)
		{
			int alignment = LOTRLevelData.getData(entityplayer).getAlignment(faction);
			if (alignment >= LOTRAlignmentValues.Levels.MORDOR_TRUST)
			{
				return false;
			}
			else if (alignment >= 0)
			{
				int chance = (alignment + 1) * 5;
				if (taskOwner.getRNG().nextInt(chance) != 0)
				{
					return false;
				}
			}
		}
		return super.isPlayerSuitableTarget(entityplayer);
	}
}
