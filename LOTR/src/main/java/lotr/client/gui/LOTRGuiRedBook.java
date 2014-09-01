package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.*;
import java.util.Map.Entry;

import lotr.common.*;
import lotr.common.quest.LOTRMiniQuest;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.*;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class LOTRGuiRedBook extends LOTRGuiScreenBase
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/quest/redBook.png");
	private static ResourceLocation guiTexture_miniquests = new ResourceLocation("lotr:gui/quest/redBook_miniquests.png");
	
	private static RenderItem renderItem = new RenderItem();

	public int xSize = 420;
    public int ySize = 256;
    private int guiLeft;
    private int guiTop;
    
    private int pageWidth = 188;
    private int pageTop = 18;
    private int pageBorder = 5;
    
    private float currentScroll = 0F;
    private boolean isScrolling = false;
	private boolean wasMouseDown;
	
	private int scrollBarWidth = 12;
	private int scrollBarHeight = 216;
	private int scrollBarX = pageWidth + 198;
	private int scrollBarY = 18;
	private int scrollBarBorder = 1;
	private int scrollWidgetWidth = 10;
	private int scrollWidgetHeight = 17;
    
	private Map<LOTRMiniQuest, Pair<Integer, Integer>> displayedMiniQuests = new HashMap();
	private int maxDisplayedMiniQuests = 4;
    private int miniquestPanelWidth = 170;
    private int miniquestPanelHeight = 45;
    private int miniquestDelX = 158;
    private int miniquestDelY = 4;
    private int miniquestDelWidth = 8;
    private int miniquestDelHeight = 8;
    
    private LOTRMiniQuest deletingMiniquest;
    
    private GuiButton buttonQuestDelete;
    private GuiButton buttonQuestDeleteCancel;
    
    private int textColor = 0x7A5D43;
    
    private static Page page;
    
    private static enum Page
    {
    	MINIQUESTS("miniquests");
    	
    	private String name;
    	
    	private Page(String s)
    	{
    		name = s;
    	}
    	
    	public String getTitle()
    	{
    		return StatCollector.translateToLocal("lotr.gui.redBook.page." + name);
    	}
    }
	
	public LOTRGuiRedBook()
	{
	}
	
	@Override
    public void initGui()
    {
		if (page == null)
		{
			page = Page.values()[0];
		}
				
        guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		
		int buttonX = guiLeft + xSize / 2 + pageWidth / 2;
		int buttonY = guiTop + ySize - 60;
		buttonList.add((buttonQuestDelete = new GuiButton(0, buttonX - 10 - 60, buttonY, 60, 20, StatCollector.translateToLocal("lotr.gui.redBook.miniquests.deleteYes"))));
		buttonList.add((buttonQuestDeleteCancel = new GuiButton(1, buttonX + 10, buttonY, 60, 20, StatCollector.translateToLocal("lotr.gui.redBook.miniquests.deleteNo"))));
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		displayedMiniQuests.clear();
		
		setupScrollBar(i, j);

		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(guiTexture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize, 512);
		
		int x = guiLeft + xSize / 2 - pageWidth / 2;
		int y = guiTop + 30;
		
		float scale = 2F;
		float invScale = 1F / scale;
		x *= invScale;
		y *= invScale;
		GL11.glScalef(scale, scale, scale);
		drawCenteredString(page.getTitle(), x, y, textColor);
		GL11.glScalef(invScale, invScale, invScale);
		
		x = guiLeft + xSize / 2 - pageWidth / 2;
		
		drawCenteredString(LOTRTime.ShireReckoning.getShireDate().getDateName(false), guiLeft + xSize / 2 - pageWidth / 2, guiTop + ySize - 30, textColor);
		
		if (page == Page.MINIQUESTS)
		{
			drawCenteredString(StatCollector.translateToLocalFormatted("lotr.gui.redBook.miniquests.active", new Object[] {getPlayerData().getActiveMiniQuests().size()}), x, guiTop + 70, textColor);
			
			drawCenteredString(StatCollector.translateToLocalFormatted("lotr.gui.redBook.miniquests.complete", new Object[] {getPlayerData().getCompletedMiniQuests()}), x, guiTop + 90, textColor);
			
			if (deletingMiniquest == null)
			{
				List<LOTRMiniQuest> miniquests = getMiniQuests();
				int size = miniquests.size();
				if (size > 0)
				{
					Collections.sort(miniquests);
					
					int min = 0 + Math.round(currentScroll * (size - maxDisplayedMiniQuests));
					int max = maxDisplayedMiniQuests - 1 + Math.round(currentScroll * (size - maxDisplayedMiniQuests));
					min = Math.max(min, 0);
					max = Math.min(max, size - 1);
					
					for (int index = min; index <= max; index++)
					{
						LOTRMiniQuest quest = miniquests.get(index);
						int displayIndex = index - min;
						int questX = guiLeft + xSize / 2 + pageBorder;
						int questY = guiTop + pageTop + (displayIndex * (4 + miniquestPanelHeight));
						
						renderMiniQuestPanel(quest, questX, questY);
						
						displayedMiniQuests.put(quest, Pair.of(questX, questY));
					}
				}
			}
			else
			{
				drawCenteredString(StatCollector.translateToLocal("lotr.gui.redBook.miniquests.delete1"), guiLeft + xSize / 2 + pageWidth / 2, guiTop + 50, textColor);
				drawCenteredString(StatCollector.translateToLocal("lotr.gui.redBook.miniquests.delete2"), guiLeft + xSize / 2 + pageWidth / 2, guiTop + 50 + fontRendererObj.FONT_HEIGHT, textColor);
				
				int questX = guiLeft + xSize / 2 + pageWidth / 2 - miniquestPanelWidth / 2;
				int questY = guiTop + pageTop + 80;
				
				renderMiniQuestPanel(deletingMiniquest, questX, questY);
			}
		}
		
		if (hasScrollBar())
		{
			mc.getTextureManager().bindTexture(guiTexture_miniquests);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			drawTexturedModalRect(guiLeft + scrollBarX, guiTop + scrollBarY, 244, 0, scrollBarWidth, scrollBarHeight);
			
			if (canScroll())
			{
				int scroll = (int)(currentScroll * (scrollBarHeight - (scrollBarBorder) * 2 - scrollWidgetHeight));
				drawTexturedModalRect(guiLeft + scrollBarX + scrollBarBorder, guiTop + scrollBarY + scrollBarBorder + scroll, 224, 0, scrollWidgetWidth, scrollWidgetHeight);
			}
			else
			{
				drawTexturedModalRect(guiLeft + scrollBarX + scrollBarBorder, guiTop + scrollBarY + scrollBarBorder, 234, 0, scrollWidgetWidth, scrollWidgetHeight);
			}
		}
		
		boolean hasQuestDeleteButtons = page == Page.MINIQUESTS && deletingMiniquest != null;
		buttonQuestDelete.enabled = buttonQuestDelete.visible = hasQuestDeleteButtons;
		buttonQuestDeleteCancel.enabled = buttonQuestDeleteCancel.visible = hasQuestDeleteButtons;

		super.drawScreen(i, j, f);
	}
	
	private void renderMiniQuestPanel(LOTRMiniQuest quest, int questX, int questY)
	{
		GL11.glPushMatrix();
		
		mc.getTextureManager().bindTexture(guiTexture_miniquests);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(questX, questY, 0, 0, miniquestPanelWidth, miniquestPanelHeight);
		
		float[] factionColors = quest.entityFaction.factionColor.getColorComponents(null);
		GL11.glColor4f(factionColors[0], factionColors[1], factionColors[2], 1F);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		drawTexturedModalRect(questX, questY, 0, miniquestPanelHeight, miniquestPanelWidth, miniquestPanelHeight);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		String questName = quest.entityName;
		String factionName = quest.entityFaction.factionName();
		if (quest.isEntityDead())
		{
			questName = EnumChatFormatting.STRIKETHROUGH + questName;
			factionName = EnumChatFormatting.STRIKETHROUGH + factionName;
		}
		
		fontRendererObj.drawString(questName, questX + 4, questY + 4, textColor);
		fontRendererObj.drawString(factionName, questX + 4, questY + 4 + fontRendererObj.FONT_HEIGHT, textColor);
		
		if (quest.isEntityDead())
		{
			fontRendererObj.drawString(StatCollector.translateToLocal("lotr.miniquest.entityDead"), questX + 4, questY + 25, 0xFF0000);
		}
		else
		{
			fontRendererObj.drawString(quest.getQuestObjective(), questX + 4, questY + 25, textColor);
			fontRendererObj.drawString(quest.getQuestProgress(), questX + 4, questY + 25 + fontRendererObj.FONT_HEIGHT, textColor);
		}
		
		mc.getTextureManager().bindTexture(guiTexture_miniquests);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(questX + miniquestDelX, questY + miniquestDelY, miniquestPanelWidth, 0, miniquestDelWidth, miniquestDelHeight);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), quest.getQuestIcon(), questX + 149, questY + 24);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		GL11.glPopMatrix();
	}
	
	private void setupScrollBar(int i, int j)
	{
        boolean isMouseDown = Mouse.isButtonDown(0);
        
        int i1 = guiLeft + scrollBarX;
        int j1 = guiTop + scrollBarY;
        int i2 = i1 + scrollBarWidth;
        int j2 = j1 + scrollBarHeight;

        if (!wasMouseDown && isMouseDown && i >= i1 && j >= j1 && i < i2 && j < j2)
        {
            isScrolling = canScroll();
        }

        if (!isMouseDown)
        {
            isScrolling = false;
        }

        wasMouseDown = isMouseDown;

        if (isScrolling)
        {
            currentScroll = ((float)(j - j1) - ((float)scrollWidgetHeight / 2F)) / ((float)(j2 - j1) - (float)scrollWidgetHeight);

            if (currentScroll < 0F)
            {
                currentScroll = 0F;
            }

            if (currentScroll > 1F)
            {
                currentScroll = 1F;
            }
        }
	}
	
	private boolean hasScrollBar()
	{
		return page == Page.MINIQUESTS && deletingMiniquest == null;
	}
	
	private boolean canScroll()
	{
		return hasScrollBar() && getMiniQuests().size() > maxDisplayedMiniQuests;
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.enabled)
		{
			if (button == buttonQuestDelete && deletingMiniquest != null)
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeInt(mc.thePlayer.getEntityId());
				data.writeByte(mc.thePlayer.dimension);
				data.writeLong(deletingMiniquest.entityUUID.getMostSignificantBits());
				data.writeLong(deletingMiniquest.entityUUID.getLeastSignificantBits());

				C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.mqDelete", data);
				mc.thePlayer.sendQueue.addToSendQueue(packet);
				
				deletingMiniquest = null;
			}
			
			if (button == buttonQuestDeleteCancel && deletingMiniquest != null)
			{
				deletingMiniquest = null;
			}
		}
	}
	
	@Override
    protected void mouseClicked(int i, int j, int mouse)
    {
		if (mouse == 0)
		{
			if (page == Page.MINIQUESTS && deletingMiniquest == null)
			{
				for (Entry<LOTRMiniQuest, Pair<Integer, Integer>> entry : displayedMiniQuests.entrySet())
				{
					int questX = entry.getValue().getLeft();
					int questY = entry.getValue().getRight();
					
					int i1 = questX + miniquestDelX;
					int j1 = questY + miniquestDelY;
					int i2 = i1 + miniquestDelWidth;
					int j2 = j1 + miniquestDelHeight;
					
			        if (i >= i1 && j >= j1 && i < i2 && j < j2)
			        {
			        	deletingMiniquest = entry.getKey();
			        	return;
			        }
				}
			}
		}
		
		super.mouseClicked(i, j, mouse);
    }
	
	@Override
    protected void keyTyped(char c, int i)
    {
        if ((i == Keyboard.KEY_ESCAPE || i == mc.gameSettings.keyBindInventory.getKeyCode()) && deletingMiniquest != null)
        {
            deletingMiniquest = null;
        }
        else
        {
        	super.keyTyped(c, i);
        }
	}
	
	@Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && canScroll())
        {
            int j = getMiniQuests().size() - maxDisplayedMiniQuests;

            if (i > 0)
            {
                i = 1;
            }

            if (i < 0)
            {
                i = -1;
            }

            currentScroll = (float)((double)currentScroll - (double)i / (double)j);

            if (currentScroll < 0F)
            {
                currentScroll = 0F;
            }

            if (currentScroll > 1F)
            {
                currentScroll = 1F;
            }
        }
    }
	
	private LOTRPlayerData getPlayerData()
	{
		return LOTRLevelData.getData(mc.thePlayer);
	}
	
	private List<LOTRMiniQuest> getMiniQuests()
	{
		return getPlayerData().getMiniQuests();
	}
}
