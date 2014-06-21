package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelBeacon extends ModelBase
{
	private ModelRenderer base;
	private ModelRenderer[][] logs = new ModelRenderer[3][4];
	
	public LOTRModelBeacon()
	{
		base = new ModelRenderer(this, 0, 0);
		base.addBox(-8F, -8F, -2F, 16, 16, 4);
		base.setRotationPoint(0F, 22F, 0F);
		base.rotateAngleX = (float)Math.PI / 2F;
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				logs[i][j] = new ModelRenderer(this, 30, 15);
				logs[i][j].addBox(-1.5F, 0F, -7F, 3, 3, 14);
				logs[i][j].setRotationPoint(i != 1 ? -6F + j * 4F : 0F, 17F - i * 3F, i == 1 ? -6F + j * 4F : 0F);
				if (i == 1)
				{
					logs[i][j].rotateAngleY = (float)Math.PI / 2F;
				}
			}
		}
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		base.render(f5);
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				logs[i][j].render(f5);
			}
		}
	}
}
