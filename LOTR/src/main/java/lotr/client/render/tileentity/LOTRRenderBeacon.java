package lotr.client.render.tileentity;

import lotr.client.model.LOTRModelBeacon;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderBeacon extends TileEntitySpecialRenderer
{
	private ModelBase beaconModel = new LOTRModelBeacon();
	private ResourceLocation beaconTexture = new ResourceLocation("lotr:item/beacon.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		bindTexture(beaconTexture);
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
		GL11.glScalef(1F, -1F, 1F);
		beaconModel.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glPopMatrix();
	}
	
	public void renderInvBeacon()
	{
		GL11.glRotatef(90F, 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		renderTileEntityAt(null, 0D, 0D, 0D, 0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}
}
