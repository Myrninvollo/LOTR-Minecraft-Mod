package lotr.common.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTRAmbientCreature;
import lotr.common.entity.animal.LOTREntityWildBoar;
import lotr.common.entity.npc.LOTREntityGundabadOrc;
import lotr.common.entity.npc.LOTREntityGundabadOrcArcher;
import lotr.common.world.genlayer.LOTRGenLayerWorld;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.EnumHelper;

public class LOTRBiome extends BiomeGenBase
{
	private static Class[][] correctCreatureTypeParams =
	{
		{EnumCreatureType.class, Class.class, int.class, Material.class, boolean.class, boolean.class}
	};
	
	public static EnumCreatureType creatureType_LOTRAmbient = (EnumCreatureType)EnumHelper.addEnum(correctCreatureTypeParams, EnumCreatureType.class, "LOTRAmbient", LOTRAmbientCreature.class, 60, Material.air, true, false);
	
	public static BiomeGenBase river;
	public static BiomeGenBase rohan;
	public static BiomeGenBase mistyMountains;
	public static BiomeGenBase shire;
	public static BiomeGenBase shireWoodlands;
	public static BiomeGenBase mordor;
	public static BiomeGenBase mordorMountains;
	public static BiomeGenBase gondor;
	public static BiomeGenBase whiteMountains;
	public static BiomeGenBase lothlorien;
	public static BiomeGenBase lothlorienClearing;
	public static BiomeGenBase ironHills;
	public static BiomeGenBase deadMarshes;
	public static BiomeGenBase trollshaws;
	public static BiomeGenBase mirkwood;
	public static BiomeGenBase mirkwoodCorrupted;
	public static BiomeGenBase rohanUrukHighlands;
	public static BiomeGenBase emynMuil;
	public static BiomeGenBase ithilien;
	public static BiomeGenBase mordorRiver;
	public static BiomeGenBase deadMarshesRiver;
	public static BiomeGenBase loneLands;
	public static BiomeGenBase loneLandsHills;
	public static BiomeGenBase dunland;
	public static BiomeGenBase fangorn;
	public static BiomeGenBase mirkwoodRiver;
	public static BiomeGenBase ettenmoors;
	public static BiomeGenBase oldForest;
	public static BiomeGenBase harondor;
	public static BiomeGenBase eriador;
	public static BiomeGenBase eriadorDowns;
	public static BiomeGenBase eriadorWoodlands;
	public static BiomeGenBase greyMountains;
	public static BiomeGenBase midgewater;
	public static BiomeGenBase brownLands;
	public static BiomeGenBase ocean;
	public static BiomeGenBase valesOfAnduin;
	public static BiomeGenBase valesOfAnduinWoodlands;
	public static BiomeGenBase gladdenFields;
	public static BiomeGenBase lothlorienEdge;
	public static BiomeGenBase forodwaith;
	public static BiomeGenBase enedwaith;
	public static BiomeGenBase angmar;
	public static BiomeGenBase eregion;
	public static BiomeGenBase lindon;
	public static BiomeGenBase lindonWoodlands;
	public static BiomeGenBase eregionForest;
	public static BiomeGenBase blueMountains;
	public static BiomeGenBase mirkwoodMountains;
	public static BiomeGenBase wilderland;
	public static BiomeGenBase dagorlad;
	public static BiomeGenBase nurn;
	public static BiomeGenBase nurnen;
	public static BiomeGenBase eregionHills;
	public static BiomeGenBase brownLandsWoodlands;
	public static BiomeGenBase angmarMountains;
	public static BiomeGenBase enedwaithWoodlands;
	public static BiomeGenBase fangornMountains;
	public static BiomeGenBase fangornBirchForest;
	public static BiomeGenBase mistyMountainsForest;
	public static BiomeGenBase fangornWasteland;
	public static BiomeGenBase rohanWoodlands;
	public static BiomeGenBase gondorWoodlands;
	public static BiomeGenBase lake;
	public static BiomeGenBase beachStone;
	public static BiomeGenBase barrowDowns;
	//EMPTY SLOT
	public static BiomeGenBase fangornClearing;
	public static BiomeGenBase ithilienHills;
	public static BiomeGenBase ithilienWasteland;
	public static BiomeGenBase nindalf;
	public static BiomeGenBase coldfells;
	public static BiomeGenBase rohanBoulderFields;
	public static BiomeGenBase shireOrchard;
	public static BiomeGenBase wilderlandForest;
	public static BiomeGenBase swanfleet;
	public static BiomeGenBase harondorShrubland;
	public static BiomeGenBase minhiriath;
	public static BiomeGenBase minhiriathWoodlands;
	public static BiomeGenBase minhiriathWasteland;
	public static BiomeGenBase dunlandForest;
	public static BiomeGenBase nanUngol;
	public static BiomeGenBase gondorHills;
	public static BiomeGenBase island;
	public static BiomeGenBase forodwaithMountains;
	public static BiomeGenBase mistyMountainsFoothills;
	public static BiomeGenBase greyMountainsFoothills;
	public static BiomeGenBase blueMountainsFoothills;
	public static BiomeGenBase tundra;
	public static BiomeGenBase taiga;
	public static BiomeGenBase breeland;
	public static BiomeGenBase chetwood;
	public static BiomeGenBase forodwaithGlacier;
	public static BiomeGenBase whiteMountainsFoothills;
	public static BiomeGenBase beach;
	public static BiomeGenBase beachGravel;
	public static BiomeGenBase nearHarad;
	public static BiomeGenBase farHarad;
	public static BiomeGenBase haradMountains;
	public static BiomeGenBase umbar;
	public static BiomeGenBase farHaradJungle;
	public static BiomeGenBase farHaradJungleHills;
	public static BiomeGenBase nearHaradDunes;
	public static BiomeGenBase nearHaradBoulderFields;
	public static BiomeGenBase farHaradRiver;
	public static BiomeGenBase farHaradForest;
	public static BiomeGenBase nearHaradFertile;
	public static BiomeGenBase pertorogwaith;
	public static BiomeGenBase eriadorWoodlandsDense;
	public static BiomeGenBase valesOfAnduinWoodlandsDense;
	public static BiomeGenBase enedwaithWoodlandsDense;
	public static BiomeGenBase wilderlandForestDense;
	public static BiomeGenBase wilderlandHills;
	public static BiomeGenBase tolfalas;
	public static BiomeGenBase lebennin;
	public static BiomeGenBase rhun;
	public static BiomeGenBase rhunForest;
	public static BiomeGenBase redMountains;
	public static BiomeGenBase redMountainsFoothills;
	
