package lotr.common.item;

import java.util.List;

import lotr.common.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemRedBook extends Item
{
	public LOTRItemRedBook()
	{
		super();
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (!world.isRemote)
		{
			//entityplayer.openGui(LOTRMod.instance, LOTRMod.proxy.GUI_ID_RED_BOOK, world, 0, 0, 0);
		}
		return itemstack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
		LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
		int activeQuests = playerData.getActiveMiniQuests().size();
		list.add(StatCollector.translateToLocalFormatted("item.lotr.redBook.activeQuests", new Object[] {activeQuests}));
    }
}
