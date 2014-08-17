package lotr.common.item;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockGuldurilBrick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemGuldurilCrystal extends Item
{
	public LOTRItemGuldurilCrystal()
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
		
		int guldurilBrickMeta = LOTRBlockGuldurilBrick.guldurilMetaForBlock(world.getBlock(i, j, k), world.getBlockMetadata(i, j, k));
		boolean hasAlignment = LOTRLevelData.getData(entityplayer).getAlignment(LOTRFaction.MORDOR) >= LOTRAlignmentValues.Levels.USE_PORTAL || LOTRLevelData.getData(entityplayer).getAlignment(LOTRFaction.ANGMAR) >= LOTRAlignmentValues.Levels.USE_PORTAL;
		
		if (guldurilBrickMeta >= 0)
		{
			if (hasAlignment)
			{
				world.setBlock(i, j, k, LOTRMod.guldurilBrick, guldurilBrickMeta, 3);
				itemstack.stackSize--;
				for (int l = 0; l < 16; l++)
				{
					double d = i - 0.25D + (double)world.rand.nextFloat() * 1.5D;
					double d1 = j - 0.25D + (double)world.rand.nextFloat() * 1.5D;
					double d2 = k - 0.25D + (double)world.rand.nextFloat() * 1.5D;
					world.spawnParticle("iconcrack_" + Item.getIdFromItem(this), d, d1, d2, 0D, 0D, 0D);
				}
			}
			else
			{
				for (int l = 0; l < 8; l++)
				{
					double d = i - 0.25D + (double)world.rand.nextFloat() * 1.5D;
					double d1 = j - 0.25D + (double)world.rand.nextFloat() * 1.5D;
					double d2 = k - 0.25D + (double)world.rand.nextFloat() * 1.5D;
					world.spawnParticle("smoke", d, d1, d2, 0D, 0D, 0D);
				}
				if (!world.isRemote)
				{
					LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, LOTRAlignmentValues.Levels.USE_PORTAL, LOTRFaction.MORDOR, LOTRFaction.ANGMAR);
				}
			}
			
			return true;
		}
		
		return false;
	}
}
