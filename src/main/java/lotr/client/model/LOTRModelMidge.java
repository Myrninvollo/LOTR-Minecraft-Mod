package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelMidge extends ModelBase
{
    private ModelRenderer body;
    private ModelRenderer rightWing;
    private ModelRenderer leftWing;

    public LOTRModelMidge()
    {
        body = new ModelRenderer(this, 0, 0);
        body.addBox(-0.5F, -1.5F, -0.5F, 1, 5, 1);
		
        rightWing = new ModelRenderer(this, 0, 6);
        rightWing.addBox(-5F, -2.5F, 0F, 5, 5, 1);
		
        leftWing = new ModelRenderer(this, 0, 6);
        leftWing.mirror = true;
        leftWing.addBox(0F, -2.5F, 0F, 5, 5, 1);
		
        body.addChild(rightWing);
        body.addChild(leftWing);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
		body.setRotationPoint(0F, 8F, 0F);
		body.rotateAngleX = ((float)Math.PI / 4F) + MathHelper.cos(f2 * 0.1F) * 0.15F;
		rightWing.rotateAngleY = MathHelper.cos(f2 * 4F) * (float)Math.PI * 0.25F;
		leftWing.rotateAngleY = -rightWing.rotateAngleY;
        body.render(f5);
    }
}
