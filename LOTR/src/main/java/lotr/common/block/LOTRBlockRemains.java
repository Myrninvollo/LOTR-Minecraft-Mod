package lotr.common.block;

import java.util.ArrayList;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRBlockRemains extends Block
{
	public LOTRBlockRemains()
	{
		super(LOTRMaterialRemains.remains);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
    @Override 
    public ArrayList getDrops(World world, int i, int j, int k, int metadata, int fortune)
    {
        ArrayList drops = new ArrayList();
		
		int boneCount = 1 + world.rand.nextInt(2 + fortune);
		int bone = world.rand.nextInt(3);
		switch (bone)
		{
			case 0:
				drops.add(new ItemStack(Items.bone, boneCount));
				break;
			case 1:
				drops.add(new ItemStack(LOTRMod.elfBone, boneCount));
				break;
			case 2:
				drops.add(new ItemStack(LOTRMod.orcBone, boneCount));
				break;
		}
		
		int rareDropChance = 4 - fortune;
		if (rareDropChance < 1)
		{
			rareDropChance = 1;
		}
		if (world.rand.nextInt(rareDropChance) == 0)
		{
			int drop = world.rand.nextInt(4);
			switch (drop)
			{
				case 0:
					drops.add(new ItemStack(Items.skull));
					break;
				case 1:
					drops.add(new ItemStack(LOTRMod.silverCoin, 1 + world.rand.nextInt(3)));
					break;
				case 2:
					drops.add(new ItemStack(LOTRMod.silverNugget, 1 + world.rand.nextInt(3)));
					break;
				case 3:
					drops.add(new ItemStack(Items.gold_nugget, 1 + world.rand.nextInt(3)));
					break;
			}
		}
		
		int ancientPartDropChance = 4 - fortune;
		if (ancientPartDropChance < 1)
		{
			ancientPartDropChance = 1;
		}
		if (world.rand.nextInt(ancientPartDropChance) == 0)
		{
			if (world.rand.nextBoolean())
			{
				drops.add(new ItemStack(LOTRMod.ancientItemParts, 1, world.rand.nextInt(3)));
			}
			else
			{
				drops.add(new ItemStack(LOTRMod.ancientItemParts, 1, 3));
			}
		}
		
		return drops;
    }
	
	@Override
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
		super.harvestBlock(world, entityplayer, i, j, k, l);
		if (!world.isRemote)
		{
			LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.mineRemains);
		}
	}
}
