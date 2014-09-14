package lotr.common.entity.npc;

import lotr.common.LOTRFaction;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class LOTREntityUtumnoWarg extends LOTREntityWarg
{
	public LOTREntityUtumnoWarg(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase getWargAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.8D, true, 0.7F);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
	
	@Override
	public LOTREntityNPC createWargRider()
	{
		return worldObj.rand.nextBoolean() ? new LOTREntityUtumnoOrcArcher(worldObj) : new LOTREntityUtumnoOrc(worldObj);
	}
	
	@Override
	public boolean canWargBeRidden()
	{
		return false;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.UTUMNO;
	}
}
