package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGaladhrimLord extends LOTREntityGaladhrimWarrior implements LOTRUnitTradeable
{
	public LOTREntityGaladhrimLord(World world)
	{
		super(world);
		addTargetTasks(false);
		npcCape = LOTRCapes.GALADHRIM;
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsElven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsElven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyElven));
		setCurrentItemOrArmor(4, null);
		return data;
    }
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.GALADHRIM_LORD;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.ELF_LORD;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.ELF_LORD_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeElfLord);
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
				return "galadhrimLord_friendly";
			}
			else
			{
				return "galadhrimLord_neutral";
			}
		}
		else
		{
			return "galadhrimWarrior_hostile";
		}
	}
}
