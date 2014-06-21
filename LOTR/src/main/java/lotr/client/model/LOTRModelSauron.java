package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelSauron extends LOTRModelBiped
{
	private ModelRenderer bipedCape;
	
	public LOTRModelSauron()
	{
		super();
		
		bipedHead = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
		bipedHead.setRotationPoint(0F, -12F, 0F);
		bipedHead.setTextureOffset(32, 0).addBox(-0.5F, -15F, -3.5F, 1, 7, 1);
		bipedHead.setTextureOffset(32, 0).addBox(-2.5F, -13F, -3.5F, 1, 5, 1);
		bipedHead.setTextureOffset(32, 0).addBox(1.5F, -13F, -3.5F, 1, 5, 1);
		bipedHead.setTextureOffset(32, 0).addBox(-0.5F, -16F, 2.5F, 1, 8, 1);
		bipedHead.setTextureOffset(32, 0).addBox(-3.5F, -16F, -0.5F, 1, 8, 1);
		bipedHead.setTextureOffset(32, 0).addBox(2.5F, -16F, -0.5F, 1, 8, 1);
		
		bipedBody = new ModelRenderer(this, 40, 42).setTextureSize(64, 64);
		bipedBody.addBox(-4F, 0F, -2F, 8, 18, 4);
		bipedBody.setRotationPoint(0F, -12F, 0F);
		
		bipedRightArm = new ModelRenderer(this, 0, 43).setTextureSize(64, 64);
		bipedRightArm.addBox(-3F, -2F, -2F, 4, 17, 4);
		bipedRightArm.setRotationPoint(-5F, -8F, 0F);
		bipedRightArm.setTextureOffset(16, 52).addBox(-4F, -3F, -3F, 6, 6, 6);
		
		bipedLeftArm = new ModelRenderer(this, 0, 43).setTextureSize(64, 64);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1F, -2F, -2F, 4, 17, 4);
		bipedLeftArm.setRotationPoint(5F, -8F, 0F);
		bipedLeftArm.setTextureOffset(16, 52).addBox(-2F, -3F, -3F, 6, 6, 6);
		
		bipedRightLeg = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
		bipedRightLeg.addBox(-2F, 0F, -2F, 4, 18, 4);
		bipedRightLeg.setRotationPoint(-2F, 6F, 0F);
		
		bipedLeftLeg = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 18, 4);
		bipedLeftLeg.setRotationPoint(2F, 6F, 0F);
		
		bipedCape = new ModelRenderer(this, 38, 0).setTextureSize(64, 64);
		bipedCape.addBox(-6F, 1F, 1F, 12, 32, 1);
		bipedCape.setRotationPoint(0F, -12F, 0F);
		bipedCape.rotateAngleX = 0.15F;
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bipedHead.render(f5);
		bipedBody.render(f5);
		bipedRightArm.render(f5);
		bipedLeftArm.render(f5);
		bipedRightLeg.render(f5);
		bipedLeftLeg.render(f5);
		bipedCape.render(f5);
	}
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
        if (isSneak)
        {
            bipedRightLeg.rotationPointY = 3F;
            bipedLeftLeg.rotationPointY = 3F;
            bipedHead.rotationPointY = -11F;
        }
        else
        {
            bipedRightLeg.rotationPointY = 6F;
            bipedLeftLeg.rotationPointY = 6F;
            bipedHead.rotationPointY = -12F;
        }
    }
}
