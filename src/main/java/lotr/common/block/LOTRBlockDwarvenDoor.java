package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityDwarvenDoor;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockDwarvenDoor extends BlockDoor
{
	public LOTRBlockDwarvenDoor()
	{
		super(Material.rock);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        return Blocks.stone.getIcon(i, j);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int i, int j, int k, int side)
    {
		return getIcon(i, world.getBlockMetadata(i, j, k));
    }
	
	@SideOnly(Side.CLIENT)
	public IIcon getGlowTexture(IBlockAccess world, int i, int j, int k, int side)
	{
		return super.getIcon(world, i, j, k, side);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
	{
		String s = getTextureName();
		setBlockTextureName(s + "_glow");
		super.registerBlockIcons(iconregister);
		setBlockTextureName(s);
	}
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		boolean flag = super.onBlockActivated(world, i, j, k, entityplayer, side, f, f1, f2);
		if (flag && !world.isRemote)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useDwarvenDoor);
		}
		return flag;
	}
	
	@Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

	@Override
    public TileEntity createTileEntity(World world, int metadata)
	{
		return new LOTRTileEntityDwarvenDoor();
    }
	
	@Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return (i & 8) != 0 ? null : LOTRMod.dwarvenDoorItem;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
        return LOTRMod.dwarvenDoorItem;
    }
}
