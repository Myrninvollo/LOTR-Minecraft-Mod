package lotr.common.block;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockBlueDwarvenTable extends LOTRBlockCraftingTable
{
	@SideOnly(Side.CLIENT)
	private IIcon[] tableIcons;
	
	public LOTRBlockBlueDwarvenTable()
	{
		super(Material.rock, LOTRFaction.BLUE_MOUNTAINS, LOTRCommonProxy.GUI_ID_BLUE_DWARVEN_TABLE);
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
			return LOTRMod.brick.getIcon(0, 14);
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
}
