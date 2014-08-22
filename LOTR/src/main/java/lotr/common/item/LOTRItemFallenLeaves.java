package lotr.common.item;

import lotr.common.block.LOTRBlockFallenLeaves;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LOTRItemFallenLeaves extends LOTRItemBlockMetadata
{
    public LOTRItemFallenLeaves(Block block)
    {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack)
    {
    	Object[] obj = ((LOTRBlockFallenLeaves)field_150939_a).forFallenLeaf(itemstack.getItemDamage());
    	ItemStack leaves = new ItemStack((Block)obj[0], 1, (Integer)obj[1]);
        String name = leaves.getDisplayName();
        return StatCollector.translateToLocalFormatted("item.lotr.fallenLeaves", new Object[] {name});
    }
}
