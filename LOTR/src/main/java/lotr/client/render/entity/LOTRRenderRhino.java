package lotr.client.render.entity;

import lotr.client.model.LOTRModelRhino;
import lotr.common.entity.animal.LOTREntityRhino;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderRhino extends RenderLiving
{
	private static ResourceLocation rhinoTexture = new ResourceLocation("lotr:mob/rhino/rhino.png");
	private static ResourceLocation saddleTexture = new ResourceLocation("lotr:mob/rhino/saddle.png");
	
    public LOTRRenderRhino()
    {
        super(new LOTRModelRhino(), 0.5F);
        setRenderPassModel(new LOTRModelRhino(0.5F));
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return rhinoTexture;
    }
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int i, float f)
    {
		if (i == 0 && ((LOTREntityRhino)entity).isMountSaddled())
		{
			bindTexture(saddleTexture);
			return 1;
		}
		return -1;
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(1.3F, 1.3F, 1.3F);
	}
}
