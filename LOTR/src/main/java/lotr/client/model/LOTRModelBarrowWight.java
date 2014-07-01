package lotr.client.model;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class LOTRModelBarrowWight extends ModelBase
{
	private Random rand = new Random();

	private ModelRenderer head;
	private ModelRenderer body;
	private ModelRenderer[] cloaks;
	
	public LOTRModelBarrowWight()
	{
		textureWidth = 64;
		textureHeight = 64;
		
		head = new ModelRenderer(this, 0, 0);
		head.setRotationPoint(0F, -12F, 0F);
		head.addBox(-4F, -8F, -4F, 8, 8, 8);
		head.setTextureOffset(32, 0).addBox(-4F, -8F, -4F, 8, 8, 8, 1F);
		
		body = new ModelRenderer(this, 0, 16);
		body.setRotationPoint(0F, -12F, 0F);
		body.addBox(-3F, 0F, -3F, 6, 24, 6);
		
		cloaks = new ModelRenderer[16];
		for (int i = 0; i < cloaks.length; i++)
		{
			ModelRenderer model = new ModelRenderer(this, 52, 30);
			model.setRotationPoint(0F, -4F, 0F);
			model.addBox(0F, 0F, 0F, 0, 28, 6);
			model.rotateAngleY = ((float)i / (float)cloaks.length) * (float)Math.PI * 2F;
			
			cloaks[i] = model;
		}
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        head.render(f5);
        body.render(f5);
        for (ModelRenderer model : cloaks)
        {
        	model.render(f5);
        }
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		head.rotateAngleX = f4 / (float)Math.toDegrees(1D);
        head.rotateAngleY = f3 / (float)Math.toDegrees(1D);
        
        for (int i = 0; i < cloaks.length; i++)
        {
        	ModelRenderer model = cloaks[i];
        	rand.setSeed(i ^ 38346436396L * 47956693L);
        	model.rotateAngleY += MathHelper.cos(f2 * 0.6F) * 0.1F;
        }
	}
}
