package lotr.common.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class LOTREntityRegistry
{
	public static Map registeredNPCs = new HashMap();
	
	public static class RegistryInfo
	{
		public LOTRFaction alignmentFaction;
		public boolean shouldTargetEnemies;
		public LOTRAlignmentValues.Bonus alignmentBonus;
		
		public RegistryInfo(String entityName, LOTRFaction side, boolean flag, int bonus)
		{
			alignmentFaction = side;
			shouldTargetEnemies = flag;
			alignmentBonus = new LOTRAlignmentValues.Bonus(bonus, "entity." + entityName + ".name");
			alignmentBonus.needsTranslation = true;
		}
	}
	
	public static void loadRegisteredNPCs(FMLPreInitializationEvent event)
	{
		StringBuilder stringbuilder = new StringBuilder();
		for (LOTRFaction faction : LOTRFaction.values())
		{
			if (!faction.allowEntityRegistry)
			{
				continue;
			}
			
			if (faction.ordinal() > 0)
			{
				stringbuilder.append(", ");
			}
			stringbuilder.append(faction.name());
		}
		String allFactions = stringbuilder.toString();
		
		try
		{
			File file = event.getModConfigurationDirectory();
			File config = new File(file, "LOTR_EntityRegistry.txt");
			if (!config.exists())
			{
				if (config.createNewFile())
				{
					PrintWriter printwriter = new PrintWriter(new FileWriter(config));
					printwriter.println("#Lines starting with '#' will be ignored");
					printwriter.println("#");
					printwriter.println("#Use this file to register entities with the LOTR alignment system.");
					printwriter.println("#");
					printwriter.println("#An example format for registering an entity is as follows: (do not use spaces)");
					printwriter.println("#name=LOTR.MordorOrc,faction=MORDOR,targetEnemies=true,bonus=1");
					printwriter.println("#");
					printwriter.println("#'name' is the entity name, prefixed with the associated mod ID.");
					printwriter.println("#The mod ID can be found in the Mod List on the main menu - for example, \"LOTR\" for the LOTR mod.");
					printwriter.println("#The entity name is not necessarily the in-game name. It is the name used to register the entity in the code.");
					printwriter.println("#You may be able to discover the entity name in the mod's language file if there is one - otherwise, contact the mod author.");
					printwriter.println("#The mod ID and entity name must be separated by a '.' character.");
					printwriter.println("#Vanilla entities have no mod ID and therefore no prefix.");
					printwriter.println("#");
					printwriter.println("#'faction' can be " + allFactions);
					printwriter.println("#");
					printwriter.println("#'targetEnemies' can be true or false.");
					printwriter.println("#If true, the entity will be equipped with AI modules to target its enemies.");
					printwriter.println("#Actual combat behaviour may or may not be present, depending on whether the entity is designed with combat AI modules.");
					printwriter.println("#");
					printwriter.println("#'bonus' is the alignment bonus awarded to a player who kills the entity.");
					printwriter.println("#It can be positive, negative, or zero, in which case no bonus will be awarded.");
					printwriter.println("#");
					printwriter.close();
				}
			}
			else
			{
				BufferedReader bufferedreader = new BufferedReader(new FileReader(config));
				String s = "";

				while ((s = bufferedreader.readLine()) != null)
				{
					String line = s;
					
					if (s.startsWith("#"))
					{
						continue;
					}
					
					String name;
					LOTRFaction faction = null;
					boolean targetEnemies;
					int bonus;
					
					if (!s.startsWith("name="))
					{
						continue;
					}
					
					s = s.substring("name=".length());
					if (s.startsWith("LOTR."))
					{
						continue;
					}

					int i = s.indexOf(",side=");
					if (i < 0)
					{
						continue;
					}
					int j = s.indexOf(",targetEnemies=");
					if (j < 0)
					{
						continue;
					}
					int k = s.indexOf(",bonus=");
					if (k < 0)
					{
						continue;
					}
					
					name = s.substring(0, i);
					if (name.length() == 0)
					{
						continue;
					}
					
					String factionString = s.substring(i + "faction=".length() + 1, j);
					for (LOTRFaction f : LOTRFaction.values())
					{
						if (f.name().equals(factionString))
						{
							faction = f;
							break;
						}
					}
					
					if (faction == null)
					{
						continue;
					}
					
					String targetEnemiesString = s.substring(j + "targetEnemies=".length() + 1, k);
					if (targetEnemiesString.equals("true"))
					{
						targetEnemies = true;
					}
					else if (targetEnemiesString.equals("false"))
					{
						targetEnemies = false;
					}
					else
					{
						continue;
					}
					
					String bonusString = s.substring(k + "bonus=".length() + 1);
					bonus = Integer.parseInt(bonusString);
					
					registeredNPCs.put(name, new RegistryInfo(name, faction, targetEnemies, bonus));
					System.out.println("Successfully registered entity " + name + " with the LOTR alignment system as " + line);
				}
				
				bufferedreader.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
