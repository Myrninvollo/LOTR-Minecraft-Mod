package lotr.client.model;

import lotr.common.entity.npc.LOTREntityGollum;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelGollum extends ModelBase
{
	public ModelRenderer head;
	public ModelRenderer body;
	public ModelRenderer rightShoulder;
	public ModelRenderer rightArm;
	public ModelRenderer leftShoulder;
	public ModelRenderer leftArm;
	public ModelRenderer rightThigh;
	public ModelRenderer rightLeg;
	public ModelRenderer leftThigh;
	public ModelRenderer leftLeg;
	
	public LOTRModelGollum()
	{
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3.5F, -6.5F, -6.5F, 7, 7, 7);
		head.setRotationPoint(0F, 5F, -5.5F);
		head.addBox(3.5F, -4F, -4F, 1, 2, 2);
		head.mirror = true;
		head.addBox(-4.5F, -4F, -4F, 1, 2, 2);
		
		body = new ModelRenderer(this, 20, 17);
		body.addBox(-5F, -12F, -2F, 10, 12, 3);
		body.setRotationPoint(0F, 11F, 5F);
		body.setTextureOffset(32, 0).addBox(-5.5F, -2F, -3.5F, 11, 4, 5);
		body.rotateAngleX = (float)Math.PI / 180F * 60F;
		
		rightShoulder = new ModelRenderer(this, 0, 23);
		rightShoulder.addBox(-0.5F, -1F, -2F, 3, 6, 3);
		rightShoulder.setRotationPoint(5F, 6F, -4.5F);
		rightShoulder.rotateAngleX = (float)Math.PI / 180F * 30F;
		
		rightArm = new ModelRenderer(this, 12, 22);
		rightArm.addBox(0F, 4F, 0.5F, 2, 8, 2);
		rightArm.setRotationPoint(5F, 6F, -4.5F);
		
		leftShoulder = new ModelRenderer(this, 0, 23);
		leftShoulder.mirror = true;
		leftShoulder.addBox(-1.5F, -1F, -2F, 3, 6, 3);
		leftShoulder.setRotationPoint(-5F, 6F, -4.5F);
		leftShoulder.rotateAngleX = (float)Math.PI / 180F * 30F;
		
		leftArm = new ModelRenderer(this, 12, 22);
		leftArm.mirror = true;
		leftArm.addBox(-1F, 4F, 0.5F, 2, 8, 2);
		leftArm.setRotationPoint(-5F, 6F, -4.5F);
		
		rightThigh = new ModelRenderer(this, 0, 23);
		rightThigh.addBox(-0.5F, -1F, -1F, 3, 6, 3);
		rightThigh.setRotationPoint(2F, 12F, 4F);
		rightThigh.rotateAngleX = (float)Math.PI / 180F * -25F;
		
		rightLeg = new ModelRenderer(this, 12, 22);
		rightLeg.addBox(0F, 4F, -2.5F, 2, 8, 2);
		rightLeg.setRotationPoint(2F, 12F, 4F);
		
		leftThigh = new ModelRenderer(this, 0, 23);
		leftThigh.addBox(-2.5F, -1F, -1F, 3, 6, 3);
		leftThigh.setRotationPoint(-2F, 12F, 4F);
		leftThigh.rotateAngleX = (float)Math.PI / 180F * -25F;
		
		leftLeg = new ModelRenderer(this, 12, 22);
		leftLeg.addBox(-2F, 4F, -2.5F, 2, 8, 2);
		leftLeg.setRotationPoint(-2F, 12F, 4F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		head.render(f5);
		body.render(f5);
		rightShoulder.render(f5);
		rightArm.render(f5);
		leftShoulder.render(f5);
		leftArm.render(f5);
		rightThigh.render(f5);
		rightLeg.render(f5);
		leftThigh.render(f5);
		leftLeg.render(f5);
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
        head.rotateAngleY = f3 / (180F / (float)Math.PI);
        head.rotateAngleX = f4 / (180F / (float)Math.PI);
        rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2F * f1 * 0.5F;
        leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2F * f1 * 0.5F;
        rightArm.rotateAngleZ = 0F;
        leftArm.rotateAngleZ = 0F;
        rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        rightLeg.rotateAngleY = 0F;
        leftLeg.rotateAngleY = 0F;
        rightArm.rotateAngleY = 0F;
        leftArm.rotateAngleY = 0F;
        rightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        leftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        rightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
        leftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		if (((LOTREntityGollum)entity).isGollumSitting())
		{
			float f6 = f2 / 20F;
			f6 *= (float)Math.PI * 2F;
			rightArm.rotateAngleX = MathHelper.sin(f6) * 3F;
			leftArm.rotateAngleX = MathHelper.sin(f6) * -3F;
			rightLeg.rotateAngleX = MathHelper.sin(f6) * 0.5F;
			leftLeg.rotateAngleX = MathHelper.sin(f6) * -0.5F;
		}
		else if (((LOTREntityGollum)entity).isGollumFleeing())
		{
			rightArm.rotateAngleX += (float)Math.PI;
			leftArm.rotateAngleX += (float)Math.PI;
		}
		body.rotateAngleZ = MathHelper.cos(f * 0.6662F) * 0.25F * f1;
		syncRotationAngles(rightArm, rightShoulder, 30F);
		syncRotationAngles(leftArm, leftShoulder, 30F);
		syncRotationAngles(rightLeg, rightThigh, -25F);
		syncRotationAngles(leftLeg, leftThigh, -25F);
	}
	
	private void syncRotationAngles(ModelRenderer source, ModelRenderer target, float additionalAngle)
	{
		target.rotationPointX = source.rotationPointX;
		target.rotationPointY = source.rotationPointY;
		target.rotationPointZ = source.rotationPointZ;
		target.rotateAngleX = source.rotateAngleX + ((float)Math.PI / 180F * additionalAngle);
		target.rotateAngleY = source.rotateAngleY;
		target.rotateAngleZ = source.rotateAngleZ;
	}
}