	public static LOTRBiome[] lotrBiomeList = new LOTRBiome[256];
	
	public static void initBiomes()
	{
		river = new LOTRBiomeGenRiver(0).setMinMaxHeight(-0.5F, 0F).setColor(0x0087FF).setBiomeName("river");
		rohan = new LOTRBiomeGenRohan(1, false).setTemperatureRainfall(1F, 0.2F).setMinMaxHeight(0.2F, 0.3F).setColor(0x9DA152).setBiomeName("rohan");
		mistyMountains = new LOTRBiomeGenMistyMountains(2).setTemperatureRainfall(0.2F, 0.5F).setMinMaxHeight(2F, 2F).setColor(0xB8BCC1).setBiomeName("mistyMountains");
		shire = new LOTRBiomeGenShire(3).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.1F, 0.3F).setColor(0x36A01E).setBiomeName("shire");
		shireWoodlands = new LOTRBiomeGenShireWoodlands(4).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.2F, 0.5F).setColor(0x2B631F).setBiomeName("shireWoodlands");
		mordor = new LOTRBiomeGenMordor(5).setTemperatureRainfall(2F, 0F).setMinMaxHeight(0.3F, 0.6F).setColor(0x000000).setBiomeName("mordor");
		mordorMountains = new LOTRBiomeGenMordorMountains(6).setTemperatureRainfall(2F, 0F).setMinMaxHeight(2F, 3F).setColor(0x444444).setBiomeName("mordorMountains");
		gondor = new LOTRBiomeGenGondor(7).setTemperatureRainfall(0.8F, 0.8F).setMinMaxHeight(0.1F, 0.12F).setColor(0xC5CC53).setBiomeName("gondor");
		whiteMountains = new LOTRBiomeGenWhiteMountains(8).setTemperatureRainfall(0.6F, 0.8F).setMinMaxHeight(1.5F, 2F).setColor(0xDFE0CC).setBiomeName("whiteMountains");
		lothlorien = new LOTRBiomeGenLothlorien(9).setTemperatureRainfall(0.9F, 1F).setMinMaxHeight(0.2F, 0.5F).setColor(0xF2D337).setBiomeName("lothlorien");
		lothlorienClearing = new LOTRBiomeGenLothlorienClearing(10).setTemperatureRainfall(0.9F, 1F).setMinMaxHeight(0.2F, 0.2F).setColor(0xD0E03E).setBiomeName("lothlorienClearing");
		ironHills = new LOTRBiomeGenIronHills(11).setTemperatureRainfall(0.27F, 0.4F).setMinMaxHeight(0.3F, 1.4F).setColor(0xC48C62).setBiomeName("ironHills");
		deadMarshes = new LOTRBiomeGenDeadMarshes(12).setTemperatureRainfall(0.2F, 1F).setMinMaxHeight(0F, 0.1F).setColor(0x60523B).setBiomeName("deadMarshes");
		trollshaws = new LOTRBiomeGenTrollshaws(13).setTemperatureRainfall(0.6F, 0.8F).setMinMaxHeight(0.15F, 1F).setColor(0x9E8D69).setBiomeName("trollshaws");
		mirkwood = new LOTRBiomeGenMirkwood(14, false).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.2F, 0.3F).setColor(0x183811).setBiomeName("mirkwood");
		mirkwoodCorrupted = new LOTRBiomeGenMirkwood(15, true).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.2F, 0.4F).setColor(0x142112).setBiomeName("mirkwoodCorrupted");
		rohanUrukHighlands = new LOTRBiomeGenRohan(16, true).setTemperatureRainfall(0.7F, 0.4F).setMinMaxHeight(0.8F, 0.3F).setColor(0x90904D).setBiomeName("rohanUrukHighlands");
		emynMuil = new LOTRBiomeGenEmynMuil(17).setTemperatureRainfall(0.5F, 0.9F).setMinMaxHeight(0.2F, 0.8F).setColor(0x967E72).setBiomeName("emynMuil");
		ithilien = new LOTRBiomeGenIthilien(18).setTemperatureRainfall(0.7F, 0.9F).setMinMaxHeight(0.15F, 0.5F).setColor(0xA9B542).setBiomeName("ithilien");
		mordorRiver = new LOTRBiomeGenMordorRiver(19).setTemperatureRainfall(2F, 0F).setMinMaxHeight(-0.5F, 0F).setColor(0x000070).setBiomeName("mordorRiver");
		deadMarshesRiver = new LOTRBiomeGenDeadMarshes(20).setTemperatureRainfall(0.2F, 1F).setMinMaxHeight(-0.5F, 0F).setColor(0x4E5C60).setBiomeName("deadMarshesRiver");
		loneLands = new LOTRBiomeGenLoneLands(21).setTemperatureRainfall(0.6F, 0.5F).setMinMaxHeight(0.15F, 0.4F).setColor(0xBAA748).setBiomeName("loneLands");
		loneLandsHills = new LOTRBiomeGenLoneLandsHills(22).setTemperatureRainfall(0.6F, 0.5F).setMinMaxHeight(0.6F, 0.8F).setColor(0xC6B45B).setBiomeName("loneLandsHills");
		dunland = new LOTRBiomeGenDunland(23).setTemperatureRainfall(0.5F, 0.7F).setMinMaxHeight(0.3F, 0.5F).setColor(0x327238).setBiomeName("dunland");
		fangorn = new LOTRBiomeGenFangorn(24).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.2F, 0.4F).setColor(0x2C892D).setBiomeName("fangorn");
		mirkwoodRiver = new LOTRBiomeGenMirkwoodRiver(25).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(-0.5F, 0F).setColor(0x2C21A8).setBiomeName("mirkwoodRiver");
		ettenmoors = new LOTRBiomeGenEttenmoors(26).setTemperatureRainfall(0.2F, 0.6F).setMinMaxHeight(0.5F, 0.8F).setColor(0x7A6C52).setBiomeName("ettenmoors");
		oldForest = new LOTRBiomeGenOldForest(27).setTemperatureRainfall(0.5F, 1F).setMinMaxHeight(0.2F, 0.3F).setColor(0x21301D).setBiomeName("oldForest");
		harondor = new LOTRBiomeGenHarondor(28).setTemperatureRainfall(1F, 0.4F).setMinMaxHeight(0.2F, 0.3F).setColor(0xC9BA66).setBiomeName("harondor");
		eriador = new LOTRBiomeGenEriador(29).setTemperatureRainfall(0.7F, 0.7F).setMinMaxHeight(0.1F, 0.2F).setColor(0x6AAA42).setBiomeName("eriador");
		eriadorDowns = new LOTRBiomeGenEriadorDowns(30).setTemperatureRainfall(0.6F, 0.7F).setMinMaxHeight(0.5F, 0.3F).setColor(0x638E4E).setBiomeName("eriadorDowns");
		eriadorWoodlands = new LOTRBiomeGenEriadorWoodlands(31).setTemperatureRainfall(0.7F, 0.9F).setMinMaxHeight(0.1F, 0.4F).setColor(0x629944).setBiomeName("eriadorWoodlands");
		greyMountains = new LOTRBiomeGenGreyMountains(32).setTemperatureRainfall(0.28F, 0.2F).setMinMaxHeight(1.8F, 2F).setColor(0x777B7F).setBiomeName("greyMountains");
		midgewater = new LOTRBiomeGenMidgewater(33).setTemperatureRainfall(0.6F, 1F).setMinMaxHeight(0.05F, 0.2F).setColor(0x377755).setBiomeName("midgewater");
		brownLands = new LOTRBiomeGenBrownLands(34).setTemperatureRainfall(0.6F, 0.2F).setMinMaxHeight(0.2F, 0.2F).setColor(0x775438).setBiomeName("brownLands");
		ocean = new LOTRBiomeGenOcean(35).setTemperatureRainfall(0.5F, 0.5F).setMinMaxHeight(-1F, 0.3F).setColor(0x0055A0).setBiomeName("ocean");
		valesOfAnduin = new LOTRBiomeGenValesOfAnduin(36).setTemperatureRainfall(0.8F, 0.8F).setMinMaxHeight(0.1F, 0.1F).setColor(0x30AD75).setBiomeName("valesOfAnduin");
		valesOfAnduinWoodlands = new LOTRBiomeGenValesOfAnduinWoodlands(37).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.1F, 0.2F).setColor(0x2DAD5E).setBiomeName("valesOfAnduinWoodlands");
		gladdenFields = new LOTRBiomeGenGladdenFields(38).setTemperatureRainfall(0.6F, 1F).setMinMaxHeight(0.05F, 0.2F).setColor(0x40BC7A).setBiomeName("gladdenFields");
		lothlorienEdge = new LOTRBiomeGenLothlorienEdge(39).setTemperatureRainfall(0.9F, 1F).setMinMaxHeight(0.2F, 0.3F).setColor(0xEFC743).setBiomeName("lothlorienEdge");
		forodwaith = new LOTRBiomeGenForodwaith(40).setTemperatureRainfall(0F, 0.2F).setMinMaxHeight(0.1F, 0.1F).setColor(0xC9DAE0).setBiomeName("forodwaith");
		enedwaith = new LOTRBiomeGenEnedwaith(41).setTemperatureRainfall(0.6F, 0.8F).setMinMaxHeight(0.2F, 0.3F).setColor(0xADB754).setBiomeName("enedwaith");
		angmar = new LOTRBiomeGenAngmar(42).setTemperatureRainfall(0.2F, 0.2F).setMinMaxHeight(0.2F, 0.6F).setColor(0x262626).setBiomeName("angmar");
		eregion = new LOTRBiomeGenEregion(43).setTemperatureRainfall(0.6F, 0.7F).setMinMaxHeight(0.2F, 0.3F).setColor(0x40604C).setBiomeName("eregion");
		lindon = new LOTRBiomeGenLindon(44).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.15F, 0.2F).setColor(0x71BC45).setBiomeName("lindon");
		lindonWoodlands = new LOTRBiomeGenLindonWoodlands(45).setTemperatureRainfall(0.9F, 1F).setMinMaxHeight(0.2F, 0.5F).setColor(0x4DD151).setBiomeName("lindonWoodlands");
		eregionForest = new LOTRBiomeGenEregionForest(46).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.2F, 0.4F).setColor(0x39704C).setBiomeName("eregionForest");
		blueMountains = new LOTRBiomeGenBlueMountains(47).setTemperatureRainfall(0.22F, 0.8F).setMinMaxHeight(1.5F, 2F).setColor(0x8FA9BC).setBiomeName("blueMountains");
		mirkwoodMountains = new LOTRBiomeGenMirkwoodMountains(48).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(1.5F, 2F).setColor(0x334430).setBiomeName("mirkwoodMountains");
		wilderland = new LOTRBiomeGenWilderland(49).setTemperatureRainfall(0.9F, 0.4F).setMinMaxHeight(0.2F, 0.4F).setColor(0x93A850).setBiomeName("wilderland");
		dagorlad = new LOTRBiomeGenDagorlad(50).setTemperatureRainfall(1F, 0.2F).setMinMaxHeight(0.1F, 0.05F).setColor(0x6B6048).setBiomeName("dagorlad");
		nurn = new LOTRBiomeGenNurn(51).setTemperatureRainfall(0.9F, 0.4F).setMinMaxHeight(0.1F, 0.2F).setColor(0x28241B).setBiomeName("nurn");
		nurnen = new LOTRBiomeGenNurnen(52).setTemperatureRainfall(0.9F, 0.4F).setMinMaxHeight(-1F, 0.3F).setColor(0x153A58).setBiomeName("nurnen");
		eregionHills = new LOTRBiomeGenEregionHills(53).setTemperatureRainfall(0.6F, 0.6F).setMinMaxHeight(0.6F, 0.8F).setColor(0x4A7A5C).setBiomeName("eregionHills");
		brownLandsWoodlands = new LOTRBiomeGenBrownLandsWoodlands(54).setTemperatureRainfall(0.6F, 0.2F).setMinMaxHeight(0.2F, 0.2F).setColor(0x664933).setBiomeName("brownLandsWoodlands");
		angmarMountains = new LOTRBiomeGenAngmarMountains(55).setTemperatureRainfall(0.25F, 0.1F).setMinMaxHeight(1.6F, 1.5F).setColor(0x5E5E5E).setBiomeName("angmarMountains");
		enedwaithWoodlands = new LOTRBiomeGenEnedwaithWoodlands(56).setTemperatureRainfall(0.7F, 0.9F).setMinMaxHeight(0.2F, 0.4F).setColor(0x95A841).setBiomeName("enedwaithWoodlands");
		fangornMountains = new LOTRBiomeGenFangorn(57).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(1.5F, 1F).setColor(0x419343).setBiomeName("fangornMountains");
		fangornBirchForest = new LOTRBiomeGenFangorn(58).setBirchFangorn().setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.2F, 0.4F).setColor(0x5EA85E).setBiomeName("fangornBirchForest");
		mistyMountainsForest = new LOTRBiomeGenMistyMountainsForest(59).setTemperatureRainfall(0.2F, 0.6F).setMinMaxHeight(1.2F, 0.4F).setColor(0x94A69E).setBiomeName("mistyMountainsForest");
		fangornWasteland = new LOTRBiomeGenFangornWasteland(60).setTemperatureRainfall(0.7F, 0.4F).setMinMaxHeight(0.2F, 0.4F).setColor(0x6B7738).setBiomeName("fangornWasteland");
		rohanWoodlands = new LOTRBiomeGenRohanWoodlands(61).setTemperatureRainfall(0.9F, 0.5F).setMinMaxHeight(0.2F, 0.4F).setColor(0x839E3F).setBiomeName("rohanWoodlands");
		gondorWoodlands = new LOTRBiomeGenGondorWoodlands(62).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.2F, 0.2F).setColor(0xA6C444).setBiomeName("gondorWoodlands");
		lake = new LOTRBiomeGenLake(63).setColor(0x005DFF).setBiomeName("lake");
		beachStone = new LOTRBiomeGenBeach(64).setBeachBlock(Blocks.stone).setTemperatureRainfall(0.3F, 0.3F).setMinMaxHeight(0.1F, 0.8F).setColor(0x777777).setBiomeName("beachStone");
		barrowDowns = new LOTRBiomeGenBarrowDowns(65).setTemperatureRainfall(0.5F, 0.5F).setMinMaxHeight(0.2F, 0.6F).setColor(0x828E52).setBiomeName("barrowDowns");
		//EMPTY SLOT
		fangornClearing = new LOTRBiomeGenFangornClearing(67).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.2F, 0.1F).setColor(0x3DA53D).setBiomeName("fangornClearing");
		ithilienHills = new LOTRBiomeGenIthilienHills(68).setTemperatureRainfall(0.7F, 0.7F).setMinMaxHeight(0.6F, 0.6F).setColor(0x9CA548).setBiomeName("ithilienHills");
		ithilienWasteland = new LOTRBiomeGenIthilienWasteland(69).setTemperatureRainfall(0.6F, 0.6F).setMinMaxHeight(0.15F, 0.2F).setColor(0x9B985A).setBiomeName("ithilienWasteland");
		nindalf = new LOTRBiomeGenNindalf(70).setTemperatureRainfall(0.4F, 1F).setMinMaxHeight(0.1F, 0.1F).setColor(0x636340).setBiomeName("nindalf");
		coldfells = new LOTRBiomeGenColdfells(71).setTemperatureRainfall(0.25F, 0.8F).setMinMaxHeight(0.4F, 0.8F).setColor(0x827E58).setBiomeName("coldfells");
		rohanBoulderFields = new LOTRBiomeGenRohanBoulderFields(72).setTemperatureRainfall(1F, 0.2F).setMinMaxHeight(0.2F, 0.3F).setColor(0x918F51).setBiomeName("rohanBoulderFields");
		shireOrchard = new LOTRBiomeGenShireOrchard(73).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.2F, 0F).setColor(0x80B71B).setBiomeName("shireOrchard");
		wilderlandForest = new LOTRBiomeGenWilderlandForest(74).setTemperatureRainfall(0.9F, 0.7F).setMinMaxHeight(0.2F, 0.1F).setColor(0x6F8737).setBiomeName("wilderlandForest");
		swanfleet = new LOTRBiomeGenSwanfleet(75).setTemperatureRainfall(0.8F, 1F).setMinMaxHeight(0F, 0.1F).setColor(0x319B5C).setBiomeName("swanfleet");
		harondorShrubland = new LOTRBiomeGenHarondorShrubland(76).setTemperatureRainfall(1F, 0.6F).setMinMaxHeight(0.2F, 0.4F).setColor(0xA9AF52).setBiomeName("harondorShrubland");
		minhiriath = new LOTRBiomeGenMinhiriath(77).setTemperatureRainfall(0.7F, 0.4F).setMinMaxHeight(0.1F, 0.2F).setColor(0x7DAD5D).setBiomeName("minhiriath");
		minhiriathWoodlands = new LOTRBiomeGenMinhiriathWoodlands(78).setTemperatureRainfall(0.8F, 0.7F).setMinMaxHeight(0.1F, 0.3F).setColor(0x5D8E3D).setBiomeName("minhiriathWoodlands");
		minhiriathWasteland = new LOTRBiomeGenMinhiriathWasteland(79).setTemperatureRainfall(0.6F, 0.2F).setMinMaxHeight(0.1F, 0.1F).setColor(0xADAD87).setBiomeName("minhiriathWasteland");
		dunlandForest = new LOTRBiomeGenDunlandForest(80).setTemperatureRainfall(0.6F, 0.8F).setMinMaxHeight(0.3F, 0.5F).setColor(0x235928).setBiomeName("dunlandForest");
		nanUngol = new LOTRBiomeGenNanUngol(81).setTemperatureRainfall(2F, 0F).setMinMaxHeight(0.1F, 0.4F).setColor(0x1E1811).setBiomeName("nanUngol");
		gondorHills = new LOTRBiomeGenGondorHills(82).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.5F, 0.5F).setColor(0xB6B75B).setBiomeName("gondorHills");
		island = new LOTRBiomeGenOcean(83).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0F, 0.3F).setColor(0x39B57B).setBiomeName("island");
		forodwaithMountains = new LOTRBiomeGenForodwaithMountains(84).setTemperatureRainfall(0F, 0.2F).setMinMaxHeight(2F, 2F).setColor(0xD4EDF4).setBiomeName("forodwaithMountains");
		mistyMountainsFoothills = new LOTRBiomeGenMistyMountains(85).setTemperatureRainfall(0.25F, 0.6F).setMinMaxHeight(0.6F, 0.9F).setColor(0x8DB59D).setBiomeName("mistyMountainsFoothills");
		greyMountainsFoothills = new LOTRBiomeGenGreyMountains(86).setTemperatureRainfall(0.5F, 0.7F).setMinMaxHeight(0.5F, 0.9F).setColor(0x659165).setBiomeName("greyMountainsFoothills");
		blueMountainsFoothills = new LOTRBiomeGenBlueMountainsFoothills(87).setTemperatureRainfall(0.5F, 0.8F).setMinMaxHeight(0.5F, 0.9F).setColor(0x59AF7D).setBiomeName("blueMountainsFoothills");
		tundra = new LOTRBiomeGenTundra(88).setTemperatureRainfall(0.1F, 0.3F).setMinMaxHeight(0.1F, 0.2F).setColor(0xA5C9AA).setBiomeName("tundra");
		taiga = new LOTRBiomeGenTaiga(89).setTemperatureRainfall(0.1F, 0.7F).setMinMaxHeight(0.1F, 0.5F).setColor(0x5A8E4B).setBiomeName("taiga");
		breeland = new LOTRBiomeGenBreeland(90).setTemperatureRainfall(0.8F, 0.7F).setMinMaxHeight(0.1F, 0.2F).setColor(0x74B747).setBiomeName("breeland");
		chetwood = new LOTRBiomeGenChetwood(91).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.2F, 0.5F).setColor(0x78A02E).setBiomeName("chetwood");
		forodwaithGlacier = new LOTRBiomeGenForodwaithGlacier(92).setTemperatureRainfall(0F, 0.1F).setMinMaxHeight(1F, 0.1F).setColor(0x8FCCE0).setBiomeName("forodwaithGlacier");
		whiteMountainsFoothills = new LOTRBiomeGenWhiteMountains(93).setTemperatureRainfall(0.6F, 0.7F).setMinMaxHeight(0.5F, 0.9F).setColor(0xD2D693).setBiomeName("whiteMountainsFoothills");
		beach = new LOTRBiomeGenBeach(94).setBeachBlock(Blocks.sand).setColor(0xDBCA97).setBiomeName("beach");
		beachGravel = new LOTRBiomeGenBeach(95).setBeachBlock(Blocks.gravel).setColor(0x9695A0).setBiomeName("beachGravel");
		nearHarad = new LOTRBiomeGenNearHarad(96).setTemperatureRainfall(1.5F, 0.1F).setMinMaxHeight(0.2F, 0F).setColor(0xC4A66D).setBiomeName("nearHarad");
		farHarad = new LOTRBiomeGenFarHarad(97).setTemperatureRainfall(1.2F, 0.2F).setMinMaxHeight(0.2F, 0.25F).setColor(0x9BA574).setBiomeName("farHarad");
		haradMountains = new LOTRBiomeGenHaradMountains(98).setTemperatureRainfall(0.9F, 0.5F).setMinMaxHeight(1.8F, 2F).setColor(0x9E9381).setBiomeName("haradMountains");
		umbar = new LOTRBiomeGenUmbar(99).setTemperatureRainfall(0.7F, 0.5F).setMinMaxHeight(0.1F, 0.2F).setColor(0x7A7661).setBiomeName("umbar");
		farHaradJungle = new LOTRBiomeGenFarHaradJungle(100).setTemperatureRainfall(1.2F, 0.9F).setMinMaxHeight(0.2F, 0.4F).setColor(0x7F9137).setBiomeName("farHaradJungle");
		farHaradJungleHills = new LOTRBiomeGenFarHaradJungle(101).setTemperatureRainfall(1.2F, 0.9F).setMinMaxHeight(1.6F, 0.6F).setColor(0x8A994F).setBiomeName("farHaradJungleHills");
		nearHaradDunes = new LOTRBiomeGenNearHarad(102).setTemperatureRainfall(1.5F, 0.1F).setMinMaxHeight(0.5F, 0.9F).setColor(0xCEB073).setBiomeName("nearHaradDunes");
		nearHaradBoulderFields = new LOTRBiomeGenNearHaradBoulderFields(103).setTemperatureRainfall(1.5F, 0.1F).setMinMaxHeight(0.2F, 0.3F).setColor(0xA89A7E).setBiomeName("nearHaradBoulderFields");
		farHaradRiver = new LOTRBiomeGenFarHaradRiver(104).setMinMaxHeight(-0.5F, 0F).setColor(0x20BEE5).setBiomeName("farHaradRiver");
		farHaradForest = new LOTRBiomeGenFarHaradForest(105).setTemperatureRainfall(0.9F, 0.5F).setMinMaxHeight(0.4F, 0.6F).setColor(0x8E9964).setBiomeName("farHaradForest");
		nearHaradFertile = new LOTRBiomeGenNearHaradFertile(106).setTemperatureRainfall(1.2F, 0.7F).setMinMaxHeight(0.2F, 0.1F).setColor(0x9CA055).setBiomeName("nearHaradFertile");
		pertorogwaith = new LOTRBiomeGenPertorogwaith(107).setTemperatureRainfall(0.7F, 0.1F).setMinMaxHeight(0.2F, 0.5F).setColor(0x897A67).setBiomeName("pertorogwaith");
		eriadorWoodlandsDense = new LOTRBiomeGenEriadorWoodlandsDense(108).setTemperatureRainfall(0.7F, 0.9F).setMinMaxHeight(0.3F, 0.8F).setColor(0x557A3F).setBiomeName("eriadorWoodlandsDense");
		valesOfAnduinWoodlandsDense = new LOTRBiomeGenValesOfAnduinWoodlandsDense(109).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.3F, 0.8F).setColor(0x398E55).setBiomeName("valesOfAnduinWoodlandsDense");
		enedwaithWoodlandsDense = new LOTRBiomeGenEnedwaithWoodlandsDense(110).setTemperatureRainfall(0.7F, 0.9F).setMinMaxHeight(0.3F, 0.8F).setColor(0x82913F).setBiomeName("enedwaithWoodlandsDense");
		wilderlandForestDense = new LOTRBiomeGenWilderlandForestDense(111).setTemperatureRainfall(0.9F, 0.7F).setMinMaxHeight(0.3F, 0.8F).setColor(0x607235).setBiomeName("wilderlandForestDense");
		wilderlandHills = new LOTRBiomeGenWilderlandHills(112).setTemperatureRainfall(0.7F, 0.5F).setMinMaxHeight(0.5F, 0.5F).setColor(0x869652).setBiomeName("wilderlandHills");
		tolfalas = new LOTRBiomeGenTolfalas(113).setTemperatureRainfall(0.8F, 0.4F).setMinMaxHeight(0.3F, 1F).setColor(0x979978).setBiomeName("tolfalas");
		lebennin = new LOTRBiomeGenLebennin(114).setTemperatureRainfall(1F, 0.9F).setMinMaxHeight(0.1F, 0.4F).setColor(0x98C138).setBiomeName("lebennin");
		rhun = new LOTRBiomeGenRhun(115).setTemperatureRainfall(0.8F, 0.5F).setMinMaxHeight(0.3F, 0F).setColor(0xAEB269).setBiomeName("rhun");
		rhunForest = new LOTRBiomeGenRhunForest(116).setTemperatureRainfall(0.8F, 0.9F).setMinMaxHeight(0.3F, 0.5F).setColor(0x929646).setBiomeName("rhunForest");
		redMountains = new LOTRBiomeGenRedMountains(117).setTemperatureRainfall(0.3F, 0.4F).setMinMaxHeight(1.5F, 2F).setColor(0xA37B6E).setBiomeName("redMountains");
		redMountainsFoothills = new LOTRBiomeGenRedMountains(118).setTemperatureRainfall(0.7F, 0.4F).setMinMaxHeight(0.5F, 0.9F).setColor(0xA69C7B).setBiomeName("redMountainsFoothills");
	}
	
	private static Random rand = new Random();
	
	protected LOTRBiomeDecorator decorator;
	public List spawnableGoodList = new ArrayList();
	public List spawnableEvilList = new ArrayList();
	private int goodWeight;
	private int evilWeight;
	protected List spawnableLOTRAmbientList = new ArrayList();
	public boolean hasPodzol = false;
	private List spawnableTraders = new ArrayList();
	
	public LOTRBiome(int i)
	{
		super(i, false);
		lotrBiomeList[i] = this;
		
		decorator = new LOTRBiomeDecorator(this);
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableCaveCreatureList.clear();
		
		if (hasDomesticAnimals())
		{
			spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12, 4, 4));
			spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10, 4, 4));
			spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
			spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));
		}
		else
		{
			spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12, 4, 4));
			spawnableCreatureList.add(new SpawnListEntry(LOTREntityWildBoar.class, 10, 4, 4));
			spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
			spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));
		}
		
        spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
		
		spawnableEvilList.add(new SpawnListEntry(LOTREntityGundabadOrc.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityGundabadOrcArcher.class, 10, 4, 6));
		
		setGoodEvilWeight(0, 100);
		
        spawnableCaveCreatureList.add(new SpawnListEntry(EntityBat.class, 10, 8, 8));
	}
	
	public LOTRBiome setTemperatureRainfall(float f, float f1)
	{
		return (LOTRBiome)super.setTemperatureRainfall(f, f1);
	}
	
	public LOTRBiome setMinMaxHeight(float f, float f1)
	{
		f -= 2F;
		f += 0.2F;
		if (f == -2F)
		{
			f = -1.999F;
		}
		
		rootHeight = f;
		heightVariation = f1 / 2F;
		return this;
	}

	@Override
	public BiomeGenBase setColor(int i)
	{
		Object obj = LOTRGenLayerWorld.colorsToBiomeIDs.get(Integer.valueOf(i));
		if (obj != null)
		{
			throw new RuntimeException("LOTR biome (ID " + biomeID + ") is duplicating the color of another LOTR biome (ID " + ((Integer)obj).intValue() + ")");
		}
		
		LOTRGenLayerWorld.colorsToBiomeIDs.put(Integer.valueOf(i), Integer.valueOf(biomeID));
		return super.setColor(i);
	}
	
	public final String getBiomeDisplayName()
	{
		return StatCollector.translateToLocal("lotr.biome." + biomeName + ".name");
	}
	
	public FlowerEntry getRandomFlower(Random random)
	{
		return (FlowerEntry)WeightedRandom.getRandomItem(rand, flowers);
	}
	
	protected void registerPlainsFlowers()
	{
        flowers.clear();
        addFlower(Blocks.red_flower, 4, 3);
        addFlower(Blocks.red_flower, 5, 3);
        addFlower(Blocks.red_flower, 6, 3);
        addFlower(Blocks.red_flower, 7, 3);
        addFlower(Blocks.red_flower, 0, 20);
        addFlower(Blocks.red_flower, 3, 20);
        addFlower(Blocks.red_flower, 8, 20);
        addFlower(Blocks.yellow_flower, 0, 30);
	}
	
	protected void registerForestFlowers()
	{
		flowers.clear();
		addDefaultFlowers();
	}
	
	protected void registerJungleFlowers()
	{
		flowers.clear();
		addDefaultFlowers();
	}
	
	protected void registerMountainsFlowers()
	{
		flowers.clear();
		addDefaultFlowers();
		addFlower(Blocks.red_flower, 1, 10);
	}
	
	protected void registerTaigaFlowers()
	{
		flowers.clear();
		addDefaultFlowers();
		addFlower(Blocks.red_flower, 1, 10);
	}
	
	protected void registerSavannaFlowers()
	{
		flowers.clear();
		addDefaultFlowers();
	}
	
	protected void registerSwampFlowers()
	{
		flowers.clear();
		addDefaultFlowers();
	}
	
	protected void registerTravellingTrader(Class entityClass)
	{
		spawnableTraders.add(entityClass);
	}
	
	protected void clearTravellingTraders()
	{
		spawnableTraders.clear();
	}
	
	public boolean canSpawnTravellingTrader(Class entityClass)
	{
		return spawnableTraders.contains(entityClass);
	}
	
	protected boolean hasDomesticAnimals()
	{
		return false;
	}

	public boolean hasSky()
	{
		return true;
	}
	
	public LOTRAchievement getBiomeAchievement()
	{
		return null;
	}
	
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return null;
	}
	
	public boolean getEnableRiver()
	{
		return true;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        decorator.decorate(world, random, i, k);
    }
	
	protected void setGoodEvilWeight(int good, int evil)
	{
		goodWeight = good;
		evilWeight = evil;
	}
	
    public List getNPCSpawnList()
    {
		if (rand.nextInt(goodWeight + evilWeight) < goodWeight)
		{
			return spawnableGoodList;
		}
		else
		{
			return spawnableEvilList;
		}
	}
	
	@Override
    public List getSpawnableList(EnumCreatureType creatureType)
    {
		if (creatureType == creatureType_LOTRAmbient)
		{
			return spawnableLOTRAmbientList;
		}
		return super.getSpawnableList(creatureType);
	}
	
    protected void genStandardOre(World world, Random random, int chunkX, int chunkZ, int count, WorldGenerator oreGen, int offset, int heightRange)
    {
        for (int l = 0; l < count; l++)
        {
            int i = chunkX + random.nextInt(16);
            int j = random.nextInt(heightRange - offset) + offset;
            int k = chunkZ + random.nextInt(16);
            oreGen.generate(world, random, i, j, k);
        }
    }

	public float getChanceToSpawnAnimals()
	{
		return 1F;
	}
	
	public boolean canSpawnHostilesInDay()
	{
		return false;
	}
	
	public float getChanceToSpawnLakes()
	{
		return 1F;
	}
	
	public float getChanceToSpawnLavaLakes()
	{
		return 0.1F;
	}
	
	@Override
	public WorldGenAbstractTree func_150567_a(Random random)
	{
		return random.nextInt(10) == 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false);
	}
	
	public float getTreeIncreaseChance()
	{
		return 0.1F;
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random)
	{
		if (decorator.enableFern && random.nextInt(4) == 0)
		{
			return new WorldGenTallGrass(Blocks.tallgrass, 2);
		}
		return new WorldGenTallGrass(Blocks.tallgrass, 1);
	}
	
	public WorldGenerator getRandomWorldGenForDoubleGrass(Random random)
	{
		WorldGenDoublePlant generator = new WorldGenDoublePlant();
		if (decorator.enableFern && random.nextInt(4) == 0)
		{
			generator.func_150548_a(3);
		}
		else
		{
			generator.func_150548_a(2);
		}
		return generator;
	}
	
	public WorldGenerator getRandomWorldGenForDoubleFlower(Random random)
	{
        WorldGenDoublePlant doubleFlowerGen = new WorldGenDoublePlant();
        int i = random.nextInt(3);
        switch (i)
        {
        	case 0:
        		doubleFlowerGen.func_150548_a(1);
        		break;
        	case 1:
        		doubleFlowerGen.func_150548_a(4);
        		break;
        	case 2:
        		doubleFlowerGen.func_150548_a(5);
        		break;
        }
        return doubleFlowerGen;
	}
	
	@Override
	public boolean canSpawnLightningBolt()
	{
		return !getEnableSnow() && super.canSpawnLightningBolt();
	}
	
	@Override
	public boolean getEnableSnow()
	{
		if (LOTRMod.isChristmas() && LOTRMod.proxy.isClient())
		{
			return true;
		}
		return super.getEnableSnow();
	}
	
	public int getSnowHeight()
	{
		return 0;
	}
	
	public Vec3 getCloudColor(Vec3 clouds)
	{
		return clouds;
	}
	
	public Vec3 getFogColor(Vec3 fog)
	{
		return fog;
	}
	
	public int spawnCountMultiplier()
	{
		return 1;
	}
	
	@Override
    public BiomeGenBase createMutation()
    {
        return this;
    }
}
