package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class LOTRModelBannerWall extends ModelBase
{
	private ModelRenderer post;
	private ModelRenderer banner;
	
	public LOTRModelBannerWall()
	{
		textureWidth = 64;
		textureHeight = 64;
		
		post = new ModelRenderer(this, 4, 18);
		post.setRotationPoint(0F, -8F, 0F);
		post.addBox(-8F, 0F, -0.5F, 16, 1, 1);
		
		banner = new ModelRenderer(this, 0, 0);
		banner.setRotationPoint(0F, -7F, 0F);
		banner.addBox(-8F, 0F, 0F, 16, 32, 0);
	}
	
	public void renderPost(float f)
	{
		post.render(f);
	}
	
	public void renderBanner(float f)
	{
		banner.render(f);
	}
}
