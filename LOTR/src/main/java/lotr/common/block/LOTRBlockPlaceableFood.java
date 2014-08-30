package lotr.common.block;

import net.minecraft.block.BlockCake;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockPlaceableFood extends BlockCake
{
	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	@SideOnly(Side.CLIENT)
	private IIcon iconSide;
	@SideOnly(Side.CLIENT)
	private IIcon iconEaten;
	
	public Item foodItem;
	private float foodHalfWidth;
	private float foodHeight;
	
	private static int maxEats = 6;
	
	public LOTRBlockPlaceableFood()
	{
		this(0.4375F, 0.5F);
	}
	
    public LOTRBlockPlaceableFood(float f, float f1)
	{
		super();
		foodHalfWidth = f;
		foodHeight = f1;
	}

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        if (i == 0)
		{
			return iconBottom;
		}
        else if (i == 1)
		{
			return iconTop;
		}
		else if (j > 0 && i == 4)
		{
			return iconEaten;
		}
		return iconSide;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
        iconBottom = iconregister.registerIcon(getTextureName() + "_bottom");
		iconTop = iconregister.registerIcon(getTextureName() + "_top");
		iconSide = iconregister.registerIcon(getTextureName() + "_side");
		iconEaten = iconregister.registerIcon(getTextureName() + "_eaten");
    }

	@Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
		return foodItem;
    }
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
		float f = 0.5F - foodHalfWidth;
        float f1 = 0.5F + foodHalfWidth;
        float f2 = f + (f1 - f) * ((float)world.getBlockMetadata(i, j, k) / (float)maxEats);
        setBlockBounds(f2, 0F, f, f1, foodHeight, f1);
    }

	@Override
    public void setBlockBoundsForItemRender()
    {
		float f = 0.5F - foodHalfWidth;
        float f1 = 0.5F + foodHalfWidth;
        setBlockBounds(f, 0F, f,f1, foodHeight, f1);
    }

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
		float f = 0.5F - foodHalfWidth;
        float f1 = 0.5F + foodHalfWidth;
        float f2 = f + (f1 - f) * ((float)world.getBlockMetadata(i, j, k) / (float)maxEats);
        return AxisAlignedBB.getBoundingBox(i + f2, j, k + f, i + f1, j + foodHeight, k + f1);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return getCollisionBoundingBoxFromPool(world, i, j, k);
    }
}
