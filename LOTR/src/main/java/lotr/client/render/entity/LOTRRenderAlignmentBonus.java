package lotr.client.render.entity;

import lotr.client.LOTRClientProxy;
import lotr.client.LOTRTickHandlerClient;
import lotr.client.fx.LOTREntityAlignmentBonus;
import lotr.common.LOTRFaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderAlignmentBonus extends Render
{
	public LOTRRenderAlignmentBonus()
	{
		shadowSize = 0F;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return LOTRClientProxy.alignmentTexture;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		Minecraft mc = Minecraft.getMinecraft();
		LOTREntityAlignmentBonus alignmentBonus = (LOTREntityAlignmentBonus)entity;
		LOTRFaction viewingFaction = LOTRTickHandlerClient.currentAlignmentFaction;
		int bonus = alignmentBonus.bonus;
		
		if (alignmentBonus.faction != viewingFaction)
		{
			return;
		}

		FontRenderer fr = mc.fontRenderer;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-0.025F, -0.025F, 0.025F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		int age = alignmentBonus.particleAge;
		float transparency = age < 60 ? 1F : (float)(80 - age) / 20F;
		GL11.glColor4f(1F, 1F, 1F, transparency);

		String s = String.valueOf(bonus);
		if (!s.startsWith("-"))
		{
			s = "+" + s;
		}
		String s1 = alignmentBonus.name;
		
		bindEntityTexture(entity);
		LOTRTickHandlerClient.drawTexturedModalRect(-MathHelper.floor_double((fr.getStringWidth(s) + 18) / 2D), -19, 0, 18, 16, 16);
		
		LOTRTickHandlerClient.drawTextWithShadow(fr, 18 - MathHelper.floor_double((fr.getStringWidth(s) + 18) / 2D), -12, s, transparency);
		
		LOTRTickHandlerClient.drawTextWithShadow(fr, -MathHelper.floor_double(fr.getStringWidth(s1) / 2D), 2, s1, transparency);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}