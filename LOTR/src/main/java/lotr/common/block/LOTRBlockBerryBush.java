package lotr.common.block;

import static net.minecraftforge.common.EnumPlantType.Crop;

import java.util.*;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.*;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockBerryBush extends Block implements IPlantable, IGrowable
{
	@SideOnly(Side.CLIENT)
	private IIcon[] iconsBare;
	@SideOnly(Side.CLIENT)
	private IIcon[] iconsGrown;
	public static String[] bushTypes = {"blueberry", "blackberry", "raspberry", "cranberry", "elderberry"};
	
	public LOTRBlockBerryBush()
	{
		super(Material.plants);
		setTickRandomly(true);
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setHardness(0.4F);
		setStepSound(Block.soundTypeGrass);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		int berryType = getBerryType(j);
		if (berryType >= bushTypes.length)
		{
			berryType = 0;
		}

		if (hasBerries(j))
		{
			return iconsGrown[berryType];
		}
		else
		{
			return iconsBare[berryType];
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		iconsBare = new IIcon[bushTypes.length];
		iconsGrown = new IIcon[bushTypes.length];
		
        for (int i = 0; i < bushTypes.length; i++)
        {
        	iconsBare[i] = iconregister.registerIcon(getTextureName() + "_" + bushTypes[i] + "_bare");
        	iconsGrown[i] = iconregister.registerIcon(getTextureName() + "_" + bushTypes[i]);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int meta = 0; meta < bushTypes.length; meta++)
		{
			list.add(new ItemStack(item, 1, setHasBerries(meta, true)));
			list.add(new ItemStack(item, 1, setHasBerries(meta, false)));
		}
    }
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	public static boolean hasBerries(int meta)
	{
		return (meta & 8) != 0;
	}
	
	public static int getBerryType(int meta)
	{
		return meta & 7;
	}
	
	public static int setHasBerries(int meta, boolean flag)
	{
		if (flag)
		{
			return getBerryType(meta) | 8;
		}
		else
		{
			return getBerryType(meta);
		}
	}
	
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
		int meta = world.getBlockMetadata(i, j, k);
        if (!world.isRemote && !hasBerries(meta))
        {
            float growth = getGrowthFactor(world, i, j, k);
            if (random.nextFloat() < growth)
            {
            	growBerries(world, i, j, k);
            }
        }
    }
	
	private void growBerries(World world, int i, int j, int k)
	{
		int berryType = getBerryType(world.getBlockMetadata(i, j, k));
		world.setBlockMetadataWithNotify(i, j, k, setHasBerries(berryType, true), 3);
	}
	
    private float getGrowthFactor(World world, int i, int j, int k)
    {
    	Block below = world.getBlock(i, j - 1, k);
    	
    	if (below == Blocks.grass || below == Blocks.dirt)
    	{
    		float growth = (float)world.getBlockLightValue(i, j + 1, k) / 2000F;
    		if (world.isRaining())
	        {
	        	growth *= 3D;
	        }
    		return growth;
    	}
    	
    	if (below == Blocks.farmland && world.getBlockLightValue(i, j + 1, k) >= 9)
    	{
	        float growth = 1F;
	        
	        boolean bushAdjacent = false;
	        boolean bushAdjacentCorner = false;
	        
	        bushSearch:
	        for (int i1 = i - 1; i1 <= i + 1; i1++)
	        {
	        	for (int k1 = k - 1; k1 <= k + 1; k1++)
	            {
	            	if (i1 == i && k1 == k)
	            	{
	            		continue;
	            	}
	
	            	if (world.getBlock(i1, j, k1) instanceof LOTRBlockBerryBush)
	            	{
	            		bushAdjacent = true;
	            		break bushSearch;
	            	}
	            }
	        }
	
	        for (int i1 = i - 1; i1 <= i + 1; i1++)
	        {
	            for (int k1 = k - 1; k1 <= k + 1; k1++)
	            {
	                float growthBonus = 0F;
	
	                if (world.getBlock(i1, j - 1, k1).canSustainPlant(world, i1, j - 1, k1, ForgeDirection.UP, this))
	                {
	                	growthBonus = 1F;
	                    if (world.getBlock(i1, j - 1, k1).isFertile(world, i1, j - 1, k1))
	                    {
	                    	growthBonus = 3F;
	                    }
	                }
	
	                if (i1 != i || k1 != k)
	                {
	                	growthBonus /= 4F;
	                }
	
	                growth += growthBonus;
	            }
	        }
	
	        if (bushAdjacent)
	        {
	        	growth /= 2F;
	        }
	        
	        if (world.isRaining())
	        {
	        	growth *= 3D;
	        }

	        return growth / 150F;
    	}
    	
    	return 0F;
    }
	
	@Override
	public int damageDropped(int i)
	{
		return i;
	}
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		int meta = world.getBlockMetadata(i, j, k);
        if (hasBerries(meta))
        {
        	int berryType = getBerryType(meta);
        	world.setBlockMetadataWithNotify(i, j, k, setHasBerries(berryType, false), 3);
        	
        	if (!world.isRemote)
        	{
        		List<ItemStack> drops = getBerryDrops(world, i, j, k, meta);
        		for (ItemStack berry : drops)
        		{
        			dropBlockAsItem(world, i, j, k, berry);
        		}
        	}
        	
        	return true;
        }
        return false;
    }
	
	@Override
    public ArrayList<ItemStack> getDrops(World world, int i, int j, int k, int meta, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList();
        drops.add(new ItemStack(this, 1, setHasBerries(meta, false)));
        drops.addAll(getBerryDrops(world, i, j, k, meta));
        return drops;
    }
	
	private ArrayList<ItemStack> getBerryDrops(World world, int i, int j, int k, int meta)
	{
		ArrayList drops = new ArrayList();
		
        if (hasBerries(meta))
        {
        	int berryType = getBerryType(meta);
        	Item berry = null;
        	int berries = 1 + world.rand.nextInt(4);
        	
        	switch (berryType)
        	{
        		case 0:
        			berry = LOTRMod.blueberry;
        			break;
        		case 1:
        			berry = LOTRMod.blackberry;
        			break;
        		case 2:
        			berry = LOTRMod.raspberry;
        			break;
        		case 3:
        			berry = LOTRMod.cranberry;
        			break;
        		case 4:
        			berry = LOTRMod.elderberry;
        			break;
        	}
        	
        	if (berry != null)
        	{
        		for (int l = 0; l < berries; l++)
        		{
        			drops.add(new ItemStack(berry));
        		}
        	}
        }
        
        return drops;
	}
	
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int i, int j, int k)
    {
        return Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int i, int j, int k)
    {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int i, int j, int k)
    {
        return world.getBlockMetadata(i, j, k);
    }

	@Override
	public boolean func_149851_a(World world, int i, int j, int k, boolean isRemote)
	{
		return !hasBerries(world.getBlockMetadata(i, j, k));
	}

	@Override
	public boolean func_149852_a(World world, Random random, int i, int j, int k)
	{
		return true;
	}

	@Override
	public void func_149853_b(World world, Random random, int i, int j, int k)
	{
		if (random.nextInt(3) == 0)
		{
			growBerries(world, i, j, k);
		}
	}
}
