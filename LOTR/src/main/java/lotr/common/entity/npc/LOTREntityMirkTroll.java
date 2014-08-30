package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class LOTREntityMirkTroll extends LOTREntityTroll
{
	public LOTREntityMirkTroll(World world)
	{
		super(world);
		setSize(1.8F, 3.6F);
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, new LOTREntityAIAttackOnCollide(this, 2D, false, 0.8F));
		tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
        tasks.addTask(4, new EntityAIWander(this, 1D));
        tasks.addTask(5, new EntityAIWatchClosest2(this, EntityPlayer.class, 12F, 0.02F));
        tasks.addTask(5, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 8F, 0.02F));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityLiving.class, 12F, 0.01F));
        tasks.addTask(7, new EntityAILookIdle(this));
		addTargetTasks(true, LOTREntityAINearestAttackableTargetOrc.class);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(60D);
		getEntityAttribute(npcAttackDamage).setBaseValue(6D);
	}
	
	@Override
	public int getTotalArmorValue()
	{
		return 12;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.DOL_GULDUR;
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
	public boolean attackEntityAsMob(Entity entity)
    {
        if (super.attackEntityAsMob(entity))
        {
            if (entity instanceof EntityLivingBase)
            {
                byte duration = 0;

				if (worldObj.difficultySetting == EnumDifficulty.EASY)
				{
					duration = 2;
				}
				else if (worldObj.difficultySetting == EnumDifficulty.NORMAL)
				{
					duration = 5;
				}
				else if (worldObj.difficultySetting == EnumDifficulty.HARD)
				{
					duration = 8;
				}

                if (duration > 0)
                {
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, duration * 20, 0));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killMirkTroll;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.MIRK_TROLL;
	}
	
	public void dropTrollItems(boolean flag, int i)
	{
		if (flag)
		{
			int rareDropChance = 8 - i;
			if (rareDropChance < 1)
			{
				rareDropChance = 1;
			}
			
			if (rand.nextInt(rareDropChance) == 0)
			{
				int drops = 1 + rand.nextInt(2) + rand.nextInt(i + 1);
				for (int j = 0; j < drops; j++)
				{
					dropItem(LOTRMod.orcSteel, 1);
				}
			}
		}
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 4 + rand.nextInt(7);
    }
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return null;
	}
}
