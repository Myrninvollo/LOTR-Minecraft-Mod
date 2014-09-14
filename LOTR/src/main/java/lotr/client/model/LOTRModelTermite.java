package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class LOTRModelTermite extends ModelBase
{
	private ModelRenderer body;
	private ModelRenderer head;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg5;
	private ModelRenderer leg4;
	private ModelRenderer leg6;
	private ModelRenderer rightfeeler;
	private ModelRenderer leftfeeler;
	
	public LOTRModelTermite()
	{
		body = new ModelRenderer(this, 10, 5);
		body.addBox(0F, 0F, 0F, 6, 6, 21);
		body.setRotationPoint(-3F, 17F, -5F);
		
		head = new ModelRenderer(this, 0, 0);
		head.addBox(0F, 0F, 0F, 8, 8, 7);
		head.setRotationPoint(-4F, 14F, -10F);

		leg1 = new ModelRenderer(this, 34, 0);
		leg1.addBox(-12F, -1F, -1F, 13, 2, 2);
		leg1.setRotationPoint(-2F, 19F, 1F);

		leg2 = new ModelRenderer(this, 34, 0);
		leg2.addBox(-1F, -1F, -1F, 13, 2, 2);
		leg2.setRotationPoint(2F, 19F, 1F);

		leg3 = new ModelRenderer(this, 34, 0);
		leg3.addBox(-12F, -1F, -1F, 13, 2, 2);
		leg3.setRotationPoint(-2F, 19F, 0F);

		leg4 = new ModelRenderer(this, 34, 0);
		leg4.addBox(-1F, -1F, -1F, 13, 2, 2);
		leg4.setRotationPoint(2F, 19F, 0F);

		leg5 = new ModelRenderer(this, 34, 0);
		leg5.addBox(-12F, -1F, -1F, 13, 2, 2);
		leg5.setRotationPoint(-2F, 19F, -1F);

		leg6 = new ModelRenderer(this, 34, 0);
		leg6.addBox(-1F, -1F, -1F, 13, 2, 2);
		leg6.setRotationPoint(2F, 19F, -1F);

		rightfeeler = new ModelRenderer(this, 50, 18);
		rightfeeler.addBox(0F, 0F, -8F, 1, 1, 6);
		rightfeeler.setRotationPoint(-3F, 15F, -8F);
		rightfeeler.rotateAngleY = -0.1F;
		
		leftfeeler = new ModelRenderer(this, 50, 18);
		leftfeeler.addBox(0F, 0F, -8F, 1, 1, 6);
		leftfeeler.setRotationPoint(2F, 15F, -8F);
		leftfeeler.rotateAngleY = -0.1F;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		body.render(f5);
		head.render(f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
		leg5.render(f5);
		leg6.render(f5);
		rightfeeler.render(f5);
		leftfeeler.render(f5);
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
        float f6 = ((float)Math.PI / 4F) - 1.3F;
        
        leg1.rotateAngleZ = f6;
        leg2.rotateAngleZ = -f6;
        leg3.rotateAngleZ = f6 * 0.74F;
        leg4.rotateAngleZ = -f6 * 0.74F;
        leg5.rotateAngleZ = f6 * 0.74F;
        leg6.rotateAngleZ = -f6 * 0.74F;

        float f7 = -0F;
        float f8 = 0.3926991F;
        leg1.rotateAngleY = f8 * 2F + f7;
        leg2.rotateAngleY = -f8 * 2F - f7;
        leg3.rotateAngleY = f8 * 1F + f7;
        leg4.rotateAngleY = -f8 * 1F - f7;
        leg5.rotateAngleY = -f8 * 1F + f7;
        leg6.rotateAngleY = f8 * 1F - f7;
        
        float f9 = -(MathHelper.cos(f * 0.6662F * 2F + 0F) * 0.4F) * f1;
        float f10 = -(MathHelper.cos(f * 0.6662F * 2F + (float)Math.PI) * 0.4F) * f1;
        float f11 = -(MathHelper.cos(f * 0.6662F * 2F + ((float)Math.PI / 2F)) * 0.4F) * f1;
        float f12 = -(MathHelper.cos(f * 0.6662F * 2F + ((float)Math.PI * 3F / 2F)) * 0.4F) * f1;
        float f13 = Math.abs(MathHelper.sin(f * 0.6662F + 0F) * 0.4F) * f1;
        float f14 = Math.abs(MathHelper.sin(f * 0.6662F + (float)Math.PI) * 0.4F) * f1;
        float f15 = Math.abs(MathHelper.sin(f * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * f1;

        leg1.rotateAngleY += f9;
        leg2.rotateAngleY += -f9;
        leg3.rotateAngleY += f10;
        leg4.rotateAngleY += -f10;
        leg5.rotateAngleY += f11;
        leg6.rotateAngleY += -f11;

        leg1.rotateAngleZ += f13;
        leg2.rotateAngleZ += -f13;
        leg3.rotateAngleZ += f14;
        leg4.rotateAngleZ += -f14;
        leg5.rotateAngleZ += f15;
        leg6.rotateAngleZ += -f15;
	}
}