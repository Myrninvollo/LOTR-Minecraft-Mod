package lotr.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import lotr.client.fx.*;
import lotr.client.gui.LOTRGuiMap;
import lotr.client.model.*;
import lotr.client.render.LOTRRenderBlocks;
import lotr.client.render.LOTRRenderPlayer;
import lotr.client.render.entity.*;
import lotr.client.render.item.*;
import lotr.client.render.tileentity.*;
import lotr.common.*;
import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.animal.*;
import lotr.common.entity.item.*;
import lotr.common.entity.npc.*;
import lotr.common.entity.projectile.*;
import lotr.common.item.*;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class LOTRClientProxy extends LOTRCommonProxy
{
	public static ResourceLocation enchantmentTexture = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	public static ResourceLocation alignmentTexture = new ResourceLocation("lotr:gui/alignment.png");
	public static ResourceLocation particlesTexture = new ResourceLocation("lotr:misc/particles.png");
	
	public static int TESSELLATOR_MAX_BRIGHTNESS = 15728880;

	public static LOTRModelWingedHelmet modelWingedHelmet = new LOTRModelWingedHelmet(1F);
	public static LOTRModelLeatherHat modelLeatherHat = new LOTRModelLeatherHat();
	public static LOTRModelMorgulHelmet modelMorgulHelmet = new LOTRModelMorgulHelmet(1F);
	public static LOTRModelGemsbokHelmet modelGemsbokHelmet = new LOTRModelGemsbokHelmet(1F);
	public static LOTRModelElvenHelmet modelHighElvenHelmet = new LOTRModelElvenHelmet(1F);
	
	public static LOTREffectRenderer customEffectRenderer;
	public static LOTRRenderPlayer specialPlayerRenderer = new LOTRRenderPlayer();
	public static LOTRTickHandlerClient tickHandler = new LOTRTickHandlerClient();
	public static LOTRKeyHandler keyHandler = new LOTRKeyHandler();
	
	private int beaconRenderID;
	private int barrelRenderID;
	private int orcBombRenderID;
	private int orcTorchRenderID;
	private int mobSpawnerRenderID;
	private int plateRenderID;
	private int stalactiteRenderID;
	private int flowerPotRenderID;
	private int cloverRenderID;
	private int entJarRenderID;
	private int trollTotemRenderID;
	private int fenceRenderID;
	private int grassRenderID;
	
	@Override
	public boolean isClient()
	{
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
	}
	
	@Override
	public World getClientWorld()
	{
		return Minecraft.getMinecraft().theWorld;
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void onLoad()
	{
		FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(LOTRMod.getModID());
		channel.register(new LOTRPacketHandlerClient(this));

		customEffectRenderer = new LOTREffectRenderer(Minecraft.getMinecraft());
		
		LOTRTextures.load();

		RenderingRegistry.registerEntityRenderingHandler(LOTREntityPortal.class, new LOTRRenderPortal());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityHorse.class, new LOTRRenderHorse());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityHobbit.class, new LOTRRenderHobbit());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntitySmokeRing.class, new LOTRRenderSmokeRing());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityOrc.class, new LOTRRenderOrc());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityShirePony.class, new LOTRRenderShirePony());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityOrcBomb.class, new LOTRRenderOrcBomb());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityWarg.class, new LOTRRenderWarg());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGandalfFireball.class, new LOTRRenderGandalfFireball());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorSoldier.class, new LOTRRenderGondorSoldier());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntitySpear.class, new LOTRRenderSpear());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntitySauron.class, new LOTRRenderSauron());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityElf.class, new LOTRRenderElf());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityPlate.class, new LOTRRenderPlate());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityWargskinRug.class, new LOTRRenderWargskinRug());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntitySkeletalWraith.class, new LOTRRenderSkeleton());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorBlacksmith.class, new LOTRRenderBlacksmith("gondor/blacksmith"));
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityElvenTrader.class, new LOTRRenderElvenTrader());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityAlignmentBonus.class, new LOTRRenderAlignmentBonus());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityDwarf.class, new LOTRRenderDwarf());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMarshWraith.class, new LOTRRenderMarshWraith());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMarshWraithBall.class, new LOTRRenderWraithBall());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityDwarfCommander.class, new LOTRRenderDwarfCommander());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBlueDwarfCommander.class, new LOTRRenderDwarfCommander());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBlueDwarfMerchant.class, new LOTRRenderDwarfCommander());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityThrowingAxe.class, new LOTRRenderThrowingAxe());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityCrossbowBolt.class, new LOTRRenderCrossbowBolt());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityTroll.class, new LOTRRenderTroll());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityOlogHai.class, new LOTRRenderOlogHai());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityStoneTroll.class, new LOTRRenderStoneTroll());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGollum.class, new LOTRRenderGollum());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMirkwoodSpider.class, new LOTRRenderMirkwoodSpider());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohirrim.class, new LOTRRenderRohirrim());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityPebble.class, new RenderSnowball(LOTRMod.pebble));
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMysteryWeb.class, new RenderSnowball(LOTRMod.mysteryWeb));
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanBlacksmith.class, new LOTRRenderBlacksmith("rohan/blacksmith"));
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRanger.class, new LOTRRenderRanger());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityDunlendingWarrior.class, new LOTRRenderDunlendingBase());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityDunlending.class, new LOTRRenderDunlending());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityEnt.class, new LOTRRenderEnt());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityTraderRespawn.class, new LOTRRenderTraderRespawn());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMountainTroll.class, new LOTRRenderMountainTroll());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityThrownRock.class, new LOTRRenderThrownRock());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMountainTrollChieftain.class, new LOTRRenderMountainTrollChieftain());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityHuornBase.class, new LOTRRenderHuorn());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanMeadhost.class, new LOTRRenderRohanMeadhost());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityButterfly.class, new LOTRRenderButterfly());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBarrel.class, new LOTRRenderEntityBarrel());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMidges.class, new LOTRRenderMidges());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityDeadMarshFace.class, new LOTRRenderDeadMarshFace());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityNurnSlave.class, new LOTRRenderNurnSlave());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRabbit.class, new LOTRRenderRabbit());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityWildBoar.class, new LOTRRenderWildBoar());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMordorSpider.class, new LOTRRenderMordorSpider());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBanner.class, new LOTRRenderBanner());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBannerWall.class, new LOTRRenderBannerWall());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityLionBase.class, new LOTRRenderLion());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGiraffe.class, new LOTRRenderGiraffe());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityZebra.class, new LOTRRenderZebra());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRhino.class, new LOTRRenderRhino());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityCrocodile.class, new LOTRRenderCrocodile());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityNearHaradrim.class, new LOTRRenderNearHaradrim());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityNearHaradrimWarlord.class, new LOTRRenderNearHaradrimWarlord());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGemsbok.class, new LOTRRenderGemsbok());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityFlamingo.class, new LOTRRenderFlamingo());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityScorpion.class, new LOTRRenderScorpion());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBird.class, new LOTRRenderBird());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityCamel.class, new LOTRRenderCamel());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBandit.class, new LOTRRenderBandit());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntitySaruman.class, new LOTRRenderSaruman());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityInvasionSpawner.class, new LOTRRenderInvasionSpawner());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityElk.class, new LOTRRenderElk());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMirkTroll.class, new LOTRRenderMirkTroll());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityTermite.class, new LOTRRenderTermite());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityThrownTermite.class, new RenderSnowball(LOTRMod.termite));
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityDikDik.class, new LOTRRenderDikDik());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityUtumnoIceSpider.class, new LOTRRenderUtumnoIceSpider());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityConker.class, new RenderSnowball(LOTRMod.chestnut));

		beaconRenderID = RenderingRegistry.getNextAvailableRenderId();
		barrelRenderID = RenderingRegistry.getNextAvailableRenderId();
		orcBombRenderID = RenderingRegistry.getNextAvailableRenderId();
		orcTorchRenderID = RenderingRegistry.getNextAvailableRenderId();
		mobSpawnerRenderID = RenderingRegistry.getNextAvailableRenderId();
		plateRenderID = RenderingRegistry.getNextAvailableRenderId();
		stalactiteRenderID = RenderingRegistry.getNextAvailableRenderId();
		flowerPotRenderID = RenderingRegistry.getNextAvailableRenderId();
		cloverRenderID = RenderingRegistry.getNextAvailableRenderId();
		entJarRenderID = RenderingRegistry.getNextAvailableRenderId();
		trollTotemRenderID = RenderingRegistry.getNextAvailableRenderId();
		fenceRenderID = RenderingRegistry.getNextAvailableRenderId();
		grassRenderID = RenderingRegistry.getNextAvailableRenderId();
		
		RenderingRegistry.registerBlockHandler(beaconRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(barrelRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(orcBombRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(orcTorchRenderID, new LOTRRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(mobSpawnerRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(plateRenderID, new LOTRRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(stalactiteRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(flowerPotRenderID, new LOTRRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(cloverRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(entJarRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(trollTotemRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(fenceRenderID, new LOTRRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(grassRenderID, new LOTRRenderBlocks(false));
		
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityBeacon.class, new LOTRRenderBeacon());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityMobSpawner.class, new LOTRTileEntityMobSpawnerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityPlate.class, new LOTRRenderPlateFood());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityElvenPortal.class, new LOTRRenderElvenPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityGulduril.class, new LOTRRenderGuldurilGlow());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityDwarvenDoor.class, new LOTRRenderDwarvenDoor());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityMorgulPortal.class, new LOTRRenderMorgulPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityArmorStand.class, new LOTRRenderArmorStand());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityMug.class, new LOTRRenderMug());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityEntJar.class, new LOTRRenderEntJar());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityTrollTotem.class, new LOTRRenderTrollTotem());
		ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityUtumnoPortal.class, new LOTRRenderUtumnoPortal());
		
		MinecraftForgeClient.registerItemRenderer(LOTRMod.hobbitPipe, new LOTRRenderBlownItem());
		MinecraftForgeClient.registerItemRenderer(LOTRMod.commandHorn, new LOTRRenderBlownItem());
		
		MinecraftForgeClient.registerItemRenderer(LOTRMod.banner, new LOTRRenderBannerItem());
		MinecraftForgeClient.registerItemRenderer(LOTRMod.orcSkullStaff, new LOTRRenderSkullStaff());
		
		try
		{
			for (Field field : LOTRMod.class.getFields())
			{
				if (field.get(null) instanceof Item)
				{
					Item item = (Item)field.get(null);
					
					if (item instanceof LOTRItemCrossbow)
					{
						MinecraftForgeClient.registerItemRenderer(item, new LOTRRenderCrossbow());
					}
					else if (item instanceof LOTRItemBow)
					{
						MinecraftForgeClient.registerItemRenderer(item, new LOTRRenderBow());
					}
					else if (item instanceof LOTRItemSword && ((LOTRItemSword)item).isElvenBlade())
					{
						double d = 24D;
						if (item == LOTRMod.sting)
						{
							d = 40D;
						}
						
						MinecraftForgeClient.registerItemRenderer(item, new LOTRRenderElvenBlade(d));
					}
					else
					{
						try
						{
							ResourceLocation large = LOTRRenderLargeItem.getLargeItemTexture(item);
							if (Minecraft.getMinecraft().getResourceManager().getResource(large) != null)
							{
								MinecraftForgeClient.registerItemRenderer(item, new LOTRRenderLargeItem());
							}
						}
						catch (FileNotFoundException e) {}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void onPostload()
	{
		LOTRLang.updateTranslations();
	}
	
    public static void renderHealthBar(EntityLivingBase entity, double d, double d1, double d2, int i, RenderManager renderManager)
    {
		if (entity.riddenByEntity instanceof LOTREntityNPC)
		{
			return;
		}
		
        double d3 = entity.getDistanceSqToEntity(renderManager.livingPlayer);

        if (d3 <= (double)(i * i))
        {
            float f1 = 1.6F;
            float f2 = 0.016666668F * f1;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d, (float)d1 + entity.height + 0.7F, (float)d2);
            GL11.glNormal3f(0F, 1F, 0F);
            GL11.glRotatef(-renderManager.playerViewY, 0F, 1F, 0F);
            GL11.glRotatef(renderManager.playerViewX, 1F, 0F, 0F);
            GL11.glScalef(-f2, -f2, f2);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator tessellator = Tessellator.instance;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
			
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(1F, 0F, 0F, 1F);
            tessellator.addVertex(-19D, 19D, 0D);
            tessellator.addVertex(-19D, 20.5D, 0D);
            tessellator.addVertex(19D, 20.5D, 0D);
            tessellator.addVertex(19D, 19D, 0D);
            tessellator.draw();
			
			double healthRemaining = (double)(entity.getHealth() / entity.getMaxHealth());
			if (healthRemaining < 0D)
			{
				healthRemaining = 0D;
			}
			
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(0F, 1F, 0F, 1F);
            tessellator.addVertex(-19D, 19D, 0D);
            tessellator.addVertex(-19D, 20.5D, 0D);
            tessellator.addVertex(-19D + (38D * healthRemaining), 20.5D, 0D);
            tessellator.addVertex(-19D + (38D * healthRemaining), 19D, 0D);
            tessellator.draw();
			
			if (entity.ridingEntity instanceof EntityLivingBase)
			{
				EntityLivingBase mount = (EntityLivingBase)entity.ridingEntity;
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_F(1F, 0F, 0F, 1F);
				tessellator.addVertex(-19D, 24D, 0D);
				tessellator.addVertex(-19D, 25.5D, 0D);
				tessellator.addVertex(19D, 25.5D, 0D);
				tessellator.addVertex(19D, 24D, 0D);
				tessellator.draw();
				
				double mountHealthRemaining = (double)(mount.getHealth() / mount.getMaxHealth());
				if (mountHealthRemaining < 0D)
				{
					mountHealthRemaining = 0D;
				}
				
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_F(0F, 0.75F, 1F, 1F);
				tessellator.addVertex(-19D, 24D, 0D);
				tessellator.addVertex(-19D, 25.5D, 0D);
				tessellator.addVertex(-19D + (38D * mountHealthRemaining), 25.5D, 0D);
				tessellator.addVertex(-19D + (38D * mountHealthRemaining), 24D, 0D);
				tessellator.draw();
			}
			
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glPopMatrix();
        }
    }

    public static void renderQuestBook(LOTREntityNPC npc, double d, double d1, double d2, float tick)
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	World world = mc.theWorld;
    	world.theProfiler.startSection("renderMiniquestBook");
    	
    	float distance = mc.renderViewEntity.getDistanceToEntity(npc);
    	boolean aboveHead = distance <= LOTRMiniQuest.RENDER_HEAD_DISTANCE;
    	
    	TextureManager textureManager = mc.getTextureManager();
    	RenderManager renderManager = RenderManager.instance;
    	EntityPlayer entityplayer = mc.thePlayer;
    	
    	if (!LOTRLevelData.getData(entityplayer).getMiniQuestsForEntity(npc, true).isEmpty())
    	{
    		ItemStack questBook = new ItemStack(LOTRMod.redBook);
			IIcon icon =  questBook.getIconIndex();
	        if (icon == null)
	        {
	            icon = ((TextureMap)textureManager.getTexture(TextureMap.locationItemsTexture)).getAtlasSprite("missingno");
	        }
			Tessellator tessellator = Tessellator.instance;
	        float minU = icon.getMinU();
	        float maxU = icon.getMaxU();
	        float minV = icon.getMinV();
	        float maxV = icon.getMaxV();
			
    		if (aboveHead)
    		{
	    		float age = ((float)npc.ticksExisted + tick);
	    		float rotation = age % 360F;
	    		rotation *= 6F;

				GL11.glPushMatrix();
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glTranslatef((float)d, (float)d1 + npc.height + 1F, (float)d2);
				float scale = 1F;
				GL11.glRotatef(rotation, 0F, 1F, 0F);
				GL11.glTranslatef(-0.5F * scale, 0F, 0.03125F * scale);
				GL11.glScalef(scale, scale, scale);
				
				textureManager.bindTexture(TextureMap.locationItemsTexture);
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				ItemRenderer.renderItemIn2D(tessellator, maxU, minV, minU, maxV, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
				
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				GL11.glPopMatrix();
    		}
    		else
            {
    			float scale = (distance / (float)LOTRMiniQuest.RENDER_HEAD_DISTANCE) * 1F;
    			scale = (float)Math.pow(scale, 1.4D);
    			
    			float alpha = (float)Math.pow(scale, -0.5D);
    			
                GL11.glPushMatrix();
                GL11.glTranslatef((float)d, (float)d1 + npc.height + 1F, (float)d2);
                GL11.glNormal3f(0F, 1F, 0F);
                GL11.glRotatef(-renderManager.playerViewY, 0F, 1F, 0F);
                GL11.glRotatef(renderManager.playerViewX, 1F, 0F, 0F);
                GL11.glScalef(scale, scale, scale);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

	    		textureManager.bindTexture(TextureMap.locationItemsTexture);
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
				GL11.glColor4f(1F, 1F, 1F, alpha);
				GL11.glColor4f(1F, 1F, 1F, alpha);
				GL11.glTranslatef(-0.5F, 0F, 0F);
				ItemRenderer.renderItemIn2D(tessellator, maxU, minV, minU, maxV, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
    			
				GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glColor4f(1F, 1F, 1F, 1F);
                GL11.glPopMatrix();
            }
    	}
    	
    	world.theProfiler.endSection();
    }
    
    public static void sendClientInfoPacket()
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	
    	ByteBuf data = Unpooled.buffer();
    	
    	data.writeInt(mc.thePlayer.getEntityId());
    	data.writeByte((byte)mc.thePlayer.dimension);
    	data.writeByte(LOTRTickHandlerClient.currentAlignmentFaction.ordinal());
    	data.writeByte(LOTRGuiMap.waypointMode.ordinal());
    	
    	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.clientInfo", data);
    	mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
	
	@Override
	public void setInPortal(EntityPlayer entityplayer)
	{
		if (!LOTRTickHandlerClient.playersInPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerClient.playersInPortals.put(entityplayer, Integer.valueOf(0));
		}
		
		if (Minecraft.getMinecraft().isSingleplayer() && !LOTRTickHandlerServer.playersInPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerServer.playersInPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	@Override
	public void setInElvenPortal(EntityPlayer entityplayer)
	{
		if (!LOTRTickHandlerClient.playersInElvenPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerClient.playersInElvenPortals.put(entityplayer, Integer.valueOf(0));
		}
		
		if (Minecraft.getMinecraft().isSingleplayer() && !LOTRTickHandlerServer.playersInElvenPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerServer.playersInElvenPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	@Override
	public void setInMorgulPortal(EntityPlayer entityplayer)
	{
		if (!LOTRTickHandlerClient.playersInMorgulPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerClient.playersInMorgulPortals.put(entityplayer, Integer.valueOf(0));
		}
		
		if (Minecraft.getMinecraft().isSingleplayer() && !LOTRTickHandlerServer.playersInMorgulPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerServer.playersInMorgulPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	@Override
	public void spawnParticle(String type, double d, double d1, double d2, double d3, double d4, double d5)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.renderViewEntity != null && mc.theWorld != null)
		{
			World world = mc.theWorld;
			int i = mc.gameSettings.particleSetting;
			
            if (i == 1 && world.rand.nextInt(3) == 0)
            {
                i = 2;
            }
			
			if (i > 1)
			{
				return;
			}
			
			if (type.equals("blueFlame"))
			{
				customEffectRenderer.addEffect(new LOTREntityBlueFlameFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("elvenGlow"))
			{
				mc.effectRenderer.addEffect(new LOTREntityElvenGlowFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("largeStone"))
			{
				mc.effectRenderer.addEffect(new LOTREntityLargeBlockFX(world, d, d1, d2, d3, d4, d5, Blocks.stone, 0));
			}

			else if (type.startsWith("leaf"))
			{
				String s = type.substring(4);
				int textureIndex = 0;
				if (s.startsWith("Gold"))
				{
					textureIndex = 16 + world.rand.nextInt(2);
				}
				else if (s.startsWith("Red"))
				{
					textureIndex = 18 + world.rand.nextInt(2);
				}
				else if (s.startsWith("Green"))
				{
					textureIndex = 20 + world.rand.nextInt(2);
				}
				
				if (textureIndex > 0)
				{
					if (type.indexOf("_") > -1)
					{
						int age = Integer.parseInt(type.substring(type.indexOf("_") + 1));
						customEffectRenderer.addEffect(new LOTREntityLeafFX(world, d, d1, d2, d3, d4, d5, textureIndex, age));
					}
					else
					{
						customEffectRenderer.addEffect(new LOTREntityLeafFX(world, d, d1, d2, d3, d4, d5, textureIndex));
					}
				}
			}
			
			else if (type.equals("lorienButterfly"))
			{
				mc.effectRenderer.addEffect(new LOTREntityLorienButterflyFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("marshFlame"))
			{
				mc.effectRenderer.addEffect(new LOTREntityMarshFlameFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("marshLight"))
			{
				customEffectRenderer.addEffect(new LOTREntityMarshLightFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("mirkwoodWater"))
			{
				mc.effectRenderer.addEffect(new LOTREntityMirkwoodWaterFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("morgulPortal"))
			{
				mc.effectRenderer.addEffect(new LOTREntityMorgulPortalFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("mtcArmor"))
			{
				mc.effectRenderer.addEffect(new LOTREntityLargeBlockFX(world, d, d1, d2, d3, d4, d5, Blocks.iron_block, 0));
			}
			
			else if (type.equals("mtcHeal"))
			{
				mc.effectRenderer.addEffect(new LOTREntityMTCHealFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("mtcSpawn"))
			{
				mc.effectRenderer.addEffect(new LOTREntityMTCSpawnFX(world, d, d1, d2, d3, d4, d5));
			}
			
			else if (type.equals("quenditeSmoke"))
			{
				mc.effectRenderer.addEffect(new LOTREntityQuenditeSmokeFX(world, d, d1, d2, d3, d4, d5));
			}
		}
	}
	
	@Override
	public void placeFlowerInPot(World world, int i, int j, int k, int side, ItemStack itemstack)
	{
		if (!world.isRemote)
		{
			super.placeFlowerInPot(world, i, j, k, side, itemstack);
		}
		else
		{
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(i, j, k, side, itemstack, 0F, 0F, 0F));
		}
	}
	
	@Override
	public void fillMugFromCauldron(World world, int i, int j, int k, int side, ItemStack itemstack)
	{
		if (!world.isRemote)
		{
			super.fillMugFromCauldron(world, i, j, k, side, itemstack);
		}
		else
		{
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(i, j, k, side, itemstack, 0F, 0F, 0F));
		}
	}
	
	@Override
	public int getBeaconRenderID()
	{
		return beaconRenderID;
	}
	
	@Override
	public int getBarrelRenderID()
	{
		return barrelRenderID;
	}
	
	@Override
	public int getOrcBombRenderID()
	{
		return orcBombRenderID;
	}
	
	@Override
	public int getOrcTorchRenderID()
	{
		return orcTorchRenderID;
	}
	
	@Override
	public int getMobSpawnerRenderID()
	{
		return mobSpawnerRenderID;
	}
	
	@Override
	public int getPlateRenderID()
	{
		return plateRenderID;
	}
	
	@Override
	public int getStalactiteRenderID()
	{
		return stalactiteRenderID;
	}
	
	@Override
	public int getFlowerPotRenderID()
	{
		return flowerPotRenderID;
	}
	
	@Override
	public int getCloverRenderID()
	{
		return cloverRenderID;
	}
	
	@Override
	public int getEntJarRenderID()
	{
		return entJarRenderID;
	}
	
	@Override
	public int getTrollTotemRenderID()
	{
		return trollTotemRenderID;
	}
	
	@Override
	public int getFenceRenderID()
	{
		return fenceRenderID;
	}
	
	@Override
	public int getGrassRenderID()
	{
		return grassRenderID;
	}
}
