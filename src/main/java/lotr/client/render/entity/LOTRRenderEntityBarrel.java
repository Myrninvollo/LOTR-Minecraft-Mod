package lotr.client.render.entity;

import lotr.common.LOTRMod;
import lotr.common.entity.item.LOTREntityBarrel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderEntityBarrel extends Render
{
	private ItemStack barrelItem = new ItemStack(LOTRMod.barrel);
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationBlocksTexture;
    }

	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		LOTREntityBarrel barrel = (LOTREntityBarrel)entity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1 + 0.5F, (float)d2);
        GL11.glRotatef(180F - f, 0F, 1F, 0F);
        float f2 = (float)barrel.getTimeSinceHit() - f1;
        float f3 = barrel.getDamageTaken() - f1;

        if (f3 < 0F)
        {
            f3 = 0F;
        }

        if (f2 > 0F)
        {
            GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10F * (float)barrel.getForwardDirection(), 1F, 0F, 0F);
        }

        bindEntityTexture(barrel);
        GL11.glScalef(1.5F, 1.5F, 1.5F);
        renderManager.itemRenderer.renderItem(renderManager.livingPlayer, barrelItem, 0);
        GL11.glPopMatrix();
    }
}
