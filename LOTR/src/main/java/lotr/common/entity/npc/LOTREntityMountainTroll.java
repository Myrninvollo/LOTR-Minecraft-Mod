package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import lotr.common.entity.projectile.LOTREntityThrownRock;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityMountainTroll extends LOTREntityTroll implements IRangedAttackMob
{
	public static IAttribute thrownRockDamage = new RangedAttribute("lotr.thrownRockDamage", 5D, 0D, 100D).setDescription("LOTR Thrown Rock Damage");
	
	private EntityAIBase rangedAttackAI = getTrollRangedAttackAI();
	private EntityAIBase meleeAttackAI;
	
	public LOTREntityMountainTroll(World world)
	{
		super(world);
		setSize(2.56F, 5.12F);
	}
	
	@Override
	public EntityAIBase getTrollAttackAI()
	{
		return (meleeAttackAI = new LOTREntityAIAttackOnCollide(this, 1.8D, false, 0.8F));
	}
	
	protected EntityAIBase getTrollRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.2D, 30, 60, 24F);
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(21, Byte.valueOf((byte)0));
	}
	
	public boolean isThrowingRocks()
	{
		return dataWatcher.getWatchableObjectByte(21) == (byte)1;
	}
	
	public void setThrowingRocks(boolean flag)
	{
		dataWatcher.updateObject(21, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(60D);
        getEntityAttribute(npcAttackDamage).setBaseValue(7D);
		getAttributeMap().registerAttribute(thrownRockDamage);
    }
	
	@Override
	protected boolean hasTrollName()
	{
		return false;
	}
	
	@Override
	protected boolean canTrollBeTickled(EntityPlayer entityplayer)
	{
		return false;
	}
	
	@Override
	public void onAttackModeChange(AttackMode mode)
	{
		if (mode == AttackMode.IDLE)
		{
			tasks.removeTask(rangedAttackAI);
			tasks.removeTask(meleeAttackAI);
			setThrowingRocks(false);
		}
		
		if (mode == AttackMode.MELEE)
		{
			tasks.removeTask(rangedAttackAI);
			tasks.addTask(3, meleeAttackAI);
			setThrowingRocks(false);
		}
		
		if (mode == AttackMode.RANGED)
		{
			tasks.removeTask(meleeAttackAI);
			tasks.addTask(3, rangedAttackAI);
			setThrowingRocks(true);
		}
	}
	
	protected double getMeleeRange()
	{
		return 10D;
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
		EntityArrow template = new EntityArrow(worldObj, this, target, f * 1.5F, 0.5F);
        LOTREntityThrownRock rock = getThrownRock();
		rock.setLocationAndAngles(template.posX, template.posY, template.posZ, template.rotationYaw, template.rotationPitch);
		rock.motionX = template.motionX;
		rock.motionY = template.motionY + 0.6D;
		rock.motionZ = template.motionZ;
        worldObj.spawnEntityInWorld(rock);
		playSound(getLivingSound(), getSoundVolume(), getSoundPitch() * 0.75F);
		swingItem();
    }
	
	protected LOTREntityThrownRock getThrownRock()
	{
		LOTREntityThrownRock rock = new LOTREntityThrownRock(worldObj, this);
		rock.setDamage((float)getEntityAttribute(thrownRockDamage).getAttributeValue());
		return rock;
	}
	
	@Override
	public void onTrollDeathBySun()
	{
		worldObj.playSoundAtEntity(this, "lotr:troll.transform", getSoundVolume(), getSoundPitch());
		worldObj.setEntityState(this, (byte)15);
		setDead();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte b)
	{
		if (b == 15)
		{
			super.handleHealthUpdate(b);
			for (int l = 0; l < 64; l++)
			{
				LOTRMod.proxy.spawnParticle("largeStone", posX + rand.nextGaussian() * (double)width * 0.5D, posY + rand.nextDouble() * (double)height, posZ + rand.nextGaussian() * (double)width * 0.5D, 0D, 0D, 0D);
			}
		}
		else
		{
			super.handleHealthUpdate(b);
		}
	}
	
	@Override
	public void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		if (rand.nextInt(15) == 0)
		{
			entityDropItem(new ItemStack(LOTRMod.trollTotem, 1, rand.nextInt(3)), 0F);
		}
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killMountainTroll;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.MOUNTAIN_TROLL_BONUS;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 5 + rand.nextInt(6);
    }
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return null;
	}
}
