package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.inventory.LOTRContainerPouch;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Charsets;

public class LOTRGuiPouch extends GuiContainer
{
	private static ResourceLocation texture = new ResourceLocation("lotr:gui/pouch.png");
	private LOTRContainerPouch thePouch;
	
	private GuiTextField theGuiTextField;
	
    public LOTRGuiPouch(EntityPlayer entityplayer)
    {
        super(new LOTRContainerPouch(entityplayer));
		thePouch = (LOTRContainerPouch)inventorySlots;
		ySize = 180;
    }
	
	@Override
    public void initGui()
    {
		super.initGui();
        theGuiTextField = new GuiTextField(fontRendererObj, guiLeft + xSize / 2 - 80, guiTop + 7, 160, 20);
        theGuiTextField.setText(thePouch.getDisplayName());
	}

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(texture);;
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int rows = thePouch.capacity / 9;
		for (int l = 0; l < rows; l++)
		{
			drawTexturedModalRect(guiLeft + 7, guiTop + 29 + l * 18, 0, 180, 162, 18);
		}
		
        GL11.glDisable(GL11.GL_LIGHTING);
        theGuiTextField.drawTextBox();
		GL11.glEnable(GL11.GL_LIGHTING);
    }
	
	@Override
    public void updateScreen()
    {
		super.updateScreen();
        theGuiTextField.updateCursorCounter();
    }
	
	@Override
    protected void keyTyped(char c, int i)
    {
        if (theGuiTextField.textboxKeyTyped(c, i))
        {
            renamePouch();
        }
        else
        {
            super.keyTyped(c, i);
		}
    }
	
	@Override
	protected void mouseClicked(int i, int j, int k)
    {
		super.mouseClicked(i, j, k);
		theGuiTextField.mouseClicked(i, j, k);
	}
	
	@Override
    protected boolean checkHotbarKeys(int i)
	{
		return false;
	}
	
    private void renamePouch()
    {
        String s = theGuiTextField.getText();
		thePouch.renamePouch(s);

		byte[] nameData = s.getBytes(Charsets.UTF_8);
    	ByteBuf data = Unpooled.buffer();
    	
    	data.writeInt(mc.thePlayer.getEntityId());
    	data.writeByte((byte)mc.thePlayer.dimension);
    	data.writeShort(nameData.length);
    	data.writeBytes(nameData);
    	
    	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.namePouch", data);
    	mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
