package lotr.client.render.entity;

import java.util.HashMap;
import java.util.Map;

import lotr.client.model.LOTRModelBanner;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.item.LOTRItemBanner;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderBanner extends Render
{
	public static Map standTextures = new HashMap();
	public static Map bannerTextures = new HashMap();
    private static LOTRModelBanner model = new LOTRModelBanner();
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
	{
		return getStandTexture(entity);
	}
	
	public static ResourceLocation getStandTexture(int bannerType)
	{
		if (bannerType < 0 || bannerType >= LOTRItemBanner.bannerTypes.length)
		{
			bannerType = 0;
		}
		
		String s = LOTRItemBanner.bannerTypes[bannerType];
		ResourceLocation r = (ResourceLocation)standTextures.get(s);
		if (r == null)
		{
			r = new ResourceLocation("lotr:item/banner/stand_" + s + ".png");
			standTextures.put(s, r);
		}
		return r;
	}
	
	private ResourceLocation getStandTexture(Entity entity)
    {
		return getStandTexture(((LOTREntityBanner)entity).getBannerType());
	}
	
	public static ResourceLocation getBannerTexture(int bannerType)
	{
		if (bannerType < 0 || bannerType >= LOTRItemBanner.bannerTypes.length)
		{
			bannerType = 0;
		}
		
		String s = LOTRItemBanner.bannerTypes[bannerType];
		ResourceLocation r = (ResourceLocation)bannerTextures.get(s);
		if (r == null)
		{
			r = new ResourceLocation("lotr:item/banner/banner_" + s + ".png");
			bannerTextures.put(s, r);
		}
		return r;
	}
	
	private ResourceLocation getBannerTexture(Entity entity)
    {
		return getBannerTexture(((LOTREntityBanner)entity).getBannerType());
	}

	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)d, (float)d1 + 1.5F, (float)d2);
        GL11.glScalef(-1F, -1F, 1F);
		GL11.glRotatef(180F - entity.rotationYaw, 0F, 1F, 0F);
		GL11.glTranslatef(0F, 0.01F, 0F);
		bindTexture(getStandTexture(entity));
        model.renderStand(0.0625F);
        model.renderPost(0.0625F);
		bindTexture(getBannerTexture(entity));
		model.renderBanner(0.0625F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
