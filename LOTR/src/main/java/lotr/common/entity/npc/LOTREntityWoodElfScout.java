package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityWoodElfScout extends LOTREntityWoodElf
{
	public LOTREntityWoodElfScout(World world)
	{
		super(world);
		tasks.addTask(2, rangedAttackAI);
	}
	
	@Override
	public EntityAIBase createElfRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 25, 35, 24F);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(getElfBowId(), 1, 0));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsWoodElvenScout));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsWoodElvenScout));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyWoodElvenScout));
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetWoodElvenScout));
		}
		return data;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && isEntityAlive() && ridingEntity == null)
		{
			ItemStack currentItem = getEquipmentInSlot(0);
			if (currentItem != null && currentItem.getItem() instanceof ItemBow)
			{
				EntityLivingBase lastAttacker = getAITarget();
				if (lastAttacker != null && getDistanceSqToEntity(lastAttacker) < 16D && rand.nextInt(20) == 0)
				{
					for (int l = 0; l < 32; l++)
					{
						int i = MathHelper.floor_double(posX) - 8 + rand.nextInt(17);
						int j = MathHelper.floor_double(posY) - 3 + rand.nextInt(7);
						int k = MathHelper.floor_double(posZ) - 8 + rand.nextInt(17);
						if (getDistanceSq(i, j, k) > 25D && worldObj.getBlock(i, j - 1, k).isNormalCube() && !worldObj.getBlock(i, j, k).isNormalCube() && !worldObj.getBlock(i, j + 1, k).isNormalCube())
						{
							double d = (double)i + 0.5D;
							double d1 = (double)j;
							double d2 = (double)k + 0.5D;
							AxisAlignedBB aabb = boundingBox.copy().offset(d - posX, d1 - posY, d2 - posZ);
							if (worldObj.checkNoEntityCollision(aabb) && worldObj.getCollidingBoundingBoxes(this, aabb).isEmpty() && !worldObj.isAnyLiquid(aabb))
							{
								doTeleportEffects();
								setPosition(d, d1, d2);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private void doTeleportEffects()
	{
		worldObj.playSoundAtEntity(this, "lotr:elf.woodElf_teleport", getSoundVolume(), 0.5F + rand.nextFloat());
		worldObj.setEntityState(this, (byte)15);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte b)
    {
        if (b == 15)
        {
            for (int i = 0; i < 16; i++)
			{
				double d = posX + (rand.nextDouble() - 0.5D) * (double)width;
				double d1 = posY + rand.nextDouble() * (double)height;
				double d2 = posZ + (rand.nextDouble() - 0.5D) * (double)width;
				double d3 = -0.05D + (double)(rand.nextFloat() * 0.1F);
				double d4 = -0.05D + (double)(rand.nextFloat() * 0.1F);
				double d5 = -0.05D + (double)(rand.nextFloat() * 0.1F);
				LOTRMod.proxy.spawnParticle("leafGreen_" + (20 + rand.nextInt(30)), d, d1, d2, d3, d4, d5);
			}
        }
        else
        {
            super.handleHealthUpdate(b);
        }
    }
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.WOOD_ELF_WARRIOR;
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        EntityArrow arrow = new EntityArrow(worldObj, this, target, 1.3F + (getDistanceToEntity(target) / 24F * 0.3F), 0.5F);
		arrow.setDamage(arrow.getDamage() + 0.75D);
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(arrow);
    }
	
	@Override
	public boolean shouldRenderHair()
	{
		return getEquipmentInSlot(4) == null;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "woodElf_hired";
			}
			else
			{
				if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.WOOD_ELF_TRUST)
				{
					return "woodElfWarrior_friendly";
				}
				else
				{
					return "woodElf_neutral";
				}
			}
		}
		else
		{
			return "woodElfWarrior_hostile";
		}
	}
}
