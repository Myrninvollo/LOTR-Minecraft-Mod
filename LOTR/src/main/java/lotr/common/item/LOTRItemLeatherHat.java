package lotr.common.item;

import java.util.List;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelLeatherHat;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemLeatherHat extends LOTRItemArmor
{
	public static int HAT_LEATHER = 0x684A36;
	public static int HAT_SHIRRIFF_CHIEF = 0x23201D;
	public static int FEATHER_WHITE = 0xFFFFFF;
	public static int FEATHER_SHIRRIFF_CHIEF = 0x339919;
	public static int HAT_BLACK = 0x000000;
	
	@SideOnly(Side.CLIENT)
	private IIcon featherIcon;
	
	public LOTRItemLeatherHat()
	{
		super(LOTRMod.materialLeatherHat, 0);
		setMaxDamage(0);
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister)
	{
		super.registerIcons(iconregister);
		featherIcon = iconregister.registerIcon(getIconString() + "_feather");
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack itemstack, int pass)
    {
		if (pass == 1 && hasFeather(itemstack))
		{
			return featherIcon;
		}
		return itemIcon;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int pass)
    {
		if (pass == 1 && hasFeather(itemstack))
		{
			return getFeatherColor(itemstack);
		}
		return getHatColor(itemstack);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
		if (isHatDyed(itemstack))
		{
			list.add(StatCollector.translateToLocal("item.lotr.hat.dyed"));
		}
		if (hasFeather(itemstack))
		{
			list.add(StatCollector.translateToLocal("item.lotr.hat.feathered"));
		}
	}
	
	public static int getHatColor(ItemStack itemstack)
	{
		int i = HAT_LEATHER;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("HatColor"))
		{
			i = itemstack.getTagCompound().getInteger("HatColor");
		}
		return i;
	}
	
	public static boolean isHatDyed(ItemStack itemstack)
	{
		return getHatColor(itemstack) != HAT_LEATHER;
	}
	
	public static void setHatColor(ItemStack itemstack, int i)
	{
		if (itemstack.getTagCompound() == null)
		{
			itemstack.setTagCompound(new NBTTagCompound());
		}
		itemstack.getTagCompound().setInteger("HatColor", i);
	}
	
	public static int getFeatherColor(ItemStack itemstack)
	{
		int i = -1;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("FeatherColor"))
		{
			i = itemstack.getTagCompound().getInteger("FeatherColor");
		}
		return i;
	}
	
	public static boolean hasFeather(ItemStack itemstack)
	{
		return getFeatherColor(itemstack) > -1;
	}
	
	public static void setFeatherColor(ItemStack itemstack, int i)
	{
		if (itemstack.getTagCompound() == null)
		{
			itemstack.setTagCompound(new NBTTagCompound());
		}
		itemstack.getTagCompound().setInteger("FeatherColor", i);
	}
	
	@Override
    public boolean isDamageable()
    {
        return false;
    }
	
	@Override
	public int getItemEnchantability()
    {
        return 0;
    }
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		return "lotr:armor/hat.png";
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack itemstack, int slot)
    {
        LOTRModelLeatherHat hat = LOTRClientProxy.modelLeatherHat;
		hat.setHatItem(itemstack);
		return hat;
    }
}
