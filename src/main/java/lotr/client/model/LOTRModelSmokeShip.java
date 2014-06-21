package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelSmokeShip extends ModelBase
{
	private ModelRenderer hull;
	private ModelRenderer deck;
	private ModelRenderer mast1;
	private ModelRenderer sail1;
	private ModelRenderer mast2;
	private ModelRenderer sail2;
	private ModelRenderer mast3;
	private ModelRenderer sail3;
	private ModelRenderer bow;
	private ModelRenderer stern;
	
	public LOTRModelSmokeShip()
	{
		hull = new ModelRenderer(this);
		hull.addBox(-3.5F, 1F, -8F, 7, 5, 16);
		hull.setRotationPoint(0F, 0F, 0F);
		
		deck = new ModelRenderer(this);
		deck.addBox(-5F, 0F, -8F, 10, 1, 16);
		deck.setRotationPoint(0F, 0F, 0F);
		
		mast1 = new ModelRenderer(this);
		mast1.addBox(-1F, -9F, -6F, 2, 9, 2);
		mast1.setRotationPoint(0F, 0F, 0F);
		
		sail1 = new ModelRenderer(this);
		sail1.addBox(-6F, -8F, -5.5F, 12, 6, 1);
		sail1.setRotationPoint(0F, 0F, 0F);
		
		mast2 = new ModelRenderer(this);
		mast2.addBox(-1F, -12F, -1F, 2, 12, 2);
		mast2.setRotationPoint(0F, 0F, 0F);
		
		sail2 = new ModelRenderer(this);
		sail2.addBox(-8F, -11F, -0.5F, 16, 8, 1);
		sail2.setRotationPoint(0F, 0F, 0F);
		
		mast3 = new ModelRenderer(this);
		mast3.addBox(-1F, -9F, 4F, 2, 9, 2);
		mast3.setRotationPoint(0F, 0F, 0F);
		
		sail3 = new ModelRenderer(this);
		sail3.addBox(-6F, -8F, 4.5F, 12, 6, 1);
		sail3.setRotationPoint(0F, 0F, 0F);
		
		bow = new ModelRenderer(this);
		bow.addBox(-3.5F, -1F, -12F, 7, 3, 4);
		bow.setRotationPoint(0F, 0F, 0F);
		
		stern = new ModelRenderer(this);
		stern.addBox(-3.5F, -1F, 8F, 7, 3, 4);
		stern.setRotationPoint(0F, 0F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		hull.render(f5);
		deck.render(f5);
		mast1.render(f5);
		sail1.render(f5);
		mast2.render(f5);
		sail2.render(f5);
		mast3.render(f5);
		sail3.render(f5);
		bow.render(f5);
		stern.render(f5);
	}
}
