package lotr.client.render.entity;

import lotr.client.model.LOTRModelBiped;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderBlacksmith extends LOTRRenderBiped
{
	private ResourceLocation skin;
	private static ResourceLocation apron = new ResourceLocation("lotr:mob/blacksmith_apron.png");
	
	private ModelBiped standardRenderPassModel = new LOTRModelBiped(0.5F);
	
    public LOTRRenderBlacksmith(String s)
    {
        super(new LOTRModelBiped(), 0.5F);
		skin = new ResourceLocation("lotr:mob/" + s + ".png");
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
