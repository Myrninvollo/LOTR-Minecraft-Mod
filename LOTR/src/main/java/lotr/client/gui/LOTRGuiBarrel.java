package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.inventory.LOTRContainerBarrel;
import lotr.common.tileentity.LOTRTileEntityBarrel;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class LOTRGuiBarrel extends GuiContainer
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/barrel/barrel.png");
	private static ResourceLocation brewingTexture = new ResourceLocation("lotr:gui/barrel/brewing.png");
	
	private LOTRTileEntityBarrel theBarrel;
	private GuiButton brewingButton;
	
	public LOTRGuiBarrel(InventoryPlayer inv, LOTRTileEntityBarrel barrel)
	{
		super(new LOTRContainerBarrel(inv, barrel));
		theBarrel = barrel;
		xSize = 210;
		ySize = 221;
	}
	
	@Override
    public void initGui()
    {
        super.initGui();
		buttonList.add(brewingButton = new GuiButton(0, guiLeft + 25, guiTop + 97, 100, 20, StatCollector.translateToLocal("container.lotr.barrel.startBrewing")));
	}
	
	@Override
    public void drawScreen(int i, int j, float f)
    {
		if (theBarrel.barrelMode == LOTRTileEntityBarrel.EMPTY)
		{
			brewingButton.enabled = theBarrel.getStackInSlot(9) != null;
			brewingButton.displayString = StatCollector.translateToLocal("container.lotr.barrel.startBrewing");
		}
		if (theBarrel.barrelMode == LOTRTileEntityBarrel.BREWING)
		{
			brewingButton.enabled = theBarrel.getStackInSlot(9) != null && theBarrel.getStackInSlot(9).getItemDamage() > 0;
			brewingButton.displayString = StatCollector.translateToLocal("container.lotr.barrel.stopBrewing");
		}
		if (theBarrel.barrelMode == LOTRTileEntityBarrel.FULL)
		{
			brewingButton.enabled = false;
			brewingButton.displayString = StatCollector.translateToLocal("container.lotr.barrel.startBrewing");
		}
		
		super.drawScreen(i, j, f);
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled && button.id == 0)
        {
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(mc.thePlayer.getEntityId());
        	data.writeByte((byte)mc.thePlayer.dimension);
        	
        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.brewing", data);
        	mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
		String s = theBarrel.getInventoryName();
		String s1 = theBarrel.getInvSubtitle();
        fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
		fontRendererObj.drawString(s1, xSize / 2 - fontRendererObj.getStringWidth(s1) / 2, 17, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 25, 127, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		mc.getTextureManager().bindTexture(brewingTexture);
		if (theBarrel.barrelMode == LOTRTileEntityBarrel.BREWING)
		{
			int k = theBarrel.getBrewProgressScaled(96);
			drawTexturedModalRect(guiLeft + 147, guiTop + 34 + 96 - k, 0, 96 - k, 50, k);
			int l = theBarrel.getBrewAnimationProgressScaled(96);
			drawTexturedModalRect(guiLeft + 147, guiTop + 34 + 96 - l, 50, 96 - l, 50, l);
		}
		if (theBarrel.barrelMode == LOTRTileEntityBarrel.FULL)
		{
			int k = theBarrel.getBarrelFullAmountScaled(96);
			drawTexturedModalRect(guiLeft + 147, guiTop + 34 + 96 - k, 100, 96 - k, 50, k);
		}
    }
}
