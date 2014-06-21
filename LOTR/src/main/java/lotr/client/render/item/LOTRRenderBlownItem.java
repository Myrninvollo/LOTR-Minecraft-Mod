package lotr.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderBlownItem implements IItemRenderer
{
	@Override
    public boolean handleRenderType(ItemStack itemstack, ItemRenderType type)
	{
		return type == ItemRenderType.EQUIPPED;
	}
    
	@Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemstack, ItemRendererHelper helper)
	{
		return false;
	}
    
	@Override
    public void renderItem(ItemRenderType type, ItemStack itemstack, Object... data)
	{
		Tessellator tessellator = Tessellator.instance;
		IIcon icon = itemstack.getItem().getIconFromDamage(0);
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0 || data[1] != Minecraft.getMinecraft().thePlayer)
		{
			GL11.glScalef(-1F, 1F, 1F);
			GL11.glTranslatef(-1.35F, 0F, 0F);
			GL11.glRotatef(-45F, 0F, 0F, 1F);
			GL11.glTranslatef(0F, 0.3F, 0F);
			GL11.glTranslatef(0F, 0F, 0.15F);
		}
		ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
