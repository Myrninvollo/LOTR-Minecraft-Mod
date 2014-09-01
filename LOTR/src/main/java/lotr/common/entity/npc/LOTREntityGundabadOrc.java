package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.quest.LOTRMiniQuestFactory;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGundabadOrc extends LOTREntityOrc
{
	private static ItemStack[] weapons = new ItemStack[]
	{
		new ItemStack(Items.stone_sword),
		new ItemStack(Items.stone_axe),
		new ItemStack(Items.stone_pickaxe),
		new ItemStack(Items.iron_sword),
		new ItemStack(Items.iron_axe),
		new ItemStack(Items.iron_pickaxe),
		new ItemStack(LOTRMod.daggerIron),
		new ItemStack(LOTRMod.daggerIronPoisoned),
		new ItemStack(LOTRMod.spearIron),
		new ItemStack(LOTRMod.swordBronze),
		new ItemStack(LOTRMod.axeBronze),
		new ItemStack(LOTRMod.pickaxeBronze),
		new ItemStack(LOTRMod.daggerBronze),
		new ItemStack(LOTRMod.daggerBronzePoisoned),
		new ItemStack(LOTRMod.spearBronze),
		new ItemStack(LOTRMod.swordAngmar),
		new ItemStack(LOTRMod.axeAngmar),
		new ItemStack(LOTRMod.pickaxeAngmar),
		new ItemStack(LOTRMod.daggerAngmar),
		new ItemStack(LOTRMod.daggerAngmarPoisoned),
		new ItemStack(LOTRMod.spearAngmar),
		new ItemStack(LOTRMod.battleaxeAngmar),
		new ItemStack(LOTRMod.hammerAngmar),
		new ItemStack(LOTRMod.scimitarOrc),
		new ItemStack(LOTRMod.axeOrc),
		new ItemStack(LOTRMod.pickaxeOrc),
		new ItemStack(LOTRMod.daggerOrc),
		new ItemStack(LOTRMod.daggerOrcPoisoned),
		new ItemStack(LOTRMod.spearOrc),
		new ItemStack(LOTRMod.battleaxeOrc),
		new ItemStack(LOTRMod.hammerOrc)
	};
	
	private static ItemStack[] helmets = new ItemStack[]
	{
		new ItemStack(Items.leather_helmet),
		new ItemStack(LOTRMod.helmetBronze),
		new ItemStack(LOTRMod.helmetWarg),
		new ItemStack(LOTRMod.helmetAngmar),
		new ItemStack(LOTRMod.helmetOrc)
	};
	
	private static ItemStack[] bodies = new ItemStack[]
	{
		new ItemStack(Items.leather_chestplate),
		new ItemStack(LOTRMod.bodyBronze),
		new ItemStack(LOTRMod.bodyWarg),
		new ItemStack(LOTRMod.bodyAngmar),
		new ItemStack(LOTRMod.bodyOrc)
	};
	
	private static ItemStack[] legs = new ItemStack[]
	{
		new ItemStack(Items.leather_leggings),
		new ItemStack(LOTRMod.legsBronze),
		new ItemStack(LOTRMod.legsWarg),
		new ItemStack(LOTRMod.legsAngmar),
		new ItemStack(LOTRMod.legsOrc)
	};
	
	private static ItemStack[] boots = new ItemStack[]
	{
		new ItemStack(Items.leather_boots),
		new ItemStack(LOTRMod.bootsBronze),
		new ItemStack(LOTRMod.bootsWarg),
		new ItemStack(LOTRMod.bootsAngmar),
		new ItemStack(LOTRMod.bootsOrc)
	};
	
	public LOTREntityGundabadOrc(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase createOrcAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		
		int i = rand.nextInt(weapons.length);
		setCurrentItemOrArmor(0, weapons[i].copy());
		
		i = rand.nextInt(boots.length);
		setCurrentItemOrArmor(1, boots[i].copy());
		
		i = rand.nextInt(legs.length);
		setCurrentItemOrArmor(2, legs[i].copy());
		
		i = rand.nextInt(bodies.length);
		setCurrentItemOrArmor(3, bodies[i].copy());
		
		if (rand.nextInt(3) != 0)
		{
			i = rand.nextInt(helmets.length);
			setCurrentItemOrArmor(4, helmets[i].copy());
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
		return LOTRAlignmentValues.Bonuses.GUNDABAD_ORC;
	}
	
	@Override
	public LOTRMiniQuest createMiniQuest(EntityPlayer entityplayer)
	{
		return LOTRMiniQuestFactory.GUNDABAD.createQuest(entityplayer);
	}
}
