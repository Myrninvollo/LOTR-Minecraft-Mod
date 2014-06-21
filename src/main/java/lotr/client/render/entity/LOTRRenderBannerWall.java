package lotr.client.render.entity;

import lotr.client.model.LOTRModelBannerWall;
import lotr.common.entity.item.LOTREntityBannerWall;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderBannerWall extends Render
{
    private static LOTRModelBannerWall model = new LOTRModelBannerWall();
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
	{
		LOTREntityBannerWall banner = (LOTREntityBannerWall)entity;
		int bannerType = banner.getBannerType();
		return LOTRRenderBanner.getStandTexture(bannerType);
	}

	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		LOTREntityBannerWall banner = (LOTREntityBannerWall)entity;
		int bannerType = banner.getBannerType();
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glScalef(-1F, -1F, 1F);
		GL11.glRotatef(f, 0F, 1F, 0F);
		bindTexture(LOTRRenderBanner.getStandTexture(bannerType));
        model.renderPost(0.0625F);
		bindTexture(LOTRRenderBanner.getBannerTexture(bannerType));
		model.renderBanner(0.0625F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
