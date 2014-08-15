package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.dispenser.LOTRDispenserBehaviorThrowingAxe;
import lotr.common.entity.projectile.LOTREntityThrowingAxe;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRItemThrowingAxe extends Item
{
	private ToolMaterial axeMaterial;
	
	public LOTRItemThrowingAxe(ToolMaterial material)
	{
		super();
		axeMaterial = material;
		setMaxStackSize(1);
		setMaxDamage(material.getMaxUses());
		setFull3D();
		setCreativeTab(LOTRCreativeTabs.tabCombat);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, new LOTRDispenserBehaviorThrowingAxe());
	}
	
	public ToolMaterial getAxeMaterial()
	{
		return axeMaterial;
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        LOTREntityThrowingAxe axe = new LOTREntityThrowingAxe(world, entityplayer, itemstack.getItem(), itemstack.getItemDamage(), 1F);
        world.playSoundAtEntity(entityplayer, "random.bow", 1F, 1F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
        if (!world.isRemote)
        {
            world.spawnEntityInWorld(axe);
        }
		if (!entityplayer.capabilities.isCreativeMode)
		{
			itemstack.stackSize--;
		}
		return itemstack;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem)
    {
        return axeMaterial.func_150995_f() == repairItem.getItem() ? true : super.getIsRepairable(itemstack, repairItem);
    }
}