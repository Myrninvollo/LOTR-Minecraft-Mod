package lotr.client.render.entity;

import java.util.*;
import java.util.Map.Entry;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.*;
import net.minecraft.util.ResourceLocation;

public class LOTRRandomSkins implements IResourceManagerReloadListener
{
	private static Random rand = new Random();
	private static Minecraft mc = Minecraft.getMinecraft();
	private static ResourceLocation missingTexture = mc.getTextureManager().getDynamicTextureLocation("lotr.missingSkin", TextureUtil.missingTexture);
	
	private static Map<String, List> skinLists = new HashMap();
	
	static
	{
		((SimpleReloadableResourceManager)mc.getResourceManager()).registerReloadListener(new LOTRRandomSkins());
	}
	
	public static List loadSkinsList(String path)
	{
		List skins = skinLists.get(path);
		if (skins == null)
		{
			skins = loadAllRandomSkins(path);
			skinLists.put(path, skins);
		}
		
		return skins;
	}
	
	private static List loadAllRandomSkins(String path)
	{
		List skins = new ArrayList();
		
		int i = 0;
		while (true)
		{
			ResourceLocation r = new ResourceLocation(path + "/" + i + ".png");
			
			try
			{
				if (mc.getResourceManager().getResource(r) == null)
				{
					break;
				}
			}
			catch (Exception e)
			{
				break;
			}
			
			skins.add(r);
			i++;
		}
		
		return skins;
	}
	
	public static ResourceLocation getRandomSkin(List skins, LOTREntityNPC entity)
	{
		if (skins == null || skins.isEmpty())
		{
			return missingTexture;
		}
		
		long l = entity.getUniqueID().getLeastSignificantBits();
		rand.setSeed(l);
		int i = rand.nextInt(skins.size());
		return (ResourceLocation)skins.get(i);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourcemanager)
	{
		for (Entry<String, List> entry : skinLists.entrySet())
		{
			String path = entry.getKey();
			List skins = entry.getValue();
			
			skins.clear();
			List newSkins = loadAllRandomSkins(path);
			skins.addAll(newSkins);
		}
	}
}
