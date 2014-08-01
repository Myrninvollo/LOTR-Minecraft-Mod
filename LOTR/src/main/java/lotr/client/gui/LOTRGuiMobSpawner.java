package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class LOTRGuiMobSpawner extends LOTRGuiScreenBase
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/mob_spawner.png");
	
	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;
	private LOTRTileEntityMobSpawner tileEntity;
    private int xSize = 256;
    private int ySize = 256;
    private int guiLeft;
    private int guiTop;
	private ArrayList pageControls = new ArrayList();
	private ArrayList spawnerControls = new ArrayList();
	private ArrayList mobControls = new ArrayList();
	
	private int page;
	private String[] pageNames = {"Spawner Properties", "Entity Properties"};
	
	private int active;
    private int minSpawnDelay;
    private int maxSpawnDelay;
    private int nearbyMobLimit;
	private int nearbyMobCheckRange;
    private int requiredPlayerRange;
    private int maxSpawnCount;
    private int maxSpawnRange;
	private int maxSpawnRangeVertical;
	
	private boolean spawnsPersistentNPCs;
	private int maxHealth;
	private int navigatorRange;
	private float moveSpeed;
	private int attackDamage;
	
	private boolean needsUpdate;
	private Class mobClass;
	private String mobName;
	
	public LOTRGuiMobSpawner(World world, int i, int j, int k)
	{
		worldObj = world;
		posX = i;
		posY = j;
		posZ = k;
		tileEntity = (LOTRTileEntityMobSpawner)worldObj.getTileEntity(posX, posY, posZ);
		syncTileEntityDataToGui();
	}
	
	private void syncTileEntityDataToGui()
	{
		active = tileEntity.active;
		spawnsPersistentNPCs = tileEntity.spawnsPersistentNPCs;
		minSpawnDelay = tileEntity.minSpawnDelay;
		maxSpawnDelay = tileEntity.maxSpawnDelay;
		nearbyMobLimit = tileEntity.nearbyMobLimit;
		nearbyMobCheckRange = tileEntity.nearbyMobCheckRange;
		requiredPlayerRange = tileEntity.requiredPlayerRange;
		maxSpawnCount = tileEntity.maxSpawnCount;
		maxSpawnRange = tileEntity.maxSpawnRange;
		maxSpawnRangeVertical = tileEntity.maxSpawnRangeVertical;
		maxHealth = (tileEntity.maxHealth + 256) & 255;
		navigatorRange = tileEntity.navigatorRange;
		moveSpeed = tileEntity.moveSpeed;
		attackDamage = tileEntity.attackDamage;
		mobClass = LOTREntities.getClassFromString(tileEntity.getMobID());
		mobName = tileEntity.getMobID();
	}
	
	private void sendGuiDataToClientTileEntity()
	{
		tileEntity.active = active;
		tileEntity.spawnsPersistentNPCs = spawnsPersistentNPCs;
		tileEntity.minSpawnDelay = minSpawnDelay;
		tileEntity.maxSpawnDelay = maxSpawnDelay;
		tileEntity.nearbyMobLimit = nearbyMobLimit;
		tileEntity.nearbyMobCheckRange = nearbyMobCheckRange;
		tileEntity.requiredPlayerRange = requiredPlayerRange;
		tileEntity.maxSpawnCount = maxSpawnCount;
		tileEntity.maxSpawnRange = maxSpawnRange;
		tileEntity.maxSpawnRangeVertical = maxSpawnRangeVertical;
		tileEntity.maxHealth = maxHealth;
		tileEntity.navigatorRange = navigatorRange;
		tileEntity.moveSpeed = moveSpeed;
		tileEntity.attackDamage = attackDamage;
	}
	
	private void sendGuiDataToServerTileEntity()
	{
		ByteBuf data = Unpooled.buffer();
		
		data.writeInt(posX);
		data.writeInt(posY);
		data.writeInt(posZ);
		data.writeByte((byte)mc.thePlayer.dimension);
		data.writeByte((byte)active);
		data.writeByte(spawnsPersistentNPCs ? (byte)1 : (byte)0);
		data.writeInt(minSpawnDelay);
		data.writeInt(maxSpawnDelay);
		data.writeByte((byte)nearbyMobLimit);
		data.writeByte((byte)nearbyMobCheckRange);
		data.writeByte((byte)requiredPlayerRange);
		data.writeByte((byte)maxSpawnCount);
		data.writeByte((byte)maxSpawnRange);
		data.writeByte((byte)maxSpawnRangeVertical);
		data.writeByte((byte)maxHealth);
		data.writeByte((byte)navigatorRange);
		data.writeFloat(moveSpeed);
		data.writeByte((byte)attackDamage);

		C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.mobSpawner", data);
		mc.thePlayer.sendQueue.addToSendQueue(packet);
		needsUpdate = false;
	}
	
	@Override
    public void initGui()
    {
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
		
		pageControls.add(new GuiButton(1, guiLeft + xSize / 2 + 2, guiTop + 52, 110, 20, pageNames[page]));
		
		spawnerControls.add(new GuiButton(0, guiLeft + xSize / 2 - 112, guiTop + 52, 110, 20, active == 2 ? "Redstone Activated" : (active == 1 ? "Active" : "Inactive")));
		spawnerControls.add(new LOTRGuiSlider(1, guiLeft + xSize / 2 - 112, guiTop + 76, 224, 20, "Min Spawn Delay: " + ticksToSeconds(minSpawnDelay), (float)(minSpawnDelay / 1200F)));
		spawnerControls.add(new LOTRGuiSlider(2, guiLeft + xSize / 2 - 112, guiTop + 100, 224, 20, "Max Spawn Delay: " + ticksToSeconds(maxSpawnDelay), (float)(maxSpawnDelay / 1200F)));
		spawnerControls.add(new LOTRGuiSlider(3, guiLeft + xSize / 2 - 112, guiTop + 124, 224, 20, "Nearby Mob Limit: " + nearbyMobLimit, (float)((nearbyMobLimit - 1) / 31F)));
		spawnerControls.add(new LOTRGuiSlider(4, guiLeft + xSize / 2 - 112, guiTop + 148, 224, 20, "Nearby Mob Check Range: " + nearbyMobCheckRange, (float)((nearbyMobCheckRange - 1) / 63F)));
		spawnerControls.add(new LOTRGuiSlider(5, guiLeft + xSize / 2 - 112, guiTop + 172, 224, 20, "Required Player Range: " + requiredPlayerRange, (float)((requiredPlayerRange - 1) / 99F)));
		spawnerControls.add(new LOTRGuiSlider(6, guiLeft + xSize / 2 - 112, guiTop + 196, 224, 20, "Max Spawn Count: " + maxSpawnCount, (float)((maxSpawnCount - 1) / 15F)));
		spawnerControls.add(new LOTRGuiSlider(7, guiLeft + xSize / 2 - 112, guiTop + 220, 110, 20, "Horizontal Range: " + maxSpawnRange, (float)((maxSpawnRange - 1) / 63F)));
		spawnerControls.add(new LOTRGuiSlider(8, guiLeft + xSize / 2 + 2, guiTop + 220, 110, 20, "Vertical Range: " + maxSpawnRangeVertical, (float)((maxSpawnRangeVertical - 1) / 15F)));
		
		mobControls.add(new GuiButton(0, guiLeft + xSize / 2 - 112, guiTop + 52, 110, 20, spawnsPersistentNPCs ? "Persistent NPCs" : "Normal NPCs"));
		((GuiButton)mobControls.get(0)).enabled = LOTREntityNPC.class.isAssignableFrom(mobClass);
		mobControls.add(new LOTRGuiSlider(1, guiLeft + xSize / 2 - 112, guiTop + 76, 224, 20, "Max Health: " + maxHealth, (float)((maxHealth - 1) / 199F)));
		mobControls.add(new LOTRGuiSlider(2, guiLeft + xSize / 2 - 112, guiTop + 100, 224, 20, "Navigator Range: " + navigatorRange, (float)((navigatorRange - 1) / 99F)));
		mobControls.add(new LOTRGuiSlider(3, guiLeft + xSize / 2 - 112, guiTop + 124, 224, 20, "Movement Speed: " + trim(moveSpeed), (moveSpeed - 0.01F) / 0.99F));
		mobControls.add(new LOTRGuiSlider(4, guiLeft + xSize / 2 - 112, guiTop + 148, 224, 20, "Base Attack Damage: " + attackDamage, (float)((attackDamage - 1) / 19F)));
	}
	
    public void setWorldAndResolution(Minecraft mc, int i, int j)
    {
		pageControls.clear();
		spawnerControls.clear();
		mobControls.clear();
        super.setWorldAndResolution(mc, i, j);
    }
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled && button.getClass() == GuiButton.class)
        {
			if (button.id == 0)
			{
				if (page == 0)
				{
					active++;
					if (active > 2)
					{
						active = 0;
					}
					
					switch (active)
					{
						case 0:
							button.displayString = "Inactive";
							break;
						case 1:
							button.displayString = "Active";
							break;
						case 2:
							button.displayString = "Redstone Activated";
							break;
					}
				}
				else if (page == 1)
				{
					spawnsPersistentNPCs = !spawnsPersistentNPCs;
					
					if (spawnsPersistentNPCs)
					{
						button.displayString = "Persistent NPCs";
					}
					else
					{
						button.displayString = "Normal NPCs";
					}
				}
				needsUpdate = true;
			}
			
			if (button.id == 1)
			{
				page = 1 - page;
				button.displayString = pageNames[page];
			}
		}
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		fontRendererObj.drawString("Mob Spawner", guiLeft + 127 - fontRendererObj.getStringWidth("Mob Spawner") / 2, guiTop + 11, 0x373737);
		fontRendererObj.drawString(mobName, guiLeft + 127 - fontRendererObj.getStringWidth(mobName) / 2, guiTop + 26, 0x373737);
		super.drawScreen(i, j, f);
		for (int k = 0; k < pageControls.size(); k++)
		{
			((GuiButton)pageControls.get(k)).drawButton(mc, i, j);
		}
		if (page == 0)
		{
			for (int k = 0; k < spawnerControls.size(); k++)
			{
				((GuiButton)spawnerControls.get(k)).drawButton(mc, i, j);
			}
		}
		else if (page == 1)
		{
			for (int k = 0; k < mobControls.size(); k++)
			{
				((GuiButton)mobControls.get(k)).drawButton(mc, i, j);
			}
		}
	}

	@Override
    public void updateScreen()
    {
        super.updateScreen();
		
		if (page == 0)
		{
			LOTRGuiSlider slider_minSpawnDelay = ((LOTRGuiSlider)spawnerControls.get(1));
			if (slider_minSpawnDelay.dragging)
			{
				minSpawnDelay = Math.round(slider_minSpawnDelay.sliderValue * 1200);
				slider_minSpawnDelay.displayString = "Min Spawn Delay: " + ticksToSeconds(minSpawnDelay);
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_maxSpawnDelay = ((LOTRGuiSlider)spawnerControls.get(2));
			if (slider_maxSpawnDelay.dragging)
			{
				maxSpawnDelay = Math.round(slider_maxSpawnDelay.sliderValue * 1200);
				slider_maxSpawnDelay.displayString = "Max Spawn Delay: " + ticksToSeconds(maxSpawnDelay);
				needsUpdate = true;
			}
			
			if (minSpawnDelay > maxSpawnDelay)
			{
				if (slider_minSpawnDelay.dragging)
				{
					slider_maxSpawnDelay.sliderValue = slider_minSpawnDelay.sliderValue;
					maxSpawnDelay = minSpawnDelay;
					slider_maxSpawnDelay.displayString = "Max Spawn Delay: " + ticksToSeconds(maxSpawnDelay);
					needsUpdate = true;
				}
				else if (slider_maxSpawnDelay.dragging)
				{
					slider_minSpawnDelay.sliderValue = slider_maxSpawnDelay.sliderValue;
					minSpawnDelay = maxSpawnDelay;
					slider_minSpawnDelay.displayString = "Min Spawn Delay: " + ticksToSeconds(minSpawnDelay);
					needsUpdate = true;
				}
			}
			
			LOTRGuiSlider slider_mobLimit = ((LOTRGuiSlider)spawnerControls.get(3));
			if (slider_mobLimit.dragging)
			{
				nearbyMobLimit = Math.round(slider_mobLimit.sliderValue * 31F) + 1;
				slider_mobLimit.displayString = "Nearby Mob Limit: " + nearbyMobLimit;
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_mobCheckRange = ((LOTRGuiSlider)spawnerControls.get(4));
			if (slider_mobCheckRange.dragging)
			{
				nearbyMobCheckRange = Math.round(slider_mobCheckRange.sliderValue * 63F) + 1;
				slider_mobCheckRange.displayString = "Nearby Mob Check Range: " + nearbyMobCheckRange;
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_playerRange = ((LOTRGuiSlider)spawnerControls.get(5));
			if (slider_playerRange.dragging)
			{
				requiredPlayerRange = Math.round(slider_playerRange.sliderValue * 99F) + 1;
				slider_playerRange.displayString = "Required Player Range: " + requiredPlayerRange;
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_maxSpawnCount = ((LOTRGuiSlider)spawnerControls.get(6));
			if (slider_maxSpawnCount.dragging)
			{
				maxSpawnCount = Math.round(slider_maxSpawnCount.sliderValue * 15F) + 1;
				slider_maxSpawnCount.displayString = "Max Spawn Count: " + maxSpawnCount;
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_maxSpawnRange = ((LOTRGuiSlider)spawnerControls.get(7));
			if (slider_maxSpawnRange.dragging)
			{
				maxSpawnRange = Math.round(slider_maxSpawnRange.sliderValue * 63F) + 1;
				slider_maxSpawnRange.displayString = "Horizontal Range: " + maxSpawnRange;
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_maxSpawnRangeVertical = ((LOTRGuiSlider)spawnerControls.get(8));
			if (slider_maxSpawnRangeVertical.dragging)
			{
				maxSpawnRangeVertical = Math.round(slider_maxSpawnRangeVertical.sliderValue * 15F) + 1;
				slider_maxSpawnRangeVertical.displayString = "Vertical Range: " + maxSpawnRangeVertical;
				needsUpdate = true;
			}
		}
		else if (page == 1)
		{
			LOTRGuiSlider slider_maxHealth = ((LOTRGuiSlider)mobControls.get(1));
			if (slider_maxHealth.dragging)
			{
				maxHealth = Math.round(slider_maxHealth.sliderValue * 199F) + 1;
				slider_maxHealth.displayString = "Max Health: " + maxHealth;
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_navigatorRange = ((LOTRGuiSlider)mobControls.get(2));
			if (slider_navigatorRange.dragging)
			{
				navigatorRange = Math.round(slider_navigatorRange.sliderValue * 99F) + 1;
				slider_navigatorRange.displayString = "Navigator Range: " + navigatorRange;
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_moveSpeed = ((LOTRGuiSlider)mobControls.get(3));
			if (slider_moveSpeed.dragging)
			{
				moveSpeed = (slider_moveSpeed.sliderValue * 0.99F) + 0.01F;
				slider_moveSpeed.displayString = "Movement Speed: " + trim(moveSpeed);
				needsUpdate = true;
			}
			
			LOTRGuiSlider slider_attackDamage = ((LOTRGuiSlider)mobControls.get(4));
			if (slider_attackDamage.dragging)
			{
				attackDamage = Math.round(slider_attackDamage.sliderValue * 19F) + 1;
				slider_attackDamage.displayString = "Base Attack Damage: " + attackDamage;
				needsUpdate = true;
			}
		}
		
		if (needsUpdate)
		{
			sendGuiDataToClientTileEntity();
			sendGuiDataToServerTileEntity();
		}
    }

	@Override	
    protected void mouseClicked(int i, int j, int k)
    {
		buttonList.addAll(pageControls);
		if (page == 0)
		{
			buttonList.addAll(spawnerControls);
		}
		else if (page == 1)
		{
			buttonList.addAll(mobControls);
		}
        super.mouseClicked(i, j, k);
		buttonList.clear();
    }
	
	private String trim(float f)
	{
		return String.format("%.2f", new Object[] {Double.valueOf(f)});
	}
	
	private String ticksToSeconds(int ticks)
	{
		return trim((float)ticks / 20F) + "s";
	}
}
