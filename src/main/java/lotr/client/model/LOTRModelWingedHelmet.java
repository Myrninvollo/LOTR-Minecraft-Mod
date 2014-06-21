package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;

public class LOTRModelWingedHelmet extends LOTRModelBiped
{
	public LOTRModelWingedHelmet()
	{
		this(0F);
	}
	
	public LOTRModelWingedHelmet(float f)
	{
		super(f);
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, f);
		bipedHead.setRotationPoint(0F, 0F, 0F);
		bipedHead.setTextureOffset(0, 16).addBox(-6.5F, -4F, -0.5F, 2, 2, 1, 0F);
		bipedHead.setTextureOffset(6, 16).addBox(-7.5F, -13F, -0.5F, 3, 9, 1, 0F);
		bipedHead.setTextureOffset(14, 16).addBox(-6F, -17F, -0.5F, 2, 4, 1, 0F);
		bipedHead.mirror = true;
		bipedHead.setTextureOffset(0, 16).addBox(4.5F, -4F, -0.5F, 2, 2, 1, 0F);
		bipedHead.setTextureOffset(6, 16).addBox(4.5F, -13F, -0.5F, 3, 9, 1, 0F);
		bipedHead.setTextureOffset(14, 16).addBox(4F, -17F, -0.5F, 2, 4, 1, 0F);

		bipedBody.showModel = false;
		bipedRightArm.showModel = false;
		bipedLeftArm.showModel = false;
		bipedRightLeg.showModel = false;
		bipedLeftLeg.showModel = false;
	}
}
