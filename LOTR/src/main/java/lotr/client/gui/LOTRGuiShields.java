package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import lotr.client.render.LOTRRenderShield;
import lotr.common.*;
import lotr.common.LOTRShields.ShieldType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRGuiShields extends LOTRGuiMenu
{
	private static ModelBiped playerModel = new ModelBiped();
	public static int playerModelRotation = 0;
	
	private ShieldType currentShieldType;
	private static int currentShieldTypeID;
	private LOTRShields currentShield;
	private static int currentShieldID;
	
	private GuiButton shieldLeft;
	private GuiButton shieldRight;
	private GuiButton shieldSelect;
	private GuiButton changeCategory;
	
	static
	{
		playerModel.isChild = false;
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
		buttonList.add(shieldLeft = new LOTRGuiButtonShieldsArrows(0, true, guiLeft + xSize / 2 - 64, guiTop + 210));
		buttonList.add(shieldSelect = new GuiButton(1, guiLeft + xSize / 2 - 40, guiTop + 210, 80, 20, StatCollector.translateToLocal("lotr.gui.shields.select")));
		buttonList.add(shieldRight = new LOTRGuiButtonShieldsArrows(2, false, guiLeft + xSize / 2 + 44, guiTop + 210));
		buttonList.add(changeCategory = new GuiButton(3, guiLeft + xSize / 2 - 80, guiTop + 240, 160, 20, ""));
		updateCurrentShield();
	}
	
	private void updateCurrentShield()
	{
		currentShieldType = ShieldType.values()[currentShieldTypeID];
		currentShield = (LOTRShields)currentShieldType.list.get(currentShieldID);
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		String s = StatCollector.translateToLocal("lotr.gui.shields.title");
		drawCenteredString(s, guiLeft + xSize / 2, guiTop - 30, 0xFFFFFF);
		
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
		
		LOTRRenderShield.renderShield(currentShield, null, playerModel);
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		int x = guiLeft + (xSize / 2);
		int y = guiTop + 145;
		
		s = currentShield.getShieldName();
		drawCenteredString(s, x, y, 0xFFFFFF);
		
		y += fontRendererObj.FONT_HEIGHT * 2;
		
		List desc = fontRendererObj.listFormattedStringToWidth(currentShield.getShieldDesc(), 200);
		for (int l = 0; l < desc.size(); l++)
		{
			s = (String)desc.get(l);
			drawCenteredString(s, x, y, 0xFFFFFF);
			y += fontRendererObj.FONT_HEIGHT;
		}
		
		shieldLeft.enabled = currentShieldID > 0;
		shieldSelect.enabled = currentShield.canPlayerWear(mc.thePlayer);
		shieldSelect.displayString = LOTRLevelData.getData(mc.thePlayer).getShield() == currentShield ? StatCollector.translateToLocal("lotr.gui.shields.selected") : StatCollector.translateToLocal("lotr.gui.shields.select");
		shieldRight.enabled = currentShieldID < currentShieldType.list.size() - 1;
		changeCategory.displayString = currentShieldType.getDisplayName();
		
		super.drawScreen(i, j, f);
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
			if (button == shieldLeft)
			{
				if (currentShieldID > 0)
				{
					currentShieldID--;
					updateCurrentShield();
				}
			}
			
			else if (button == shieldSelect)
			{
	        	ByteBuf data = Unpooled.buffer();
	        	
	        	data.writeInt(mc.thePlayer.getEntityId());
	        	data.writeByte((byte)mc.thePlayer.dimension);
	        	data.writeByte((byte)currentShieldID);
	        	data.writeByte((byte)currentShieldTypeID);
	        	
	        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.selectShld", data);
	        	mc.thePlayer.sendQueue.addToSendQueue(packet);
			}
			
			else if (button == shieldRight)
			{
				if (currentShieldID < currentShieldType.list.size() - 1)
				{
					currentShieldID++;
					updateCurrentShield();
				}
			}
			
			else if (button == changeCategory)
			{
				if (currentShieldTypeID < ShieldType.values().length - 1)
				{
					currentShieldTypeID++;
				}
				else
				{
					currentShieldTypeID = 0;
				}
				currentShieldID = 0;
				updateCurrentShield();
			}
			
			else
			{
				super.actionPerformed(button);
			}
		}
	}
}
