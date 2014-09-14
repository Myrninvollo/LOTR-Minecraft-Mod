package lotr.client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

import lotr.client.render.entity.LOTRRandomSkins;
import lotr.common.LOTRMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;

public class LOTRTextures implements IResourceManagerReloadListener
{
	private static Minecraft mc = Minecraft.getMinecraft();
	public static void load()
	{
		LOTRTextures textures = new LOTRTextures();
		textures.onResourceManagerReload(mc.getResourceManager());
		((SimpleReloadableResourceManager)mc.getResourceManager()).registerReloadListener(textures);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		loadMapTexture();
		replaceWaterParticles();
	}
	
	public static ResourceLocation mapTexture;
	
	public static void loadMapTexture()
	{
		ResourceLocation map = new ResourceLocation("lotr:map/map.png");
		
		if (LOTRMod.enableSepiaMap)
		{
			try
			{
				BufferedImage mapImage = ImageIO.read(mc.getResourceManager().getResource(map).getInputStream());
				
				int mapWidth = mapImage.getWidth();
				int mapHeight = mapImage.getHeight();
				int[] colors = mapImage.getRGB(0, 0, mapWidth, mapHeight, null, 0, mapWidth);
	
				for (int i = 0; i < colors.length; i++)
				{
					int color = colors[i];
					color = getSepia(color);
					colors[i] = color;
				}
				
				BufferedImage newMapImage = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
				newMapImage.setRGB(0, 0, mapWidth, mapHeight, colors, 0, mapWidth);
	
				mapTexture = mc.renderEngine.getDynamicTextureLocation("lotr:mapDisplayTexture", new DynamicTexture(newMapImage));
			}
			catch (IOException e)
			{
				System.out.println("Failed to generate LOTR map display image");
				e.printStackTrace();
				mapTexture = map;
			}
		}
		else
		{
			mapTexture = map;
		}
	}
	
	private static int getSepia(int rgb)
	{
		Color color = new Color(rgb);
		float[] colors = color.getColorComponents(null);
		float r = colors[0];
		float g = colors[1];
		float b = colors[2];

		float newR = (r * 0.65F) + (g * 0.61F) + (b * 0.31F);
		float newG = (r * 0.51F) + (g * 0.49F) + (b * 0.19F);
		float newB = (r * 0.34F) + (g * 0.31F) + (b * 0.12F);
		
		newR = Math.min(Math.max(0F, newR), 1F);
		newG = Math.min(Math.max(0F, newG), 1F);
		newB = Math.min(Math.max(0F, newB), 1F);
		
		return new Color(newR, newG, newB).getRGB();
	}
	
	private static ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
	private static ResourceLocation newWaterParticles = new ResourceLocation("lotr:misc/waterParticles.png");
	
	private static int newWaterU = 0;
	private static int newWaterV = 8;
	private static int newWaterWidth = 64;
	private static int newWaterHeight = 8;
	
	public static void replaceWaterParticles()
	{
		try
		{
			BufferedImage particles = ImageIO.read(mc.getResourcePackRepository().rprDefaultResourcePack.getInputStream(particleTextures));
			BufferedImage waterParticles = ImageIO.read(mc.getResourceManager().getResource(newWaterParticles).getInputStream());
			
			int[] rgb = waterParticles.getRGB(0, 0, waterParticles.getWidth(), waterParticles.getHeight(), null, 0, waterParticles.getWidth());
			particles.setRGB(newWaterU, newWaterV, newWaterWidth, newWaterHeight, rgb, 0, newWaterWidth);
			
			TextureManager textureManager = mc.getTextureManager();
			textureManager.bindTexture(particleTextures);
			AbstractTexture texture = (AbstractTexture)textureManager.getTexture(particleTextures);
	        TextureUtil.uploadTextureImageAllocate(texture.getGlTextureId(), particles, false, false);
		}
		catch (IOException e)
		{
			System.out.println("Failed to replace rain particles");
			e.printStackTrace();
		}
	}
}
