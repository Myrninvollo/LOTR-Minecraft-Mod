package lotr.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lotr.client.render.entity.LOTRHuornTextures;
import lotr.common.entity.npc.LOTREntityHuornBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class LOTRModelHuorn extends ModelBase
{
	private List woodBlocks = new ArrayList();
	private List leafBlocks = new ArrayList();
	private ModelRenderer face;
	
	private int baseX = 2;
	private int baseY = 0;
	private int baseZ = 2;
	private Random rand = new Random();
	
	public LOTRModelHuorn()
	{	
		rand.setSeed(100L);
		int height = 6;
		byte leafStart = 3;
		byte leafRangeMin = 0;
		
		for (int j = baseY - leafStart + height; j <= baseY + height; j++)
		{
			int j1 = j - (baseY + height);
			int leafRange = leafRangeMin + 1 - j1 / 2;
			for (int i = baseX - leafRange; i <= baseX + leafRange; i++)
			{
				int i1 = i - baseX;
				for (int k = baseZ - leafRange; k <= baseZ + leafRange; k++)
				{
					int k1 = k - baseZ;
					if (Math.abs(i1) != leafRange || Math.abs(k1) != leafRange || rand.nextInt(2) != 0 && j1 != 0)
					{
						ModelRenderer block = new ModelRenderer(this, 0, 0);
						block.addBox(-8F, -8F, -8F, 16, 16, 16);
						block.setRotationPoint((float)i * 16F, 16F - (float)j * 16F, (float)k * 16F);
						leafBlocks.add(block);
					}
				}
			}
		}

		for (int j = 0; j < height; j++)
		{
			ModelRenderer block = new ModelRenderer(this, 0, 0);
			block.addBox(-8F, -8F, -8F, 16, 16, 16);
			block.setRotationPoint((float)baseX * 16F, 16F - (float)j * 16F, (float)baseZ * 16F);
			woodBlocks.add(block);
		}
		
		face = new ModelRenderer(this, 0, 0);
		face.addBox(-8F, -8F, -8F, 16, 16, 16, 0.5F);
		face.setRotationPoint(0F, 0F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		LOTREntityHuornBase huorn = (LOTREntityHuornBase)entity;
		if (huorn.isHuornActive())
		{
			face.render(f5);
		}
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glTranslatef(-(float)baseX, -(float)baseY, -(float)baseZ);
		
		for (int i = 0; i < woodBlocks.size(); i++)
		{
			if (i == 0)
			{
				LOTRHuornTextures.INSTANCE.bindWoodTexture(huorn);
			}
			
			ModelRenderer block = (ModelRenderer)woodBlocks.get(i);
			block.render(f5);
		}
		
		for (int i = 0; i < leafBlocks.size(); i++)
		{
			if (i == 0)
			{
				LOTRHuornTextures.INSTANCE.bindLeafTexture(huorn);
			}
			
			ModelRenderer block = (ModelRenderer)leafBlocks.get(i);
			block.render(f5);
		}

		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
