package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class LOTRGuiHiredNPC extends GuiScreen
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/npc/hired.png");
    public int xSize = 200;
    public int ySize = 220;
    public int guiLeft;
    public int guiTop;
	public LOTREntityNPC theNPC;
	public int page;
	
	public LOTRGuiHiredNPC(LOTREntityNPC npc)
	{
		theNPC = npc;
	}

	@Override
    public void initGui()
    {
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
	}

	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		String s = theNPC.getNPCName();
		fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 11, 0x373737);
		
		s = theNPC.getEntityClassName();
		fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 26, 0x373737);
		
		if (page == 0)
		{
			s = String.valueOf(theNPC.hiredNPCInfo.alignmentRequiredToCommand);
			if (theNPC.hiredNPCInfo.alignmentRequiredToCommand > 0)
			{
				s = "+" + s;
			}
			s = StatCollector.translateToLocalFormatted("lotr.hiredNPC.alignmentRequired", new Object[] {s});
			fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 200, 0x373737);
		}
		
		super.drawScreen(i, j, f);
	}
	
    public void updateScreen()
    {
        super.updateScreen();

        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
		
		if (!theNPC.isEntityAlive() || theNPC.hiredNPCInfo.getHiringPlayer() != mc.thePlayer || theNPC.getDistanceSqToEntity(mc.thePlayer) > 64D)
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
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		sendActionPacket(-1);
	}
	
	public void sendActionPacket(int action)
	{
		sendActionPacket(action, 0);
	}
	
	public void sendActionPacket(int action, int value)
	{
		ByteBuf data = Unpooled.buffer();
		
		data.writeInt(mc.thePlayer.getEntityId());
		data.writeByte((byte)mc.thePlayer.dimension);
		data.writeInt(theNPC.getEntityId());
		data.writeByte((byte)page);
		data.writeByte((byte)action);
		data.writeByte((byte)value);

		C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.commandGui", data);
		mc.thePlayer.sendQueue.addToSendQueue(packet);
	}
}
