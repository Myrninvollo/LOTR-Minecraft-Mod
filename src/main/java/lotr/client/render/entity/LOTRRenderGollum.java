package lotr.client.render.entity;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelGollum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderGollum extends RenderLiving
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/gollum.png");
	
    public LOTRRenderGollum()
    {
        super(new LOTRModelGollum(), 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return skin;
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(0.85F, 0.85F, 0.85F);
	}
	
	@Override
	public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1)
	{
		super.doRender(entity, d, d1, d2, f, f1);
		if (Minecraft.isGuiEnabled())
		{
			func_147906_a(entity, "Gollum", d, d1 + 0.5D, d2, 64);
			LOTRClientProxy.renderHealthBar(entity, d, d1 + 0.5D, d2, 64, renderManager);
		}
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase entity, float f)
	{
        GL11.glColor3f(1F, 1F, 1F);
        ItemStack heldItem = entity.getHeldItem();

        if (heldItem != null && heldItem.getItem() == Items.fish)
        {
            GL11.glPushMatrix();
			
            ((LOTRModelGollum)mainModel).head.postRender(0.0625F);
            GL11.glTranslatef(0.21875F, 0.03125F, -0.375F);
			float f1 = 0.375F;
			GL11.glScalef(f1, f1, f1);
			GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-50.0F, 1.0F, 0.0F, 0.0F);
			
            renderManager.itemRenderer.renderItem(entity, heldItem, 0);

            GL11.glPopMatrix();
        }
	}
}
