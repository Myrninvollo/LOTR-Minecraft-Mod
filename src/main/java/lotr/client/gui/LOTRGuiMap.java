package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lotr.client.LOTRKeyHandler;
import lotr.common.LOTRAbstractWaypoint;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.genlayer.LOTRGenLayerWorld;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.EmptyChunk;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Charsets;

public class LOTRGuiMap extends LOTRGui
{
	private static ResourceLocation mapTexture = new ResourceLocation("lotr:map/map.png");
	private static ResourceLocation borderTexture = new ResourceLocation("lotr:map/mapScreen.png");
	
	public static Map playerLocations = new HashMap();
	private static Map playerSkins = new HashMap();
	
	private static final int MIN_ZOOM = -3;
	private static final int MAX_ZOOM = 2;
	private static final int mapXSize = 256;
	private static final int mapYSize = 200;
	
	private static final int addWPButtonX = mapXSize - 16;
	private static final int addWPButtonY = 6;
	private static final int addWPButtonWidth = 10;
	
	private static final int delWPButtonX = mapXSize - 16;
	private static final int delWPButtonY = 18;
	private static final int delWPButtonWidth = 10;
	
	private static int zoom = 0;
	
	public boolean isPlayerOp = false;
	
	private double zoomPower;
	private int posX;
	private int posY;
	private int isMouseButtonDown;
	private int prevMouseX;
	private int prevMouseY;
	private int mouseXCoord;
	private int mouseZCoord;
	private boolean isMouseWithinMap;
	private LOTRAbstractWaypoint selectedWaypoint;
	private boolean hasOverlay;
	private String overlayDisplayString[];
	private boolean canCreateWaypoint;
	private boolean creatingWaypoint;
	private GuiTextField nameWaypointTextField;
	private boolean deletingWaypoint;
	
	public LOTRGuiMap()
	{
		if (LOTRGenLayerWorld.biomeImageData == null)
		{
			new LOTRGenLayerWorld();
		}
	}
	
	@Override
    public void initGui()
    {
		xSize = 256;
		ySize = 256;
		super.initGui();
		posX = ((int)mc.thePlayer.posX / LOTRGenLayerWorld.scale) + LOTRGenLayerWorld.originX;
		posY = ((int)mc.thePlayer.posZ / LOTRGenLayerWorld.scale) + LOTRGenLayerWorld.originZ;
		nameWaypointTextField = new GuiTextField(fontRendererObj, guiLeft + mapXSize / 2 - 80, guiTop + 40, 160, 20);
	}
	
	@Override
    public void updateScreen()
    {
		super.updateScreen();
		
		if (creatingWaypoint)
		{
			nameWaypointTextField.updateCursorCounter();
		}
    }
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawCenteredString(StatCollector.translateToLocal("lotr.gui.map.title"), guiLeft + mapXSize / 2, guiTop - 30, 0xFFFFFF);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(mapTexture);
		
		zoomPower = Math.pow(2, zoom);
		int zoomScaleX = (int)Math.round(mapXSize / zoomPower);
		int zoomScaleY = (int)Math.round(mapYSize / zoomPower);
		
		int i1 = guiLeft + 4;
		int i2 = guiLeft + mapXSize - 4;
		int j1 = guiTop + 4;
		int j2 = guiTop + mapYSize - 4;
		isMouseWithinMap = i >= i1 && i < i2 && j >= j1 && j < j2;
		
        if (!hasOverlay && Mouse.isButtonDown(0))
        {
            if ((isMouseButtonDown == 0 || isMouseButtonDown == 1) && isMouseWithinMap)
            {
                if (isMouseButtonDown == 0)
                {
                    isMouseButtonDown = 1;
                }
                else
                {
					int x = (int)((double)(i - prevMouseX) / zoomPower);
					int y = (int)((double)(j - prevMouseY) / zoomPower);
                    posX -= x;
					posY -= y;
                    if (x != 0 || y != 0)
					{
						selectedWaypoint = null;
					}
                }

                prevMouseX = i;
                prevMouseY = j;
            }
        }
        else
        {
            isMouseButtonDown = 0;
        }
		
