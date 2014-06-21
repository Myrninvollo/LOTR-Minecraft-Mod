package lotr.common.block;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockRock extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] rockIcons;
	private String[] rockNames = {"mordor", "gondor", "rohan", "blue", "red"};
	
	public LOTRBlockRock()
	{
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}
	
	@Override
    public boolean isReplaceableOreGen(World world, int i, int j, int k, Block target)
    {
        if (target == this)
		{
			return world.getBlockMetadata(i, j, k) == 0;
		}
		return false;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		if (j >= rockNames.length)
		{
			j = 0;
		}
		
		return rockIcons[j];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        rockIcons = new IIcon[rockNames.length];
        for (int i = 0; i < rockNames.length; i++)
        {
            rockIcons[i] = iconregister.registerIcon(getTextureName() + "_" + rockNames[i]);
        }
    }
	
	@Override
    public int damageDropped(int i)
    {
        return i;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 4; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if (world.getBlockMetadata(i, j, k) == 0 && random.nextInt(10) == 0)
        {
            world.spawnParticle("smoke", (double)((float)i + random.nextFloat()), (double)((float)j + 1.1F), (double)((float)k + random.nextFloat()), 0D, 0D, 0D);
        }
    }
}
