package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.LOTRMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class LOTRGuiHornSelect extends GuiScreen
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/horn_select.png");
	private static RenderItem itemRenderer = new RenderItem();
    private int xSize = 176;
    private int ySize = 256;
    private int guiLeft;
    private int guiTop;

	@Override
    public void initGui()
    {
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
		buttonList.add(new GuiButton(1, guiLeft + 40, guiTop + 40, 120, 20, StatCollector.translateToLocal("lotr.gui.hornSelect.haltReady")));
		buttonList.add(new GuiButton(3, guiLeft + 40, guiTop + 75, 120, 20, StatCollector.translateToLocal("lotr.gui.hornSelect.summon")));
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(mc.thePlayer.getEntityId());
        	data.writeByte((byte)mc.thePlayer.dimension);
        	data.writeByte((byte)button.id);
        	
        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.hornSelect", data);
        	mc.thePlayer.sendQueue.addToSendQueue(packet);

			mc.thePlayer.closeScreen();
		}
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        String s = StatCollector.translateToLocal("lotr.gui.hornSelect.title");
		fontRendererObj.drawString("Horn of Command Selection", guiLeft + 88 - fontRendererObj.getStringWidth("Horn of Command Selection") / 2, guiTop + 11, 0x373737);
		super.drawScreen(i, j, f);
		for (int k = 0; k < buttonList.size(); k++)
		{
			GuiButton button = (GuiButton)buttonList.get(k);
			itemRenderer.renderItemIntoGUI(fontRendererObj, mc.getTextureManager(), new ItemStack(LOTRMod.commandHorn, 1, button.id), button.xPosition - 22, button.yPosition + 2);
		}
	}
	
    public void updateScreen()
    {
        super.updateScreen();

        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
		
		ItemStack itemstack = mc.thePlayer.inventory.getCurrentItem();
		if (itemstack == null || itemstack.getItem() != LOTRMod.commandHorn || itemstack.getItemDamage() != 0)
		{
			mc.thePlayer.closeScreen();
		}
    }
	
	@Override
    protected void keyTyped(char c, int i)
    {
        if (i == 1 || i == mc.gameSettings.keyBindInventory.getKeyCode())
        {
            mc.thePlayer.closeScreen();
        }
	}
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
