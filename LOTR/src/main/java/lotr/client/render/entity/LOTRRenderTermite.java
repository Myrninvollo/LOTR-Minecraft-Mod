package lotr.client.render.entity;

import lotr.client.model.LOTRModelTermite;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderTermite extends RenderLiving
{
	private static ResourceLocation texture = new ResourceLocation("lotr:mob/termite.png");
	
    public LOTRRenderTermite()
    {
        super(new LOTRModelTermite(), 0.2F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		float scale = 0.25F;
		GL11.glScalef(scale, scale, scale);
	}
}