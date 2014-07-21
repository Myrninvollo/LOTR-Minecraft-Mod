package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.UUID;

import lotr.common.entity.item.LOTREntityBanner;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

public class LOTRGuiBanner extends LOTRGuiScreenBase
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/banner_edit.png");

	private LOTREntityBanner theBanner;
	public int xSize = 200;
    public int ySize = 200;
    private int guiLeft;
    private int guiTop;
    private GuiButton modeButton;
    private GuiTextField[] allowedPlayers = new GuiTextField[LOTREntityBanner.MAX_PLAYERS];
    private boolean[] invalidUsernames = new boolean[LOTREntityBanner.MAX_PLAYERS];
	
	public LOTRGuiBanner(LOTREntityBanner banner)
	{
		theBanner = banner;
	}
	
	@Override
    public void initGui()
    {
        guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		buttonList.add((modeButton = new GuiButton(0, guiLeft + xSize / 2 - 80, guiTop + 20, 160, 20, "")));
		
		for (int i = 0; i < allowedPlayers.length; i++)
		{
			GuiTextField textBox = new GuiTextField(fontRendererObj, guiLeft + xSize / 2 - 80, guiTop + 70 + i * 24, 160, 20);
			if (theBanner.allowedPlayers[i] != null)
			{
				UUID uuid = theBanner.allowedPlayers[i];
				GameProfile profile = mc.getIntegratedServer().func_152358_ax().func_152652_a(uuid);
				if (StringUtils.isEmpty(profile.getName()))
				{
					MinecraftServer.getServer().func_147130_as().fillProfileProperties(profile, true);
				}
				textBox.setText(profile.getName());
			}
			allowedPlayers[i] = textBox;
		}
		
		allowedPlayers[0].setEnabled(false);
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(guiTexture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		String title = StatCollector.translateToLocal("lotr.gui.bannerEdit.title");
		fontRendererObj.drawString(title, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(title) / 2, guiTop + 6, 0x404040);

		if (theBanner.playerSpecificProtection)
		{
			modeButton.displayString = StatCollector.translateToLocal("lotr.gui.bannerEdit.protectionMode.playerSpecific");
			
			String s = StatCollector.translateToLocal("lotr.gui.bannerEdit.protectionMode.playerSpecific.desc.1");
			fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 46, 0x404040);
			
			s = StatCollector.translateToLocal("lotr.gui.bannerEdit.protectionMode.playerSpecific.desc.2");
			fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 46 + fontRendererObj.FONT_HEIGHT, 0x404040);
		}
		else
		{
			modeButton.displayString = StatCollector.translateToLocal("lotr.gui.bannerEdit.protectionMode.faction");
			
			String s = StatCollector.translateToLocalFormatted("lotr.gui.bannerEdit.protectionMode.faction.desc.1", new Object[] {theBanner.getBannerFaction().factionName()});
			fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 46, 0x404040);
			
			s = StatCollector.translateToLocal("lotr.gui.bannerEdit.protectionMode.faction.desc.2");
			fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 46 + fontRendererObj.FONT_HEIGHT, 0x404040);
		}
		
		for (int l = 0; l < allowedPlayers.length; l++)
		{
			GuiTextField textBox = allowedPlayers[l];
			textBox.drawTextBox();
		}
		
		super.drawScreen(i, j, f);
	}
	
	@Override
    public void updateScreen()
    {
		super.updateScreen();
		
		for (int l = 0; l < allowedPlayers.length; l++)
		{
			GuiTextField textBox = allowedPlayers[l];
			textBox.updateCursorCounter();
			textBox.setVisible(theBanner.playerSpecificProtection);
			textBox.setEnabled(l != 0 && theBanner.playerSpecificProtection);
		}
    }
	
	@Override
    protected void keyTyped(char c, int i)
    {
		for (int l = 1; l < allowedPlayers.length; l++)
		{
			GuiTextField textBox = allowedPlayers[l];
			if (textBox.getVisible() && textBox.textboxKeyTyped(c, i))
			{
				return;
			}
		}

        super.keyTyped(c, i);
    }
	
	@Override
	protected void mouseClicked(int i, int j, int k)
    {
		super.mouseClicked(i, j, k);
		
		for (int l = 1; l < allowedPlayers.length; l++)
		{
			GuiTextField textBox = allowedPlayers[l];
			if (textBox.getVisible())
			{
				textBox.mouseClicked(i, j, k);
				
				String text = textBox.getText();
				if (!textBox.isFocused() && !StringUtils.isEmpty(text) && !invalidUsernames[l])
				{
					UUID uuid = UUIDFromUsername(text);
					if (uuid == null)
					{
						invalidUsernames[l] = true;
						textBox.setTextColor(0xFF0000);
						textBox.setText(StatCollector.translateToLocal("lotr.gui.bannerEdit.invalidUsername"));
					}
				}
				
				if (textBox.isFocused() && invalidUsernames[l])
				{
					invalidUsernames[l] = false;
					textBox.setTextColor(0xFFFFFF);
					textBox.setText("");
				}
			}
		}
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
			if (button == modeButton)
			{
				theBanner.playerSpecificProtection = !theBanner.playerSpecificProtection;
			}
		}
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		
		ByteBuf data = Unpooled.buffer();
		
		data.writeInt(theBanner.getEntityId());
		data.writeByte(theBanner.worldObj.provider.dimensionId);
		data.writeBoolean(theBanner.playerSpecificProtection);

		for (int l = 1; l < allowedPlayers.length; l++)
		{
			if (!invalidUsernames[l])
			{
				String text = allowedPlayers[l].getText();
				UUID uuid = UUIDFromUsername(text);
				if (uuid != null)
				{
					data.writeInt(l);
					data.writeLong(uuid.getMostSignificantBits());
					data.writeLong(uuid.getLeastSignificantBits());
				}
			}
		}
		data.writeInt(-1);
		
		Packet packet = new C17PacketCustomPayload("lotr.editBanner", data);
		mc.thePlayer.sendQueue.addToSendQueue(packet);
	}
	
	private UUID UUIDFromUsername(String name)
	{
		GameProfile profile = mc.getIntegratedServer().func_152358_ax().func_152655_a(name);
		return profile == null ? null : profile.getId();
	}
}
