package lotr.common.item;

import java.util.List;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemCommandHorn extends Item
{
	public LOTRItemCommandHorn()
	{
		super();
        setHasSubtypes(true);
        setMaxDamage(0);
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
	}
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (!world.isRemote)
		{
			List entities = world.loadedEntityList;
			for (int l = 0; l < entities.size(); l++)
			{
				if (entities.get(l) instanceof LOTREntityNPC)
				{
					LOTREntityNPC npc = (LOTREntityNPC)entities.get(l);
					if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == entityplayer)
					{
						if (itemstack.getItemDamage() == 1 && npc.hiredNPCInfo.getObeyHornHaltReady())
						{
							npc.hiredNPCInfo.halt();
						}
						else if (itemstack.getItemDamage() == 2 && npc.hiredNPCInfo.getObeyHornHaltReady())
						{
							npc.hiredNPCInfo.ready();
						}
						else if (itemstack.getItemDamage() == 3 && npc.hiredNPCInfo.getObeyHornSummon())
						{
							npc.hiredNPCInfo.tryTeleportToHiringPlayer();
						}
					}
				}
			}
		}
		
		if (itemstack.getItemDamage() == 1)
		{
			itemstack.setItemDamage(2);
		}
		else if (itemstack.getItemDamage() == 2)
		{
			itemstack.setItemDamage(1);
		}
		
		world.playSoundAtEntity(entityplayer, "lotr:item.horn", 4F, 1F);
        return itemstack;
	}

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 40;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (itemstack.getItemDamage() == 0)
		{
			entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_HORN_SELECT, world, 0, 0, 0);
		}
		else
		{
			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}
        return itemstack;
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
		String s = "";
		if (itemstack.getItemDamage() == 1)
		{
			s = ".halt";
		}
		else if (itemstack.getItemDamage() == 2)
		{
			s = ".ready";
		}
		else if (itemstack.getItemDamage() == 3)
		{
			s = ".summon";
		}
		return super.getUnlocalizedName(itemstack) + s;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j <= 3; j++)
        {
            list.add(new ItemStack(item, 1, j));
        }
    }
}
