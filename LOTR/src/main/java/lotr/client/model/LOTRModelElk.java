package lotr.client.model;

import org.lwjgl.opengl.GL11;

import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityElk;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelElk extends ModelBase
{
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
	private ModelRenderer tail;
	private ModelRenderer head;
	private ModelRenderer nose;

	public LOTRModelElk()
	{
		this(0F);
	}
	
	public LOTRModelElk(float f)
	{
		textureWidth = 128;
		textureHeight = 64;
		
		body = new ModelRenderer(this, 0, 0);
		body.setRotationPoint(0F, 5F, -5F);
		body.addBox(-6F, -4F, -7F, 12, 11, 26, f);
		
		leg1 = new ModelRenderer(this, 20, 37);
		leg1.setRotationPoint(-5F, 4F, 8F);
		leg1.addBox(-4.5F, 0F, -3F, 6, 10, 7, f);
		leg1.setTextureOffset(46, 37).addBox(-3.5F, 8F, -1F, 4, 10, 4, f);
		
		leg2 = new ModelRenderer(this, 20, 37);
		leg2.mirror = true;
		leg2.setRotationPoint(5F, 4F, 8F);
		leg2.addBox(-1.5F, 0F, -3F, 6, 10, 7, f);
		leg2.setTextureOffset(46, 37).addBox(-0.5F, 8F, -1F, 4, 10, 4, f);
		
		leg3 = new ModelRenderer(this, 0, 37);
		leg3.setRotationPoint(-5F, 5F, -7F);
		leg3.addBox(-3.5F, 0F, -2.5F, 5, 9, 5, f);
		leg3.setTextureOffset(0, 50).addBox(-2.5F, 8F, -1F, 3, 10, 3, f);
		
		leg4 = new ModelRenderer(this, 0, 37);
		leg4.mirror = true;
		leg4.setRotationPoint(5F, 5F, -7F);
		leg4.addBox(-1.5F, 0F, -2.5F, 5, 9, 5, f);
		leg4.setTextureOffset(0, 50).addBox(-0.5F, 8F, -1F, 3, 10, 3, f);
		
		tail = new ModelRenderer(this, 20, 54);
		tail.setRotationPoint(0F, 4F, 14F);
		tail.addBox(-1F, -1.5F, -0.5F, 2, 2, 8, f);
		
		head = new ModelRenderer(this, 50, 0);
		head.setRotationPoint(0F, 5F, -10F);
		head.addBox(-2F, -10F, -4F, 4, 12, 8, f);
		head.setTextureOffset(74, 0).addBox(-3F, -15F, -8F, 6, 5, 13, f);
		head.setTextureOffset(44, 16);
		head.addBox(-2.5F, -17F, 3F, 2, 2, 1, f);
		head.mirror = true;
		head.addBox(0.5F, -17F, 3F, 2, 2, 1, f);
		
		nose = new ModelRenderer(this, 50, 16);
		nose.addBox(-1F, -14F, -9F, 2, 2, 1, f);
		
		ModelRenderer antlersRight_1 = new ModelRenderer(this, 0, 0);
		antlersRight_1.addBox(10F, -19F, 2.5F, 1, 12, 1, f);
		antlersRight_1.rotateAngleZ = (float)Math.toRadians(-65D);
		
		ModelRenderer antlersRight_2 = new ModelRenderer(this, 4, 0);
		antlersRight_2.addBox(-3F, -23.6F, 2.5F, 1, 8, 1, f);
		antlersRight_2.rotateAngleZ = (float)Math.toRadians(-15D);
		
		ModelRenderer antlersRight_3 = new ModelRenderer(this, 8, 0);
		antlersRight_3.addBox(-8F, -36F, 2.5F, 1, 16, 1, f);
		antlersRight_3.rotateAngleZ = (float)Math.toRadians(-15D);
		
		ModelRenderer antlersRight_4 = new ModelRenderer(this, 12, 0);
		antlersRight_4.addBox(7.5F, -35F, 2.5F, 1, 10, 1, f);
		antlersRight_4.rotateAngleZ = (float)Math.toRadians(-50D);
		
		head.addChild(antlersRight_1);
		head.addChild(antlersRight_2);
		head.addChild(antlersRight_3);
		head.addChild(antlersRight_4);
		
		ModelRenderer antlersLeft_1 = new ModelRenderer(this, 0, 0);
		antlersLeft_1.mirror = true;
		antlersLeft_1.addBox(-11F, -19F, 2.5F, 1, 12, 1, f);
		antlersLeft_1.rotateAngleZ = (float)Math.toRadians(65D);
		
		ModelRenderer antlersLeft_2 = new ModelRenderer(this, 4, 0);
		antlersLeft_2.mirror = true;
		antlersLeft_2.addBox(2F, -23.6F, 2.5F, 1, 8, 1, f);
		antlersLeft_2.rotateAngleZ = (float)Math.toRadians(15D);
		
		ModelRenderer antlersLeft_3 = new ModelRenderer(this, 8, 0);
		antlersLeft_3.mirror = true;
		antlersLeft_3.addBox(7F, -36F, 2.5F, 1, 16, 1, f);
		antlersLeft_3.rotateAngleZ = (float)Math.toRadians(15D);
		
		ModelRenderer antlersLeft_4 = new ModelRenderer(this, 12, 0);
		antlersLeft_4.mirror = true;
		antlersLeft_4.addBox(-8.5F, -35F, 2.5F, 1, 10, 1, f);
		antlersLeft_4.rotateAngleZ = (float)Math.toRadians(50D);
		
		head.addChild(antlersLeft_1);
		head.addChild(antlersLeft_2);
		head.addChild(antlersLeft_3);
		head.addChild(antlersLeft_4);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		LOTREntityElk elk = (LOTREntityElk)entity;
		setRotationAngles(f, f1, f2, f3, f4, f5, elk);
		
		float scale = elk.getHorseSize();
		GL11.glScalef(scale, scale, scale);
		
		body.render(f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
		tail.render(f5);
		head.render(f5);
		if (LOTRMod.isChristmas())
		{
			GL11.glColor3f(1F, 0F, 0F);
		}
		nose.render(f5);
		if (LOTRMod.isChristmas())
		{
			GL11.glColor3f(1F, 1F, 1F);
		}
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		LOTREntityElk elk = (LOTREntityElk)entity;
		
		head.rotateAngleX = (float)Math.toRadians(20D);
		head.rotateAngleY = 0F;
		
		head.rotateAngleX += f4 / 180F / (float)Math.PI;
		head.rotateAngleY += f3 / 180F / (float)Math.PI;
		
		nose.setRotationPoint(head.rotationPointX, head.rotationPointY, head.rotationPointZ);
		nose.rotateAngleX = head.rotateAngleX;
		nose.rotateAngleY = head.rotateAngleY;
		
		tail.rotateAngleX = (float)Math.toRadians(-60D);
		
		leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
	}
}
