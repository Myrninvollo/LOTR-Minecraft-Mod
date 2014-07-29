package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemSword extends ItemSword
{
	@SideOnly(Side.CLIENT)
	public IIcon glowingIcon;
	private boolean isElvenBlade = false;
	private ToolMaterial toolMaterial;
	
	public LOTRItemSword(ToolMaterial material)
	{
		super(material);
		toolMaterial = material;
		setCreativeTab(LOTRCreativeTabs.tabCombat);
	}
	
	public LOTRItemSword setIsElvenBlade()
	{
		isElvenBlade = true;
		return this;
	}
	
	public boolean isElvenBlade()
	{
		return isElvenBlade;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
		super.registerIcons(iconregister);
		if (isElvenBlade)
		{
			glowingIcon = iconregister.registerIcon(getIconString() + "_glowing");
		}
    }
}
