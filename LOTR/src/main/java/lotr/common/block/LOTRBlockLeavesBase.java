package lotr.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockLeavesBase extends BlockLeaves
{
	public static List allLeafBlocks = new ArrayList();
	
	@SideOnly(Side.CLIENT)
	private IIcon[][] leafIcons;
	private String[] leafNames;

    public LOTRBlockLeavesBase()
    {
        super();
        setCreativeTab(LOTRCreativeTabs.tabDeco);
		allLeafBlocks.add(this);
    }
	
	public void setLeafNames(String... s)
	{
		leafNames = s;
	}
	
	@Override
    public String[] func_150125_e()
    {
        return leafNames;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int i)
    {
        return 0xFFFFFF;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int i, int j, int k)
    {
		return 0xFFFFFF;
    }
	
	@Override
    public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int meta, float f, int fortune)
    {
        if (!world.isRemote)
        {
            int originalChance = getSaplingChance(meta & 3);
            int chance = originalChance;
            if (fortune > 0)
            {
                chance -= 2 << fortune;
                chance = Math.max(chance, originalChance / 2);
                chance = Math.max(chance, 1);
            }
            if (world.rand.nextInt(chance) == 0)
            {
                Item block = getItemDropped(meta, world.rand, fortune);
                dropBlockAsItem(world, i, j, k, new ItemStack(block, 1, damageDropped(meta)));
            }
        }
    }
    
    protected int getSaplingChance(int meta)
    {
    	return 20;
    }
	
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
		j &= 3;
		if (j >= leafNames.length)
		{
			j = 0;
		}
        return leafIcons[field_150121_P ? 0 : 1][j];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		leafIcons = new IIcon[2][leafNames.length];
        for (int i = 0; i < leafNames.length; i++)
        {
            leafIcons[0][i] = iconregister.registerIcon(getTextureName() + "_" + leafNames[i] + "_fancy");
			leafIcons[1][i] = iconregister.registerIcon(getTextureName() + "_" + leafNames[i] + "_fast");
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for (int j = 0; j < leafNames.length; j++)
		{
			list.add(new ItemStack(item, 1, j));
		}
    }
	
	public static void setAllGraphicsLevels(boolean flag)
	{
		for (int i = 0; i < allLeafBlocks.size(); i++)
		{
			((LOTRBlockLeavesBase)allLeafBlocks.get(i)).setGraphicsLevel(flag);
		}
	}
}
