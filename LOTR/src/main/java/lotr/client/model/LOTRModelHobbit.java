package lotr.client.model;

import lotr.common.entity.npc.LOTREntityHobbit;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class LOTRModelHobbit extends LOTRModelBiped
{
	private ModelRenderer bipedChest;
	
	public LOTRModelHobbit()
	{
		this(0F);
	}
	
	public LOTRModelHobbit(float f)
	{
		super(f, 0F, 64, 64);
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, f);
		bipedHead.setRotationPoint(0F, 4F, 0F);
		
		bipedBody = new ModelRenderer(this, 16, 18);
		bipedBody.addBox(-4F, 0F, -2F, 8, 10, 4, f);
		bipedBody.setRotationPoint(0F, 4F, 0F);
		
		bipedRightArm = new ModelRenderer(this, 40, 18);
		bipedRightArm.addBox(-3F, -2F, -2F, 4, 10, 4, f);
		bipedRightArm.setRotationPoint(-5F, 6F, 0F);
		
		bipedLeftArm = new ModelRenderer(this, 40, 18);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1F, -2F, -2F, 4, 10, 4, f);
		bipedLeftArm.setRotationPoint(5F, 6F, 0F);
		
		bipedRightLeg = new ModelRenderer(this, 0, 18);
		bipedRightLeg.addBox(-2F, 0F, -2F, 4, 10, 4, f);
		bipedRightLeg.setRotationPoint(-2F, 14F, 0F);
		
		ModelRenderer rightFoot = new ModelRenderer(this, 0, 32);
		rightFoot.addBox(-2F, 8F, -5F, 4, 2, 3, f);
		rightFoot.rotateAngleY = (float)Math.toRadians(10D);
		bipedRightLeg.addChild(rightFoot);
		
		bipedLeftLeg = new ModelRenderer(this, 0, 18);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 10, 4, f);
		bipedLeftLeg.setRotationPoint(2F, 14F, 0F);
		
		ModelRenderer leftFoot = new ModelRenderer(this, 0, 32);
		leftFoot.addBox(-2F, 8F, -5F, 4, 2, 3, f);
		leftFoot.rotateAngleY = (float)Math.toRadians(-10D);
		bipedLeftLeg.addChild(leftFoot);
		
		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addBox(-4F, -8F, -4F, 8, 10, 8, 0.5F + f);
		bipedHeadwear.setRotationPoint(0F, 4F, 0F);
		
		bipedChest = new ModelRenderer(this, 24, 0);
		bipedChest.addBox(-3F, 2F, -4F, 6, 3, 2, f);
		bipedChest.setRotationPoint(0F, 4F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (isChild)
        {
            float f6 = 2F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
            GL11.glTranslatef(0F, 16F * f5, 0F);
            bipedHead.render(f5);
			bipedHeadwear.render(f5);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1F / f6, 1F / f6, 1F / f6);
            GL11.glTranslatef(0F, 24F * f5, 0F);
            bipedBody.render(f5);
            bipedRightArm.render(f5);
            bipedLeftArm.render(f5);
            bipedRightLeg.render(f5);
            bipedLeftLeg.render(f5);
            GL11.glPopMatrix();
        }
        else
        {
            bipedHead.render(f5);
			bipedHeadwear.render(f5);
            bipedBody.render(f5);
            bipedRightArm.render(f5);
            bipedLeftArm.render(f5);
            bipedRightLeg.render(f5);
            bipedLeftLeg.render(f5);
			if (!((LOTREntityHobbit)entity).familyInfo.isNPCMale())
			{
				bipedChest.render(f5);
			}
        }
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
        if (isSneak)
        {
            bipedRightLeg.rotationPointY = 11F;
            bipedLeftLeg.rotationPointY = 11F;
            bipedHead.rotationPointY = 5F;
            bipedHeadwear.rotationPointY = 5F;
        }
        else
        {
            bipedRightLeg.rotationPointY = 14F;
            bipedLeftLeg.rotationPointY = 14F;
            bipedHead.rotationPointY = 4F;
            bipedHeadwear.rotationPointY = 4F;
        }
		
		bipedChest.rotateAngleX = bipedBody.rotateAngleX;
		bipedChest.rotateAngleY = bipedBody.rotateAngleY;
		bipedChest.rotateAngleZ = bipedBody.rotateAngleZ;
		
		bipedRightLeg.rotateAngleY += (float)Math.toRadians(5D);
		bipedLeftLeg.rotateAngleY += (float)Math.toRadians(-5D);
	}
}
