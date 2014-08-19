package lotr.client.render.entity;

import java.util.List;

import org.lwjgl.opengl.GL11;

import lotr.client.model.LOTRModelBiped;
import lotr.client.render.LOTRRenderShield;
import lotr.common.LOTRShields;
import lotr.common.entity.npc.LOTREntityGondorMan;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderGondorSoldier extends LOTRRenderBiped
{
	private static List gondorSkins;
	
	public LOTRRenderGondorSoldier()
	{
		super(new LOTRModelBiped(), 0.5F);
		gondorSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/gondor/gondorSoldier");
		setShield(LOTRShields.ALIGNMENT_GONDOR);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return LOTRRandomSkins.getRandomSkin(gondorSkins, (LOTREntityGondorMan)entity);
    }
}
