package lotr.client.render.entity;

import lotr.client.model.LOTRModelBiped;
import lotr.common.entity.npc.LOTREntityDunlendingBartender;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderDunlending extends LOTRRenderDunlendingBase
{
	private static ResourceLocation outfitStandard = new ResourceLocation("lotr:mob/dunland/outfit.png");
	private static ResourceLocation outfitApron = new ResourceLocation("lotr:mob/dunland/bartender_apron.png");
	
	private ModelBiped standardRenderPassModel = new LOTRModelBiped(0.5F);
	
	public LOTRRenderDunlending()
	{
		super();
		setRenderPassModel(standardRenderPassModel);
	}

	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		if (pass == 1 && entity.getEquipmentInSlot(3) == null)
		{
			setRenderPassModel(standardRenderPassModel);
			if (entity instanceof LOTREntityDunlendingBartender)
			{
				bindTexture(outfitApron);
			}
			else
			{
				bindTexture(outfitStandard);
			}
			return 1;
		}
		return super.shouldRenderPass(entity, pass, f);
    }
}
