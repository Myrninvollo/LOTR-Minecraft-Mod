package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelBiped;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderNearHaradrim extends LOTRRenderBiped
{
	private static List haradrimSkinsMale;
	private static List haradrimSkinsFemale;
	
	public LOTRRenderNearHaradrim()
	{
		super(new LOTRModelBiped(), 0.5F);
		haradrimSkinsMale = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/haradrim_male");
		haradrimSkinsFemale = LOTRRandomSkins.loadSkinsList("lotr:mob/nearHarad/haradrim_female");
	}
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
    {
		LOTREntityNearHaradrim haradrim = (LOTREntityNearHaradrim)entity;
		if (haradrim.isHaradrimMale())
		{
			return LOTRRandomSkins.getRandomSkin(haradrimSkinsMale, haradrim);
		}
		else
		{
			return LOTRRandomSkins.getRandomSkin(haradrimSkinsFemale, haradrim);
		}
    }
}
