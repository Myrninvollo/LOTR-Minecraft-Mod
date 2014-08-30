package lotr.client.render.item;

import java.util.List;

import lotr.client.LOTRClientProxy;
import lotr.common.entity.npc.LOTREntityOrc;
import lotr.common.item.LOTRItemSword;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderElvenBlade implements IItemRenderer
{
	private double distance;
	
	public LOTRRenderElvenBlade(double d)
	{
		distance = d;
	}
	
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
		boolean glows = false;
		IIcon icon = itemstack.getItem().getIconFromDamage(0);
		EntityLivingBase entity = (EntityLivingBase)data[1];
		
		entity.worldObj.theProfiler.startSection("elvenBlade");
		
		List orcs = entity.worldObj.getEntitiesWithinAABB(LOTREntityOrc.class, entity.boundingBox.expand(distance, distance, distance));
		if (!orcs.isEmpty())
		{
			IIcon glowingIcon = ((LOTRItemSword)itemstack.getItem()).glowingIcon;
			if (glowingIcon != null)
			{
				icon = glowingIcon;
				glows = true;
			}
		}
		
		Tessellator tessellator = Tessellator.instance;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		
		if (glows)
		{
			GL11.glDisable(GL11.GL_LIGHTING);
		}
		
		ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
		
		if (itemstack != null && itemstack.hasEffect(0))
		{
			renderEffect();
		}
		
		if (glows)
		{
			GL11.glEnable(GL11.GL_LIGHTING);
			for (int i = 0; i < 4; i++)
			{
				renderEffect();
			}
		}
		
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		entity.worldObj.theProfiler.endSection();
	}
	
	private void renderEffect()
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glDepthFunc(GL11.GL_EQUAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		Minecraft.getMinecraft().getTextureManager().bindTexture(LOTRClientProxy.enchantmentTexture);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
		float var14 = 0.76F;
		GL11.glColor4f(0.25F * var14, 0.4F * var14, 0.9F * var14, 1F);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		float var15 = 0.125F;
		GL11.glScalef(var15, var15, var15);
		float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000F * 8F;
		GL11.glTranslatef(var16, 0F, 0F);
		GL11.glRotatef(-50F, 0F, 0F, 1F);
		ItemRenderer.renderItemIn2D(tessellator, 0F, 0F, 1F, 1F, 256, 256, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glScalef(var15, var15, var15);
		var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873F * 8F;
		GL11.glTranslatef(-var16, 0F, 0F);
		GL11.glRotatef(10F, 0F, 0F, 1F);
		ItemRenderer.renderItemIn2D(tessellator, 0F, 0F, 1F, 1F, 256, 256, 0.0625F);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
}
