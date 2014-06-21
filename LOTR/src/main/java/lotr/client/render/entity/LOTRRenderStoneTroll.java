package lotr.client.render.entity;

import lotr.client.model.LOTRModelTroll;
import lotr.common.entity.item.LOTREntityStoneTroll;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderStoneTroll extends Render
{
	private static ResourceLocation texture = new ResourceLocation("lotr:mob/troll/stone.png");
    private static LOTRModelTroll model = new LOTRModelTroll();
	private static LOTRModelTroll shirtModel = new LOTRModelTroll(1F, 0);
	private static LOTRModelTroll trousersModel = new LOTRModelTroll(0.75F, 1);

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return texture;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)d, (float)d1 + 1.5F, (float)d2);
        bindEntityTexture(entity);
        GL11.glScalef(-1F, -1F, 1F);
		GL11.glRotatef(180F - entity.rotationYaw, 0F, 1F, 0F);
        model.render(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		bindTexture(LOTRRenderTroll.trollOutfits[((LOTREntityStoneTroll)entity).getTrollOutfit()]);
		shirtModel.render(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		trousersModel.render(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
