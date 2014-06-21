package lotr.common.block;

import lotr.common.LOTRMod;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockLettuceCrop extends BlockCrops
{
	@SideOnly(Side.CLIENT)
	private IIcon[] lettuceIcons;
	
    public LOTRBlockLettuceCrop()
    {
        super();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j)
    {
        if (j < 7)
        {
            if (j == 6)
            {
                j = 5;
            }
            return lettuceIcons[j >> 1];
        }
        else
        {
            return lettuceIcons[3];
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
	{
		lettuceIcons = new IIcon[4];
        for (int i = 0; i < 4; i++)
        {
            lettuceIcons[i] = iconregister.registerIcon(getTextureName() + "_" + i);
        }
    }

	@Override
    public Item func_149866_i()
    {
        return LOTRMod.lettuce;
    }

	@Override
    public Item func_149865_P()
    {
        return LOTRMod.lettuce;
    }
	
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int i, int j, int k)
    {
        return EnumPlantType.Crop;
    }
	
	@Override
	public int getRenderType()
	{
		return 1;
	}
}