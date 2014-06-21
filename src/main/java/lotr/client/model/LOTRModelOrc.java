package lotr.client.model;

import lotr.common.entity.npc.LOTREntityOrc;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelOrc extends LOTRModelBiped
{
	private ModelRenderer nose;
	private ModelRenderer earRight;
	private ModelRenderer earLeft;
	
	private ModelRenderer mercenaryCaptainStaff;
	private ModelRenderer mercenaryCaptainStaffSkull;
	
	public LOTRModelOrc()
	{
		this(0F);
	}
	
	public LOTRModelOrc(float f)
	{
		super(f);
		
		nose = new ModelRenderer(this, 14, 17);
		nose.addBox(-0.5F, -4F, -4.8F, 1, 2, 1, f);
		nose.setRotationPoint(0F, 0F, 0F);
		
		earRight = new ModelRenderer(this, 0, 0);
		earRight.addBox(-3.5F, -5.5F, 2F, 1, 2, 3, f);
		earRight.setRotationPoint(0F, 0F, 0F);
		earRight.rotateAngleX = 15F / (180F / (float)Math.PI);
		earRight.rotateAngleY = -30F / (180F / (float)Math.PI);
		earRight.rotateAngleZ = -13F / (180F / (float)Math.PI);
		
		earLeft = new ModelRenderer(this, 24, 0);
		earLeft.addBox(2.5F, -5.5F, 2F, 1, 2, 3, f);
		earLeft.setRotationPoint(0F, 0F, 0F);
		earLeft.rotateAngleX = 15F / (180F / (float)Math.PI);
		earLeft.rotateAngleY = 30F / (180F / (float)Math.PI);
		earLeft.rotateAngleZ = 13F / (180F / (float)Math.PI);
		
		bipedHead.addChild(nose);
		bipedHead.addChild(earRight);
		bipedHead.addChild(earLeft);
		
		mercenaryCaptainStaff = new ModelRenderer(this, 0, 0);
		mercenaryCaptainStaff.addBox(-1.5F, 8F, -6F, 1, 1, 28, f);
		mercenaryCaptainStaff.setRotationPoint(-5F, 2F, 0F);
		
		mercenaryCaptainStaffSkull = new ModelRenderer(this, 0, 0);
		mercenaryCaptainStaffSkull.addBox(-3.5F, 6F, -11F, 5, 5, 5, f);
		mercenaryCaptainStaffSkull.setRotationPoint(-5F, 2F, 0F);
	}
	
	public void renderMercenaryCaptainStaff(float f)
	{
		mercenaryCaptainStaff.render(f);
		mercenaryCaptainStaffSkull.render(f);
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		if (entity instanceof LOTREntityOrc && ((LOTREntityOrc)entity).renderOrcSkullStaff())
		{
			bipedRightArm.rotateAngleX -= 1.35F;
			
			mercenaryCaptainStaffSkull.rotateAngleX = mercenaryCaptainStaff.rotateAngleX = bipedRightArm.rotateAngleX;
			mercenaryCaptainStaffSkull.rotateAngleY = mercenaryCaptainStaff.rotateAngleY = bipedRightArm.rotateAngleY;
			mercenaryCaptainStaffSkull.rotateAngleZ = mercenaryCaptainStaff.rotateAngleZ = bipedRightArm.rotateAngleZ;
			
			mercenaryCaptainStaffSkull.rotationPointX = mercenaryCaptainStaff.rotationPointX = bipedRightArm.rotationPointX;
			mercenaryCaptainStaffSkull.rotationPointY = mercenaryCaptainStaff.rotationPointY = bipedRightArm.rotationPointY;
			mercenaryCaptainStaffSkull.rotationPointZ = mercenaryCaptainStaff.rotationPointZ = bipedRightArm.rotationPointZ;
		}
	}
}
