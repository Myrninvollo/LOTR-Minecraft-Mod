package lotr.common.item;

import java.util.ArrayList;
import java.util.List;

import lotr.common.*;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemMugBrewable extends Item
{
	private static String[] strengthNames = {"weak", "light", "moderate", "strong", "potent"};
	private static float[] strengths = {0.25F, 0.5F, 1F, 2F, 3F};
	
	public IIcon barrelGui_emptyBucketSlotIcon;
	public IIcon barrelGui_emptyMugSlotIcon;

	private float alcoholicity;
	private int foodHealAmount;
	private float foodSaturationAmount;
	private List potionEffects = new ArrayList();
	private int damageAmount;
	
	public LOTRItemMugBrewable(float f)
	{
		super();
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabFood);
        setHasSubtypes(true);
        setMaxDamage(0);
		alcoholicity = f;
	}
	
	public LOTRItemMugBrewable setDrinkStats(int i, float f)
	{
		foodHealAmount = i;
		foodSaturationAmount = f;
		return this;
	}
	
	public LOTRItemMugBrewable addPotionEffect(int i, int j)
	{
		potionEffects.add(new PotionEffect(i, j * 20));
		return this;
	}
	
	public LOTRItemMugBrewable setDamageAmount(int i)
	{
		damageAmount = i;
		return this;
	}
	
	private float getStrength(ItemStack itemstack)
	{
		int i = itemstack.getItemDamage();
		if (i < 0 || i >= strengths.length)
		{
			i = 0;
		}
		return strengths[i];
	}
	
	private List convertPotionEffectsForStrength(float strength)
	{
		List list = new ArrayList();
		for (int i = 0; i < potionEffects.size(); i++)
		{
			PotionEffect base = (PotionEffect)potionEffects.get(i);
			PotionEffect modified = new PotionEffect(base.getPotionID(), (int)((float)base.getDuration() * strength));
			list.add(modified);
		}
		return list;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
	{
		int i = itemstack.getItemDamage();
		if (i < 0 || i >= strengths.length)
		{
			i = 0;
		}
		float strength = getStrength(itemstack);
		
        list.add(StatCollector.translateToLocal("item.lotr.drink." + strengthNames[i]));
        
        if (alcoholicity > 0F)
        {
        	EnumChatFormatting c = EnumChatFormatting.GREEN;
        	float f = alcoholicity * strength * 10F;
        	if (f < 2F)
        	{
        		c = EnumChatFormatting.GREEN;
        	}
        	else if (f < 5F)
        	{
        		c = EnumChatFormatting.YELLOW;
        	}
        	else if (f < 10F)
        	{
        		c = EnumChatFormatting.GOLD;
        	}
        	else if (f < 20F)
        	{
        		c = EnumChatFormatting.RED;
        	}
        	else
        	{
        		c = EnumChatFormatting.DARK_RED;
        	}
        	list.add(c + StatCollector.translateToLocal("item.lotr.drink.alcoholicity") + ": " + String.format("%.2f", f) + "%");
        }
        
        addPotionEffectsToTooltip(itemstack, entityplayer, list, flag, convertPotionEffectsForStrength(strength));
	}
	
	public static void addPotionEffectsToTooltip(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag, List itemEffects)
	{
		if (!itemEffects.isEmpty())
		{
			ItemStack potionEquivalent = new ItemStack(Items.potionitem);
			potionEquivalent.setItemDamage(69);
			NBTTagList effectsData = new NBTTagList();
			
        	for (int l = 0; l < itemEffects.size(); l++)
        	{
        		PotionEffect effect = (PotionEffect)itemEffects.get(l);
        		NBTTagCompound nbt = new NBTTagCompound();
        		effect.writeCustomPotionEffectToNBT(nbt);
        		effectsData.appendTag(nbt);
        	}
        	
        	potionEquivalent.setTagCompound(new NBTTagCompound());
        	potionEquivalent.getTagCompound().setTag("CustomPotionEffects", effectsData);
        	
        	List effectTooltips = new ArrayList();
        	potionEquivalent.getItem().addInformation(potionEquivalent, entityplayer, effectTooltips, flag);
        	
        	list.addAll(effectTooltips);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j <= 4; j++)
        {
            list.add(new ItemStack(item, 1, j));
        }
    }
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
		return LOTRMod.mug.onItemUse(itemstack, entityplayer, world, i, j, k, side, f, f1, f2);
	}
	
	@Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.drink;
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		return itemstack;
	}
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		float strength = getStrength(itemstack);

		if (entityplayer.canEat(false))
		{
			entityplayer.getFoodStats().addStats((int)(foodHealAmount * strength), foodSaturationAmount * strength);
		}
		
		if (!world.isRemote && itemRand.nextFloat() < alcoholicity * strength)
		{
			int duration = (int)(60F * (1F + itemRand.nextFloat() * 0.5F) * alcoholicity * strength);
			if (duration >= 1)
			{
				duration *= 20;
				entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, duration));
			}
		}
		
		if (!world.isRemote)
		{
			List effects = convertPotionEffectsForStrength(strength);
			for (int i = 0; i < effects.size(); i++)
			{
				PotionEffect effect = (PotionEffect)effects.get(i);
				entityplayer.addPotionEffect(effect);
			}
		}
		
		if (damageAmount > 0)
		{
			entityplayer.attackEntityFrom(DamageSource.magic, (float)damageAmount * strength);
		}
		
		if (!world.isRemote)
		{
			if (this == LOTRMod.mugOrcDraught)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.drinkOrcDraught);
			}
			
			if (this == LOTRMod.mugAthelasBrew)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.drinkAthelasBrew);
				
				for (int i = 0; i < Potion.potionTypes.length; i++)
				{
					Potion potion = Potion.potionTypes[i];
					if (potion != null && LOTRReflection.isBadEffect(potion))
					{
						entityplayer.removePotionEffect(potion.id);
					}
				}
			}
			
			if (this == LOTRMod.mugRedWine)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.drinkWine);
			}
			
			if (this == LOTRMod.mugDwarvenTonic)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.drinkDwarvenTonic);
			}
		}

        return !entityplayer.capabilities.isCreativeMode ? new ItemStack(LOTRMod.mug) : itemstack;
    }
	
	public void applyToNPC(LOTREntityNPC npc, ItemStack itemstack)
	{
		float strength = getStrength(itemstack);
		
		npc.heal(foodHealAmount * strength);

		List effects = convertPotionEffectsForStrength(strength);
		for (int i = 0; i < effects.size(); i++)
		{
			PotionEffect effect = (PotionEffect)effects.get(i);
			npc.addPotionEffect(effect);
		}
		
		if (damageAmount > 0)
		{
			npc.attackEntityFrom(DamageSource.magic, (float)damageAmount * strength);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister)
	{
		super.registerIcons(iconregister);
		barrelGui_emptyBucketSlotIcon = iconregister.registerIcon("lotr:barrel_emptyBucketSlot");
		barrelGui_emptyMugSlotIcon = iconregister.registerIcon("lotr:barrel_emptyMugSlot");
	}
}
