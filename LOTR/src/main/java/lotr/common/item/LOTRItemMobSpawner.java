package lotr.common.item;

import java.util.Iterator;
import java.util.List;

import lotr.common.entity.LOTREntities;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemMobSpawner extends ItemBlock
{
	public LOTRItemMobSpawner(Block block)
	{
		super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
	}
	
	@Override
    public int getMetadata(int i)
    {
        return 0;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        Iterator it = LOTREntities.creatures.values().iterator();
        while (it.hasNext())
        {
        	LOTREntities.SpawnEggInfo info = (LOTREntities.SpawnEggInfo)it.next();
            list.add(new ItemStack(item, 1, info.spawnedID));
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
	{
		String entityName = LOTREntities.getStringFromID(itemstack.getItemDamage());
		list.add(LOTREntities.getFullEntityName(entityName));
	}
	
	@Override
    public boolean placeBlockAt(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2, int metadata)
    {
		if (super.placeBlockAt(itemstack, entityplayer, world, i, j, k, side, f, f1, f2, metadata))
		{
			TileEntity tileentity = world.getTileEntity(i, j, k);
			if (tileentity != null && tileentity instanceof LOTRTileEntityMobSpawner)
			{
				((LOTRTileEntityMobSpawner)tileentity).setMobID(itemstack.getItemDamage());
				((LOTRTileEntityMobSpawner)tileentity).spawnsPersistentNPCs = true;
			}
			return true;
		}
		return false;
    }
}
