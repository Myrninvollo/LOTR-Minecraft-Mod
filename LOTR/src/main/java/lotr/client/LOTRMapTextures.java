package lotr.client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import lotr.common.world.biome.LOTRBiome;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class LOTRMapTextures
{
	public static ResourceLocation mapTexture;
	
	private static boolean sepia = false;

	public static void loadMapTexture()
	{
		ResourceLocation map = new ResourceLocation("lotr:map/map.png");
		
		if (sepia)
		{
			try
			{
				BufferedImage mapImage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(map).getInputStream());
				
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
	
				mapTexture = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("lotr:mapDisplayTexture", new DynamicTexture(newMapImage));
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
}
