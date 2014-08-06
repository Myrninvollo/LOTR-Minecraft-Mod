package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class LOTRModelElk extends ModelBase
{
	private ModelRenderer horn;
	private ModelRenderer backhorn;
	private ModelRenderer leftear;
	private ModelRenderer rightear;
	private ModelRenderer head;
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;

	public LOTRModelElk()
	{
		this(0F);
	}
	
	public LOTRModelElk(float f)
	{
		horn = new ModelRenderer(this, 56, 0).setTextureSize(64, 64);
		horn.addBox(-1F, -9F, -4.5F, 2, 6, 2, f);
		horn.setRotationPoint(0F, 9F, -11F);
			
		backhorn = new ModelRenderer(this, 60, 8).setTextureSize(64, 64);
		backhorn.addBox(-0.5F, -6.5F, 0F, 1, 3, 1, f);
		backhorn.setRotationPoint(0F, 9F, -11F);
			 
		leftear = new ModelRenderer(this, 44, 0).setTextureSize(64, 64);
		leftear.addBox(-4.6F, -6F, 3F, 1, 3, 2, f);
		leftear.setRotationPoint(0F, 9F, -11F);
			 
		rightear = new ModelRenderer(this, 50, 0).setTextureSize(64, 64);
		rightear.addBox(3.6F, -6F, 3F, 1, 3, 2, f);
		rightear.setRotationPoint(0F, 9F, -11F);
		
		head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		head.addBox(-4F, -4F, -6F, 8, 8, 12, f);
		head.setRotationPoint(0F, 9F, -11F);
			
		body = new ModelRenderer(this, 0, 32).setTextureSize(64, 64);
		body.addBox(-5.466667F, -10F, -8F, 13, 22, 10, f);
		body.setRotationPoint(-1F, 9F, 1F);
			
		leg1 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
		leg1.addBox(-3F, 0F, -2F, 4, 8, 4, f);
		leg1.setRotationPoint(-3F, 16F, 10F);
		 
		leg2 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
		leg2.addBox(-1F, 0F, -2F, 4, 8, 4, f);
		leg2.setRotationPoint(3F, 16F, 10F);
		
		leg3 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
		leg3.addBox(-3F, 0F, -3F, 4, 8, 4, f);
		leg3.setRotationPoint(-3F, 16F, -5F);
			
		leg4 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
		leg4.addBox(-1F, 0F, -3F, 4, 8, 4, f);
		leg4.setRotationPoint(3F, 16F, -5F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (isChild)
		{
			float f6 = 2F;
			GL11.glPushMatrix();
			GL11.glTranslatef(0F, 8F * f5, 4F * f5);
			head.render(f5);
			leftear.render(f5);
			rightear.render(f5);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1F / f6, 1F / f6, 1F / f6);
			GL11.glTranslatef(0F, 24F * f5, 0F);
			body.render(f5);
			leg1.render(f5);
			leg2.render(f5);
			leg3.render(f5);
			leg4.render(f5);
			GL11.glPopMatrix();
		}
		else
		{
			head.render(f5);
			horn.render(f5);
			backhorn.render(f5);
			leftear.render(f5);
			rightear.render(f5);
			body.render(f5);
			leg1.render(f5);
			leg2.render(f5);
			leg3.render(f5);
			leg4.render(f5);
		}
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		head.rotateAngleX = (f4 / 57.29578F) + 0.279256F;
		head.rotateAngleY = f3 / 57.29578F;
		
		horn.rotateAngleX = head.rotateAngleX;
		horn.rotateAngleY = head.rotateAngleY;
		backhorn.rotateAngleX = head.rotateAngleX;
		backhorn.rotateAngleY = head.rotateAngleY;
		leftear.rotateAngleX = head.rotateAngleX;
		leftear.rotateAngleY = head.rotateAngleY;
		rightear.rotateAngleX = head.rotateAngleX;
		rightear.rotateAngleY = head.rotateAngleY;
		
		body.rotateAngleX = 1.570796F;
		
		leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
	}
}
