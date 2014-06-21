package lotr.client.model;

import lotr.common.entity.animal.LOTREntityFlamingo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelFlamingo extends ModelBase
{
	private ModelRenderer head;
	private ModelRenderer body;
	private ModelRenderer tail;
	private ModelRenderer wingLeft;
	private ModelRenderer wingRight;
	private ModelRenderer legLeft;
	private ModelRenderer legRight;
	
	private ModelRenderer head_child;
	private ModelRenderer body_child;
	private ModelRenderer tail_child;
	private ModelRenderer wingLeft_child;
	private ModelRenderer wingRight_child;
	private ModelRenderer legLeft_child;
	private ModelRenderer legRight_child;
	
	public LOTRModelFlamingo()
	{
		head = new ModelRenderer(this, 8, 24);
		head.addBox(-2F, -17F, -2F, 4, 4, 4);
		head.setRotationPoint(0F, 5F, -2F);
		head.setTextureOffset(24, 27).addBox(-1.5F, -16F, -5F, 3, 2, 3);
		head.setTextureOffset(36, 30).addBox(-1F, -14F, -5F, 2, 1, 1);
		head.setTextureOffset(0, 16).addBox(-1F, -15F, -1F, 2, 14, 2);
		
		body = new ModelRenderer(this, 0, 0);
		body.addBox(-3F, 0F, -4F, 6, 7, 8);
		body.setRotationPoint(0F, 3F, 0F);
		
		tail = new ModelRenderer(this, 42, 23);
		tail.addBox(-2.5F, 0F, 0F, 5, 3, 6);
		tail.setRotationPoint(0F, 4F, 3F);
		
		wingLeft = new ModelRenderer(this, 36, 0);
		wingLeft.addBox(-1F, 0F, -3F, 1, 8, 6);
		wingLeft.setRotationPoint(-3F, 3F, 0F);
		
		wingRight = new ModelRenderer(this, 50, 0);
		wingRight.addBox(0F, 0F, -3F, 1, 8, 6);
		wingRight.setRotationPoint(3F, 3F, 0F);
		
		legLeft = new ModelRenderer(this, 30, 0);
		legLeft.addBox(-0.5F, 0F, -0.5F, 1, 16, 1);
		legLeft.setRotationPoint(-2F, 8F, 0F);
		legLeft.setTextureOffset(30, 17).addBox(-1.5F, 14.9F, -3.5F, 3, 1, 3);
		
		legRight = new ModelRenderer(this, 30, 0);
		legRight.addBox(-0.5F, 0F, -0.5F, 1, 16, 1);
		legRight.setRotationPoint(2F, 8F, 0F);
		legRight.setTextureOffset(30, 17).addBox(-1.5F, 14.9F, -3.5F, 3, 1, 3);
		
		head_child = new ModelRenderer(this, 0, 24);
		head_child.addBox(-2F, -4F, -4F, 4, 4, 4);
		head_child.setRotationPoint(0F, 15F, -3F);
		head_child.setTextureOffset(16, 28).addBox(-1F, -2F, -6F, 2, 2, 2);
		
		body_child = new ModelRenderer(this, 0, 0);
		body_child.addBox(-3F, 0F, -4F, 6, 5, 7);
		body_child.setRotationPoint(0F, 14F, 0F);
		
		tail_child = new ModelRenderer(this, 0, 14);
		tail_child.addBox(-2F, 0F, 0F, 4, 2, 3);
		tail_child.setRotationPoint(0F, 14.5F, 3F);
		
		wingLeft_child = new ModelRenderer(this, 40, 0);
		wingLeft_child.addBox(-1F, 0F, -3F, 1, 4, 5);
		wingLeft_child.setRotationPoint(-3F, 14F, 0F);
		
		wingRight_child = new ModelRenderer(this, 52, 0);
		wingRight_child.addBox(0F, 0F, -3F, 1, 4, 5);
		wingRight_child.setRotationPoint(3F, 14F, 0F);
		
		legLeft_child = new ModelRenderer(this, 27, 0);
		legLeft_child.addBox(-0.5F, 0F, -0.5F, 1, 5, 1);
		legLeft_child.setRotationPoint(-2F, 19F, 0F);
		legLeft_child.setTextureOffset(27, 7).addBox(-1.5F, 3.9F, -3.5F, 3, 1, 3);
		
		legRight_child = new ModelRenderer(this, 27, 0);
		legRight_child.addBox(-0.5F, 0F, -0.5F, 1, 5, 1);
		legRight_child.setRotationPoint(2F, 19F, 0F);
		legRight_child.setTextureOffset(27, 7).addBox(-1.5F, 3.9F, -3.5F, 3, 1, 3);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (isChild)
		{
			head_child.render(f5);
			body_child.render(f5);
			tail_child.render(f5);
			wingLeft_child.render(f5);
			wingRight_child.render(f5);
			legLeft_child.render(f5);
			legRight_child.render(f5);
		}
		else
		{
			head.render(f5);
			body.render(f5);
			tail.render(f5);
			wingLeft.render(f5);
			wingRight.render(f5);
			legLeft.render(f5);
			legRight.render(f5);
		}
	}
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
		if (isChild)
		{
			head_child.rotateAngleX = f4 / 57.29578F;
			head_child.rotateAngleY = f3 / 57.29578F;
			legLeft_child.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.9F * f1;
			legRight_child.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 0.9F * f1;
			wingLeft_child.rotateAngleZ = f2 * 0.4F;
			wingRight_child.rotateAngleZ = -f2 * 0.4F;
			tail_child.rotateAngleX = -0.25F;
		}
		else
		{
			head.rotateAngleX = f4 / 57.29578F;
			head.rotateAngleY = f3 / 57.29578F;
			legLeft.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.9F * f1;
			legRight.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 0.9F * f1;
			wingLeft.rotateAngleZ = f2 * 0.4F;
			wingRight.rotateAngleZ = -f2 * 0.4F;
			tail.rotateAngleX = -0.25F;
			
			int i = ((LOTREntityFlamingo)entity).getFishingTick();
			if (i > 100)
			{
				head.rotateAngleX = (float)Math.PI / 20F * (float)(120 - i);
			}
			else if (i > 20)
			{
				head.rotateAngleX = (float)Math.PI;
			}
			else if (i > 0)
			{
				head.rotateAngleX = (float)Math.PI / 20F * (float)i;
			}
		}
    }
}
