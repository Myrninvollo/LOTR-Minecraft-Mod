package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGundabadOrc extends LOTREntityOrc
{
	public LOTREntityGundabadOrc(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase getOrcAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false).setSpearReplacement(LOTRMod.daggerIron);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(24);

		if (i == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.iron_axe));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerIron));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerIronPoisoned));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearIron));
		}
		else if (i == 5)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordBronze));
		}
		else if (i == 6)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.axeBronze));
		}
		else if (i == 7)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerBronze));
		}
		else if (i == 8)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerBronzePoisoned));
		}
		else if (i == 9)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearBronze));
		}
		else if (i == 10)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
		}
		else if (i == 11)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_axe));
		}
		else if (i == 12)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeOrc));
		}
		else if (i == 13)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerOrc));
		}
		else if (i == 14)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerOrcPoisoned));
		}
		else if (i == 15)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearOrc));
		}
		else if (i == 16)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.scimitarOrc));
		}
		else if (i == 17)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerOrc));
		}
		else if (i == 18)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeAngmar));
		}
		else if (i == 19)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerAngmar));
		}
		else if (i == 20)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerAngmarPoisoned));
		}
		else if (i == 21)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearAngmar));
		}
		else if (i == 22)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordAngmar));
		}
		else if (i == 23)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerAngmar));
		}
		
		i = rand.nextInt(5);
		if (i == 0)
		{
			setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsOrc));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsBronze));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsAngmar));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsWarg));
		}
		
		i = rand.nextInt(5);
		if (i == 0)
		{
			setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsOrc));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsBronze));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsAngmar));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(2, new ItemStack(Items.leather_leggings));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsWarg));
		}
		
		i = rand.nextInt(5);
		if (i == 0)
		{
			setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyOrc));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyBronze));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyAngmar));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(3, new ItemStack(Items.leather_chestplate));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyWarg));
		}
		
		if (rand.nextInt(10) != 0)
		{
			i = rand.nextInt(5);
			if (i == 0)
			{
				setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetOrc));
			}
			else if (i == 1)
			{
				setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetBronze));
			}
			else if (i == 2)
			{
				setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetAngmar));
			}
			else if (i == 3)
			{
				setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
			}
			else if (i == 4)
			{
				setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetWarg));
			}
		}
		
		return data;
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.GUNDABAD;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.GUNDABAD_ORC_BONUS;
	}
}
