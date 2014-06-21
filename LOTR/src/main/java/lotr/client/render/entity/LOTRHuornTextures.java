package lotr.client.render.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import lotr.common.entity.npc.LOTREntityHuornBase;
import lotr.common.entity.npc.LOTREntityTree;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRHuornTextures implements IResourceManagerReloadListener
{
	public static LOTRHuornTextures INSTANCE;
	
	private RenderManager renderManager = RenderManager.instance;
	private HashMap woodTextures = new HashMap();
	private HashMap leafTexturesFast = new HashMap();
	private HashMap leafTexturesFancy = new HashMap();
	
	static
	{
		INSTANCE = new LOTRHuornTextures();
		((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(INSTANCE);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourcemanager)
	{
		woodTextures.clear();
		leafTexturesFast.clear();
		leafTexturesFancy.clear();
	}
	
	public void bindWoodTexture(LOTREntityHuornBase entity)
	{
		int treeType = entity.getTreeType();
		Block block = LOTREntityTree.WOOD_BLOCKS[treeType];
		int meta = LOTREntityTree.WOOD_META[treeType];
		
		ResourceLocation texture = (ResourceLocation)woodTextures.get(Integer.valueOf(treeType));
		if (texture == null)
		{
			texture = getDynamicHuornTexture(block, meta);
			woodTextures.put(Integer.valueOf(treeType), texture);
		}
		renderManager.renderEngine.bindTexture(texture);
	}
	
	public void bindLeafTexture(LOTREntityHuornBase entity)
	{
		int treeType = entity.getTreeType();
		Block block = LOTREntityTree.LEAF_BLOCKS[treeType];
		int meta = LOTREntityTree.LEAF_META[treeType];
		
		ResourceLocation texture = (ResourceLocation)leafTexturesFast.get(Integer.valueOf(treeType));
		if (Minecraft.isFancyGraphicsEnabled())
		{
			texture = (ResourceLocation)leafTexturesFancy.get(Integer.valueOf(treeType));
		}
		if (texture == null)
		{
			texture = getDynamicHuornTexture(block, meta);
			if (Minecraft.isFancyGraphicsEnabled())
			{
				leafTexturesFancy.put(Integer.valueOf(treeType), texture);
			}
			else
			{
				leafTexturesFast.put(Integer.valueOf(treeType), texture);
			}
		}
		renderManager.renderEngine.bindTexture(texture);
		
		int color = block.getRenderColor(meta);
		if (block == Blocks.leaves && meta == 0)
		{
			int i = MathHelper.floor_double(entity.posX);
			int j = MathHelper.floor_double(entity.boundingBox.minY);
			int k = MathHelper.floor_double(entity.posZ);
			
			color = entity.worldObj.getBiomeGenForCoords(i, k).getBiomeFoliageColor(i, j, k);
		}
		float r = (float)(color >> 16 & 255) / 255F;
		float g = (float)(color >> 8 & 255) / 255F;
		float b = (float)(color & 255) / 255F;
		GL11.glColor4f(r, g, b, 1F);
	}
	
	private ResourceLocation getDynamicHuornTexture(Block block, int meta)
	{
		try
		{
			boolean aF = Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1;
			TextureMap terrainAtlas = (TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture);
			BufferedImage[] icons = new BufferedImage[6];
			int width = block.getIcon(0, meta).getIconWidth();
			if (aF)
			{
				width -= 16;
			}
			
			for (int i = 0; i < 6; i++)
			{
				IIcon icon = block.getIcon(i, meta);
				int iconWidth = icon.getIconWidth();
				int iconHeight = icon.getIconHeight();
				if (aF)
				{
					iconWidth -= 16;
					iconHeight -= 16;
				}
				
				if (iconWidth != width || iconHeight != width)
				{
					throw new RuntimeException("Error registering Huorn textures: all icons for block " + block.getUnlocalizedName() + " must have the same texture dimensions");
				}
				ResourceLocation iconPath = new ResourceLocation(icon.getIconName());
				ResourceLocation r = new ResourceLocation(iconPath.getResourceDomain(), "textures/blocks/" + iconPath.getResourcePath() + ".png");
				BufferedImage iconImage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(r).getInputStream());
				icons[i] = iconImage.getSubimage(0, 0, width, width);
			}
			
			BufferedImage image = new BufferedImage(width * 4, width * 2, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = image.createGraphics();
			g2d.drawImage(icons[1], null, width, 0);
			g2d.drawImage(icons[0], null, width * 2, 0);
			g2d.drawImage(icons[4], null, 0, width);
			g2d.drawImage(icons[2], null, width, width);
			g2d.drawImage(icons[5], null, width * 2, width);
			g2d.drawImage(icons[3], null, width * 3, width);
			g2d.dispose();
			
			ImageIO.write(image, "png", new java.io.File(Minecraft.getMinecraft().mcDataDir, "huorn.png"));
			
			return renderManager.renderEngine.getDynamicTextureLocation("lotr:huorn", new DynamicTexture(image));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
