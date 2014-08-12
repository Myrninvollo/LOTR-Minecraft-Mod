package lotr.common;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;

public class LOTRLore
{
	private static Map lore = new HashMap();
	
	public static void loadAllNameBanks()
	{
		Map loreInputStreams = new HashMap();
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
					String path = "assets/lotr/lore/";
					if (s.startsWith(path) && s.endsWith(".txt"))
					{
						s = s.substring(path.length());
						int i = s.indexOf(".txt");
						try
						{
							s = s.substring(0, i);
							InputStream in = zip.getInputStream(entry);
							loreInputStreams.put(s, in);
						}
						catch (Exception e)
						{
							System.out.println("Failed to load LOTR lore " + s + "from zip file");
							e.printStackTrace();
						}
					}
				}
			}
			else
			{
				File nameBankDir = new File(LOTRMod.class.getResource("/assets/lotr/lore").toURI());
				for (File file: nameBankDir.listFiles())
				{
					String s = file.getName();
					int i = s.indexOf(".txt");
					if (i < 0)
					{
						System.out.println("Failed to load LOTR lore " + s + " from MCP folder; name bank files must be in .txt format");
						continue;
					}
					try
					{
						s = s.substring(0, i);
						InputStream in = new FileInputStream(file);
						loreInputStreams.put(s, in);
					}
					catch (Exception e)
					{
						System.out.println("Failed to load LOTR lore " + s + " from MCP folder");
						e.printStackTrace();
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Failed to load LOTR lore");
			e.printStackTrace();
		}
		
		Iterator i = loreInputStreams.keySet().iterator();
		while (i.hasNext())
		{
			String loreName = (String)i.next();
			InputStream inputStream = (InputStream)loreInputStreams.get(loreName);
			try
			{
				String loreText = IOUtils.toString(inputStream, Charsets.UTF_8);
				lore.put(loreName, loreText);
				System.out.println("Succesfully loaded LOTR lore " + loreName);
			}
			catch (Exception e)
			{
				System.out.println("Failed to load LOTR lore " + loreName);
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
	
	public static ItemStack getRandomLore(Random random)
	{
		ItemStack itemstack = new ItemStack(Items.written_book);
		
		List loreTitles = new ArrayList(lore.keySet());
		String loreTitle = (String)loreTitles.get(random.nextInt(loreTitles.size()));
		String loreText = (String)lore.get(loreTitle);
		
		NBTTagCompound data = new NBTTagCompound();
		itemstack.getTagCompound().setString("title", loreTitle);

		itemstack.setTagCompound(data);
		
		return itemstack;
	}
}
