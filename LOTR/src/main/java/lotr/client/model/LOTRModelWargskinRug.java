package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelWargskinRug extends ModelBase
{
	private ModelRenderer body;
	private ModelRenderer tail;
	private ModelRenderer head;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
	
	public LOTRModelWargskinRug()
	{
		body = new ModelRenderer(this, 54, 44).setTextureSize(128, 64);
		body.addBox(-9.5F, 0F, 0F, 19, 2, 18);
		body.setRotationPoint(0F, 22F, -2F);
		body.setTextureOffset(0, 0).addBox(-12F, -2F, -14F, 24, 4, 14);
		
		tail = new ModelRenderer(this, 98, 33).setTextureSize(128, 64);
		tail.addBox(-1F, -1F, 0F, 2, 1, 8);
		tail.setRotationPoint(0F, 24F, 15F);
		
		head = new ModelRenderer(this, 92, 0).setTextureSize(128, 64);
		head.addBox(-5F, -5F, -7F, 10, 10, 8);
		head.setRotationPoint(0F, 19F, -15F);
		head.setTextureOffset(108, 18).addBox(-3F, -1F, -11F, 6, 5, 4);
		head.setTextureOffset(102, 18).addBox(-4F, -7.8F, -2F, 2, 3, 1);
		head.setTextureOffset(102, 18).addBox(2F, -7.8F, -2F, 2, 3, 1);
		
		leg1 = new ModelRenderer(this, 0, 47).setTextureSize(128, 64);
		leg1.addBox(-6F, -1F, -2.5F, 6, 9, 8);
		leg1.setRotationPoint(-8F, 25F, 9F);
		leg1.setTextureOffset(28, 48).addBox(-5.5F, 8F, -1F, 5, 10, 5);
		
		leg2 = new ModelRenderer(this, 0, 47).setTextureSize(128, 64);
		leg2.addBox(0F, -1F, -2.5F, 6, 9, 8);
		leg2.setRotationPoint(8F, 25F, 9F);
		leg2.setTextureOffset(28, 48).addBox(0.5F, 8F, -1F, 5, 10, 5);
		
		leg3 = new ModelRenderer(this, 0, 47).setTextureSize(128, 64);
		leg3.addBox(-6F, -1F, -2.5F, 6, 9, 8);
		leg3.setRotationPoint(-10F, 24F, -11F);
		leg3.setTextureOffset(28, 48).addBox(-5.5F, 8F, -1F, 5, 11, 5);
		
		leg4 = new ModelRenderer(this, 0, 47).setTextureSize(128, 64);
		leg4.addBox(0F, -1F, -2.5F, 6, 9, 8);
		leg4.setRotationPoint(10F, 24F, -11F);
		leg4.setTextureOffset(28, 48).addBox(0.5F, 8F, -1F, 5, 11, 5);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
        setRotationAngles();
		body.render(f5);
		tail.render(f5);
		head.render(f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
	}
	
	private void setRotationAngles()
	{
		float f = ((float)Math.PI) / 180F;
		
		leg1.rotateAngleX =	f * 30F;
		leg1.rotateAngleZ = f * 90F;
		
		leg2.rotateAngleX =	f * 30F;
		leg2.rotateAngleZ = f * -90F;
		
		leg3.rotateAngleX =	f * -20F;
		leg3.rotateAngleZ = f * 90F;
		
		leg4.rotateAngleX =	f * -20F;
		leg4.rotateAngleZ = f * -90F;
	}
}
