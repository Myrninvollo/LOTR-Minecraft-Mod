package lotr.client.gui;

import lotr.common.inventory.LOTRContainerCraftingTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public abstract class LOTRGuiCraftingTable extends GuiContainer
{
	private static ResourceLocation craftingTexture = new ResourceLocation("textures/gui/container/crafting_table.png");
	
	private String unlocalizedName;
	
    public LOTRGuiCraftingTable(LOTRContainerCraftingTable container, String s)
    {
        super(container);
        unlocalizedName = s;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
    	String s = StatCollector.translateToLocal("container.lotr." + unlocalizedName);
        fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(craftingTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
