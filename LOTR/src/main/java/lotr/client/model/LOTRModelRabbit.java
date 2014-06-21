package lotr.client.model;

import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelRabbit extends ModelBase
{
    private ModelRenderer head;
    private ModelRenderer body;
    private ModelRenderer rightArm;
    private ModelRenderer leftArm;
    private ModelRenderer rightLeg;
    private ModelRenderer leftLeg;

    public LOTRModelRabbit()
    {
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-2F, -2F, -2F, 4, 4, 4);
		head.setRotationPoint(0F, -7F, 0F);
		head.setTextureOffset(0, 8).addBox(-1.5F, 0F, -3F, 3, 2, 2);
		
		ModelRenderer rightEar = new ModelRenderer(this, 16, 0);
		rightEar.addBox(-1.2F, -4.5F, -0.5F, 2, 5, 1);
		rightEar.setRotationPoint(-1F, -1.5F, 0F);
		rightEar.rotateAngleX = (float)Math.toRadians(-20D);
		
		ModelRenderer leftEar = new ModelRenderer(this, 16, 0);
		leftEar.mirror = true;
		leftEar.addBox(-0.8F, -4.5F, -0.5F, 2, 5, 1);
		leftEar.setRotationPoint(1F, -1.5F, 0F);
		leftEar.rotateAngleX = (float)Math.toRadians(-20D);
		
		head.addChild(rightEar);
		head.addChild(leftEar);
		
        body = new ModelRenderer(this, 0, 19);
        body.addBox(-2.5F, -4F, -2.5F, 5, 8, 5);
		body.setRotationPoint(0F, 18.5F, 0F);
		body.setTextureOffset(0, 14).addBox(-1.5F, -6F, -1.5F, 3, 2, 3);
		
		ModelRenderer tail = new ModelRenderer(this, 32, 30);
		tail.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tail.setRotationPoint(0F, 4.5F, 2.5F);
		tail.rotateAngleX = (float)Math.toRadians(-45D);
		
		body.addChild(head);
		body.addChild(tail);
		
		rightArm = new ModelRenderer(this, 32, 0);
		rightArm.addBox(-0.5F, -0.5F, -0.5F, 1, 4, 1);
		rightArm.setRotationPoint(-1.5F, -2F, -2.5F);
		
		leftArm = new ModelRenderer(this, 32, 0);
		leftArm.mirror = true;
		leftArm.addBox(-0.5F, -0.5F, -0.5F, 1, 4, 1);
		leftArm.setRotationPoint(1.5F, -2F, -2.5F);
		
		body.addChild(rightArm);
		body.addChild(leftArm);
		
		rightLeg = new ModelRenderer(this, 32, 8);
		rightLeg.addBox(-1F, -2F, -2F, 2, 4, 4);
		rightLeg.setRotationPoint(-3F, 21.5F, 1F);
		
		ModelRenderer rightFoot = new ModelRenderer(this, 32, 16);
		rightFoot.addBox(-1F, -0.5F, -2.5F, 2, 1, 3);
		rightFoot.setRotationPoint(0F, 2F, -1F);
		rightFoot.rotateAngleX = (float)Math.toRadians(-15D);
		
		rightLeg.addChild(rightFoot);
		
		leftLeg = new ModelRenderer(this, 32, 8);
		leftLeg.mirror = true;
		leftLeg.addBox(-1F, -2F, -2F, 2, 4, 4);
		leftLeg.setRotationPoint(3F, 21.5F, 1F);
		
		ModelRenderer leftFoot = new ModelRenderer(this, 32, 16);
		leftFoot.mirror = true;
		leftFoot.addBox(-1F, -0.5F, -2.5F, 2, 1, 3);
		leftFoot.setRotationPoint(0F, 2F, -1F);
		leftFoot.rotateAngleX = (float)Math.toRadians(-15D);
		
		leftLeg.addChild(leftFoot);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		body.render(f5);
		rightLeg.render(f5);
		leftLeg.render(f5);
    }

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		body.rotateAngleX = (float)Math.toRadians(45D);
		head.rotateAngleX = (float)Math.toRadians(-45D);
        rightArm.rotateAngleX = (float)Math.toRadians(-55D);
        leftArm.rotateAngleX = (float)Math.toRadians(-55D);
		
		float f6 = (float)Math.toRadians(45D);
		if (LOTRMod.isAprilFools())
		{
			f6 *= f2;
		}
		else
		{
			f6 *= f1;
		}

		body.rotateAngleX += f6;
		head.rotateAngleX -= f6;
		rightArm.rotateAngleX -= f6;
		leftArm.rotateAngleX -= f6;
		
		if (((LOTREntityRabbit)entity).isRabbitEating())
		{
			float f7 = (float)Math.toRadians(30D);
			body.rotateAngleX += f7;
			rightArm.rotateAngleX += f7;
			leftArm.rotateAngleX += f7;
			head.rotateAngleX += f7 * 2F;
		}
		else
		{
			head.rotateAngleX += f4 / (180F / (float)Math.PI);
			head.rotateAngleY = MathHelper.cos(head.rotateAngleX) * f3 / (180F / (float)Math.PI);
			head.rotateAngleZ = MathHelper.sin(head.rotateAngleX) * f3 / (180F / (float)Math.PI);
		}

        rightArm.rotateAngleX += MathHelper.cos(f * 0.6662F + (float)Math.PI) * f1;
        leftArm.rotateAngleX += MathHelper.cos(f * 0.6662F) * f1;
		body.rotateAngleZ = MathHelper.cos(f * 0.6662F) * f1 * 0.3F;

        rightLeg.rotateAngleX = (float)Math.toRadians(15D);
        leftLeg.rotateAngleX = (float)Math.toRadians(15D);
        rightLeg.rotateAngleX += MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leftLeg.rotateAngleX += MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
	}
}
