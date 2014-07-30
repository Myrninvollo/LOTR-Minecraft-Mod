package lotr.common.entity.npc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Charsets;

import lotr.common.LOTRMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;

public class LOTRNames
{
	private static Map allNameBanks = new HashMap();
	
	public static void loadAllNameBanks()
	{
		Map nameBankNamesAndReaders = new HashMap();
		ZipFile zip = null;
		
		try
		{
			ModContainer mc = FMLCommonHandler.instance().findContainerFor(LOTRMod.instance);
			if (mc.getSource().isFile())
			{
				zip = new ZipFile(mc.getSource());
				Enumeration entries = zip.entries();
				while (entries.hasMoreElements())
				{
					ZipEntry entry = (ZipEntry)entries.nextElement();
					String s = entry.getName();
					String path = "assets/lotr/names/";
					if (s.startsWith(path) && s.endsWith(".txt"))
					{
						s = s.substring(path.length());
						int i = s.indexOf(".txt");
						try
						{
							s = s.substring(0, i);
							BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry), Charsets.UTF_8.name()));
							nameBankNamesAndReaders.put(s, reader);
						}
						catch (Exception e)
						{
							System.out.println("Failed to load LOTR name bank " + s + "from zip file");
							e.printStackTrace();
						}
					}
				}
			}
			else
			{
				File nameBankDir = new File(LOTRMod.class.getResource("/assets/lotr/names").toURI());
				for (File file: nameBankDir.listFiles())
				{
					String s = file.getName();
					int i = s.indexOf(".txt");
					if (i < 0)
					{
						System.out.println("Failed to load LOTR name bank " + s + " from MCP folder; name bank files must be in .txt format");
						continue;
					}
					try
					{
						s = s.substring(0, i);
						BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8.name()));
						nameBankNamesAndReaders.put(s, reader);
					}
					catch (Exception e)
					{
						System.out.println("Failed to load LOTR name bank " + s + " from MCP folder");
						e.printStackTrace();
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Failed to load LOTR name banks");
			e.printStackTrace();
		}
		
		Iterator i = nameBankNamesAndReaders.keySet().iterator();
		while (i.hasNext())
		{
			String nameBankName = (String)i.next();
			BufferedReader reader = (BufferedReader)nameBankNamesAndReaders.get(nameBankName);
			try
			{
				List list = new ArrayList();
				String line;
				while ((line = reader.readLine()) != null)
				{
					list.add(line);
				}
				reader.close();
				
				if (list.isEmpty())
				{
					System.out.println("LOTR name bank " + nameBankName + " is empty!");
					continue;
				}
				
				String[] nameBank = new String[list.size()];
				for (int j = 0; j < list.size(); j++)
				{
					nameBank[j] = (String)list.get(j);
				}
				
				allNameBanks.put(nameBankName, nameBank);
				System.out.println("Succesfully loaded LOTR name bank " + nameBankName);
			}
			catch (Exception e)
			{
				System.out.println("Failed to load LOTR name bank " + nameBankName);
				e.printStackTrace();
			}
		}
		
		if (zip != null)
		{
			try
			{
				zip.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static String getRandomName(String nameBankName, Random rand)
	{
		if (allNameBanks.get(nameBankName) != null && allNameBanks.get(nameBankName) instanceof String[])
		{
			String[] nameBank = (String[])allNameBanks.get(nameBankName);
			return nameBank[rand.nextInt(nameBank.length)];
		}
		return "Unnamed";
	}

	public static String getRandomHobbitName(boolean male, Random rand)
	{
		String name = getRandomName(male ? "hobbit_male" : "hobbit_female", rand);
		String surname = getRandomName("hobbit_surnames", rand);
		return name + " " + surname;
	}
	
	public static String[] getRandomHobbitCoupleNames(Random rand)
	{
		String[] names = new String[2];
		String surname = getRandomName("hobbit_surnames", rand);
		String maleName = getRandomName("hobbit_male", rand);
		String femaleName = getRandomName("hobbit_female", rand);
		names[0] = maleName + " " + surname;
		names[1] = femaleName + " " + surname;
		return names;
	}
	
	public static String getRandomHobbitChildNameForParent(boolean male, Random rand, LOTREntityHobbit parent)
	{
		String name = getRandomName(male ? "hobbit_male" : "hobbit_female", rand);
		String surname = parent.getHobbitName().substring(parent.getHobbitName().indexOf(" ") + 1);
		return name + " " + surname;
	}
	
	public static void changeHobbitSurnameForMarriage(LOTREntityHobbit maleHobbit, LOTREntityHobbit femaleHobbit)
	{
		String surname = maleHobbit.getHobbitName().substring(maleHobbit.getHobbitName().indexOf(" ") + 1);
		String femaleFirstName = femaleHobbit.getHobbitName().substring(0, femaleHobbit.getHobbitName().indexOf(" "));
		femaleHobbit.setHobbitName(femaleFirstName + " " + surname);
	}

	public static String[] getRandomHobbitTavernName(Random rand)
	{
		String prefix = getRandomName("hobbitTavern_prefixes", rand);
		String suffix = getRandomName("hobbitTavern_suffixes", rand);
		return new String[]{prefix, suffix};
	}

	public static String getRandomElfName(boolean male, Random rand)
	{
		String name = getRandomName(male ? "elf_male" : "elf_female", rand);
		if (rand.nextInt(5) == 0)
		{
			return name + " " + getRandomName("elf_titles", rand);
		}
		return name;
	}

	public static String getRandomRohanName(Random rand)
	{
		String name = getRandomName("rohan_male", rand);
		return name;
	}
	
	public static String[] getRandomRohanMeadHallName(Random rand)
	{
		String prefix = getRandomName("rohanMeadHall_prefixes", rand);
		String suffix = getRandomName("rohanMeadHall_suffixes", rand);
		return new String[]{prefix, suffix};
	}
	
	public static String getRandomEntName(Random rand)
	{
		String prefix = getRandomName("ent_prefixes", rand);
		String suffix = getRandomName("ent_suffixes", rand);
		return prefix + suffix;
	}

	public static String getRandomGondorName(Random rand)
	{
		String name = getRandomName("gondor_male", rand);
		return name;
	}

	public static String getRandomDwarfName(boolean male, Random rand)
	{
		String name = getRandomName(male ? "dwarf_male" : "dwarf_female", rand);
		String parentName = getRandomName("dwarf_male", rand);
		return name + (male ? " son of " : " daughter of ") + parentName;
	}
	
	public static String getRandomDwarfChildNameForParent(boolean male, Random rand, LOTREntityDwarf parent)
	{
		String name = getRandomName(male ? "dwarf_male" : "dwarf_female", rand);
		String parentName = parent.getDwarfName();
		parentName = parentName.substring(0, parentName.indexOf(" "));
		return name + (male ? " son of " : " daughter of ") + parentName;
	}
	
	public static String getRandomOrcName(Random rand)
	{
		String name = getRandomName("orc", rand);
		return name;
	}

	public static String getRandomTrollName(Random rand)
	{
		String name = getRandomName("troll", rand);
		return name;
	}
	
	public static String[] getRandomDunlendingTavernName(Random rand)
	{
		String prefix = getRandomName("dunlendingTavern_prefixes", rand);
		String suffix = getRandomName("dunlendingTavern_suffixes", rand);
		return new String[]{prefix, suffix};
	}
	
	public static String getRandomNearHaradName(boolean male, Random rand)
	{
		String name = getRandomName(male ? "nearHaradrim_male" : "nearHaradrim_female", rand);
		return name;
	}
}
