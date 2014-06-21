package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class LOTRModelLion extends ModelBase
{
    private ModelRenderer head;
    private ModelRenderer mane;
    private ModelRenderer body;
    private ModelRenderer leg1;
    private ModelRenderer leg2;
    private ModelRenderer leg3;
    private ModelRenderer leg4;
    
    public LOTRModelLion()
    {
        head = new ModelRenderer(this, 0, 0).setTextureSize(64, 96);
        head.addBox(-4F, -4F, -7F, 8, 8, 8, 0F); 
        head.setRotationPoint(0F, 4F, -9F);
		head.setTextureOffset(52, 34).addBox(-2F, 0F, -9F, 4, 4, 2);

        mane = new ModelRenderer(this, 0, 36).setTextureSize(64, 96);
        mane.addBox(-7F, -7F, -5F, 14, 14, 9, 0F);  
        mane.setRotationPoint(0F, 4F, -9F); 
        
        body = new ModelRenderer(this, 0, 68).setTextureSize(64, 96);
        body.addBox(-6F, -10F, -7F, 12, 18, 10, 0F);
		body.setRotationPoint(0F, 5F, 2F);
		
        leg1 = new ModelRenderer(this, 0, 19).setTextureSize(64, 96);
        leg1.addBox(-2F, 0F, -2F, 4, 12, 4, 0F);
        leg1.setRotationPoint(-4F, 12F, 7F);
        
        leg2 = new ModelRenderer(this, 0, 19).setTextureSize(64, 96);
        leg2.addBox(-2F, 0F, -2F, 4, 12, 4, 0F);
        leg2.setRotationPoint(4F, 12F, 7F);
        
        leg3 = new ModelRenderer(this, 0, 19).setTextureSize(64, 96);
        leg3.addBox(-2F, 0F, -2F, 4, 12, 4, 0F);
        leg3.setRotationPoint(-4F, 12F, -5F);
        
        leg4 = new ModelRenderer(this, 0, 19).setTextureSize(64, 96);
        leg4.addBox(-2F, 0F, -2F, 4, 12, 4, 0F);
        leg4.setRotationPoint(4F, 12F, -5F);
    }

	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (isChild)
        {
            float f6 = 2F;
            GL11.glPushMatrix();
            GL11.glTranslatef(0F, 8F * f5, 4F * f5);
            head.render(f5);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1F / f6, 1F / f6, 1F / f6);
            GL11.glTranslatef(0F, 24F * f5, 0F);
            body.render(f5);
            leg1.render(f5);
            leg2.render(f5);
            leg3.render(f5);
            leg4.render(f5);
            GL11.glPopMatrix();
        }
		else
        {
            head.render(f5);
            mane.render(f5);
            body.render(f5);
            leg1.render(f5);
            leg2.render(f5);
            leg3.render(f5);
            leg4.render(f5);
        }
    }

	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        head.rotateAngleX = f4 / 57.29578F;
        head.rotateAngleY = f3 / 57.29578F;
        mane.rotateAngleX = f4 / 57.29578F;
        mane.rotateAngleY = f3 / 57.29578F;

		body.rotateAngleX = ((float)Math.PI) / 2F;
		
		leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}
