package lotr.client.render.tileentity;

import lotr.common.tileentity.LOTRTileEntityEntJar;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
		if (jar.drinkItem == null)
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
		GL11.glColor4f(1F, 1F, 1F, 0.5F);
		bindTexture(TextureMap.locationItemsTexture);
		ItemStack drink = new ItemStack(jar.drinkItem, 1, 0);
		IIcon icon = drink.getIconIndex();
		float f1 = icon.getInterpolatedU(7);
		float f2 = icon.getInterpolatedU(8);
		float f3 = icon.getInterpolatedV(7);
		float f4 = icon.getInterpolatedV(8);
		double d3 = 0.1875D;
		double d4 = -0.0625D - (0.75D * (double)jar.drinkAmount / (double)LOTRTileEntityEntJar.MAX_CAPACITY);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-d3, d4, d3, (double)f1, (double)f4);
		tessellator.addVertexWithUV(d3, d4, d3, (double)f2, (double)f4);
		tessellator.addVertexWithUV(d3, d4, -d3, (double)f2, (double)f3);
		tessellator.addVertexWithUV(-d3, d4, -d3, (double)f1, (double)f3);
		tessellator.draw();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