		int minX = posX - zoomScaleX / 2;
		int maxX = posX + zoomScaleX / 2;
		if (minX < 0)
		{
			posX = 0 + zoomScaleX / 2;
		}
		if (maxX >= LOTRGenLayerWorld.imageWidth)
		{
			posX = LOTRGenLayerWorld.imageWidth - zoomScaleX / 2;
		}
		
		int minY = posY - zoomScaleY / 2;
		int maxY = posY + zoomScaleY / 2;
		if (minY < 0)
		{
			posY = 0 + zoomScaleY / 2;
		}
		if (maxY >= LOTRGenLayerWorld.imageHeight)
		{
			posY = LOTRGenLayerWorld.imageHeight - zoomScaleY / 2;
		}
		
		double minU = (double)(posX - zoomScaleX / 2) / (double)LOTRGenLayerWorld.imageWidth;
		double maxU = (double)(posX + zoomScaleX / 2) / (double)LOTRGenLayerWorld.imageWidth;
		double minV = (double)(posY - zoomScaleY / 2) / (double)LOTRGenLayerWorld.imageHeight;
		double maxV = (double)(posY + zoomScaleY / 2) / (double)LOTRGenLayerWorld.imageHeight;
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(guiLeft, guiTop + mapYSize, zLevel, minU, maxV);
		tessellator.addVertexWithUV(guiLeft + mapXSize, guiTop + mapYSize, zLevel, maxU, maxV);
		tessellator.addVertexWithUV(guiLeft + mapXSize, guiTop, zLevel, maxU, minV);
		tessellator.addVertexWithUV(guiLeft, guiTop, zLevel, minU, minV);
		tessellator.draw();
		
		if (!hasOverlay && isMiddleEarth() && selectedWaypoint != null)
		{
			boolean hasUnlocked = selectedWaypoint.hasPlayerUnlocked(mc.thePlayer);
			int fastTravel = LOTRLevelData.getFastTravelTimer(mc.thePlayer);
			boolean canFastTravel = fastTravel <= 0 && hasUnlocked;
			
			int border = 3;
			int stringHeight = fontRendererObj.FONT_HEIGHT;
				
			int x = guiLeft;
			int y = guiTop + mapYSize + 10;
			int rectWidth = 256;
			int rectHeight = border * 2 + stringHeight;
			drawRect(x, y, x + rectWidth, y + rectHeight, 0xC0000000);
			
			if (!hasUnlocked)
			{
				String s = "If you can see this message, something has gone hideously wrong";
				
				if (!selectedWaypoint.isUnlockable(mc.thePlayer))
				{
					s = StatCollector.translateToLocal("lotr.gui.map.waypointUnavailableEnemy");
				}
				else
				{
					s = StatCollector.translateToLocal("lotr.gui.map.waypointUnavailableTravel");
				}
				
				drawCenteredString(s, guiLeft + mapXSize / 2, y + border, 0xFFFFFF);
			}
			else if (canFastTravel)
			{
				String s = StatCollector.translateToLocalFormatted("lotr.gui.map.fastTravel", new Object[] {GameSettings.getKeyDisplayString(LOTRKeyHandler.keyBindingFastTravel.getKeyCode())});
				drawCenteredString(s, guiLeft + mapXSize / 2, y + border, 0xFFFFFF);
			}
			else
			{
				String s = StatCollector.translateToLocalFormatted("lotr.gui.map.fastTravelTimeRemaining", new Object[] {LOTRLevelData.getHMSTime(fastTravel)});
				drawCenteredString(s, guiLeft + mapXSize / 2, y + border, 0xFFFFFF);
			}
		}
		else if (!hasOverlay && isMouseWithinMap)
		{
			int biomePosX = posX + (int)Math.round(((i - guiLeft) - mapXSize / 2) / zoomPower);
			int biomePosZ = posY + (int)Math.round(((j - guiTop) - mapYSize / 2) / zoomPower);
			
			int biomeID = LOTRGenLayerWorld.biomeImageData[biomePosZ * LOTRGenLayerWorld.imageWidth + biomePosX];
			BiomeGenBase biome = LOTRBiome.lotrBiomeList[biomeID];
			if (biome instanceof LOTRBiome)
			{
				String biomeName = ((LOTRBiome)biome).getBiomeDisplayName();
				int border = 3;
				int stringHeight = fontRendererObj.FONT_HEIGHT;
				
				int x = guiLeft;
				int y = guiTop + mapYSize + 10;
				int rectWidth = 256;
				int rectHeight = border * 3 + stringHeight * 2;
				if (canTeleport())
				{
					rectHeight += (stringHeight + border) * 2;
				}
				drawRect(x, y, x + rectWidth, y + rectHeight, 0xC0000000);
				drawCenteredString(biomeName, guiLeft + mapXSize / 2, y + border, 0xFFFFFF);
				
				mouseXCoord = (biomePosX - LOTRGenLayerWorld.originX) * LOTRGenLayerWorld.scale;
				mouseZCoord = (biomePosZ - LOTRGenLayerWorld.originZ) * LOTRGenLayerWorld.scale;
				String coords = "x: " + mouseXCoord + ", z: " + mouseZCoord;
				int stringX = guiLeft + mapXSize / 2;
				int stringY = y + border * 2 + stringHeight;
				drawCenteredString(coords, stringX, stringY, 0xFFFFFF);
				
				if (canTeleport())
				{
					String teleport = StatCollector.translateToLocalFormatted("lotr.gui.map.tp", new Object[] {GameSettings.getKeyDisplayString(LOTRKeyHandler.keyBindingMapTeleport.getKeyCode())});
					drawCenteredString(teleport, stringX, stringY + (stringHeight + border) * 2, 0xFFFFFF);
				}
			}
		}
		
