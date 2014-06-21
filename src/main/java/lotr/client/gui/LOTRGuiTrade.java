package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRTradeEntry;
import lotr.common.entity.npc.LOTRTradeable;
import lotr.common.inventory.LOTRContainerTrade;
import lotr.common.inventory.LOTRSlotTrade;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class LOTRGuiTrade extends GuiContainer
{
	public static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/npc/trade.png");
	public LOTREntityNPC theEntity;
	private GuiButton tradeButton;
	
	public LOTRGuiTrade(InventoryPlayer inv, LOTRTradeable trader, World world)
	{
		super(new LOTRContainerTrade(inv, trader, world));
		ySize = 238;
		theEntity = (LOTREntityNPC)trader;
	}
	
	@Override
    public void initGui()
    {
        super.initGui();
		tradeButton = new LOTRGuiTradeButton(0, guiLeft + 79, guiTop + 135);
		tradeButton.enabled = false;
		buttonList.add(tradeButton);
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
        drawCenteredString(theEntity.getNPCName(), 89, 11, 0x373737);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.lotr.trade.buy"), 8, 32, 0x404040);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.lotr.trade.sell"), 8, 85, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 144, 0x404040);
		
		for (int k = 0; k < 9; k++)
		{
			LOTRSlotTrade tradeSlot = (LOTRSlotTrade)inventorySlots.inventorySlots.get(k);
			int cost = tradeSlot.cost();
			if (cost > 0)
			{
				drawCenteredString(Integer.valueOf(cost).toString(), 16 + k * 18, 66, 0x373737);
			}
		}
		
		int totalSellPrice = 0;
		for (int k = 0; k < 9; k++)
		{
			ItemStack item = ((Slot)inventorySlots.inventorySlots.get(k + 9)).getStack();
			if (item != null)
			{
				int[] ints = LOTRTradeEntry.getSellPrice_TradeCount_SellAmount(item, theEntity);
				int sellPrice = ints[0];
				int tradeCount = ints[1];
				if (sellPrice > 0)
				{
					drawCenteredString(Integer.valueOf(sellPrice).toString(), 16 + k * 18, 119, 0x373737);
					totalSellPrice += sellPrice * tradeCount;
				}
			}
		}
		
		if (totalSellPrice > 0)
		{
			fontRendererObj.drawString(StatCollector.translateToLocalFormatted("container.lotr.trade.sellPrice", new Object[] {Integer.valueOf(totalSellPrice)}), 100, 141, 0x404040);
		}
		
		tradeButton.enabled = totalSellPrice > 0;
		
        zLevel = 400F;
        itemRender.zLevel = 400F;
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(guiTexture);
		int buttonX = 8 + fontRendererObj.getStringWidth(StatCollector.translateToLocal("container.lotr.trade.sell")) + 10;
		int buttonY = 82;
		drawTexturedModalRect(buttonX, buttonY, 176, 54, 12, 12);

		int x = i - guiLeft;
		int y = j - guiTop;
		if (x >= buttonX && x < buttonX + 12 && y >= buttonY && y < buttonY + 12)
		{
			int sellTrades = theEntity.traderNPCInfo.getSellTrades().length;
			int rows = MathHelper.ceiling_double_int((double)sellTrades / 9D);
			for (int k = 0; k < rows; k++)
			{
				GL11.glColor4f(1F, 1F, 1F, 1F);
				mc.getTextureManager().bindTexture(guiTexture);
				drawTexturedModalRect(x, y + k * 18, 7, 96, 162, 18);
				for (int l = 0; l < 9; l++)
				{
					int index = k * 9 + l;
					if (index < theEntity.traderNPCInfo.getSellTrades().length)
					{
						ItemStack itemstack = theEntity.traderNPCInfo.getSellTrades()[index].item;
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glEnable(GL11.GL_CULL_FACE);
						itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack, x + l * 18 + 1, y + k * 18 + 1);
						itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack, x + l * 18 + 1, y + k * 18 + 1);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glColor4f(1F, 1F, 1F, 1F);
					}
				}
			}
		}
		
        zLevel = 0F;
        itemRender.zLevel = 0F;
	}

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled && button.id == 0)
        {
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(mc.thePlayer.getEntityId());
        	data.writeByte((byte)mc.thePlayer.dimension);
        	
        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.sell", data);
        	mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}
	
    private void drawCenteredString(String s, int i, int j, int k)
    {
        fontRendererObj.drawString(s, i - fontRendererObj.getStringWidth(s) / 2, j, k);
    }
}
