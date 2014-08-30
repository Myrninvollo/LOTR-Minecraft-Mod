package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityNearHaradrimWarlord extends LOTREntityNearHaradrimWarrior implements LOTRUnitTradeable
{
	public LOTREntityNearHaradrimWarlord(World world)
	{
		super(world);
		addTargetTasks(false);
		npcCape = LOTRCapes.NEAR_HARAD;
	}
	
	@Override
	public EntityAIBase createHaradrimAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.6D, false);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.scimitarNearHarad));
		setCurrentItemOrArmor(4, null);
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.NEAR_HARADRIM_WARLORD;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.NEAR_HARADRIM_WARLORD;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.NEAR_HARADRIM_WARLORD_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeNearHaradWarlord);
	}
	
	@Override
	public boolean shouldTraderRespawn()
	{
		return true;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (canTradeWith(entityplayer))
			{
				return "nearHaradrimWarlord_friendly";
			}
			else
			{
				return "nearHaradrimWarlord_neutral";
			}
		}
		else
		{
			return "nearHaradrim_hostile";
		}
	}
}
