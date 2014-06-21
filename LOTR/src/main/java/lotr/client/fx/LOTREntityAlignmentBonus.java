package lotr.client.fx;

import lotr.common.LOTRFaction;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTREntityAlignmentBonus extends Entity
{
	public int particleAge;
	public String name;
	public int bonus;
	public LOTRFaction faction;
	public boolean isKill;
	
	public LOTREntityAlignmentBonus(World world, double d, double d1, double d2, String s, int i, LOTRFaction f, boolean flag)
	{
		super(world);
		setSize(0.5F, 0.5F);
		yOffset = height / 2F;
        setPosition(d, d1, d2);
		particleAge = 0;
		name = s;
		bonus = i;
		faction = f;
		isKill = flag;
	}
	
	@Override
	protected void entityInit() {}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		particleAge++;
		if (particleAge >= 80)
		{
			setDead();
		}
	}
	
	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }
	
	@Override
    public boolean isEntityInvulnerable()
    {
        return true;
    }
	
	@Override
    public boolean canBePushed()
    {
        return false;
    }
}
