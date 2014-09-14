package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.*;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTRBlockPortal extends BlockContainer
{
	private LOTRFaction[] portalFactions;
	private Class teleporterClass;
	
	public LOTRBlockPortal(LOTRFaction[] factions, Class c)
    {
        super(Material.portal);
        portalFactions = factions;
		teleporterClass = c;
    }
	
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        float f = 0.0625F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }
	
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int side)
    {
        return side != 0 ? false : super.shouldSideBeRendered(world, i, j, k, side);
    }

    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB aabb, List list, Entity entity) {}
	
	public abstract void setPlayerInPortal(EntityPlayer entityplayer);
	
    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
		if (entity instanceof EntityPlayer)
		{
			for (LOTRFaction faction : portalFactions)
			{
				if (LOTRLevelData.getData((EntityPlayer)entity).getAlignment(faction) >= LOTRAlignmentValues.Levels.USE_PORTAL)
				{
					if (entity.ridingEntity == null && entity.riddenByEntity == null)
					{
						setPlayerInPortal((EntityPlayer)entity);
					}
					return;
				}
			}
		}
		else
		{
			for (LOTRFaction faction : portalFactions)
			{
				if (LOTRMod.getNPCFaction(entity).isAllied(faction))
				{
					if (entity.ridingEntity == null && entity.riddenByEntity == null && entity.timeUntilPortal == 0)
					{
						transferEntity(entity, world);
					}
					return;
				}
			}
		}
		
		if (!world.isRemote)
		{
			entity.setFire(4);
			entity.attackEntityFrom(DamageSource.inFire, 2F);
			world.playSoundAtEntity(entity, "random.fizz", 0.5F, 1.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.5F);
		}
    }

	public Teleporter getPortalTeleporter(WorldServer world)
	{
		for (Object obj : world.customTeleporters)
		{
			if (teleporterClass.isInstance(obj))
			{
				return (Teleporter)obj;
			}
		}
		
		Teleporter teleporter = null;
		try
		{
			teleporter = (Teleporter)teleporterClass.getConstructor(new Class[]{WorldServer.class}).newInstance(new Object[]{world});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		world.customTeleporters.add(teleporter);
		return teleporter;
	}
	
	private void transferEntity(Entity entity, World world)
	{
		if (!world.isRemote)
		{
			int dimension = 0;
			if (entity.dimension == 0)
			{
				dimension = LOTRDimension.MIDDLE_EARTH.dimensionID;
			}
			else if (entity.dimension == LOTRDimension.MIDDLE_EARTH.dimensionID)
			{
				dimension = 0;
			}
			
			LOTRMod.transferEntityToDimension(entity, dimension, getPortalTeleporter(DimensionManager.getWorld(dimension)));
		}
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
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if (random.nextInt(100) == 0)
        {
            world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "portal.portal", 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
		if (world.provider.dimensionId != 0 && world.provider.dimensionId != LOTRDimension.MIDDLE_EARTH.dimensionID)
		{
			world.setBlockToAir(i, j, k);
		}
    }
	
	public abstract boolean isValidPortalLocation(World world, int i, int j, int k, boolean portalAlreadyMade);
	
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
