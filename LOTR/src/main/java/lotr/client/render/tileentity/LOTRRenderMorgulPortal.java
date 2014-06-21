package lotr.client.render.tileentity;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderMorgulPortal extends TileEntitySpecialRenderer
{
	private static ResourceLocation texture = new ResourceLocation("lotr:misc/gulduril_glow.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glPushMatrix();
		float f1 = (float)(tileentity.getWorldObj().getWorldTime()) + f;
		bindTexture(texture);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		float f2 = f1 * 0.01F;
		GL11.glTranslatef(f2, 0F, 0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.5F + (0.25F * MathHelper.sin(f1 / 40F)), (float)d2 + 0.5F);
		float f4 = 0.9F;
		GL11.glColor4f(f4, f4, f4, 1F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.5D, 0D, 0.5D, 0D, 0D);
		tessellator.addVertexWithUV(0.5D, 0D, -0.5D, 0D, 0D);
		tessellator.addVertexWithUV(-0.5D, 0D, -0.5D, 0D, 0D);
		tessellator.addVertexWithUV(-0.5D, 0D, 0.5D, 0D, 0D);
		tessellator.draw();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
