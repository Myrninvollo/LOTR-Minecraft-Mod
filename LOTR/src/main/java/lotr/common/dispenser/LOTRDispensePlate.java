package lotr.common.dispenser;

import lotr.common.entity.projectile.LOTREntityPlate;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class LOTRDispensePlate extends BehaviorProjectileDispense
{
    @Override
    protected IProjectile getProjectileEntity(World world, IPosition position)
    {
        return new LOTREntityPlate(world, position.getX(), position.getY(), position.getZ());
    }
}
