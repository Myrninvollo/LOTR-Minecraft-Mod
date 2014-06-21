package lotr.client.render.entity;

import lotr.client.model.LOTRModelScorpion;
import lotr.common.entity.animal.LOTREntityDesertScorpion;
import lotr.common.entity.npc.LOTREntityScorpion;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderScorpion extends RenderLiving
{
	private static ResourceLocation jungleTexture = new ResourceLocation("lotr:mob/scorpion/jungle.png");
	private static ResourceLocation desertTexture = new ResourceLocation("lotr:mob/scorpion/desert.png");
	
    public LOTRRenderScorpion()
    {
        super(new LOTRModelScorpion(), 1F);
        setRenderPassModel(new LOTRModelScorpion());
    }
    
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
    	if (entity instanceof LOTREntityDesertScorpion)
    	{
    		return desertTexture;
    	}
		return jungleTexture;
	}

    @Override
    protected void preRenderCallback(EntityLivingBase entity, float f)
    {
        float scale = ((LOTREntityScorpion)entity).getScorpionScaleAmount();
		GL11.glScalef(scale, scale, scale);
    }	

	@Override
    protected float getDeathMaxRotation(EntityLivingBase entity)
    {
        return 180F;
    }
	
	@Override
	public float handleRotationFloat(EntityLivingBase entity, float f)
	{
		float strikeTime = (float)((LOTREntityScorpion)entity).getStrikeTime();
		if (strikeTime > 0F)
		{
			strikeTime -= f;
		}
		return strikeTime / 20F;
	}
}
