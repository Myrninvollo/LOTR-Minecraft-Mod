package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelBiped;
import lotr.common.entity.npc.LOTREntityDunlending;
import lotr.common.entity.npc.LOTREntityDunlendingBartender;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderDunlending extends LOTRRenderDunlendingBase
{
	private static List dunlendingOutfits;
	private static ResourceLocation outfitApron = new ResourceLocation("lotr:mob/dunland/bartender_apron.png");
	
	private ModelBiped standardRenderPassModel = new LOTRModelBiped(0.5F);
	
	public LOTRRenderDunlending()
	{
		super();
		setRenderPassModel(standardRenderPassModel);
		dunlendingOutfits = LOTRRandomSkins.loadSkinsList("lotr:mob/dunland/outfit");
	}

	@Override
	public int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		LOTREntityDunlending dunlending = (LOTREntityDunlending)entity;
		if (pass == 1 && dunlending.getEquipmentInSlot(3) == null)
		{
			setRenderPassModel(standardRenderPassModel);
			if (dunlending instanceof LOTREntityDunlendingBartender)
			{
				bindTexture(outfitApron);
			}
			else
			{
				bindTexture(LOTRRandomSkins.getRandomSkin(dunlendingOutfits, dunlending));
			}
			return 1;
		}
		return super.shouldRenderPass(dunlending, pass, f);
    }
}
