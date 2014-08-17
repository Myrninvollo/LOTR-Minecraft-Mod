package lotr.common.entity.animal;

import lotr.common.LOTRReflection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGiraffe extends LOTREntityHorse
{
    public LOTREntityGiraffe(World world)
    {
        super(world);
        setSize(1.7F, 3.5F);
    }
	
	@Override
	public int getHorseType()
	{
		return 0;
	}
	
	@Override
	protected void onLOTRHorseSpawn()
	{
		double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getAttributeValue();
		jumpStrength *= 0.8D;
		getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength);
	}
	
	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack != null && Block.getBlockFromItem(itemstack.getItem()) instanceof BlockLeavesBase;
    }

	@Override
    protected Item getDropItem()
    {
        return Items.leather;
    }
}
