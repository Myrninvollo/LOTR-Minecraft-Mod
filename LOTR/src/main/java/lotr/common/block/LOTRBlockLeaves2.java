package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.item.Item;

public class LOTRBlockLeaves2 extends LOTRBlockLeavesBase
{
    public LOTRBlockLeaves2()
    {
        super();
		setLeafNames("lebethron", "beech", "holly", "banana");
    }
	
    @Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return Item.getItemFromBlock(LOTRMod.sapling2);
    }
    
    @Override
    protected int getSaplingChance(int meta)
    {
    	if (meta == 3)
    	{
    		return 9;
    	}
    	else
    	{
    		return super.getSaplingChance(meta);
    	}
    }
}
