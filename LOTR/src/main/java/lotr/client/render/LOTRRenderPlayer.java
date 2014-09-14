package lotr.client.render;

import lotr.client.LOTRClientProxy;
import lotr.client.LOTRTickHandlerClient;
import lotr.common.*;
import lotr.common.world.LOTRWorldProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LOTRRenderPlayer
{
	private Minecraft mc = Minecraft.getMinecraft();
	private RenderManager renderManager = RenderManager.instance;
	
	public LOTRRenderPlayer()
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void preRenderSpecials(RenderPlayerEvent.Specials.Pre event)
	{
		EntityPlayer entityplayer = event.entityPlayer;
		float tick = event.partialRenderTick;
		
		if (!entityplayer.isInvisibleToPlayer(mc.thePlayer))
		{
			LOTRShields shield = LOTRLevelData.getData(entityplayer).getShield();
			if (shield != null && LOTRLevelData.getData(entityplayer).getEnableShield())
			{
				LOTRRenderShield.renderShield(shield, entityplayer, event.renderer.modelBipedMain);
			}
		}
	}
	
	@SubscribeEvent
	public void postRender(RenderPlayerEvent.Post event)
	{
		EntityPlayer entityplayer = event.entityPlayer;
		float tick = event.partialRenderTick;
		
		if (shouldRenderAlignment(entityplayer) && (mc.theWorld.provider instanceof LOTRWorldProvider || LOTRMod.alwaysShowAlignment))
		{
			int alignment = LOTRLevelData.getData(entityplayer).getAlignment(LOTRTickHandlerClient.currentAlignmentFaction);
            double dist = entityplayer.getDistanceSqToEntity(renderManager.livingPlayer);
            float range = RendererLivingEntity.NAME_TAG_RANGE;
            if (dist < (double)(range * range))
            {
				float yOffset = entityplayer.isPlayerSleeping() ? -1.5F : 0F;
				FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
				GL11.glPushMatrix();
				float f = (float)entityplayer.lastTickPosX + (float)(entityplayer.posX - entityplayer.lastTickPosX) * tick;
				float f1 = (float)entityplayer.lastTickPosY + (float)(entityplayer.posY - entityplayer.lastTickPosY) * tick;
				float f2 = (float)entityplayer.lastTickPosZ + (float)(entityplayer.posZ - entityplayer.lastTickPosZ) * tick;
				GL11.glTranslatef(f - (float)RenderManager.renderPosX, f1 - (float)RenderManager.renderPosY, f2 - (float)RenderManager.renderPosZ);
				GL11.glTranslatef(0F, entityplayer.height + 0.6F + yOffset, 0F);
				GL11.glNormal3f(0F, 1F, 0F);
				GL11.glRotatef(-renderManager.playerViewY, 0F, 1F, 0F);
				GL11.glRotatef(renderManager.playerViewX, 1F, 0F, 0F);
				GL11.glScalef(-1F, -1F, 1F);
				float scale = 0.025F;
				GL11.glScalef(scale, scale, scale);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDepthMask(false);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(1F, 1F, 1F, 1F);

				String s = Integer.valueOf(alignment).toString();
				if (alignment != 0 && !s.startsWith("-"))
				{
					s = "+" + s;
				}
				
				mc.getTextureManager().bindTexture(LOTRClientProxy.alignmentTexture);
				LOTRTickHandlerClient.drawTexturedModalRect(-MathHelper.floor_double((fr.getStringWidth(s) + 18) / 2D), -19, 0, 18, 16, 16);
				LOTRTickHandlerClient.drawTextWithShadow(fr, 18 - MathHelper.floor_double((fr.getStringWidth(s) + 18) / 2D), -12, s, 1F);
				
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				GL11.glPopMatrix();
			}
		}
	}
	
	private boolean shouldRenderAlignment(EntityPlayer entityplayer)
	{
		if (!Minecraft.isGuiEnabled())
		{
			return false;
		}
		if (entityplayer == renderManager.livingPlayer)
		{
			return false;
		}
		if (entityplayer.isInvisibleToPlayer(mc.thePlayer))
		{
			return false;
		}
		if (entityplayer.isSneaking())
		{
			return false;
		}
		if (!LOTRMod.displayAlignmentAboveHead)
		{
			return false;
		}
		return !LOTRLevelData.getData(entityplayer).getHideAlignment();
	}
}