		Iterator it = playerLocations.keySet().iterator();
		while (it.hasNext())
		{
			UUID player = (UUID)it.next();
			PlayerLocationInfo info = (PlayerLocationInfo)playerLocations.get(player);
			renderPlayer(player, info.name, info.posX, info.posZ, i, j);
		}
		
		if (isMiddleEarth())
		{
			renderPlayer(mc.thePlayer.getUniqueID(), mc.thePlayer.getCommandSenderName(), mc.thePlayer.posX, mc.thePlayer.posZ, i, j);
		}
		
		mc.getTextureManager().bindTexture(borderTexture);

		List waypoints = LOTRWaypoint.getListOfAllWaypoints(mc.thePlayer);
		for (int l = 0; l < waypoints.size(); l++)
		{
			LOTRAbstractWaypoint waypoint = (LOTRAbstractWaypoint)waypoints.get(l);
			int x = MathHelper.floor_double((waypoint.getX() - posX) * zoomPower) + mapXSize / 2;
			int y = MathHelper.floor_double((waypoint.getY() - posY) * zoomPower) + mapYSize / 2;
			if (x - 2 >= 0 && x + 2 <= mapXSize && y - 2 >= 0 && y + 2 <= mapYSize)
			{
				boolean flag = waypoint.hasPlayerUnlocked(mc.thePlayer);
				drawTexturedModalRect(guiLeft + x - 2, guiTop + y - 2, flag ? 4 : 0, 200, 4, 4);
			}
		}
		
