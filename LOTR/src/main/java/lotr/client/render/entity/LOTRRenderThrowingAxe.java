package lotr.client.render.entity;

import lotr.common.entity.projectile.LOTREntityThrowingAxe;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderThrowingAxe extends Render
{
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TextureMap.locationItemsTexture;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		LOTREntityThrowingAxe axe = (LOTREntityThrowingAxe)entity;

        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
		
		if (!axe.inGround)
		{
			GL11.glTranslatef(0F, 0.5F, 0F);
		}
		
        GL11.glRotatef((axe.prevRotationYaw + (axe.rotationYaw - axe.prevRotationYaw) * f1) - 90F, 0F, 1F, 0F);
		
		if (!axe.inGround)
		{
			GL11.glRotatef(axe.rotationPitch + (axe.inGround ? 0F :(45F * f1)), 0F, 0F, -1F);
		}
		else
		{
			GL11.glRotatef(-90F, 0F, 0F, 1F);
			GL11.glTranslatef(0F, 0.75F, 0F);
		}

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f2 = (float)axe.shake - f1;
        if (f2 > 0F)
        {
            float f3 = -MathHelper.sin(f2 * 3F) * f2;
            GL11.glRotatef(f3, 0F, 0F, 1F);
        }
		GL11.glRotatef(-135F, 0F, 0F, 1F);
		
		ItemStack axeItem = axe.getItem();
		IIcon icon = axeItem.getIconIndex();
		if (icon == null)
		{
			System.out.println("Error rendering throwing axe: no icon for " + axeItem.toString());
			GL11.glPopMatrix();
			return;
		}
		
		bindEntityTexture(entity);
		Tessellator tessellator = Tessellator.instance;
		ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		
		GL11.glPopMatrix();
    }
}
