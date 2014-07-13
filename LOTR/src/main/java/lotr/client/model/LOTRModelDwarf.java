package lotr.client.model;

import lotr.common.entity.npc.LOTREntityDwarf;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class LOTRModelDwarf extends LOTRModelBiped
{
	private ModelRenderer bipedChest;
	
	public LOTRModelDwarf()
	{
		this(0F);
	}
	
	public LOTRModelDwarf(float f)
	{
		this(f, 64, f == 0F ? 64 : 32);
	}
	
	public LOTRModelDwarf(float f, int width, int height)
	{
		super(f, 0F, width, height);
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, f);
		bipedHead.setRotationPoint(0F, 2F, 0F);
		
		bipedBody = new ModelRenderer(this, 18, 17);
		bipedBody.addBox(-5F, 0F, -2F, 10, 11, 4, f);
		bipedBody.setRotationPoint(0F, 2F, 0F);
		
		bipedRightArm = new ModelRenderer(this, 46, 17);
		bipedRightArm.addBox(-4F, -2F, -2F, 4, 11, 4, f);
		bipedRightArm.setRotationPoint(-5F, 4F, 0F);
		
		bipedLeftArm = new ModelRenderer(this, 46, 17);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(0F, -2F, -2F, 4, 10, 4, f);
		bipedLeftArm.setRotationPoint(5F, 4F, 0F);
		
		bipedRightLeg = new ModelRenderer(this, 0, 17);
		bipedRightLeg.addBox(-2F, 0F, -2F, 5, 11, 4, f);
		bipedRightLeg.setRotationPoint(-3F, 13F, 0F);
		
		bipedLeftLeg = new ModelRenderer(this, 0, 17);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-2F, 0F, -2F, 5, 11, 4, f);
		bipedLeftLeg.setRotationPoint(2F, 13F, 0F);
		
		bipedChest = new ModelRenderer(this, 24, 0);
		bipedChest.addBox(-3F, 2F, -4F, 6, 3, 2, f);
		bipedChest.setRotationPoint(0F, 2F, 0F);
		
		if (height == 64)
		{
			bipedHeadwear = new ModelRenderer(this, 0, 32);
			bipedHeadwear.addBox(-4F, -8F, -4F, 8, 12, 8, 0.5F + f);
			bipedHeadwear.setRotationPoint(0F, 2F, 0F);
		}
		else
		{
			bipedHeadwear = new ModelRenderer(this, 32, 0);
			bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, 0.5F + f);
			bipedHeadwear.setRotationPoint(0F, 2F, 0F);
		}
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
			if (!((LOTREntityDwarf)entity).familyInfo.isNPCMale())
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
            bipedRightLeg.rotationPointY = 10F;
            bipedLeftLeg.rotationPointY = 10F;
            bipedHead.rotationPointY = 3F;
            bipedHeadwear.rotationPointY = 3F;
        }
        else
        {
            bipedRightLeg.rotationPointY = 13F;
            bipedLeftLeg.rotationPointY = 13F;
            bipedHead.rotationPointY = 2F;
            bipedHeadwear.rotationPointY = 2F;
        }
		
		bipedChest.rotateAngleX = bipedBody.rotateAngleX;
		bipedChest.rotateAngleY = bipedBody.rotateAngleY;
		bipedChest.rotateAngleZ = bipedBody.rotateAngleZ;
	}
}
