package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderDunlendingBase extends LOTRRenderBiped
{
	private static List dunlendingSkins;
	
	public LOTRRenderDunlendingBase()
	{
		super(new LOTRModelBiped(), 0.5F);
		dunlendingSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/dunland/dunlending_male");
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return LOTRRandomSkins.getRandomSkin(dunlendingSkins, entity);
    }
}
