package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityMordorOrcSlaver extends LOTREntityMordorOrc implements LOTRUnitTradeable
{
	public LOTREntityMordorOrcSlaver(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		isWeakOrc = false;
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(Items.lead));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsOrc));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsOrc));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyOrc));
		setCurrentItemOrArmor(4, null);
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.MORDOR_ORC_SLAVER_BONUS;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.MORDOR_ORC_SLAVER;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= LOTRAlignmentValues.MORDOR_ORC_SLAVER_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {}
	
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
				return "orcSlaver_friendly";
			}
			else
			{
				return "orcSlaver_neutral";
			}
		}
		else
		{
			return "orc_hostile";
		}
	}
}
