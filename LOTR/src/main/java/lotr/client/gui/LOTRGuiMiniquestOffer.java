package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import lotr.client.model.LOTRModelLeatherHat;
import lotr.client.render.entity.LOTRRenderBiped;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRSpeech;
import lotr.common.quest.LOTRMiniQuest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRGuiMiniquestOffer extends LOTRGuiScreenBase
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/quest/miniquest.png");

	private LOTRMiniQuest theMiniQuest;
	private LOTREntityNPC theNPC;
	private String description;
	
	public int xSize = 256;
    public int ySize = 200;
    private int guiLeft;
    private int guiTop;
    private int descriptionX = 85;
    private int descriptionY = 45;
    private int descriptionWidth = 160;
    private int npcX = 46;
    private int npcY = 90;
    
    private GuiButton buttonAccept;
    private GuiButton buttonDecline;
    private boolean sentClosePacket = false;
	
	public LOTRGuiMiniquestOffer(LOTRMiniQuest quest, LOTREntityNPC npc)
	{
		theMiniQuest = quest;
		theNPC = npc;
	}
	
	@Override
    public void initGui()
    {
        guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		buttonList.add((buttonAccept = new GuiButton(0, guiLeft + xSize / 2 - 20 - 80, guiTop + ySize - 30, 80, 20, StatCollector.translateToLocal("lotr.gui.miniquestOffer.accept"))));
		buttonList.add((buttonDecline = new GuiButton(1, guiLeft + xSize / 2 + 20, guiTop + ySize - 30, 80, 20, StatCollector.translateToLocal("lotr.gui.miniquestOffer.decline"))));
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		if (description == null)
		{
			description = LOTRSpeech.getSpeechUnnamed(theMiniQuest.speechBankStart, mc.thePlayer, null, theMiniQuest.getObjectiveInSpeech());
		}
		
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(guiTexture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		String name = theNPC.getNPCName();
		drawCenteredString(name, guiLeft + xSize / 2, guiTop + 10, 0x7A5D43);
		
		fontRendererObj.drawSplitString(description, guiLeft + descriptionX, guiTop + descriptionY, descriptionWidth, 0x7A5D43);
		
		renderNPC(guiLeft + npcX, guiTop + npcY, guiLeft + npcX - i, guiTop + npcY - j);
		
		super.drawScreen(i, j, f);
	}
	
    private void renderNPC(int i, int j, float f, float f1)
    {
    	GL11.glColor4f(1F, 1F, 1F, 1F);
    	
		float scale = 70F;
		
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)i, (float)j, 40F);
        GL11.glScalef(-scale, -scale, -scale);
        GL11.glRotatef(180F, 0F, 0F, 1F);
        GL11.glRotatef(135F, 0F, 1F, 0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0F, 1F, 0F);
        GL11.glRotatef(((float)Math.atan((double)(f / 40F))) * 20F, 0F, 1F, 0F);
        GL11.glRotatef(-((float)Math.atan((double)(f1 / 40F))) * 20F, 1F, 0F, 0F);
        
        GL11.glTranslatef(0F, theNPC.yOffset, 0F);
        RenderManager.instance.playerViewY = 180F;
        
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        
        Render render = RenderManager.instance.getEntityRenderObject(theNPC);
        if (render instanceof LOTRRenderBiped)
        {
        	LOTRRenderBiped npcRenderer = (LOTRRenderBiped)render;
        	ModelBiped model = npcRenderer.modelBipedMain;
        	model.isChild = theNPC.isChild();
        	mc.getTextureManager().bindTexture(npcRenderer.getEntityTexture(theNPC));
        	
        	model.bipedHead.rotateAngleX = model.bipedHead.rotateAngleY = model.bipedHead.rotateAngleZ = 0F;
        	model.bipedHeadwear.rotateAngleX = model.bipedHeadwear.rotateAngleY = model.bipedHeadwear.rotateAngleZ = 0F;
        	model.bipedHead.render(0.0625F);
        	model.bipedHeadwear.render(0.0625F);
        	
        	for (int pass = 0; pass < 4; pass++)
        	{
        		int shouldRenderPass = npcRenderer.shouldRenderPass(theNPC, pass, 1F);
        		if (shouldRenderPass > 0)
        		{
        			model = npcRenderer.npcRenderPassModel;
        			model.isChild = theNPC.isChild();
        			npcRenderer.func_82408_c(theNPC, pass, 1F);
        			
                	model.bipedHead.rotateAngleX = model.bipedHead.rotateAngleY = model.bipedHead.rotateAngleZ = 0F;
                	model.bipedHeadwear.rotateAngleX = model.bipedHeadwear.rotateAngleY = model.bipedHeadwear.rotateAngleZ = 0F;
                	
                	if (model instanceof LOTRModelLeatherHat)
                	{
                		model.render(theNPC, 0F, 0F, 0F, 0F, 0F, 0.0625F);
                	}
                	else
                	{
	        			model.bipedHead.render(0.0625F);
	        			model.bipedHeadwear.render(0.0625F);
                	}
        		}
        	}
        }
        
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
        	boolean close = false;
        	
        	if (button == buttonAccept)
        	{
        		sendClosePacket(theMiniQuest, theNPC, true);
        		close = true;
        	}
        	else if (button == buttonDecline)
        	{
        		sendClosePacket(theMiniQuest, theNPC, false);
        		close = true;
        	}
        	
        	if (close)
        	{
        		sentClosePacket = true;
        		mc.thePlayer.closeScreen();
        	}
		}
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
		if (!theNPC.isEntityAlive() || mc.thePlayer.getDistanceToEntity(theNPC) > 8F)
		{
			mc.thePlayer.closeScreen();
		}
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		
		if (!sentClosePacket)
		{
			sendClosePacket(theMiniQuest, theNPC, false);
			sentClosePacket = true;
		}
	}
	
	public static void sendClosePacket(LOTRMiniQuest quest, LOTREntityNPC npc, boolean accept)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		
		ByteBuf data = Unpooled.buffer();
		
		data.writeInt(minecraft.thePlayer.getEntityId());
		data.writeByte(minecraft.thePlayer.dimension);
		data.writeBoolean(accept);
		
		data.writeInt(npc.getEntityId());

		try
		{
			NBTTagCompound nbt = new NBTTagCompound();
			quest.writeToNBT(nbt);
			new PacketBuffer(data).writeNBTTagCompoundToBuffer(nbt);
		}
		catch (IOException e)
		{
			System.out.println("Failed to write miniquest data to accept packet");
			e.printStackTrace();
		}
		
		C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.mqAccept", data);
		minecraft.thePlayer.sendQueue.addToSendQueue(packet);
	}
}
