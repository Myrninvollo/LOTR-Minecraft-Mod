package lotr.common.block;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.entity.item.LOTREntityOrcBomb;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockOrcBomb extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] orcBombIcons;
	
    public LOTRBlockOrcBomb()
    {
        super(Material.iron);
        setCreativeTab(LOTRCreativeTabs.tabCombat);
		setBlockBounds(0.125F, 0F, 0.125F, 0.875F, 0.9375F, 0.875F);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int i)
    {
		int strength = getBombStrengthLevel(i);
		
		if (strength == 1)
		{
			return 0xB6B6B6;
		}
		if (strength == 2)
		{
			return 0x777777;
		}
		return 0xFFFFFF;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int i, int j, int k)
    {
        int meta = world.getBlockMetadata(i, j, k);
		int strength = getBombStrengthLevel(meta);

		if (strength == 1)
		{
			return 0xB6B6B6;
		}
		if (strength == 2)
		{
			return 0x777777;
		}
		return 0xFFFFFF;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		boolean isFire = isFireBomb(j);
		
		if (i == -1)
		{
			return orcBombIcons[2];
		}
		if (i == 1)
		{
			return isFire ? orcBombIcons[4] : orcBombIcons[1];
		}
		return isFire ? orcBombIcons[3] : orcBombIcons[0];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		orcBombIcons = new IIcon[5];
        orcBombIcons[0] = iconregister.registerIcon(getTextureName() + "_side");
		orcBombIcons[1] = iconregister.registerIcon(getTextureName() + "_top");
		orcBombIcons[2] = iconregister.registerIcon(getTextureName() + "_handle");
        orcBombIcons[3] = iconregister.registerIcon(getTextureName() + "_fire_side");
		orcBombIcons[4] = iconregister.registerIcon(getTextureName() + "_fire_top");
    }
	
	public static int getBombStrengthLevel(int meta)
	{
		return meta & 7;
	}
	
	public static boolean isFireBomb(int meta)
	{
		return (meta & 8) != 0;
	}

	@Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        if (world.isBlockIndirectlyGettingPowered(i, j, k))
        {
            onBlockDestroyedByPlayer(world, i, j, k, -1);
            world.setBlockToAir(i, j, k);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block block)
    {
        if (block.getMaterial() != Material.air && block.canProvidePower() && world.isBlockIndirectlyGettingPowered(i, j, k))
        {
            onBlockDestroyedByPlayer(world, i, j, k, -1);
            world.setBlockToAir(i, j, k);
        }
    }

    @Override
    public void onBlockExploded(World world, int i, int j, int k, Explosion explosion)
    {
        if (!world.isRemote)
        {
			int meta = world.getBlockMetadata(i, j, k);
				
            LOTREntityOrcBomb bomb = new LOTREntityOrcBomb(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), explosion.getExplosivePlacedBy());
			bomb.setBombStrengthLevel(meta);
			bomb.setFuseFromExplosion();
			bomb.droppedByPlayer = true;
			
            world.spawnEntityInWorld(bomb);
        }
		super.onBlockExploded(world, i, j, k, explosion);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int meta)
    {
        if (!world.isRemote)
        {
            if (meta == -1)
            {
				meta = world.getBlockMetadata(i, j, k);
				
                LOTREntityOrcBomb bomb = new LOTREntityOrcBomb(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), null);
				bomb.setBombStrengthLevel(meta);
				bomb.droppedByPlayer = true;
				
                world.spawnEntityInWorld(bomb);
                world.playSoundAtEntity(bomb, "random.fuse", 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int l, float f, float f1, float f2)
    {
        if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == LOTRMod.orcTorchItem)
        {
            onBlockDestroyedByPlayer(world, i, j, k, -1);
            world.setBlockToAir(i, j, k);
            return true;
        }
        return false;
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion)
    {
        return false;
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
		return LOTRMod.proxy.getOrcBombRenderID();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int k = 0; k <= 1; k++)
		{
			for (int j = 0; j <= 2; j++)
			{
				list.add(new ItemStack(item, 1, j + k * 8));
			}
		}
    }
}
