package lotr.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemBattleaxe extends LOTRItemSword
{
	private float efficiencyOnProperMaterial;
	
	public LOTRItemBattleaxe(ToolMaterial material)
	{
		super(material);
		efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
		setHarvestLevel("axe", material.getHarvestLevel());
	}
	
	@Override
    public float func_150893_a(ItemStack itemstack, Block block)
	{
		float f = super.func_150893_a(itemstack, block);
		if (f == 1F && block != null && (block.getMaterial() == Material.wood || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine))
		{
			return efficiencyOnProperMaterial;
		}
		else
		{
			return f;
		}
    }
	
	@Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int i, int j, int k, EntityLivingBase entity)
    {
        if ((double)block.getBlockHardness(world, i, j, k) != 0D)
        {
            itemstack.damageItem(1, entity);
        }
        return true;
    }
	
    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.none;
    }
}
