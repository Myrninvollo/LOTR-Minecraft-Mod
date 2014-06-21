package lotr.common.item;

import lotr.client.LOTRClientProxy;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDwarf;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemArmor extends ItemArmor
{
	private String extraName;
	
	public LOTRItemArmor(ArmorMaterial material, int slotType)
	{
		super(material, 0, slotType);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
	}
	
	public LOTRItemArmor(ArmorMaterial material, String s, int slotType)
	{
		this(material, slotType);
		extraName = s;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		String prefix = getArmorMaterial().name().substring(5).toLowerCase();
		if (extraName != null)
		{
			prefix = prefix + "_" + extraName;
		}
		String suffix = armorType == 2 ? "_2" : "_1";
		if (getArmorMaterial() == LOTRMod.armorMorgul && slot == 0)
		{
			suffix = "_helmet";
		}
		String path = entity instanceof LOTREntityDwarf ? "lotr:mob/dwarf/" : "lotr:armor/";
		return path + prefix + suffix + ".png";
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack itemstack, int slot)
    {
        if (itemstack.getItem() == LOTRMod.helmetGondorWinged && slot == 0)
		{
			return LOTRClientProxy.modelWingedHelmet;
		}
        if (getArmorMaterial() == LOTRMod.armorMorgul && slot == 0)
		{
			return LOTRClientProxy.modelMorgulHelmet;
		}
        if (getArmorMaterial() == LOTRMod.armorGemsbok && slot == 0)
		{
			return LOTRClientProxy.modelGemsbokHelmet;
		}
        if (getArmorMaterial() == LOTRMod.armorHighElven && slot == 0)
		{
			return LOTRClientProxy.modelHighElvenHelmet;
		}
		return super.getArmorModel(entity, itemstack, slot);
    }
}
