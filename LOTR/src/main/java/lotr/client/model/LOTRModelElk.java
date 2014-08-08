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
	private ModelRenderer antlersRight_1;
	private ModelRenderer antlersRight_2;
	private ModelRenderer antlersRight_3;
	private ModelRenderer antlersRight_4;
	private ModelRenderer antlersLeft_1;
	private ModelRenderer antlersLeft_2;
	private ModelRenderer antlersLeft_3;
	private ModelRenderer antlersLeft_4;
	
	private ModelRenderer saddle;

	public LOTRModelElk()
	{
		this(0F);
	}
	
	public LOTRModelElk(float f)
	{
		textureWidth = 128;
		textureHeight = 64;
		
		body = new ModelRenderer(this, 0, 0);
		body.setRotationPoint(0F, 4F, -5F);
		body.addBox(-6F, -4F, -7F, 12, 11, 26, f);
		
		leg1 = new ModelRenderer(this, 42, 37);
		leg1.setRotationPoint(-5F, 3F, 8F);
		leg1.addBox(-5.5F, 0F, -3F, 7, 11, 8, f);
		leg1.setTextureOffset(26, 37).addBox(-4F, 11F, -1F, 4, 10, 4, f);
		
		leg2 = new ModelRenderer(this, 42, 37);
		leg2.mirror = true;
		leg2.setRotationPoint(5F, 3F, 8F);
		leg2.addBox(-1.5F, 0F, -3F, 7, 11, 8, f);
		leg2.setTextureOffset(26, 37).addBox(0F, 11F, -1F, 4, 10, 4, f);
		
		leg3 = new ModelRenderer(this, 0, 37);
		leg3.setRotationPoint(-5F, 4F, -6F);
		leg3.addBox(-4.5F, 0F, -3F, 6, 10, 7, f);
		leg3.setTextureOffset(26, 37).addBox(-3.5F, 10F, -2F, 4, 10, 4, f);
		
		leg4 = new ModelRenderer(this, 0, 37);
		leg4.mirror = true;
		leg4.setRotationPoint(5F, 4F, -6F);
		leg4.addBox(-1.5F, 0F, -3F, 6, 10, 7, f);
		leg4.setTextureOffset(26, 37).addBox(-0.5F, 10F, -2F, 4, 10, 4, f);
		
		tail = new ModelRenderer(this, 0, 54);
		tail.setRotationPoint(0F, 3F, 14F);
		tail.addBox(-1F, -1.5F, -0.5F, 2, 2, 8, f);
		
		head = new ModelRenderer(this, 50, 0);
		head.setRotationPoint(0F, 4F, -10F);
		head.addBox(-2F, -10F, -4F, 4, 12, 8, f);
		head.setTextureOffset(74, 0).addBox(-3F, -15F, -8F, 6, 5, 13, f);
		head.setTextureOffset(50, 20);
		head.addBox(-2F, -17F, 3F, 2, 2, 1, f);
		head.mirror = true;
		head.addBox(1F, -17F, 3F, 2, 2, 1, f);
		
		nose = new ModelRenderer(this, 56, 20);
		nose.addBox(-1F, -14F, -9F, 2, 2, 1, f);
		
		antlersRight_1 = new ModelRenderer(this, 0, 0);
		antlersRight_1.addBox(10F, -19F, 2.5F, 1, 12, 1, f);
		antlersRight_1.rotateAngleZ = (float)Math.toRadians(-65D);
		
		antlersRight_2 = new ModelRenderer(this, 4, 0);
		antlersRight_2.addBox(-3F, -23.6F, 2.5F, 1, 8, 1, f);
		antlersRight_2.rotateAngleZ = (float)Math.toRadians(-15D);
		
		antlersRight_3 = new ModelRenderer(this, 8, 0);
		antlersRight_3.addBox(-8F, -36F, 2.5F, 1, 16, 1, f);
		antlersRight_3.rotateAngleZ = (float)Math.toRadians(-15D);
		
		antlersRight_4 = new ModelRenderer(this, 12, 0);
		antlersRight_4.addBox(7.5F, -35F, 2.5F, 1, 10, 1, f);
		antlersRight_4.rotateAngleZ = (float)Math.toRadians(-50D);
		
		head.addChild(antlersRight_1);
		head.addChild(antlersRight_2);
		head.addChild(antlersRight_3);
		head.addChild(antlersRight_4);
		
		antlersLeft_1 = new ModelRenderer(this, 0, 0);
		antlersLeft_1.mirror = true;
		antlersLeft_1.addBox(-11F, -19F, 2.5F, 1, 12, 1, f);
		antlersLeft_1.rotateAngleZ = (float)Math.toRadians(65D);
		
		antlersLeft_2 = new ModelRenderer(this, 4, 0);
		antlersLeft_2.mirror = true;
		antlersLeft_2.addBox(2F, -23.6F, 2.5F, 1, 8, 1, f);
		antlersLeft_2.rotateAngleZ = (float)Math.toRadians(15D);
		
		antlersLeft_3 = new ModelRenderer(this, 8, 0);
		antlersLeft_3.mirror = true;
		antlersLeft_3.addBox(7F, -36F, 2.5F, 1, 16, 1, f);
		antlersLeft_3.rotateAngleZ = (float)Math.toRadians(15D);
		
		antlersLeft_4 = new ModelRenderer(this, 12, 0);
		antlersLeft_4.mirror = true;
		antlersLeft_4.addBox(-8.5F, -35F, 2.5F, 1, 10, 1, f);
		antlersLeft_4.rotateAngleZ = (float)Math.toRadians(50D);
		
		head.addChild(antlersLeft_1);
		head.addChild(antlersLeft_2);
		head.addChild(antlersLeft_3);
		head.addChild(antlersLeft_4);
		
		saddle = new ModelRenderer(this, 76, 28);
		saddle.setRotationPoint(0F, 3F, 3F);
		saddle.addBox(-6F, -4F, -7F, 12, 1, 8, f);
		saddle.setTextureOffset(76, 37).addBox(-6.5F, -3.5F, -3F, 1, 6, 1, f);
		saddle.setTextureOffset(80, 37).addBox(-6.5F, 2.5F, -3.5F, 1, 2, 2, f);
		saddle.mirror = true;
		saddle.setTextureOffset(76, 37).addBox(5.5F, -3.5F, -3F, 1, 6, 1, f);
		saddle.setTextureOffset(80, 37).addBox(5.5F, 2.5F, -3.5F, 1, 2, 2, f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		LOTREntityElk elk = (LOTREntityElk)entity;
		setRotationAngles(f, f1, f2, f3, f4, f5, elk);
		
		GL11.glPushMatrix();
		
		float scale = elk.getHorseSize();
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(0F, 24F * f5, 0F);
		
		boolean showAntlers = scale > 0.75F;
		antlersRight_1.showModel = showAntlers;
		antlersRight_2.showModel = showAntlers;
		antlersRight_3.showModel = showAntlers;
		antlersRight_4.showModel = showAntlers;
		antlersLeft_1.showModel = showAntlers;
		antlersLeft_2.showModel = showAntlers;
		antlersLeft_3.showModel = showAntlers;
		antlersLeft_4.showModel = showAntlers;
		
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
		
		GL11.glPopMatrix();
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
		
		leg1.rotateAngleX = MathHelper.cos(f * 0.4F) * f1;
		leg2.rotateAngleX = MathHelper.cos(f * 0.4F + (float)Math.PI) * f1;
		leg3.rotateAngleX = MathHelper.cos(f * 0.4F + (float)Math.PI) * f1;
		leg4.rotateAngleX = MathHelper.cos(f * 0.4F) * f1;
	}
	
	public void renderSaddle(float f)
	{
		saddle.render(f);
	}
}
