package lotr.common;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToFindFieldException;

public class LOTRReflection
{
	private static void logFailure(Exception e)
	{
		System.out.println("LOTRReflection failed");
		throw new RuntimeException(e);
	}
	
    private static String[] remapMethodNames(String className, String... methodNames)
    {
        String internalClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(className.replace('.', '/'));
        String[] mappedNames = new String[methodNames.length];
        int i = 0;
        for (String mName : methodNames)
        {
            mappedNames[i++] = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(internalClassName, mName, null);
        }
        return mappedNames;
    }
    
    public static <E> Method getPrivateMethod(Class<? super E> classToAccess, E instance, Class[] methodClasses, String... methodNames)
    {
        try
        {
            return ReflectionHelper.findMethod(classToAccess, instance, remapMethodNames(classToAccess.getName(), methodNames), methodClasses);
        }
        catch (UnableToFindFieldException e)
        {
            FMLLog.log(Level.ERROR,e,"Unable to locate any method %s on type %s", Arrays.toString(methodNames), classToAccess.getName());
            throw e;
        }
        catch (UnableToAccessFieldException e)
        {
            FMLLog.log(Level.ERROR, e, "Unable to access any method %s on type %s", Arrays.toString(methodNames), classToAccess.getName());
            throw e;
        }
    }
    
    public static void testAll()
    {
    	// setWorldInfo will be tested automatically
    	getHorseJumpStrength();
    	getStackList(new InventoryCrafting(new ContainerChest(new InventoryBasic("test", false, 1), new InventoryBasic("test", false, 1)), 1, 1));
    	getStemFruitBlock((BlockStem)Blocks.melon_stem);
    	getCropItem((BlockCrops)Blocks.potatoes);
    	isBadEffect(Potion.poison);
    }
    
    public static void setWorldInfo(World world, WorldInfo newWorldInfo)
    {
    	try
		{
			ObfuscationReflectionHelper.setPrivateValue(World.class, world, newWorldInfo, "worldInfo", "field_72986_A");
		}
		catch (Exception e)
		{
			logFailure(e);
		}
    }
	
	public static IAttribute getHorseJumpStrength()
	{
		try
		{
			return ObfuscationReflectionHelper.getPrivateValue(EntityHorse.class, null, "horseJumpStrength", "field_110271_bv");
		}
		catch (Exception e)
		{
			logFailure(e);
			return null;
		}
	}
	
	public static ItemStack[] getStackList(InventoryCrafting inv)
	{
		try
		{
			return ObfuscationReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, "stackList", "field_70466_a");
		}
		catch (Exception e)
		{
			logFailure(e);
			return null;
		}
	}
	
	public static Block getStemFruitBlock(BlockStem block)
	{
		try
		{
			return ObfuscationReflectionHelper.getPrivateValue(BlockStem.class, block, "field_149877_a");
		}
		catch (Exception e)
		{
			logFailure(e);
			return null;
		}
	}
	
	public static Item getCropItem(BlockCrops block)
	{
		try
		{
			Method method = getPrivateMethod(BlockCrops.class, block, new Class[0], "func_149865_P");
			return (Item)method.invoke(block, new Object[0]);
		}
		catch (Exception e)
		{
			logFailure(e);
			return null;
		}
	}
	
	public static boolean isBadEffect(Potion potion)
	{
		try
		{
			return ObfuscationReflectionHelper.getPrivateValue(Potion.class, potion, "isBadEffect", "field_76418_K");
		}
		catch (Exception e)
		{
			logFailure(e);
			return false;
		}
	}
}
