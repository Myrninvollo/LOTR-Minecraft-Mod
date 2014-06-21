package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockMorgulTable extends LOTRBlockCraftingTable
{
	@SideOnly(Side.CLIENT)
	private IIcon[] tableIcons;
	
	public LOTRBlockMorgulTable()
	{
		super(Material.rock, LOTRFaction.MORDOR, LOTRCommonProxy.GUI_ID_MORGUL_TABLE);
		setLightLevel(0.5F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j)
	{
		if (i == 1)
		{
			return tableIcons[1];
		}
		if (i == 0)
		{
			return LOTRMod.rock.getIcon(2, 0);
		}
		return tableIcons[0];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister)
    {
		tableIcons = new IIcon[2];
        tableIcons[0] = iconregister.registerIcon(getTextureName() + "_side");
		tableIcons[1] = iconregister.registerIcon(getTextureName() + "_top");
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		for (int l = 0; l < 2; l++)
		{
			double d = i + 0.25D + (double)(random.nextFloat() * 0.5F);
			double d1 = j + 1D;
			double d2 = k + 0.25D + (double)(random.nextFloat() * 0.5F);
			world.spawnParticle("flame", d, d1, d2, 0D, 0D, 0D);
		}
	}
}
