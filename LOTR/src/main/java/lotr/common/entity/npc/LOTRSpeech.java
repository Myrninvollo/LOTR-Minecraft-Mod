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
	private static Map allSpeechBanks = new HashMap();
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
							BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry), Charsets.UTF_8.name()));
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
				for (File file: speechBankDir.listFiles())
				{
					String s = file.getName();
					int i = s.indexOf(".txt");
					if (i < 0)
					{
						System.out.println("Failed to load LOTR speech bank " + s + " from MCP folder; speech bank files must be in .txt format");
						continue;
					}
					try
					{
						s = s.substring(0, i);
						BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8.name()));
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
	
	public static String[] getSpeechBank(String speechBankName)
	{
		if (allSpeechBanks.get(speechBankName) != null && allSpeechBanks.get(speechBankName) instanceof String[])
		{
			return (String[])allSpeechBanks.get(speechBankName);
		}
		return new String[] {"Speech bank " + speechBankName + " could not be found"};
	}
	
	public static String getRandomSpeech(String speechBankName)
	{
		String[] speechBank = getSpeechBank(speechBankName);
		return speechBank[rand.nextInt(speechBank.length)];
	}
	
	public static IChatComponent getNamedSpeech(EntityLivingBase entity, String speechBankName)
	{
		String s = EnumChatFormatting.YELLOW + "<" + entity.getCommandSenderName() + ">" + EnumChatFormatting.WHITE + " " + getRandomSpeech(speechBankName);
		return new ChatComponentText(s);
	}

	public static IChatComponent getNamedSpeechForPlayer(EntityLivingBase entity, String speechBankName, EntityPlayer entityplayer)
	{
		String s = EnumChatFormatting.YELLOW + "<" + entity.getCommandSenderName() + ">" + EnumChatFormatting.WHITE + " " + getRandomSpeech(speechBankName);
		s = s.replace("#", entityplayer.getCommandSenderName());
		return new ChatComponentText(s);
	}
	
	public static IChatComponent getNamedLocationSpeechForPlayer(EntityLivingBase entity, String locationName, String speechBankName, EntityPlayer entityplayer)
	{
		String s = EnumChatFormatting.YELLOW + "<" + entity.getCommandSenderName() + ">" + EnumChatFormatting.WHITE + " " + getRandomSpeech(speechBankName);
		s = s.replace("#", entityplayer.getCommandSenderName()).replace("@", locationName);
		return new ChatComponentText(s);
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
