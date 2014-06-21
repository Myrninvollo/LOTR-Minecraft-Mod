package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.item.Item;

public class LOTRBlockLeaves3 extends LOTRBlockLeavesBase
{
    public LOTRBlockLeaves3()
    {
        super();
		setLeafNames("maple", "larch", "datePalm");
    }
	
    @Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return Item.getItemFromBlock(LOTRMod.sapling3);
    }
}
