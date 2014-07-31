package lotr.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lotr.common.LOTRMod;

import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;

public class LOTRLang
{
	public static void updateTranslations()
	{
		try
		{
			ModContainer container = FMLCommonHandler.instance().findContainerFor(LOTRMod.instance);
			File mod = container.getSource();
			if (!mod.isFile())
			{
				return;
			}
			
			ZipFile zip = new ZipFile(mod);
			
			ZipEntry en_US = null;
			ZipEntry en_GB = null;
			
			List<ZipEntry> langFiles = new ArrayList<ZipEntry>();
			
			Enumeration entries = zip.entries();
			while (entries.hasMoreElements())
			{
				ZipEntry file = (ZipEntry)entries.nextElement();
				String filename = file.getName();
				
				if (filename.endsWith(".lang"))
				{
					langFiles.add(file);
				}
				
				if (filename.endsWith("en_US.lang"))
				{
					en_US = file;
				}
				if (filename.endsWith("en_GB.lang"))
				{
					en_GB = file;
				}
			}
			
			File newLangFolder = new File(mod.getParentFile(), "LOTR_UpdatedLangFiles");
			if (newLangFolder.exists())
			{
				File[] contents = newLangFolder.listFiles();
				for (File file : contents)
				{
					file.delete();
				}
				newLangFolder.delete();
			}
			newLangFolder.mkdir();
			
			generateReadmeFile(newLangFolder);
					
			for (ZipEntry file : langFiles)
			{
				if (file.equals(en_US) || file.equals(en_GB))
				{
					continue;
				}
				
				String name = FilenameUtils.getName(file.getName());
				System.out.println("Checking lang file for updates " + name);

				File oldLang = File.createTempFile(name + "_old", ".lang");
				copyZipEntryToFile(zip, file, oldLang);
				
				File newLang = new File(newLangFolder, name);
				newLang.createNewFile();
				PrintStream writer = new PrintStream(new FileOutputStream(newLang), true, Charsets.UTF_8.name());

				BufferedReader en_US_reader = new BufferedReader(new InputStreamReader(zip.getInputStream(en_US), Charsets.UTF_8.name()));
				String en_US_line = "";
				while ((en_US_line = en_US_reader.readLine()) != null)
				{
					int i1 = en_US_line.indexOf("=");
					if (i1 < 0)
					{
						writer.println(en_US_line);
					}
					else
					{
						String en_US_key = en_US_line.substring(0, i1);
						boolean foundKey = false;
						
						BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(oldLang), Charsets.UTF_8.name()));
						String line = "";
						while ((line = reader.readLine()) != null)
						{
							int i2 = line.indexOf("=");
							if (i2 >= 0)
							{
								String key = line.substring(0, i2);
								if (key.equals(en_US_key))
								{
									foundKey = true;
									writer.println(line);
									break;
								}
							}
						}
						reader.close();
						
						if (!foundKey)
						{
							writer.println(en_US_key + "=");
							continue;
						}
					}
				}

				writer.close();
				en_US_reader.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void copyZipEntryToFile(ZipFile zip, ZipEntry entry, File copy) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry), Charsets.UTF_8.name()));
		PrintStream writer = new PrintStream(new FileOutputStream(copy), true, Charsets.UTF_8.name());
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			writer.println(line);
		}
		reader.close();
		writer.close();
	}
	
	private static void generateReadmeFile(File folder) throws IOException
	{
		File readme = new File(folder, "readme.txt");
		readme.createNewFile();
		PrintStream writer = new PrintStream(new FileOutputStream(readme));
		writer.println("LOTR lang file update-helper");
		writer.println();
		writer.println("The purpose of this helper is to assist people in updating the mod's lang files after a mod update.");
		writer.println();
		writer.println("When the mod is loaded, it checks all lang files against en_US.lang, and outputs a copy of them here.");
		writer.println("If a lang file is missing any keys (i.e. names of new blocks, items, mobs, etc. added in a mod update) then those keys are added to the lang file.");
		writer.println("Unnecessary keys are also removed from the lang file - for example, if a feature is removed from the mod.");
		writer.println("The lang files outputted here also have their contents organised in the same order as en_US.lang, to make comparisons easier.");
		writer.println();
		writer.println("I hope this system will be much easier than manually checking your lang file against en_US.lang, for every update, to find out what has been added.");
		writer.println();
		writer.println("DO NOT STORE ANYTHING in this folder - the folder, and its contents, are re-created every time the mod loads.");
		writer.println("Anything else in the folder will be deleted.");
		writer.println("If you want to update one of the lang files in this folder, copy and paste it somewhere safe!");
		writer.println();
		writer.println("And finally, if you have updated a lang file (or created a new one) and want to send it to us, the best way to do so is through the mod's Facebook page.");
		writer.println("We credit everyone by name unless asked not to, so please let us know if you do not want your name mentioned in the credits file.");
		writer.println();
		writer.println("Please note: Lang files must be in UTF-8 format, otherwise errors will occur.");
		writer.close();
	}
}
