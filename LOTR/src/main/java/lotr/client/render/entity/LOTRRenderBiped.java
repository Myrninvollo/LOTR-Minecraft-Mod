package lotr.client.render.entity;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;

import java.util.UUID;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelBiped;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTRBannerBearer;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.item.LOTRItemBanner;
import lotr.common.item.LOTRItemMug;
import lotr.common.item.LOTRItemMugBrewable;
import lotr.common.item.LOTRItemSpear;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

public abstract class LOTRRenderBiped extends RenderBiped
{
	private ResourceLocation capeTexture;
	private ModelBiped capeModel = new LOTRModelBiped();
	
	public LOTRRenderBiped(ModelBiped model, float f)
	{
		super(model, f);
	}
	
	@Override
	protected void func_82421_b()
    {
        field_82423_g = new LOTRModelBiped(1F);
        field_82425_h = new LOTRModelBiped(0.5F);
    }
	
	protected void setCapeTexture(ResourceLocation r)
	{
		capeTexture = r;
	}
	
	@Override
	public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1)
	{
		super.doRender(entity, d, d1, d2, f, f1);
		
		if (Minecraft.isGuiEnabled() && entity instanceof LOTREntityNPC && ((LOTREntityNPC)entity).hiredNPCInfo.getHiringPlayer() == renderManager.livingPlayer)
		{
			if (entity.riddenByEntity == null)
			{
				func_147906_a(entity, "Hired", d, d1 + 0.5D, d2, 64);
			}
			LOTRClientProxy.renderHealthBar(entity, d, d1 + 0.5D, d2, 64, renderManager);
		}
	}
	
	@Override
	protected void func_82420_a(EntityLiving entity, ItemStack itemstack)
    {
		super.func_82420_a(entity, itemstack);
		
		setupHeldItems(entity, itemstack, true);
		setupHeldItems(entity, getHeldItemLeft(entity), false);
    }
	
	private void setupHeldItems(EntityLiving entity, ItemStack itemstack, boolean rightArm)
	{
		int value = 0;
		boolean aimBow = false;
		
		if (itemstack != null)
		{
			Item item = itemstack.getItem();
			
			if (!(item instanceof LOTRItemSpear) && item.getItemUseAction(itemstack) == EnumAction.bow)
			{
				value = 3;
				aimBow = true;
			}
			
			if (item instanceof LOTRItemBanner)
			{
				value = 3;
			}
			
			if (item instanceof ItemFood || (item instanceof LOTRItemMug && item != LOTRMod.mug) || item instanceof LOTRItemMugBrewable || item == LOTRMod.hobbitPipe)
			{
				value = 3;
			}
		}
		
		if (rightArm)
		{
			field_82423_g.heldItemRight = field_82425_h.heldItemRight = modelBipedMain.heldItemRight = value;
		}
		else
		{
			field_82423_g.heldItemLeft = field_82425_h.heldItemLeft = modelBipedMain.heldItemLeft = value;
		}
		
		field_82423_g.aimedBow = field_82425_h.aimedBow = modelBipedMain.aimedBow = aimBow;
	}
	
	@Override
	protected void renderEquippedItems(EntityLivingBase entity, float f)
	{
        GL11.glColor3f(1F, 1F, 1F);
		
		ItemStack headItem = ((EntityLiving)entity).func_130225_q(3);
        if (headItem != null)
        {
            GL11.glPushMatrix();
            modelBipedMain.bipedHead.postRender(0.0625F);

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(headItem, EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, headItem, BLOCK_3D));

            if (headItem.getItem() instanceof ItemBlock)
            {
            	if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(headItem.getItem()).getRenderType()))
                {
                    float f1 = 0.625F;
                    GL11.glTranslatef(0F, -0.25F, 0F);
                    GL11.glRotatef(90F, 0F, 1F, 0F);
                    GL11.glScalef(f1, -f1, -f1);
                }

                renderManager.itemRenderer.renderItem(entity, headItem, 0);
            }
            else if (headItem.getItem() == Items.skull)
            {
                float f1 = 1.0625F;
                GL11.glScalef(f1, -f1, -f1);
                GameProfile gameprofile = null;

                if (headItem.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = headItem.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", new NBTTagCompound().getId()))
                    {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", new NBTTagString().getId()) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner")))
                    {
                        gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    }
                }

                TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0F, -0.5F, 1, 180F, headItem.getItemDamage(), gameprofile);
            }

            GL11.glPopMatrix();
        }
		
        ItemStack heldItem = entity.getHeldItem();
        if (heldItem != null)
        {
            GL11.glPushMatrix();

            if (mainModel.isChild)
            {
                float f1 = 0.5F;
                GL11.glTranslatef(0F, 0.625F, 0F);
                GL11.glRotatef(-20F, -1F, 0F, 0F);
                GL11.glScalef(f1, f1, f1);
            }

            modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(heldItem, EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, heldItem, BLOCK_3D));

            if (heldItem.getItem() instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(heldItem.getItem()).getRenderType())))
            {
                float f1 = 0.5F;
                GL11.glTranslatef(0F, getHeldItemYTranslation(), -0.3125F);
                f1 *= 0.75F;
                GL11.glRotatef(20F, 1F, 0F, 0F);
                GL11.glRotatef(45F, 0F, 1F, 0F);
                GL11.glScalef(-f1, -f1, f1);
            }
            else if (heldItem.getItem() == Items.bow)
            {
                float f1 = 0.625F;
                GL11.glTranslatef(0F, getHeldItemYTranslation(), 0.3125F);
                GL11.glRotatef(-20F, 0F, 1F, 0F);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100F, 1F, 0F, 0F);
                GL11.glRotatef(45F, 0F, 1F, 0F);
            }
            else if (heldItem.getItem().isFull3D())
            {
                float f1 = 0.625F;

                if (heldItem.getItem().shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180F, 0F, 0F, 1F);
                    GL11.glTranslatef(0F, -getHeldItemYTranslation(), 0F);
                }

                GL11.glTranslatef(0F, getHeldItemYTranslation(), 0F);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100F, 1F, 0F, 0F);
                GL11.glRotatef(45F, 0F, 1F, 0F);
            }
            else
            {
                float f1 = 0.375F;
                GL11.glTranslatef(0.25F, getHeldItemYTranslation(), -0.1875F);
                GL11.glScalef(f1, f1, f1);
                GL11.glRotatef(60F, 0F, 0F, 1F);
                GL11.glRotatef(-90F, 1F, 0F, 0F);
                GL11.glRotatef(20F, 0F, 0F, 1F);
            }

            renderManager.itemRenderer.renderItem(entity, heldItem, 0);

            if (heldItem.getItem().requiresMultipleRenderPasses())
            {
                for (int x = 1; x < heldItem.getItem().getRenderPasses(heldItem.getItemDamage()); x++)
                {
                    renderManager.itemRenderer.renderItem(entity, heldItem, x);
                }
            }

            GL11.glPopMatrix();
        }
		
		ItemStack heldItemLeft = getHeldItemLeft(entity);
		if (heldItemLeft != null)
		{
            GL11.glPushMatrix();
            
            if (mainModel.isChild)
            {
                float f1 = 0.5F;
                GL11.glTranslatef(0F, 0.625F, 0F);
                GL11.glRotatef(-20F, -1F, 0F, 0F);
                GL11.glScalef(f1, f1, f1);
            }
            
            modelBipedMain.bipedLeftArm.postRender(0.0625F);
            GL11.glTranslatef(0.0625F, 0.4375F, 0.0625F);

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(heldItemLeft, EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, heldItemLeft, BLOCK_3D));

            if (heldItemLeft.getItem() instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(heldItemLeft.getItem()).getRenderType())))
            {
                float f1 = 0.5F;
                GL11.glTranslatef(0F, getHeldItemYTranslation(), -0.3125F);
                f1 *= 0.75F;
                GL11.glRotatef(20F, 1F, 0F, 0F);
                GL11.glRotatef(45F, 0F, 1F, 0F);
                GL11.glScalef(-f1, -f1, f1);
            }
            else if (heldItemLeft.getItem().isFull3D())
            {
                float f1 = 0.625F;

                if (heldItemLeft.getItem().shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180F, 0F, 0F, 1F);
                    GL11.glTranslatef(0F, -getHeldItemYTranslation(), 0F);
                }

                GL11.glTranslatef(0F, getHeldItemYTranslation(), 0F);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100F, 1F, 0F, 0F);
                GL11.glRotatef(45F, 0F, 1F, 0F);
            }
            else
            {
                float f1 = 0.3175F;
                GL11.glTranslatef(0F, getHeldItemYTranslation(), 0F);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100F, 1F, 0F, 0F);
                GL11.glRotatef(45F, 0F, 1F, 0F);
            }

            renderManager.itemRenderer.renderItem(entity, heldItemLeft, 0);

            if (heldItemLeft.getItem().requiresMultipleRenderPasses())
            {
                for (int i = 1; i < heldItemLeft.getItem().getRenderPasses(heldItemLeft.getItemDamage()); i++)
                {
                    renderManager.itemRenderer.renderItem(entity, heldItemLeft, i);
                }
            }

            GL11.glPopMatrix();
        }
		
		if (capeTexture != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0F, 0F, 0.125F);
			GL11.glRotatef(180F, 0F, 1F, 0F);
			GL11.glRotatef(-10F, 1F, 0F, 0F);
			bindTexture(capeTexture);
			capeModel.renderCloak(0.0625F);
			GL11.glPopMatrix();
		}
	}
	
	public float getHeldItemYTranslation()
	{
		return 0.1875F;
	}
	
	public ItemStack getHeldItemLeft(EntityLivingBase entity)
	{
		if (entity instanceof LOTRBannerBearer)
		{
			LOTRBannerBearer bannerBearer = (LOTRBannerBearer)entity;
			return new ItemStack(LOTRMod.banner, 1, LOTRItemBanner.getSubtypeForFaction(bannerBearer.getFaction()));
		}
		if (entity instanceof LOTREntityNPC && ((LOTREntityNPC)entity).isTrader())
		{
			return new ItemStack(LOTRMod.silverCoin);
		}
		return null;
	}
}
