package lotr.common.block;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityDesertScorpion;
import lotr.common.entity.npc.LOTREntityHaradPyramidWraith;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.tileentity.LOTRTileEntitySpawnerChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockSpawnerChest extends BlockChest
{
	private boolean dropChestItems = true;
	
	public LOTRBlockSpawnerChest()
	{
		super(0);
		setCreativeTab(null);
	}
	
	@Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new LOTRTileEntitySpawnerChest();
    }
	
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
	{
		spawnEntity(world, i, j, k);
		dropChestItems = false;
		if (!world.isRemote)
		{
			ItemStack[] chestInventory = new ItemStack[27];
			TileEntity tileentity = world.getTileEntity(i, j, k);
			if (tileentity != null && tileentity instanceof TileEntityChest)
			{
				for (int l = 0; l < 27; l++)
				{
					chestInventory[l] = ((TileEntityChest)tileentity).getStackInSlot(l);
				}
			}
			
			world.setBlock(i, j, k, Blocks.chest, world.getBlockMetadata(i, j, k), 3);
			for (int l = 0; l < 27; l++)
			{
				((TileEntityChest)world.getTileEntity(i, j, k)).setInventorySlotContents(l, chestInventory[l]);
			}
		}
		dropChestItems = true;
		return true;
	}
	
	@Override
	public void breakBlock(World world, int i, int j, int k, Block block, int meta)
	{
		if (dropChestItems)
		{
			spawnEntity(world, i, j, k);
			super.breakBlock(world, i, j, k, block, meta);
		}
		else
		{
			world.removeTileEntity(i, j, k);
		}
	}
	
	private void spawnEntity(World world, int i, int j, int k)
	{
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity == null || !(tileentity instanceof LOTRTileEntitySpawnerChest))
		{
			return;
		}
		
		Entity entity = ((LOTRTileEntitySpawnerChest)tileentity).createMob();
		if (entity == null || !(entity instanceof EntityLiving))
		{
			return;
		}
		
		EntityLiving entityliving = (EntityLiving)entity;
		entityliving.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0F, 0F);
		entityliving.spawnExplosionParticle();
		if (!world.isRemote)
		{
			entityliving.onSpawnWithEgg(null);
			if (entityliving instanceof LOTREntityNPC)
			{
				((LOTREntityNPC)entityliving).isNPCPersistent = true;
			}
			world.spawnEntityInWorld(entityliving);
			world.playSoundAtEntity(entityliving, "lotr:wraith.spawn", 1F, 0.7F + (world.rand.nextFloat() * 0.6F));
			
			if (entityliving instanceof LOTREntityHaradPyramidWraith)
			{
				for (int l = 0; l < 4; l++)
				{
					LOTREntityDesertScorpion desertScorpion = new LOTREntityDesertScorpion(world);
					desertScorpion.pyramidSpawned = true;
					double d = entityliving.posX - world.rand.nextDouble() * 3D + world.rand.nextDouble() * 3D;
					double d1 = entityliving.posY;
					double d2 = entityliving.posZ - world.rand.nextDouble() * 3D + world.rand.nextDouble() * 3D;
					desertScorpion.setLocationAndAngles(d, d1, d2, world.rand.nextFloat() * 360F, 0F);
					if (desertScorpion.getCanSpawnHere())
					{
						world.spawnEntityInWorld(desertScorpion);
					}
				}
				
				world.addWeatherEffect(new EntityLightningBolt(world, entityliving.posX, entityliving.posY, entityliving.posZ));
			}
		}
	}
	
	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return Item.getItemFromBlock(Blocks.chest);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
		return Item.getItemFromBlock(Blocks.chest);
    }
}
