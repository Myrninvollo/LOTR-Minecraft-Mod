package lotr.client.render.tileentity;

import lotr.client.render.LOTRRenderBlocks;
import lotr.common.tileentity.LOTRTileEntityDwarvenDoor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderDwarvenDoor extends TileEntitySpecialRenderer
{
	private RenderBlocks renderBlocks;
	
	@Override
	public void func_147496_a(World world)
	{
		renderBlocks = new RenderBlocks(world);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		if (renderBlocks == null)
		{
			renderBlocks = new RenderBlocks(tileentity.getWorldObj());
		}
		
		LOTRTileEntityDwarvenDoor door = (LOTRTileEntityDwarvenDoor)tileentity;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
		float f1 = door.getGlowBrightness(f);
		GL11.glColor4f(f1, f1, f1, 1F);
		bindTexture(TextureMap.locationBlocksTexture);
		LOTRRenderBlocks.renderDwarvenDoorGlow(renderBlocks, door.xCoord, door.yCoord, door.zCoord);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}
}
