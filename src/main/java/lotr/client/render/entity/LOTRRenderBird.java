package lotr.client.render.entity;

import lotr.client.model.LOTRModelBird;
import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityCrebain;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderBird extends RenderLiving
{
	private static ResourceLocation crebainTexture = new ResourceLocation("lotr:mob/bird/crebain.png");
    public LOTRRenderBird()
    {
        super(new LOTRModelBird(), 0.2F);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
		if (entity instanceof LOTREntityCrebain)
		{
			return crebainTexture;
		}
        return ((LOTREntityBird)entity).getBirdType().texture;
    }
	
	@Override
	public void preRenderCallback(EntityLivingBase entity, float f)
	{
		if (entity instanceof LOTREntityCrebain)
		{
			GL11.glScalef(1.8F, 1.8F, 1.8F);
		}
	}
	
	@Override
    protected float handleRotationFloat(EntityLivingBase entity, float f)
    {
		LOTREntityBird bird = ((LOTREntityBird)entity);
		if (bird.isBirdStill() && bird.flapTime > 0)
		{
			return bird.flapTime - f;
		}
		else
		{
			return super.handleRotationFloat(entity, f);
		}
    }
}
