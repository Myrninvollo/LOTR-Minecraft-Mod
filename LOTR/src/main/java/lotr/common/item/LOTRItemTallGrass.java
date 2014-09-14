package lotr.common.item;

import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockTallGrass;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemTallGrass extends LOTRItemBlockMetadata
{
	public LOTRItemTallGrass(Block block)
	{
		super(block);
	}
	
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
    public int getRenderPasses(int meta)
    {
        return LOTRBlockTallGrass.grassOverlay[meta] ? 2 : 1;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass)
    {
        if (pass > 0)
        {
        	return LOTRMod.tallGrass.getIcon(-1, meta);
        }
        return super.getIconFromDamageForRenderPass(meta, pass);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int pass)
    {
        if (pass > 0)
        {
        	return 0xFFFFFF;
        }
        return super.getColorFromItemStack(itemstack, pass);
    }
}
