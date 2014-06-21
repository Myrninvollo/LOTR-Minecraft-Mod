package lotr.client.render;

import lotr.client.LOTRClientProxy;
import lotr.client.LOTRTickHandlerClient;
import lotr.common.LOTRCapes;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.item.LOTREntityBarrel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
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
	private ModelBiped playerModel = new ModelBiped(0F);
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
		
		if (!entityplayer.isInvisibleToPlayer(mc.thePlayer) && !entityplayer.getHideCape() && event.renderCape)
		{
			LOTRCapes cape = LOTRLevelData.getCape(entityplayer);
			if (cape != null && LOTRLevelData.getEnableCape(entityplayer))
			{
				mc.getTextureManager().bindTexture(cape.capeTexture);
				GL11.glPushMatrix();
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glTranslatef(0F, 0F, 0.125F);
				double d = entityplayer.field_71091_bM + (entityplayer.field_71094_bP - entityplayer.field_71091_bM) * (double)tick - (entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)tick);
				double d1 = entityplayer.field_71096_bN + (entityplayer.field_71095_bQ - entityplayer.field_71096_bN) * (double)tick - (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)tick);
				double d2 = entityplayer.field_71097_bO + (entityplayer.field_71085_bR - entityplayer.field_71097_bO) * (double)tick - (entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)tick);
				float f6 = entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * tick;
				double d3 = (double)MathHelper.sin(f6 * (float)Math.PI / 180F);
				double d4 = (double)(-MathHelper.cos(f6 * (float)Math.PI / 180F));
				float f7 = (float)d1 * 10F;

				if (f7 < -6F)
				{
					f7 = -6F;
				}

				if (f7 > 32F)
				{
					f7 = 32F;
				}

				float f8 = (float)(d * d3 + d2 * d4) * 100F;
				float f9 = (float)(d * d4 - d2 * d3) * 100F;

				if (f8 < 0F)
				{
					f8 = 0F;
				}

				float f10 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * tick;
				f7 += MathHelper.sin((entityplayer.prevDistanceWalkedModified + (entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified) * tick) * 6F) * 32F * f10;

				if (entityplayer.isSneaking())
				{
					f7 += 25F;
				}

				GL11.glRotatef(6F + f8 / 2F + f7, 1F, 0F, 0F);
				GL11.glRotatef(f9 / 2F, 0F, 0F, 1F);
				GL11.glRotatef(-f9 / 2F, 0F, 1F, 0F);
				GL11.glRotatef(180F, 0F, 1F, 0F);
				playerModel.renderCloak(0.0625F);
				GL11.glPopMatrix();
				
				if (cape == LOTRCapes.ELVEN_CONTEST)
				{
					if ((entityplayer.isSprinting() || (!entityplayer.onGround && (Math.abs(entityplayer.motionX) > 0D || Math.abs(entityplayer.motionY) > 0D || Math.abs(entityplayer.motionZ) > 0D))) && entityplayer.getRNG().nextInt(4) == 0)
					{
						double d10 = entityplayer.posX + (entityplayer.getRNG().nextDouble() - 0.5D) * (double)entityplayer.width;
						double d11 = entityplayer.boundingBox.minY + ((double)entityplayer.height * 0.5D);
						double d12 = entityplayer.posZ + (entityplayer.getRNG().nextDouble() - 0.5D) * (double)entityplayer.width;
						double d13 = entityplayer.motionX * -0.6D;
						double d14 = entityplayer.motionY * -0.6D;
						double d15 = entityplayer.motionZ * -0.6D;
						LOTRMod.proxy.spawnParticle("leafGold_" + (40 + entityplayer.getRNG().nextInt(20)), d10, d11, d12, d13, d14, d15);
					}
				}
				
				event.renderCape = false;
			}
		}
	}
	
	@SubscribeEvent
	public void postRender(RenderPlayerEvent.Post event)
	{
		EntityPlayer entityplayer = event.entityPlayer;
		float tick = event.partialRenderTick;
		
		if (shouldRenderAlignment(entityplayer) && (mc.theWorld.provider.dimensionId == LOTRMod.idDimension || LOTRMod.alwaysShowAlignment))
		{
			int alignment = LOTRLevelData.getAlignment(entityplayer, LOTRTickHandlerClient.currentAlignmentFaction);
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
		return !LOTRLevelData.getHideAlignment(entityplayer);
	}
}
