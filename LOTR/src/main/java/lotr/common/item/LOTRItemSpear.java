package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.dispenser.LOTRDispenserBehaviorSpear;
import lotr.common.entity.projectile.LOTREntitySpear;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

public class LOTRItemSpear extends Item
{
	public ToolMaterial spearMaterial;
	private float weaponDamage;
	public ItemSword swordReplacement;
	
	public LOTRItemSpear(ToolMaterial material, Item sword)
	{
		super();
		spearMaterial = material;
		setMaxStackSize(1);
		setMaxDamage(material.getMaxUses());
		setFull3D();
		setCreativeTab(LOTRCreativeTabs.tabCombat);
		
		weaponDamage = 3F + material.getDamageVsEntity();
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, new LOTRDispenserBehaviorSpear());
		
		if (sword == null || !(sword instanceof ItemSword))
		{
			throw new RuntimeException("Spear must have a sword replacement");
		}
		else
		{
			swordReplacement = (ItemSword)sword;
		}
	}
	
	@Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
		float charge = (float)(getMaxItemUseDuration(itemstack) - i);
		charge /= getMaxDrawTime();
		if (charge > 2F)
		{
			charge = 2F;
		}
        LOTREntitySpear spear = new LOTREntitySpear(world, entityplayer, itemstack.getItem(), itemstack.getItemDamage(), charge);
		if (charge == 2F)
		{
			spear.setIsCritical(true);
		}
        world.playSoundAtEntity(entityplayer, "random.bow", 1F, 1F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
		entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
        if (!world.isRemote)
        {
            world.spawnEntityInWorld(spear);
        }
    }
	
	public float getMaxDrawTime()
	{
		return 20F;
	}

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
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
        return spearMaterial.func_150995_f() == repairItem.getItem() ? true : super.getIsRepairable(itemstack, repairItem);
    }
}
