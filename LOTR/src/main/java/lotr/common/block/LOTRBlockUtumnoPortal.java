package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRDimension;
import lotr.common.tileentity.LOTRTileEntityUtumnoPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockUtumnoPortal extends BlockContainer
{
	public LOTRBlockUtumnoPortal()
    {
        super(Material.portal);
        setHardness(-1F);
        setResistance(Float.MAX_VALUE);
        setStepSound(Block.soundTypeStone);
    }

	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new LOTRTileEntityUtumnoPortal();
	}
	
    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB aabb, List list, Entity entity) {}

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
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	
    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
		if (world.provider.dimensionId != LOTRDimension.MIDDLE_EARTH.dimensionID)
		{
			world.setBlockToAir(i, j, k);
		}
    }

	@Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
        return Item.getItemById(0);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        return Blocks.portal.getIcon(i, j);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {}
}
