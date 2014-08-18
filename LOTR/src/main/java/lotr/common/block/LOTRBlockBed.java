package lotr.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockBed extends BlockBed
{
	public Item bedItem;
	
	private Block bedBottomBlock;
	private int bedBottomMetadata;
    @SideOnly(Side.CLIENT)
    private IIcon[] bedIconsEnd;
    @SideOnly(Side.CLIENT)
    private IIcon[] bedIconsSide;
    @SideOnly(Side.CLIENT)
    private IIcon[] bedIconsTop;

    public LOTRBlockBed(Block block, int k)
    {
        super();
		bedBottomBlock = block;
		bedBottomMetadata = k;
		setHardness(0.2F);
		setStepSound(Block.soundTypeWood);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        if (i == 0)
        {
            return bedBottomBlock.getIcon(0, bedBottomMetadata);
        }
        else
        {
            int k = getDirection(j);
            int l = Direction.bedDirection[k][i];
            int i1 = isBlockHeadOfBed(j) ? 1 : 0;
            return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? bedIconsTop[i1] : bedIconsSide[i1]) : bedIconsEnd[i1];
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        bedIconsTop = new IIcon[] {iconregister.registerIcon(getTextureName() + "_feet_top"), iconregister.registerIcon(getTextureName() + "_head_top")};
        bedIconsEnd = new IIcon[] {iconregister.registerIcon(getTextureName() + "_feet_end"), iconregister.registerIcon(getTextureName() + "_head_end")};
        bedIconsSide = new IIcon[] {iconregister.registerIcon(getTextureName() + "_feet_side"), iconregister.registerIcon(getTextureName() + "_head_side")};
    }

    @Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return isBlockHeadOfBed(i) ? null : bedItem;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
        return bedItem;
    }
	
	@Override
    public boolean isBed(IBlockAccess world, int i, int j, int k, EntityLivingBase entity)
    {
        return true;
    }
}
