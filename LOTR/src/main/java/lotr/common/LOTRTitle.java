package lotr.common;

import static lotr.common.LOTRTitle.TitleType.*;

import java.util.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class LOTRTitle implements Comparable<LOTRTitle>
{
	public static enum TitleType
	{
		STARTER,
		PLAYER_EXCLUSIVE,
		ALIGNMENT,
		ACHIEVEMENT;
	}
	
	public static List<LOTRTitle> allTitles = new ArrayList();
	
	public static LOTRTitle adventurer;
	public static LOTRTitle rogue;
	public static LOTRTitle bartender;
	public static LOTRTitle gaffer;
	public static LOTRTitle scholar;
	public static LOTRTitle minstrel;
	public static LOTRTitle bard;
	public static LOTRTitle artisan;
	public static LOTRTitle warrior;
	public static LOTRTitle ranger;
	public static LOTRTitle scourge;
	
	public static LOTRTitle creator;
	public static LOTRTitle creator2;
	public static LOTRTitle moderator;
	public static LOTRTitle gruk;
	
	public static LOTRTitle HOBBIT_100;
	public static LOTRTitle RANGER_100;
	public static LOTRTitle BLUE_MOUNTAINS_100;
	public static LOTRTitle HIGH_ELF_100;
	public static LOTRTitle GUNDABAD_100;
	public static LOTRTitle ANGMAR_100;
	public static LOTRTitle WOOD_ELF_100;
	public static LOTRTitle DOL_GULDUR_100;
	public static LOTRTitle DURIN_100;
	public static LOTRTitle LOTHLORIEN_100;
	public static LOTRTitle DUNLAND_100;
	public static LOTRTitle URUK_100;
	public static LOTRTitle FANGORN_100;
	public static LOTRTitle ROHAN_100;
	public static LOTRTitle GONDOR_100;
	public static LOTRTitle MORDOR_100;
	public static LOTRTitle NEAR_HARAD_100;
	public static LOTRTitle FAR_HARAD_100;
	public static LOTRTitle HALF_TROLL_100;
	
	public static LOTRTitle trollSlayer;
	public static LOTRTitle alcoholic;
	public static LOTRTitle earnCoins;
	public static LOTRTitle explore50Biomes;
	public static LOTRTitle fourLeafClover;
	public static LOTRTitle enterUtumno;
	
	private static int nextTitleID = 0;
	
	public final int titleID;
	private String name;
	private boolean isHidden = false;
	private TitleType titleType = STARTER;
	
	private UUID[] uuids;
	
	private LOTRFaction alignmentFaction;
	private int alignmentRequired;
	
	private LOTRAchievement titleAchievement;
	
	private LOTRTitle(String s)
	{
		titleID = nextTitleID;
		nextTitleID++;
		name = s;
		allTitles.add(this);
	}
	
	private LOTRTitle setPlayerExclusive(UUID... players)
	{
		titleType = PLAYER_EXCLUSIVE;
		uuids = players;
		isHidden = true;
		return this;
	}
	
	private LOTRTitle setRequiresAlignment(LOTRFaction faction, int alignment)
	{
		titleType = ALIGNMENT;
		alignmentFaction = faction;
		alignmentRequired = alignment;
		return this;
	}
	
	private LOTRTitle setRequiresAchievement(LOTRAchievement achievement)
	{
		titleType = ACHIEVEMENT;
		titleAchievement = achievement;
		return this;
	}
	
	public String getTitleName()
	{
		return name;
	}
	
	public static LOTRTitle forName(String name)
	{
		for (LOTRTitle title : allTitles)
		{
			if (title.getTitleName().equals(name))
			{
				return title;
			}
		}
		return null;
	}
	
	public static LOTRTitle forID(int ID)
	{
		for (LOTRTitle title : allTitles)
		{
			if (title.titleID == ID)
			{
				return title;
			}
		}
		return null;
	}
	
	public String getUntranslatedName()
	{
		return "lotr.title." + name;
	}
	
	public String getDisplayName()
	{
		return StatCollector.translateToLocal(getUntranslatedName());
	}
	
	public String getDescription()
	{
		switch (titleType)
		{
			case STARTER:
			{
				return StatCollector.translateToLocal("lotr.titles.unlock.starter");
			}
			
			case PLAYER_EXCLUSIVE:
			{
				return StatCollector.translateToLocal("lotr.titles.unlock.exclusive");
			}
			
			case ALIGNMENT:
			{
				return StatCollector.translateToLocalFormatted("lotr.titles.unlock.alignment", new Object[] {alignmentFaction.factionName(), alignmentRequired});
			}
			
			case ACHIEVEMENT:
			{
				return titleAchievement.getDescription();
			}
		}
		
		return "If you can read this, something has gone hideously wrong";
	}

	@Override
	public int compareTo(LOTRTitle other)
	{
		return getDisplayName().compareTo(other.getDisplayName());
	}
	
	public boolean canPlayerUse(EntityPlayer entityplayer)
	{
		switch (titleType)
		{
			case STARTER:
			{
				return true;
			}
				
			case PLAYER_EXCLUSIVE:
			{
				for (UUID player : uuids)
				{
					if (entityplayer.getUniqueID().equals(player))
					{
						return true;
					}
				}
				return false;
			}
			
			case ALIGNMENT:
			{
				return LOTRLevelData.getData(entityplayer).getAlignment(alignmentFaction) >= alignmentRequired;
			}
			
			case ACHIEVEMENT:
			{
				return LOTRLevelData.getData(entityplayer).hasAchievement(titleAchievement);
			}
		}

		return true;
	}
	
	public boolean canDisplay(EntityPlayer entityplayer)
	{
		return !isHidden || canPlayerUse(entityplayer);
	}
	
	public static void createTitles()
	{
		adventurer = new LOTRTitle("adventurer");
		rogue = new LOTRTitle("rogue");
		bartender = new LOTRTitle("bartender");
		gaffer = new LOTRTitle("gaffer");
		scholar = new LOTRTitle("scholar");
		minstrel = new LOTRTitle("minstrel");
		bard = new LOTRTitle("bard");
		artisan = new LOTRTitle("artisan");
		warrior = new LOTRTitle("warrior");
		ranger = new LOTRTitle("ranger");
		scourge = new LOTRTitle("scourge");
		
		creator = new LOTRTitle("creator").setPlayerExclusive(UUID.fromString("7bc56da6-f133-4e47-8d0f-a2776762bca6"));
		creator2 = new LOTRTitle("creator2").setPlayerExclusive(UUID.fromString("e9587327-156d-4fc4-91a5-f2e7cfaa9d66"));
		moderator = new LOTRTitle("moderator").setPlayerExclusive(LOTRShields.MOD.playersForShield);
		gruk = new LOTRTitle("gruk").setPlayerExclusive(UUID.fromString("6c94c61a-aebb-4b77-9699-4d5236d0e78a"));
		
		HOBBIT_100 = new LOTRTitle("HOBBIT_100").setRequiresAlignment(LOTRFaction.HOBBIT, 100);
		RANGER_100 = new LOTRTitle("RANGER_100").setRequiresAlignment(LOTRFaction.RANGER_NORTH, 100);
		BLUE_MOUNTAINS_100 = new LOTRTitle("BLUE_MOUNTAINS_100").setRequiresAlignment(LOTRFaction.BLUE_MOUNTAINS, 100);
		HIGH_ELF_100 = new LOTRTitle("HIGH_ELF_100").setRequiresAlignment(LOTRFaction.HIGH_ELF, 100);
		GUNDABAD_100 = new LOTRTitle("GUNDABAD_100").setRequiresAlignment(LOTRFaction.GUNDABAD, 100);
		ANGMAR_100 = new LOTRTitle("ANGMAR_100").setRequiresAlignment(LOTRFaction.ANGMAR, 100);
		WOOD_ELF_100 = new LOTRTitle("WOOD_ELF_100").setRequiresAlignment(LOTRFaction.WOOD_ELF, 100);
		DOL_GULDUR_100 = new LOTRTitle("DOL_GULDUR_100").setRequiresAlignment(LOTRFaction.DOL_GULDUR, 100);
		DURIN_100 = new LOTRTitle("DURIN_100").setRequiresAlignment(LOTRFaction.DWARF, 100);
		LOTHLORIEN_100 = new LOTRTitle("LOTHLORIEN_100").setRequiresAlignment(LOTRFaction.GALADHRIM, 100);
		DUNLAND_100 = new LOTRTitle("DUNLAND_100").setRequiresAlignment(LOTRFaction.DUNLAND, 100);
		URUK_100 = new LOTRTitle("URUK_100").setRequiresAlignment(LOTRFaction.URUK_HAI, 100);
		FANGORN_100 = new LOTRTitle("FANGORN_100").setRequiresAlignment(LOTRFaction.FANGORN, 100);
		ROHAN_100 = new LOTRTitle("ROHAN_100").setRequiresAlignment(LOTRFaction.ROHAN, 100);
		GONDOR_100 = new LOTRTitle("GONDOR_100").setRequiresAlignment(LOTRFaction.GONDOR, 100);
		MORDOR_100 = new LOTRTitle("MORDOR_100").setRequiresAlignment(LOTRFaction.MORDOR, 100);
		NEAR_HARAD_100 = new LOTRTitle("NEAR_HARAD_100").setRequiresAlignment(LOTRFaction.NEAR_HARAD, 100);
		FAR_HARAD_100 = new LOTRTitle("FAR_HARAD_100").setRequiresAlignment(LOTRFaction.FAR_HARAD, 100);
		HALF_TROLL_100 = new LOTRTitle("HALF_TROLL_100").setRequiresAlignment(LOTRFaction.HALF_TROLL, 100);
		
		trollSlayer = new LOTRTitle("trollSlayer").setRequiresAchievement(LOTRAchievement.killMountainTrollChieftain);
		alcoholic = new LOTRTitle("alcoholic").setRequiresAchievement(LOTRAchievement.gainHighAlcoholTolerance);
		earnCoins = new LOTRTitle("earnCoins").setRequiresAchievement(LOTRAchievement.earnManyCoins);
		explore50Biomes = new LOTRTitle("explore50Biomes").setRequiresAchievement(LOTRAchievement.travel50);
		fourLeafClover = new LOTRTitle("fourLeafClover").setRequiresAchievement(LOTRAchievement.findFourLeafClover);
		enterUtumno = new LOTRTitle("enterUtumno").setRequiresAchievement(LOTRAchievement.enterUtumnoIce);
	}
	
	public static class PlayerTitle
	{
		private final LOTRTitle theTitle;
		private final EnumChatFormatting theColor;
		
		public PlayerTitle(LOTRTitle title)
		{
			this(title, null);
		}
		
		public PlayerTitle(LOTRTitle title, EnumChatFormatting color)
		{
			theTitle = title;
			if (color == null || !color.isColor())
			{
				color = EnumChatFormatting.WHITE;
			}
			theColor = color;
		}
		
		public LOTRTitle getTitle()
		{
			return theTitle;
		}
		
		public EnumChatFormatting getColor()
		{
			return theColor;
		}
		
		public static EnumChatFormatting colorForID(int ID)
		{
			for (EnumChatFormatting color : EnumChatFormatting.values())
			{
				if (color.getFormattingCode() == ID)
				{
					return color;
				}
			}
			return null;
		}
	}
}
