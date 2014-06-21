package lotr.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

public class LOTRRenderShirePony extends LOTRRenderHorse
{
    public LOTRRenderShirePony(ModelBase model, float f)
    {
        super(model, f);
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(0.8F, 0.8F, 0.8F);
	}
}
