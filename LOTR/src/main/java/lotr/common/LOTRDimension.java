package lotr.common;

import java.util.*;

import lotr.common.world.*;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

public enum LOTRDimension
{
	MIDDLE_EARTH("MiddleEarth", 100, LOTRWorldProviderMiddleEarth.class, true, 100),
	UTUMNO("Utumno", 101, LOTRWorldProviderUtumno.class, true, 300);
	
	public String dimensionName;
	private int defaultID;
	public int dimensionID;
	private Class providerClass;
	private boolean loadSpawn;
	
	public LOTRBiome[] biomeList = new LOTRBiome[256];
	public Map<Integer, Integer> colorsToBiomeIDs = new HashMap();
	
	public List<LOTRAchievement.Category> achievementCategories = new ArrayList();
	public List<LOTRAchievement> allAchievements = new ArrayList();
	
	public List<LOTRFaction> factionList = new ArrayList();
	
	public int maxSpawnedNPCs;
	
	private LOTRDimension(String s, int i, Class c, boolean flag, int spawns)
	{
		dimensionName = s;
		defaultID = i;
		providerClass = c;
		loadSpawn = flag;
		maxSpawnedNPCs = spawns;
	}
	
	public String getDimensionName()
	{
		return StatCollector.translateToLocal("lotr.dimension." + dimensionName);
	}
	
	public static void configureDimensions(Configuration config)
	{
		for (LOTRDimension dim : values())
		{
			dim.dimensionID = config.get(Configuration.CATEGORY_GENERAL, dim.dimensionName + " Dimension ID", dim.defaultID).getInt();
		}
	}
	
	public static void registerDimensions()
	{
		for (LOTRDimension dim : values())
		{
			DimensionManager.registerProviderType(dim.dimensionID, dim.providerClass, dim.loadSpawn);
			DimensionManager.registerDimension(dim.dimensionID, dim.dimensionID);
		}
	}
	
	public static LOTRDimension getCurrentDimension(World world)
	{
		if (world != null)
		{
			WorldProvider provider = world.provider;
			if (provider instanceof LOTRWorldProvider)
			{
				return ((LOTRWorldProvider)provider).getLOTRDimension();
			}
		}
		return LOTRDimension.MIDDLE_EARTH;
	}
}
