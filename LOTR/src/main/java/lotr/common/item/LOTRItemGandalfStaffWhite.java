package lotr.common.item;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.LOTRCreativeTabs;
import lotr.common.entity.projectile.LOTREntityGandalfFireball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

public class LOTRItemGandalfStaffWhite extends Item
{
	private float weaponDamage = 7F;
	
	public LOTRItemGandalfStaffWhite()
	{
		super();
		setMaxStackSize(1);
		setMaxDamage(1500);
		setCreativeTab(LOTRCreativeTabs.tabMagic);
		setFull3D();
	}
	
	@Override
	public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)weaponDamage, 0));
        return multimap;
    }
	
	@Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase user)
    {
        itemstack.damageItem(1, user);
        return true;
    }
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.swingItem();
		itemstack.damageItem(2, entityplayer);
		world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 2F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1F);
		
		if (!world.isRemote)
		{
			world.spawnEntityInWorld(new LOTREntityGandalfFireball(world, entityplayer));
		
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(entityplayer.getEntityId());
        	
        	S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.staffWhite", data);
        	MinecraftServer.getServer().getConfigurationManager().sendToAllNear(entityplayer.posX, entityplayer.posY, entityplayer.posZ, 64D, entityplayer.dimension, packet);
		}
		
		return itemstack;
	}

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 40;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }
}
