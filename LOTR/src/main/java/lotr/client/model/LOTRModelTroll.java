package lotr.client.model;

import lotr.common.entity.item.LOTREntityStoneTroll;
import lotr.common.entity.npc.LOTREntityMountainTroll;
import lotr.common.entity.npc.LOTREntityTroll;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class LOTRModelTroll extends ModelBase
{
	public ModelRenderer head;
	public ModelRenderer headHurt;
	public ModelRenderer body;
	public ModelRenderer rightArm;
	public ModelRenderer leftArm;
	public ModelRenderer rightLeg;
	public ModelRenderer leftLeg;
	
	public ModelRenderer woodenClub;
	public ModelRenderer woodenClubSpikes;
	public ModelRenderer warhammer;
	public ModelRenderer battleaxe;
	
	private boolean isOutiftModel = false;
	
	public LOTRModelTroll()
	{
		this(0F);
	}
	
	public LOTRModelTroll(float f)
	{
		textureWidth = 128;
		textureHeight = 128;
		
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-6F, -6F, -12F, 12, 12, 12, f);
		head.setRotationPoint(0F, -27F, -6F);
		head.setTextureOffset(40, 0).addBox(6F, -2F, -8F, 1, 4, 3, f);
		head.mirror = true;
		head.setTextureOffset(40, 0).addBox(-7F, -2F, -8F, 1, 4, 3, f);
		head.mirror = false;
		head.setTextureOffset(0, 0).addBox(-1F, -1F, -14F, 2, 3, 2, f);
		
		headHurt = new ModelRenderer(this, 48, 44);
		headHurt.addBox(-6F, -6F, -12F, 12, 12, 12, f);
		headHurt.setRotationPoint(0F, -27F, -6F);
		headHurt.setTextureOffset(40, 0).addBox(6F, -2F, -8F, 1, 4, 3, f);
		headHurt.mirror = true;
		headHurt.setTextureOffset(40, 0).addBox(-7F, -2F, -8F, 1, 4, 3, f);
		headHurt.mirror = false;
		headHurt.setTextureOffset(0, 0).addBox(-1F, -1F, -14F, 2, 3, 2, f);
		
		body = new ModelRenderer(this, 48, 0);
		body.addBox(-12F, -28F, -8F, 24, 28, 16, f);
		body.setRotationPoint(0F, 0F, 0F);
		
		rightArm = new ModelRenderer(this, 0, 24);
		rightArm.mirror = true;
		rightArm.addBox(-12F, -3F, -6F, 12, 12, 12, f);
		rightArm.setRotationPoint(-12F, -23F, 0F);
		rightArm.setTextureOffset(0, 48).addBox(-11F, 9F, -5F, 10, 20, 10, f);
		
		leftArm = new ModelRenderer(this, 0, 24);
		leftArm.addBox(0F, -3F, -6F, 12, 12, 12, f);
		leftArm.setRotationPoint(12F, -23F, 0F);
		leftArm.setTextureOffset(0, 48).addBox(1F, 9F, -5F, 10, 20, 10, f);
		
		rightLeg = new ModelRenderer(this, 0, 78);
		rightLeg.mirror = true;
		rightLeg.addBox(-6F, 0F, -6F, 11, 12, 12, f);
		rightLeg.setRotationPoint(-6F, 0F, 0F);
		rightLeg.setTextureOffset(0, 102).addBox(-5.5F, 12F, -5F, 10, 12, 10);
		
		leftLeg = new ModelRenderer(this, 0, 78);
		leftLeg.addBox(-5F, 0F, -6F, 11, 12, 12, f);
		leftLeg.setRotationPoint(6F, 0F, 0F);
		leftLeg.setTextureOffset(0, 102).addBox(-4.5F, 12F, -5F, 10, 12, 10);
		
		woodenClub = new ModelRenderer(this, 0, 0);
		woodenClub.addBox(-9F, 5F, 21F, 6, 24, 6, f);
		woodenClub.setRotationPoint(-12F, -23F, 0F);
		
		woodenClubSpikes = new ModelRenderer(this, 24, 0);
		woodenClubSpikes.addBox(-12F, 25F, 23.5F, 12, 1, 1, f);
		woodenClubSpikes.addBox(-12F, 20F, 23.5F, 12, 1, 1, f);
		woodenClubSpikes.addBox(-12F, 15F, 23.5F, 12, 1, 1, f);
		woodenClubSpikes.setTextureOffset(24, 2);
		woodenClubSpikes.addBox(-6.5F, 25F, 18F, 1, 1, 12, f);
		woodenClubSpikes.addBox(-6.5F, 20F, 18F, 1, 1, 12, f);
		woodenClubSpikes.addBox(-6.5F, 15F, 18F, 1, 1, 12, f);
		woodenClubSpikes.setRotationPoint(-12F, -23F, 0F);
		
		warhammer = new ModelRenderer(this, 52, 29);
		warhammer.setRotationPoint(-12F, -23F, 0F);
		warhammer.addBox(-7.5F, 5F, 22.5F, 3, 20, 3, f);
		warhammer.setTextureOffset(0, 32).addBox(-12F, 25F, 14F, 12, 12, 20, f);
		
		battleaxe = new ModelRenderer(this, 64, 0);
		battleaxe.setRotationPoint(-12F, -23F, 0F);
		battleaxe.addBox(-7F, 5F, 22.5F, 2, 40, 2, f);
		battleaxe.setTextureOffset(72, 0);
		battleaxe.addBox(-6.5F, 40F, 20F, 1, 1, 16, f);
		battleaxe.addBox(-6.5F, 34F, 20F, 1, 1, 16, f);
		battleaxe.addBox(-6.5F, 28F, 20F, 1, 1, 16, f);
	}
	
	public LOTRModelTroll(float f, int i)
	{
		this(f);
		isOutiftModel = true;
		if (i == 0)
		{
			head.showModel = true;
			body.showModel = true;
			rightArm.showModel = true;
			leftArm.showModel = true;
			rightLeg.showModel = false;
			leftLeg.showModel = false;
		}
		else if (i == 1)
		{
			head.showModel = false;
			body.showModel = false;
			rightArm.showModel = false;
			leftArm.showModel = false;
			rightLeg.showModel = true;
			leftLeg.showModel = true;
		}
		else if (i == 2)
		{
			head.showModel = true;
			body.showModel = false;
			rightArm.showModel = false;
			leftArm.showModel = false;
			rightLeg.showModel = false;
			leftLeg.showModel = false;
		}
		else if (i == 3)
		{
			head.showModel = false;
			body.showModel = true;
			rightArm.showModel = true;
			leftArm.showModel = true;
			rightLeg.showModel = false;
			leftLeg.showModel = false;
		}
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		boolean isHurt = false;
		boolean hasTwoHeads = false;
		
		if (entity instanceof LOTREntityTroll)
		{
			LOTREntityTroll troll = (LOTREntityTroll)entity;
			isHurt = !isOutiftModel && troll.shouldRenderHeadHurt();
			hasTwoHeads = troll.hasTwoHeads();
		}
		else if (entity instanceof LOTREntityStoneTroll)
		{
			LOTREntityStoneTroll troll = (LOTREntityStoneTroll)entity;
			isHurt = false;
			hasTwoHeads = troll.hasTwoHeads();
		}
		
		if (hasTwoHeads)
		{
			GL11.glPushMatrix();
			GL11.glRotatef(-15F, 0F, 0F, 1F);
			GL11.glRotatef(10F, 0F, 1F, 0F);
			if (isHurt)
			{
				headHurt.render(f5);
			}
			else
			{
				head.render(f5);
			}
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glRotatef(15F, 0F, 0F, 1F);
			GL11.glRotatef(-10F, 0F, 1F, 0F);
			if (isHurt)
			{
				headHurt.render(f5);
			}
			else
			{
				head.render(f5);
			}
			GL11.glPopMatrix();
		}
		else
		{
			if (isHurt)
			{
				headHurt.render(f5);
			}
			else
			{
				head.render(f5);
			}
		}
		
		body.render(f5);
		rightArm.render(f5);
		leftArm.render(f5);
		rightLeg.render(f5);
		leftLeg.render(f5);
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		head.rotationPointX = 0F;
		head.rotationPointY = -27F;
		head.rotateAngleZ = 0F;
		
		body.rotationPointX = 0F;
		body.rotationPointY = 0F;
		body.rotateAngleZ = 0F;
		
		rightArm.rotationPointX = -12F;
		rightArm.rotationPointY = -23F;
		rightArm.rotateAngleZ = 0F;
		
		leftArm.rotationPointX = 12F;
		leftArm.rotationPointY = -23F;
		leftArm.rotateAngleZ = 0F;
		
        head.rotateAngleY = f3 / (180F / (float)Math.PI);
        head.rotateAngleX = f4 / (180F / (float)Math.PI);
		if (entity instanceof LOTREntityTroll && ((LOTREntityTroll)entity).sniffTime > 0)
		{
			float f6 = ((float)((LOTREntityTroll)entity).sniffTime - (f2 - entity.ticksExisted)) / 8F;
			f6 *= (float)Math.PI * 2F;
			head.rotateAngleY = MathHelper.sin(f6) * 0.5F;
		}

        rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2F * f1 * 0.5F;
        leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2F * f1 * 0.5F;
		if (entity instanceof LOTREntityTroll)
		{
			rightArm.rotateAngleX = rightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * 1F;
		}
        rightArm.rotateAngleZ = 0F;
        leftArm.rotateAngleZ = 0F;
		
        if (onGround > -9990F)
        {
            float f6 = onGround;
            body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2F) * 0.2F;
            rightArm.rotationPointZ = MathHelper.sin(body.rotateAngleY) * 5F;
            rightArm.rotationPointX = -MathHelper.cos(body.rotateAngleY) * 12F;
            leftArm.rotationPointZ = -MathHelper.sin(body.rotateAngleY) * 5F;
            leftArm.rotationPointX = MathHelper.cos(body.rotateAngleY) * 12F;
            rightArm.rotateAngleY += body.rotateAngleY;
            leftArm.rotateAngleY += body.rotateAngleY;
            leftArm.rotateAngleX += body.rotateAngleY;
            f6 = 1F - onGround;
            f6 *= f6;
            f6 *= f6;
            f6 = 1F - f6;
            float f7 = MathHelper.sin(f6 * (float)Math.PI);
            float f8 = MathHelper.sin(onGround * (float)Math.PI) * -(head.rotateAngleX - 0.7F) * 0.75F;
            rightArm.rotateAngleX = (float)((double)rightArm.rotateAngleX - ((double)f7 * 1.2D + (double)f8));
            rightArm.rotateAngleY += body.rotateAngleY * 2F;
            rightArm.rotateAngleZ = MathHelper.sin(onGround * (float)Math.PI) * -0.4F;
        }
		
        rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        rightLeg.rotateAngleY = 0F;
        leftLeg.rotateAngleY = 0F;
        rightArm.rotateAngleY = 0F;
        leftArm.rotateAngleY = 0F;
        rightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        leftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        rightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
        leftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		
		if (entity instanceof LOTREntityMountainTroll && ((LOTREntityMountainTroll)entity).isThrowingRocks())
		{
			rightArm.rotateAngleX -= 0.5F;
			rightArm.rotateAngleZ -= 0.4F;
			leftArm.rotateAngleX = rightArm.rotateAngleX;
			leftArm.rotateAngleY = -rightArm.rotateAngleY;
			leftArm.rotateAngleZ = -rightArm.rotateAngleZ;
		}
		
		if (entity instanceof EntityLivingBase)
		{
			float f6 = MathHelper.sin(f * 0.2F) * 0.3F * f1;
			head.rotationPointX += MathHelper.sin(f6) * 27F;
			head.rotationPointY += 27 - (MathHelper.cos(f6) * 27F);
			head.rotateAngleZ += f6;
			body.rotateAngleZ += f6;
			float armRotationOffsetX = (MathHelper.sin(f6) * 23F) + (MathHelper.cos(f6) * 12F) - 12;
			float armRotationOffsetY = (MathHelper.cos(f6) * -23F) + (MathHelper.sin(f6) * 12F) + 23;
			rightArm.rotationPointX += armRotationOffsetX;
			rightArm.rotationPointY += -armRotationOffsetY;
			rightArm.rotateAngleZ += f6;
			leftArm.rotationPointX += armRotationOffsetX;
			leftArm.rotationPointY += armRotationOffsetY;
			leftArm.rotateAngleZ += f6;
		}
		
		headHurt.rotationPointX = head.rotationPointX;
		headHurt.rotationPointY = head.rotationPointY;
		headHurt.rotationPointZ = head.rotationPointZ;
		headHurt.rotateAngleX = head.rotateAngleX;
		headHurt.rotateAngleY = head.rotateAngleY;
		headHurt.rotateAngleZ = head.rotateAngleZ;
	}
	
	public void renderWoodenClub(float f)
	{
		woodenClub.rotationPointX = rightArm.rotationPointX;
		woodenClub.rotationPointY = rightArm.rotationPointY;
		woodenClub.rotationPointZ = rightArm.rotationPointZ;
		woodenClub.rotateAngleX = rightArm.rotateAngleX - ((float)Math.PI * 0.5F);
		woodenClub.rotateAngleY = rightArm.rotateAngleY;
		woodenClub.rotateAngleZ = rightArm.rotateAngleZ;
		woodenClub.render(f);
	}
	
	public void renderWoodenClubWithSpikes(float f)
	{
		renderWoodenClub(f);
		woodenClubSpikes.rotationPointX = woodenClub.rotationPointX;
		woodenClubSpikes.rotationPointY = woodenClub.rotationPointY;
		woodenClubSpikes.rotationPointZ = woodenClub.rotationPointZ;
		woodenClubSpikes.rotateAngleX = woodenClub.rotateAngleX;
		woodenClubSpikes.rotateAngleY = woodenClub.rotateAngleY;
		woodenClubSpikes.rotateAngleZ = woodenClub.rotateAngleZ;
		woodenClubSpikes.render(f);
	}
	
	public void renderWarhammer(float f)
	{
		warhammer.rotationPointX = rightArm.rotationPointX;
		warhammer.rotationPointY = rightArm.rotationPointY;
		warhammer.rotationPointZ = rightArm.rotationPointZ;
		warhammer.rotateAngleX = rightArm.rotateAngleX - ((float)Math.PI * 0.5F);
		warhammer.rotateAngleY = rightArm.rotateAngleY;
		warhammer.rotateAngleZ = rightArm.rotateAngleZ;
		warhammer.render(f);
	}
	
	public void renderBattleaxe(float f)
	{
		battleaxe.rotationPointX = rightArm.rotationPointX;
		battleaxe.rotationPointY = rightArm.rotationPointY;
		battleaxe.rotationPointZ = rightArm.rotationPointZ;
		battleaxe.rotateAngleX = rightArm.rotateAngleX - ((float)Math.PI * 0.5F);
		battleaxe.rotateAngleY = rightArm.rotateAngleY;
		battleaxe.rotateAngleZ = rightArm.rotateAngleZ;
		battleaxe.render(f);
	}
}
