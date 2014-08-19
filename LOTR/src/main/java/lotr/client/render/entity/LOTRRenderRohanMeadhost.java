package lotr.client.render.entity;

import lotr.client.model.LOTRModelBiped;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderRohanMeadhost extends LOTRRenderBiped
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/rohan/blacksmith.png");
	private static ResourceLocation apron = new ResourceLocation("lotr:mob/rohan/mead_apron.png");
	
	private ModelBiped standardRenderPassModel = new LOTRModelBiped(0.5F);
	
	public LOTRRenderRohanMeadhost()
	{
		super(new LOTRModelBiped(), 0.5F);
		setRenderPassModel(standardRenderPassModel);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return skin;
	}

	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		if (pass == 1)
		{
			setRenderPassModel(standardRenderPassModel);
			bindTexture(apron);
			return 1;
		}
        else
		{
			return super.shouldRenderPass(entity, pass, f);
		}
    }
}
