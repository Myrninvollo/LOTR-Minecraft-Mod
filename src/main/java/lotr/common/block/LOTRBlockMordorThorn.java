package lotr.common.block;

import java.util.ArrayList;
import java.util.Random;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class LOTRBlockMordorThorn extends LOTRBlockMordorPlant implements IShearable
{
    public LOTRBlockMordorThorn()
    {
        super();
		setBlockBounds(0.1F, 0F, 0.1F, 0.9F, 0.8F, 0.9F);
    }
	
	@Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return null;
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int i, int j, int k)
    {
        return true;
    }

    @Override
    public ArrayList onSheared(ItemStack item, IBlockAccess world, int i, int j, int k, int fortune)
    {
        ArrayList drops = new ArrayList();
        drops.add(new ItemStack(this));
        return drops;
	}
	
	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
		if (LOTRMod.getNPCFaction(entity) != LOTRFaction.MORDOR)
		{
			entity.attackEntityFrom(DamageSource.cactus, 2F);
		}
    }
}
