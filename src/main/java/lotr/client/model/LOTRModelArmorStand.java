package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelArmorStand extends ModelBase
{
	private ModelRenderer base;
	private ModelRenderer head;
	private ModelRenderer spine;
	private ModelRenderer rightArm;
	private ModelRenderer leftArm;
	private ModelRenderer rightHip;
	private ModelRenderer leftHip;
	private ModelRenderer rightLeg;
	private ModelRenderer leftLeg;
	private ModelRenderer rightFoot;
	private ModelRenderer leftFoot;
	
	public LOTRModelArmorStand()
	{
		base = new ModelRenderer(this, 0, 0);
		base.addBox(-8F, -8F, -8F, 16, 16, 2);
		base.setRotationPoint(0F, 30F, 0F);
		base.rotateAngleX = (float)-Math.PI / 2F;
		
		head = new ModelRenderer(this, 40, 0);
		head.addBox(-3F, 0F, -2F, 6, 8, 4);
		head.setRotationPoint(0F, -11F, 0F);
		
		spine = new ModelRenderer(this, 60, 0);
		spine.addBox(-0.5F, 0F, -0.5F, 1, 25, 1);
		spine.setRotationPoint(0F, -3F, 0F);
		
		rightArm = new ModelRenderer(this, 44, 12);
		rightArm.addBox(-7.5F, 0F, -0.5F, 7, 1, 1);
		rightArm.setRotationPoint(0F, -2F, 0F);
		
		leftArm = new ModelRenderer(this, 44, 12);
		leftArm.mirror = true;
		leftArm.addBox(0.5F, 0F, -0.5F, 7, 1, 1);
		leftArm.setRotationPoint(0F, -2F, 0F);
		
		rightHip = new ModelRenderer(this, 42, 30);
		rightHip.addBox(-3.5F, 0F, -0.5F, 3, 1, 1);
		rightHip.setRotationPoint(0F, 9F, 0F);
		
		leftHip = new ModelRenderer(this, 42, 30);
		leftHip.mirror = true;
		leftHip.addBox(0.5F, 0F, -0.5F, 3, 1, 1);
		leftHip.setRotationPoint(0F, 9F, 0F);
		
		rightLeg = new ModelRenderer(this, 50, 23);
		rightLeg.addBox(-0.5F, 0F, -0.5F, 1, 8, 1);
		rightLeg.setRotationPoint(-2F, 10F, 0F);
		
		leftLeg = new ModelRenderer(this, 50, 23);
		leftLeg.mirror = true;
		leftLeg.addBox(-0.5F, 0F, -0.5F, 1, 8, 1);
		leftLeg.setRotationPoint(2F, 10F, 0F);
		
		rightFoot = new ModelRenderer(this, 54, 27);
		rightFoot.addBox(-2F, 0F, -1F, 3, 3, 2);
		rightFoot.setRotationPoint(-2F, 18F, 0F);
		
		leftFoot = new ModelRenderer(this, 54, 27);
		leftFoot.mirror = true;
		leftFoot.addBox(-1F, 0F, -1F, 3, 3, 2);
		leftFoot.setRotationPoint(2F, 18F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		base.render(f5);
		head.render(f5);
		spine.render(f5);
		rightArm.render(f5);
		leftArm.render(f5);
		rightHip.render(f5);
		leftHip.render(f5);
		rightLeg.render(f5);
		leftLeg.render(f5);
		rightFoot.render(f5);
		leftFoot.render(f5);
	}
}
