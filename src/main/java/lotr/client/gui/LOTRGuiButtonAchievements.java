package lotr.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRGuiButtonAchievements extends GuiButton
{
	private static ResourceLocation iconsTexture = new ResourceLocation("lotr:gui/achievements/icons.png");
	private boolean leftOrRight;
	
    public LOTRGuiButtonAchievements(int i, boolean flag, int j, int k)
    {
        super(i, j, k, 16, 24, "");
		leftOrRight = flag;
    }

    @Override
    public void drawButton(Minecraft mc, int i, int j)
    {
        if (visible)
        {
            mc.getTextureManager().bindTexture(iconsTexture);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            boolean flag = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
            int k = 0;
			int l = leftOrRight ? 100 : 124;

            if (!enabled)
            {
                k += width * 2;
            }
            else if (flag)
            {
                k += width;
            }

            drawTexturedModalRect(xPosition, yPosition, k, l, width, height);
        }
    }
}
