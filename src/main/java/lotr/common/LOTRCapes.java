package lotr.common;

import static lotr.common.LOTRCapes.CapeType.*;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public enum LOTRCapes
{
	ALIGNMENT_BLUE_MOUNTAINS(LOTRFaction.BLUE_MOUNTAINS,
		"Cape of the Blue Mountains",
		"Awarded for reaching +1000 alignment",
		"with the Dwarves of the Blue Mountains"
		),
			
	ALIGNMENT_HIGH_ELF(LOTRFaction.HIGH_ELF,
		"Cape of the High Elves",
		"Awarded for reaching +1000 alignment",
		"with the High Elves"
		),
		
	ALIGNMENT_ANGMAR(LOTRFaction.ANGMAR,
		"Cape of Angmar",
		"Awarded for reaching +1000 alignment",
		"with Angmar"
		),
		
	ALIGNMENT_WOOD_ELF(LOTRFaction.WOOD_ELF,
		"Cape of the Woodland Realm",
		"Awarded for reaching +1000 alignment",
		"with the Wood-elves"
		),
	
	ALIGNMENT_DWARF(LOTRFaction.DWARF,
		"Cape of Durin's Folk",
		"Awarded for reaching +1000 alignment",
		"with Durin's Folk"
		),
		
	ALIGNMENT_GALADHRIM(LOTRFaction.GALADHRIM,
		"Cape of Lothlórien",
		"Awarded for reaching +1000 alignment",
		"with the Galadhrim"
		),
		
	ALIGNMENT_DUNLAND(LOTRFaction.DUNLAND,
		"Cape of Dunland",
		"Awarded for reaching +1000 alignment",
		"with the Dunlendings"
		),
		
	ALIGNMENT_ROHAN(LOTRFaction.ROHAN,
		"Cape of Rohan",
		"Awarded for reaching +1000 alignment",
		"with the Rohirrim"
		),
		
	ALIGNMENT_URUK_HAI(LOTRFaction.URUK_HAI,
		"Cape of the Uruk-hai",
		"Awarded for reaching +1000 alignment",
		"with the Uruk-hai"
		),
		
	ALIGNMENT_GONDOR(LOTRFaction.GONDOR,
		"Cape of Gondor",
		"Awarded for reaching +1000 alignment",
		"with Gondor"
		),
		
	ALIGNMENT_MORDOR(LOTRFaction.MORDOR,
		"Cape of Mordor",
		"Awarded for reaching +1000 alignment",
		"with Mordor"
		),
		
	ALIGNMENT_NEAR_HARAD(LOTRFaction.NEAR_HARAD,
		"Cape of Near Harad",
		"Awarded for reaching +1000 alignment",
		"with the Near Haradrim"
		),
	
	ACHIEVEMENT_BRONZE(
		"Bronze Adventurer's Cape",
		"Available upon earning 25",
		"achievements in Middle-earth"
		),
			
	ACHIEVEMENT_SILVER(
		"Silver Adventurer's Cape",
		"Available upon earning 50",
		"achievements in Middle-earth"
		),
		
	ACHIEVEMENT_GOLD(
		"Gold Adventurer's Cape",
		"Available upon earning 100",
		"achievements in Middle-earth"
		),
		
	ACHIEVEMENT_MITHRIL(
		"Mithril Adventurer's Cape",
		"Available upon earning 200",
		"achievements in Middle-earth"
		),
	
	DEFEAT_MTC(
		"Trollslayer's Cape",
		"Earned by defeating the",
		"Mountain Troll Chieftain"
		),
	
	MELON(
		"Melonseeker's Cape",
		"Awarded to those who find fruit",
		"in the unlikeliest of places"
		),
		
	ELVEN_CONTEST(new String[] {"7a19ce50-d5c8-4e16-b8f9-932d27ec3251"},
		"Elven Contest Cape",
		"Awarded as first prize",
		"in the Elven building contest"
		),
	
	EVIL_CONTEST(new String[] {"0c1eb454-df57-4b5c-91bf-16d8f15cae74", "2418e4fa-2483-4bd9-bf58-1e09c586959e"},
		"Evil Contest Cape",
		"Awarded as first and second prize",
		"in the Evil building contest"
		),
	
	SHIRE_CONTEST(new String[] {"a166423a-a1e3-45b1-a4b8-dea69f2bd64e", "3967d432-37ec-450d-ad37-9eec6bac9707", "bab181b6-7b76-49fe-baa2-b3ca4a48f486"},
		"Shire Contest Cape",
		"Awarded as first and second prize",
		"in the Shire building contest"
		),
		
	GONDOR_CONTEST(new String[] {"ccfdb788-46f6-4142-8d41-1a89ac745db6", "7c9bfb4c-6b65-4d2f-9cbb-7e037bbad14f", "bab181b6-7b76-49fe-baa2-b3ca4a48f486", "753cdbe5-414c-4f60-829a-f65fc38cc539", "3967d432-37ec-450d-ad37-9eec6bac9707"},
		"Gondor Contest Cape",
		"Awarded as first and second prize",
		"in the Gondor building contest"
		),
		
	STRUCTURE_CONTEST(new String[] {"82f1c77b-07b8-4334-9edf-f29d8fc74bf8", "69f45bcd-4955-42a8-a740-dc0e16dc8565", "37f2bfb6-a613-43dd-8c1b-ffa4cbbbd172", "8bc4c772-f305-4545-a44d-a4f1b9eb2b98", "a166423a-a1e3-45b1-a4b8-dea69f2bd64e", "3967d432-37ec-450d-ad37-9eec6bac9707", "7e869d33-df0f-4433-b235-a62205c5f986"},
		"Architect's Cape",
		"Awarded for having your build",
		"implemented into the mod",
		"in the Random Structures building contest"
		),
		
	MOD(new String[] {"7bc56da6-f133-4e47-8d0f-a2776762bca6", "e9587327-156d-4fc4-91a5-f2e7cfaa9d66", "d054f0bf-a9cb-41d1-8496-709f97faa475", "8bc4c772-f305-4545-a44d-a4f1b9eb2b98", "3967d432-37ec-450d-ad37-9eec6bac9707", "12e96c35-6964-4993-8876-c04ac9f2fb8c", "6458975e-0160-4b45-b364-866b4922cc29", "d0b25c60-65ec-4d75-b0ac-07b3fa9dd6a6", "7a55441d-02d9-401c-b259-495fba1c3148", "963296be-a555-4151-aa45-b7df8598faba", "c0eb3931-701b-4bb3-ac12-c03017e09b8d", "0a283d3e-7fd7-4469-a219-4189c0e012b5", "972ecfe5-4164-44ea-944f-9d7a4fb80145", "bab181b6-7b76-49fe-baa2-b3ca4a48f486", "a166423a-a1e3-45b1-a4b8-dea69f2bd64e", "461c1adb-bbd1-4513-b02f-946b6388b496", "757223ca-bd8e-4cbf-bd69-397f244263af", "b03bd343-8fe8-42ca-a56f-ee3a5b17150f"},
		"Modder's Cape",
		"Worn by the creator of the mod",
		"and the administration team"
		),
	
	ELECTRICIAN(new String[] {"60241a10-eeb0-4cc5-831c-d5f7123d46f0"},
		"Electrician's Cape",
		"Worn by Mr. Gibbs"
		),
		
	LOREMASTER_2013(new String[] {"7bc56da6-f133-4e47-8d0f-a2776762bca6", "a166423a-a1e3-45b1-a4b8-dea69f2bd64e", "a041a9a7-286d-470f-8826-c9ab0b3166dd", "1f9b1c1d-d4fa-49a2-b6a7-fc58bb62d19b", "e3db1a07-846a-4843-98b2-64f4f9331b4a"},
		"Loremaster's Cape 2013",
		"Awarded to the top five contributors",
		"on the mod's wiki during 2013"
		);
	
	public CapeType capeType;
	public int capeID;
	public UUID[] playersForCape;
	private LOTRFaction alignmentFaction;
	public String[] capeDescription;
	public ResourceLocation capeTexture;
	
	private LOTRCapes(LOTRFaction faction, String... description)
	{
		this(ALIGNMENT, new String[0], description);
		alignmentFaction = faction;
	}
	
	private LOTRCapes(String... description)
	{
		this(ACHIEVABLE, new String[0], description);
	}
	
	private LOTRCapes(String[] players, String... description)
	{
		this(EXCLUSIVE, players, description);
	}
	
	private LOTRCapes(CapeType type, String[] players, String... description)
	{
		capeType = type;
		capeID = capeType.list.size();
		capeType.list.add(this);
		capeDescription = description;
		capeTexture = new ResourceLocation("lotr:cape/" + name().toLowerCase() + ".png");
		
		playersForCape = new UUID[players.length];
		for (int i = 0; i < playersForCape.length; i++)
		{
			String s = players[i];
			playersForCape[i] = UUID.fromString(s);
		}
	}
	
	public static void forceClassLoad() {}

	public boolean canPlayerWearCape(EntityPlayer entityplayer)
	{
		if (capeType == ALIGNMENT)
		{
			return LOTRLevelData.getAlignment(entityplayer, alignmentFaction) >= 1000;
		}
		if (this == ACHIEVEMENT_BRONZE)
		{
			return LOTRLevelData.getPlayerEarnedAchievements(entityplayer).size() >= 25;
		}
		else if (this == ACHIEVEMENT_SILVER)
		{
			return LOTRLevelData.getPlayerEarnedAchievements(entityplayer).size() >= 50;
		}
		else if (this == ACHIEVEMENT_GOLD)
		{
			return LOTRLevelData.getPlayerEarnedAchievements(entityplayer).size() >= 100;
		}
		else if (this == ACHIEVEMENT_MITHRIL)
		{
			return LOTRLevelData.getPlayerEarnedAchievements(entityplayer).size() >= 200;
		}
		else if (this == DEFEAT_MTC)
		{
			return LOTRLevelData.hasAchievement(entityplayer, LOTRAchievement.killMountainTrollChieftain);
		}
		else if (this == MELON)
		{
			return LOTRLevelData.getPlayerFoundMelon(entityplayer);
		}
		else if (capeType == EXCLUSIVE)
		{
			for (UUID uuid : playersForCape)
			{
				if (uuid.equals(entityplayer.getUniqueID()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static LOTRCapes capeForName(String capeName)
	{
		for (LOTRCapes cape : LOTRCapes.values())
		{
			if (cape.name().equals(capeName))
			{
				return cape;
			}
		}
		return null;
	}
	
	public enum CapeType
	{
		ALIGNMENT("Alignment"),
		ACHIEVABLE("Achievable"),
		EXCLUSIVE("Exclusive");
		
		public String displayName;
		public ArrayList list = new ArrayList();
		
		private CapeType(String s)
		{
			displayName = s + " Capes";
		}
	}
}
