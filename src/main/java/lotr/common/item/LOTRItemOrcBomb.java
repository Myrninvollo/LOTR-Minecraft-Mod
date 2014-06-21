package lotr.common.item;

import java.util.List;

import lotr.common.block.LOTRBlockOrcBomb;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemOrcBomb extends ItemBlock
{
    public LOTRItemOrcBomb(Block block)
    {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

	@Override
    public int getMetadata(int i)
    {
        return i;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
		int meta = itemstack.getItemDamage();
		
		int strength = LOTRBlockOrcBomb.getBombStrengthLevel(meta);
        if (strength == 1)
		{
			list.add(StatCollector.translateToLocal("tile.lotr:orcBomb.doubleStrength"));
		}
        if (strength == 2)
		{
			list.add(StatCollector.translateToLocal("tile.lotr:orcBomb.tripleStrength"));
		}
		
		if (LOTRBlockOrcBomb.isFireBomb(meta))
		{
			list.add(StatCollector.translateToLocal("tile.lotr:orcBomb.fire"));
		}
    }
}
