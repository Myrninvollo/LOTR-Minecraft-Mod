package lotr.client.render.tileentity;

import java.util.Random;

import lotr.common.tileentity.LOTRTileEntityPlate;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderPlateFood extends TileEntitySpecialRenderer
{
	private Random rand = new Random();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		LOTRTileEntityPlate plate = (LOTRTileEntityPlate)tileentity;
		if (plate.foodItem != null)
		{
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.125F, (float)d2 + 0.5F);
			rand.setSeed((long)((plate.xCoord * 3129871) ^ plate.zCoord * 116129781L ^ plate.yCoord));
			float rotation = rand.nextFloat() * 360F;
			GL11.glRotatef(rotation, 0F, 1F, 0F);
			GL11.glRotatef(90F, 1F, 0F, 0F);
			GL11.glTranslatef(-0.25F, -0.25F, 0F);
			ItemStack itemstack = new ItemStack(plate.foodItem, 1, plate.foodDamage);
			bindTexture(TextureMap.locationItemsTexture);
			IIcon icon = itemstack.getIconIndex();
			Tessellator tessellator = Tessellator.instance;
			float f4 = icon.getMinU();
			float f1 = icon.getMaxU();
			float f2 = icon.getMinV();
			float f3 = icon.getMaxV();
			GL11.glScalef(0.5625F, 0.5625F, 0.5625F);
			ItemRenderer.renderItemIn2D(tessellator, f1, f2, f4, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPopMatrix();
		}
	}
}
