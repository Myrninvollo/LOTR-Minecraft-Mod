package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;

public class LOTRModelOrc extends LOTRModelBiped
{
	private ModelRenderer nose;
	private ModelRenderer earRight;
	private ModelRenderer earLeft;
	
	public LOTRModelOrc()
	{
		this(0F);
	}
	
	public LOTRModelOrc(float f)
	{
		super(f);
		
		nose = new ModelRenderer(this, 14, 17);
		nose.addBox(-0.5F, -4F, -4.8F, 1, 2, 1, f);
		nose.setRotationPoint(0F, 0F, 0F);
		
		earRight = new ModelRenderer(this, 0, 0);
		earRight.addBox(-3.5F, -5.5F, 2F, 1, 2, 3, f);
		earRight.setRotationPoint(0F, 0F, 0F);
		earRight.rotateAngleX = 15F / (180F / (float)Math.PI);
		earRight.rotateAngleY = -30F / (180F / (float)Math.PI);
		earRight.rotateAngleZ = -13F / (180F / (float)Math.PI);
		
		earLeft = new ModelRenderer(this, 24, 0);
		earLeft.addBox(2.5F, -5.5F, 2F, 1, 2, 3, f);
		earLeft.setRotationPoint(0F, 0F, 0F);
		earLeft.rotateAngleX = 15F / (180F / (float)Math.PI);
		earLeft.rotateAngleY = 30F / (180F / (float)Math.PI);
		earLeft.rotateAngleZ = 13F / (180F / (float)Math.PI);
		
		bipedHead.addChild(nose);
		bipedHead.addChild(earRight);
		bipedHead.addChild(earLeft);
	}
}
