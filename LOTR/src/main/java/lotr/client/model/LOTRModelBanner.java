package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class LOTRModelBanner extends ModelBase
{
	private ModelRenderer stand;
	private ModelRenderer post;
	private ModelRenderer lowerPost;
	private ModelRenderer bannerFront;
	private ModelRenderer bannerBack;
	
	public LOTRModelBanner()
	{
		textureWidth = 64;
		textureHeight = 64;
		
		stand = new ModelRenderer(this, 0, 0);
		stand.setRotationPoint(0F, 24F, 0F);
		stand.addBox(-6F, -2F, -6F, 12, 2, 12);
		
		post = new ModelRenderer(this, 0, 14);
		post.setRotationPoint(0F, 24F, 0F);
		post.addBox(-0.5F, -48F, -0.5F, 1, 47, 1);
		post.setTextureOffset(4, 14).addBox(-8F, -43F, -1.5F, 16, 1, 3);
		
		lowerPost = new ModelRenderer(this, 0, 14);
		lowerPost.setRotationPoint(0F, 24F, 0F);
		lowerPost.addBox(-0.5F, -1F, -0.5F, 1, 24, 1);
		
		bannerFront = new ModelRenderer(this, 0, 0);
		bannerFront.setRotationPoint(0F, -18F, 0F);
		bannerFront.addBox(-8F, 0F, -1F, 16, 32, 0);
		
		bannerBack = new ModelRenderer(this, 0, 0);
		bannerBack.setRotationPoint(0F, -18F, 0F);
		bannerBack.addBox(-8F, 0F, -1F, 16, 32, 0);
		
		bannerBack.rotateAngleY = (float)Math.PI;
	}
	
	public void renderStand(float f)
	{
		stand.render(f);
	}
	
	public void renderPost(float f)
	{
		post.render(f);
	}
	
	public void renderLowerPost(float f)
	{
		lowerPost.render(f);
	}
	
	public void renderBanner(float f)
	{
		bannerFront.render(f);
		bannerBack.render(f);
	}
}
