package lotr.client.render.entity;

import lotr.client.model.LOTRModelBoar;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderWildBoar extends RenderPig
{
	public static ResourceLocation boarSkin = new ResourceLocation("lotr:mob/boar.png");
	
    public LOTRRenderWildBoar()
    {
        super(new LOTRModelBoar(), new LOTRModelBoar(0.5F), 0.7F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return boarSkin;
    }
}
