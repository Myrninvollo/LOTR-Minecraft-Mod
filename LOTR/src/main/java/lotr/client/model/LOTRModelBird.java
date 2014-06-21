package lotr.client.model;

import lotr.common.entity.animal.LOTREntityBird;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelBird extends ModelBase
{
	private ModelRenderer body;
	private ModelRenderer head;
	private ModelRenderer wingRight;
	private ModelRenderer wingLeft;
	private ModelRenderer legRight;
	private ModelRenderer legLeft;
	
	public LOTRModelBird()
	{
		body = new ModelRenderer(this, 0, 7);
		body.addBox(-1.5F, -2F, -2F, 3, 3, 5);
		body.setTextureOffset(8, 0).addBox(-1F, -1.5F, 3F, 2, 1, 3);
		body.setTextureOffset(8, 4).addBox(-1F, -0.5F, 3F, 2, 1, 2);
		body.setRotationPoint(0F, 21F, 0F);
		
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-1F, -1.5F, -1.5F, 2, 2, 2);
		head.setTextureOffset(0, 4).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 1);
		head.setTextureOffset(15, 0).addBox(-0.5F, -0.5F, -3.5F, 1, 1, 2);
		head.setRotationPoint(0F, -2F, -2F);
		body.addChild(head);
		
		wingRight = new ModelRenderer(this, 16, 7);
		wingRight.addBox(0F, 0F, -2F, 0, 5, 4);
		wingRight.setRotationPoint(-1.5F, -1.5F, 0.5F);
		body.addChild(wingRight);
		
		wingLeft = new ModelRenderer(this, 16, 7);
		wingLeft.mirror = true;
		wingLeft.addBox(0F, 0F, -2F, 0, 5, 4);
		wingLeft.setRotationPoint(1.5F, -1.5F, 0.5F);
		body.addChild(wingLeft);
		
		legRight = new ModelRenderer(this, 0, 16);
		legRight.addBox(-1F, 0F, -1.5F, 1, 2, 2);
		legRight.setRotationPoint(-0.3F, 1F, 0.5F);
		body.addChild(legRight);
		
		legLeft = new ModelRenderer(this, 0, 16);
		legLeft.mirror = true;
		legLeft.addBox(0F, 0F, -1.5F, 1, 2, 2);
		legLeft.setRotationPoint(0.3F, 1F, 0.5F);
		body.addChild(legLeft);
	}
	
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		body.render(f5);
    }
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        LOTREntityBird bird = (LOTREntityBird)entity;

        if (bird.isBirdStill())
        {
        	body.rotateAngleX = (float)Math.toRadians(-10D);
            head.rotateAngleX = (float)Math.toRadians(20D);

			if (bird.flapTime > 0)
			{
				wingRight.rotateAngleZ = (float)Math.toRadians(90D) + MathHelper.cos(f2 * 1.5F) * (float)Math.toRadians(30D);
			}
			else
			{
				wingRight.rotateAngleZ = (float)Math.toRadians(30D);
			}
			wingLeft.rotateAngleZ = -wingRight.rotateAngleZ;
			
			legRight.rotateAngleX = legLeft.rotateAngleX = -body.rotateAngleX;
			legRight.rotateAngleX += MathHelper.cos(f * 0.6662F) * f1;
			legLeft.rotateAngleX += MathHelper.cos(f * 0.6662F + 3.141593F) * f1;
			legRight.rotationPointY = 1F;
			legLeft.rotationPointY = 1F;
        }
        else
        {
        	body.rotateAngleX = 0F;
            head.rotateAngleX = 0F;
            
            wingRight.rotateAngleZ = (float)Math.toRadians(90D) + MathHelper.cos(f2 * 1.5F) * (float)Math.toRadians(30D);
            wingLeft.rotateAngleZ = -wingRight.rotateAngleZ;
            
            legRight.rotateAngleX = legLeft.rotateAngleX = 0F;
            legRight.rotationPointY = 0F;
			legLeft.rotationPointY = 0F;
        }
    }
}
