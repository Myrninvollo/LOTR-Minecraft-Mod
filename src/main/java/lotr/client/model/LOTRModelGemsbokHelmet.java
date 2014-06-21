package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;

public class LOTRModelGemsbokHelmet extends LOTRModelBiped
{
	private ModelRenderer hornRight;
	private ModelRenderer hornLeft;
	
	public LOTRModelGemsbokHelmet()
	{
		this(0F);
	}
	
	public LOTRModelGemsbokHelmet(float f)
	{
		super(f);
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, f);
		bipedHead.setRotationPoint(0F, 0F, 0F);
		
		hornRight = new ModelRenderer(this, 32, 0);
		hornRight.addBox(-4.9F, -7F, 7.5F, 1, 1, 13);
		
		hornLeft = new ModelRenderer(this, 32, 0);
		hornLeft.mirror = true;
		hornLeft.addBox(3.9F, -7F, 7.5F, 1, 1, 13);
		
		hornRight.rotateAngleX = hornLeft.rotateAngleX = (float)Math.toRadians(20D);
				
		bipedHead.addChild(hornRight);
		bipedHead.addChild(hornLeft);

		bipedHeadwear.showModel = false;
		bipedBody.showModel = false;
		bipedRightArm.showModel = false;
		bipedLeftArm.showModel = false;
		bipedRightLeg.showModel = false;
		bipedLeftLeg.showModel = false;
	}
}
