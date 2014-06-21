package lotr.client.render.tileentity;

import lotr.client.model.LOTRModelMug;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityMug;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderMug extends TileEntitySpecialRenderer
{
	private static ResourceLocation mugTexture = new ResourceLocation("lotr:item/mug.png");
	private static ModelBase mugModel = new LOTRModelMug();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		LOTRTileEntityMug mug = (LOTRTileEntityMug)tileentity;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)d + 0.5F, (float)d1, (float)d2 + 0.5F);
		GL11.glScalef(-1F, -1F, 1F);
		GL11.glScalef(0.75F, 0.75F, 0.75F);
		
		GL11.glPushMatrix();
		switch (mug.getBlockMetadata())
		{
			case 0:
				GL11.glRotatef(90F, 0F, 1F, 0F);
				break;
			case 1:
				GL11.glRotatef(180F, 0F, 1F, 0F);
				break;
			case 2:
				GL11.glRotatef(270F, 0F, 1F, 0F);
				break;
			case 3:
				GL11.glRotatef(0F, 0F, 1F, 0F);
				break;
		}
		
		float scale = 0.0625F;
		bindTexture(mugTexture);
		mugModel.render(null, 0F, 0F, 0F, 0F, 0F, scale);
		GL11.glPopMatrix();
		
		ItemStack mugItem = new ItemStack(mug.mugItem, 1, mug.itemDamage);
		if (mugItem.getItem() != LOTRMod.mug)
		{
			bindTexture(TextureMap.locationItemsTexture);
			IIcon icon = mugItem.getIconIndex();
			float f1 = icon.getInterpolatedU(7);
			float f2 = icon.getInterpolatedU(8);
			float f3 = icon.getInterpolatedV(4);
			float f4 = icon.getInterpolatedV(5);
			double d3 = 0.125D;
			double d4 = -0.4375D;
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(-d3, d4, d3, (double)f1, (double)f4);
			tessellator.addVertexWithUV(d3, d4, d3, (double)f2, (double)f4);
			tessellator.addVertexWithUV(d3, d4, -d3, (double)f2, (double)f3);
			tessellator.addVertexWithUV(-d3, d4, -d3, (double)f1, (double)f3);
			tessellator.draw();
		}
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
