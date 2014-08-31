package lotr.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import lotr.client.fx.LOTREntityDeadMarshFace;
import lotr.client.gui.*;
import lotr.client.render.tileentity.LOTRTileEntityMobSpawnerRenderer;
import lotr.common.*;
import lotr.common.block.LOTRBlockLeavesBase;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.item.LOTREntityPortal;
import lotr.common.entity.npc.LOTREntityNPCRideable;
import lotr.common.item.*;
import lotr.common.world.biome.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class LOTRTickHandlerClient
{
	private static ResourceLocation portalOverlay = new ResourceLocation("lotr:misc/portal_overlay.png");
	private static ResourceLocation elvenPortalOverlay = new ResourceLocation("lotr:misc/elvenportal_overlay.png");
	private static ResourceLocation morgulPortalOverlay = new ResourceLocation("lotr:misc/morgulportal_overlay.png");
	private static ResourceLocation mistOverlay = new ResourceLocation("lotr:misc/mist_overlay.png");
	private static ResourceLocation frostOverlay = new ResourceLocation("lotr:misc/frost_overlay.png");
	private static ResourceLocation burnOverlay = new ResourceLocation("lotr:misc/burn_overlay.png");
	
	public static HashMap playersInPortals = new HashMap();
	public static HashMap playersInElvenPortals = new HashMap();
	public static HashMap playersInMorgulPortals = new HashMap();
	
	private GuiScreen lastGuiOpen;
	
	private boolean checkedUpdate = false;
	
	private int mistTick;
	private static final int mistTickMax = 80;
	
	public static LOTRFaction currentAlignmentFaction = LOTRFaction.HOBBIT;
	private int alignmentXPosBase;
	private int alignmentYPosBase;
	private int alignmentXPosCurrent;
	private int alignmentYPosCurrent;
	private boolean firstAlignmentRender = true;
	public static int alignmentChange;
	public static LOTRGuiAchievementDisplay achievementDisplay;
	public static boolean renderMenuPrompt = false;
	public static boolean renderAlignmentPrompt = false;
	private int onscreenPromptTick;
	
	private int frostTick;
	private static final int frostTickMax = 80;
	
	private int burnTick;
	private static final int burnTickMax = 40;
	
	private int drunkennessDirection = 1;
	
	private int newDate = 0;
	private static final int newDateMax = 200;
	
	public LOTRTickHandlerClient()
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		achievementDisplay = new LOTRGuiAchievementDisplay();
	}

	@SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityClientPlayerMP entityplayer = minecraft.thePlayer;
		World world = minecraft.theWorld;

		if (event.phase == Phase.END)
		{
			if (alignmentChange > 0)
			{
				alignmentChange--;
			}
			
			if (minecraft.currentScreen == null)
			{
				lastGuiOpen = null;
			}
			
			if (FMLClientHandler.instance().hasOptifine())
			{
				int optifineSetting = 0;
				try
				{
					Object field = GameSettings.class.getField("ofTrees").get(minecraft.gameSettings);
					if (field instanceof Integer)
					{
						optifineSetting = (Integer)field;
					}
				}
				catch (Exception e) {}
	
				boolean fancyGraphics = (optifineSetting == 0 ? minecraft.gameSettings.fancyGraphics : (optifineSetting == 1 ? false : (optifineSetting == 2 ? true : false)));
				LOTRBlockLeavesBase.setAllGraphicsLevels(fancyGraphics);
			}
			else
			{
				LOTRBlockLeavesBase.setAllGraphicsLevels(minecraft.gameSettings.fancyGraphics);
			}
			
			if (entityplayer != null && world != null)
			{
				if (!checkedUpdate)
				{
					try
					{
						/*URL updateURL = new URL("http://dl.dropbox.com/s/sidxw1dicl2nsev/version.txt?dl=1");
						File updateFile = new File(DimensionManager.getCurrentSaveRootDirectory(), "lotrupdate.txt");
						FileUtils.copyURLToFile(updateURL, updateFile);
						BufferedReader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(updateFile)), Charsets.UTF_8.name()));
						String updateVersion = reader.readLine();
						reader.close();
						
						String version = null;
						for (ModContainer mod : Loader.instance().getModList())
						{
							if (mod.getMod() == LOTRMod.instance)
							{
								version = mod.getVersion();
							}
						}

						if (version != null && !updateVersion.equals(version))
						{
							IChatComponent component = new ChatComponentText("The Lord of the Rings Mod:");
							component.getChatStyle().setColor(EnumChatFormatting.YELLOW);
							entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.update", new Object[] {component, updateVersion}));
						}*/
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					checkedUpdate = true;
				}
			
				if ((entityplayer.dimension == 0 || entityplayer.dimension == LOTRMod.idDimension) && playersInPortals.containsKey(entityplayer))
				{
					List portals = world.getEntitiesWithinAABB(LOTREntityPortal.class, entityplayer.boundingBox.expand(8D, 8D, 8D));
					boolean inPortal = false;
					for (int i = 0; i < portals.size(); i++)
					{
						LOTREntityPortal portal = (LOTREntityPortal)portals.get(i);
						if (portal.boundingBox.intersectsWith(entityplayer.boundingBox))
						{
							inPortal = true;
							break;
						}
					}
					if (inPortal)
					{
						int i = (Integer)playersInPortals.get(entityplayer);
						i++;
						playersInPortals.put(entityplayer, Integer.valueOf(i));
						if (i >= 100)
						{
							minecraft.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("portal.trigger"), world.rand.nextFloat() * 0.4F + 0.8F));
							playersInPortals.remove(entityplayer);
						}
					}
					else
					{
						playersInPortals.remove(entityplayer);
					}
				}
				
				updatePlayerInPortal(entityplayer, playersInElvenPortals, LOTRMod.elvenPortal);
				updatePlayerInPortal(entityplayer, playersInMorgulPortals, LOTRMod.morgulPortal);
				
				if (!isGamePaused(minecraft))
				{
					int i = MathHelper.floor_double(entityplayer.posX);
					int j = MathHelper.floor_double(entityplayer.posY);
					int k = MathHelper.floor_double(entityplayer.posZ);
					
					if (LOTRMod.enableMistyMountainsMist)
					{
						BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
						if (entityplayer.posY >= 72 && biome instanceof LOTRBiomeGenMistyMountains && biome != LOTRBiome.mistyMountainsFoothills && world.canBlockSeeTheSky(i, j, k) && world.getSavedLightValue(EnumSkyBlock.Block, i, j, k) < 7)
						{
							if (mistTick < mistTickMax)
							{
								mistTick++;
							}
						}
						else
						{
							if (mistTick > 0)
							{
								mistTick--;
							}
						}
					}
					
					if (frostTick > 0)
					{
						frostTick--;
					}	
					
					if (burnTick > 0)
					{
						burnTick--;
					}
					
					if (minecraft.gameSettings.particleSetting < 2)
					{
						spawnEnvironmentFX(entityplayer, world);
					}
					
					LOTRClientProxy.customEffectRenderer.updateEffects();
					
					if (minecraft.renderViewEntity.isPotionActive(Potion.confusion.id))
					{
						float drunkenness = (float)minecraft.renderViewEntity.getActivePotionEffect(Potion.confusion).getDuration();
						drunkenness /= 20F;
						if (drunkenness > 100F)
						{
							drunkenness = 100F;
						}
						
						minecraft.renderViewEntity.rotationYaw += (float)drunkennessDirection * drunkenness / 20F;
						minecraft.renderViewEntity.rotationPitch += MathHelper.cos((float)minecraft.renderViewEntity.ticksExisted / 10F) * drunkenness / 20F;
						
						if (world.rand.nextInt(100) == 0)
						{
							drunkennessDirection *= -1;
						}
					}
					
					if (newDate > 0)
					{
						newDate--;
					}
				}
			}
			
			GuiScreen guiscreen = minecraft.currentScreen;
			if (guiscreen != null)
			{
				if (guiscreen instanceof GuiMainMenu && !(lastGuiOpen instanceof GuiMainMenu))
				{
					LOTRLevelData.needsLoad = true;
				}
				
				lastGuiOpen = guiscreen;
			}
			
			LOTRGuiShields.playerModelRotation += 2;
			if (LOTRGuiShields.playerModelRotation >= 360)
			{
				LOTRGuiShields.playerModelRotation = 0;
			}
		}
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		
		if (event.phase == Phase.START)
		{
			EntityClientPlayerMP entityplayer = minecraft.thePlayer;
			World world = minecraft.theWorld;
			
			if (entityplayer != null && world != null && entityplayer.ridingEntity instanceof LOTREntityNPCRideable)
			{
				LOTREntityNPCRideable npc = (LOTREntityNPCRideable)entityplayer.ridingEntity;
				if (npc.getMountInventory() != null)
				{
					GuiScreen gui = minecraft.currentScreen;
					if (gui instanceof GuiInventory || gui instanceof GuiContainerCreative)
					{
						entityplayer.closeScreen();
						
						ByteBuf data = Unpooled.buffer();
			        	
			        	data.writeInt(entityplayer.getEntityId());
			        	data.writeByte((byte)entityplayer.dimension);
			        	
			        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.mountInv", data);
			        	minecraft.thePlayer.sendQueue.addToSendQueue(packet);
					}
				}
			}
		}
		
		if (event.phase == Phase.END)
		{	
			LOTRTileEntityMobSpawnerRenderer.onRenderTick();
			
			if (minecraft.thePlayer != null && minecraft.theWorld != null)
			{
				if (minecraft.thePlayer.dimension == LOTRMod.idDimension || LOTRMod.alwaysShowAlignment)
				{
					alignmentXPosCurrent = alignmentXPosBase;
					int interval = (int)(((float)alignmentYPosBase + 20F) / 10F);
					if (minecraft.currentScreen == null && !minecraft.gameSettings.keyBindPlayerList.getIsKeyPressed() && !minecraft.gameSettings.showDebugInfo)
					{
						if (alignmentYPosCurrent < alignmentYPosBase)
						{
							alignmentYPosCurrent += interval;
						}
						else
						{
							alignmentYPosCurrent = alignmentYPosBase;
						}
					}
					else
					{
						if (alignmentYPosCurrent > -20)
						{
							alignmentYPosCurrent -= interval;
						}
						else
						{
							alignmentYPosCurrent = -20;
						}
					}
					renderAlignment(minecraft);
				}
				
				if (minecraft.thePlayer.dimension == LOTRMod.idDimension && minecraft.currentScreen == null)
				{
					onscreenPromptTick++;
					if (onscreenPromptTick >= 150)
					{
						onscreenPromptTick = 0;
					}
					
					float transparency = 1F;
					if (onscreenPromptTick < 50)
					{
						transparency = 0.5F + 0.5F * ((float)onscreenPromptTick / 50F);
					}
					else if (onscreenPromptTick >= 100)
					{
						transparency = 0.5F + 0.5F * ((float)(149 - onscreenPromptTick) / 50F);
					}
					
					String message = null;
					if (renderMenuPrompt)
					{
						message = StatCollector.translateToLocalFormatted("lotr.gui.menuPrompt", new Object[] {GameSettings.getKeyDisplayString(LOTRKeyHandler.keyBindingMenu.getKeyCode())});
					}
					else if (renderAlignmentPrompt)
					{
						message = StatCollector.translateToLocalFormatted("lotr.gui.alignmentPrompt", new Object[] {GameSettings.getKeyDisplayString(LOTRKeyHandler.keyBindingAlignmentCycleLeft.getKeyCode()), GameSettings.getKeyDisplayString(LOTRKeyHandler.keyBindingAlignmentCycleRight.getKeyCode())});
					}
					
					if (message != null)
					{
						ScaledResolution resolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
						int i = resolution.getScaledWidth();
						int j = resolution.getScaledHeight();
						int x = (i - minecraft.fontRenderer.getStringWidth(message)) / 2;
						int y = (j - minecraft.fontRenderer.FONT_HEIGHT) * 2 / 3;
						
						GL11.glEnable(GL11.GL_BLEND);
						OpenGlHelper.glBlendFunc(770, 771, 1, 0);
						minecraft.fontRenderer.drawString(message, x, y, 0xFFFFFF + ((int)(transparency * 255F) << 24));
						GL11.glDisable(GL11.GL_BLEND);
					}
					
					if (newDate > 0)
					{
						int halfMaxDate = newDateMax / 2;
						float alpha = 0F;
						if (newDate > halfMaxDate)
						{
							alpha = (float)(newDateMax - newDate) / (float)halfMaxDate;
						}
						else
						{
							alpha = (float)(newDate) / (float)halfMaxDate;
						}
						
						if (alpha > 0.1F)
						{			
							String date = LOTRTime.ShireReckoning.getShireDate().getDateName(true);
							
							ScaledResolution resolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
							int i = resolution.getScaledWidth();
							int j = resolution.getScaledHeight();

							float scale = 1.5F;
							float invScale = 1F / scale;
							
							i *= invScale;
							j *= invScale;
							
							int x = (i - minecraft.fontRenderer.getStringWidth(date)) / 2;
							int y = (j - minecraft.fontRenderer.FONT_HEIGHT) * 2 / 5;
							
							GL11.glScalef(scale, scale, scale);
		                    GL11.glEnable(GL11.GL_BLEND);
		                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
							minecraft.fontRenderer.drawString(date, x, y, 0xFFFFFF + ((int)(alpha * 255F) << 24));
							GL11.glDisable(GL11.GL_BLEND);
							GL11.glScalef(invScale, invScale, invScale);
						}
					}
				}
			}
			
			achievementDisplay.updateAchievementWindow();
		}
	}

	private void updatePlayerInPortal(EntityPlayer entityplayer, HashMap players, Block portalBlock)
	{
		if ((entityplayer.dimension == 0 || entityplayer.dimension == LOTRMod.idDimension) && players.containsKey(entityplayer))
		{
			boolean inPortal = entityplayer.worldObj.getBlock(MathHelper.floor_double(entityplayer.posX), MathHelper.floor_double(entityplayer.boundingBox.minY), MathHelper.floor_double(entityplayer.posZ)) == portalBlock;
			if (inPortal)
			{
				int i = (Integer)players.get(entityplayer);
				i++;
				players.put(entityplayer, Integer.valueOf(i));
				if (i >= entityplayer.getMaxInPortalTime())
				{
					Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("portal.trigger"), entityplayer.worldObj.rand.nextFloat() * 0.4F + 0.8F));
					players.remove(entityplayer);
				}
			}
			else
			{
				players.remove(entityplayer);
			}
		}
	}
	
	private void spawnEnvironmentFX(EntityPlayer entityplayer, World world)
	{
		world.theProfiler.startSection("lotrEnvironmentFX");
		int i = MathHelper.floor_double(entityplayer.posX);
		int j = MathHelper.floor_double(entityplayer.boundingBox.minY);
		int k = MathHelper.floor_double(entityplayer.posZ);
		byte range = 16;
		
        for (int l = 0; l < 50; l++)
        {
            int i1 = i + world.rand.nextInt(range) - world.rand.nextInt(range);
            int j1 = j + world.rand.nextInt(range) - world.rand.nextInt(range);
            int k1 = k + world.rand.nextInt(range) - world.rand.nextInt(range);
            if (world.getBlock(i1, j1, k1).getMaterial() == Material.water)
			{
				BiomeGenBase biome = world.getBiomeGenForCoords(i1, k1);
				if (biome instanceof LOTRBiomeGenMirkwood && ((LOTRBiomeGenMirkwood)biome).corrupted)
				{
					LOTRMod.proxy.spawnParticle("mirkwoodWater", (double)i1 + (double)world.rand.nextFloat(), (double)j1 + 0.75D, (double)k1 + (double)world.rand.nextFloat(), 0D, 0.05D, 0D);
				}
				else if (biome instanceof LOTRBiomeGenDeadMarshes && world.rand.nextInt(50) == 0)
				{
					world.spawnEntityInWorld(new LOTREntityDeadMarshFace(world, (double)i1 + (double)world.rand.nextFloat(), (double)j1 + 0.25D - (double)world.rand.nextFloat(), (double)k1 + (double)world.rand.nextFloat()));
				}
            }
        }
		
		world.theProfiler.endSection();
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if (event.world instanceof WorldClient)
		{
			LOTRClientProxy.customEffectRenderer.clearEffectsAndSetWorld(event.world);
		}
	}
	
	@SubscribeEvent
	public void onPreRenderGameOverlay(RenderGameOverlayEvent.Pre event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.theWorld != null && mc.thePlayer != null)
		{
			if (event.type == RenderGameOverlayEvent.ElementType.HELMET)
			{
				if (playersInPortals.containsKey(mc.thePlayer))
				{
					int i = (Integer)playersInPortals.get(mc.thePlayer);
					if (i > 0)
					{
						renderOverlay(0.1F + ((float)i / 100F) * 0.6F, mc, portalOverlay);
					}
				}
				
				if (playersInElvenPortals.containsKey(mc.thePlayer))
				{
					int i = (Integer)playersInElvenPortals.get(mc.thePlayer);
					if (i > 0)
					{
						renderOverlay(0.1F + ((float)i / (float)mc.thePlayer.getMaxInPortalTime()) * 0.6F, mc, elvenPortalOverlay);
					}
				}
				
				if (playersInMorgulPortals.containsKey(mc.thePlayer))
				{
					int i = (Integer)playersInMorgulPortals.get(mc.thePlayer);
					if (i > 0)
					{
						renderOverlay(0.1F + ((float)i / (float)mc.thePlayer.getMaxInPortalTime()) * 0.6F, mc, morgulPortalOverlay);
					}
				}
				
				if (LOTRMod.enableMistyMountainsMist)
				{
					if (mistTick > 0)
					{
						float mistFactor = (float)mc.thePlayer.posY / 256F;
						mistFactor *= 0.75F;
						renderOverlay(((float)mistTick / (float)mistTickMax) * mistFactor, mc, mistOverlay);
					}
				}
				
				if (frostTick > 0)
				{
					renderOverlay((float)frostTick / (float)frostTickMax * 0.9F, mc, frostOverlay);
				}
				
				if (burnTick > 0)
				{
					renderOverlay((float)burnTick / (float)burnTickMax * 0.6F, mc, burnOverlay);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPostDrawScreen(DrawScreenEvent.Post event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = event.gui;
		int x = event.mouseX;
		int y = event.mouseY;
		
		if (gui instanceof GuiChat)
		{
			IChatComponent component = mc.ingameGUI.getChatGUI().func_146236_a(Mouse.getX(), Mouse.getY());
	        if (component != null && component.getChatStyle().getChatHoverEvent() != null)
	        {
	            HoverEvent hoverevent = component.getChatStyle().getChatHoverEvent();
	            if (hoverevent.getAction() == LOTRAchievement.SHOW_LOTR_ACHIEVEMENT)
	            {
	            	LOTRGuiAchievementHoverEvent proxyGui = new LOTRGuiAchievementHoverEvent();
	            	proxyGui.setWorldAndResolution(mc, gui.width, gui.height);
	            	
	            	try
	            	{
		            	String unformattedText = hoverevent.getValue().getUnformattedText();
		            	int splitIndex = unformattedText.indexOf("$");
		            	
		            	String categoryName = unformattedText.substring(0, splitIndex);
		            	LOTRAchievement.Category category = LOTRAchievement.categoryForName(categoryName);
		            	int achievementID = Integer.parseInt(unformattedText.substring(splitIndex + 1));
		            	LOTRAchievement achievement = LOTRAchievement.achievementForCategoryAndID(category, achievementID);

		            	IChatComponent name = new ChatComponentTranslation("lotr.gui.achievements.hover.name", new Object[] {achievement.getAchievementChatComponent(), category.getDisplayName()});
	                    IChatComponent subtitle = new ChatComponentTranslation("lotr.gui.achievements.hover.subtitle", new Object[0]);
	                    subtitle.getChatStyle().setItalic(true);
	                    String desc = achievement.getDescription();
	                    
	                    ArrayList list = Lists.newArrayList(new String[] {name.getFormattedText(), subtitle.getFormattedText()});
	                    list.addAll(mc.fontRenderer.listFormattedStringToWidth(desc, 150));
	                    proxyGui.func_146283_a(list, x, y);
	                    
	                    System.out.println("Drawing");
	            	}
	            	catch (Exception e)
	            	{
	            		proxyGui.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid LOTRAchievement!", x, y);
	            	}
	            }
	        }
		}
	}
	
	@SubscribeEvent
	public void onRenderDebugText(RenderGameOverlayEvent.Text event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.gameSettings.showDebugInfo && mc.theWorld != null && mc.thePlayer != null && mc.thePlayer.dimension == LOTRMod.idDimension)
		{
            int i = MathHelper.floor_double(mc.thePlayer.posX);
            int k = MathHelper.floor_double(mc.thePlayer.posZ);
			BiomeGenBase biome = mc.theWorld.getBiomeGenForCoords(i, k);

			event.left.add(null);
			
			String color = Integer.toHexString(biome.color);
			while (color.length() < 6)
			{
				color = "0" + color;
			}
			
			event.left.add("Middle-earth biome: " + ((LOTRBiome)biome).getBiomeDisplayName() + ", ID: " + biome.biomeID + ", c: #" + color);
		}
	}
	
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		float f = event.partialTicks;
		mc.entityRenderer.enableLightmap((double)f);
		RenderHelper.disableStandardItemLighting();
		LOTRClientProxy.customEffectRenderer.renderParticles(mc.renderViewEntity, f);
		mc.entityRenderer.disableLightmap((double)f);
	}
	
	@SubscribeEvent
	public void getItemTooltip(ItemTooltipEvent event)
	{
		ItemStack item = event.itemStack;
		
		if (item.getTagCompound() != null && item.getTagCompound().hasKey("LOTROwner"))
		{
			String s = item.getTagCompound().getString("LOTROwner");
			event.toolTip.add(StatCollector.translateToLocalFormatted("item.lotr.itemOwner", new Object[] {s}));
		}
	}
	
	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent event)
	{
		EntityPlayerSP entityplayer = event.entity;
		float fov = event.fov;
		
        if (entityplayer.isUsingItem())
        {
			ItemStack itemstack = entityplayer.getItemInUse();
			Item item = itemstack.getItem();
			
			float maxDrawTime = 0F;
			if (item instanceof LOTRItemBow)
			{
				maxDrawTime = ((LOTRItemBow)item).getMaxDrawTime();
			}
			else if (item instanceof LOTRItemCrossbow)
			{
				maxDrawTime = ((LOTRItemCrossbow)item).getMaxDrawTime();
			}
			else if (item instanceof LOTRItemSpear)
			{
				maxDrawTime = ((LOTRItemSpear)item).getMaxDrawTime();
			}
			if (maxDrawTime > 0F)
			{
				int i = entityplayer.getItemInUseDuration();
				float use = (float)i / maxDrawTime;
				if (use > 1F)
				{
					use = 1F;
				}
				else
				{
					use *= use;
				}
				fov *= 1F - use * 0.15F;
				event.newfov = fov;
			}
        }
	}
	
	private boolean isGamePaused(Minecraft mc)
	{
		return mc.isSingleplayer() && mc.currentScreen != null && mc.currentScreen.doesGuiPauseGame() && !mc.getIntegratedServer().getPublic();
	}
	
	private void renderOverlay(float f, Minecraft mc, ResourceLocation texture)
	{
		if (mc.currentScreen != null)
		{
			return;
		}
		if (mc.gameSettings.keyBindPlayerList.getIsKeyPressed())
		{
			return;
		}
		
		ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int i = resolution.getScaledWidth();
		int j = resolution.getScaledHeight();
        mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, f);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		mc.getTextureManager().bindTexture(texture);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0D, (double)j, -90D, (double)0F, (double)256F);
        tessellator.addVertexWithUV((double)i, (double)j, -90D, (double)0F, (double)256F);
        tessellator.addVertexWithUV((double)i, 0D, -90D, (double)0F, (double)256F);
        tessellator.addVertexWithUV(0D, 0D, -90D, (double)0F, (double)256F);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}
	
	private void renderAlignment(Minecraft mc)
	{
		int alignment = LOTRLevelData.getData(mc.thePlayer).getAlignment(currentAlignmentFaction);
		ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int i = resolution.getScaledWidth();
		int j = resolution.getScaledHeight();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(LOTRClientProxy.alignmentTexture);
		
		boolean boss = BossStatus.bossName != null && BossStatus.statusBarTime > 0 && LOTRMod.alignmentMoveWhenBoss;
		alignmentXPosBase = (i / 2) + LOTRMod.alignmentXOffset;
		alignmentYPosBase = (boss ? 22 : 2) + LOTRMod.alignmentYOffset;
		
		if (firstAlignmentRender)
		{
			alignmentXPosCurrent = alignmentXPosBase;
			alignmentYPosCurrent = alignmentYPosBase;
			firstAlignmentRender = false;
		}
		
        drawTexturedModalRect(alignmentXPosCurrent - 116, alignmentYPosCurrent, 0, 0, 232, 18);
        
        float[] factionColors = currentAlignmentFaction.factionColor.getColorComponents(null);
        GL11.glColor4f(factionColors[0], factionColors[1], factionColors[2], 1F);
        drawTexturedModalRect(alignmentXPosCurrent - 116, alignmentYPosCurrent, 0, 34, 232, 18);

        GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(alignmentXPosCurrent - 8 + calculateAlignmentDisplay(alignment), alignmentYPosCurrent + 1, 16 * Math.round(alignmentChange / 3), 18, 16, 16);
		
		if (alignmentYPosCurrent == alignmentYPosBase)
		{
			FontRenderer f = mc.fontRenderer;
			int x = alignmentXPosCurrent;
			int y = alignmentYPosCurrent + 22;
			
			String s = currentAlignmentFaction.factionName();
			drawTextWithShadow(f, x - f.getStringWidth(s) / 2, y, s, 1F);
		
			int max = calculateMaxDisplayValue(alignment);
			String sMax = "+" + String.valueOf(max);
			String sMin = "-" + String.valueOf(max);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int xMax = (alignmentXPosCurrent * 2) - 220 - f.getStringWidth(sMax) / 2;
			int xMin = (alignmentXPosCurrent * 2) + 220 - f.getStringWidth(sMax) / 2;
			y *= 2;
			drawTextWithShadow(f, xMax, y, sMax, 1F);
			drawTextWithShadow(f, xMin, y, sMin, 1F);
			GL11.glScalef(2F, 2F, 2F);
			y /= 2;

			y += f.FONT_HEIGHT + 4;
			s = String.valueOf(alignment);
			if (alignment > 0)
			{
				s = "+" + s;
			}
			drawTextWithShadow(f, x - f.getStringWidth(s) / 2, y, s, 1F);
		}
	}
	
	public static int calculateAlignmentDisplay(int alignment)
	{
		float f = (float)alignment;
		while (MathHelper.abs(f) >= 1F)
		{
			f /= 10F;
		}
		f *= 110F;
		f *= -1F;
		return Math.round(f);
	}
	
	public static int calculateMaxDisplayValue(int alignment)
	{
		if (alignment == 0)
		{
			return 10;
		}
		else
		{
			int i = 1;
			float f = (float)alignment;
			while (MathHelper.abs(f) >= 1F)
			{
				f /= 10F;
				i *= 10;
			}
			return i;
		}
	}
	
    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height)
    {
        float f = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)0F, (double)((float)(u + 0) * f), (double)((float)(v + height) * f));
        tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)0F, (double)((float)(u + width) * f), (double)((float)(v + height) * f));
        tessellator.addVertexWithUV((double)(x + width), (double)(y + 0), (double)0F, (double)((float)(u + width) * f), (double)((float)(v + 0) * f));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)0F, (double)((float)(u + 0) * f), (double)((float)(v + 0) * f));
        tessellator.draw();
    }
	
	public static void drawTextWithShadow(FontRenderer f, int x, int y, String s, float transparencyFloat)
	{
		int transparency = (int)(transparencyFloat * 255F) << 24;
		f.drawString(s, x - 1, y - 1, 0x000000 + transparency);
		f.drawString(s, x, y - 1, 0x000000 + transparency);
		f.drawString(s, x + 1, y - 1, 0x000000 + transparency);
		f.drawString(s, x + 1, y, 0x000000 + transparency);
		f.drawString(s, x + 1, y + 1, 0x000000 + transparency);
		f.drawString(s, x, y + 1, 0x000000 + transparency);
		f.drawString(s, x - 1, y + 1, 0x000000 + transparency);
		f.drawString(s, x - 1, y, 0x000000 + transparency);
		f.drawString(s, x, y, 0xFFEE0C + transparency);
	}
	
	public void onFrostDamage()
	{
		frostTick = frostTickMax;
	}
	
	public void onBurnDamage()
	{
		burnTick = burnTickMax;
	}
	
	public void updateDate()
	{
		newDate = newDateMax;
	}
}
