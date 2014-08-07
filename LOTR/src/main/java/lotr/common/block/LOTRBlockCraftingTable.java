package lotr.common.block;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LOTRBlockCraftingTable extends Block
{
	private LOTRFaction tableFaction;
	private int tableGUIID;
	
	public LOTRBlockCraftingTable(Material material, LOTRFaction faction, int guiID)
	{
		super(material);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		tableFaction = faction;
		tableGUIID = guiID;
	}
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		if (LOTRLevelData.getData(entityplayer).getAlignment(tableFaction) >= LOTRAlignmentValues.USE_TABLE)
		{
			if (!world.isRemote)
			{
				entityplayer.openGui(LOTRMod.instance, tableGUIID, world, i, j, k);
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
				LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, LOTRAlignmentValues.USE_TABLE, tableFaction);
			}
		}
		return true;
    }
}
