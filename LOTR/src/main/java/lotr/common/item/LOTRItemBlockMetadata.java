package lotr.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemBlockMetadata extends ItemBlock
{
    public LOTRItemBlockMetadata(Block block)
    {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        return field_150939_a.getIcon(2, i);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int pass)
    {
        return field_150939_a.getRenderColor(itemstack.getItemDamage());
    }

	@Override
    public int getMetadata(int i)
    {
        return i;
    }

	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
}
