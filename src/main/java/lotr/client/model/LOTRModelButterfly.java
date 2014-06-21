package lotr.client.model;

import lotr.common.entity.animal.LOTREntityButterfly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelButterfly extends ModelBase
{
    private ModelRenderer body;
    private ModelRenderer rightWing;
    private ModelRenderer leftWing;

    public LOTRModelButterfly()
    {
        body = new ModelRenderer(this, 0, 0);
        body.addBox(-1F, -6F, -1F, 2, 12, 2);
		
        rightWing = new ModelRenderer(this, 8, 0);
        rightWing.addBox(-12F, -10F, 0F, 12, 20, 1);
		
        leftWing = new ModelRenderer(this, 8, 0);
        leftWing.mirror = true;
        leftWing.addBox(0F, -10F, 0F, 12, 20, 1);
		
        body.addChild(rightWing);
        body.addChild(leftWing);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        LOTREntityButterfly butterfly = (LOTREntityButterfly)entity;

        if (butterfly.isButterflyStill())
        {
			body.setRotationPoint(0F, 24F, 0F);
            body.rotateAngleX = (float)Math.PI / 2F;
			if (butterfly.flapTime > 0)
			{
				rightWing.rotateAngleY = MathHelper.cos(f2 * 1.3F) * (float)Math.PI * 0.25F;
			}
			else
			{
				rightWing.rotateAngleY = ((float)Math.PI * 2F / 20F);
			}
			leftWing.rotateAngleY = -rightWing.rotateAngleY;
        }
        else
        {
			body.setRotationPoint(0F, 8F, 0F);
            body.rotateAngleX = ((float)Math.PI / 4F) + MathHelper.cos(f2 * 0.1F) * 0.15F;
            rightWing.rotateAngleY = MathHelper.cos(f2 * 1.3F) * (float)Math.PI * 0.25F;
            leftWing.rotateAngleY = -rightWing.rotateAngleY;
        }

        body.render(f5);
    }
}
