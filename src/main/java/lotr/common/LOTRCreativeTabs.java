package lotr.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRCreativeTabs extends CreativeTabs
{
	public static LOTRCreativeTabs tabBlock = new LOTRCreativeTabs("blocks");
	public static LOTRCreativeTabs tabDeco = new LOTRCreativeTabs("decorations");
	public static LOTRCreativeTabs tabFood = new LOTRCreativeTabs("food");
	public static LOTRCreativeTabs tabMaterials = new LOTRCreativeTabs("materials");
	public static LOTRCreativeTabs tabMisc = new LOTRCreativeTabs("misc");
	public static LOTRCreativeTabs tabTools = new LOTRCreativeTabs("tools");
	public static LOTRCreativeTabs tabCombat = new LOTRCreativeTabs("combat");
	public static LOTRCreativeTabs tabMagic = new LOTRCreativeTabs("magic");
	public static LOTRCreativeTabs tabSpawn = new LOTRCreativeTabs("spawning");
	
	public ItemStack theIcon;
	
	public LOTRCreativeTabs(String label)
	{
		super(label);
	}
	
	public static void setupIcons()
	{
		tabBlock.theIcon = new ItemStack(LOTRMod.brick, 1, 6);
		tabDeco.theIcon = new ItemStack(LOTRMod.beacon);
		tabFood.theIcon = new ItemStack(LOTRMod.lembas);
		tabMaterials.theIcon = new ItemStack(LOTRMod.mithril);
		tabMisc.theIcon = new ItemStack(LOTRMod.hobbitPipe);
		tabTools.theIcon = new ItemStack(LOTRMod.pickaxeOrc);
		tabCombat.theIcon = new ItemStack(LOTRMod.swordGondor);
		tabMagic.theIcon = new ItemStack(LOTRMod.goldRing);
		tabSpawn.theIcon = new ItemStack(LOTRMod.spawnEgg, 1, 55);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel()
    {
        return StatCollector.translateToLocal("lotr.creativetab." + getTabLabel());
    }
	
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return theIcon.getItem();
    }
    
	@Override
    public ItemStack getIconItemStack()
    {
        return theIcon;
    }
}
