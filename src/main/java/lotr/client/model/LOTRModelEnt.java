package lotr.client.model;

import lotr.common.entity.npc.LOTREntityEnt;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelEnt extends ModelBase
{
	private ModelRenderer trunk;
	private ModelRenderer browRight;
	private ModelRenderer browLeft;
	private ModelRenderer eyeRight;
	private ModelRenderer eyeLeft;
	private ModelRenderer nose;
	private ModelRenderer beard;
	
	private ModelRenderer rightArm;
	private ModelRenderer rightHand;
	
	private ModelRenderer leftArm;
	private ModelRenderer leftHand;

	private ModelRenderer rightLeg;
	private ModelRenderer rightFoot;
	
	private ModelRenderer leftLeg;
	private ModelRenderer leftFoot;
	
	private ModelRenderer branch1;
	private ModelRenderer branch1twig1;
	private ModelRenderer branch1twig2;
	private ModelRenderer branch1twig3;
	
	private ModelRenderer branch2;
	
	private ModelRenderer branch3;
	
	private ModelRenderer branch4;
	private ModelRenderer branch4twig1;
	
	private ModelRenderer branch5;
	
	public LOTRModelEnt()
	{
		textureWidth = 128;
		textureHeight = 128;
		
		trunk = new ModelRenderer(this, 0, 0);
		trunk.addBox(-8F, -48F, -6F, 16, 48, 12);
		trunk.setRotationPoint(0F, -10F, 0F);
		
		browRight = new ModelRenderer(this, 56, 26);
		browRight.addBox(-6.5F, 0F, -8F, 5, 1, 2);
		browRight.setRotationPoint(0F, -49F, 0F);
		browRight.rotateAngleZ = (float)Math.toRadians(10D);
		
		browLeft = new ModelRenderer(this, 56, 26);
		browLeft.mirror = true;
		browLeft.addBox(1.5F, 0F, -8F, 5, 1, 2);
		browLeft.setRotationPoint(0F, -49F, 0F);
		browLeft.rotateAngleZ = (float)Math.toRadians(-10D);
		
		eyeRight = new ModelRenderer(this, 56, 29);
		eyeRight.addBox(-1.5F, -2F, -7F, 3, 3, 1);
		eyeRight.setRotationPoint(-3.5F, -46F, 0F);
		
		eyeLeft = new ModelRenderer(this, 56, 29);
		eyeLeft.mirror = true;
		eyeLeft.addBox(-1.5F, -2F, -7F, 3, 3, 1);
		eyeLeft.setRotationPoint(3.5F, -46F, 0F);
		
		nose = new ModelRenderer(this, 56, 33);
		nose.addBox(-1.5F, -2F, -9F, 3, 6, 3);
		nose.setRotationPoint(0F, -46F, 0F);
		
		beard = new ModelRenderer(this, 56, 0);
		beard.addBox(-5F, 0F, -8F, 10, 24, 2);
		beard.setRotationPoint(0F, -41F, 0F);
		
		rightArm = new ModelRenderer(this, 96, 28);
		rightArm.addBox(-8F, 0F, -4F, 8, 12, 8);
		rightArm.setTextureOffset(112, 48).addBox(-7F, 12F, -2F, 4, 16, 4);
		rightArm.setRotationPoint(-8F, -48F, 0F);
		
		rightHand = new ModelRenderer(this, 102, 68);
		rightHand.addBox(-2.5F, 0F, -4F, 5, 16, 8);
		rightHand.setTextureOffset(102, 92).addBox(-2F, 16F, -4F, 3, 10, 2);
		rightHand.setTextureOffset(112, 92).addBox(-2F, 16F, -1F, 2, 8, 2);
		rightHand.setTextureOffset(120, 92).addBox(-2F, 16F, 2F, 2, 6, 2);
		rightHand.setRotationPoint(-13F, -20F, 0F);
		
		leftArm = new ModelRenderer(this, 96, 28);
		leftArm.mirror = true;
		leftArm.addBox(0F, 0F, -4F, 8, 12, 8);
		leftArm.setTextureOffset(112, 48).addBox(3F, 12F, -2F, 4, 16, 4);
		leftArm.setRotationPoint(8F, -48F, 0F);
		
		leftHand = new ModelRenderer(this, 102, 68);
		leftHand.mirror = true;
		leftHand.addBox(-2.5F, 0F, -4F, 5, 16, 8);
		leftHand.setTextureOffset(102, 92).addBox(-1F, 16F, -4F, 3, 10, 2);
		leftHand.setTextureOffset(112, 92).addBox(0F, 16F, -1F, 2, 8, 2);
		leftHand.setTextureOffset(120, 92).addBox(0F, 16F, 2F, 2, 6, 2);
		leftHand.setRotationPoint(13F, -20F, 0F);
		
		rightLeg = new ModelRenderer(this, 0, 60);
		rightLeg.addBox(-7F, -4F, -4F, 6, 22, 8);
		rightLeg.setRotationPoint(-4F, -12F, 0F);
		
		rightFoot = new ModelRenderer(this, 28, 60);
		rightFoot.addBox(-4F, 0F, -5F, 8, 12, 10);
		rightFoot.setTextureOffset(0, 90).addBox(-5F, 12F, -7F, 10, 6, 15);
		rightFoot.setTextureOffset(0, 111).addBox(2F, 13F, -16F, 3, 5, 9);
		rightFoot.setTextureOffset(24, 113).addBox(-2F, 14F, -15F, 3, 4, 8);
		rightFoot.setTextureOffset(46, 115).addBox(-5F, 15F, -14F, 2, 3, 7);
		rightFoot.setRotationPoint(-8F, 6F, 0F);
		
		leftLeg = new ModelRenderer(this, 0, 60);
		leftLeg.mirror = true;
		leftLeg.addBox(1F, -4F, -4F, 6, 22, 8);
		leftLeg.setRotationPoint(4F, -12F, 0F);
		
		leftFoot = new ModelRenderer(this, 28, 60);
		leftFoot.mirror = true;
		leftFoot.addBox(-4F, 0F, -5F, 8, 12, 10);
		leftFoot.setTextureOffset(0, 90).addBox(-5F, 12F, -7F, 10, 6, 15);
		leftFoot.setTextureOffset(0, 111).addBox(-5F, 13F, -16F, 3, 5, 9);
		leftFoot.setTextureOffset(24, 113).addBox(-1F, 14F, -15F, 3, 4, 8);
		leftFoot.setTextureOffset(46, 115).addBox(3F, 15F, -14F, 2, 3, 7);
		leftFoot.setRotationPoint(8F, 6F, 0F);
		
		branch1 = new ModelRenderer(this, 0, 20);
		branch1.addBox(-1.5F, -28F, -1.5F, 3, 32, 3);
		branch1.setTextureOffset(80, 0).addBox(-3.5F, -32F, -3.5F, 7, 7, 7);
		branch1.setRotationPoint(-1F, -58F, 0F);
		setRotation(branch1, -7F, 17F, 0F);
		
		branch1twig1 = new ModelRenderer(this, 0, 20);
		branch1twig1.addBox(-7.5F, -22F, -1.5F, 1, 12, 1);
		branch1twig1.setTextureOffset(80, 0).addBox(-8.5F, -23F, -2.5F, 3, 3, 3);
		branch1twig1.setRotationPoint(-1F, -58F, 0F);
		setRotation(branch1twig1, -7F, -56F, 49F);
		
		branch1twig2 = new ModelRenderer(this, 0, 20);
		branch1twig2.addBox(-14F, -24F, -1F, 2, 12, 2);
		branch1twig2.setTextureOffset(80, 0).addBox(-15.5F, -26F, -2.5F, 5, 5, 5);
		branch1twig2.setRotationPoint(-1F, -58F, 0F);
		setRotation(branch1twig2, -9F, 20F, 47F);
		
		branch1twig3 = new ModelRenderer(this, 0, 20);
		branch1twig3.addBox(-7.5F, -24F, -3.5F, 1, 12, 1);
		branch1twig3.setTextureOffset(80, 0).addBox(-8.5F, -25F, -4.5F, 3, 3, 3);
		branch1twig3.setRotationPoint(-1F, -58F, 0F);
		setRotation(branch1twig3, -9F, 165F, 30F);
		
		branch2 = new ModelRenderer(this, 0, 20);
		branch2.addBox(-0.5F, -10F, -0.5F, 1, 14, 1);
		branch2.setTextureOffset(80, 0).addBox(-1.5F, -12F, -1.5F, 3, 3, 3);
		branch2.setRotationPoint(6F, -58F, 2F);
		setRotation(branch2, -20F, 42F, 0F);
		
		branch3 = new ModelRenderer(this, 0, 20);
		branch3.addBox(-1F, -16F, -1F, 2, 20, 2);
		branch3.setTextureOffset(80, 0).addBox(-2.5F, -18F, -2.5F, 5, 5, 5);
		branch3.setRotationPoint(3F, -58F, -3F);
		setRotation(branch3, 26F, -27F, 0F);
		
		branch4 = new ModelRenderer(this, 0, 20);
		branch4.addBox(-1F, -18F, -1F, 2, 22, 2);
		branch4.setTextureOffset(80, 0).addBox(-2.5F, -20F, -2.5F, 5, 5, 5);
		branch4.setRotationPoint(-5F, -58F, -4F);
		setRotation(branch4, 17F, 60F, 0F);
		
		branch4twig1 = new ModelRenderer(this, 0, 20);
		branch4twig1.addBox(-2.5F, -21F, -7.5F, 1, 12, 1);
		branch4twig1.setTextureOffset(80, 0).addBox(-4F, -22F, -9F, 4, 4, 4);
		branch4twig1.setRotationPoint(-5F, -58F, -4F);
		setRotation(branch4twig1, -43F, 160F, 30F);
		
		branch5 = new ModelRenderer(this, 0, 20);
		branch5.addBox(-1F, -24F, -1F, 2, 28, 2);
		branch5.setTextureOffset(80, 0).addBox(-2F, -25F, -2F, 4, 4, 4);
		branch5.setRotationPoint(-5F, -58F, 3F);
		setRotation(branch5, -20F, -36F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		trunk.render(f5);
		browRight.render(f5);
		browLeft.render(f5);
		eyeRight.render(f5);
		eyeLeft.render(f5);
		nose.render(f5);
		beard.render(f5);
		rightArm.render(f5);
		rightHand.render(f5);
		leftArm.render(f5);
		leftHand.render(f5);
		rightLeg.render(f5);
		rightFoot.render(f5);
		leftLeg.render(f5);
		leftFoot.render(f5);
		branch1.render(f5);
		branch1twig1.renderWithRotation(f5);
		branch1twig2.renderWithRotation(f5);
		branch1twig3.renderWithRotation(f5);
		branch2.render(f5);
		branch3.render(f5);
		branch4.render(f5);
		branch4twig1.renderWithRotation(f5);
		branch5.render(f5);
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		LOTREntityEnt ent = (LOTREntityEnt)entity;
		
		eyeRight.showModel = ent.eyesClosed > 0;
		eyeLeft.showModel = ent.eyesClosed > 0;
		
		if (ent.hurtTime > 0)
		{
			browRight.rotateAngleZ = (float)Math.toRadians(30D);
			browLeft.rotateAngleZ = -browRight.rotateAngleZ;
			browRight.rotationPointY = browLeft.rotationPointY = -50F;
		}
		else
		{
			browRight.rotateAngleZ = (float)Math.toRadians(10D);
			browLeft.rotateAngleZ = -browRight.rotateAngleZ;
			browRight.rotationPointY = browLeft.rotationPointY = -49F;
		}
		
		rightArm.rotateAngleX = 0F;
        leftArm.rotateAngleX = 0F;
        rightArm.rotateAngleZ = 0F;
        leftArm.rotateAngleZ = 0F;
		
        if (onGround > -9990F)
        {
            float f6 = onGround;
            float f7 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2F) * 0.2F;
            leftArm.rotateAngleX += f7;
            f6 = 1F - onGround;
            f6 *= f6;
            f6 *= f6;
            f6 = 1F - f6;
            float f8 = MathHelper.sin(f6 * (float)Math.PI);
            float f9 = MathHelper.sin(onGround * (float)Math.PI) * -(trunk.rotateAngleX - 0.7F) * 0.75F;
            rightArm.rotateAngleX = (float)((double)rightArm.rotateAngleX - ((double)f8 * 1.2D + (double)f9));
        }
		
	    rightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        leftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		
		float angle = rightArm.rotateAngleZ;
		float sin = MathHelper.sin(angle);
		float cos = MathHelper.cos(angle);
        rightHand.rotationPointX = -8F - (28F * sin) - (5F * cos);
		rightHand.rotationPointY = -48F + (28F * cos) - (5F * sin);
		rightHand.rotateAngleZ = angle;
		
		angle = leftArm.rotateAngleZ;
		sin = MathHelper.sin(-angle);
		cos = MathHelper.cos(-angle);
        leftHand.rotationPointX = 8F + (28F * sin) + (5F * cos);
		leftHand.rotationPointY = -48F + (28F * cos) - (5F * sin);
		leftHand.rotateAngleZ = angle;
		
        rightArm.rotateAngleX += MathHelper.cos(f * 0.6662F + (float)Math.PI) * 0.8F * f1;
        leftArm.rotateAngleX += MathHelper.cos(f * 0.6662F) * 0.8F * f1;
		
		angle = rightArm.rotateAngleX;
		sin = MathHelper.sin(angle);
		cos = MathHelper.cos(angle);
		rightHand.rotationPointZ = 0F + (28F * sin);
		rightHand.rotationPointY = -48F + (28F * cos);
		rightHand.rotateAngleX = angle * 1.5F;
		
		angle = leftArm.rotateAngleX;
		sin = MathHelper.sin(-angle);
		cos = MathHelper.cos(-angle);
		leftHand.rotationPointZ = 0F - (28F * sin);
		leftHand.rotationPointY = -48F + (28F * cos);
		leftHand.rotateAngleX = angle * 1.5F;
		
		rightLeg.rotateAngleX = 0F;
		rightFoot.rotateAngleX = 0F;
		leftLeg.rotateAngleX = 0F;
		leftFoot.rotateAngleX = 0F;
		
		if (MathHelper.sin(f * 0.6662F * 0.3F) * f1 >= 0F)
		{
			rightLeg.rotateAngleX += MathHelper.sin(f * 0.6662F * 0.3F * 2F) * 1.2F * f1;
			leftLeg.rotateAngleX -= rightLeg.rotateAngleX;
			leftFoot.rotateAngleX = leftLeg.rotateAngleX;
		}
		else
		{
			leftLeg.rotateAngleX += MathHelper.sin(f * 0.6662F * 0.3F * 2F) * 1.2F * f1;
			rightLeg.rotateAngleX -= leftLeg.rotateAngleX;
			rightFoot.rotateAngleX = rightLeg.rotateAngleX;
		}
		
		angle = rightLeg.rotateAngleX;
		sin = MathHelper.sin(angle);
		cos = MathHelper.cos(angle);
		rightFoot.rotationPointZ = 0F + (18F * sin);
		rightFoot.rotationPointY = -12F + (18F * cos);

		angle = leftLeg.rotateAngleX;
		sin = MathHelper.sin(angle);
		cos = MathHelper.cos(angle);
		leftFoot.rotationPointZ = 0F + (18F * sin);
		leftFoot.rotationPointY = -12F + (18F * cos);
	}
	
	private void setRotation(ModelRenderer part, float x, float y, float z)
	{
		part.rotateAngleX = (float)Math.toRadians((double)x);
		part.rotateAngleY = (float)Math.toRadians((double)y);
		part.rotateAngleZ = (float)Math.toRadians((double)z);
	}
}
