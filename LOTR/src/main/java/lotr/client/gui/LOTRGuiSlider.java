package lotr.client.gui;

import lotr.common.entity.npc.LOTRHiredNPCInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class LOTRGuiSlider extends GuiButton
{
    public float sliderValue = 1F;
    public boolean dragging = false;
	private String baseDisplayString;

    public LOTRGuiSlider(int id, int x, int y, int width, int height, String s, float f)
    {
        super(id, x, y, width, height, s);
		baseDisplayString = s;
        sliderValue = f;
    }
	
    public void setState(String s)
	{
		displayString = baseDisplayString + ": " + s;
	}

    @Override
	public int getHoverState(boolean flag)
    {
        return 0;
    }
    
    public int getSliderValue(int min, int max)
    {
    	return min + Math.round(sliderValue * (max - min));
    }
    
    public void setSliderValue(int value, int min, int max)
    {
    	value = MathHelper.clamp_int(value, min, max);
    	sliderValue = (float)(value - min) / (float)(max - min);
    	setState(String.valueOf(value));
    }

	@Override
	protected void mouseDragged(Minecraft mc, int i, int j)
    {
        if (visible && enabled)
        {
            if (dragging)
            {
                sliderValue = (float)(i - (xPosition + 4)) / (float)(width - 8);

                if (sliderValue < 0F)
                {
                    sliderValue = 0F;
                }

                if (sliderValue > 1F)
                {
                    sliderValue = 1F;
                }
            }

            GL11.glColor4f(1F, 1F, 1F, 1F);
            drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)), yPosition, 0, 66, 4, 20);
            drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)) + 4, yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int i, int j)
    {
        if (super.mousePressed(mc, i, j))
        {
            sliderValue = (float)(i - (xPosition + 4)) / (float)(width - 8);

            if (sliderValue < 0F)
            {
                sliderValue = 0F;
            }

            if (sliderValue > 1F)
            {
                sliderValue = 1F;
            }

            dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void mouseReleased(int i, int j)
    {
        dragging = false;
    }
}
