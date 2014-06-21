package lotr.client.gui;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRHiredNPCInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

public class LOTRGuiHiredFarmer extends LOTRGuiHiredNPC
{
	public LOTRGuiHiredFarmer(LOTREntityNPC npc)
	{
		super(npc);
	}

	@Override
    public void initGui()
    {
        super.initGui();
		
		buttonList.add(new LOTRGuiButtonOptions(0, guiLeft + xSize / 2 - 80, guiTop + 70, 160, 20, StatCollector.translateToLocal("lotr.gui.farmer.mode")));
		((LOTRGuiButtonOptions)buttonList.get(0)).setState(theNPC.hiredNPCInfo.isGuardMode());
		
		buttonList.add(new LOTRGuiSlider(1, guiLeft + xSize / 2 - 80, guiTop + 94, 160, 20, StatCollector.translateToLocal("lotr.gui.farmer.range"), 0F));
		((LOTRGuiSlider)buttonList.get(1)).sliderValue = (float)(theNPC.hiredNPCInfo.getGuardRange() - LOTRHiredNPCInfo.GUARD_RANGE_MIN) / (float)(LOTRHiredNPCInfo.GUARD_RANGE_MAX - LOTRHiredNPCInfo.GUARD_RANGE_MIN);
		((LOTRGuiSlider)buttonList.get(1)).setState(String.valueOf(theNPC.hiredNPCInfo.getGuardRange()));
		((LOTRGuiSlider)buttonList.get(1)).visible = theNPC.hiredNPCInfo.isGuardMode();
		
		buttonList.add(new LOTRGuiButtonOptions(2, guiLeft + xSize / 2 - 80, guiTop + 142, 160, 20, StatCollector.translateToLocal("lotr.gui.farmer.openInv")));
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
			sendActionPacket(button.id);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f)
	{
		super.drawScreen(i, j, f);
		
		String s = theNPC.hiredNPCInfo.getStatusString();
		fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 50, 0x373737);
	}
	
    public void updateScreen()
    {
        super.updateScreen();

		((LOTRGuiButtonOptions)buttonList.get(0)).setState(theNPC.hiredNPCInfo.isGuardMode());
		
		LOTRGuiSlider slider_guardRange = ((LOTRGuiSlider)buttonList.get(1));
		slider_guardRange.visible = theNPC.hiredNPCInfo.isGuardMode();
		if (slider_guardRange.dragging)
		{
			int i = LOTRHiredNPCInfo.GUARD_RANGE_MIN + Math.round(slider_guardRange.sliderValue * (LOTRHiredNPCInfo.GUARD_RANGE_MAX - LOTRHiredNPCInfo.GUARD_RANGE_MIN));
			theNPC.hiredNPCInfo.setGuardRange(i);
			slider_guardRange.setState(String.valueOf(i));
			sendActionPacket(slider_guardRange.id, i);
		}
    }
}
