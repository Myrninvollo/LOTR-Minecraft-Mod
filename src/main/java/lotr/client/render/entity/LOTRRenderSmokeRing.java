package lotr.client.render.entity;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelSmokeShip;
import lotr.common.entity.projectile.LOTREntitySmokeRing;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderSmokeRing extends Render
{
	private ModelBase magicShipModel = new LOTRModelSmokeShip();
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return LOTRClientProxy.particlesTexture;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int age = ((LOTREntitySmokeRing)entity).getSmokeAge();
		float transparency = (float)((200 - age) / 200F);
		int colour = ((LOTREntitySmokeRing)entity).getSmokeColour();
		if (colour == 16)
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glColor4f(1F, 1F, 1F, transparency * 0.75F);
			GL11.glScalef(0.3F, -0.3F, 0.3F);
			GL11.glRotatef((entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1) - 90F, 0F, 1F, 0F);
			GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0F, 0F, -1F);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			magicShipModel.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		else
		{
			float[] colours = new float[3];
			colours = EntitySheep.fleeceColorTable[colour];
			GL11.glColor4f(colours[0], colours[1], colours[2], transparency);
			bindEntityTexture(entity);
			drawSprite(tessellator, 0);
		}
        GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glPopMatrix();
    }

    private void drawSprite(Tessellator tessellator, int index)
    {
        float var3 = (float)(index % 16 * 16 + 0) / 128.0F;
        float var4 = (float)(index % 16 * 16 + 16) / 128.0F;
        float var5 = (float)(index / 16 * 16 + 0) / 128.0F;
        float var6 = (float)(index / 16 * 16 + 16) / 128.0F;
        float var7 = 1.0F;
        float var8 = 0.5F;
        float var9 = 0.25F;
        GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - var8), (double)(0.0F - var9), 0.0D, (double)var3, (double)var6);
        tessellator.addVertexWithUV((double)(var7 - var8), (double)(0.0F - var9), 0.0D, (double)var4, (double)var6);
        tessellator.addVertexWithUV((double)(var7 - var8), (double)(var7 - var9), 0.0D, (double)var4, (double)var5);
        tessellator.addVertexWithUV((double)(0.0F - var8), (double)(var7 - var9), 0.0D, (double)var3, (double)var5);
        tessellator.draw();
    }
}
