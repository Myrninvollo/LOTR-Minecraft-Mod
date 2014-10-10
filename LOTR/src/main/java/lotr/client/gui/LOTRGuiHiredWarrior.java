package lotr.client.gui;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRHiredNPCInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

public class LOTRGuiHiredWarrior extends LOTRGuiHiredNPC
{
	private static String[] pageTitles = {"Overview", "Options"};
	private GuiButton buttonLeft;
	private GuiButton buttonRight;
	private boolean updatePage;
	
	public LOTRGuiHiredWarrior(LOTREntityNPC npc)
	{
		super(npc);
	}

	@Override
    public void initGui()
    {
        super.initGui();
		
		if (page == 1)
		{
			buttonList.add(new LOTRGuiButtonOptions(0, guiLeft + xSize / 2 - 80, guiTop + 122, 160, 20, StatCollector.translateToLocal("lotr.gui.warrior.obeyHaltReady")));
			buttonList.add(new LOTRGuiButtonOptions(1, guiLeft + xSize / 2 - 80, guiTop + 146, 160, 20, StatCollector.translateToLocal("lotr.gui.warrior.obeySummon")));
			buttonList.add(new LOTRGuiButtonOptions(2, guiLeft + xSize / 2 - 80, guiTop + 194, 160, 20, StatCollector.translateToLocal("lotr.gui.warrior.teleport")));
			
			buttonList.add(new LOTRGuiButtonOptions(3, guiLeft + xSize / 2 - 80, guiTop + 50, 160, 20, StatCollector.translateToLocal("lotr.gui.warrior.guardMode")));
			buttonList.add(new LOTRGuiSlider(4, guiLeft + xSize / 2 - 80, guiTop + 74, 160, 20, StatCollector.translateToLocal("lotr.gui.warrior.guardRange"), 0F));
			((LOTRGuiSlider)buttonList.get(4)).setSliderValue(theNPC.hiredNPCInfo.getGuardRange(), LOTRHiredNPCInfo.GUARD_RANGE_MIN, LOTRHiredNPCInfo.GUARD_RANGE_MAX);
		}
		
		buttonLeft = new LOTRGuiButtonLeftRight(1000, true, guiLeft - 160, guiTop + 50, "");
		buttonRight = new LOTRGuiButtonLeftRight(1001, false, guiLeft + xSize + 40, guiTop + 50, "");
		buttonList.add(buttonLeft);
		buttonList.add(buttonRight);
		
		if (page == 0)
		{
			buttonLeft.displayString = pageTitles[pageTitles.length - 1];
		}
		else
		{
			buttonLeft.displayString = pageTitles[page - 1];
		}
		
		if (page == pageTitles.length - 1)
		{
			buttonRight.displayString = pageTitles[0];
		}
		else
		{
			buttonRight.displayString = pageTitles[page + 1];
		}
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
		if (button instanceof LOTRGuiSlider)
		{
			return;
		}
		
        if (button.enabled)
        {
			if (button instanceof LOTRGuiButtonLeftRight)
			{
				if (button == buttonLeft)
				{
					page--;
					if (page < 0)
					{
						page = pageTitles.length - 1;
					}
				}
				
				else if (button == buttonRight)
				{
					page++;
					if (page >= pageTitles.length)
					{
						page = 0;
					}
				}
				
				buttonList.clear();
				updatePage = true;
			}
			else
			{
				sendActionPacket(button.id);
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f)
	{
		super.drawScreen(i, j, f);
		
		if (page == 0)
		{
			String s = StatCollector.translateToLocal("lotr.gui.warrior.health") + ": " + Math.round(theNPC.getHealth()) + "/" + Math.round(theNPC.getMaxHealth());
			fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 50, 0x373737);
			
			s = theNPC.hiredNPCInfo.getStatusString();
			fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 62, 0x373737);
			
			s = StatCollector.translateToLocal("lotr.gui.warrior.kills") + ": " + theNPC.hiredNPCInfo.mobKills;
			fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 74, 0x373737);
		}
	}
	
    public void updateScreen()
    {
		if (updatePage)
		{
			initGui();
			updatePage = false;
		}
		
        super.updateScreen();
		
		if (page == 1)
		{
			((LOTRGuiButtonOptions)buttonList.get(0)).setState(theNPC.hiredNPCInfo.obeyHornHaltReady);
			((LOTRGuiButtonOptions)buttonList.get(0)).enabled = !theNPC.hiredNPCInfo.isGuardMode();
			
			((LOTRGuiButtonOptions)buttonList.get(1)).setState(theNPC.hiredNPCInfo.obeyHornSummon);
			((LOTRGuiButtonOptions)buttonList.get(1)).enabled = !theNPC.hiredNPCInfo.isGuardMode();
			
			((LOTRGuiButtonOptions)buttonList.get(2)).setState(theNPC.hiredNPCInfo.teleportAutomatically);
			((LOTRGuiButtonOptions)buttonList.get(2)).enabled = !theNPC.hiredNPCInfo.isGuardMode();
			
			((LOTRGuiButtonOptions)buttonList.get(3)).setState(theNPC.hiredNPCInfo.isGuardMode());
			
			LOTRGuiSlider slider_guardRange = ((LOTRGuiSlider)buttonList.get(4));
			slider_guardRange.visible = theNPC.hiredNPCInfo.isGuardMode();
			if (slider_guardRange.dragging)
			{
				int i = slider_guardRange.getSliderValue(LOTRHiredNPCInfo.GUARD_RANGE_MIN, LOTRHiredNPCInfo.GUARD_RANGE_MAX);
				theNPC.hiredNPCInfo.setGuardRange(i);
				slider_guardRange.setState(String.valueOf(i));
				sendActionPacket(slider_guardRange.id, i);
			}
		}
    }
}
