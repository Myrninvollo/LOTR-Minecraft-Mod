package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.entity.projectile.LOTREntityCrossbowBolt;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemCrossbow extends ItemBow
{
	private double boltDamageBonus;
	@SideOnly(Side.CLIENT)
	private IIcon[] crossbowPullIcons;
	private ToolMaterial repairMaterial;
	private int enchantability;
	
	public LOTRItemCrossbow(int j, double d, ToolMaterial material, int k)
	{
		super();
		setMaxDamage(j);
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
		boltDamageBonus = d;
		repairMaterial = material;
		enchantability = k;
	}
	
	@Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        int useTick = getMaxItemUseDuration(itemstack) - i;
        boolean consumeNoBolt = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;

        if (consumeNoBolt || entityplayer.inventory.hasItem(LOTRMod.crossbowBolt))
        {
            float f = (float)useTick / getMaxDrawTime();
            f = (f * f + f * 2F) / 3F;

            if ((double)f < 0.1D)
            {
                return;
            }

            if (f > 1F)
            {
                f = 1F;
            }

            LOTREntityCrossbowBolt bolt = new LOTREntityCrossbowBolt(world, entityplayer, f * 2F);
			bolt.boltDamageFactor += boltDamageBonus;
			if (bolt.boltDamageFactor < 1D)
			{
				bolt.boltDamageFactor = 1D;
			}

            if (f == 1F)
            {
                bolt.setIsCritical(true);
            }
			
            int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemstack);
            if (power > 0)
            {
                bolt.boltDamageFactor += (double)power * 0.5D + 0.5D;
            }
			
	        int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemstack);
            if (punch > 0)
            {
                bolt.knockbackStrength = punch;
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemstack) > 0)
            {
                bolt.setFire(100);
            }

            itemstack.damageItem(1, entityplayer);
            world.playSoundAtEntity(entityplayer, "lotr:item.crossbow", 1F, 1F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (consumeNoBolt)
            {
                bolt.canBePickedUp = 2;
            }
            else
            {
                entityplayer.inventory.consumeInventoryItem(LOTRMod.crossbowBolt);
            }

            if (!world.isRemote)
            {
                world.spawnEntityInWorld(bolt);
            }
        }
    }
	
	public float getMaxDrawTime()
	{
		return 30F;
	}

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.capabilities.isCreativeMode || entityplayer.inventory.hasItem(LOTRMod.crossbowBolt))
        {
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }
        return itemstack;
    }
	
	@Override
    public int getItemEnchantability()
    {
        return enchantability;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem)
    {
        return repairMaterial.func_150995_f() == repairItem.getItem() ? true : super.getIsRepairable(itemstack, repairItem);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack itemstack, int renderPass, EntityPlayer entityplayer, ItemStack usingItem, int useRemaining)
    {
		if (usingItem != null && usingItem.getItem() == this)
		{
			int i = usingItem.getMaxItemUseDuration() - useRemaining;
			if (i >= 27)
			{
				return crossbowPullIcons[2];
			}
			else if (i > 20)
			{
				return crossbowPullIcons[1];
			}
			else if (i > 0)
			{
				return crossbowPullIcons[0];
			}
		}
		return itemIcon;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
		itemIcon = iconregister.registerIcon(getIconString());
		crossbowPullIcons = new IIcon[3];
        for (int i = 0; i < 3; i++)
        {
            crossbowPullIcons[i] = iconregister.registerIcon(getIconString() + "_pull_" + i);
        }
    }
}
