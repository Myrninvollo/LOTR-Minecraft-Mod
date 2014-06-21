package lotr.client.model;

import lotr.common.entity.npc.LOTREntityWarg;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelWarg extends ModelBase
{
	public ModelRenderer body;
	public ModelRenderer tail;
	public ModelRenderer head;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;
	
	public LOTRModelWarg()
	{
		this(0F);
	}
	
	public LOTRModelWarg(float f)
	{
		body = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
		body.addBox(-8F, -2F, -14F, 16, 14, 14, f);
		body.setRotationPoint(0F, 2F, 1F);
		body.setTextureOffset(0, 28).addBox(-6.5F, 0F, 0F, 13, 11, 18, f);
		
		tail = new ModelRenderer(this, 98, 55).setTextureSize(128, 64);
		tail.addBox(-1F, -1F, 0F, 2, 1, 8, f);
		tail.setRotationPoint(0F, 4F, 18F);
		
		head = new ModelRenderer(this, 92, 0).setTextureSize(128, 64);
		head.addBox(-5F, -5F, -8F, 10, 10, 8, f);
		head.setRotationPoint(0F, 8F, -13F);
		head.setTextureOffset(108, 18).addBox(-3F, -1F, -12F, 6, 5, 4, f);
		head.setTextureOffset(102, 18).addBox(-4F, -7.8F, -3F, 2, 3, 1, f);
		head.setTextureOffset(102, 18).addBox(2F, -7.8F, -3F, 2, 3, 1, f);
		
		leg1 = new ModelRenderer(this, 62, 0).setTextureSize(128, 64);
		leg1.mirror = true;
		leg1.addBox(-6F, -1F, -2.5F, 6, 9, 8, f);
		leg1.setRotationPoint(-4F, 6F, 12F);
		leg1.setTextureOffset(66, 17).addBox(-5.5F, 8F, -1F, 5, 10, 5, f);
		
		leg2 = new ModelRenderer(this, 62, 0).setTextureSize(128, 64);
		leg2.addBox(0F, -1F, -2.5F, 6, 9, 8, f);
		leg2.setRotationPoint(4F, 6F, 12F);
		leg2.setTextureOffset(66, 17).addBox(0.5F, 8F, -1F, 5, 10, 5, f);
		
		leg3 = new ModelRenderer(this, 62, 0).setTextureSize(128, 64);
		leg3.mirror = true;
		leg3.addBox(-6F, -1F, -2.5F, 6, 9, 8, f);
		leg3.setRotationPoint(-6F, 5F, -8F);
		leg3.setTextureOffset(66, 17).addBox(-5.5F, 8F, -1F, 5, 11, 5, f);
		
		leg4 = new ModelRenderer(this, 62, 0).setTextureSize(128, 64);
		leg4.addBox(0F, -1F, -2.5F, 6, 9, 8, f);
		leg4.setRotationPoint(6F, 5F, -8F);
		leg4.setTextureOffset(66, 17).addBox(0.5F, 8F, -1F, 5, 11, 5, f);
	}
	
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		body.render(f5);
		tail.render(f5);
		head.render(f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
    }
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
		head.rotateAngleX = f4 / (180F / (float)Math.PI);
		head.rotateAngleY = f3 / (180F / (float)Math.PI);
		
        leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.9F * f1;
        leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 0.9F * f1;
        leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 0.9F * f1;
        leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.9F * f1;
		
		tail.rotateAngleX = ((LOTREntityWarg)entity).getTailRotation();
    }
}
