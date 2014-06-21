package lotr.client.gui;

import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.inventory.LOTRContainerCamel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class LOTRGuiCamel extends GuiContainer
{
    private static final ResourceLocation camelGuiTexture = new ResourceLocation("lotr:gui/camel.png");
    private IInventory playerInv;
    private LOTREntityCamel theCamel;
    private float xPos;
    private float yPos;

    public LOTRGuiCamel(IInventory inv, LOTREntityCamel camel)
    {
        super(new LOTRContainerCamel(inv, camel));
        playerInv = inv;
        theCamel = camel;
        allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
    	String s = theCamel.camelInv.getInventoryName();
        fontRendererObj.drawString(theCamel.camelInv.hasCustomInventoryName() ? s : StatCollector.translateToLocal(s), 8, 6, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(camelGuiTexture);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        if (theCamel.hasChest())
        {
            drawTexturedModalRect(k + 79, l + 17, 0, ySize, 90, 54);
        }

        GuiInventory.func_147046_a(k + 51, l + 60, 17, (float)(k + 51) - xPos, (float)(l + 75 - 50) - yPos, theCamel);
    }

    @Override
    public void drawScreen(int i, int j, float k)
    {
        xPos = (float)i;
        yPos = (float)j;
        super.drawScreen(i, j, k);
    }
}