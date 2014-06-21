package lotr.client.model;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelBoar extends ModelPig
{
	private ModelRenderer tusks;
	
	public LOTRModelBoar()
	{
		this(0F);
	}
	
	public LOTRModelBoar(float f)
	{
		super(f);
		
		head.setTextureOffset(24, 0).addBox(-3F, 0F, -10F, 6, 4, 2, f);
		head.setTextureOffset(40, 0).addBox(-5F, -5F, -6F, 1, 2, 2, f);
		head.mirror = true;
		head.addBox(4F, -5F, -6F, 1, 2, 2, f);
		
		tusks = new ModelRenderer(this, 0, 0);
		tusks.addBox(-4F, 2F, -11F, 1, 1, 2, f);
		tusks.setTextureOffset(1, 1).addBox(-4F, 1F, -11.5F, 1, 1, 1, f);
		tusks.mirror = true;
		tusks.setTextureOffset(0, 0).addBox(3F, 2F, -11F, 1, 1, 2, f);
		tusks.setTextureOffset(1, 1).addBox(3F, 1F, -11.5F, 1, 1, 1, f);
		
		head.addChild(tusks);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		tusks.showModel = !isChild;
		super.render(entity, f, f1, f2, f3, f4, f5);
	}
}
