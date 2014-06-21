package lotr.common.item;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemQuenditeCrystal extends Item
{
	public LOTRItemQuenditeCrystal()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabMaterials);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
	{
		if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack))
		{
			return false;
		}
		
		if (world.getBlock(i, j, k) == Blocks.grass)
		{
			if (LOTRLevelData.getAlignment(entityplayer, LOTRFaction.GALADHRIM) >= LOTRAlignmentValues.USE_PORTAL)
			{
				world.setBlock(i, j, k, LOTRMod.quenditeGrass, 0, 3);
				itemstack.stackSize--;
				for (int l = 0; l < 8; l++)
				{
					world.spawnParticle("iconcrack_" + Item.getIdFromItem(this), i + (double)world.rand.nextFloat(), j + 1.5D, k + (double)world.rand.nextFloat(), 0D, 0D, 0D);
				}
			}
			else
			{
				for (int l = 0; l < 8; l++)
				{
					double d = i + (double)world.rand.nextFloat();
					double d1 = j + 1D;
					double d2 = k + (double)world.rand.nextFloat();
					world.spawnParticle("smoke", d, d1, d2, 0D, 0D, 0D);
				}
				if (!world.isRemote)
				{
					LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, LOTRAlignmentValues.USE_PORTAL, LOTRFaction.GALADHRIM);
				}
			}
			return true;
		}
		
		return false;
	}
}
