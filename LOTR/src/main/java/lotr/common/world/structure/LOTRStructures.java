package lotr.common.world.structure;

import java.util.HashMap;
import java.util.LinkedHashMap;

import lotr.common.world.mapgen.dwarvenmine.LOTRStructureDwarvenMinePieces;
import lotr.common.world.structure2.*;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRStructures
{
	private static HashMap<Integer, Class> idToClassMapping = new HashMap();
	private static HashMap<Integer, String> idToStringMapping = new HashMap();
	public static HashMap<Integer, StructureInfo> structureSpawners = new LinkedHashMap();
	
	private static void registerStructure(int id, Class structureClass, String name, int background, int foreground)
	{
		idToClassMapping.put(id, structureClass);
		idToStringMapping.put(id, name);
		structureSpawners.put(id, new StructureInfo(id, background, foreground));
	}
	
	public static WorldGenerator getStructureFromID(int ID)
	{
		WorldGenerator generator = null;
		try
		{
			Class structureClass = idToClassMapping.get(ID);
			if (structureClass != null)
			{
				generator = (WorldGenerator)structureClass.getConstructor(new Class[] {boolean.class}).newInstance(new Object[] {true});
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return generator;
	}
	
	public static String getNameFromID(int ID)
	{
		return idToStringMapping.get(ID);
	}
	
	public static void registerStructures()
	{
		registerStructure(1, LOTRWorldGenHobbitHole.class, "HobbitHole", 0x29A029, 0x89492C);
		registerStructure(2, LOTRWorldGenHobbitTavern.class, "HobbitTavern", 0x8E4631, 0xF3C57F);
		registerStructure(3, LOTRWorldGenHobbitPicnicBench.class, "HobbitPicnicBench", 0x6B4F2E, 0xD3D3D3);
		registerStructure(4, LOTRWorldGenHobbitWindmill.class, "HobbitWindmill", 0x8E4631, 0xF3C57F);
		registerStructure(5, LOTRWorldGenHobbitFarm.class, "HobbitFarm", 0x8E4631, 0xF3C57F);
		
		registerStructure(20, LOTRWorldGenBlueMountainsHouse.class, "BlueMountainsHouse", 0x9EA6C4, 0x747B97);
		registerStructure(21, LOTRWorldGenBlueMountainsStronghold.class, "BlueMountainsStronghold", 0x9EA6C4, 0x747B97);
		
		registerStructure(30, LOTRWorldGenElvenTurret.class, "ElvenTurret", 0xA69F93, 0x7A746A);
		registerStructure(31, LOTRWorldGenRuinedElvenTurret.class, "RuinedElvenTurret", 0xA69F93, 0x7A746A);
		registerStructure(32, LOTRWorldGenHighElvenHall.class, "HighElvenHall", 0xA69F93, 0x7A746A);
		registerStructure(33, LOTRWorldGenUnderwaterElvenRuin.class, "UnderwaterElvenRuin", 0xA69F93, 0x7A746A);
		
		registerStructure(50, LOTRWorldGenRuinedDunedainTower.class, "RuinedDunedainTower", 0x888888, 0x5C5C5C);
		registerStructure(51, LOTRWorldGenRuinedHouse.class, "RuinedHouse", 0x7F7D7D, 0x685A3D);
		registerStructure(52, LOTRWorldGenRangerTent.class, "RangerTent", 0x394C1D, 0x3F341F);
		registerStructure(53, LOTRWorldGenNumenorRuin.class, "NumenorRuin", 0x888888, 0x5C5C5C);
		registerStructure(54, LOTRWorldGenBDBarrow.class, "BDBarrow", 0x647F5A, 0x63453A);
		registerStructure(55, LOTRWorldGenRangerWatchtower.class, "RangerWatchtower", 0x5B482C, 0xCCA46C);
		
		registerStructure(80, LOTRWorldGenOrcDungeon.class, "OrcDungeon", 0x888888, 0x5C5C5C);
		registerStructure(81, LOTRWorldGenGundabadTent.class, "GundabadTent", 0x231D1A, 0x020202);
		registerStructure(82, LOTRWorldGenGundabadForgeTent.class, "GundabadForgeTent", 0x231D1A, 0x020202);
		
		registerStructure(100, LOTRWorldGenAngmarTower.class, "AngmarTower", 0x3A3A3A, 0x191919);
		registerStructure(101, LOTRWorldGenAngmarShrine.class, "AngmarShrine", 0x3A3A3A, 0x191919);
		
		registerStructure(120, LOTRWorldGenWoodElfPlatform.class, "WoodElfLookoutPlatform", 0x262118, 0x4B4335);
		registerStructure(121, LOTRWorldGenWoodElfHouse.class, "WoodElfHouse", 0x262118, 0x0F541E);
		registerStructure(122, LOTRWorldGenWoodElfTower.class, "WoodElfTower", 0xA69F93, 0x4B4335);
		registerStructure(123, LOTRWorldGenRuinedWoodElfTower.class, "RuinedWoodElfTower", 0xA69F93, 0x7A746A);
		
		registerStructure(130, LOTRWorldGenDolGuldurAltar.class, "DolGuldurAltar", 0x43454E, 0x1F2125);
		registerStructure(131, LOTRWorldGenDolGuldurTower.class, "DolGuldurTower", 0x43454E, 0x1F2125);
		
		registerStructure(150, LOTRWorldGenDwarvenMineEntrance.class, "DwarvenMineEntrance", 0x44514B, 0x25302C);
		registerStructure(151, LOTRWorldGenDwarvenTower.class, "DwarvenTower", 0x44514B, 0x25302C);
		registerStructure(152, LOTRWorldGenDwarfHouse.class, "DwarfHouse", 0x44514B, 0x25302C);
		
		registerStructure(200, LOTRWorldGenElfHouse.class, "ElfHouse", 0xE9D9AF, 0x235621);
		registerStructure(201, LOTRWorldGenElfLordHouse.class, "ElfLordHouse", 0xE9D9AF, 0x235621);
		
		registerStructure(300, LOTRWorldGenMeadHall.class, "RohanMeadHall", 0x5B482C, 0xCCA46C);
		registerStructure(301, LOTRWorldGenRohanWatchtower.class, "RohanWatchtower", 0x5B482C, 0xCCA46C);
		registerStructure(302, LOTRWorldGenRohanBarrow.class, "RohanBarrow", 0x899345, 0xFFFADD);
		registerStructure(303, LOTRWorldGenRohanFortress.class, "RohanFortress", 0x5B482C, 0xCCA46C);
		
		registerStructure(350, LOTRWorldGenUrukTent.class, "UrukTent", 0x231D1A, 0x020202);
		registerStructure(351, LOTRWorldGenRuinedRohanWatchtower.class, "RuinedRohanWatchtower", 0x110D09, 0x322D25);
		registerStructure(352, LOTRWorldGenUrukForgeTent.class, "UrukForgeTent", 0x383124, 0x1F1B13);
		registerStructure(353, LOTRWorldGenUrukWargPit.class, "UrukWargPit", 0x383124, 0x1F1B13);
		
		registerStructure(380, LOTRWorldGenDunlendingHouse.class, "DunlendingHouse", 0x665139, 0x3A2F22);
		registerStructure(381, LOTRWorldGenDunlendingTavern.class, "DunlendingTavern", 0x665139, 0x3A2F22);
		registerStructure(382, LOTRWorldGenDunlendingCampfire.class, "DunlendingCampfire", 0x918F90, 0x685433);
		registerStructure(383, LOTRWorldGenDunlandHillFort.class, "DunlandHillFort", 0x665139, 0x3A2F22);
		
		registerStructure(400, LOTRWorldGenBeaconTower.class, "BeaconTower", 0xDBDBDB, 0xAFAFAF);
		registerStructure(401, LOTRWorldGenGondorFortress.class, "GondorFortress", 0xDBDBDB, 0xAFAFAF);
		registerStructure(402, LOTRWorldGenGondorSmithy.class, "GondorSmithy", 0xDBDBDB, 0xAFAFAF);
		registerStructure(403, LOTRWorldGenGondorTurret.class, "GondorTurret", 0xDBDBDB, 0xAFAFAF);
		
		registerStructure(420, LOTRWorldGenRuinedBeaconTower.class, "RuinedBeaconTower", 0xDBDBDB, 0xAFAFAF);
		registerStructure(421, LOTRWorldGenRuinedGondorTower.class, "RuinedGondorTower", 0xDBDBDB, 0xAFAFAF);
		registerStructure(422, LOTRWorldGenGondorObelisk.class, "GondorObelisk", 0xDBDBDB, 0xAFAFAF);
		registerStructure(423, LOTRWorldGenGondorRuin.class, "GondorRuin", 0xDBDBDB, 0xAFAFAF);
		
		registerStructure(500, LOTRWorldGenMordorTower.class, "MordorTower", 0x282828, 0x050505);
		registerStructure(501, LOTRWorldGenMordorTent.class, "MordorTent", 0x5B3A23, 0x161616);
		registerStructure(502, LOTRWorldGenMordorForgeTent.class, "MordorForgeTent", 0x282828, 0x050505);
		registerStructure(503, LOTRWorldGenMordorWargPit.class, "MordorWargPit", 0x282828, 0x050505);
		
		registerStructure(550, LOTRWorldGenNurnWheatFarm.class, "NurnWheatFarm", 0x443424, 0x050505);
		registerStructure(551, LOTRWorldGenOrcSlaverTower.class, "OrcSlaverTower", 0x110D09, 0x322D25);
		
		registerStructure(570, LOTRWorldGenMordorSpiderPit.class, "MordorSpiderPit", 0x170F0D, 0xC51B1E);
		
		registerStructure(600, LOTRWorldGenHaradObelisk.class, "HaradObelisk", 0xA59E77, 0xEDE4AF);
		registerStructure(601, LOTRWorldGenNearHaradHouse.class, "NearHaradHouse", 0xE5D9B7, 0xA06248);
		registerStructure(602, LOTRWorldGenNearHaradLargeHouse.class, "NearHaradLargeHouse", 0xE5D9B7, 0xA06248);
		registerStructure(603, LOTRWorldGenNearHaradVillage.class, "NearHaradVillage", 0xE5D9B7, 0xA06248);
		registerStructure(604, LOTRWorldGenNearHaradTower.class, "NearHaradTower", 0xE5D9B7, 0xA06248);
		registerStructure(605, LOTRWorldGenNearHaradFortress.class, "NearHaradFortress", 0xE5D9B7, 0xA06248);
		registerStructure(606, LOTRWorldGenNearHaradTent.class, "NearHaradTent", 0xCE4942, 0x1B1919);
		registerStructure(607, LOTRWorldGenNearHaradBazaar.class, "NearHaradBazaar", 0xCE4942, 0x1B1919);
		registerStructure(608, LOTRWorldGenHaradPyramid.class, "HaradPyramid", 0xA59E77, 0xEDE4AF);
		
		LOTRStructureDwarvenMinePieces.register();
	}
	
    public static class StructureInfo
    {
        public final int spawnedID;
        public final int primaryColor;
        public final int secondaryColor;

        public StructureInfo(int i, int j, int k)
        {
            spawnedID = i;
            primaryColor = j;
            secondaryColor = k;
        }
    }
}
