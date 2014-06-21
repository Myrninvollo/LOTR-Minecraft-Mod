package lotr.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderZebra extends LOTRRenderHorse
{
	private static ResourceLocation zebraTexture = new ResourceLocation("lotr:mob/zebra.png");
	
    public LOTRRenderZebra(ModelBase model, float f)
    {
        super(model, f);
    }
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
	{
		return zebraTexture;
	}
}
