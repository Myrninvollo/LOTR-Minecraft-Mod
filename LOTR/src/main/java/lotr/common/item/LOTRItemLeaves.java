package lotr.common.item;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemLeaves extends ItemBlock
{
    public LOTRItemLeaves(Block block)
    {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

	@Override
    public int getMetadata(int i)
    {
        return i | 4;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        return LOTRMod.leaves.getIcon(0, i);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int par2)
    {
        int meta = itemstack.getItemDamage() & 3;
		if (meta == 0)
		{
			return 0x3A8E3A;
		}
        return ColorizerFoliage.getFoliageColorBasic();
    }

	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
}
