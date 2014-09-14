package lotr.client.render.entity;

import java.util.HashMap;

import lotr.client.model.LOTRModelDikDik;
import lotr.common.entity.animal.LOTREntityDikDik;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderDikDik extends RenderLiving
{
	private static HashMap<Integer, ResourceLocation> dikDikTextures = new HashMap();
	
    public LOTRRenderDikDik()
    {
        super(new LOTRModelDikDik(), 0.8F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        int i = ((LOTREntityDikDik)entity).getDikdikType();
        ResourceLocation texture = dikDikTextures.get(i);
		if (texture == null)
		{
			texture = new ResourceLocation("lotr:mob/dikdik/" + i + ".png");
			dikDikTextures.put(i, texture);
		}
		return texture;
    }
}