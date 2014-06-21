package lotr.client.fx;

import java.util.ArrayList;
import java.util.List;

import lotr.client.LOTRClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class LOTREffectRenderer
{
    private Minecraft mc;
    private World worldObj;
    private List particles = new ArrayList();

    public LOTREffectRenderer(Minecraft minecraft)
    {
    	mc = minecraft;
    }

    public void addEffect(EntityFX entityfx)
    {
        if (particles.size() >= 4000)
        {
            particles.remove(0);
        }
        particles.add(entityfx);
    }

    public void updateEffects()
    {
		for (int i = 0; i < particles.size(); i++)
		{
			EntityFX entityfx = (EntityFX)particles.get(i);

			if (entityfx != null)
			{
				entityfx.onUpdate();
			}

			if (entityfx == null || entityfx.isDead)
			{
				particles.remove(i--);
			}
		}
    }

    public void renderParticles(Entity entity, float f)
    {
        float f1 = ActiveRenderInfo.rotationX;
        float f2 = ActiveRenderInfo.rotationZ;
        float f3 = ActiveRenderInfo.rotationYZ;
        float f4 = ActiveRenderInfo.rotationXY;
        float f5 = ActiveRenderInfo.rotationXZ;
        EntityFX.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        EntityFX.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        EntityFX.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;

		if (!particles.isEmpty())
		{
			mc.getTextureManager().bindTexture(LOTRClientProxy.particlesTexture);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			for (int i = 0; i < particles.size(); i++)
			{
				EntityFX entityfx = (EntityFX)particles.get(i);
				if (entityfx == null)
				{
					continue;
				}
				tessellator.setBrightness(entityfx.getBrightnessForRender(f));
				entityfx.renderParticle(tessellator, f, f1, f5, f2, f3, f4);
			}
			tessellator.draw();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		}
    }

    public void clearEffectsAndSetWorld(World world)
    {
        worldObj = world;
		particles.clear();
    }
}
