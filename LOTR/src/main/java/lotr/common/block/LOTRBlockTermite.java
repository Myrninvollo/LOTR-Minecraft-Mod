package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.entity.animal.LOTREntityTermite;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockTermite extends Block
{
    public LOTRBlockTermite()
    {
        super(Material.ground);
        setCreativeTab(LOTRCreativeTabs.tabBlock);
    }

	@Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int meta)
    {
        if (!world.isRemote && meta == 0 && world.rand.nextBoolean())
        {
			int termites = 1 + world.rand.nextInt(3);
			for (int l = 0; l < termites; l++)
			{
            	spawnTermite(world, i, j, k);
			}
        }
    }
	
	@Override
	public void onBlockExploded(World world, int i, int j, int k, Explosion explosion)
	{
		int meta = world.getBlockMetadata(i, j, k);
		if (!world.isRemote && meta == 0 && world.rand.nextBoolean())
		{
			spawnTermite(world, i, j, k);
		}
		
		super.onBlockExploded(world, i, j, k, explosion);
	}
	
	private void spawnTermite(World world, int i, int j, int k)
	{
		LOTREntityTermite termite = new LOTREntityTermite(world);
		termite.setLocationAndAngles((double)i + 0.5D, j, (double)k + 0.5D, world.rand.nextFloat() * 360F, 0F);
		world.spawnEntityInWorld(termite);
	}
	
	@Override
    public int damageDropped(int i)
    {
		return i;
    }

	@Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
    	return meta == 1 ? 1 : 0;
    }
	
	@Override
    protected ItemStack createStackedBlock(int i)
	{
		return new ItemStack(this, 1, 1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int i = 0; i <= 1; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
    }
}