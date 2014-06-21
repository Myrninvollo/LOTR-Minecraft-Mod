package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;

public class LOTRModelMorgulHelmet extends LOTRModelBiped
{
	private ModelRenderer[] spikes = new ModelRenderer[8];
	
	public LOTRModelMorgulHelmet()
	{
		this(0F);
	}
	
	public LOTRModelMorgulHelmet(float f)
	{
		super(f);
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 12, 8, f);
		bipedHead.setRotationPoint(0F, 0F, 0F);
		bipedHead.setTextureOffset(0, 20).addBox(-3.5F, -18F, -3.5F, 7, 10, 1, f);
		
		for (int i = 0; i < spikes.length; i++)
		{
			spikes[i] = new ModelRenderer(this, 16, 20);
			spikes[i].setRotationPoint(0F, 0F, 0F);
		}

		spikes[0].addBox(-1F, -5.5F, -10F, 1, 1, 4);
		spikes[0].rotateAngleX = (float)Math.toRadians(-20D);
		spikes[0].rotateAngleY = (float)Math.toRadians(20D);
		spikes[1].addBox(0F, -5.5F, -10F, 1, 1, 4);
		spikes[1].rotateAngleX = (float)Math.toRadians(-20D);
		spikes[1].rotateAngleY = (float)Math.toRadians(-20D);
		spikes[2].addBox(6F, -5.5F, -1F, 4, 1, 1);
		spikes[2].rotateAngleZ = (float)Math.toRadians(-20D);
		spikes[2].rotateAngleY = (float)Math.toRadians(20D);
		spikes[3].addBox(6F, -5.5F, 0F, 4, 1, 1);
		spikes[3].rotateAngleZ = (float)Math.toRadians(-20D);
		spikes[3].rotateAngleY = (float)Math.toRadians(-20D);
		spikes[4].addBox(0F, -5.5F, 6F, 1, 1, 4);
		spikes[4].rotateAngleX = (float)Math.toRadians(20D);
		spikes[4].rotateAngleY = (float)Math.toRadians(20D);
		spikes[5].addBox(-1F, -5.5F, 6F, 1, 1, 4);
		spikes[5].rotateAngleX = (float)Math.toRadians(20D);
		spikes[5].rotateAngleY = (float)Math.toRadians(-20D);
		spikes[6].addBox(-10F, -5.5F, 0F, 4, 1, 1);
		spikes[6].rotateAngleZ = (float)Math.toRadians(20D);
		spikes[6].rotateAngleY = (float)Math.toRadians(20D);
		spikes[7].addBox(-10F, -5.5F, -1F, 4, 1, 1);
		spikes[7].rotateAngleZ = (float)Math.toRadians(20D);
		spikes[7].rotateAngleY = (float)Math.toRadians(-20D);
		
		for (int i = 0; i < spikes.length; i++)
		{
			bipedHead.addChild(spikes[i]);
		}

		bipedBody.showModel = false;
		bipedRightArm.showModel = false;
		bipedLeftArm.showModel = false;
		bipedRightLeg.showModel = false;
		bipedLeftLeg.showModel = false;
	}
}
