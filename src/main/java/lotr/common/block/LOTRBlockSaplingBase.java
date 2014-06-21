package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTRBlockSaplingBase extends LOTRBlockFlower
{
	@SideOnly(Side.CLIENT)
	private IIcon[] saplingIcons;
	private String[] saplingNames;
	
    public LOTRBlockSaplingBase()
    {
        super();
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        setCreativeTab(LOTRCreativeTabs.tabDeco);
    }
	
	public void setSaplingNames(String... s)
	{
		saplingNames = s;
	}
	
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        j &= 3;
		if (j >= saplingNames.length)
		{
			j = 0;
		}
		
        return saplingIcons[j];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        saplingIcons = new IIcon[saplingNames.length];
        for (int i = 0; i < saplingNames.length; i++)
        {
            saplingIcons[i] = iconregister.registerIcon(getTextureName() + "_" + saplingNames[i]);
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (!world.isRemote)
        {
            super.updateTick(world, i, j, k, random);
            if (world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(7) == 0)
            {
                incrementGrowth(world, i, j, k, random);
            }
        }
    }
	
    public void incrementGrowth(World world, int i, int j, int k, Random random)
    {
        int metadata = world.getBlockMetadata(i, j, k);
        if ((metadata & 8) == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, metadata | 8, 4);
        }
        else
        {
			if (!TerrainGen.saplingGrowTree(world, random, i, j, k))
			{
				return;
			}
            growTree(world, i, j, k, random);
        }
    }

    public abstract void growTree(World world, int i, int j, int k, Random random);
	
    public boolean isSameSapling(World world, int i, int j, int k, int meta)
    {
        return world.getBlock(i, j, k) == this && (world.getBlockMetadata(i, j, k) & 3) == meta;
    }

	@Override
    public int damageDropped(int i)
    {
        return i & 3;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j < saplingNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
}
