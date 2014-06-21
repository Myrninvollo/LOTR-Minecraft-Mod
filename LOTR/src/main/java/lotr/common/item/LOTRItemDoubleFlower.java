package lotr.common.item;

import lotr.common.block.LOTRBlockDoubleFlower;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemDoubleFlower extends LOTRItemBlockMetadata
{
    public LOTRItemDoubleFlower(Block block)
    {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        return ((LOTRBlockDoubleFlower)field_150939_a).func_149888_a(true, i);
    }
}