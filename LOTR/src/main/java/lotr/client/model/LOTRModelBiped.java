package lotr.client.model;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelBiped extends ModelBiped
{
	public LOTRModelBiped()
	{
		super();
	}
	
	public LOTRModelBiped(float f)
	{
		super(f);
	}
	
	public LOTRModelBiped(float f, float f1, int width, int height)
	{
		super(f, f1, width, height);
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		if (entity instanceof LOTREntityNPC && ((LOTREntityNPC)entity).isDrunkard())
		{
			float f6 = f2 / 80F;
			float f7 = (f2 + 40F) / 80F;
			f6 *= (float)Math.PI * 2F;
			f7 *= (float)Math.PI * 2F;
			float f8 = MathHelper.sin(f6) * 0.5F;
			float f9 = MathHelper.sin(f7) * 0.5F;
			bipedHead.rotateAngleX += f8;
			bipedHead.rotateAngleY += f9;
			bipedHeadwear.rotateAngleX += f8;
			bipedHeadwear.rotateAngleY += f9;
			bipedRightArm.rotateAngleX = -60F / (180F / (float)Math.PI);
		}
	}
}
