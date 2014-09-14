package lotr.client.render.tileentity;

import lotr.common.tileentity.LOTRTileEntityUtumnoPortal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderUtumnoPortal extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		int passes = LOTRTileEntityUtumnoPortal.HEIGHT * 2;
		for (int i = 0; i < passes; i++)
		{
			GL11.glPushMatrix();
			
			GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1F + ((float)i * 0.5F), (float)d2 + 0.5F);

			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(0F, 0F, 0F, (float)(passes - i) / (float)passes);
			double width = (double)LOTRTileEntityUtumnoPortal.WIDTH / 2D;
			tessellator.addVertexWithUV(width, 0D, width, 0D, 0D);
			tessellator.addVertexWithUV(width, 0D, -width, 0D, 0D);
			tessellator.addVertexWithUV(-width, 0D, -width, 0D, 0D);
			tessellator.addVertexWithUV(-width, 0D, width, 0D, 0D);
			tessellator.draw();
			
			GL11.glPopMatrix();
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
