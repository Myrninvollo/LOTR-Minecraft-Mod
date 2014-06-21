package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenFangorn;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeHooks;

public abstract class LOTREntityTree extends LOTREntityNPC
{
	public static Block[] WOOD_BLOCKS = new Block[] {Blocks.log, LOTRMod.wood2, Blocks.log};
	public static Block[] LEAF_BLOCKS = new Block[] {Blocks.leaves, LOTRMod.leaves2, Blocks.leaves};
	
	public static int[] WOOD_META = new int[] {0, 1, 2};
	public static int[] LEAF_META = new int[] {0, 1, 2};

	public static String[] TYPES = new String[] {"oak", "beech", "birch"};
	
	public LOTREntityTree(World world)
	{
		super(world);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
		if (rand.nextInt(9) == 0)
		{
			setTreeType(2);
		}
		else if (rand.nextInt(3) == 0)
		{
			setTreeType(1);
		}
		else
		{
			setTreeType(0);
		}
	}
	
	public int getTreeType()
	{
		return dataWatcher.getWatchableObjectByte(16);
	}
	
	public void setTreeType(int i)
	{
		dataWatcher.updateObject(16, Byte.valueOf((byte)i));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("EntType", (byte)getTreeType());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setTreeType(nbt.getByte("EntType"));
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public void setAttackTarget(EntityLivingBase entity)
	{
		if (entity instanceof LOTREntityTree)
		{
			return;
		}
		super.setAttackTarget(entity);
	}
	
	@Override
    public void knockBack(Entity entity, float f, double d, double d1)
    {
        super.knockBack(entity, f, d, d1);
		motionX /= 2D;
		motionY /= 2D;
		motionZ /= 2D;
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		if (damagesource.isFireDamage())
		{
			f *= 2F;
		}
		else if (damagesource.getEntity() instanceof EntityLivingBase)
		{
			ItemStack itemstack = ((EntityLivingBase)damagesource.getEntity()).getHeldItem();
			if (itemstack != null && ForgeHooks.canToolHarvestBlock(Blocks.log, 0, itemstack))
			{
				f *= 2F;
			}
		}
		return super.attackEntityFrom(damagesource, f);
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		int j = 1 + rand.nextInt(4) + rand.nextInt(3 * (i + 1));
		for (int k = 0; k < j; k++)
		{
			int treeType = getTreeType();
			if (treeType < 0 || treeType > WOOD_BLOCKS.length)
			{
				treeType = 0;
			}
			entityDropItem(new ItemStack(WOOD_BLOCKS[treeType], 1, WOOD_META[treeType]), 0F);
		}
		
		j = rand.nextInt(6) + rand.nextInt(3 * (i + 1));
		for (int k = 0; k < j; k++)
		{
			dropItem(Items.stick, 1);
		}
	}
	
	@Override
	public boolean canDropPouch()
	{
		return false;
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		if (super.getCanSpawnHere())
		{
			if (liftSpawnRestrictions)
			{
				return true;
			}
			else
			{
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(boundingBox.minY);
				int k = MathHelper.floor_double(posZ);
				if (j > 62 && worldObj.getBlock(i, j - 1, k) == Blocks.grass)
				{
					BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
					return isTreeHomeBiome(biome);
				}
			}
		}
		return false;
	}

	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (isTreeHomeBiome(biome))
		{
			f += 20F;
		}
		return f;
	}
	
	protected boolean isTreeHomeBiome(BiomeGenBase biome)
	{
		return biome instanceof LOTRBiomeGenFangorn;
	}
}
