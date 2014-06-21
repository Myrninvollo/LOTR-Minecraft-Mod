package lotr.client.render.entity;

import lotr.client.render.LOTRRenderBlocks;
import lotr.common.LOTRMod;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderPlate extends Render
{
	private RenderBlocks blockRenderer = new RenderBlocks();
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TextureMap.locationBlocksTexture;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(-f, 0F, 1F, 0F);
		int i = entity.getBrightnessForRender(f1);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1F, (float)k / 1F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		bindEntityTexture(entity);
		LOTRRenderBlocks.renderEntityPlate(entity.worldObj, 0, 0, 0, LOTRMod.plateBlock, blockRenderer);
		GL11.glPopMatrix();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }
}