		if (selectedWaypoint != null)
		{
			String name = selectedWaypoint.getDisplayName();
			String coords = "x: " + selectedWaypoint.getXCoord() + ", z: " + selectedWaypoint.getZCoord();
			
			int x = MathHelper.floor_double((selectedWaypoint.getX() - posX) * zoomPower) + mapXSize / 2;
			int y = MathHelper.floor_double((selectedWaypoint.getY() - posY) * zoomPower) + mapYSize / 2;
			y += 5;
			
			x += guiLeft;
			y += guiTop;
			int border = 3;
			int stringHeight = fontRendererObj.FONT_HEIGHT;
			int rectWidth = Math.max(fontRendererObj.getStringWidth(name), fontRendererObj.getStringWidth(coords)) + border * 2;
			x -= rectWidth / 2;
			int rectHeight = border * 3 + stringHeight * 2;
			
			x = Math.max(x, guiLeft + 6);
			x = Math.min(x, guiLeft + mapXSize - 6 - rectWidth);
			y = Math.max(y, guiTop + 6);
			y = Math.min(y, guiTop + mapYSize - 6 - rectHeight);
			
			drawRect(x, y, x + rectWidth, y + rectHeight, 0xC0000000);
			
			int stringX = x + rectWidth / 2;
			int stringY = y + border;
			drawCenteredString(name, stringX, stringY, 0xFFFFFF);
			
			drawCenteredString(coords, stringX, stringY + stringHeight + border, 0xFFFFFF);	
		}
		
        mc.getTextureManager().bindTexture(borderTexture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, mapXSize, mapYSize);
		
		if (isMiddleEarth())
		{
			drawTexturedModalRect(guiLeft + addWPButtonX, guiTop + addWPButtonY, 0, 204, addWPButtonWidth, addWPButtonWidth);
			
			if (selectedWaypoint instanceof LOTRWaypoint.Custom)
			{
				drawTexturedModalRect(guiLeft + delWPButtonX, guiTop + delWPButtonY, 0, 214, delWPButtonWidth, delWPButtonWidth);
			}
		}
		
		if (hasOverlay)
		{
			int x = guiLeft + 4;
			int y = guiTop + 4;
			int x1 = guiLeft + mapXSize - 4;
			int y1 = guiTop + mapYSize - 4;
			drawRect(x, y, x1, y1, 0xC0000000);
			
			if (overlayDisplayString != null)
			{
				int stringX = guiLeft + mapXSize / 2;
				int stringY = y + 3 + mc.fontRenderer.FONT_HEIGHT;
				for (String s : overlayDisplayString)
				{
					drawCenteredString(s, stringX, stringY, 0xFFFFFF);	
					stringY += mc.fontRenderer.FONT_HEIGHT;
				}
			}
			
			if (creatingWaypoint)
			{
		        GL11.glDisable(GL11.GL_LIGHTING);
		        nameWaypointTextField.drawTextBox();
				GL11.glEnable(GL11.GL_LIGHTING);
			}
		}
		
