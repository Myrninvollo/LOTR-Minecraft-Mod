package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelTroll;
import lotr.common.entity.npc.LOTREntityOlogHai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderOlogHai extends LOTRRenderTroll
{
	private static List ologSkins;
	private static List ologArmorSkins;
	
    public LOTRRenderOlogHai()
    {
        super();
        ologSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/troll/ologHai");
        ologArmorSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/troll/ologHai_armor");
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return LOTRRandomSkins.getRandomSkin(ologSkins, (LOTREntityOlogHai)entity);
    }
	
	@Override
	protected void renderTrollWeapon(EntityLivingBase entity, float f)
	{
		((LOTRModelTroll)mainModel).renderWarhammer(0.0625F);
	}

	@Override
	protected void bindTrollOutfitTexture(EntityLivingBase entity)
	{
		bindTexture(LOTRRandomSkins.getRandomSkin(ologArmorSkins, (LOTREntityOlogHai)entity));
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(1.25F, 1.25F, 1.25F);
	}
}
