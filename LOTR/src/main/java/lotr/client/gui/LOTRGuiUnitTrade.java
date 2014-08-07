package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.client.LOTRClientProxy;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRUnitTradeEntry;
import lotr.common.entity.npc.LOTRUnitTradeable;
import lotr.common.inventory.LOTRContainerUnitTrade;
import lotr.common.inventory.LOTRSlotAlignmentReward;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRGuiUnitTrade extends GuiContainer
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/npc/unit_trade.png");
	private LOTRUnitTradeable theUnitTrader;
	private LOTRFaction faction;
	private LOTRUnitTradeEntry[] trades;
	private int currentTradeEntryIndex;
	private EntityLiving currentDisplayedMob;
	private EntityLiving currentDisplayedMount;
    private float screenXSize;
    private float screenYSize;
	
	public LOTRGuiUnitTrade(EntityPlayer entityplayer, LOTRUnitTradeable trader, World world)
	{
		super(new LOTRContainerUnitTrade(entityplayer, trader, world));
		xSize = 220;
		ySize = 238;
		theUnitTrader = trader;
		faction = ((LOTREntityNPC)theUnitTrader).getFaction();
		trades = theUnitTrader.getUnits();
	}
	
	@Override
    public void initGui()
    {
        super.initGui();
		buttonList.add(new LOTRGuiUnitTradeButton(0, guiLeft + 90, guiTop + 125, 12, 19));
		((GuiButton)buttonList.get(0)).enabled = false;
		buttonList.add(new LOTRGuiUnitTradeButton(1, guiLeft + 102, guiTop + 125, 16, 19));
		buttonList.add(new LOTRGuiUnitTradeButton(2, guiLeft + 118, guiTop + 125, 12, 19));
    }
	
	@Override
    public void drawScreen(int i, int j, float f)
    {
		((GuiButton)buttonList.get(0)).enabled = currentTradeEntryIndex > 0;
		((GuiButton)buttonList.get(1)).enabled = trades[currentTradeEntryIndex].hasRequiredCostAndAlignment(mc.thePlayer, theUnitTrader);
		((GuiButton)buttonList.get(2)).enabled = currentTradeEntryIndex < trades.length - 1;
		
        super.drawScreen(i, j, f);
        screenXSize = (float)i;
        screenYSize = (float)j;
    }
	
	@Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
        drawCenteredString(theUnitTrader.getNPCName(), 110, 11, 0x373737);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 30, 144, 0x404040);

		drawCenteredString(trades[currentTradeEntryIndex].getUnitTradeName(), 138, 50, 0x373737);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), new ItemStack(LOTRMod.silverCoin), 68, 72);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		int cost = trades[currentTradeEntryIndex].getCost(mc.thePlayer, theUnitTrader);
		fontRendererObj.drawString(String.valueOf(cost), 87, 76, 0x373737);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(LOTRClientProxy.alignmentTexture);
		drawTexturedModalRect(68, 91, 0, 18, 16, 16);
		
		int alignment = trades[currentTradeEntryIndex].alignmentRequired;
		String s = String.valueOf(alignment);
		if (alignment > 0)
		{
			s = "+" + s;
		}
		fontRendererObj.drawString(s, 87, 95, 0x373737);
		
		if (((LOTRContainerUnitTrade)inventorySlots).alignmentRewardSlots > 0)
		{
			Slot slot = inventorySlots.getSlot(0);

			boolean hasRewardCost = slot.getHasStack() && LOTRLevelData.hasTakenAlignmentRewardItem(mc.thePlayer, faction);
			if (hasRewardCost)
			{
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_CULL_FACE);
				itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), new ItemStack(LOTRMod.silverCoin), 160, 100);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				
				cost = LOTRSlotAlignmentReward.REBUY_COST;
				fontRendererObj.drawString(String.valueOf(cost), 179, 104, 0x373737);
			}
			else if (!slot.getHasStack() && LOTRLevelData.getData(mc.thePlayer).getAlignment(faction) < LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED)
			{
				if (func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, i, j))
		        {
		            drawCreativeTabHoveringText(StatCollector.translateToLocalFormatted("container.lotr.unitTrade.requiresAlignment", new Object[] {LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED}), i - guiLeft, j- guiTop);
		        }
			}
		}
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
		GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
		if (((LOTRContainerUnitTrade)inventorySlots).alignmentRewardSlots > 0)
		{
			Slot slot = inventorySlots.getSlot(0);
	        drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 3, guiTop + slot.yDisplayPosition - 3, xSize, 16, 22, 22);
	        
			if (!slot.getHasStack() && LOTRLevelData.getData(mc.thePlayer).getAlignment(faction) < LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED)
			{
		        drawTexturedModalRect(guiLeft + slot.xDisplayPosition, guiTop + slot.yDisplayPosition, xSize, 0, 16, 16);
			}
		}
		
		drawMobOnGui(guiLeft + 32, guiTop + 109, (float)(guiLeft + 32) - screenXSize, (float)(guiTop + 109 - 50) - screenYSize);
    }
	
    private void drawMobOnGui(int i, int j, float f, float f1)
    {
		Class entityClass = trades[currentTradeEntryIndex].entityClass;
		Class mountClass = trades[currentTradeEntryIndex].mountClass;
		if (currentDisplayedMob == null || currentDisplayedMob.getClass() != entityClass || (mountClass == null && currentDisplayedMount != null) || (mountClass != null && (currentDisplayedMount == null || currentDisplayedMount.getClass() != mountClass)))
		{
			EntityLiving entity = (EntityLiving)LOTREntities.createEntityByClass(entityClass, mc.theWorld);
			if (entity instanceof LOTREntityNPC)
			{
				((LOTREntityNPC)entity).initCreatureForHire(null);
			}
			else
			{
				entity.onSpawnWithEgg(null);
			}
			currentDisplayedMob = entity;

			if (mountClass != null)
			{
				EntityLiving mount = (EntityLiving)LOTREntities.createEntityByClass(mountClass, mc.theWorld);
				if (mount instanceof LOTREntityNPC)
				{
					((LOTREntityNPC)mount).initCreatureForHire(null);
				}
				else
				{
					mount.onSpawnWithEgg(null);
				}
				currentDisplayedMount = mount;
				currentDisplayedMob.mountEntity(currentDisplayedMount);
			}
			else
			{
				currentDisplayedMount = null;
			}
		}
		
		float size = currentDisplayedMob.width * currentDisplayedMob.height * currentDisplayedMob.width;
		if (currentDisplayedMount != null)
		{
			size += (currentDisplayedMount.width * currentDisplayedMount.height * currentDisplayedMount.width * 0.5F);
		}
		float scale = MathHelper.sqrt_float(MathHelper.sqrt_float(1F / size)) * 30F;
		
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)i, (float)j, 50F);
        GL11.glScalef(-scale, scale, scale);
        GL11.glRotatef(180F, 0F, 0F, 1F);
        GL11.glRotatef(135F, 0F, 1F, 0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0F, 1F, 0F);
        GL11.glRotatef(-((float)Math.atan((double)(f1 / 40F))) * 20F, 1F, 0F, 0F);
        currentDisplayedMob.renderYawOffset = (float)Math.atan((double)(f / 40F)) * 20F;
        currentDisplayedMob.rotationYaw = (float)Math.atan((double)(f / 40F)) * 40F;
        currentDisplayedMob.rotationPitch = -((float)Math.atan((double)(f1 / 40F))) * 20F;
        currentDisplayedMob.rotationYawHead = currentDisplayedMob.rotationYaw;
        GL11.glTranslatef(0F, currentDisplayedMob.yOffset, 0F);
		if (currentDisplayedMount != null)
		{
			GL11.glTranslatef(0F, (float)currentDisplayedMount.getMountedYOffset(), 0F);
		}
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(currentDisplayedMob, 0D, 0D, 0D, 0F, 1F);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		
		if (currentDisplayedMount != null)
		{
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glPushMatrix();
			GL11.glTranslatef((float)i, (float)j, 50F);
			GL11.glScalef((float)(-scale), (float)scale, (float)scale);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glRotatef(135F, 0F, 1F, 0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glRotatef(-135F, 0F, 1F, 0F);
			GL11.glRotatef(-((float)Math.atan((double)(f1 / 40F))) * 20F, 1F, 0F, 0F);
			currentDisplayedMount.renderYawOffset = (float)Math.atan((double)(f / 40F)) * 20F;
			currentDisplayedMount.rotationYaw = (float)Math.atan((double)(f / 40F)) * 40F;
			currentDisplayedMount.rotationPitch = -((float)Math.atan((double)(f1 / 40F))) * 20F;
			currentDisplayedMount.rotationYawHead = currentDisplayedMount.rotationYaw;
			GL11.glTranslatef(0F, currentDisplayedMount.yOffset, 0F);
			RenderManager.instance.playerViewY = 180F;
			RenderManager.instance.renderEntityWithPosYaw(currentDisplayedMount, 0D, 0D, 0D, 0F, 1F);
			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		}
    }
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
			if (button.id == 0)
			{
				if (currentTradeEntryIndex > 0)
				{
					currentTradeEntryIndex--;
				}
			}
			
			else if (button.id == 1)
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeInt(mc.thePlayer.getEntityId());
				data.writeByte((byte)mc.thePlayer.dimension);
				data.writeByte((byte)currentTradeEntryIndex);
				
				C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.buyUnit", data);
				mc.thePlayer.sendQueue.addToSendQueue(packet);
			}
			
			else if (button.id == 2)
			{
				if (currentTradeEntryIndex < trades.length - 1)
				{
					currentTradeEntryIndex++;
				}
			}
		}
	}
	
    private void drawCenteredString(String s, int i, int j, int k)
    {
        fontRendererObj.drawString(s, i - fontRendererObj.getStringWidth(s) / 2, j, k);
    }
}
