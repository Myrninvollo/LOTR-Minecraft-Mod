package lotr.common.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import lotr.common.LOTRMod;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;

public class LOTREntities
{
	public static HashMap creatures = new LinkedHashMap();
	private static Map summonNamesToIDMapping = new HashMap();
	public static Map stringToIDMapping = new HashMap();
	public static Map stringToClassMapping = new HashMap();

	public static void registerCreature(Class entityClass, String name, int id, int eggBackground, int eggSpots)
	{
		registerEntity(entityClass, name, id, 80, 3, true);
		creatures.put(Integer.valueOf(id), new SpawnEggInfo(id, eggBackground, eggSpots));
	}
	
	public static void registerCreature(Class entityClass, String name, int id)
	{
		registerEntity(entityClass, name, id, 80, 3, true);
	}

	public static void registerEntity(Class entityClass, String name, int id, int updateRange, int updateFreq, boolean sendVelocityUpdates)
	{
		EntityRegistry.registerModEntity(entityClass, name, id, LOTRMod.instance, updateRange, updateFreq, sendVelocityUpdates);
		summonNamesToIDMapping.put(getFullEntityName(name), id);
		stringToIDMapping.put(name, id);
		stringToClassMapping.put(name, entityClass);
	}
	
	public static String getFullEntityName(String name)
	{
		ModContainer container = FMLCommonHandler.instance().findContainerFor(LOTRMod.instance);
		return String.format("%s.%s", container.getModId(), name);
	}
	
	public static int getEntityID(Entity entity)
	{
		EntityRegistry.EntityRegistration registry = EntityRegistry.instance().lookupModSpawn(entity.getClass(), false);
		if (registry != null)
		{
			return registry.getModEntityId();
		}
		return 0;
	}
	
	public static int getEntityIDFromClass(Class entityClass)
	{
		EntityRegistry.EntityRegistration registry = EntityRegistry.instance().lookupModSpawn(entityClass, false);
		if (registry != null)
		{
			return registry.getModEntityId();
		}
		return 0;
	}
	
	public static String getStringFromClass(Class entityClass)
	{
		EntityRegistry.EntityRegistration registry = EntityRegistry.instance().lookupModSpawn(entityClass, false);
		if (registry != null)
		{
			return registry.getEntityName();
		}
		return "";
	}
	
	public static int getIDFromString(String name)
	{
		Object obj = stringToIDMapping.get(name);
		if (obj instanceof Integer)
		{
			return (Integer)obj;
		}
		return 0;
	}
	
	public static Class getClassFromString(String name)
	{
		return (Class)stringToClassMapping.get(name);
	}
	
	public static Entity createEntityByID(int id, World world)
	{
		Entity entity = null;
		try
		{
			ModContainer container = FMLCommonHandler.instance().findContainerFor(LOTRMod.instance);
			EntityRegistry.EntityRegistration registry = EntityRegistry.instance().lookupModSpawn(container, id);
			Class entityClass = registry.getEntityClass();

			if (entityClass != null)
			{
				entity = (Entity)entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world});
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (entity == null)
		{
			System.out.println("Skipping LOTR Entity with id " + id);
		}
		return entity;
	}
	
	public static Entity createEntityByClass(Class entityClass, World world)
	{
		Entity entity = null;
		try
		{
			entity = (Entity)entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (entity == null)
		{
			System.out.println("Skipping LOTR Entity with class " + entityClass);
		}
		return entity;
	}
	
	public static String getStringFromID(int id)
	{
		ModContainer container = FMLCommonHandler.instance().findContainerFor(LOTRMod.instance);
		EntityRegistry.EntityRegistration registry = EntityRegistry.instance().lookupModSpawn(container, id);
		if (registry != null)
		{
			return registry.getEntityName();
		}
		return "";
	}
	
	public static Class getClassFromID(int id)
	{
		ModContainer container = FMLCommonHandler.instance().findContainerFor(LOTRMod.instance);
		EntityRegistry.EntityRegistration registry = EntityRegistry.instance().lookupModSpawn(container, id);
		if (registry != null)
		{
			return registry.getEntityClass();
		}
		return null;
	}
	
    public static Set getSummonNames()
    {
        return Collections.unmodifiableSet(summonNamesToIDMapping.keySet());
    }
	
    public static class SpawnEggInfo
    {
        public final int spawnedID;
        public final int primaryColor;
        public final int secondaryColor;

        public SpawnEggInfo(int i, int j, int k)
        {
            spawnedID = i;
            primaryColor = j;
            secondaryColor = k;
        }
    }
}
