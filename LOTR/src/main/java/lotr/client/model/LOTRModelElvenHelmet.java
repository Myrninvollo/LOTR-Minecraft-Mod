package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;

public class LOTRModelElvenHelmet extends LOTRModelBiped
{
	private ModelRenderer crest;
	
	public LOTRModelElvenHelmet()
	{
		this(0F);
	}
	
	public LOTRModelElvenHelmet(float f)
	{
		super(f);
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, f);
		bipedHead.setRotationPoint(0F, 0F, 0F);
		bipedHead.addBox(-0.5F, -11F, -2F, 1, 3, 1, 0F);
		bipedHead.setTextureOffset(0, 4).addBox(-0.5F, -10F, 2F, 1, 2, 1, 0F);
		
		crest = new ModelRenderer(this, 32, 0);
		crest.addBox(-1F, -11F, -8F, 2, 1, 11, 0F);
		crest.setTextureOffset(32, 12).addBox(-1F, -10F, -8F, 2, 1, 1, 0F);
		
		crest.rotateAngleX = (float)Math.toRadians(-16D);
		bipedHead.addChild(crest);

		bipedHeadwear.showModel = false;
		bipedBody.showModel = false;
		bipedRightArm.showModel = false;
		bipedLeftArm.showModel = false;
		bipedRightLeg.showModel = false;
		bipedLeftLeg.showModel = false;
	}
}
