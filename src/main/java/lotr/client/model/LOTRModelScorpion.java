package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelScorpion extends LOTRModelSpider
{
	private ModelRenderer armRight;
	private ModelRenderer armLeft;
	private ModelRenderer tail;
	
	public LOTRModelScorpion()
	{
		super();
		
        abdomen.showModel = false;
        
        armRight = new ModelRenderer(this, 36, 16);
        armRight.addBox(-16F, -1F, 0F, 16, 2, 2);
        armRight.setRotationPoint(-3F, 18.5F, -4F);
        
        ModelRenderer clawRight = new ModelRenderer(this, 0, 12);
        clawRight.addBox(-13F, -2F, -16F, 4, 3, 5);
        clawRight.addBox(-13F, -1F, -20F, 1, 1, 4);
        clawRight.addBox(-10F, -1F, -20F, 1, 1, 4);
        clawRight.rotateAngleY = (float)Math.toRadians(50D);
        
        armRight.addChild(clawRight);
        
        armLeft = new ModelRenderer(this, 36, 16);
        armLeft.mirror = true;
        armLeft.addBox(0F, -1F, 0F, 16, 2, 2);
        armLeft.setRotationPoint(3F, 18.5F, -4F);
        
        ModelRenderer clawLeft = new ModelRenderer(this, 0, 12);
        clawLeft.mirror = true;
        clawLeft.addBox(9F, -2F, -16F, 4, 3, 5);
        clawLeft.addBox(12F, -1F, -20F, 1, 1, 4);
        clawLeft.addBox(9F, -1F, -20F, 1, 1, 4);
        clawLeft.rotateAngleY = (float)Math.toRadians(-50D);
        
        armLeft.addChild(clawLeft);
        
        tail = new ModelRenderer(this, 0, 12);
        tail.addBox(-2.5F, -3F, 0F, 5, 5, 11);
        tail.setRotationPoint(0F, 19.5F, 3F);
        
        ModelRenderer tail1 = new ModelRenderer(this, 0, 12);
        tail1.addBox(-2F, -2F, 0F, 4, 4, 10);
        tail1.setRotationPoint(0F, -0.5F, 11F);
        tail1.rotateAngleX = (float)Math.toRadians(40D); 
        tail.addChild(tail1);
        
        ModelRenderer tail2 = new ModelRenderer(this, 0, 12);
        tail2.addBox(-1.5F, -2F, 0F, 3, 4, 10);
        tail2.setRotationPoint(0F, 0F, 11F);
        tail2.rotateAngleX = (float)Math.toRadians(40D); 
        tail1.addChild(tail2);
        
        ModelRenderer sting = new ModelRenderer(this, 0, 12);
        sting.addBox(-1F, -0.5F, 0F, 2, 3, 5);
        sting.addBox(-0.5F, 0F, 5F, 1, 1, 3);
        sting.setRotationPoint(0F, 0F, 9F);
        sting.rotateAngleX = (float)Math.toRadians(90D); 
        tail2.addChild(sting);
	}
	
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        armRight.render(f5);
        armLeft.render(f5);
        tail.render(f5);
    }
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        
        armRight.rotateAngleY = (float)Math.toRadians(-50D) + MathHelper.cos(f * 0.4F) * f1 * 0.4F;
        armRight.rotateAngleY += f2 * (float)Math.toRadians(-40D);
        armLeft.rotateAngleY = -armRight.rotateAngleY;
        
        tail.rotateAngleX = (float)Math.toRadians(30D) + MathHelper.cos(f * 0.4F) * f1 * 0.15F;
        tail.rotateAngleX += f2 * (float)Math.toRadians(90D);
    }
}
