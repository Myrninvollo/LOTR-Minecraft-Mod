package lotr.common.entity.ai;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAINearestAttackableTargetWoodElf extends LOTREntityAINearestAttackableTargetBasic
{
    public LOTREntityAINearestAttackableTargetWoodElf(EntityCreature entity, Class targetClass, int chance, boolean flag)
    {
        super(entity, targetClass, chance, flag);
    }
	
    public LOTREntityAINearestAttackableTargetWoodElf(EntityCreature entity, Class targetClass, int chance, boolean flag, IEntitySelector selector)
    {
        super(entity, targetClass, chance, flag, selector);
    }
	
	@Override
    protected boolean isPlayerSuitableTarget(EntityPlayer entityplayer)
    {
		int alignment = LOTRLevelData.getData(entityplayer).getAlignment(LOTRFaction.WOOD_ELF);
		if (alignment >= LOTRAlignmentValues.Levels.WOOD_ELF_TRUST)
		{
			return false;
		}
		else if (alignment >= 0)
		{
			int chance = (alignment + 1) * 20;
			if (taskOwner.getRNG().nextInt(chance) != 0)
			{
				return false;
			}
		}
		return super.isPlayerSuitableTarget(entityplayer);
	}
}
