package lotr.client.render;

import lotr.common.LOTRMod;
import lotr.common.LOTRShields;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.item.LOTRItemArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderShield
{
	private static int SHIELD_WIDTH = 32;
	private static int SHIELD_HEIGHT = 32;
	private static float MODELSCALE = 0.0625F;

	public static void renderShield(LOTRShields shield, EntityLivingBase entity, ModelBiped model)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ResourceLocation shieldTexture = shield.shieldTexture;

		ItemStack held = entity == null ? null : entity.getHeldItem();
		ItemStack heldLeft = entity instanceof LOTREntityNPC ? ((LOTREntityNPC)entity).getHeldItemLeft() : null;
		ItemStack inUse = entity instanceof EntityPlayer ? ((EntityPlayer)entity).getItemInUse() : null;
		boolean holdingSword = entity == null ? true : (held != null && (held.getItem() instanceof ItemSword || held.getItem() instanceof ItemTool) && (inUse == null || inUse.getItemUseAction() != EnumAction.bow));
		boolean blocking = holdingSword && inUse != null && inUse.getItemUseAction() == EnumAction.block;
		
		if (heldLeft != null && entity instanceof LOTREntityNPC)
		{
			LOTREntityNPC npc = (LOTREntityNPC)entity;
			if (npc.npcCape != null)
			{
				return;
			}
		}
		
		ItemStack chestplate = entity == null ? null : entity.getEquipmentInSlot(3);
		boolean wearingChestplate = chestplate != null && chestplate.getItem().isValidArmor(chestplate, ((LOTRItemArmor)LOTRMod.bodyMithril).armorType, entity);
		
		boolean renderOnBack = !holdingSword || (holdingSword && heldLeft != null);
		
		GL11.glPushMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		if (renderOnBack)
		{
			model.bipedLeftArm.postRender(MODELSCALE);
		}
		else
		{
			model.bipedBody.postRender(MODELSCALE);
		}
		
		GL11.glScalef(-1.5F, -1.5F, 1.5F);
		
		if (holdingSword)
		{
			if (blocking)
			{
				GL11.glRotatef(10F, 0F, 1F, 0F);
				GL11.glTranslatef(-0.4F, -0.9F, -0.15F);
			}
			else
			{
				GL11.glRotatef(60F, 0F, 1F, 0F);
				GL11.glTranslatef(-0.5F, -0.75F, 0F);
				if (wearingChestplate)
				{
					GL11.glTranslatef(0F, 0F, -0.22F);
				}
				else
				{
					GL11.glTranslatef(0F, 0F, -0.16F);
				}
				GL11.glRotatef(-15F, 0F, 0F, 1F);
			}
		}
		else
		{
			GL11.glTranslatef(0.5F, -0.8F, 0F);
			if (wearingChestplate)
			{
				GL11.glTranslatef(0F, 0F, 0.18F);
			}
			else
			{
				GL11.glTranslatef(0F, 0F, 0.1F);
			}
			GL11.glRotatef(180F, 0F, 1F, 0F);
		}
		
		mc.getTextureManager().bindTexture(shieldTexture);
		
		doRenderShield(0F);
		doRenderShield(0.5F);

		GL11.glPopMatrix();
	}
	
	private static void doRenderShield(float f)
	{
		float minU = 0F + f;
		float maxU = 0.5F + f;
		float minV = 0F;
		float maxV = 1F;

		int width = SHIELD_WIDTH;
		int height = SHIELD_HEIGHT;
		
		double depth1 = (double)(MODELSCALE * 0.5F * f);
		double depth2 = (double)(MODELSCALE * 0.5F * (0.5F + f));
		
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, 1F);
        tessellator.addVertexWithUV(0D, 0D, depth1, (double)maxU, (double)maxV);
        tessellator.addVertexWithUV(1D, 0D, depth1, (double)minU, (double)maxV);
        tessellator.addVertexWithUV(1D, 1D, depth1, (double)minU, (double)minV);
        tessellator.addVertexWithUV(0D, 1D, depth1, (double)maxU, (double)minV);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, -1F);
        tessellator.addVertexWithUV(0D, 1D, depth2, (double)maxU, (double)minV);
        tessellator.addVertexWithUV(1D, 1D, depth2, (double)minU, (double)minV);
        tessellator.addVertexWithUV(1D, 0D, depth2, (double)minU, (double)maxV);
        tessellator.addVertexWithUV(0D, 0D, depth2, (double)maxU, (double)maxV);
        tessellator.draw();
        float f5 = 0.5F * (maxU - minU) / (float)width;
        float f6 = 0.5F * (maxV - minV) / (float)height;
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0F, 0F);
        int k;
        float f7;
        float f8;
        float f9;

        for (k = 0; k < width; k++)
        {
            f7 = (float)k / (float)width;
            f8 = maxU + (minU - maxU) * f7 - f5;
            tessellator.addVertexWithUV((double)f7, 0D, depth2, (double)f8, (double)maxV);
            tessellator.addVertexWithUV((double)f7, 0D, depth1, (double)f8, (double)maxV);
            tessellator.addVertexWithUV((double)f7, 1D, depth1, (double)f8, (double)minV);
            tessellator.addVertexWithUV((double)f7, 1D, depth2, (double)f8, (double)minV);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1F, 0F, 0F);

        for (k = 0; k < width; k++)
        {
            f7 = (float)k / (float)width;
            f8 = maxU + (minU - maxU) * f7 - f5;
            f9 = f7 + 1F / (float)width;
            tessellator.addVertexWithUV((double)f9, 1D, depth2, (double)f8, (double)minV);
            tessellator.addVertexWithUV((double)f9, 1D, depth1, (double)f8, (double)minV);
            tessellator.addVertexWithUV((double)f9, 0D, depth1, (double)f8, (double)maxV);
            tessellator.addVertexWithUV((double)f9, 0D, depth2, (double)f8, (double)maxV);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 1F, 0F);

        for (k = 0; k < height; k++)
        {
            f7 = (float)k / (float)height;
            f8 = maxV + (minV - maxV) * f7 - f6;
            f9 = f7 + 1F / (float)height;
            tessellator.addVertexWithUV(0D, (double)f9, depth1, (double)maxU, (double)f8);
            tessellator.addVertexWithUV(1D, (double)f9, depth1, (double)minU, (double)f8);
            tessellator.addVertexWithUV(1D, (double)f9, depth2, (double)minU, (double)f8);
            tessellator.addVertexWithUV(0D, (double)f9, depth2, (double)maxU, (double)f8);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, -1F, 0F);

        for (k = 0; k < height; k++)
        {
            f7 = (float)k / (float)height;
            f8 = maxV + (minV - maxV) * f7 - f6;
            tessellator.addVertexWithUV(1D, (double)f7, depth1, (double)minU, (double)f8);
            tessellator.addVertexWithUV(0D, (double)f7, depth1, (double)maxU, (double)f8);
            tessellator.addVertexWithUV(0D, (double)f7, depth2, (double)maxU, (double)f8);
            tessellator.addVertexWithUV(1D, (double)f7, depth2, (double)minU, (double)f8);
        }

        tessellator.draw();
	}
}
