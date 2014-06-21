package lotr.client.gui;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.inventory.LOTRContainerHiredFarmerInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class LOTRGuiHiredFarmerInventory extends GuiContainer
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/npc/hiredFarmer.png");
	private LOTREntityNPC theNPC;
	
	public LOTRGuiHiredFarmerInventory(InventoryPlayer inv, LOTREntityNPC entity)
	{
		super(new LOTRContainerHiredFarmerInventory(inv, entity));
		theNPC = entity;
		ySize = 161;
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
		String s = theNPC.getNPCName();
        fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 67, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if (((Slot)inventorySlots.inventorySlots.get(0)).getStack() == null)
		{
			drawTexturedModalRect(guiLeft + 80, guiTop + 21, 176, 0, 16, 16);
		}
    }
}
