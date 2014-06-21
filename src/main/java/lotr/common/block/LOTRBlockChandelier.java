package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockChandelier extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] chandelierIcons;
	private String[] chandelierNames = {"bronze", "iron", "silver", "gold", "mithril", "elven", "woodElven", "orc", "dwarven", "uruk", "highElven", "blueDwarven"};
	
	public LOTRBlockChandelier()
	{
		super(Material.circuits);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setLightLevel(0.9375F);
		setBlockBounds(0.0625F, 0.1875F, 0.0625F, 0.9375F, 1F, 0.9375F);
	}
	
	@Override
	public IIcon getIcon(int i, int j)
	{
		if (j >= chandelierNames.length)
		{
			j = 0;
		}
		return chandelierIcons[j];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        chandelierIcons = new IIcon[chandelierNames.length];
        for (int i = 0; i < chandelierNames.length; i++)
        {
            chandelierIcons[i] = iconregister.registerIcon(getTextureName() + "_" + chandelierNames[i]);
        }
    }
	
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
		return 1;
	}
	
	@Override
	public boolean canBlockStay(World world, int i, int j, int k)
	{
		Block block = world.getBlock(i, j + 1, k);
		if (block instanceof BlockFence || block instanceof BlockWall)
		{
			return true;
		}
		
		return world.getBlock(i, j + 1, k).isSideSolid(world, i, j + 1, k, ForgeDirection.DOWN);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		return canBlockStay(world, i, j, k);
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 11; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		int meta = world.getBlockMetadata(i, j, k);
		if (meta == 5 || meta == 10)
		{
			LOTRMod.proxy.spawnParticle("elvenGlow", i + 0.13D, j + 0.6875D, k + 0.13D, 0D, 0D, 0D);
			
			LOTRMod.proxy.spawnParticle("elvenGlow", i + 0.87D, j + 0.6875D, k + 0.87D, 0D, 0D, 0D);
			
			LOTRMod.proxy.spawnParticle("elvenGlow", i + 0.13D, j + 0.6875D, k + 0.87D, 0D, 0D, 0D);
			
			LOTRMod.proxy.spawnParticle("elvenGlow", i + 0.87D, j + 0.6875D, k + 0.13D, 0D, 0D, 0D);
		}
		else if (meta == 6)
		{
			String s = "leafRed_" + (10 + random.nextInt(20));
			double d5 = -0.005D + (double)(random.nextFloat() * 0.01F);
			double d6 = -0.005D + (double)(random.nextFloat() * 0.01F);
			double d7 = -0.005D + (double)(random.nextFloat() * 0.01F);
			
			LOTRMod.proxy.spawnParticle(s, i + 0.13D, j + 0.6875D, k + 0.13D, d5, d6, d7);
			
			LOTRMod.proxy.spawnParticle(s, i + 0.87D, j + 0.6875D, k + 0.87D, d5, d6, d7);
			
			LOTRMod.proxy.spawnParticle(s, i + 0.13D, j + 0.6875D, k + 0.87D, d5, d6, d7);
			
			LOTRMod.proxy.spawnParticle(s, i + 0.87D, j + 0.6875D, k + 0.13D, d5, d6, d7);

		}
		else
		{
			world.spawnParticle("smoke", i + 0.13D, j + 0.6875D, k + 0.13D, 0D, 0D, 0D);
			world.spawnParticle("flame", i + 0.13D, j + 0.6875D, k + 0.13D, 0D, 0D, 0D);
			
			world.spawnParticle("smoke", i + 0.87D, j + 0.6875D, k + 0.87D, 0D, 0D, 0D);
			world.spawnParticle("flame", i + 0.87D, j + 0.6875D, k + 0.87D, 0D, 0D, 0D);
			
			world.spawnParticle("smoke", i + 0.13D, j + 0.6875D, k + 0.87D, 0D, 0D, 0D);
			world.spawnParticle("flame", i + 0.13D, j + 0.6875D, k + 0.87D, 0D, 0D, 0D);
			
			world.spawnParticle("smoke", i + 0.87D, j + 0.6875D, k + 0.13D, 0D, 0D, 0D);
			world.spawnParticle("flame", i + 0.87D, j + 0.6875D, k + 0.13D, 0D, 0D, 0D);
		}
	}
}
