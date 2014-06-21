package lotr.client.render.tileentity;

import lotr.client.model.LOTRModelBlock;
import lotr.common.tileentity.LOTRTileEntityGulduril;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderGuldurilGlow extends TileEntitySpecialRenderer
{
	private static ModelBase blockModel = new LOTRModelBlock(0.5F);
	private static ResourceLocation texture = new ResourceLocation("lotr:misc/gulduril_glow.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glPushMatrix();
		float f1 = (float)((LOTRTileEntityGulduril)tileentity).ticksExisted + f;
		bindTexture(texture);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		float f2 = f1 * 0.01F;
		float f3 = f1 * 0.01F;
		GL11.glTranslatef(f2, f3, 0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.5F, (float)d2 + 0.5F);
		float f4 = 0.6F;
		GL11.glColor4f(f4, f4, f4, 1F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
		blockModel.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
