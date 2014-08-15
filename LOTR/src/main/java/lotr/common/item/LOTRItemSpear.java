package lotr.common.item;

import lotr.common.dispenser.LOTRDispenserBehaviorSpear;
import lotr.common.entity.projectile.LOTREntitySpear;
import net.minecraft.block.BlockDispenser;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTRItemSpear extends LOTRItemSword
{
	public ItemSword swordReplacement;
	
	public LOTRItemSpear(ToolMaterial material, Item sword)
	{
		super(material);
		
		lotrWeaponDamage = material.getDamageVsEntity() + 3F;
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
		charge = Math.max(charge, 2F);
		
        LOTREntitySpear spear = new LOTREntitySpear(world, entityplayer, itemstack.getItem(), itemstack.getItemDamage(), charge);
		if (charge >= 2F)
		{
			spear.setIsCritical(true);
		}
		
		if (itemstack.getTagCompound() != null)
		{
			spear.setItemData((NBTTagCompound)itemstack.getTagCompound().copy());
		}
		
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemstack) > 0)
        {
        	spear.setFire(1000);
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
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }
}
