package lotr.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class LOTRAlignmentValues
{
	public static int MAX_ALIGNMENT = 10000;
	
	public static AlignmentBonus MARRIAGE_BONUS = new AlignmentBonus(5, "lotr.alignment.marriage");
	public static AlignmentBonus FANGORN_TREE_PENALTY = new AlignmentBonus(-1, "lotr.alignment.cutFangornTree");
	public static AlignmentBonus ROHAN_HORSE_PENALTY = new AlignmentBonus(-1, "lotr.alignment.killRohanHorse");
	
	public static class Bonuses
	{
		public static int HOBBIT = 1;
		public static int HOBBIT_SHIRRIFF = 2;
		public static int HOBBIT_SHIRRIFF_CHIEF = 5;
		public static int HOBBIT_BARTENDER = 2;
		public static int HOBBIT_ORCHARDER = 2;
		public static int HOBBIT_FARMER = 2;
		
		public static int RANGER_NORTH = 2;
		public static int RANGER_NORTH_CAPTAIN = 5;
		
		public static int BLUE_DWARF = 1;
		public static int BLUE_DWARF_WARRIOR = 2;
		public static int BLUE_DWARF_COMMANDER = 5;
		public static int BLUE_DWARF_MINER = 2;
		public static int BLUE_DWARF_MERCHANT = 5;
		
		public static int HIGH_ELF = 1;
		public static int HIGH_ELF_WARRIOR = 2;
		public static int HIGH_ELF_LORD = 5;
		
		public static int GUNDABAD_ORC = 1;
		public static int GUNDABAD_ORC_MERCENARY_CAPTAIN = 5;
		public static int GUNDABAD_WARG = 1;
		
		public static int ANGMAR_ORC = 1;
		public static int ANGMAR_ORC_WARRIOR = 2;
		public static int ANGMAR_ORC_MERCENARY_CAPTAIN = 5;
		public static int ANGMAR_WARG = 1;
		
		public static int TROLL = 2;
		public static int MOUNTAIN_TROLL = 3;
		public static int MOUNTAIN_TROLL_CHIEFTAIN = 50;
		
		public static int WOOD_ELF = 1;
		public static int WOOD_ELF_WARRIOR = 2;
		public static int WOOD_ELF_CAPTAIN = 5;
		
		public static int MIRKWOOD_SPIDER = 1;
		
		public static int DWARF = 1;
		public static int DWARF_WARRIOR = 2;
		public static int DWARF_COMMANDER = 5;
		public static int DWARF_MINER = 2;
		
		public static int GALADHRIM = 1;
		public static int GALADHRIM_WARRIOR = 2;
		public static int GALADHRIM_LORD = 5;
		public static int GALADHRIM_TRADER = 2;
		
		public static int DUNLENDING = 1;
		public static int DUNLENDING_WARRIOR = 2;
		public static int DUNLENDING_WARLORD = 5;
		public static int DUNLENDING_BARTENDER = 2;
		
		public static int ENT = 3;
		public static int HUORN = 2;
		
		public static int ROHIRRIM = 2;
		public static int ROHIRRIM_MARSHAL = 5;
		public static int ROHAN_BLACKSMITH = 2;
		public static int ROHAN_MEADHOST = 2;
		
		public static int URUK_HAI = 1;
		public static int URUK_HAI_MERCENARY_CAPTAIN = 5;
		public static int URUK_HAI_TRADER = 2;
		public static int URUK_WARG = 1;
		
		public static int GONDOR_SOLDIER = 2;
		public static int GONDOR_CAPTAIN = 5;
		public static int GONDOR_BLACKSMITH = 2;
		public static int RANGER_ITHILIEN = 2;
		
		public static int MORDOR_ORC = 1;
		public static int MORDOR_ORC_MERCENARY_CAPTAIN = 5;
		public static int MORDOR_ORC_TRADER = 2;
		public static int MORDOR_ORC_SLAVER = 5;
		public static int MORDOR_ORC_SPIDER_KEEPER = 5;
		public static int MORDOR_WARG = 1;
		public static int OLOG_HAI = 3;
		public static int MORDOR_SPIDER = 1;
		
		public static int NEAR_HARADRIM = 1;
		public static int NEAR_HARADRIM_WARRIOR = 2;
		public static int NEAR_HARADRIM_WARLORD = 5;
		public static int NEAR_HARADRIM_TRADER = 5;
	}
	
	public static class Levels
	{
		public static int USE_TABLE = 1;
		
		public static int USE_PORTAL = 1;
		
		public static int HOBBIT_MARRY = 100;
		public static int HOBBIT_CHILD_FOLLOW = 200;
		public static int HOBBIT_SHIRRIFF_CHIEF_TRADE = 50;
		public static int HOBBIT_FLEE = -100;
		
		public static int RANGER_NORTH_CAPTAIN_TRADE = 300;
		
		public static int BLUE_DWARF_MINER_TRADE = 100;
		public static int BLUE_DWARF_COMMANDER_TRADE = 200;
		public static int BLUE_DWARF_MERCHANT_TRADE = 0;
		
		public static int HIGH_ELF_LORD_TRADE = 200;
		
		public static int TROLL_TRUST = 100;
		
		public static int WOOD_ELF_TRUST = 50;
		public static int WOOD_ELF_CAPTAIN_TRADE = 200;
		
		public static int DWARF_MINER_TRADE = 100;
		public static int DWARF_COMMANDER_TRADE = 200;
		public static int DWARF_MARRY = 200;
		
		public static int ELVEN_TRADER_TRADE = 75;
		public static int ELF_LORD_TRADE = 250;
		
		public static int ROHIRRIM_MARSHAL_TRADE = 100;
		public static int ROHAN_BLACKSMITH_TRADE = 50;
		
		public static int DUNLENDING_WARLORD_TRADE = 100;
		
		public static int SPAWN_HUORN = 500;
		
		public static int GONDOR_BLACKSMITH_TRADE = 50;
		public static int GONDORIAN_CAPTAIN_TRADE = 100;
		
		public static int ORC_FLEE = -500;
		public static int ORC_FRIENDLY = 100;
		
		public static int MORDOR_TRUST = 100;
		public static int MORDOR_ORC_TRADER_TRADE = 100;
		public static int MORDOR_ORC_MERCENARY_CAPTAIN_TRADE = 150;
		public static int MORDOR_ORC_SLAVER_TRADE = 200;
		public static int MORDOR_ORC_SPIDER_KEEPER_TRADE = 250;
		
		public static int ANGMAR_ORC_MERCENARY_CAPTAIN_TRADE = 150;
		
		public static int GUNDABAD_ORC_MERCENARY_CAPTAIN_TRADE = 100;
		
		public static int URUK_HAI_TRADER_TRADE = 100;
		public static int URUK_HAI_MERCENARY_CAPTAIN_TRADE = 150;
		
		public static int WARG_RIDE = 50;
		
		public static int NEAR_HARADRIM_WARLORD_TRADE = 100;
		public static int NEAR_HARAD_MERCHANT_TRADE = 0;
		public static int NEAR_HARAD_BAZAAR_TRADE = 0;
	}
	
	public static class AlignmentBonus
	{
		public int bonus;
		public String name;
		public boolean needsTranslation = true;
		public boolean isKill = false;
		
		public AlignmentBonus(int i, String s)
		{
			bonus = i;
			name = s;
		}
		
		public static int scalePenalty(int penalty, int alignment)
		{
			if (alignment > 0 && penalty < 0)
			{
				float f = (float)alignment / 50F;
				if (f < 1F)
				{
					f = 1F;
				}
				if (f > 20F)
				{
					f = 20F;
				}
				penalty = Math.round((float)penalty * f);
			}
			
			return penalty;
		}
	}
	
	public static void notifyAlignmentNotHighEnough(EntityPlayer entityplayer, int alignmentRequired, LOTRFaction faction)
	{
        IChatComponent componentAlignmentRequired = new ChatComponentText("+" + alignmentRequired);
        componentAlignmentRequired.getChatStyle().setColor(EnumChatFormatting.YELLOW);
		entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.insufficientAlignment", new Object[] {componentAlignmentRequired, faction.factionName()}));
	}
	
	public static void notifyAlignmentNotHighEnough(EntityPlayer entityplayer, int alignmentRequired, LOTRFaction faction1, LOTRFaction faction2)
	{
        IChatComponent componentAlignmentRequired = new ChatComponentText("+" + alignmentRequired);
        componentAlignmentRequired.getChatStyle().setColor(EnumChatFormatting.YELLOW);
		entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.insufficientAlignment2", new Object[] {componentAlignmentRequired, faction1.factionName(), faction2.factionName()}));
	}
}
