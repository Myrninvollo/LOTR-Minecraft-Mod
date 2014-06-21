package lotr.client.model;

import lotr.common.entity.animal.LOTREntityCamel;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelCamel extends ModelBase
{
	private ModelRenderer body;
	private ModelRenderer humps;
	private ModelRenderer tail;
	private ModelRenderer head;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
	private ModelRenderer chest;
	
	public LOTRModelCamel()
	{
		this(0F);
	}
	
	public LOTRModelCamel(float f)
	{
		textureWidth = 64;
		textureHeight = 64;
		
		body = new ModelRenderer(this, 0, 16);
		body.setRotationPoint(0F, 10F, 0F);
		body.addBox(-4.5F, -5F, -10F, 9, 10, 22, f);
		
		humps = new ModelRenderer(this, 34, 0);
		humps.setRotationPoint(0F, 10F, 0F);
		humps.addBox(-3F, -9F, -8F, 6, 4, 6, f);
		humps.addBox(-3F, -9F, 5F, 6, 4, 6, f);
		
		tail = new ModelRenderer(this, 54, 52);
		tail.setRotationPoint(0F, 7F, 12F);
		tail.addBox(-1F, -1F, 0F, 2, 10, 2);
		
		head = new ModelRenderer(this, 0, 0);
		head.setRotationPoint(0F, 6F, -10F);
		head.addBox(-3F, -13F, -10.5F, 6, 5, 11, f);
		head.addBox(-2.5F, -15F, -1F, 2, 2, 1, f);
		head.mirror = true;
		head.addBox(0.5F, -15F, -1F, 2, 2, 1, f);
		head.mirror = false;
		head.setTextureOffset(0, 16).addBox(-2.5F, -9F, -5F, 5, 14, 5, f);
		
		leg1 = new ModelRenderer(this, 0, 52);
		leg1.setRotationPoint(-4.5F, 7F, 8F);
		leg1.addBox(-4F, -1F, -2.5F, 4, 7, 5, f);
		leg1.setTextureOffset(18, 53).addBox(-3.5F, 6F, -1.5F, 3, 8, 3, f);
		leg1.setTextureOffset(30, 57).addBox(-4F, 14F, -2F, 4, 3, 4, f);
		
		leg2 = new ModelRenderer(this, 0, 52);
		leg2.mirror = true;
		leg2.setRotationPoint(4.5F, 7F, 8F);
		leg2.addBox(0F, -1F, -2.5F, 4, 7, 5, f);
		leg2.setTextureOffset(18, 53).addBox(0.5F, 6F, -1.5F, 3, 8, 3, f);
		leg2.setTextureOffset(30, 57).addBox(0F, 14F, -2F, 4, 3, 4, f);
		
		leg3 = new ModelRenderer(this, 0, 52);
		leg3.setRotationPoint(-4.5F, 7F, -5F);
		leg3.addBox(-4F, -1F, -2.5F, 4, 7, 5, f);
		leg3.setTextureOffset(18, 53).addBox(-3.5F, 6F, -1.5F, 3, 8, 3, f);
		leg3.setTextureOffset(30, 57).addBox(-4F, 14F, -2F, 4, 3, 4, f);
		
		leg4 = new ModelRenderer(this, 0, 52);
		leg4.mirror = true;
		leg4.setRotationPoint(4.5F, 7F, -5F);
		leg4.addBox(0F, -1F, -2.5F, 4, 7, 5, f);
		leg4.setTextureOffset(18, 53).addBox(0.5F, 6F, -1.5F, 3, 8, 3, f);
		leg4.setTextureOffset(30, 57).addBox(0F, 14F, -2F, 4, 3, 4, f);
		
		chest = new ModelRenderer(this, 40, 22);
		chest.setRotationPoint(0F, 10F, 0F);
		chest.addBox(-7.5F, -4.5F, -2.5F, 3, 8, 8, f);
		chest.mirror = true;
		chest.addBox(4.5F, -4.5F, -2.5F, 3, 8, 8, f);
	}
	
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
		LOTREntityCamel camel = (LOTREntityCamel)entity;
		
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (isChild)
        {
        	float f6 = 2F;
        	GL11.glPushMatrix();
        	GL11.glScalef(1F / f6, 1F / f6, 1F / f6);
        	GL11.glTranslatef(0F, 24F * f5, 0F);
        	body.render(f5);
        	tail.render(f5);
        	head.render(f5);
        	leg1.render(f5);
        	leg2.render(f5);
        	leg3.render(f5);
        	leg4.render(f5);
        	GL11.glPopMatrix();
        }
        else
        {
        	body.render(f5);
        	humps.render(f5);
        	tail.render(f5);
        	head.render(f5);
        	leg1.render(f5);
        	leg2.render(f5);
        	leg3.render(f5);
        	leg4.render(f5);
        	
        	if (camel.hasChest())
        	{
        		chest.render(f5);
        	}
        }
    }
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        head.rotateAngleX = f4 / (float)Math.toDegrees(1D);
        head.rotateAngleY = f3 / (float)Math.toDegrees(1D);
        
        head.rotateAngleX += MathHelper.cos(f * 0.3331F) * 0.1F * f1;
		
		leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.8F * f1;
		leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 0.8F * f1;
		leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 0.8F * f1;
		leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.8F * f1;
		
		tail.rotateAngleZ = 0.1F * MathHelper.cos(f * 0.3331F + (float)Math.PI) * f1;
    }
}
