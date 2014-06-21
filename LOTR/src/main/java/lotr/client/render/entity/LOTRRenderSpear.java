package lotr.client.render.entity;

import lotr.client.model.LOTRModelBoar;
import lotr.common.LOTRMod;
import lotr.common.entity.projectile.LOTREntitySpear;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderSpear extends Render
{
	private static ModelBase boarModel = new LOTRModelBoar();
	static
	{
		boarModel.isChild = false;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TextureMap.locationItemsTexture;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		LOTREntitySpear spear = (LOTREntitySpear)entity;

        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef((spear.prevRotationYaw + (spear.rotationYaw - spear.prevRotationYaw) * f1) - 90F, 0F, 1F, 0F);
        GL11.glRotatef(spear.prevRotationPitch + (spear.rotationPitch - spear.prevRotationPitch) * f1, 0F, 0F, 1F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f2 = (float)spear.shake - f1;
        if (f2 > 0F)
        {
            float f3 = -MathHelper.sin(f2 * 3F) * f2;
            GL11.glRotatef(f3, 0F, 0F, 1F);
        }
		GL11.glRotatef(-135F, 0F, 0F, 1F);
		GL11.glTranslatef(0F, -1F, 0F);
		
		if (LOTRMod.isAprilFools())
		{
			bindTexture(LOTRRenderWildBoar.boarSkin);
			GL11.glTranslatef(0F, -2.5F, 0F);
			GL11.glScalef(0.25F, 0.25F, 0.25F);
			boarModel.render(null, 0F, 0F, 0F, 0F, 0F, 0.625F);
		}
		else
		{
			ItemStack itemstack = new ItemStack(Item.getItemById(spear.getItemID()), 1, 0);
			IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, IItemRenderer.ItemRenderType.EQUIPPED);
			customRenderer.renderItem(IItemRenderer.ItemRenderType.EQUIPPED, itemstack, new Object[2]);
		}

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}