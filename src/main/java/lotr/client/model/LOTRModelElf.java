package lotr.client.model;

import lotr.common.entity.npc.LOTREntityElf;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class LOTRModelElf extends LOTRModelBiped
{
	private ModelRenderer earRight;
	private ModelRenderer earLeft;
	private ModelRenderer bipedChest;
	
	public LOTRModelElf()
	{
		this(0F);
	}
	
	public LOTRModelElf(float f)
	{
		this(f, 64, f == 0F ? 64 : 32);
	}
	
	public LOTRModelElf(float f, int width, int height)
	{
		super(f, 0F, width, height);
		
		earRight = new ModelRenderer(this, 0, 0);
		earRight.addBox(-4F, -6.5F, -1F, 1, 4, 2);
		earRight.setRotationPoint(0F, 0F, 0F);
		earRight.rotateAngleZ = -15F / (180F / (float)Math.PI);
		
		earLeft = new ModelRenderer(this, 0, 0);
		earLeft.mirror = true;
		earLeft.addBox(3F, -6.5F, -1F, 1, 4, 2);
		earLeft.setRotationPoint(0F, 0F, 0F);
		earLeft.rotateAngleZ = 15F / (180F / (float)Math.PI);
		
		bipedHead.addChild(earRight);
		bipedHead.addChild(earLeft);
		
		bipedChest = new ModelRenderer(this, 24, 0);
		bipedChest.addBox(-3F, 2F, -4F, 6, 3, 2, f);
		bipedChest.setRotationPoint(0F, 0F, 0F);
		
		if (height == 64)
		{
			bipedHeadwear = new ModelRenderer(this, 0, 32);
			bipedHeadwear.addBox(-4F, -8F, -4F, 8, 16, 8, 0.5F + f);
			bipedHeadwear.setRotationPoint(0F, 0F, 0F);
		}
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (isChild)
        {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
            GL11.glTranslatef(0.0F, 16.0F * f5, 0.0F);
            bipedHead.render(f5);
			if (((LOTREntityElf)entity).shouldRenderHair())
			{
				bipedHeadwear.render(f5);
			}
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
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
			if (((LOTREntityElf)entity).shouldRenderHair())
			{
				bipedHeadwear.render(f5);
			}
			bipedBody.render(f5);
			bipedRightArm.render(f5);
			bipedLeftArm.render(f5);
			bipedRightLeg.render(f5);
			bipedLeftLeg.render(f5);
			if (!((LOTREntityElf)entity).familyInfo.isNPCMale())
			{
				bipedChest.render(f5);
			}
		}
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		bipedChest.rotateAngleX = bipedBody.rotateAngleX;
		bipedChest.rotateAngleY = bipedBody.rotateAngleY;
		bipedChest.rotateAngleZ = bipedBody.rotateAngleZ;
	}
}
