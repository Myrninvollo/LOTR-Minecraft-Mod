package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelBiped;
import lotr.common.entity.npc.LOTREntityBandit;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderBandit extends LOTRRenderBiped
{
	private static List banditSkins;
	
	public LOTRRenderBandit()
	{
		super(new LOTRModelBiped(), 0.5F);
		banditSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/bandit");
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return LOTRRandomSkins.getRandomSkin(banditSkins, (LOTREntityBandit)entity);
    }
}
