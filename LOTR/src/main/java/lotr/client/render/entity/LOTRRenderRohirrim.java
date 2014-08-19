package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelBiped;
import lotr.common.LOTRShields;
import lotr.common.entity.npc.LOTREntityRohanMan;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderRohirrim extends LOTRRenderBiped
{
	private static List rohirrimSkins;
	
	public LOTRRenderRohirrim()
	{
		super(new LOTRModelBiped(), 0.5F);
		rohirrimSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/rohan/rohirrim");
		setShield(LOTRShields.ALIGNMENT_ROHAN);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return LOTRRandomSkins.getRandomSkin(rohirrimSkins, (LOTREntityRohanMan)entity);
    }
}
