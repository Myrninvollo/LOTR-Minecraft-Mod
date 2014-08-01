package lotr.client;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import lotr.client.fx.LOTREffectRenderer;
import lotr.client.fx.LOTREntityAlignmentBonus;
import lotr.client.fx.LOTREntityBlueFlameFX;
import lotr.client.fx.LOTREntityDeadMarshFace;
import lotr.client.fx.LOTREntityElvenGlowFX;
import lotr.client.fx.LOTREntityLargeBlockFX;
import lotr.client.fx.LOTREntityLeafFX;
import lotr.client.fx.LOTREntityLorienButterflyFX;
import lotr.client.fx.LOTREntityMTCHealFX;
import lotr.client.fx.LOTREntityMTCSpawnFX;
import lotr.client.fx.LOTREntityMarshFlameFX;
import lotr.client.fx.LOTREntityMarshLightFX;
import lotr.client.fx.LOTREntityMirkwoodWaterFX;
import lotr.client.fx.LOTREntityMorgulPortalFX;
import lotr.client.fx.LOTREntityQuenditeSmokeFX;
import lotr.client.model.LOTRModelElvenHelmet;
import lotr.client.model.LOTRModelGemsbokHelmet;
import lotr.client.model.LOTRModelLeatherHat;
import lotr.client.model.LOTRModelMorgulHelmet;
import lotr.client.model.LOTRModelWingedHelmet;
import lotr.client.render.LOTRRenderBlocks;
import lotr.client.render.LOTRRenderPlayer;
import lotr.client.render.entity.LOTRRenderAlignmentBonus;
import lotr.client.render.entity.LOTRRenderBandit;
import lotr.client.render.entity.LOTRRenderBanner;
import lotr.client.render.entity.LOTRRenderBannerWall;
import lotr.client.render.entity.LOTRRenderBird;
import lotr.client.render.entity.LOTRRenderBlacksmith;
import lotr.client.render.entity.LOTRRenderButterfly;
import lotr.client.render.entity.LOTRRenderCamel;
import lotr.client.render.entity.LOTRRenderCrocodile;
import lotr.client.render.entity.LOTRRenderCrossbowBolt;
import lotr.client.render.entity.LOTRRenderDeadMarshFace;
import lotr.client.render.entity.LOTRRenderDunlending;
import lotr.client.render.entity.LOTRRenderDunlendingBase;
import lotr.client.render.entity.LOTRRenderDwarf;
import lotr.client.render.entity.LOTRRenderDwarfCommander;
import lotr.client.render.entity.LOTRRenderElf;
import lotr.client.render.entity.LOTRRenderElfLord;
import lotr.client.render.entity.LOTRRenderElvenTrader;
import lotr.client.render.entity.LOTRRenderEnt;
import lotr.client.render.entity.LOTRRenderEntityBarrel;
import lotr.client.render.entity.LOTRRenderEntityOrcBomb;
import lotr.client.render.entity.LOTRRenderFlamingo;
import lotr.client.render.entity.LOTRRenderGandalfFireball;
import lotr.client.render.entity.LOTRRenderGemsbok;
import lotr.client.render.entity.LOTRRenderGiraffe;
import lotr.client.render.entity.LOTRRenderGollum;
import lotr.client.render.entity.LOTRRenderGondorSoldier;
import lotr.client.render.entity.LOTRRenderGondorianCaptain;
import lotr.client.render.entity.LOTRRenderHighElfLord;
import lotr.client.render.entity.LOTRRenderHobbit;
import lotr.client.render.entity.LOTRRenderHorse;
import lotr.client.render.entity.LOTRRenderHuorn;
import lotr.client.render.entity.LOTRRenderInvasionSpawner;
import lotr.client.render.entity.LOTRRenderLion;
import lotr.client.render.entity.LOTRRenderMarshWraith;
import lotr.client.render.entity.LOTRRenderMidges;
import lotr.client.render.entity.LOTRRenderMirkwoodSpider;
import lotr.client.render.entity.LOTRRenderMordorSpider;
import lotr.client.render.entity.LOTRRenderMountainTroll;
import lotr.client.render.entity.LOTRRenderMountainTrollChieftain;
import lotr.client.render.entity.LOTRRenderNearHaradrim;
import lotr.client.render.entity.LOTRRenderNearHaradrimWarlord;
import lotr.client.render.entity.LOTRRenderNurnSlave;
import lotr.client.render.entity.LOTRRenderOlogHai;
import lotr.client.render.entity.LOTRRenderOrc;
import lotr.client.render.entity.LOTRRenderPlate;
import lotr.client.render.entity.LOTRRenderPortal;
import lotr.client.render.entity.LOTRRenderRabbit;
import lotr.client.render.entity.LOTRRenderRanger;
import lotr.client.render.entity.LOTRRenderRhino;
import lotr.client.render.entity.LOTRRenderRohanMeadhost;
import lotr.client.render.entity.LOTRRenderRohirrim;
import lotr.client.render.entity.LOTRRenderRohirrimMarshal;
import lotr.client.render.entity.LOTRRenderSaruman;
import lotr.client.render.entity.LOTRRenderSauron;
import lotr.client.render.entity.LOTRRenderScorpion;
import lotr.client.render.entity.LOTRRenderShirePony;
import lotr.client.render.entity.LOTRRenderSkeleton;
import lotr.client.render.entity.LOTRRenderSmokeRing;
import lotr.client.render.entity.LOTRRenderSpear;
import lotr.client.render.entity.LOTRRenderStoneTroll;
import lotr.client.render.entity.LOTRRenderThrowingAxe;
import lotr.client.render.entity.LOTRRenderThrownRock;
import lotr.client.render.entity.LOTRRenderTraderRespawn;
import lotr.client.render.entity.LOTRRenderTroll;
import lotr.client.render.entity.LOTRRenderWarg;
import lotr.client.render.entity.LOTRRenderWargskinRug;
import lotr.client.render.entity.LOTRRenderWildBoar;
import lotr.client.render.entity.LOTRRenderWoodElfCaptain;
import lotr.client.render.entity.LOTRRenderWraithBall;
import lotr.client.render.entity.LOTRRenderZebra;
import lotr.client.render.item.LOTRRenderBannerItem;
import lotr.client.render.item.LOTRRenderBlownItem;
import lotr.client.render.item.LOTRRenderBow;
import lotr.client.render.item.LOTRRenderCrossbow;
import lotr.client.render.item.LOTRRenderElvenBlade;
import lotr.client.render.item.LOTRRenderLargeItem;
import lotr.client.render.tileentity.LOTRRenderArmorStand;
import lotr.client.render.tileentity.LOTRRenderBeacon;
import lotr.client.render.tileentity.LOTRRenderDwarvenDoor;
import lotr.client.render.tileentity.LOTRRenderElvenPortal;
import lotr.client.render.tileentity.LOTRRenderEntJar;
import lotr.client.render.tileentity.LOTRRenderGuldurilGlow;
import lotr.client.render.tileentity.LOTRRenderMorgulPortal;
import lotr.client.render.tileentity.LOTRRenderMug;
import lotr.client.render.tileentity.LOTRRenderPlateFood;
import lotr.client.render.tileentity.LOTRRenderTrollTotem;
import lotr.client.render.tileentity.LOTRTileEntityMobSpawnerRenderer;
import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRMod;
import lotr.common.LOTRTickHandlerServer;
import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.animal.LOTREntityCrocodile;
import lotr.common.entity.animal.LOTREntityFlamingo;
import lotr.common.entity.animal.LOTREntityGemsbok;
import lotr.common.entity.animal.LOTREntityGiraffe;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.animal.LOTREntityLionBase;
import lotr.common.entity.animal.LOTREntityMidges;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.entity.animal.LOTREntityRhino;
import lotr.common.entity.animal.LOTREntityShirePony;
import lotr.common.entity.animal.LOTREntityWildBoar;
import lotr.common.entity.animal.LOTREntityZebra;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.item.LOTREntityBannerWall;
import lotr.common.entity.item.LOTREntityBarrel;
import lotr.common.entity.item.LOTREntityOrcBomb;
import lotr.common.entity.item.LOTREntityPortal;
import lotr.common.entity.item.LOTREntityStoneTroll;
import lotr.common.entity.item.LOTREntityTraderRespawn;
import lotr.common.entity.item.LOTREntityWargskinRug;
import lotr.common.entity.npc.LOTREntityBandit;
import lotr.common.entity.npc.LOTREntityBlueDwarfCommander;
import lotr.common.entity.npc.LOTREntityBlueDwarfMerchant;
import lotr.common.entity.npc.LOTREntityDunlending;
import lotr.common.entity.npc.LOTREntityDunlendingWarrior;
import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTREntityDwarfCommander;
import lotr.common.entity.npc.LOTREntityElf;
import lotr.common.entity.npc.LOTREntityElfLord;
import lotr.common.entity.npc.LOTREntityElvenTrader;
import lotr.common.entity.npc.LOTREntityEnt;
import lotr.common.entity.npc.LOTREntityGollum;
import lotr.common.entity.npc.LOTREntityGondorBlacksmith;
import lotr.common.entity.npc.LOTREntityGondorSoldier;
import lotr.common.entity.npc.LOTREntityGondorianCaptain;
import lotr.common.entity.npc.LOTREntityHighElfLord;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityHuornBase;
import lotr.common.entity.npc.LOTREntityMarshWraith;
import lotr.common.entity.npc.LOTREntityMirkwoodSpider;
import lotr.common.entity.npc.LOTREntityMordorSpider;
import lotr.common.entity.npc.LOTREntityMountainTroll;
import lotr.common.entity.npc.LOTREntityMountainTrollChieftain;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarlord;
import lotr.common.entity.npc.LOTREntityNurnSlave;
import lotr.common.entity.npc.LOTREntityOlogHai;
import lotr.common.entity.npc.LOTREntityOrc;
import lotr.common.entity.npc.LOTREntityRanger;
import lotr.common.entity.npc.LOTREntityRohanBlacksmith;
import lotr.common.entity.npc.LOTREntityRohanMeadhost;
import lotr.common.entity.npc.LOTREntityRohirrim;
import lotr.common.entity.npc.LOTREntityRohirrimMarshal;
import lotr.common.entity.npc.LOTREntitySaruman;
import lotr.common.entity.npc.LOTREntitySauron;
import lotr.common.entity.npc.LOTREntityScorpion;
import lotr.common.entity.npc.LOTREntitySkeletalWraith;
import lotr.common.entity.npc.LOTREntityTroll;
import lotr.common.entity.npc.LOTREntityWarg;
import lotr.common.entity.npc.LOTREntityWoodElfCaptain;
import lotr.common.entity.projectile.LOTREntityCrossbowBolt;
import lotr.common.entity.projectile.LOTREntityGandalfFireball;
import lotr.common.entity.projectile.LOTREntityMarshWraithBall;
import lotr.common.entity.projectile.LOTREntityMysteryWeb;
import lotr.common.entity.projectile.LOTREntityPebble;
import lotr.common.entity.projectile.LOTREntityPlate;
import lotr.common.entity.projectile.LOTREntitySmokeRing;
import lotr.common.entity.projectile.LOTREntitySpear;
import lotr.common.entity.projectile.LOTREntityThrowingAxe;
import lotr.common.entity.projectile.LOTREntityThrownRock;
import lotr.common.item.LOTRItemBow;
import lotr.common.item.LOTRItemCrossbow;
import lotr.common.item.LOTRItemSword;
import lotr.common.tileentity.LOTRTileEntityArmorStand;
import lotr.common.tileentity.LOTRTileEntityBeacon;
import lotr.common.tileentity.LOTRTileEntityDwarvenDoor;
import lotr.common.tileentity.LOTRTileEntityElvenPortal;
import lotr.common.tileentity.LOTRTileEntityEntJar;
import lotr.common.tileentity.LOTRTileEntityGulduril;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import lotr.common.tileentity.LOTRTileEntityMorgulPortal;
import lotr.common.tileentity.LOTRTileEntityMug;
import lotr.common.tileentity.LOTRTileEntityPlate;
import lotr.common.tileentity.LOTRTileEntityTrollTotem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

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
	
	@Override
	public boolean isClient()
	{
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
	}

	@Override
	public void onLoad()
	{
		FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(LOTRMod.getModID());
		channel.register(new LOTRPacketHandlerClient(this));

		customEffectRenderer = new LOTREffectRenderer(Minecraft.getMinecraft());

		RenderingRegistry.registerEntityRenderingHandler(LOTREntityPortal.class, new LOTRRenderPortal());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityHorse.class, new LOTRRenderHorse(new ModelHorse(), 0.75F));
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityHobbit.class, new LOTRRenderHobbit());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntitySmokeRing.class, new LOTRRenderSmokeRing());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityOrc.class, new LOTRRenderOrc());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityShirePony.class, new LOTRRenderShirePony(new ModelHorse(), 0.75F));
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityOrcBomb.class, new LOTRRenderEntityOrcBomb());
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
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorianCaptain.class, new LOTRRenderGondorianCaptain());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityDwarfCommander.class, new LOTRRenderDwarfCommander());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBlueDwarfCommander.class, new LOTRRenderDwarfCommander());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBlueDwarfMerchant.class, new LOTRRenderDwarfCommander());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityThrowingAxe.class, new LOTRRenderThrowingAxe());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityCrossbowBolt.class, new LOTRRenderCrossbowBolt());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityTroll.class, new LOTRRenderTroll());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityOlogHai.class, new LOTRRenderOlogHai());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityStoneTroll.class, new LOTRRenderStoneTroll());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityElfLord.class, new LOTRRenderElfLord());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGollum.class, new LOTRRenderGollum());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityMirkwoodSpider.class, new LOTRRenderMirkwoodSpider());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohirrim.class, new LOTRRenderRohirrim());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohirrimMarshal.class, new LOTRRenderRohirrimMarshal());
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
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityWoodElfCaptain.class, new LOTRRenderWoodElfCaptain());
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
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityZebra.class, new LOTRRenderZebra(new ModelHorse(), 0.75F));
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityRhino.class, new LOTRRenderRhino());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityCrocodile.class, new LOTRRenderCrocodile());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityNearHaradrim.class, new LOTRRenderNearHaradrim());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityNearHaradrimWarlord.class, new LOTRRenderNearHaradrimWarlord());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityGemsbok.class, new LOTRRenderGemsbok());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityFlamingo.class, new LOTRRenderFlamingo());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityHighElfLord.class, new LOTRRenderHighElfLord());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityScorpion.class, new LOTRRenderScorpion());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBird.class, new LOTRRenderBird());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityCamel.class, new LOTRRenderCamel());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityBandit.class, new LOTRRenderBandit());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntitySaruman.class, new LOTRRenderSaruman());
		RenderingRegistry.registerEntityRenderingHandler(LOTREntityInvasionSpawner.class, new LOTRRenderInvasionSpawner());

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
		
		MinecraftForgeClient.registerItemRenderer(LOTRMod.hobbitPipe, new LOTRRenderBlownItem());
		MinecraftForgeClient.registerItemRenderer(LOTRMod.commandHorn, new LOTRRenderBlownItem());
		
		MinecraftForgeClient.registerItemRenderer(LOTRMod.banner, new LOTRRenderBannerItem());
		
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
}
