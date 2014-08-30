package lotr.client.render.entity;

import java.awt.Color;
import java.util.Random;

import lotr.client.model.LOTRModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderSaruman extends LOTRRenderBiped
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/char/saruman.png");
	private Random rand = new Random();
	private boolean twitch;
	
	public LOTRRenderSaruman()
	{
		super(new LOTRModelBiped(), 0.5F);
	}
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
    {
		return skin;
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		if (entity.ticksExisted % 60 == 0)
		{
			twitch = !twitch;
		}
			
		if (twitch)
		{
			GL11.glRotatef(rand.nextFloat() * 40F, 0F, 0F, 1F);
			GL11.glRotatef(rand.nextFloat() * 40F, 0F, 1F, 0F);
			GL11.glRotatef(rand.nextFloat() * 40F, 1F, 0F, 0F);
			GL11.glTranslatef(rand.nextFloat() * 0.5F, rand.nextFloat() * 0.5F, rand.nextFloat() * 0.5F);
		}
		
		int i = entity.ticksExisted % 360;
		float hue = (float)i / 360F;
		Color color = Color.getHSBColor(hue, 1F, 1F);
		float r = (float)color.getRed() / 255F;
		float g = (float)color.getGreen() / 255F;
		float b = (float)color.getBlue() / 255F;
		GL11.glColor3f(r, g, b);
	}
}
