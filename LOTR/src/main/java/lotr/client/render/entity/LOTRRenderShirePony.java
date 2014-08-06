package lotr.client.render.entity;

import lotr.common.entity.animal.LOTREntityShirePony;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

public class LOTRRenderShirePony extends LOTRRenderHorse
{
    public LOTRRenderShirePony()
    {
        super();
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		float scale = LOTREntityShirePony.PONY_SCALE;
		GL11.glScalef(scale, scale, scale);
	}
}
