package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelCrocodile extends ModelBase
{
    private ModelRenderer body;
    private ModelRenderer tail1;
    private ModelRenderer tail2;
    private ModelRenderer tail3;
    private ModelRenderer jaw;
    private ModelRenderer head;
    private ModelRenderer legFrontLeft;
    private ModelRenderer legBackLeft;
    private ModelRenderer legFrontRight;
    private ModelRenderer legBackRight;
    private ModelRenderer spines;
      
    public LOTRModelCrocodile()
	{
        body = new ModelRenderer(this, 18, 83).setTextureSize(128, 128);
        body.addBox(-8F, -5F, 0F, 16, 9, 36);
        body.setRotationPoint(0F, 17F, -16F);

        tail1 = new ModelRenderer(this, 0, 28).setTextureSize(128, 128);
        tail1.addBox(-7F, 0F, 0F, 14, 7, 19);
        tail1.setRotationPoint(0F, 13F, 18F);

        tail2 = new ModelRenderer(this, 0, 55).setTextureSize(128, 128);
        tail2.addBox(-6F, 1.5F, 17F, 12, 5, 16);
        tail2.setRotationPoint(0F, 13F, 18F);
		
        tail3 = new ModelRenderer(this, 0, 77).setTextureSize(128, 128);
        tail3.addBox(-5F, 3F, 31F, 10, 3, 14);
        tail3.setRotationPoint(0F, 13F, 18F);

        jaw = new ModelRenderer(this, 58, 18).setTextureSize(128, 128);
        jaw.addBox(-6.5F, 0.3F, -19F, 13, 4, 19);
        jaw.setRotationPoint(0F, 17F, -16F);
		
        head = new ModelRenderer(this, 0, 0).setTextureSize(128, 128);
        head.addBox(-7.5F, -6F, -21F, 15, 6, 21);
        head.setRotationPoint(0F, 18.5F, -16F);

        legFrontLeft = new ModelRenderer(this, 2, 104).setTextureSize(128, 128);
        legFrontLeft.addBox(0F, 0F, -3F, 16, 3, 6);
        legFrontLeft.setRotationPoint(6F, 15F, -11F);

        legBackLeft = new ModelRenderer(this, 2, 104).setTextureSize(128, 128);
        legBackLeft.addBox(0F, 0F, -3F, 16, 3, 6);
        legBackLeft.setRotationPoint(6F, 15F, 15F);

        legFrontRight = new ModelRenderer(this, 2, 104).setTextureSize(128, 128);
        legFrontRight.mirror = true;
        legFrontRight.addBox(-16F, 0F, -3F, 16, 3, 6);
        legFrontRight.setRotationPoint(-6F, 15F, -11F);
		
        legBackRight = new ModelRenderer(this, 2, 104).setTextureSize(128, 128);
        legBackRight.mirror = true;
        legBackRight.addBox(-16F, 0F, -3F, 16, 3, 6);
        legBackRight.setRotationPoint(-6F, 15F, 15F);
		
        spines = new ModelRenderer(this, 46, 45).setTextureSize(128, 128);
        spines.addBox(-5F, 0F, 0F, 10, 4, 32);
        spines.setRotationPoint(0F, 9.5F, -14F);
        
		legBackLeft.rotateAngleZ = ((float)Math.PI / 180F) * 25F;
		legBackRight.rotateAngleZ = ((float)Math.PI / 180F) * -25F;
		legFrontLeft.rotateAngleZ = ((float)Math.PI / 180F) * 25F;
		legFrontRight.rotateAngleZ = ((float)Math.PI / 180F) * -25F;
		
		spines.rotateAngleX = ((float)Math.PI / 180F) * -2F;
    }

	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body.render(f5);
        tail1.render(f5);
        tail2.render(f5);
		tail3.render(f5);
        jaw.render(f5);
        head.render(f5);
        legFrontLeft.render(f5);
        legBackLeft.render(f5);
        legFrontRight.render(f5);
        legBackRight.render(f5);
        spines.render(f5);
    }

	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
		head.rotateAngleX = f2 * (float)Math.PI * -0.3F;
		
        legBackRight.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1;
        legBackLeft.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1;
        legFrontRight.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1;
        legFrontLeft.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1;
        
		tail1.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1 * 0.5F;
		tail2.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1 * 0.5625F;
		tail3.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1 * 0.59375F;
    }
}
