package lotr.common.dispenser;

import lotr.common.entity.projectile.LOTREntityCrossbowBolt;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class LOTRDispenserBehaviorCrossbowBolt extends BehaviorProjectileDispense
{
    @Override
    protected IProjectile getProjectileEntity(World world, IPosition iposition)
    {
        LOTREntityCrossbowBolt bolt = new LOTREntityCrossbowBolt(world, iposition.getX(), iposition.getY(), iposition.getZ());
        bolt.canBePickedUp = 1;
        return bolt;
    }
}
