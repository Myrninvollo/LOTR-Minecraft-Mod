package lotr.common.dispenser;

import lotr.common.entity.projectile.LOTREntityThrowingAxe;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class LOTRDispenserBehaviorThrowingAxe extends BehaviorDefaultDispenseItem
{
    @Override
    public ItemStack dispenseStack(IBlockSource dispenser, ItemStack itemstack)
    {
        World world = dispenser.getWorld();
        IPosition iposition = BlockDispenser.func_149939_a(dispenser);
        EnumFacing enumfacing = BlockDispenser.func_149937_b(dispenser.getBlockMetadata());
        LOTREntityThrowingAxe axe = new LOTREntityThrowingAxe(world, itemstack.getItem(), itemstack.getItemDamage(), iposition.getX(), iposition.getY(), iposition.getZ());
        axe.setThrowableHeading((double)enumfacing.getFrontOffsetX(), (double)((float)enumfacing.getFrontOffsetY() + 0.1F), (double)enumfacing.getFrontOffsetZ(), 1.1F, 6F);
		axe.canBePickedUp = 1;
        world.spawnEntityInWorld(axe);
        itemstack.splitStack(1);
		return itemstack;
    }
	
	@Override
    protected void playDispenseSound(IBlockSource dispenser)
    {
        dispenser.getWorld().playAuxSFX(1002, dispenser.getXInt(), dispenser.getYInt(), dispenser.getZInt(), 0);
    }
}
