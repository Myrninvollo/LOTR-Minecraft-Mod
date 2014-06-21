package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelBlock extends ModelBase
{
	private ModelRenderer block;
	
	public LOTRModelBlock(float f)
	{
		block = new ModelRenderer(this, 0, 0);
		block.addBox(-8F, -8F, -8F, 16, 16, 16, f);
		block.setRotationPoint(0F, 0F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		block.render(f5);
	}
}
