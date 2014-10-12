package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.UUID;

import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.npc.LOTRHiredNPCInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Charsets;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

public class LOTRGuiBanner extends LOTRGuiScreenBase
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/banner_edit.png");

	private LOTREntityBanner theBanner;
	public String[] usernamesReceived = new String[LOTREntityBanner.MAX_PLAYERS];
	
	public int xSize = 200;
    public int ySize = 200;
    private int guiLeft;
    private int guiTop;
    private boolean firstInit = true;
    
    private GuiButton modeButton;
    
    private LOTRGuiSlider alignmentSlider;
    
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
		buttonList.add((alignmentSlider = new LOTRGuiSlider(1, guiLeft + xSize / 2 - 80, guiTop + 80, 160, 20, StatCollector.translateToLocal("lotr.gui.bannerEdit.protectionMode.faction.alignment"), 0F)));
		alignmentSlider.setSliderValue(theBanner.getAlignmentProtection(), LOTREntityBanner.ALIGNMENT_PROTECTION_MIN, LOTREntityBanner.ALIGNMENT_PROTECTION_MAX);
		
		for (int i = 0; i < allowedPlayers.length; i++)
		{
			GuiTextField textBox = new GuiTextField(fontRendererObj, guiLeft + xSize / 2 - 80, guiTop + 70 + i * 24, 160, 20);
			
			if (firstInit)
			{
				if (usernamesReceived[i] != null)
				{
					textBox.setText(usernamesReceived[i]);
				}
			}
			else
			{
				GuiTextField prevTextBox = allowedPlayers[i];
				if (prevTextBox != null)
				{
					String prevText = prevTextBox.getText();
					if (prevText != null)
					{
						textBox.setText(prevText);
					}
				}
			}
			
			allowedPlayers[i] = textBox;
		}
		
		allowedPlayers[0].setEnabled(false);
		
		if (firstInit)
		{
			firstInit = false;
		}
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

			String s = StatCollector.translateToLocalFormatted("lotr.gui.bannerEdit.protectionMode.faction.desc.1", new Object[] {theBanner.getAlignmentProtection(), theBanner.getBannerFaction().factionName()});
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
		
		alignmentSlider.visible = !theBanner.playerSpecificProtection;
		if (alignmentSlider.dragging)
		{
			int alignment = alignmentSlider.getSliderValue(LOTREntityBanner.ALIGNMENT_PROTECTION_MIN, LOTREntityBanner.ALIGNMENT_PROTECTION_MAX);
			theBanner.setAlignmentProtection(alignment);
			alignmentSlider.setState(String.valueOf(alignment));
			
			ByteBuf data = Unpooled.buffer();

			data.writeInt(theBanner.getEntityId());
			data.writeByte(theBanner.worldObj.provider.dimensionId);
			data.writeInt(alignment);
			
			Packet packet = new C17PacketCustomPayload("lotr.editBannerAlignment", data);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
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
				if (!StringUtils.isEmpty(text))
				{
					data.writeInt(l);
					data.writeByte(text.length());
					data.writeBytes(text.getBytes(Charsets.UTF_8));
				}
			}
		}
		data.writeInt(-1);

		Packet packet = new C17PacketCustomPayload("lotr.editBanner", data);
		mc.thePlayer.sendQueue.addToSendQueue(packet);
	}

	private UUID UUIDFromUsername(String name)
	{
		GameProfile profile = GameProfileLookup.getProfileByName(name);
		return profile == null ? null : profile.getId();
	}
	
	private static class GameProfileLookup
	{
		private static Minecraft theMC = Minecraft.getMinecraft();
		private static YggdrasilAuthenticationService authService = new YggdrasilAuthenticationService(theMC.getProxy(), UUID.randomUUID().toString());
		private static GameProfileRepository profileRepo = authService.createProfileRepository();
		
		public static GameProfile getProfileByName(String name)
		{
			final GameProfile[] foundProfiles = new GameProfile[1];
			
	        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
	        {
	            public void onProfileLookupSucceeded(GameProfile profile)
	            {
	            	foundProfiles[0] = profile;
	            }
	            public void onProfileLookupFailed(GameProfile profile, Exception exception)
	            {
	            	foundProfiles[0] = null;
	            }
	        };
	        
	        profileRepo.findProfilesByNames(new String[] {name}, Agent.MINECRAFT, profilelookupcallback);

	        return foundProfiles[0];
		}
	}
}