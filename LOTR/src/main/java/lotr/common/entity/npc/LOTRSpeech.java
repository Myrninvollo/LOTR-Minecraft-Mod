package lotr.common.entity.npc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;

import scala.actors.threadpool.Arrays;
import sun.management.FileSystem;

import com.google.common.base.Charsets;

import lotr.common.LOTRMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;

public class LOTRSpeech
{
	private static Map<String, String[]> allSpeechBanks = new HashMap();
	private static Random rand = new Random();
	
	public static void loadAllSpeechBanks()
	{
		Map speechBankNamesAndReaders = new HashMap();
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
					String path = "assets/lotr/speech/";
					if (s.startsWith(path) && s.endsWith(".txt"))
					{
						s = s.substring(path.length());
						int i = s.indexOf(".txt");
						try
						{
							s = s.substring(0, i);
							BufferedReader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(zip.getInputStream(entry)), Charsets.UTF_8.name()));
							speechBankNamesAndReaders.put(s, reader);
						}
						catch (Exception e)
						{
							System.out.println("Failed to load LOTR speech bank " + s + "from zip file");
							e.printStackTrace();
						}
					}
				}
			}
			else
			{
				File speechBankDir = new File(LOTRMod.class.getResource("/assets/lotr/speech").toURI());
				Collection<File> subfiles = FileUtils.listFiles(speechBankDir, null, true);
				for (File subfile : subfiles)
				{
					String s = subfile.getPath();
					s = s.substring(speechBankDir.getPath().length() + 1);
					s = s.replace(File.separator, "/");
					
					int i = s.indexOf(".txt");
					if (i < 0)
					{
						System.out.println("Failed to load LOTR speech bank " + s + " from MCP folder; speech bank files must be in .txt format");
						continue;
					}
					try
					{
						s = s.substring(0, i);
						BufferedReader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(subfile)), Charsets.UTF_8.name()));
						speechBankNamesAndReaders.put(s, reader);
					}
					catch (Exception e)
					{
						System.out.println("Failed to load LOTR speech bank " + s + " from MCP folder");
						e.printStackTrace();
					}
				}

			}
		}
		catch (Exception e)
		{
			System.out.println("Failed to load LOTR speech banks");
			e.printStackTrace();
		}
		
		Iterator i = speechBankNamesAndReaders.keySet().iterator();
		while (i.hasNext())
		{
			String speechBankName = (String)i.next();
			BufferedReader reader = (BufferedReader)speechBankNamesAndReaders.get(speechBankName);
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
					System.out.println("LOTR speech bank " + speechBankName + " is empty!");
					continue;
				}
				
				String[] speechBank = new String[list.size()];
				for (int j = 0; j < list.size(); j++)
				{
					speechBank[j] = (String)list.get(j);
				}
				
				allSpeechBanks.put(speechBankName, speechBank);
				System.out.println("Succesfully loaded LOTR speech bank " + speechBankName);
			}
			catch (Exception e)
			{
				System.out.println("Failed to load LOTR speech bank " + speechBankName);
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
	
	private static String[] getSpeechBank(String name)
	{
		String[] bank = allSpeechBanks.get(name);
		if (bank != null)
		{
			return bank;
		}
		return new String[] {"Speech bank " + name + " could not be found"};
	}
	
	public static String getRandomSpeech(String speechBankName)
	{
		String[] speechBank = getSpeechBank(speechBankName);
		return speechBank[rand.nextInt(speechBank.length)];
	}
	
	public static IChatComponent getNamedSpeech(EntityLivingBase entity, String speechBankName)
	{
		return getNamedSpeechForPlayer(entity, speechBankName, null);
	}
	
	public static IChatComponent getNamedSpeechForPlayer(EntityLivingBase entity, String speechBankName, EntityPlayer entityplayer)
	{
		return getNamedSpeechForPlayer(entity, speechBankName, entityplayer, null, null);
	}

	public static IChatComponent getNamedSpeechForPlayer(EntityLivingBase entity, String speechBankName, EntityPlayer entityplayer, String location, String objective)
	{
		String name = entity.getCommandSenderName();
		String message = getSpeechUnnamed(speechBankName, entityplayer, location, objective);
		
		String s = EnumChatFormatting.YELLOW + "<" + name + ">" + EnumChatFormatting.WHITE + " " + message;
		return new ChatComponentText(s);
	}
	
	public static String getSpeechUnnamed(String speechBankName, EntityPlayer entityplayer, String location, String objective)
	{
		String s = getRandomSpeech(speechBankName);
		
		if (entityplayer != null)
		{
			s = s.replace("#", entityplayer.getCommandSenderName());
		}
		
		if (location != null)
		{
			s = s.replace("@", location);
		}
		
		if (objective != null)
		{
			s = s.replace("$", objective);
		}
		
		return s;
	}
	
	public static void messageAllPlayers(IChatComponent message)
	{
		if (MinecraftServer.getServer() == null)
		{
			return;
		}
		
		for (Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
		{
			((EntityPlayer)player).addChatMessage(message);
		}
	}
}
