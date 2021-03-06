package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelBiped;
import lotr.common.entity.npc.LOTREntityGondorMan;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderGondorSoldier extends LOTRRenderBiped
{
	private static List gondorSkins;
	
	public LOTRRenderGondorSoldier()
	{
		super(new LOTRModelBiped(), 0.5F);
		gondorSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/gondorSoldier");
	}
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
    {
		return LOTRRandomSkins.getRandomSkin(gondorSkins, (LOTREntityGondorMan)entity);
    }
}
