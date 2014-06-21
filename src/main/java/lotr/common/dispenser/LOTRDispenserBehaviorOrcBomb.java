package lotr.common.dispenser;

import lotr.common.entity.item.LOTREntityOrcBomb;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class LOTRDispenserBehaviorOrcBomb extends BehaviorDefaultDispenseItem
{
    @Override
    protected ItemStack dispenseStack(IBlockSource dispenser, ItemStack itemstack)
    {
        EnumFacing enumfacing = BlockDispenser.func_149937_b(dispenser.getBlockMetadata());
        World world = dispenser.getWorld();
        int i = dispenser.getXInt() + enumfacing.getFrontOffsetX();
        int j = dispenser.getYInt() + enumfacing.getFrontOffsetY();
        int k = dispenser.getZInt() + enumfacing.getFrontOffsetZ();
        LOTREntityOrcBomb bomb = new LOTREntityOrcBomb(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), null);
		bomb.fuse += itemstack.getItemDamage() * 10;
		bomb.setBombStrengthLevel(itemstack.getItemDamage());
		bomb.droppedByPlayer = true;
        world.spawnEntityInWorld(bomb);
        itemstack.stackSize--;
        return itemstack;
    }
}
