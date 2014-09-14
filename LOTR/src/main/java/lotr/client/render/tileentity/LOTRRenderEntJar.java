package lotr.client.render.tileentity;

import java.awt.Color;

import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityEntJar;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderEntJar extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		LOTRTileEntityEntJar jar = (LOTRTileEntityEntJar)tileentity;
		if (jar.drinkAmount <= 0)
		{
			return;
		}
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)d + 0.5F, (float)d1, (float)d2 + 0.5F);
		GL11.glScalef(-1F, -1F, 1F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float transparency = 0.5F;
		
		Tessellator tessellator = Tessellator.instance;
		IIcon icon = null;
		float minU = 0F;
		float maxU = 0F;
		float minV = 0F;
		float maxV = 0F;
		int color = 0xFFFFFF;
		
		if (jar.drinkMeta >= 0)
		{
			bindTexture(TextureMap.locationItemsTexture);
			ItemStack drink = new ItemStack(LOTRMod.entDraught, 1, jar.drinkMeta);
			icon = drink.getIconIndex();
			minU = icon.getInterpolatedU(7);
			maxU = icon.getInterpolatedU(8);
			minV = icon.getInterpolatedV(7);
			maxV = icon.getInterpolatedV(8);
		}
		else
		{
			bindTexture(TextureMap.locationBlocksTexture);
			icon = Blocks.water.getBlockTextureFromSide(1);
			minU = icon.getInterpolatedU(0);
			maxU = icon.getInterpolatedU(6);
			minV = icon.getInterpolatedV(0);
			maxV = icon.getInterpolatedV(6);
			
			color = Blocks.water.colorMultiplier(jar.getWorldObj(), jar.xCoord, jar.yCoord, jar.zCoord);
		}
		
		double d3 = 0.1875D;
		double d4 = -0.0625D - (0.75D * (double)jar.drinkAmount / (double)LOTRTileEntityEntJar.MAX_CAPACITY);
		
		tessellator.startDrawingQuads();
		float[] colors = new Color(color).getColorComponents(null);
		tessellator.setColorRGBA_F(colors[0], colors[1], colors[2], transparency);
		tessellator.addVertexWithUV(-d3, d4, d3, (double)minU, (double)maxV);
		tessellator.addVertexWithUV(d3, d4, d3, (double)maxU, (double)maxV);
		tessellator.addVertexWithUV(d3, d4, -d3, (double)maxU, (double)minV);
		tessellator.addVertexWithUV(-d3, d4, -d3, (double)minU, (double)minV);
		tessellator.draw();
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
