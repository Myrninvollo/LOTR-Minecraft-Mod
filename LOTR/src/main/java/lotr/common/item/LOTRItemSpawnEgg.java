package lotr.common.item;

import java.util.Iterator;
import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemSpawnEgg extends Item
{
    public LOTRItemSpawnEgg()
    {
        super();
        setHasSubtypes(true);
        setCreativeTab(LOTRCreativeTabs.tabSpawn);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack)
    {
        String itemName = ("" + StatCollector.translateToLocal(getUnlocalizedName() + ".name")).trim();
        String entityName = LOTREntities.getStringFromID(itemstack.getItemDamage());

        if (entityName != null)
        {
        	itemName = itemName + " " + StatCollector.translateToLocal("entity." + LOTREntities.getFullEntityName(entityName) + ".name");
        }

        return itemName;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
    	String entityName = LOTREntities.getStringFromID(itemstack.getItemDamage());
        if (entityName != null)
        {
            list.add(LOTREntities.getFullEntityName(entityName));
        }
	}

	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int i)
    {
        LOTREntities.SpawnEggInfo info = (LOTREntities.SpawnEggInfo)LOTREntities.creatures.get(Integer.valueOf(itemstack.getItemDamage()));
        return info != null ? (i == 0 ? info.primaryColor : info.secondaryColor) : 16777215;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            Block block = world.getBlock(i, j, k);
            i += Facing.offsetsXForSide[l];
            j += Facing.offsetsYForSide[l];
            k += Facing.offsetsZForSide[l];
            double d = 0.0D;

            if (l == 1 && block != null && block.getRenderType() == 11)
            {
                d = 0.5D;
            }

            Entity entity = spawnCreature(world, itemstack.getItemDamage(), (double)i + 0.5D, (double)j + d, (double)k + 0.5D);

            if (entity != null)
            {
                if (entity instanceof EntityLiving && itemstack.hasDisplayName())
                {
                    ((EntityLiving)entity).setCustomNameTag(itemstack.getDisplayName());
                }
				
				if (entity instanceof LOTREntityNPC)
				{
					((LOTREntityNPC)entity).isNPCPersistent = true;
					((LOTREntityNPC)entity).onArtificalSpawn();
				}

                if (!entityplayer.capabilities.isCreativeMode)
                {
                    itemstack.stackSize--;
                }
            }

            return true;
        }
    }

    public static Entity spawnCreature(World world, int id, double d, double d1, double d2)
    {
        if (!LOTREntities.creatures.containsKey(Integer.valueOf(id)))
        {
            return null;
        }
        else
        {
            Entity entity = LOTREntities.createEntityByID(id, world);
			if (entity != null && entity instanceof EntityLiving)
			{
				EntityLiving entityliving = (EntityLiving)entity;
				entityliving.setLocationAndAngles(d, d1, d2, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
				entityliving.rotationYawHead = entityliving.rotationYaw;
				entityliving.renderYawOffset = entityliving.rotationYaw;
				entityliving.onSpawnWithEgg(null);
				world.spawnEntityInWorld(entityliving);
				entityliving.playLivingSound();
			}
            return entity;
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int i, int j)
    {
        return Items.spawn_egg.getIconFromDamageForRenderPass(i, j);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister) {}

	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        Iterator it = LOTREntities.creatures.values().iterator();

        while (it.hasNext())
        {
        	LOTREntities.SpawnEggInfo info = (LOTREntities.SpawnEggInfo)it.next();
            list.add(new ItemStack(item, 1, info.spawnedID));
        }
    }
}
