package lotr.common.entity.item;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityStoneTroll extends Entity
{
	private float trollHealth = 40F;
	public boolean placedByPlayer;
	private int entityAge;
	
	public LOTREntityStoneTroll(World world)
	{
		super(world);
		setSize(1.6F, 3.2F);
	}
	
	@Override
	protected void entityInit()
	{
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
	}
	
	public int getTrollOutfit()
	{
		return dataWatcher.getWatchableObjectByte(16);
	}
	
	public void setTrollOutfit(int i)
	{
		dataWatcher.updateObject(16, Byte.valueOf((byte)i));
	}
	
	public boolean hasTwoHeads()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setHasTwoHeads(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("TrollHealth", trollHealth);
		nbt.setByte("TrollOutfit", (byte)getTrollOutfit());
		nbt.setBoolean("PlacedByPlayer", placedByPlayer);
		nbt.setBoolean("TwoHeads", hasTwoHeads());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		trollHealth = nbt.getFloat("TrollHealth");
		setTrollOutfit(nbt.getByte("TrollOutfit"));
		placedByPlayer = nbt.getBoolean("PlacedByPlayer");
		setHasTwoHeads(nbt.getBoolean("TwoHeads"));
	}
	
	@Override
	public boolean canBeCollidedWith()
    {
        return true;
    }
	
	@Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }
	
	@Override
    public void onUpdate()
    {
        super.onUpdate();
		
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.03999999910593033D;

        func_145771_j(posX, (boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);
        moveEntity(motionX, motionY, motionZ);
        float f = 0.98F;

        if (onGround)
        {
            f = 0.58800006F;
            Block i = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
            if (i.getMaterial() != Material.air)
            {
                f = i.slipperiness * 0.98F;
            }
        }

        motionX *= (double)f;
        motionY *= 0.9800000190734863D;
        motionZ *= (double)f;

        if (onGround)
        {
            motionY *= -0.5D;
        }
		
		if (!worldObj.isRemote && !placedByPlayer)
		{
			entityAge++;
            EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, -1.0D);
            if (entityplayer != null)
            {
                double d = entityplayer.posX - posX;
                double d1 = entityplayer.posY - posY;
                double d2 = entityplayer.posZ - posZ;
                double distanceSq = d * d + d1 * d1 + d2 * d2;

                if (distanceSq > 16384.0D)
                {
                    setDead();
                }

                if (entityAge > 600 && rand.nextInt(800) == 0 && distanceSq > 1024.0D)
                {
                    setDead();
                }
                else if (distanceSq < 1024.0D)
                {
                    entityAge = 0;
                }
            }
		}
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		if (!worldObj.isRemote)
		{
			if (placedByPlayer)
			{
				if (damagesource.getSourceOfDamage() instanceof EntityPlayer)
				{
					worldObj.playSoundAtEntity(this, Blocks.stone.stepSound.getBreakSound(), (Blocks.stone.stepSound.getVolume() + 1F) / 2F, Blocks.stone.stepSound.getPitch() * 0.8F);
					worldObj.setEntityState(this, (byte)17);
					setDead();
					EntityPlayer entityplayer = (EntityPlayer)damagesource.getSourceOfDamage();
					if (!entityplayer.capabilities.isCreativeMode)
					{
						dropAsStatue();
					}
					return true;
				}
				return false;
			}
			else
			{
				int dropMode = 1;
				if (damagesource.getSourceOfDamage() instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)damagesource.getSourceOfDamage();
					if (entityplayer.capabilities.isCreativeMode)
					{
						dropMode = 0;
						f = trollHealth;
					}
					else
					{
						ItemStack itemstack = entityplayer.inventory.getCurrentItem();
						if (itemstack != null && itemstack.getItem() instanceof ItemPickaxe)
						{
							f = (float)entityplayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
							if (EnchantmentHelper.getSilkTouchModifier(entityplayer))
							{
								dropMode = 2;
								f = trollHealth;
							}
						}
						else
						{
							dropMode = 0;
							f = 1F;
						}
					
						if (itemstack != null)
						{
							itemstack.damageItem(1, entityplayer);
							if (itemstack.stackSize <= 0)
							{
								entityplayer.destroyCurrentEquippedItem();
							}
						}
					}
				}
				
				trollHealth -= f;
				if (trollHealth <= 0F)
				{
					worldObj.playSoundAtEntity(this, Blocks.stone.stepSound.getBreakSound(), (Blocks.stone.stepSound.getVolume() + 1F) / 2F, Blocks.stone.stepSound.getPitch() * 0.8F);
					worldObj.setEntityState(this, (byte)17);
					
					if (dropMode == 1)
					{
						int drops = 6 + rand.nextInt(7);
						for (int l = 0; l < drops; l++)
						{
							dropItem(Item.getItemFromBlock(Blocks.cobblestone), 1);
						}
					}
					else if (dropMode == 2)
					{
						if (damagesource.getSourceOfDamage() instanceof EntityPlayer)
						{
							EntityPlayer entityplayer = (EntityPlayer)damagesource.getSourceOfDamage();
							LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.getTrollStatue);
						}
						dropAsStatue();
					}
					
					setDead();
				}
				else
				{
					worldObj.playSoundAtEntity(this, Blocks.stone.stepSound.getBreakSound(), (Blocks.stone.stepSound.getVolume() + 1F) / 2F, Blocks.stone.stepSound.getPitch() * 0.5F);
					worldObj.setEntityState(this, (byte)16);
				}
				
				return true;
			}
		}
		return false;
	}
	
	private ItemStack getStatueItem()
	{
		ItemStack itemstack = new ItemStack(LOTRMod.trollStatue);
		itemstack.setItemDamage(getTrollOutfit());
		itemstack.setTagCompound(new NBTTagCompound());
		itemstack.getTagCompound().setBoolean("TwoHeads", hasTwoHeads());
		return itemstack;
	}
	
	private void dropAsStatue()
	{
		entityDropItem(getStatueItem(), 0F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte b)
	{
		if (b == 16)
		{
			for (int l = 0; l < 16; l++)
			{
				worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(Blocks.stone) + "_0", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0D, 0D, 0D);
			}
		}
		else if (b == 17)
		{
			for (int l = 0; l < 64; l++)
			{
				worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(Blocks.stone) + "_0", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0D, 0D, 0D);
			}
		}
		else
		{
			super.handleHealthUpdate(b);
		}
	}
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return getStatueItem();
    }
}
