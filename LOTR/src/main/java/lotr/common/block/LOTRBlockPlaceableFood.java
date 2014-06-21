package lotr.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
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
	
	public LOTRBlockPlaceableFood()
	{
		super();
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

	public static void registerFoodItem(Block block, Item foodItem)
	{
		((LOTRBlockPlaceableFood)block).foodItem = foodItem;
	}
}