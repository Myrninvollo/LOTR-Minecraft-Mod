package lotr.client.render.entity;

import org.lwjgl.opengl.GL11;

import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityHobbitBartender;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderHorse extends RenderHorse
{
    public LOTRRenderHorse()
    {
        super(new ModelHorse(), 0.75F);
        setRenderPassModel(new ModelHorse());
    }
	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		LOTREntityHorse horse = (LOTREntityHorse)entity;
		boolean fools = LOTRMod.isAprilFools();
		int horseType = horse.getHorseType();
		if (fools)
		{
			horse.setHorseType(1);
		}
		
		super.doRender(entity, d, d1, d2, f, f1);
		
		if (fools)
		{
			horse.setHorseType(horseType);
		}
	}
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		LOTREntityHorse horse = (LOTREntityHorse)entity;
		
		float armorScale = 1.1F;
		float armorScaleInverse = 1F / armorScale;
		
		ResourceLocation armor = horse.getMountArmorTexture();
		if (armor != null)
		{
			if (pass == 0)
			{
				bindTexture(armor);
				GL11.glScalef(armorScale, armorScale, armorScale);
				return 1;
			}
			if (pass == 1)
			{
				GL11.glScalef(armorScaleInverse, armorScaleInverse, armorScaleInverse);
				return 0;
			}
	    }
		
		return super.shouldRenderPass(entity, pass, f);
    }
}
