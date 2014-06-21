package lotr.client.render.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRandomSkins
{
	private static Random rand = new Random();
	private static Minecraft mc = Minecraft.getMinecraft();
	private static ResourceLocation missingTexture = mc.getTextureManager().getDynamicTextureLocation("lotr.missingSkin", TextureUtil.missingTexture);
	
	public static List loadSkinsList(String path)
	{
		List list = new ArrayList();

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
			
			list.add(r);
			i++;
		}
		
		return list;
	}
	
	public static ResourceLocation getRandomSkin(List skins, Entity entity)
	{
		if (skins == null || skins.isEmpty())
		{
			return missingTexture;
		}
		
		long l = (long)entity.getEntityId();
		if (entity instanceof LOTREntityNPC)
		{
			l = (long)((LOTREntityNPC)entity).getNPCName().hashCode();
		}
		rand.setSeed(l);
		int i = rand.nextInt(skins.size());
		return (ResourceLocation)skins.get(i);
	}
}
