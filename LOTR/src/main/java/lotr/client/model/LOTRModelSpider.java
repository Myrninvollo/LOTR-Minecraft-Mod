package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelSpider extends ModelBase
{
	protected ModelRenderer head;
	protected ModelRenderer thorax;
	protected ModelRenderer abdomen;
	protected ModelRenderer leg1;
	protected ModelRenderer leg2;
	protected ModelRenderer leg3;
	protected ModelRenderer leg4;
	protected ModelRenderer leg5;
	protected ModelRenderer leg6;
	protected ModelRenderer leg7;
	protected ModelRenderer leg8;
	
	public LOTRModelSpider()
	{
		float f = 0.5F;
		
        head = new ModelRenderer(this, 32, 0);
        head.addBox(-4F, -4F, -8F, 8, 8, 8, f);
        head.setRotationPoint(0F, 17F, -3F);
		
        thorax = new ModelRenderer(this, 0, 0);
        thorax.addBox(-3F, -3F, -3F, 6, 6, 6, f);
        thorax.setRotationPoint(0F, 17F, 0F);
		
        abdomen = new ModelRenderer(this, 0, 12);
        abdomen.addBox(-5F, -4F, -0.5F, 10, 8, 12, f);
        abdomen.setRotationPoint(0F, 17F, 3F);
		
        leg1 = new ModelRenderer(this, 36, 16);
        leg1.addBox(-11F, -1F, -1F, 12, 2, 2, f);
        leg1.setRotationPoint(-4F, 17F, 2F);
		leg1.setTextureOffset(60, 20).addBox(-10.5F, 0F, -0.5F, 1, 10, 1, f);
		
        leg2 = new ModelRenderer(this, 36, 16);
		leg2.mirror = true;
        leg2.addBox(-1F, -1F, -1F, 12, 2, 2, f);
        leg2.setRotationPoint(4F, 17F, 2F);
		leg2.setTextureOffset(60, 20).addBox(9.5F, 0F, -0.5F, 1, 10, 1, f);
		
        leg3 = new ModelRenderer(this, 36, 16);
        leg3.addBox(-11F, -1F, -1F, 12, 2, 2, f);
        leg3.setRotationPoint(-4F, 17F, 1F);
		leg3.setTextureOffset(60, 20).addBox(-10.5F, 0F, -0.5F, 1, 10, 1, f);
		
        leg4 = new ModelRenderer(this, 36, 16);
		leg4.mirror = true;
        leg4.addBox(-1F, -1F, -1F, 12, 2, 2, f);
        leg4.setRotationPoint(4F, 17F, 1F);
		leg4.setTextureOffset(60, 20).addBox(9.5F, 0F, -0.5F, 1, 10, 1, f);
		
        leg5 = new ModelRenderer(this, 36, 16);
        leg5.addBox(-11F, -1F, -1F, 12, 2, 2, f);
        leg5.setRotationPoint(-4F, 17F, 0F);
		leg5.setTextureOffset(60, 20).addBox(-10.5F, 0F, -0.5F, 1, 10, 1, f);
		
        leg6 = new ModelRenderer(this, 36, 16);
		leg6.mirror = true;
        leg6.addBox(-1F, -1F, -1F, 12, 2, 2, f);
        leg6.setRotationPoint(4F, 17F, 0F);
		leg6.setTextureOffset(60, 20).addBox(9.5F, 0F, -0.5F, 1, 10, 1, f);
		
        leg7 = new ModelRenderer(this, 36, 16);
        leg7.addBox(-11F, -1F, -1F, 12, 2, 2, f);
        leg7.setRotationPoint(-4F, 17F, -1F);
		leg7.setTextureOffset(60, 20).addBox(-10.5F, 0F, -0.5F, 1, 10, 1, f);
		
        leg8 = new ModelRenderer(this, 36, 16);
		leg8.mirror = true;
        leg8.addBox(-1F, -1F, -1F, 12, 2, 2, f);
        leg8.setRotationPoint(4F, 17F, -1F);
		leg8.setTextureOffset(60, 20).addBox(9.5F, 0F, -0.5F, 1, 10, 1, f);
	}
	
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        head.render(f5);
        thorax.render(f5);
        abdomen.render(f5);
        leg1.render(f5);
        leg2.render(f5);
        leg3.render(f5);
        leg4.render(f5);
        leg5.render(f5);
        leg6.render(f5);
        leg7.render(f5);
        leg8.render(f5);
    }
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        head.rotateAngleY = f3 / (180F / (float)Math.PI);
        head.rotateAngleX = f4 / (180F / (float)Math.PI);
		abdomen.rotateAngleY = MathHelper.cos(f * 0.6662F) * 0.5F * f1;
        float f6 = ((float)Math.PI / 4F) - 1.3F;
        leg1.rotateAngleZ = -f6;
        leg2.rotateAngleZ = f6;
        leg3.rotateAngleZ = -f6 * 0.74F;
        leg4.rotateAngleZ = f6 * 0.74F;
        leg5.rotateAngleZ = -f6 * 0.74F;
        leg6.rotateAngleZ = f6 * 0.74F;
        leg7.rotateAngleZ = -f6;
        leg8.rotateAngleZ = f6;
        float f7 = -0F;
        float f8 = 0.3926991F;
        leg1.rotateAngleY = f8 * 2F + f7;
        leg2.rotateAngleY = -f8 * 2F - f7;
        leg3.rotateAngleY = f8 * 1F + f7;
        leg4.rotateAngleY = -f8 * 1F - f7;
        leg5.rotateAngleY = -f8 * 1F + f7;
        leg6.rotateAngleY = f8 * 1F - f7;
        leg7.rotateAngleY = -f8 * 2F + f7;
        leg8.rotateAngleY = f8 * 2F - f7;
        float f9 = -(MathHelper.cos(f * 0.6662F * 2F + 0F) * 0.4F) * f1;
        float f10 = -(MathHelper.cos(f * 0.6662F * 2F + (float)Math.PI) * 0.4F) * f1;
        float f11 = -(MathHelper.cos(f * 0.6662F * 2F + ((float)Math.PI / 2F)) * 0.4F) * f1;
        float f12 = -(MathHelper.cos(f * 0.6662F * 2F + ((float)Math.PI * 3F / 2F)) * 0.4F) * f1;
        float f13 = Math.abs(MathHelper.sin(f * 0.6662F + 0F) * 0.4F) * f1;
        float f14 = Math.abs(MathHelper.sin(f * 0.6662F + (float)Math.PI) * 0.4F) * f1;
        float f15 = Math.abs(MathHelper.sin(f * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * f1;
        float f16 = Math.abs(MathHelper.sin(f * 0.6662F + ((float)Math.PI * 3F / 2F)) * 0.4F) * f1;
        leg1.rotateAngleY += f9;
        leg2.rotateAngleY += -f9;
        leg3.rotateAngleY += f10;
        leg4.rotateAngleY += -f10;
        leg5.rotateAngleY += f11;
        leg6.rotateAngleY += -f11;
        leg7.rotateAngleY += f12;
        leg8.rotateAngleY += -f12;
        leg1.rotateAngleZ += f13;
        leg2.rotateAngleZ += -f13;
        leg3.rotateAngleZ += f14;
        leg4.rotateAngleZ += -f14;
        leg5.rotateAngleZ += f15;
        leg6.rotateAngleZ += -f15;
        leg7.rotateAngleZ += f16;
        leg8.rotateAngleZ += -f16;
    }
}
