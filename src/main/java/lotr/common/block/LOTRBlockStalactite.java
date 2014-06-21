package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockStalactite extends Block
{
	public LOTRBlockStalactite()
	{
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 1F, 0.75F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return Blocks.stone.getIcon(i, j);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
	@Override
	public int damageDropped(int i)
	{
		return i;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return LOTRMod.proxy.getStalactiteRenderID();
	}
	
	@Override
	public boolean canBlockStay(World world, int i, int j, int k)
	{
		int metadata = world.getBlockMetadata(i, j, k);
		if (metadata == 0)
		{
			return world.getBlock(i, j + 1, k).isSideSolid(world, i, j + 1, k, ForgeDirection.DOWN);
		}
		if (metadata == 1)
		{
			return world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP);
		}
		return false;
	}
	
	@Override
    public boolean canReplace(World world, int i, int j, int k, int side, ItemStack itemstack)
    {
		int metadata = itemstack.getItemDamage();
		if (metadata == 0)
		{
			return world.getBlock(i, j + 1, k).isSideSolid(world, i, j + 1, k, ForgeDirection.DOWN);
		}
		if (metadata == 1)
		{
			return world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP);
		}
		return false;
    }
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block block)
	{
		if (!canBlockStay(world, i, j, k))
		{
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockToAir(i, j, k);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 1; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (random.nextInt(50) == 0 && world.getBlockMetadata(i, j, k) == 0 && world.getBlock(i, j + 1, k) == Blocks.stone)
		{
			world.spawnParticle("dripWater", i + 0.6D, j, k + 0.6D, 0D, 0D, 0D);
		}
	}
	
	@Override
	public void onFallenUpon(World world, int i, int j, int k, Entity entity, float fallDistance)
	{
		if (world.getBlockMetadata(i, j, k) == 1)
		{
			int damage = (int)(fallDistance * 2F) + 1;
			entity.attackEntityFrom(DamageSource.fall, (float)damage);
		}
	}
}
