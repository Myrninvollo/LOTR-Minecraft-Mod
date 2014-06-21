package lotr.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class LOTRAlignmentValues
{
	public static Bonus MARRIAGE_BONUS = new Bonus(5, "lotr.alignment.marriage");
	
	public static Bonus FANGORN_TREE_PENALTY = new Bonus(-1, "lotr.alignment.cutFangornTree");
	
	public static Bonus ROHAN_HORSE_PENALTY = new Bonus(-1, "lotr.alignment.killRohanHorse");
	
	public static int HOBBIT_BONUS = 1;
	public static int HOBBIT_SHIRRIFF_BONUS = 2;
	public static int HOBBIT_SHIRRIFF_CHIEF_BONUS = 5;
	public static int HOBBIT_BARTENDER_BONUS = 2;
	public static int HOBBIT_ORCHARDER_BONUS = 2;
	public static int HOBBIT_FARMER_BONUS = 2;
	
	public static int RANGER_NORTH_BONUS = 2;
	public static int RANGER_NORTH_CAPTAIN_BONUS = 5;
	
	public static int BLUE_DWARF_BONUS = 1;
	public static int BLUE_DWARF_WARRIOR_BONUS = 2;
	public static int BLUE_DWARF_COMMANDER_BONUS = 5;
	public static int BLUE_DWARF_MINER_BONUS = 2;
	public static int BLUE_DWARF_MERCHANT_BONUS = 5;
	
	public static int HIGH_ELF_BONUS = 1;
	public static int HIGH_ELF_WARRIOR_BONUS = 2;
	public static int HIGH_ELF_LORD_BONUS = 5;
	
	public static int GUNDABAD_ORC_BONUS = 1;
	public static int GUNDABAD_ORC_MERCENARY_CAPTAIN_BONUS = 5;
	public static int GUNDABAD_WARG_BONUS = 1;
	
	public static int ANGMAR_ORC_BONUS = 1;
	public static int ANGMAR_ORC_WARRIOR_BONUS = 2;
	public static int ANGMAR_ORC_MERCENARY_CAPTAIN_BONUS = 5;
	public static int ANGMAR_WARG_BONUS = 1;
	
	public static int TROLL_BONUS = 2;
	public static int MOUNTAIN_TROLL_BONUS = 3;
	public static int MOUNTAIN_TROLL_CHIEFTAIN_BONUS = 50;
	
	public static int WOOD_ELF_BONUS = 1;
	public static int WOOD_ELF_WARRIOR_BONUS = 2;
	public static int WOOD_ELF_CAPTAIN_BONUS = 5;
	
	public static int MIRKWOOD_SPIDER_BONUS = 1;
	
	public static int DWARF_BONUS = 1;
	public static int DWARF_WARRIOR_BONUS = 2;
	public static int DWARF_COMMANDER_BONUS = 5;
	public static int DWARF_MINER_BONUS = 2;
	
	public static int GALADHRIM_BONUS = 1;
	public static int GALADHRIM_WARRIOR_BONUS = 2;
	public static int GALADHRIM_LORD_BONUS = 5;
	public static int GALADHRIM_TRADER_BONUS = 2;
	
	public static int DUNLENDING_BONUS = 1;
	public static int DUNLENDING_WARRIOR_BONUS = 2;
	public static int DUNLENDING_WARLORD_BONUS = 5;
	public static int DUNLENDING_BARTENDER_BONUS = 2;
	
	public static int ENT_BONUS = 3;
	public static int HUORN_BONUS = 2;
	
	public static int ROHIRRIM_BONUS = 2;
	public static int ROHIRRIM_MARSHAL_BONUS = 5;
	public static int ROHAN_BLACKSMITH_BONUS = 2;
	public static int ROHAN_MEADHOST_BONUS = 2;
	
	public static int URUK_HAI_BONUS = 1;
	public static int URUK_HAI_MERCENARY_CAPTAIN_BONUS = 5;
	public static int URUK_HAI_TRADER_BONUS = 2;
	public static int URUK_WARG_BONUS = 1;
	
	public static int GONDOR_SOLDIER_BONUS = 2;
	public static int GONDOR_CAPTAIN_BONUS = 5;
	public static int GONDOR_BLACKSMITH_BONUS = 2;
	public static int RANGER_ITHILIEN_BONUS = 2;
	
	public static int MORDOR_ORC_BONUS = 1;
	public static int MORDOR_ORC_MERCENARY_CAPTAIN_BONUS = 5;
	public static int MORDOR_ORC_TRADER_BONUS = 2;
	public static int MORDOR_ORC_SLAVER_BONUS = 5;
	public static int MORDOR_ORC_SPIDER_KEEPER_BONUS = 5;
	public static int MORDOR_WARG_BONUS = 1;
	public static int OLOG_HAI_BONUS = 3;
	public static int MORDOR_SPIDER_BONUS = 1;
	
	public static int NEAR_HARADRIM_BONUS = 1;
	public static int NEAR_HARADRIM_WARRIOR_BONUS = 2;
	public static int NEAR_HARADRIM_WARLORD_BONUS = 5;
	public static int NEAR_HARADRIM_TRADER_BONUS = 5;
	
	public static int MAX_ALIGNMENT = 10000;
	
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
	public static int DUNLENDING_BARTENDER_TRADE = 1;
	
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
	
	public static class Bonus
	{
		public int bonus;
		public String name;
		public boolean needsTranslation = true;
		public boolean isKill = false;
		
		public Bonus(int i, String s)
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
}
