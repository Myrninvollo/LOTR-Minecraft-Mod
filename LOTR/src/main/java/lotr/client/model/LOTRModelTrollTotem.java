package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class LOTRModelTrollTotem extends ModelBase
{
	private ModelRenderer head;
	private ModelRenderer jaw;

	private ModelRenderer body;
	private ModelRenderer rightArm;
	private ModelRenderer leftArm;
	
	private ModelRenderer rightLeg;
	private ModelRenderer leftLeg;
	private ModelRenderer base;
	
	public LOTRModelTrollTotem()
	{
		textureWidth = 128;
		textureHeight = 64;
		
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-6F, -10F, -10F, 12, 10, 12);
		head.setRotationPoint(0F, 22F, 4F);
		head.addBox(-1F, -5F, -12F, 2, 3, 2);
		head.setTextureOffset(40, 0).addBox(-7F, -6F, -6F, 1, 4, 3);
		head.mirror = true;
		head.addBox(6F, -6F, -6F, 1, 4, 3);
		
		jaw = new ModelRenderer(this, 48, 0);
		jaw.addBox(-6F, -2F, -6F, 12, 2, 12);
		jaw.setRotationPoint(0F, 24F, 0F);
		
		body = new ModelRenderer(this, 0, 24);
		body.addBox(-5F, 0F, -5F, 10, 16, 10);
		body.setRotationPoint(0F, 8F, 0F);
		
		rightArm = new ModelRenderer(this, 40, 24);
		rightArm.addBox(-3F, 0F, -3F, 3, 10, 6);
		rightArm.setRotationPoint(-5F, 9F, 0F);
		
		leftArm = new ModelRenderer(this, 40, 24);
		leftArm.mirror = true;
		leftArm.addBox(0F, 0F, -3F, 3, 10, 6);
		leftArm.setRotationPoint(5F, 9F, 0F);
		
		rightLeg = new ModelRenderer(this, 0, 50);
		rightLeg.addBox(-3F, 0F, -3F, 6, 7, 6);
		rightLeg.setRotationPoint(-4F, 8F, 0F);
		rightLeg.setTextureOffset(24, 50).addBox(-2.5F, 7F, -2.5F, 5, 7, 5);
		
		leftLeg = new ModelRenderer(this, 0, 50);
		leftLeg.mirror = true;
		leftLeg.addBox(-3F, 0F, -3F, 6, 7, 6);
		leftLeg.setRotationPoint(4F, 8F, 0F);
		leftLeg.setTextureOffset(24, 50).addBox(-2.5F, 7F, -2.5F, 5, 7, 5);
		
		base = new ModelRenderer(this, 48, 46);
		base.addBox(-8F, 0F, -8F, 16, 2, 16);
		base.setRotationPoint(0F, 22, 0F);
	}
	
	public void renderHead(float f, float f1)
	{
		head.rotateAngleX = (f1 / 180F) * (float)Math.PI;
		head.render(f);
		jaw.render(f);
	}
	
	public void renderBody(float f)
	{
		body.render(f);
		rightArm.render(f);
		leftArm.render(f);
	}
	
	public void renderBase(float f)
	{
		rightLeg.render(f);
		leftLeg.render(f);
		base.render(f);
	}
}
