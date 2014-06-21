package lotr.client.model;

import lotr.common.entity.npc.LOTREntityMarshWraith;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRModelMarshWraith extends ModelBase
{	
	private ModelRenderer head;
	private ModelRenderer headwear;
	private ModelRenderer body;
	private ModelRenderer rightArm;
	private ModelRenderer leftArm;
	private ModelRenderer cape;
	
	public LOTRModelMarshWraith()
	{
		textureHeight = 64;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8);
		head.setRotationPoint(0F, 0F, 0F);
		
		headwear = new ModelRenderer(this, 32, 0);
		headwear.addBox(-4F, -8F, -4F, 8, 8, 8, 0.5F);
		headwear.setRotationPoint(0F, 0F, 0F);
		
		body = new ModelRenderer(this, 0, 16);
		body.addBox(-4F, 0F, -2F, 8, 24, 4);
		body.setRotationPoint(0F, 0F, 0F);
		
		rightArm = new ModelRenderer(this, 46, 16);
		rightArm.addBox(-3F, -2F, -2F, 4, 12, 4);
		rightArm.setRotationPoint(-5F, 2F, 0F);
		
		leftArm = new ModelRenderer(this, 46, 16);
		leftArm.mirror = true;
		leftArm.addBox(-1F, -2F, -2F, 4, 12, 4);
		leftArm.setRotationPoint(5F, 2F, 0F);
		
		cape = new ModelRenderer(this, 24, 16);
		cape.addBox(-5F, 1F, 3F, 10, 16, 1);
		cape.setRotationPoint(0F, 0F, 0F);
		cape.rotateAngleX = 0.1F;
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		if (entity instanceof LOTREntityMarshWraith)
		{
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			LOTREntityMarshWraith wraith = (LOTREntityMarshWraith)entity;
			if (wraith.getSpawnFadeTime() < 30)
			{
				GL11.glColor4f(1F, 1F, 1F, (float)wraith.getSpawnFadeTime() / 30F);
			}
			else if (wraith.getDeathFadeTime() > 0)
			{
				GL11.glColor4f(1F, 1F, 1F, (float)wraith.getDeathFadeTime() / 30F);
			}
			GL11.glTranslatef(0F, (float)-LOTREntityMarshWraith.VERTICAL_OFFSET, 0F);
		}
		
		head.render(f5);
		headwear.render(f5);
		body.render(f5);
		rightArm.render(f5);
		leftArm.render(f5);
		cape.render(f5);
		
		if (entity instanceof LOTREntityMarshWraith)
		{
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glColor4f(1F, 1F, 1F, 1F);
		}
	}
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        head.rotateAngleY = f3 / (180F / (float)Math.PI);
        head.rotateAngleX = (f4 + ((float)LOTREntityMarshWraith.VERTICAL_OFFSET * 10F)) / (180F / (float)Math.PI);
        headwear.rotateAngleY = head.rotateAngleY;
        headwear.rotateAngleX = head.rotateAngleX;
    }
}
