package lotr.client.render.entity;

import lotr.client.model.LOTRModelBiped;
import lotr.client.model.LOTRModelElf;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderElvenTrader extends LOTRRenderElf
{
	private static ResourceLocation outfitTexture = new ResourceLocation("lotr:mob/elf/galadhrimTrader_cloak.png");
	
	private ModelBiped capeModel = new LOTRModelBiped();
	private ModelBiped outfitModel = new LOTRModelElf(0.5F);
	
	public LOTRRenderElvenTrader()
	{
		super();
		setRenderPassModel(outfitModel);
	}
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		if (pass == 0)
		{
			setRenderPassModel(outfitModel);
			bindTexture(outfitTexture);
			return 1;
		}
        return super.shouldRenderPass(entity, pass, f);
    }
}
