package lotr.client.render.entity;

import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityHorse;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.entity.Entity;

public class LOTRRenderHorse extends RenderHorse
{
    public LOTRRenderHorse(ModelBase model, float f)
    {
        super(model, f);
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
}
