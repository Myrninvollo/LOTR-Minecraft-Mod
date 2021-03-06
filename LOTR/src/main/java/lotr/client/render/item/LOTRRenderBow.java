package lotr.client.render.item;

import lotr.client.LOTRClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderBow implements IItemRenderer
{
	@Override
    public boolean handleRenderType(ItemStack itemstack, ItemRenderType type)
	{
		return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}
    
	@Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemstack, ItemRendererHelper helper)
	{
		return false;
	}
    
	@Override
    public void renderItem(ItemRenderType type, ItemStack itemstack, Object... data)
	{
		if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0 || data[1] != Minecraft.getMinecraft().thePlayer)
		{
			GL11.glTranslatef(0.9375F, 0.0625F, 0.0F);
			GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1F / 1.5F, 1F / 1.5F, 1F / 1.5F);
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);
			
			GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
			GL11.glScalef(1F / 0.375F, 1F / 0.375F, 1F / 0.375F);
			GL11.glTranslatef(-0.25F, -0.1875F, 0.1875F);
			
			GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
			GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(0.625F, -0.625F, 0.625F);
			GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			
			GL11.glTranslatef(0.0F, -0.3F, 0.0F);
			GL11.glScalef(1.5F, 1.5F, 1.5F);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
		}
		
		IIcon icon = ((EntityLivingBase)data[1]).getItemIcon(itemstack, 0);

		if (icon == null)
		{
			GL11.glPopMatrix();
			return;
		}

		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		Tessellator tessellator = Tessellator.instance;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
		
		if (itemstack != null && itemstack.hasEffect(0))
		{
			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDisable(GL11.GL_LIGHTING);
			texturemanager.bindTexture(LOTRClientProxy.enchantmentTexture);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
			float var14 = 0.76F;
			GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glPushMatrix();
			float var15 = 0.125F;
			GL11.glScalef(var15, var15, var15);
			float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
			GL11.glTranslatef(var16, 0.0F, 0.0F);
			GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
			ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(var15, var15, var15);
			var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
			GL11.glTranslatef(-var16, 0.0F, 0.0F);
			GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
			ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
