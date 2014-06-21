package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.dispenser.LOTRDispenserBehaviorThrowingAxe;
import lotr.common.entity.projectile.LOTREntityThrowingAxe;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

public class LOTRItemThrowingAxe extends Item
{
	public ToolMaterial axeMaterial;
	private float weaponDamage;
	
	public LOTRItemThrowingAxe(ToolMaterial material)
	{
		super();
		axeMaterial = material;
		setMaxStackSize(1);
		setMaxDamage(material.getMaxUses());
		setFull3D();
		setCreativeTab(LOTRCreativeTabs.tabCombat);
		weaponDamage = 1F + material.getDamageVsEntity();
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, new LOTRDispenserBehaviorThrowingAxe());
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        LOTREntityThrowingAxe axe = new LOTREntityThrowingAxe(world, entityplayer, itemstack.getItem(), itemstack.getItemDamage(), 1F);
        world.playSoundAtEntity(entityplayer, "random.bow", 1F, 1F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
        if (!world.isRemote)
        {
            world.spawnEntityInWorld(axe);
        }
		if (!entityplayer.capabilities.isCreativeMode)
		{
			itemstack.stackSize--;
		}
		return itemstack;
    }
	
	@Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase user)
    {
        itemstack.damageItem(1, user);
        return true;
    }
	
	@Override
	public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)weaponDamage, 0));
        return multimap;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem)
    {
        return axeMaterial.func_150995_f() == repairItem.getItem() ? true : super.getIsRepairable(itemstack, repairItem);
    }
}
