package lotr.client.render.entity;

import java.util.HashMap;
import java.util.Map;

import lotr.client.model.LOTRModelRabbit;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderRabbit extends RenderLiving
{
	private static Map rabbitSkins = new HashMap();
	
    public LOTRRenderRabbit()
    {
        super(new LOTRModelRabbit(), 0.3F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		int i = ((LOTREntityRabbit)entity).getRabbitType();
		String s = String.valueOf(i);
		ResourceLocation r = (ResourceLocation)rabbitSkins.get(s);
		if (r == null)
		{
			r = new ResourceLocation("lotr:mob/rabbit/" + s + ".png");
			rabbitSkins.put(s, r);
		}
		return r;
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(0.75F, 0.75F, 0.75F);
	}
}
