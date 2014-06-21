package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class LOTRModelGemsbok extends ModelBase
{
	private ModelRenderer head;
	private ModelRenderer tail;
	private ModelRenderer earLeft;
	private ModelRenderer earRight;
	private ModelRenderer neck;
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
	private ModelRenderer leftHorn;
	private ModelRenderer rightHorn;

	public LOTRModelGemsbok()
	{	
		head = new ModelRenderer(this, 28, 0).setTextureSize(128, 64);
		head.addBox(-3F, -10F, -6F, 6, 7, 12);
		head.setRotationPoint(0F, 4F, -9F);
		 
		tail = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
		tail.addBox(0F, 0F, 0F, 2, 12, 2);
		tail.setRotationPoint(-1F, 3F, 11F);
		 
		earLeft = new ModelRenderer(this, 28, 19).setTextureSize(128, 64);
		earLeft.addBox(-3.8F, -12F, 3F, 1, 3, 2);
		earLeft.setRotationPoint(0F, 4F, -9F);
		
		earRight = new ModelRenderer(this, 34, 19).setTextureSize(128, 64);
		earRight.addBox(2.8F, -12F, 3F, 1, 3, 2);
		earRight.setRotationPoint(0F, 4F, -9F);
		 
		neck = new ModelRenderer(this, 0, 14).setTextureSize(128, 64);
		neck.addBox(-2.5F, -6F, -5F, 5, 8, 9);
		neck.setRotationPoint(0F, 4F, -9F);
		
		body = new ModelRenderer(this, 0, 31).setTextureSize(128, 64);
		body.addBox(-7F, -10F, -7F, 13, 10, 23);
		body.setRotationPoint(0.5F, 12F, -3F);
		
		leg1 = new ModelRenderer(this, 0, 38).setTextureSize(128, 64);
		leg1.addBox(-2F, 0F, -2F, 4, 12, 4);
		leg1.setRotationPoint(-4F, 12F, 10F);
		
		leg2 = new ModelRenderer(this, 0, 38).setTextureSize(128, 64);
		leg2.addBox(-2F, 0F, -2F, 4, 12, 4);
		leg2.setRotationPoint(4F, 12F, 10F);
		
		leg3 = new ModelRenderer(this, 0, 38).setTextureSize(128, 64);
		leg3.addBox(-2F, 0F, -3F, 4, 12, 4);
		leg3.setRotationPoint(-4F, 12F, -7F);
		
		leg4 = new ModelRenderer(this, 0, 38).setTextureSize(128, 64);
		leg4.addBox(-2F, 0F, -3F, 4, 12, 4);
		leg4.setRotationPoint(4F, 12F, -7F);
		
		leftHorn = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
		leftHorn.addBox(-2.8F, -9.5F, 5.8F, 1, 1, 13);
		leftHorn.setRotationPoint(0F, 4F, -9F);
		
		rightHorn = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
		rightHorn.addBox(1.8F, -9.5F, 5.8F, 1, 1, 13);
		rightHorn.setRotationPoint(0F, 4F, -9F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (isChild)
		{
			float f6 = 2f;
			GL11.glPushMatrix();
			GL11.glTranslatef(0F, 8F * f5, 4F * f5);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1F / f6);
			GL11.glTranslatef(0F, 24F * f5, 0F);
			head.render(f5);
			earLeft.render(f5);
			earRight.render(f5);
			neck.render(f5);
			body.render(f5);
			leg1.render(f5);
			leg2.render(f5);
			leg3.render(f5);
			leg4.render(f5);
			tail.render(f5);
			GL11.glPopMatrix();
		}
		else
		{
			head.render(f5);
			neck.render(f5);
			leftHorn.render(f5);
			rightHorn.render(f5);
			earLeft.render(f5);
			earRight.render(f5);
			body.render(f5);
			leg1.render(f5);
			leg2.render(f5);
			leg3.render(f5);
			leg4.render(f5);
			tail.render(f5);
		}
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		head.rotateAngleX = (f4 / 57.29578F) + 0.4014257F;
		head.rotateAngleY = f3 / 57.29578F;
		neck.rotateAngleX = -1.06465F;
		neck.rotateAngleY = head.rotateAngleY * 0.7F;
		
		rightHorn.rotateAngleX = head.rotateAngleX;
		rightHorn.rotateAngleY = head.rotateAngleY;
		leftHorn.rotateAngleX = head.rotateAngleX;
		leftHorn.rotateAngleY = head.rotateAngleY;
		
		earLeft.rotateAngleX = head.rotateAngleX;
		earLeft.rotateAngleY = head.rotateAngleY;
		earRight.rotateAngleX = head.rotateAngleX;
		earRight.rotateAngleY = head.rotateAngleY;
		
		leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		tail.rotateAngleX = 0.2967059F;
	}
}
