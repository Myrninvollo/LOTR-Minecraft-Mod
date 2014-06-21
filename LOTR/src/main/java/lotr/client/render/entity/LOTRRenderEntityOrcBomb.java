package lotr.client.render.entity;

import lotr.common.LOTRMod;
import lotr.common.entity.item.LOTREntityOrcBomb;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderEntityOrcBomb extends Render
{
    private RenderBlocks blockRenderer = new RenderBlocks();

    public LOTRRenderEntityOrcBomb()
    {
        shadowSize = 0.5F;
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationBlocksTexture;
    }

	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		LOTREntityOrcBomb bomb = (LOTREntityOrcBomb)entity;
		renderBomb(bomb, d, d1, d2, f1, bomb.fuse, bomb.getBombStrengthLevel(), 1F, entity.getBrightness(f1));
	}
	
	public void renderBomb(Entity entity, double d, double d1, double d2, float ticks, int fuse, int strengthLevel, float bombScale, float brightness)
	{
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glScalef(bombScale, bombScale, bombScale);

        if ((float)fuse - ticks + 1F < 10F)
        {
            float f2 = 1F - ((float)fuse - ticks + 1F) / 10F;

            if (f2 < 0F)
            {
                f2 = 0F;
            }

            if (f2 > 1F)
            {
                f2 = 1F;
            }

            f2 *= f2;
            f2 *= f2;
            float scale = 1F + f2 * 0.3F;
            GL11.glScalef(scale, scale, scale);
        }

		float f2 = (1F - ((float)fuse - ticks + 1F) / 100F) * 0.8F;
        bindEntityTexture(entity);
        blockRenderer.renderBlockAsItem(LOTRMod.orcBomb, strengthLevel, brightness);

        if (fuse / 5 % 2 == 0)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1F, 1F, 1F, f2);
            blockRenderer.renderBlockAsItem(LOTRMod.orcBomb, strengthLevel, 1F);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }
}
