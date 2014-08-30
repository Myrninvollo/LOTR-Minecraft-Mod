package lotr.client.render;

import lotr.client.render.tileentity.*;
import lotr.common.LOTRMod;
import lotr.common.block.*;
import lotr.common.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class LOTRRenderBlocks implements ISimpleBlockRenderingHandler
{
	private boolean renderInvIn3D;
	
	public LOTRRenderBlocks(boolean flag)
	{
		renderInvIn3D = flag;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int i, int j, int k, Block block, int id, RenderBlocks renderblocks)
	{
		if (id == LOTRMod.proxy.getBeaconRenderID())
		{
			renderBeacon(world, i, j, k, renderblocks);
			return true;
		}
		if (id == LOTRMod.proxy.getBarrelRenderID())
		{
			renderBarrel(world, i, j, k, block, renderblocks);
			return true;
		}
		if (id == LOTRMod.proxy.getOrcBombRenderID())
		{
			renderOrcBomb(world, i, j, k, block, renderblocks);
			return true;
		}
		if (id == LOTRMod.proxy.getOrcTorchRenderID())
		{
			renderOrcTorch(world, i, j, k, block, renderblocks);
			return true;
		}
		if (id == LOTRMod.proxy.getMobSpawnerRenderID())
		{
			return renderblocks.renderStandardBlock(block, i, j, k);
		}
		if (id == LOTRMod.proxy.getPlateRenderID())
		{
			renderPlate(world, i, j, k, block, renderblocks);
			return true;
		}
		if (id == LOTRMod.proxy.getStalactiteRenderID())
		{
			renderStalactite(world, i, j, k, block, renderblocks);
			return true;
		}
		if (id == LOTRMod.proxy.getFlowerPotRenderID())
		{
			renderFlowerPot(world, i, j, k, block, renderblocks);
			return true;
		}
		if (id == LOTRMod.proxy.getCloverRenderID())
		{
			renderClover(world, i, j, k, block, renderblocks, world.getBlockMetadata(i, j, k) == 1 ? 4 : 3, true);
			return true;
		}
		if (id == LOTRMod.proxy.getEntJarRenderID())
		{
			renderEntJar(world, i, j, k, block, renderblocks);
			return true;
		}
		if (id == LOTRMod.proxy.getFenceRenderID())
		{
			return renderblocks.renderBlockFence((BlockFence)block, i, j, k);
		}
		
		return false;
	}
	
	@Override
    public void renderInventoryBlock(Block block, int meta, int id, RenderBlocks renderblocks)
	{
		if (id == LOTRMod.proxy.getBeaconRenderID())
		{
			((LOTRRenderBeacon)TileEntityRendererDispatcher.instance.getSpecialRendererByClass(LOTRTileEntityBeacon.class)).renderInvBeacon();
		}
		if (id == LOTRMod.proxy.getBarrelRenderID())
		{
			renderInvBarrel(block, renderblocks);
		}
		if (id == LOTRMod.proxy.getOrcBombRenderID())
		{
			renderInvOrcBomb(block, meta, renderblocks);
		}
		if (id == LOTRMod.proxy.getMobSpawnerRenderID())
		{
			renderStandardInvBlock(renderblocks, block, 0F, 0F, 0F, 1F, 1F, 1F);
			((LOTRTileEntityMobSpawnerRenderer)TileEntityRendererDispatcher.instance.getSpecialRendererByClass(LOTRTileEntityMobSpawner.class)).renderInvMobSpawner(meta);
		}
		if (id == LOTRMod.proxy.getStalactiteRenderID())
		{
			renderInvStalactite(block, meta, renderblocks);
		}
		if (id == LOTRMod.proxy.getCloverRenderID())
		{
			renderInvClover(block, renderblocks, meta == 1 ? 4 : 3);
		}
		if (id == LOTRMod.proxy.getEntJarRenderID())
		{
			renderInvEntJar(block, renderblocks);
		}
		if (id == LOTRMod.proxy.getTrollTotemRenderID())
		{
			((LOTRRenderTrollTotem)TileEntityRendererDispatcher.instance.getSpecialRendererByClass(LOTRTileEntityTrollTotem.class)).renderInvTrollTotem(meta);
		}
		if (id == LOTRMod.proxy.getFenceRenderID())
		{
			renderInvFence(block, meta, renderblocks);
		}
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelID)
	{
		return renderInvIn3D;
	}
	
	@Override
	public int getRenderId()
	{
		return 0;
	}
	
	private void renderBeacon(IBlockAccess world, int i, int j, int k, RenderBlocks renderblocks)
	{
		if (LOTRBlockBeacon.isLit(world, i, j, k) && LOTRBlockBeacon.getLitCounter(world, i, j, k) == 100)
		{
			renderblocks.renderBlockFire(Blocks.fire, i, j, k);
		}
	}
	
	private void renderBarrel(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks)
	{
		int ao = getAO();
		setAO(0);
		renderblocks.renderAllFaces = true;
		renderblocks.setRenderBounds(0.125F, 0F, 0.125F, 0.25F, 0.8125F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.75F, 0F, 0.125F, 0.875F, 0.8125F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0F, 0.125F, 0.75F, 0.8125F, 0.25F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0F, 0.75F, 0.75F, 0.8125F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0.0625F, 0.25F, 0.75F, 0.75F, 0.75F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		renderblocks.setOverrideBlockTexture(block.getBlockTextureFromSide(-1));
		int meta = world.getBlockMetadata(i, j, k);
		if (meta == 2)
		{
			renderblocks.setRenderBounds(0.4375F, 0.25F, 0F, 0.5625F, 0.375F, 0.125F);
			renderblocks.renderStandardBlock(block, i, j, k);
			renderblocks.setRenderBounds(0.46875F, 0.125F, 0F, 0.53125F, 0.25F, 0.0625F);
			renderblocks.renderStandardBlock(block, i, j, k);
		}
		else if (meta == 3)
		{
			renderblocks.setRenderBounds(0.4375F, 0.25F, 0.875F, 0.5625F, 0.375F, 1F);
			renderblocks.renderStandardBlock(block, i, j, k);
			renderblocks.setRenderBounds(0.46875F, 0.125F, 0.9375F, 0.53125F, 0.25F, 1F);
			renderblocks.renderStandardBlock(block, i, j, k);
		}
		else if (meta == 4)
		{
			renderblocks.setRenderBounds(0F, 0.25F, 0.4375F, 0.125F, 0.375F, 0.5625F);
			renderblocks.renderStandardBlock(block, i, j, k);
			renderblocks.setRenderBounds(0F, 0.125F, 0.46875F, 0.0625F, 0.25F, 0.53125F);
			renderblocks.renderStandardBlock(block, i, j, k);
		}
		else if (meta == 5)
		{
			renderblocks.setRenderBounds(0.875F, 0.25F, 0.4375F, 1F, 0.375F, 0.5625F);
			renderblocks.renderStandardBlock(block, i, j, k);
			renderblocks.setRenderBounds(0.9375F, 0.125F, 0.46875F, 1F, 0.25F, 0.53125F);
			renderblocks.renderStandardBlock(block, i, j, k);
		}
		
		renderblocks.clearOverrideBlockTexture();
		renderblocks.renderAllFaces = false;
		setAO(ao);
	}
	
	private void renderInvBarrel(Block block, RenderBlocks renderblocks)
	{
		renderblocks.renderAllFaces = true;
		renderStandardInvBlock(renderblocks, block, 0.125F, 0F, 0.125F, 0.25F, 0.8125F, 0.875F);
		renderStandardInvBlock(renderblocks, block, 0.75F, 0F, 0.125F, 0.875F, 0.8125F, 0.875F);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0F, 0.125F, 0.75F, 0.8125F, 0.25F);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0F, 0.75F, 0.75F, 0.8125F, 0.875F);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0.0625F, 0.25F, 0.75F, 0.75F, 0.75F);
		renderblocks.setOverrideBlockTexture(block.getBlockTextureFromSide(-1));
		renderStandardInvBlock(renderblocks, block, 0.875F, 0.25F, 0.4375F, 1F, 0.375F, 0.5625F);
		renderStandardInvBlock(renderblocks, block, 0.9375F, 0.125F, 0.46875F, 1F, 0.25F, 0.53125F);
		renderblocks.clearOverrideBlockTexture();
		renderblocks.renderAllFaces = false;
	}
	
	private void renderOrcBomb(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks)
	{
		int ao = getAO();
		setAO(0);
		renderblocks.renderAllFaces = true;
		renderblocks.setRenderBounds(0.125F, 0.1875F, 0.125F, 0.875F, 0.9375F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.375F, 0.9375F, 0.375F, 0.625F, 1F, 0.625F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0.9375F, 0.375F, 0.3125F, 1F, 0.4375F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0.9375F, 0.5625F, 0.3125F, 1F, 0.625F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.6875F, 0.9375F, 0.375F, 0.75F, 1F, 0.4375F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.6875F, 0.9375F, 0.5625F, 0.75F, 1F, 0.625F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.375F, 0.9375F, 0.25F, 0.4375F, 1F, 0.3125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.5625F, 0.9375F, 0.25F, 0.625F, 1F, 0.3125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.375F, 0.9375F, 0.6875F, 0.4375F, 1F, 0.75F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.5625F, 0.9375F, 0.6875F, 0.625F, 1F, 0.75F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.125F, 0F, 0.25F, 0.1875F, 0.1875F, 0.75F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.8125F, 0F, 0.25F, 0.875F, 0.1875F, 0.75F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0F, 0.125F, 0.75F, 0.1875F, 0.1875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0F, 0.8125F, 0.75F, 0.1875F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setOverrideBlockTexture(block.getIcon(-1, world.getBlockMetadata(i, j, k)));
		renderblocks.setRenderBounds(0F, 0.625F, 0.3125F, 0.0625F, 0.6875F, 0.6875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.0625F, 0.625F, 0.3125F, 0.125F, 0.6875F, 0.375F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.0625F, 0.625F, 0.625F, 0.125F, 0.6875F, 0.6875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.9375F, 0.625F, 0.3125F, 1F, 0.6875F, 0.6875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.875F, 0.625F, 0.3125F, 0.9375F, 0.6875F, 0.375F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.875F, 0.625F, 0.625F, 0.9375F, 0.6875F, 0.6875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.3125F, 0.625F, 0F, 0.6875F, 0.6875F, 0.0625F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.3125F, 0.625F, 0.0625F, 0.375F, 0.6875F, 0.125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.625F, 0.625F, 0.0625F, 0.6875F, 0.6875F, 0.125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.3125F, 0.625F, 0.9375F, 0.6875F, 0.6875F, 1F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.3125F, 0.625F, 0.875F, 0.375F, 0.6875F, 0.9375F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.625F, 0.625F, 0.875F, 0.6875F, 0.6875F, 0.9375F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.clearOverrideBlockTexture();
		renderblocks.renderAllFaces = false;
		setAO(ao);
	}
	
	private void renderInvOrcBomb(Block block, int meta, RenderBlocks renderblocks)
	{
		renderblocks.renderAllFaces = true;
		renderStandardInvBlock(renderblocks, block, 0.125F, 0.1875F, 0.125F, 0.875F, 0.9375F, 0.875F, meta);
		renderStandardInvBlock(renderblocks, block, 0.375F, 0.9375F, 0.375F, 0.625F, 1F, 0.625F, meta);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0.9375F, 0.375F, 0.3125F, 1F, 0.4375F, meta);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0.9375F, 0.5625F, 0.3125F, 1F, 0.625F, meta);
		renderStandardInvBlock(renderblocks, block, 0.6875F, 0.9375F, 0.375F, 0.75F, 1F, 0.4375F, meta);
		renderStandardInvBlock(renderblocks, block, 0.6875F, 0.9375F, 0.5625F, 0.75F, 1F, 0.625F, meta);
		renderStandardInvBlock(renderblocks, block, 0.375F, 0.9375F, 0.25F, 0.4375F, 1F, 0.3125F, meta);
		renderStandardInvBlock(renderblocks, block, 0.5625F, 0.9375F, 0.25F, 0.625F, 1F, 0.3125F, meta);
		renderStandardInvBlock(renderblocks, block, 0.375F, 0.9375F, 0.6875F, 0.4375F, 1F, 0.75F, meta);
		renderStandardInvBlock(renderblocks, block, 0.5625F, 0.9375F, 0.6875F, 0.625F, 1F, 0.75F, meta);
		renderStandardInvBlock(renderblocks, block, 0.125F, 0F, 0.25F, 0.1875F, 0.1875F, 0.75F, meta);
		renderStandardInvBlock(renderblocks, block, 0.8125F, 0F, 0.25F, 0.875F, 0.1875F, 0.75F, meta);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0F, 0.125F, 0.75F, 0.1875F, 0.1875F, meta);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0F, 0.8125F, 0.75F, 0.1875F, 0.875F, meta);
		renderblocks.setOverrideBlockTexture(block.getIcon(-1, meta));
		renderStandardInvBlock(renderblocks, block, 0F, 0.625F, 0.3125F, 0.0625F, 0.6875F, 0.6875F, meta);
		renderStandardInvBlock(renderblocks, block, 0.0625F, 0.625F, 0.3125F, 0.125F, 0.6875F, 0.375F, meta);
		renderStandardInvBlock(renderblocks, block, 0.0625F, 0.625F, 0.625F, 0.125F, 0.6875F, 0.6875F, meta);
		renderStandardInvBlock(renderblocks, block, 0.9375F, 0.625F, 0.3125F, 1F, 0.6875F, 0.6875F, meta);
		renderStandardInvBlock(renderblocks, block, 0.875F, 0.625F, 0.3125F, 0.9375F, 0.6875F, 0.375F, meta);
		renderStandardInvBlock(renderblocks, block, 0.875F, 0.625F, 0.625F, 0.9375F, 0.6875F, 0.6875F, meta);
		renderStandardInvBlock(renderblocks, block, 0.3125F, 0.625F, 0F, 0.6875F, 0.6875F, 0.0625F, meta);
		renderStandardInvBlock(renderblocks, block, 0.3125F, 0.625F, 0.0625F, 0.375F, 0.6875F, 0.125F, meta);
		renderStandardInvBlock(renderblocks, block, 0.625F, 0.625F, 0.0625F, 0.6875F, 0.6875F, 0.125F, meta);
		renderStandardInvBlock(renderblocks, block, 0.3125F, 0.625F, 0.9375F, 0.6875F, 0.6875F, 1F, meta);
		renderStandardInvBlock(renderblocks, block, 0.3125F, 0.625F, 0.875F, 0.375F, 0.6875F, 0.9375F, meta);
		renderStandardInvBlock(renderblocks, block, 0.625F, 0.625F, 0.875F, 0.6875F, 0.6875F, 0.9375F, meta);
		renderblocks.clearOverrideBlockTexture();
		renderblocks.renderAllFaces = false;
	}
	
    private void renderOrcTorch(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks)
    {
        int meta = world.getBlockMetadata(i, j, k);
		if (meta == 0)
		{
			renderblocks.setRenderBounds(0.4375F, 0F, 0.4375F, 0.5625F, 1F, 0.5625F);
			renderblocks.renderStandardBlock(block, i, j, k);
		}
		else if (meta == 1)
		{
			renderblocks.setRenderBounds(0.4375F, 0F, 0.4375F, 0.5625F, 0.5F, 0.5625F);
			renderblocks.renderStandardBlock(block, i, j, k);
		}
    }
	
	private void renderPlate(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks)
	{
		int ao = getAO();
		setAO(0);
		renderblocks.renderAllFaces = true;
		renderblocks.setRenderBounds(0.1875F, 0F, 0.1875F, 0.8125F, 0.0625F, 0.8125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.125F, 0.0625F, 0.125F, 0.875F, 0.125F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.renderAllFaces = false;
		setAO(ao);
	}
	
	public static void renderEntityPlate(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks)
	{
		renderblocks.renderAllFaces = true;
		renderStandardInvBlock(renderblocks, block, 0.1875F, 0F, 0.1875F, 0.8125F, 0.0625F, 0.8125F);
		renderStandardInvBlock(renderblocks, block, 0.125F, 0.0625F, 0.125F, 0.875F, 0.125F, 0.875F);
		renderblocks.renderAllFaces = false;
	}
	
	private void renderStalactite(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks)
	{
		int ao = getAO();
		setAO(0);
		renderblocks.renderAllFaces = true;
		int metadata = world.getBlockMetadata(i, j, k);
		for (int l = 0; l < 16; l++)
		{
			if (metadata == 0)
			{
				renderblocks.setRenderBounds(0.5F - (l / 16F * 0.25F), l / 16F, 0.5F - (l / 16F * 0.25F), 0.5F + (l / 16F * 0.25F), (l + 1) / 16F, 0.5F + (l / 16F * 0.25F));
				renderblocks.renderStandardBlock(block, i, j, k);
			}
			else if (metadata == 1)
			{
				renderblocks.setRenderBounds(0.25F + (l / 16F * 0.25F), l / 16F, 0.25F + (l / 16F * 0.25F), 0.75F - (l / 16F * 0.25F), (l + 1) / 16F, 0.75F - (l / 16F * 0.25F));
				renderblocks.renderStandardBlock(block, i, j, k);
			}
		}
		renderblocks.renderAllFaces = false;
		setAO(ao);
	}
	
	private void renderInvStalactite(Block block, int metadata, RenderBlocks renderblocks)
	{
		renderblocks.renderAllFaces = true;
		for (int l = 0; l < 16; l++)
		{
			if (metadata == 0)
			{
				renderStandardInvBlock(renderblocks, block, 0.5F - (l / 16F * 0.25F), l / 16F, 0.5F - (l / 16F * 0.25F), 0.5F + (l / 16F * 0.25F), (l + 1) / 16F, 0.5F + (l / 16F * 0.25F));
			}
			else if (metadata == 1)
			{
				renderStandardInvBlock(renderblocks, block, 0.25F + (l / 16F * 0.25F), l / 16F, 0.25F + (l / 16F * 0.25F), 0.75F - (l / 16F * 0.25F), (l + 1) / 16F, 0.75F - (l / 16F * 0.25F));
			}
		}
		renderblocks.renderAllFaces = false;
	}
	
	private void renderFlowerPot(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks)
    {
        renderblocks.renderStandardBlock(block, i, j, k);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));
        float f = 1F;
        int l = block.colorMultiplier(world, i, j, k);
        IIcon icon = renderblocks.getBlockIconFromSide(block, 0);
        float f1 = (float)(l >> 16 & 255) / 255F;
        float f2 = (float)(l >> 8 & 255) / 255F;
        float f3 = (float)(l & 255) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        float f4 = 0.1865F;
        renderblocks.renderFaceXPos(block, (double)((float)i - 0.5F + f4), (double)j, (double)k, icon);
        renderblocks.renderFaceXNeg(block, (double)((float)i + 0.5F - f4), (double)j, (double)k, icon);
        renderblocks.renderFaceZPos(block, (double)i, (double)j, (double)((float)k - 0.5F + f4), icon);
        renderblocks.renderFaceZNeg(block, (double)i, (double)j, (double)((float)k + 0.5F - f4), icon);
        renderblocks.renderFaceYPos(block, (double)i, (double)((float)j - 0.5F + f4 + 0.1875F), (double)k, renderblocks.getBlockIcon(Blocks.dirt));
		
        ItemStack plant = LOTRBlockFlowerPot.getPlant(world, i, j, k);
		if (plant == null)
		{
			return;
		}
		Block plantBlock = Block.getBlockFromItem(plant.getItem());

		tessellator.addTranslation(0F, 0.25F, 0F);

		if (plantBlock == LOTRMod.shireHeather)
		{
			renderblocks.drawCrossedSquares(plantBlock.getIcon(2, plant.getItemDamage()), (double)i, (double)j, (double)k, 0.6F);
		}
		else if (plantBlock == LOTRMod.clover)
		{
			renderClover(world, i, j, k, plantBlock, renderblocks, plant.getItemDamage() == 1 ? 4 : 3, false);
		}
		else if (plantBlock instanceof LOTRBlockFlower)
		{
			renderblocks.drawCrossedSquares(plantBlock.getIcon(2, plant.getItemDamage()), (double)i, (double)j, (double)k, 0.75F);
		}
		else
		{
			renderblocks.renderBlockByRenderType(plantBlock, i, j, k);
		}

		tessellator.addTranslation(0F, -0.25F, 0F);
    }
	
	private static void renderClover(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks, int petalCount, boolean randomTranslation)
    {
		double scale = 0.5D;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));
        int l = block.colorMultiplier(world, i, j, k);
		float f = 1F;
        float f1 = (float)(l >> 16 & 255) / 255F;
        float f2 = (float)(l >> 8 & 255) / 255F;
        float f3 = (float)(l & 255) / 255F;
        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }
        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		renderblocks.setOverrideBlockTexture(LOTRBlockClover.stemIcon);
		double posX = (double)i;
		double posY = (double)j;
		double posZ = (double)k;
		if (randomTranslation)
		{
            long seed = (long)(i * 3129871) ^ (long)k * 116129781L ^ (long)j;
            seed = seed * seed * 42317861L + seed * 11L;
            posX += ((double)((float)(seed >> 16 & 15L) / 15F) - 0.5D) * 0.5D;
            posZ += ((double)((float)(seed >> 24 & 15L) / 15F) - 0.5D) * 0.5D;
		}
		renderblocks.drawCrossedSquares(block.getIcon(2, 0), posX, posY, posZ, (float)scale);
		renderblocks.clearOverrideBlockTexture();
		
		for (int petal = 0; petal < petalCount; petal++)
		{
			float rotation = ((float)petal / (float)petalCount) * (float)Math.PI * 2F;
			IIcon icon = LOTRBlockClover.petalIcon;
			double d = (double)icon.getMinU();
			double d1 = (double)icon.getMinV();
			double d2 = (double)icon.getMaxU();
			double d3 = (double)icon.getMaxV();
			double d4 = posX + 0.5D;
			double d5 = posY + (0.5D * scale) + ((double)petal * 0.0025D);
			double d6 = posZ + 0.5D;
			Vec3[] vecs = new Vec3[]
			{
				Vec3.createVectorHelper(0.5D * scale, 0D, -0.5D * scale),
				Vec3.createVectorHelper(-0.5D * scale, 0D, -0.5D * scale),
				Vec3.createVectorHelper(-0.5D * scale, 0D, 0.5D * scale),
				Vec3.createVectorHelper(0.5D * scale, 0D, 0.5D * scale)
				
			};
			for (int l1 = 0; l1 < vecs.length; l1++)
			{
				vecs[l1].rotateAroundY(rotation);
				vecs[l1].xCoord += d4;
				vecs[l1].yCoord += d5;
				vecs[l1].zCoord += d6;
			}
			tessellator.addVertexWithUV(vecs[0].xCoord, vecs[0].yCoord, vecs[0].zCoord, d, d1);
			tessellator.addVertexWithUV(vecs[1].xCoord, vecs[1].yCoord, vecs[1].zCoord, d, d3);
			tessellator.addVertexWithUV(vecs[2].xCoord, vecs[2].yCoord, vecs[2].zCoord, d2, d3);
			tessellator.addVertexWithUV(vecs[3].xCoord, vecs[3].yCoord, vecs[3].zCoord, d2, d1);
		}
	}
	
	private static void renderInvClover(Block block, RenderBlocks renderblocks, int petalCount)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
		double scale = 1D;
		Tessellator tessellator = Tessellator.instance;
        int l = block.getRenderColor(0);
		float f = 1F;
        float f1 = (float)(l >> 16 & 255) / 255F;
        float f2 = (float)(l >> 8 & 255) / 255F;
        float f3 = (float)(l & 255) / 255F;
        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }
        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		renderblocks.setOverrideBlockTexture(LOTRBlockClover.stemIcon);
		tessellator.startDrawingQuads();
		renderblocks.drawCrossedSquares(block.getIcon(2, 0), -scale * 0.5D, -scale * 0.5D, -scale * 0.5D, (float)scale);
		tessellator.draw();
		renderblocks.clearOverrideBlockTexture();
		
		for (int petal = 0; petal < petalCount; petal++)
		{
			float rotation = ((float)petal / (float)petalCount) * (float)Math.PI * 2F;
			IIcon icon = LOTRBlockClover.petalIcon;
			double d = (double)icon.getMinU();
			double d1 = (double)icon.getMinV();
			double d2 = (double)icon.getMaxU();
			double d3 = (double)icon.getMaxV();
			double d4 = 0D;
			double d5 = ((double)petal * 0.0025D);
			double d6 = 0D;
			Vec3[] vecs = new Vec3[]
			{
				Vec3.createVectorHelper(0.5D * scale, 0D, -0.5D * scale),
				Vec3.createVectorHelper(-0.5D * scale, 0D, -0.5D * scale),
				Vec3.createVectorHelper(-0.5D * scale, 0D, 0.5D * scale),
				Vec3.createVectorHelper(0.5D * scale, 0D, 0.5D * scale)
			};
			for (int l1 = 0; l1 < vecs.length; l1++)
			{
				vecs[l1].rotateAroundY(rotation);
				vecs[l1].xCoord += d4;
				vecs[l1].yCoord += d5;
				vecs[l1].zCoord += d6;
			}
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(vecs[0].xCoord, vecs[0].yCoord, vecs[0].zCoord, d, d1);
			tessellator.addVertexWithUV(vecs[1].xCoord, vecs[1].yCoord, vecs[1].zCoord, d, d3);
			tessellator.addVertexWithUV(vecs[2].xCoord, vecs[2].yCoord, vecs[2].zCoord, d2, d3);
			tessellator.addVertexWithUV(vecs[3].xCoord, vecs[3].yCoord, vecs[3].zCoord, d2, d1);
			tessellator.draw();
		}
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	public static void renderDwarvenDoorGlow(RenderBlocks renderblocks, int i, int j, int k)
	{
		LOTRBlockDwarvenDoor block = (LOTRBlockDwarvenDoor)LOTRMod.dwarvenDoor;
        Tessellator tessellator = Tessellator.instance;
		
        int l = renderblocks.blockAccess.getBlockMetadata(i, j, k);

        if ((l & 8) != 0)
        {
            if (renderblocks.blockAccess.getBlock(i, j - 1, k) != block)
            {
                return;
            }
        }
        else if (renderblocks.blockAccess.getBlock(i, j + 1, k) != block)
        {
            return;
        }

		block.setBlockBoundsBasedOnState(renderblocks.blockAccess, i, j, k);
		renderblocks.setRenderBoundsFromBlock(block);
		IIcon icon = renderblocks.getIconSafe(block.getGlowTexture(renderblocks.blockAccess, i, j, k, 2));
		tessellator.startDrawingQuads();
        renderblocks.renderFaceZNeg(block, 0D, 0D, -0.01D, icon);
		tessellator.draw();
        renderblocks.flipTexture = false;
        icon = renderblocks.getIconSafe(block.getGlowTexture(renderblocks.blockAccess, i, j, k, 3));
		tessellator.startDrawingQuads();
        renderblocks.renderFaceZPos(block, 0D, 0D, 0.01D, icon);
		tessellator.draw();
        renderblocks.flipTexture = false;
        icon = renderblocks.getIconSafe(block.getGlowTexture(renderblocks.blockAccess, i, j, k, 4));
		tessellator.startDrawingQuads();
        renderblocks.renderFaceXNeg(block, -0.01D, 0D, 0D, icon);
		tessellator.draw();
        renderblocks.flipTexture = false;
        icon = renderblocks.getIconSafe(block.getGlowTexture(renderblocks.blockAccess, i, j, k, 5));
		tessellator.startDrawingQuads();
        renderblocks.renderFaceXPos(block, 0.01D, 0D, 0D, icon);
		tessellator.draw();
        renderblocks.flipTexture = false;
	}
	
	private void renderEntJar(IBlockAccess world, int i, int j, int k, Block block, RenderBlocks renderblocks)
	{
		int ao = getAO();
		setAO(0);
		renderblocks.renderAllFaces = true;
		renderblocks.setRenderBounds(0.25F, 0F, 0.25F, 0.75F, 0.0625F, 0.75F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0.0625F, 0.25F, 0.75F, 0.875F, 0.3125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0.0625F, 0.6875F, 0.75F, 0.875F, 0.75F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.25F, 0.0625F, 0.3125F, 0.31255F, 0.875F, 0.6875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.6875F, 0.0625F, 0.3125F, 0.75F, 0.875F, 0.6875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.renderAllFaces = false;
		setAO(ao);
	}
	
	private void renderInvEntJar(Block block, RenderBlocks renderblocks)
	{
		renderblocks.renderAllFaces = true;
		renderStandardInvBlock(renderblocks, block, 0.25F, 0F, 0.25F, 0.75F, 0.0625F, 0.75F);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0.0625F, 0.25F, 0.75F, 0.875F, 0.3125F);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0.0625F, 0.6875F, 0.75F, 0.875F, 0.75F);
		renderStandardInvBlock(renderblocks, block, 0.25F, 0.0625F, 0.3125F, 0.31255F, 0.875F, 0.6875F);
		renderStandardInvBlock(renderblocks, block, 0.6875F, 0.0625F, 0.3125F, 0.75F, 0.875F, 0.6875F);
		renderblocks.renderAllFaces = false;
	}
	
	private void renderInvFence(Block block, int meta, RenderBlocks renderblocks)
	{
		for (int k = 0; k < 4; ++k)
		{
			float f = 0.125F;
			float f1 = 0.0625F;

			if (k == 0)
			{
				renderblocks.setRenderBounds((double)(0.5F - f), 0D, 0D, (double)(0.5F + f), 1D, (double)(f * 2F));
			}

			if (k == 1)
			{
				renderblocks.setRenderBounds((double)(0.5F - f), 0D, (double)(1F - f * 2F), (double)(0.5F + f), 1D, 1D);
			}

			if (k == 2)
			{
				renderblocks.setRenderBounds((double)(0.5F - f1), (double)(1F - f1 * 3F), (double)(-f1 * 2F), (double)(0.5F + f1), (double)(1F - f1), (double)(1F + f1 * 2F));
			}

			if (k == 3)
			{
				renderblocks.setRenderBounds((double)(0.5F - f1), (double)(0.5F - f1 * 3F), (double)(-f1 * 2F), (double)(0.5F + f1), (double)(0.5F - f1), (double)(1F + f1 * 2F));
			}

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setNormal(0F, -1F, 0F);
			renderblocks.renderFaceYNeg(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 0, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0F, 1F, 0F);
			renderblocks.renderFaceYPos(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 1, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0F, 0F, -1F);
			renderblocks.renderFaceZNeg(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 2, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0F, 0F, 1F);
			renderblocks.renderFaceZPos(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 3, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1F, 0F, 0F);
			renderblocks.renderFaceXNeg(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 4, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1F, 0F, 0F);
			renderblocks.renderFaceXPos(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 5, meta));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

		renderblocks.setRenderBounds(0D, 0D, 0D, 1D, 1D, 1D);
	}
	
	private static void renderStandardInvBlock(RenderBlocks renderblocks, Block block, float f, float f1, float f2, float f3, float f4, float f5)
	{
		renderStandardInvBlock(renderblocks, block, f, f1, f2, f3, f4, f5, 0);
	}
	
	private static void renderStandardInvBlock(RenderBlocks renderblocks, Block block, float f, float f1, float f2, float f3, float f4, float f5, int metadata)
	{
        Tessellator tessellator = Tessellator.instance;
        renderblocks.setRenderBounds(f, f1, f2, f3, f4, f5);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, -1F, 0F);
        renderblocks.renderFaceYNeg(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 1F, 0F);
        renderblocks.renderFaceYPos(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, -1F);
        renderblocks.renderFaceZNeg(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, 1F);
        renderblocks.renderFaceZPos(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0F, 0F);
        renderblocks.renderFaceXNeg(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1F, 0F, 0F);
        renderblocks.renderFaceXPos(block, 0D, 0D, 0D, renderblocks.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	private static int getAO()
	{
		return Minecraft.getMinecraft().gameSettings.ambientOcclusion;
	}
	
	private static void setAO(int i)
	{
		Minecraft.getMinecraft().gameSettings.ambientOcclusion = i;
	}
}
