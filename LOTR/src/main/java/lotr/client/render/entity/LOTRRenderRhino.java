package lotr.client.render.entity;

import lotr.client.model.LOTRModelRhino;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderRhino extends RenderLiving
{
	private static ResourceLocation rhinoTexture = new ResourceLocation("lotr:mob/rhino.png");
	
    public LOTRRenderRhino()
    {
        super(new LOTRModelRhino(), 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return rhinoTexture;
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(1.3F, 1.3F, 1.3F);
	}
}
