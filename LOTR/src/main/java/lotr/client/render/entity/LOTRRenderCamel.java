
package lotr.client.render.entity;

import org.lwjgl.opengl.GL11;

import lotr.client.model.LOTRModelCamel;
import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityCamel;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderCamel extends RenderLiving
{
	private static ResourceLocation camelSkin = new ResourceLocation("lotr:mob/camel/camel.png");
	private static ResourceLocation saddleTexture = new ResourceLocation("lotr:mob/camel/saddle.png");
	
    public LOTRRenderCamel()
    {
        super(new LOTRModelCamel(), 0.5F);
        setRenderPassModel(new LOTRModelCamel(0.5F));
    }
    
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
		return camelSkin;
	}
    
    @Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		if (pass == 0 && ((LOTREntityCamel)entity).isMountSaddled())
		{
			bindTexture(saddleTexture);
			return 1;
		}
		return super.shouldRenderPass(entity, pass, f);
    }
    
    @Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(1.25F, 1.25F, 1.25F);
	}
}
