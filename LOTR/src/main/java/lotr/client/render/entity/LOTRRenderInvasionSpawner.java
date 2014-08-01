package lotr.client.render.entity;

import lotr.common.LOTRFaction;
import lotr.common.entity.LOTREntityInvasionSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderInvasionSpawner extends Render
{
	private ItemStack theItemStack;
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationItemsTexture;
    }
	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		if (theItemStack == null)
		{
			theItemStack = new ItemStack(Items.iron_sword);
		}
		
		LOTREntityInvasionSpawner spawner = (LOTREntityInvasionSpawner)entity;
        bindEntityTexture(spawner);
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		float rotation = interpolateRotation(spawner.prevRotationYaw, spawner.rotationYaw, f1);
		float scale = 1.5F;
		GL11.glRotatef(rotation, 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F * scale, 0F, 0.03125F * scale);
		GL11.glScalef(scale, scale, scale);
		IIcon icon = theItemStack.getIconIndex();
        if (icon == null)
        {
            icon = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture)).getAtlasSprite("missingno");
        }
		Tessellator tessellator = Tessellator.instance;
        float f2 = icon.getMinU();
        float f3 = icon.getMaxU();
        float f4 = icon.getMinV();
        float f5 = icon.getMaxV();
		ItemRenderer.renderItemIn2D(tessellator, f3, f4, f2, f5, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
	
    private float interpolateRotation(float prevRotation, float newRotation, float tick)
    {
        float interval;
        for (interval = newRotation - prevRotation; interval < -180F; interval += 360F) {}

        while (interval >= 180F)
        {
            interval -= 360F;
        }

        return prevRotation + tick * interval;
    }
}