		super.drawScreen(i, j, f);
	}
	
	@Override
    protected void keyTyped(char c, int i)
    {
		if (hasOverlay)
		{
			if (canCreateWaypoint && i == Keyboard.KEY_RETURN)
			{
				canCreateWaypoint = false;
				creatingWaypoint = true;
				overlayDisplayString = new String[]
				{
					StatCollector.translateToLocal("lotr.gui.map.customWaypoint.create.1"),
					"",
					"",
					"",
					"",
					"",
					StatCollector.translateToLocalFormatted("lotr.gui.map.customWaypoint.create.2", new Object[] {GameSettings.getKeyDisplayString(Keyboard.KEY_RETURN)})
				};
				return;
			}
			else if (creatingWaypoint && !nameWaypointTextField.getText().isEmpty() && i == Keyboard.KEY_RETURN)
			{
				String waypointName = nameWaypointTextField.getText();
				
				ByteBuf data = Unpooled.buffer();
				
				data.writeInt(mc.thePlayer.getEntityId());
				data.writeByte((byte)mc.thePlayer.dimension);
				
				byte[] bytes = waypointName.getBytes(Charsets.UTF_8);
				data.writeShort(bytes.length);
				data.writeBytes(bytes);
				
				C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.createCWP", data);
				mc.thePlayer.sendQueue.addToSendQueue(packet);
				
				closeOverlay();
				return;
			}
			else if (creatingWaypoint && nameWaypointTextField.textboxKeyTyped(c, i))
			{
				return;
			}
			else if (deletingWaypoint && i == Keyboard.KEY_RETURN)
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeInt(mc.thePlayer.getEntityId());
				data.writeByte((byte)mc.thePlayer.dimension);
				
				data.writeInt(selectedWaypoint.getID());
				
				C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.deleteCWP", data);
				mc.thePlayer.sendQueue.addToSendQueue(packet);
				
				closeOverlay();
				selectedWaypoint = null;
				return;
			}
			else if (i == 1 || i == mc.gameSettings.keyBindInventory.getKeyCode())
			{
				closeOverlay();
				return;
			}
		}
		else
		{	
			if (i == LOTRKeyHandler.keyBindingFastTravel.getKeyCode() && isMiddleEarth() && selectedWaypoint != null && selectedWaypoint.hasPlayerUnlocked(mc.thePlayer) && LOTRLevelData.getFastTravelTimer(mc.thePlayer) <= 0)
			{
	        	ByteBuf data = Unpooled.buffer();
	        	
	        	data.writeInt(mc.thePlayer.getEntityId());
	        	data.writeByte((byte)mc.thePlayer.dimension);
	        	data.writeBoolean(selectedWaypoint instanceof LOTRWaypoint.Custom);
	        	data.writeInt(selectedWaypoint.getID());
	        	
	        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.fastTravel", data);
	        	mc.thePlayer.sendQueue.addToSendQueue(packet);
	
				mc.thePlayer.closeScreen();
			}
			else if (selectedWaypoint == null && i == LOTRKeyHandler.keyBindingMapTeleport.getKeyCode() && isMouseWithinMap && canTeleport())
			{
	        	ByteBuf data = Unpooled.buffer();
	        	
	        	data.writeInt(mc.thePlayer.getEntityId());
	        	data.writeByte((byte)mc.thePlayer.dimension);
	        	data.writeInt(mouseXCoord);
	        	data.writeInt(mouseZCoord);
	        	
	        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.mapTp", data);
	        	mc.thePlayer.sendQueue.addToSendQueue(packet);
	
				mc.thePlayer.closeScreen();
			}
	        else
			{
				super.keyTyped(c, i);
			}
		}
	}
	
	private void closeOverlay()
	{
		hasOverlay = false;
		overlayDisplayString = null;
		canCreateWaypoint = false;
		creatingWaypoint = false;
		deletingWaypoint = false;
	}
	
	@Override
    protected void mouseClicked(int i, int j, int k)
    {
		if (hasOverlay && creatingWaypoint)
		{
			nameWaypointTextField.mouseClicked(i, j, k);
		}
		
		if (!hasOverlay && k == 0 && isMiddleEarth() && selectedWaypoint instanceof LOTRWaypoint.Custom)
		{
			if (i >= guiLeft + delWPButtonX && i < guiLeft + delWPButtonX + delWPButtonWidth && j >= guiTop + delWPButtonY && j < guiTop + delWPButtonY + delWPButtonWidth)
			{
				hasOverlay = true;
				overlayDisplayString = new String[]
				{
					StatCollector.translateToLocal("lotr.gui.map.customWaypoint.delete.1"),
					StatCollector.translateToLocalFormatted("lotr.gui.map.customWaypoint.delete.2", selectedWaypoint.getDisplayName()),
					"",
					StatCollector.translateToLocalFormatted("lotr.gui.map.customWaypoint.delete.3", new Object[] {GameSettings.getKeyDisplayString(Keyboard.KEY_RETURN)})
				};
				deletingWaypoint = true;
				return;
			}
		}
		
		if (!hasOverlay && k == 0 && isMiddleEarth())
		{
			if (i >= guiLeft + addWPButtonX && i < guiLeft + addWPButtonX + addWPButtonWidth && j >= guiTop + addWPButtonY && j < guiTop + addWPButtonY + addWPButtonWidth)
			{
				hasOverlay = true;
				
				int waypoints = LOTRWaypoint.Custom.getWaypointList(mc.thePlayer).size();
				int maxWaypoints = LOTRWaypoint.Custom.MAX_CUSTOM;
				int remaining = maxWaypoints - waypoints;
				if (remaining <= 0)
				{
					overlayDisplayString = new String[]
					{
						StatCollector.translateToLocalFormatted("lotr.gui.map.customWaypoint.allUsed.1", new Object[] {maxWaypoints}),
						"",
						StatCollector.translateToLocal("lotr.gui.map.customWaypoint.allUsed.2"),
						StatCollector.translateToLocal("lotr.gui.map.customWaypoint.allUsed.3")
					};
				}
				else
				{
					overlayDisplayString = new String[]
					{
						StatCollector.translateToLocalFormatted("lotr.gui.map.customWaypoint.canCreate.1", new Object[] {waypoints, maxWaypoints}),
						StatCollector.translateToLocalFormatted("lotr.gui.map.customWaypoint.canCreate.2", new Object[] {remaining}),
						"",
						StatCollector.translateToLocalFormatted("lotr.gui.map.customWaypoint.canCreate.3", new Object[] {GameSettings.getKeyDisplayString(Keyboard.KEY_RETURN)}),
						StatCollector.translateToLocal("lotr.gui.map.customWaypoint.canCreate.4")
					};
					canCreateWaypoint = true;
				}
				
				return;
			}
		}
		
        if (!hasOverlay && k == 0 && isMouseWithinMap)
        {
    		List waypoints = LOTRWaypoint.getListOfAllWaypoints(mc.thePlayer);
    		for (int l = 0; l < waypoints.size(); l++)
    		{
    			LOTRAbstractWaypoint waypoint = (LOTRAbstractWaypoint)waypoints.get(l);
				int x = MathHelper.floor_double((waypoint.getX() - posX) * zoomPower) + mapXSize / 2;
				int y = MathHelper.floor_double((waypoint.getY() - posY) * zoomPower) + mapYSize / 2;
				if (Math.abs(x - (i - guiLeft)) <= 3 && Math.abs(y - (j - guiTop)) <= 3)
				{
					selectedWaypoint = waypoint;
					return;
				}
			}
			selectedWaypoint = null;
        }
		
		super.mouseClicked(i, j, k);
    }
	
	@Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        
        if (!hasOverlay)
        {
	        int i = Mouse.getEventDWheel();
	
			if (i < 0)
			{
				zoom = Math.max(zoom - 1, MIN_ZOOM);
				selectedWaypoint = null;
			}
	
			if (i > 0)
			{
				zoom = Math.min(zoom + 1, MAX_ZOOM);
				selectedWaypoint = null;
			}
        }
    }
	
    private void drawCenteredString(String s, int i, int j, int k)
    {
        fontRendererObj.drawString(s, i - fontRendererObj.getStringWidth(s) / 2, j, k);
    }
	
	private boolean canTeleport()
	{
		if (!isMiddleEarth())
		{
			return false;
		}
		
		int chunkX = MathHelper.floor_double(mc.thePlayer.posX) >> 4;
		int chunkZ = MathHelper.floor_double(mc.thePlayer.posZ) >> 4;
		if (mc.theWorld.getChunkProvider().provideChunk(chunkX, chunkZ) instanceof EmptyChunk)
		{
			return false;
		}
		
		requestIsOp();
		return isPlayerOp;
	}
	
    private void requestIsOp()
    {
		if (mc.isSingleplayer())
		{
			isPlayerOp = mc.getIntegratedServer().getConfigurationManager().isPlayerOpped(mc.thePlayer.getCommandSenderName());
		}
		else
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeInt(mc.thePlayer.getEntityId());
			data.writeByte((byte)mc.thePlayer.dimension);
			
			C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.isOpReq", data);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}
    
    private void renderPlayer(UUID player, String playerName, double playerPosX, double playerPosZ, int mouseX, int mouseY)
    {
    	Tessellator tessellator = Tessellator.instance;
    	
    	int playerX = (int)playerPosX;
		int playerZ = (int)playerPosZ;
		playerX = (playerX / LOTRGenLayerWorld.scale) + LOTRGenLayerWorld.originX;
		playerZ = (playerZ / LOTRGenLayerWorld.scale) + LOTRGenLayerWorld.originZ;
		playerX -= posX;
		playerZ -= posY;
		playerX *= zoomPower;
		playerZ *= zoomPower;
		playerX += guiLeft + mapXSize / 2;
		playerZ += guiTop + mapYSize / 2;
		
		int iconWidth = 4;
		int border = 4 + iconWidth + 1;
		
		playerX = Math.max(guiLeft + border, playerX);
		playerX = Math.min(guiLeft + mapXSize - border - 1, playerX);
		playerZ = Math.max(guiTop + border, playerZ);
		playerZ = Math.min(guiTop + mapYSize - border - 1, playerZ);

		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		ResourceLocation texture = (ResourceLocation)playerSkins.get(player);
		if (texture == null)
		{
			texture = AbstractClientPlayer.getLocationSkin(playerName);
			AbstractClientPlayer.getDownloadImageSkin(texture, playerName);
			playerSkins.put(player, texture);
		}

		mc.getTextureManager().bindTexture(texture);
		
		double iconMinU = 8D / 64D;
		double iconMaxU = 16D / 64D;
		double iconMinV = 8D / 32D;
		double iconMaxV = 16D / 32D;
		
		double playerX_d = playerX + 0.5D;
		double playerZ_d = playerZ + 0.5D;
		
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(playerX_d - iconWidth, playerZ_d + iconWidth, zLevel, iconMinU, iconMaxV);
		tessellator.addVertexWithUV(playerX_d + iconWidth, playerZ_d + iconWidth, zLevel, iconMaxU, iconMaxV);
		tessellator.addVertexWithUV(playerX_d + iconWidth, playerZ_d - iconWidth, zLevel, iconMaxU, iconMinV);
		tessellator.addVertexWithUV(playerX_d - iconWidth, playerZ_d - iconWidth, zLevel, iconMinU, iconMinV);
		tessellator.draw();
		
		iconMinU = 40D / 64D;
		iconMaxU = 48D / 64D;
		iconMinV = 8D / 32D;
		iconMaxV = 16D / 32D;
		
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(playerX_d - iconWidth - 0.5D, playerZ_d + iconWidth + 0.5D, zLevel, iconMinU, iconMaxV);
		tessellator.addVertexWithUV(playerX_d + iconWidth + 0.5D, playerZ_d + iconWidth + 0.5D, zLevel, iconMaxU, iconMaxV);
		tessellator.addVertexWithUV(playerX_d + iconWidth + 0.5D, playerZ_d - iconWidth - 0.5D, zLevel, iconMaxU, iconMinV);
		tessellator.addVertexWithUV(playerX_d - iconWidth - 0.5D, playerZ_d - iconWidth - 0.5D, zLevel, iconMinU, iconMinV);
		tessellator.draw();
		
		if (!hasOverlay && mouseX >= playerX - 4 && mouseX < playerX + 4 && mouseY >= playerZ - 4 && mouseY < playerZ + 4)
		{
			int stringWidth = mc.fontRenderer.getStringWidth(playerName);
			int stringHeight = mc.fontRenderer.FONT_HEIGHT;
			int stringBorder = 3;
			
			int x = guiLeft + mapXSize / 2 - stringWidth / 2 - stringBorder;
			int y = guiTop + mapYSize - 4 - stringHeight - stringBorder * 3;
			
			drawRect(x, y, x + stringWidth + stringBorder * 2, y + stringHeight + stringBorder * 2, 0xC0000000);
			mc.fontRenderer.drawString(playerName, x + stringBorder, y + stringBorder, 0xFFFFFF);
		}
    }
    
    private boolean isMiddleEarth()
    {
    	return mc.thePlayer.dimension == LOTRMod.idDimension;
    }
    
    public static void addPlayerLocationInfo(UUID player, String name, double x, double z)
    {
    	playerLocations.put(player, new PlayerLocationInfo(name, x, z));
    }
    
    private static class PlayerLocationInfo
    {
    	public String name;
    	public double posX;
    	public double posZ;
    	
    	public PlayerLocationInfo(String s, double x, double z)
    	{
    		name = s;
    		posX = x;
    		posZ = z;
    	}
    }
}
