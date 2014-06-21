package lotr.common.item;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class LOTRItemSting extends LOTRItemSword
{
	public LOTRItemSting()
	{
		super(ToolMaterial.IRON);
		setMaxDamage(700);
	}
	
	@Override
    public float func_150893_a(ItemStack itemstack, Block block)
    {
        if (block == LOTRMod.webUngoliant)
        {
            return 15F;
        }
        return super.func_150893_a(itemstack, block);
    }
	
	/*
	 * Old damage behaviour with bonus vs. spiders - to be reimplemented once the necessary hooks are added
	 *
	@Override
    public float getDamageVsEntity(Entity entity)
    {
        float f = super.getDamageVsEntity(entity);
		if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD)
		{
			f *= 2F;
		}
		return f;
    }
	*/
}
