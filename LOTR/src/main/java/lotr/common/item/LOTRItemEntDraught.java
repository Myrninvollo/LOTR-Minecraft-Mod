package lotr.common.item;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.block.LOTRBlockSaplingBase;
import lotr.common.entity.npc.LOTREntityHuorn;
import lotr.common.entity.npc.LOTREntityTree;
import lotr.common.entity.npc.LOTRHiredNPCInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemEntDraught extends Item
{
	@SideOnly(Side.CLIENT)
	private IIcon[] draughtIcons;
	public static DraughtInfo[] draughtTypes = new DraughtInfo[]
	{
		new DraughtInfo("green", 0, 0F).addEffect(Potion.moveSpeed.id, 120).addEffect(Potion.digSpeed.id, 120).addEffect(Potion.damageBoost.id, 120),
		new DraughtInfo("brown", 20, 3F),
		new DraughtInfo("gold", 0, 0F),
		new DraughtInfo("yellow", 0, 0F).addEffect(Potion.regeneration.id, 60),
		new DraughtInfo("red", 0, 0F).addEffect(Potion.fireResistance.id, 180),
		new DraughtInfo("silver", 0, 0F).addEffect(Potion.nightVision.id, 180),
		new DraughtInfo("blue", 0, 0F).addEffect(Potion.waterBreathing.id, 180)
	};
	
	public LOTRItemEntDraught()
	{
		super();
		setHasSubtypes(true);
		setMaxDamage(0);
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabFood);
	}
	
	private DraughtInfo getDraughtInfo(ItemStack itemstack)
	{
		int i = itemstack.getItemDamage();
		if (i >= draughtTypes.length)
		{
			i = 0;
		}
		return draughtTypes[i];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        if (i >= draughtIcons.length)
		{
			i = 0;
		}
		return draughtIcons[i];
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
		draughtIcons = new IIcon[draughtTypes.length];
        for (int i = 0; i < draughtTypes.length; i++)
        {
        	draughtIcons[i] = iconregister.registerIcon(getIconString() + "_" + draughtTypes[i].name);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j < draughtTypes.length; j++)
        {
            list.add(new ItemStack(item, 1, j));
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
	{
        LOTRItemMugBrewable.addPotionEffectsToTooltip(itemstack, entityplayer, list, flag, getDraughtInfo(itemstack).effects);
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
		if (canPlayerDrink(entityplayer, itemstack))
		{
			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}
		return itemstack;
	}
	
	public boolean canPlayerDrink(EntityPlayer entityplayer, ItemStack itemstack)
	{
		return !getDraughtInfo(itemstack).effects.isEmpty() || entityplayer.canEat(true);
	}
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (LOTRLevelData.getAlignment(entityplayer, LOTRFaction.FANGORN) < 0)
		{
			if (!world.isRemote)
			{
				entityplayer.addPotionEffect(new PotionEffect(Potion.poison.id, 100));
			}
		}
		else
		{
			if (entityplayer.canEat(false))
			{
				entityplayer.getFoodStats().addStats(getDraughtInfo(itemstack).heal, getDraughtInfo(itemstack).saturation);
			}
			
			if (!world.isRemote)
			{
				List effects = getDraughtInfo(itemstack).effects;
				for (int i = 0; i < effects.size(); i++)
				{
					PotionEffect effect = (PotionEffect)effects.get(i);
					entityplayer.addPotionEffect(new PotionEffect(effect.getPotionID(), effect.getDuration()));
				}
			}
		}
		
		if (!world.isRemote && entityplayer.getCurrentEquippedItem() == itemstack)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.drinkEntDraught);
		}

        return !entityplayer.capabilities.isCreativeMode ? new ItemStack(Items.bowl) : itemstack;
    }
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
		if (getDraughtInfo(itemstack).name.equals("gold"))
		{
			if (LOTRLevelData.getAlignment(entityplayer, LOTRFaction.FANGORN) < LOTRAlignmentValues.SPAWN_HUORN)
			{
				if (!world.isRemote)
				{
					LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, LOTRAlignmentValues.SPAWN_HUORN, LOTRFaction.FANGORN);
				}
				return false;
			}
			
			Block block = world.getBlock(i, j, k);
			int meta = world.getBlockMetadata(i, j, k);
			if (block instanceof BlockSapling || block instanceof LOTRBlockSaplingBase)
			{
				meta &= 3;
				for (int huornType = 0; huornType < LOTREntityTree.TYPES.length; huornType++)
				{
					if (block == LOTREntityTree.SAPLING_BLOCKS[huornType] && meta == LOTREntityTree.SAPLING_META[huornType])
					{
						LOTREntityHuorn huorn = new LOTREntityHuorn(world);
						huorn.setTreeType(huornType);
						huorn.isNPCPersistent = true;
						huorn.liftSpawnRestrictions = true;
						huorn.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
						if (huorn.getCanSpawnHere())
						{
							if (!world.isRemote)
							{
								world.spawnEntityInWorld(huorn);
								huorn.initCreatureForHire(null);
								huorn.hiredNPCInfo.isActive = true;
								huorn.hiredNPCInfo.alignmentRequiredToCommand = LOTRAlignmentValues.SPAWN_HUORN;
								huorn.hiredNPCInfo.setHiringPlayerUUID(entityplayer.getUniqueID().toString());
								huorn.hiredNPCInfo.setTask(LOTRHiredNPCInfo.Task.WARRIOR);
								LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.summonHuorn);
							}
							for (int l = 0; l < 24; l++)
							{
								double d = i + 0.5D - world.rand.nextDouble() * 2D + world.rand.nextDouble() * 2D;
								double d1 = j + world.rand.nextDouble() * 4D;
								double d2 = k + 0.5D - world.rand.nextDouble() * 2D + world.rand.nextDouble() * 2D;
								world.spawnParticle("happyVillager", d, d1, d2, 0D, 0D, 0D);
							}
							return true;
						}
					}
				}
			}
		}
		return false;
    }
	
	public static class DraughtInfo
	{
		public String name;
		public int heal;
		public float saturation;
		public List effects = new ArrayList();
		
		public DraughtInfo(String s, int i, float f)
		{
			name = s;
			heal = i;
			saturation = f;
		}
		
		public DraughtInfo addEffect(int i, int j)
		{
			effects.add(new PotionEffect(i, j * 20));
			return this;
		}
	}
}
