package lotr.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;

public class LOTRModelSkeleton extends ModelZombie
{
    public LOTRModelSkeleton()
    {
        this(0.0F);
    }

    public LOTRModelSkeleton(float f)
    {
        super(f, 0.0F, 64, 32);
		if (f == 0F)
		{
			bipedRightArm = new ModelRenderer(this, 40, 16);
			bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, f);
			bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
			bipedLeftArm = new ModelRenderer(this, 40, 16);
			bipedLeftArm.mirror = true;
			bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, f);
			bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
			bipedRightLeg = new ModelRenderer(this, 0, 16);
			bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, f);
			bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
			bipedLeftLeg = new ModelRenderer(this, 0, 16);
			bipedLeftLeg.mirror = true;
			bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, f);
			bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		}
    }
}
