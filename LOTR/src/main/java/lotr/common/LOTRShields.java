package lotr.common;

import static lotr.common.LOTRShields.ShieldType.*;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public enum LOTRShields
{
	ALIGNMENT_RANGER(LOTRFaction.RANGER_NORTH),
	ALIGNMENT_BLUE_MOUNTAINS(LOTRFaction.BLUE_MOUNTAINS),	
	ALIGNMENT_HIGH_ELF(LOTRFaction.HIGH_ELF),
	ALIGNMENT_ANGMAR(LOTRFaction.ANGMAR),
	ALIGNMENT_WOOD_ELF(LOTRFaction.WOOD_ELF),
	ALIGNMENT_DOL_GULDUR(LOTRFaction.DOL_GULDUR),
	ALIGNMENT_DWARF(LOTRFaction.DWARF),
	ALIGNMENT_GALADHRIM(LOTRFaction.GALADHRIM),
	ALIGNMENT_DUNLAND(LOTRFaction.DUNLAND),
	ALIGNMENT_URUK_HAI(LOTRFaction.URUK_HAI),
	ALIGNMENT_ROHAN(LOTRFaction.ROHAN),
	ALIGNMENT_GONDOR(LOTRFaction.GONDOR),
	ALIGNMENT_MORDOR(LOTRFaction.MORDOR),
	ALIGNMENT_NEAR_HARAD(LOTRFaction.NEAR_HARAD),
	
	ACHIEVEMENT_BRONZE(),
	ACHIEVEMENT_SILVER(),
	ACHIEVEMENT_GOLD(),
	ACHIEVEMENT_MITHRIL(),
	DEFEAT_MTC(),
		
	ELVEN_CONTEST(new String[] {"7a19ce50-d5c8-4e16-b8f9-932d27ec3251"}),
	EVIL_CONTEST(new String[] {"0c1eb454-df57-4b5c-91bf-16d8f15cae74", "2418e4fa-2483-4bd9-bf58-1e09c586959e"}),
	SHIRE_CONTEST(new String[] {"a166423a-a1e3-45b1-a4b8-dea69f2bd64e", "3967d432-37ec-450d-ad37-9eec6bac9707", "bab181b6-7b76-49fe-baa2-b3ca4a48f486"}),
	GONDOR_CONTEST(new String[] {"ccfdb788-46f6-4142-8d41-1a89ac745db6", "7c9bfb4c-6b65-4d2f-9cbb-7e037bbad14f", "bab181b6-7b76-49fe-baa2-b3ca4a48f486", "753cdbe5-414c-4f60-829a-f65fc38cc539", "3967d432-37ec-450d-ad37-9eec6bac9707"}),
	STRUCTURE_CONTEST(new String[] {"82f1c77b-07b8-4334-9edf-f29d8fc74bf8", "69f45bcd-4955-42a8-a740-dc0e16dc8565", "37f2bfb6-a613-43dd-8c1b-ffa4cbbbd172", "8bc4c772-f305-4545-a44d-a4f1b9eb2b98", "a166423a-a1e3-45b1-a4b8-dea69f2bd64e", "3967d432-37ec-450d-ad37-9eec6bac9707", "7e869d33-df0f-4433-b235-a62205c5f986"}),
	MOD(new String[] {"7bc56da6-f133-4e47-8d0f-a2776762bca6", "e9587327-156d-4fc4-91a5-f2e7cfaa9d66", "d054f0bf-a9cb-41d1-8496-709f97faa475", "8bc4c772-f305-4545-a44d-a4f1b9eb2b98", "3967d432-37ec-450d-ad37-9eec6bac9707", "12e96c35-6964-4993-8876-c04ac9f2fb8c", "6458975e-0160-4b45-b364-866b4922cc29", "d0b25c60-65ec-4d75-b0ac-07b3fa9dd6a6", "7a55441d-02d9-401c-b259-495fba1c3148", "963296be-a555-4151-aa45-b7df8598faba", "c0eb3931-701b-4bb3-ac12-c03017e09b8d", "0a283d3e-7fd7-4469-a219-4189c0e012b5", "972ecfe5-4164-44ea-944f-9d7a4fb80145", "bab181b6-7b76-49fe-baa2-b3ca4a48f486", "a166423a-a1e3-45b1-a4b8-dea69f2bd64e", "461c1adb-bbd1-4513-b02f-946b6388b496", "757223ca-bd8e-4cbf-bd69-397f244263af", "b03bd343-8fe8-42ca-a56f-ee3a5b17150f", "7646da8a-77fa-4697-ae05-ac75e0558106", "7de47d3a-3f3f-40a9-baa9-4214155eaef7", "d4ff096b-a38c-47c6-8cec-2bf336affb6d"}),
	ELECTRICIAN(new String[] {"60241a10-eeb0-4cc5-831c-d5f7123d46f0"}),
	OGRE(new String[] {"90dd2523-cefb-48b9-be2c-36759289724a"}),
	LOREMASTER_2013(new String[] {"7bc56da6-f133-4e47-8d0f-a2776762bca6", "a166423a-a1e3-45b1-a4b8-dea69f2bd64e", "a041a9a7-286d-470f-8826-c9ab0b3166dd", "1f9b1c1d-d4fa-49a2-b6a7-fc58bb62d19b", "e3db1a07-846a-4843-98b2-64f4f9331b4a"});
	
	public ShieldType shieldType;
	public int shieldID;
	public UUID[] playersForShield;
	private LOTRFaction alignmentFaction;
	public ResourceLocation shieldTexture;
	
	private LOTRShields(LOTRFaction faction)
	{
		this(ALIGNMENT, new String[0]);
		alignmentFaction = faction;
	}
	
	private LOTRShields()
	{
		this(ACHIEVABLE, new String[0]);
	}
	
	private LOTRShields(String[] players)
	{
		this(EXCLUSIVE, players);
	}
	
	private LOTRShields(ShieldType type, String[] players)
	{
		shieldType = type;
		shieldID = shieldType.list.size();
		shieldType.list.add(this);
		shieldTexture = new ResourceLocation("lotr:shield/" + name().toLowerCase() + ".png");
		
		playersForShield = new UUID[players.length];
		for (int i = 0; i < playersForShield.length; i++)
		{
			String s = players[i];
			playersForShield[i] = UUID.fromString(s);
		}
	}
	
	public String getShieldName()
	{
		return StatCollector.translateToLocal("lotr.shields." + name() + ".name");
	}
	
	public String getShieldDesc()
	{
		return StatCollector.translateToLocal("lotr.shields." + name() + ".desc");
	}

	public boolean canPlayerWear(EntityPlayer entityplayer)
	{
		if (shieldType == ALIGNMENT)
		{
			return LOTRLevelData.getData(entityplayer).getAlignment(alignmentFaction) >= 1000;
		}
		if (this == ACHIEVEMENT_BRONZE)
		{
			return LOTRLevelData.getData(entityplayer).getEarnedAchievements().size() >= 25;
		}
		else if (this == ACHIEVEMENT_SILVER)
		{
			return LOTRLevelData.getData(entityplayer).getEarnedAchievements().size() >= 50;
		}
		else if (this == ACHIEVEMENT_GOLD)
		{
			return LOTRLevelData.getData(entityplayer).getEarnedAchievements().size() >= 100;
		}
		else if (this == ACHIEVEMENT_MITHRIL)
		{
			return LOTRLevelData.getData(entityplayer).getEarnedAchievements().size() >= 200;
		}
		else if (this == DEFEAT_MTC)
		{
			return LOTRLevelData.getData(entityplayer).hasAchievement(LOTRAchievement.killMountainTrollChieftain);
		}
		else if (shieldType == EXCLUSIVE)
		{
			for (UUID uuid : playersForShield)
			{
				if (uuid.equals(entityplayer.getUniqueID()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static void forceClassLoad() {}
	
	public static LOTRShields shieldForName(String shieldName)
	{
		for (LOTRShields shield : LOTRShields.values())
		{
			if (shield.name().equals(shieldName))
			{
				return shield;
			}
		}
		return null;
	}
	
	public enum ShieldType
	{
		ALIGNMENT,
		ACHIEVABLE,
		EXCLUSIVE;
		
		public ArrayList list = new ArrayList();
		
		public String getDisplayName()
		{
			return StatCollector.translateToLocal("lotr.shields.category." + name());
		}
	}
}
