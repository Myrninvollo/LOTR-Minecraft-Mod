package lotr.client.render.entity;

import lotr.client.model.LOTRModelTroll;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderOlogHai extends LOTRRenderTroll
{
	private static ResourceLocation ologHaiOutfit = new ResourceLocation("lotr:mob/troll/ologHai.png");
	
    public LOTRRenderOlogHai()
    {
        super();
    }
	
	@Override
	protected void renderTrollWeapon(EntityLivingBase entityliving, float f)
	{
		((LOTRModelTroll)mainModel).renderWarhammer(0.0625F);
	}

	@Override
	protected void bindTrollOutfitTexture(EntityLivingBase entityliving)
	{
		bindTexture(ologHaiOutfit);
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(1.25F, 1.25F, 1.25F);
	}
}
