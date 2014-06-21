package lotr.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class LOTRItemHammer extends LOTRItemSword
{
	public LOTRItemHammer(ToolMaterial material)
	{
		super(material);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase user)
	{
		if (super.hitEntity(itemstack, hitEntity, user))
		{
			if (!user.worldObj.isRemote && hitEntity.hurtTime == hitEntity.maxHurtTime)
			{
				hitEntity.addVelocity((double)(-MathHelper.sin(user.rotationYaw * (float)Math.PI / 180.0F) * 0.5F), 0.1D, (double)(MathHelper.cos(user.rotationYaw * (float)Math.PI / 180.0F) * 0.5F));
			}
			return true;
		}
		return false;
	}
	
    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.none;
    }
}
