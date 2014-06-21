package lotr.common.block;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockMobSpawner extends BlockMobSpawner
{
	public LOTRBlockMobSpawner()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabSpawn);
	}
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		if (entityplayer.capabilities.isCreativeMode)
		{
			entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_MOB_SPAWNER, world, i, j, k);
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		return Blocks.mob_spawner.getIcon(i, j);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {}
	
	@Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new LOTRTileEntityMobSpawner();
    }
	
	@Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int i, int j, int k)
    {
        TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityMobSpawner)
		{
			LOTRTileEntityMobSpawner spawner = (LOTRTileEntityMobSpawner)tileentity;
			return new ItemStack(this, 1, LOTREntities.getIDFromString(spawner.getMobID()));
		}
		return null;
    }
	
	@Override
	public int getRenderType()
	{
		return LOTRMod.proxy.getMobSpawnerRenderID();
	}
}
