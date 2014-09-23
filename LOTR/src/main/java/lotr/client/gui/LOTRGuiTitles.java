package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.*;
import java.util.Map.Entry;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRTitle;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class LOTRGuiTitles extends LOTRGuiMenu
{
	private LOTRTitle.PlayerTitle currentTitle;
	
	private List<LOTRTitle> displayedTitles = new ArrayList();
	private int titleBeginIndex;
	private static final int maxDisplayedTitles = 12;
	private Map<LOTRTitle, Pair<Boolean, Pair<Integer, Integer>>> displayedTitleInfo = new HashMap();
	
	private LOTRTitle selectedTitle;
	private EnumChatFormatting selectedColor = EnumChatFormatting.WHITE;
	private int colorBoxWidth = 8;
	private int colorBoxGap = 4;
	private Map<EnumChatFormatting, Pair<Integer, Integer>> displayedColorBoxes = new HashMap();
	private GuiButton selectButton;
	private GuiButton removeButton;
	
	@Override
    public void initGui()
    {
		xSize = 256;
		super.initGui();
		buttonList.add(selectButton = new GuiButton(0, guiLeft + xSize / 2 - 10 - 80, guiTop + 230, 80, 20, StatCollector.translateToLocal("lotr.gui.titles.select")));
		buttonList.add(removeButton = new GuiButton(1, guiLeft + xSize / 2 + 10, guiTop + 230, 80, 20, StatCollector.translateToLocal("lotr.gui.titles.remove")));
		updateScreen();
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();

		currentTitle = LOTRLevelData.getData(mc.thePlayer).getPlayerTitle();
		
		displayedTitles.clear();
		List availableTitles = new ArrayList();
		List unavailableTitles = new ArrayList();
		for (LOTRTitle title : LOTRTitle.allTitles)
		{
			if (title.canPlayerUse(mc.thePlayer))
			{
				availableTitles.add(title);
			}
			else if (title.canDisplay(mc.thePlayer))
			{
				unavailableTitles.add(title);
			}
		}
		Collections.sort(availableTitles);
		Collections.sort(unavailableTitles);
		displayedTitles.addAll(availableTitles);
		displayedTitles.addAll(unavailableTitles);
		
		if (titleBeginIndex >= displayedTitles.size())
		{
			titleBeginIndex = displayedTitles.size() - 1;
		}
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		String s = StatCollector.translateToLocal("lotr.gui.titles.title");
		drawCenteredString(s, guiLeft + xSize / 2, guiTop - 30, 0xFFFFFF);
		
		String titleName = currentTitle == null ? StatCollector.translateToLocal("lotr.gui.titles.currentTitle.none") : currentTitle.getTitle().getDisplayName();
		EnumChatFormatting currentColor = currentTitle == null ? EnumChatFormatting.WHITE : currentTitle.getColor();
		titleName = currentColor + titleName + EnumChatFormatting.RESET;
		drawCenteredString(StatCollector.translateToLocalFormatted("lotr.gui.titles.currentTitle", titleName), guiLeft + xSize / 2, guiTop, 0xFFFFFF);
		
		displayedTitleInfo.clear();
		int titleX = guiLeft + xSize / 2;
		int titleY = guiTop + 30;
		int yIncrement = 12;
		for (int l = titleBeginIndex; l < titleBeginIndex + maxDisplayedTitles; l++)
		{
			if (l >= displayedTitles.size())
			{
				break;
			}
			
			LOTRTitle title = displayedTitles.get(l);

			String name = title.getDisplayName();
			
			int nameWidth = fontRendererObj.getStringWidth(name);
			int nameHeight = mc.fontRenderer.FONT_HEIGHT;
			int nameXMin = titleX - nameWidth / 2;
			int nameXMax = titleX + nameWidth / 2;
			int nameYMin = titleY;
			int nameYMax = titleY + nameHeight;
			
			boolean mouseOver = i >= nameXMin && i < nameXMax && j >= nameYMin && j < nameYMax;
			displayedTitleInfo.put(title, Pair.of(mouseOver, Pair.of(titleX, titleY)));
			
			int textColor = title.canPlayerUse(mc.thePlayer) ? (mouseOver ? 0xFFFFA0 : 0xFFFFFF) : (mouseOver ? 0xBBBBBB : 0x777777);
			drawCenteredString(name, titleX, titleY, textColor);
			
			drawVerticalLine(titleX - 70, titleY, titleY + yIncrement + 1, 0x99FFFFFF);
			drawVerticalLine(titleX + 70, titleY, titleY + yIncrement + 1, 0x99FFFFFF);

			titleY += yIncrement;
		}
		
		displayedColorBoxes.clear();
		if (selectedTitle != null)
		{
			String title = selectedColor + selectedTitle.getDisplayName();
			drawCenteredString(title, guiLeft + xSize / 2, guiTop + 190, 0xFFFFFF);
			
			List<EnumChatFormatting> colors = new ArrayList();
			for (EnumChatFormatting ecf : EnumChatFormatting.values())
			{
				if (ecf.isColor())
				{
					colors.add(ecf);
				}
			}

			int colorX = guiLeft + xSize / 2 - ((colorBoxWidth * colors.size()) + (colorBoxGap * (colors.size() - 1))) / 2;
			int colorY = guiTop + 205;
			for (EnumChatFormatting color : colors)
			{
				fontRendererObj.drawString(color + "", 0, 0, 0xFFFFFF);
				
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				drawTexturedModalRect(colorX, colorY, 0, 0, colorBoxWidth, colorBoxWidth);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				
				displayedColorBoxes.put(color, Pair.of(colorX, colorY));
				
				colorX += colorBoxWidth + colorBoxGap;
			}
		}
		
		selectButton.enabled = selectedTitle != null;
		removeButton.enabled = currentTitle != null;

		GL11.glColor4f(1F, 1F, 1F, 1F);
		super.drawScreen(i, j, f);
		
		for (Entry<LOTRTitle, Pair<Boolean, Pair<Integer, Integer>>> entry : displayedTitleInfo.entrySet())
		{
			LOTRTitle title = entry.getKey();
			String desc = title.getDescription();
			titleX = entry.getValue().getRight().getLeft();
			titleY = entry.getValue().getRight().getRight();
			boolean mouseOver = entry.getValue().getLeft();
			
			if (mouseOver)
			{
				int stringWidth = 200;
				List titleLines = fontRendererObj.listFormattedStringToWidth(desc, stringWidth);
				int offset = 10;
				int x = i + offset;
				int y = j + offset;
				func_146283_a(titleLines, x, y);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glColor4f(1F, 1F, 1F, 1F);
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.enabled)
		{
			if (button == selectButton && (currentTitle == null || selectedTitle != currentTitle.getTitle() || selectedColor != currentTitle.getColor()))
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeInt(mc.thePlayer.getEntityId());
				data.writeByte(mc.thePlayer.dimension);
				data.writeInt(selectedTitle.titleID);
				data.writeInt(selectedColor.getFormattingCode());
				
				C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.titleSelect", data);
	        	mc.thePlayer.sendQueue.addToSendQueue(packet);
			}	
			else if (button == removeButton)
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeInt(mc.thePlayer.getEntityId());
				data.writeByte(mc.thePlayer.dimension);
				data.writeInt(-1);
				data.writeInt(-1);
				
				C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.titleSelect", data);
	        	mc.thePlayer.sendQueue.addToSendQueue(packet);
			}
			else
			{
				super.actionPerformed(button);
			}
		}
	}
	
	@Override
    protected void keyTyped(char c, int i)
    {
		if (i == Keyboard.KEY_DOWN)
		{
			increaseTitleIndex();
			return;
		}
		
		if (i == Keyboard.KEY_UP)
		{
			decreaseTitleIndex();
			return;
		}
		
		super.keyTyped(c, i);
	}
	
	@Override
    protected void mouseClicked(int i, int j, int mouse)
    {
		if (mouse == 0)
		{
			for (Entry<LOTRTitle, Pair<Boolean, Pair<Integer, Integer>>> entry : displayedTitleInfo.entrySet())
			{
				LOTRTitle title = entry.getKey();
				boolean mouseOver = entry.getValue().getLeft();
		        if (mouseOver && title.canPlayerUse(mc.thePlayer))
		        {
		        	selectedTitle = title;
		        	selectedColor = EnumChatFormatting.WHITE;
		        	return;
		        }
			}
			
			if (!displayedColorBoxes.isEmpty())
			{
				for (Entry<EnumChatFormatting, Pair<Integer, Integer>> entry : displayedColorBoxes.entrySet())
				{
					EnumChatFormatting color = entry.getKey();
					int colorX = entry.getValue().getLeft();
					int colorY = entry.getValue().getRight();
					
					if (i >= colorX && i < colorX + colorBoxWidth && j >= colorY && j < colorY + colorBoxWidth)
					{
						selectedColor = color;
						break;
					}
				}
			}
		}
		
		super.mouseClicked(i, j, mouse);
    }
	
	@Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i < 0)
            {
            	increaseTitleIndex();
            }

            if (i > 0)
            {
            	decreaseTitleIndex();
            }
        }
    }
	
	private void decreaseTitleIndex()
	{
		titleBeginIndex = Math.max(titleBeginIndex - 1, 0);
	}
	
	private void increaseTitleIndex()
	{
		titleBeginIndex = Math.min(titleBeginIndex + 1, Math.max(0, displayedTitles.size() - maxDisplayedTitles));
	}
}
