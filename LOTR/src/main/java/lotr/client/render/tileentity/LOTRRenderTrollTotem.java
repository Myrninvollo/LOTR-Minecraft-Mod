package lotr.client.render.tileentity;

import lotr.client.model.LOTRModelTrollTotem;
import lotr.common.tileentity.LOTRTileEntityTrollTotem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderTrollTotem extends TileEntitySpecialRenderer
{
	private LOTRModelTrollTotem totemModel = new LOTRModelTrollTotem();
	private ResourceLocation totemTexture = new ResourceLocation("lotr:item/trollTotem.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		LOTRTileEntityTrollTotem trollTotem = (LOTRTileEntityTrollTotem)tileentity;
		int i = trollTotem.getBlockMetadata();
		int meta = i & 3;
		float rotation = 0F;
		switch ((i & 12) >> 2)
		{
			case 0:
				rotation = 180F;
				break;
			case 1:
				rotation = 270F;
				break;
			case 2:
				rotation = 0F;
				break;
			case 3:
				rotation = 90F;
				break;
		}
		renderTrollTotem(d, d1, d2, meta, rotation, trollTotem.getJawRotation(f));
	}
	
	private void renderTrollTotem(double d, double d1, double d2, int meta, float rotation, float jawRotation)
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glRotatef(rotation, 0F, 1F, 0F);
		bindTexture(totemTexture);
		switch (meta)
		{
			case 0:
				totemModel.renderHead(0.0625F, jawRotation);
				break;
			case 1:
				totemModel.renderBody(0.0625F);
				break;
			case 2:
				totemModel.renderBase(0.0625F);
				break;
		}
		GL11.glPopMatrix();
	}
	
	public void renderInvTrollTotem(int meta)
	{
		GL11.glRotatef(90F, 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		renderTrollTotem(0D, 0D, 0D, meta, 0F, 0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}
}
