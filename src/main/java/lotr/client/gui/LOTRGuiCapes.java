package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.LOTRCapes;
import lotr.common.LOTRCapes.CapeType;
import lotr.common.LOTRLevelData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRGuiCapes extends LOTRGui
{
	private static ModelBiped playerModel = new ModelBiped();
	public static int playerModelRotation = 0;
	
	private CapeType currentCapeType;
	private static int currentCapeTypeID;
	private LOTRCapes currentCape;
	private static int currentCapeID;
	
	static
	{
		playerModel.isChild = false;
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
		buttonList.add(new LOTRGuiButtonCapesArrows(2, true, guiLeft + xSize / 2 - 64, guiTop + 210));
		buttonList.add(new GuiButton(3, guiLeft + xSize / 2 - 40, guiTop + 210, 80, 20, StatCollector.translateToLocal("lotr.gui.capes.select")));
		buttonList.add(new LOTRGuiButtonCapesArrows(4, false, guiLeft + xSize / 2 + 44, guiTop + 210));
		buttonList.add(new GuiButton(5, guiLeft + xSize / 2 - 80, guiTop + 240, 160, 20, ""));
		updateCurrentCape();
	}
	
	private void updateCurrentCape()
	{
		currentCapeType = CapeType.values()[currentCapeTypeID];
		currentCape = (LOTRCapes)currentCapeType.list.get(currentCapeID);
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		String s = StatCollector.translateToLocal("lotr.gui.capes.title");
		fontRendererObj.drawString(s, guiLeft + 100 - fontRendererObj.getStringWidth(s) / 2, guiTop - 30, 0xFFFFFF);
		
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		RenderHelper.enableStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glTranslatef((float)(guiLeft + xSize / 2), (float)guiTop + 45F, 50F);
		float scale = 55F;
        GL11.glScalef(-scale, scale, scale);
		GL11.glRotatef(-30F, 1F, 0F, 0F);
        GL11.glRotatef((float)playerModelRotation + f * 2F, 0F, 1F, 0F);
		mc.getTextureManager().bindTexture(mc.thePlayer.getLocationSkin());
		playerModel.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		mc.getTextureManager().bindTexture(currentCape.capeTexture);
		GL11.glTranslatef(0F, 0F, 0.125F);
		GL11.glRotatef(180F, 0F, 1F, 0F);
		GL11.glRotatef(-10F, 1F, 0F, 0F);
		playerModel.renderCloak(0.0625F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		int i1 = guiLeft + (xSize / 2);
		int j1 = guiTop + 145;
		for (int l = 0; l < currentCape.capeDescription.length; l++)
		{
			String s1 = currentCape.capeDescription[l];
			fontRendererObj.drawString(s1, i1 - fontRendererObj.getStringWidth(s1) / 2, j1, 0xFFFFFF);
			if (l == 0)
			{
				j1 += fontRendererObj.FONT_HEIGHT * 2;
			}
			else
			{
				j1 += fontRendererObj.FONT_HEIGHT;
			}
		}
		
		((GuiButton)buttonList.get(2)).enabled = currentCapeID > 0;
		((GuiButton)buttonList.get(3)).enabled = currentCape.canPlayerWearCape(mc.thePlayer);
		((GuiButton)buttonList.get(3)).displayString = LOTRLevelData.getCape(mc.thePlayer) == currentCape ? StatCollector.translateToLocal("lotr.gui.capes.selected") : StatCollector.translateToLocal("lotr.gui.capes.select");
		((GuiButton)buttonList.get(4)).enabled = currentCapeID < currentCapeType.list.size() - 1;
		((GuiButton)buttonList.get(5)).displayString = currentCapeType.displayName;
		
		super.drawScreen(i, j, f);
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
			if (button.id == 2)
			{
				if (currentCapeID > 0)
				{
					currentCapeID--;
					updateCurrentCape();
				}
			}
			
			else if (button.id == 3)
			{
	        	ByteBuf data = Unpooled.buffer();
	        	
	        	data.writeInt(mc.thePlayer.getEntityId());
	        	data.writeByte((byte)mc.thePlayer.dimension);
	        	data.writeByte((byte)currentCapeID);
	        	data.writeByte((byte)currentCapeTypeID);
	        	
	        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.selectCape", data);
	        	mc.thePlayer.sendQueue.addToSendQueue(packet);
			}
			
			else if (button.id == 4)
			{
				if (currentCapeID < currentCapeType.list.size() - 1)
				{
					currentCapeID++;
					updateCurrentCape();
				}
			}
			
			else if (button.id == 5)
			{
				if (currentCapeTypeID < CapeType.values().length - 1)
				{
					currentCapeTypeID++;
				}
				else
				{
					currentCapeTypeID = 0;
				}
				currentCapeID = 0;
				updateCurrentCape();
			}
			
			else
			{
				super.actionPerformed(button);
			}
		}
	}
}
