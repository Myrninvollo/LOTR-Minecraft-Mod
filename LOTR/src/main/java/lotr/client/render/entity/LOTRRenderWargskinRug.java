package lotr.client.render.entity;

import java.util.HashMap;
import java.util.Map;

import lotr.client.model.LOTRModelWargskinRug;
import lotr.common.entity.item.LOTREntityWargskinRug;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderWargskinRug extends Render
{
	private static Map rugSkins = new HashMap();
    private static LOTRModelWargskinRug model = new LOTRModelWargskinRug();
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        LOTREntityWargskinRug rug = (LOTREntityWargskinRug)entity;
		String s = String.valueOf(rug.getRugType());
		ResourceLocation r = (ResourceLocation)rugSkins.get(s);
		if (r == null)
		{
			r = new ResourceLocation("lotr:item/wargskinRug_" + s + ".png");
			rugSkins.put(s, r);
		}
		return r;
    }

	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)d, (float)d1 + 1.5F, (float)d2);
        bindEntityTexture(entity);
        GL11.glScalef(-1F, -1F, 1F);
		GL11.glRotatef(180F - entity.rotationYaw, 0F, 1F, 0F);
        model.render(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
